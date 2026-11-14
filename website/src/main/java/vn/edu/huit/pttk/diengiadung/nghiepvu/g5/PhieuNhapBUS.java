package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.DonHangTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.PhieuNhapTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SerialTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietPhieuNhap;
import vn.edu.huit.pttk.diengiadung.thucthe.DonDatHangNCC;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuNhap;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonDat;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class PhieuNhapBUS {

    public static final long MUC_GIA_TRI_CAO = 15_000_000L;

    public static class DongNhap {
        private Long maSanPham;
        private String tenSanPham;
        private String model;
        private int soDat;
        private long daNhap;
        private boolean giaTriCao;
        private String chungTu = "";
        private String thucNhap = "";
        private String donGia = "";
        private String serial = "";
        private String donRieng = "";

        public Long getMaSanPham() { return maSanPham; }
        public String getTenSanPham() { return tenSanPham; }
        public String getModel() { return model; }
        public int getSoDat() { return soDat; }
        public long getDaNhap() { return daNhap; }
        public long getConThieu() { return Math.max(0, soDat - daNhap); }
        public boolean isGiaTriCao() { return giaTriCao; }
        public String getChungTu() { return chungTu; }
        public String getThucNhap() { return thucNhap; }
        public String getDonGia() { return donGia; }
        public String getSerial() { return serial; }
        public String getDonRieng() { return donRieng; }
        public void setChungTu(String v) { chungTu = v == null ? "" : v.trim(); }
        public void setThucNhap(String v) { thucNhap = v == null ? "" : v.trim(); }
        public void setDonGia(String v) { donGia = v == null ? "" : v.trim(); }
        public void setSerial(String v) { serial = v == null ? "" : v; }
        public void setDonRieng(String v) { donRieng = v == null ? "" : v.trim(); }
    }

    public static class PhieuNhapNhap {
        private Long maDonDat;
        private String tenNhaCungCap;
        private String soHoaDon = "";
        private String ngayHoaDon = "";
        private String nguoiGiao = "";
        private String tinhTrang = "";
        private final List<DongNhap> dong = new ArrayList<>();

        public Long getMaDonDat() { return maDonDat; }
        public String getTenNhaCungCap() { return tenNhaCungCap; }
        public String getSoHoaDon() { return soHoaDon; }
        public String getNgayHoaDon() { return ngayHoaDon; }
        public String getNguoiGiao() { return nguoiGiao; }
        public String getTinhTrang() { return tinhTrang; }
        public List<DongNhap> getDong() { return dong; }
        public void setSoHoaDon(String v) { soHoaDon = v == null ? "" : v.trim(); }
        public void setNgayHoaDon(String v) { ngayHoaDon = v == null ? "" : v.trim(); }
        public void setNguoiGiao(String v) { nguoiGiao = v == null ? "" : v.trim(); }
        public void setTinhTrang(String v) { tinhTrang = v == null ? "" : v.trim(); }
    }

    public record PhieuNhapDong(PhieuNhap phieu, int chungTu, int thucNhap, long tien) {
    }

    public record DongNhapXem(ChiTietPhieuNhap chiTiet, List<SanPhamSerial> serial) {

        public long thanhTien() {
            return (long) chiTiet.getSoLuongThucNhap() * chiTiet.getDonGiaNhap();
        }
    }

    public record PhieuNhapXem(PhieuNhap phieu, List<DongNhapXem> dong) {

        public long tongTien() {
            return dong.stream().mapToLong(DongNhapXem::thanhTien).sum();
        }
    }

    private final PhieuNhapTruyVanG5 phieuNhapDAO;
    private final SerialTruyVanG5 serialDAO;
    private final DonHangTruyVanG5 donHangDAO;
    private final DonDatBUS donDatBUS;
    private final EntityManager em;

    public PhieuNhapBUS(PhieuNhapTruyVanG5 phieuNhapDAO, SerialTruyVanG5 serialDAO, DonHangTruyVanG5 donHangDAO,
                        DonDatBUS donDatBUS, EntityManager em) {
        this.phieuNhapDAO = phieuNhapDAO;
        this.serialDAO = serialDAO;
        this.donHangDAO = donHangDAO;
        this.donDatBUS = donDatBUS;
        this.em = em;
    }

    @Transactional(readOnly = true)
    public List<PhieuNhapDong> danhSach() {
        List<PhieuNhapDong> kq = new ArrayList<>();
        for (PhieuNhap p : phieuNhapDAO.danhSach()) {
            int ct = 0;
            int tn = 0;
            long tien = 0;
            for (ChiTietPhieuNhap c : p.getChiTiet()) {
                ct += c.getSoLuongChungTu();
                tn += c.getSoLuongThucNhap();
                tien += (long) c.getSoLuongThucNhap() * c.getDonGiaNhap();
            }
            kq.add(new PhieuNhapDong(p, ct, tn, tien));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public PhieuNhapXem xem(Long ma) {
        PhieuNhap p = phieuNhapDAO.chiTiet(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy phiếu nhập #" + ma));
        Map<Long, List<SanPhamSerial>> theoDong = new HashMap<>();
        for (SanPhamSerial s : serialDAO.theoPhieuNhap(ma)) {
            theoDong.computeIfAbsent(s.getChiTietNhap().getMaChiTietNhap(), k -> new ArrayList<>()).add(s);
        }
        List<DongNhapXem> dong = new ArrayList<>();
        for (ChiTietPhieuNhap c : p.getChiTiet()) {
            dong.add(new DongNhapXem(c, theoDong.getOrDefault(c.getMaChiTietNhap(), List.of())));
        }
        return new PhieuNhapXem(p, dong);
    }

    @Transactional(readOnly = true)
    public PhieuNhapNhap taoNhap(Long maDonDat) {
        DonDatBUS.DonDatXem dx = donDatBUS.xem(maDonDat);
        DonDatHangNCC d = dx.don();
        if (d.getTrangThai() != TrangThaiDonDat.DA_GUI) {
            throw new LoiNghiepVu("Đơn đặt hàng #" + maDonDat + " đang ở trạng thái \"" + d.getTrangThai().getNhan()
                    + "\"; chỉ nhận hàng cho đơn đã gửi nhà cung cấp và chưa nhận đủ.");
        }
        PhieuNhapNhap f = new PhieuNhapNhap();
        f.maDonDat = d.getMaDonDat();
        f.tenNhaCungCap = d.getNhaCungCap().getTenNhaCungCap();
        for (DonDatBUS.DongDat dd : dx.dong()) {
            SanPham sp = em.find(SanPham.class, dd.maSanPham());
            DongNhap r = new DongNhap();
            r.maSanPham = dd.maSanPham();
            r.tenSanPham = dd.tenSanPham();
            r.model = dd.model();
            r.soDat = dd.soLuong();
            r.daNhap = dd.daNhap();
            r.giaTriCao = sp.getGiaBan() != null && sp.getGiaBan() >= MUC_GIA_TRI_CAO;
            if (r.getConThieu() > 0) {
                r.chungTu = String.valueOf(r.getConThieu());
                r.thucNhap = String.valueOf(r.getConThieu());
            }
            List<Long> gia = phieuNhapDAO.donGiaGanNhat(dd.maSanPham());
            if (!gia.isEmpty()) {
                r.donGia = String.valueOf(gia.get(0));
            }
            f.dong.add(r);
        }
        return f;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long luu(PhieuNhapNhap f, NhanVien thuKho) {
        DonDatHangNCC d = em.find(DonDatHangNCC.class, f.maDonDat);
        if (d == null || d.getTrangThai() != TrangThaiDonDat.DA_GUI) {
            throw new LoiNghiepVu("Đơn đặt hàng #" + f.maDonDat + " không còn chờ nhập hàng.");
        }
        if (f.soHoaDon.isEmpty()) {
            throw new LoiNghiepVu("Chưa nhập số hoá đơn của nhà cung cấp.");
        }
        if (f.soHoaDon.length() > 30) {
            throw new LoiNghiepVu("Số hoá đơn dài quá 30 ký tự.");
        }
        LocalDate ngayHd = null;
        if (!f.ngayHoaDon.isEmpty()) {
            try {
                ngayHd = LocalDate.parse(f.ngayHoaDon);
            } catch (DateTimeParseException e) {
                throw new LoiNghiepVu("Ngày hoá đơn không hợp lệ.");
            }
            if (ngayHd.isAfter(LocalDate.now())) {
                throw new LoiNghiepVu("Ngày hoá đơn không được sau hôm nay.");
            }
        }
        if (f.nguoiGiao.length() > 60) {
            throw new LoiNghiepVu("Tên người giao dài quá 60 ký tự.");
        }
        if (f.tinhTrang.length() > 120) {
            throw new LoiNghiepVu("Ô tình trạng hàng dài quá 120 ký tự.");
        }

        Map<DongNhap, int[]> soLieu = new LinkedHashMap<>();
        Map<DongNhap, List<String>> serialTheoDong = new LinkedHashMap<>();
        Map<DongNhap, DonHang> donRiengTheoDong = new HashMap<>();
        Map<Long, Long> daNhap = donDatBUS.daNhapTheoMau(f.maDonDat);
        Set<String> tatCaSerial = new LinkedHashSet<>();
        List<String> chenhLech = new ArrayList<>();
        for (DongNhap r : f.dong) {
            List<String> ds = tachSerial(r.serial);
            if (r.chungTu.isEmpty() && r.thucNhap.isEmpty() && ds.isEmpty()) {
                continue;
            }
            String ten = "Mẫu " + r.model;
            int ct = soNguyen(r.chungTu, ten + ": số lượng theo chứng từ");
            if (ct < 1) {
                throw new LoiNghiepVu(ten + ": số lượng theo chứng từ phải lớn hơn 0.");
            }
            int tn = soNguyen(r.thucNhap, ten + ": số lượng thực nhập");
            if (tn < 0) {
                throw new LoiNghiepVu(ten + ": số lượng thực nhập không được âm.");
            }
            if (tn > ct) {
                throw new LoiNghiepVu(ten + ": Số thực nhập không được lớn hơn số theo chứng từ.");
            }
            long conThieu = Math.max(0, r.soDat - daNhap.getOrDefault(r.maSanPham, 0L));
            if (tn > conThieu) {
                throw new LoiNghiepVu(ten + ": thực nhập " + tn + " vượt số còn phải nhận của đơn đặt hàng (còn " + conThieu
                        + "); hàng không có trong đơn đặt hàng thì không nhận vào kho.");
            }
            long gia = soTien(r.donGia, ten);
            if (ds.size() < tn) {
                throw new LoiNghiepVu(ten + " còn thiếu " + (tn - ds.size()) + " serial (thực nhập " + tn + ", đã ghi " + ds.size() + ").");
            }
            if (ds.size() > tn) {
                throw new LoiNghiepVu(ten + " thừa " + (ds.size() - tn) + " serial (thực nhập " + tn + ", đã ghi " + ds.size() + ").");
            }
            for (String s : ds) {
                if (s.length() > 50) {
                    throw new LoiNghiepVu("Serial " + s + " dài quá 50 ký tự.");
                }
                if (!s.matches("[A-Z0-9._/-]+")) {
                    throw new LoiNghiepVu("Serial " + s + " chỉ được gồm chữ, số và các dấu . _ / -");
                }
                if (!tatCaSerial.add(s)) {
                    throw new LoiNghiepVu("Serial " + s + " đã tồn tại (gõ trùng trên phiếu này).");
                }
            }
            if (!r.donRieng.isEmpty() && tn > 0) {
                donRiengTheoDong.put(r, timDonRieng(r.donRieng, r.maSanPham));
            }
            if (tn < ct) {
                chenhLech.add(r.model + " thiếu/hư " + (ct - tn));
            }
            soLieu.put(r, new int[]{ct, tn});
            serialTheoDong.put(r, ds);
            r.donGia = String.valueOf(gia);
        }
        if (soLieu.isEmpty()) {
            throw new LoiNghiepVu("Phiếu nhập phải có ít nhất một mẫu máy (nhập số lượng theo chứng từ).");
        }
        if (!chenhLech.isEmpty() && f.tinhTrang.isEmpty()) {
            throw new LoiNghiepVu("Số thực nhập ít hơn số theo chứng từ: hãy ghi tình trạng hàng (thiếu, thùng móp, máy hư…) để báo nhà cung cấp giao bù hoặc đổi.");
        }
        if (!tatCaSerial.isEmpty()) {
            for (Object[] r : serialDAO.phieuNhapCuaSerial(tatCaSerial)) {
                throw new LoiNghiepVu("Serial " + r[0] + " đã tồn tại (phiếu nhập #" + r[1] + ").");
            }
        }

        LocalDateTime bayGio = LocalDateTime.now();
        PhieuNhap p = new PhieuNhap();
        p.setNhaCungCap(d.getNhaCungCap());
        p.setDonDat(d);
        p.setSoHoaDonNCC(f.soHoaDon);
        p.setNgayNhap(bayGio);
        p.setThuKho(thuKho);
        List<String> ghiChu = new ArrayList<>();
        if (!f.nguoiGiao.isEmpty()) {
            ghiChu.add("Người giao: " + f.nguoiGiao);
        }
        if (ngayHd != null) {
            ghiChu.add("HĐ ngày " + ngayHd.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if (!chenhLech.isEmpty()) {
            ghiChu.add("Chênh lệch: " + String.join(", ", chenhLech));
        }
        if (!f.tinhTrang.isEmpty()) {
            ghiChu.add("Tình trạng: " + f.tinhTrang);
        }
        p.setGhiChu(ghiChu.isEmpty() ? null : DonDatBUS.cat(String.join("; ", ghiChu), 255));
        Map<DongNhap, ChiTietPhieuNhap> chiTietTheoDong = new LinkedHashMap<>();
        for (Map.Entry<DongNhap, int[]> e : soLieu.entrySet()) {
            DongNhap r = e.getKey();
            ChiTietPhieuNhap c = new ChiTietPhieuNhap();
            c.setPhieuNhap(p);
            c.setSanPham(em.getReference(SanPham.class, r.maSanPham));
            c.setSoLuongChungTu(e.getValue()[0]);
            c.setSoLuongThucNhap(e.getValue()[1]);
            c.setDonGiaNhap(Long.parseLong(r.donGia));
            p.getChiTiet().add(c);
            chiTietTheoDong.put(r, c);
        }
        em.persist(p);
        try {
            em.flush();
            for (Map.Entry<DongNhap, ChiTietPhieuNhap> e : chiTietTheoDong.entrySet()) {
                for (String so : serialTheoDong.get(e.getKey())) {
                    SanPhamSerial s = new SanPhamSerial();
                    s.setSoSerial(so);
                    s.setSanPham(e.getValue().getSanPham());
                    s.setChiTietNhap(e.getValue());
                    s.setNgayNhap(bayGio);
                    s.setTrangThai(TrangThaiSerial.TRONG_KHO);
                    s.setDonHangBan(donRiengTheoDong.get(e.getKey()));
                    em.persist(s);
                }
            }
            em.flush();
        } catch (PersistenceException ex) {
            throw new LoiNghiepVu("Chưa lưu được, thử lại: có serial vừa được người khác nhập vào hệ thống, hãy kiểm tra lại các serial.");
        }
        donDatBUS.capNhatSauNhap(d.getMaDonDat());
        return p.getMaPhieuNhap();
    }

    private DonHang timDonRieng(String ma, Long maSanPham) {
        String so = ma.toUpperCase().replaceFirst("^DH", "").replaceFirst("^0+(?=\\d)", "");
        Long maDon;
        try {
            maDon = Long.parseLong(so);
        } catch (NumberFormatException e) {
            throw new LoiNghiepVu("Mã đơn đặt riêng \"" + ma + "\" không hợp lệ (ví dụ DH000005).");
        }
        DonHang don = donHangDAO.kemChiTiet(maDon).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn " + ma + " để giữ máy."));
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN && don.getTrangThai() != TrangThaiDonHang.DA_XAC_NHAN) {
            throw new LoiNghiepVu("Đơn " + ma + " đang \"" + don.getTrangThai().getNhan() + "\", không giữ máy cho đơn này được.");
        }
        boolean coMau = don.getChiTiet().stream().anyMatch(c -> c.getSanPham().getMaSanPham().equals(maSanPham));
        if (!coMau) {
            throw new LoiNghiepVu("Đơn " + ma + " không có mẫu máy này.");
        }
        return don;
    }

    static List<String> tachSerial(String s) {
        List<String> kq = new ArrayList<>();
        if (s == null) {
            return kq;
        }
        for (String dong : s.split("[\\r\\n]+")) {
            String t = dong.trim().toUpperCase();
            if (!t.isEmpty()) {
                kq.add(t);
            }
        }
        return kq;
    }

    private static int soNguyen(String s, String ten) {
        if (s == null || s.isBlank()) {
            throw new LoiNghiepVu(ten + " chưa nhập.");
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new LoiNghiepVu(ten + " phải là số nguyên.");
        }
    }

    private static long soTien(String s, String ten) {
        String so = s == null ? "" : s.replaceAll("[\\s.,₫đ]", "");
        if (so.isEmpty()) {
            throw new LoiNghiepVu(ten + ": chưa nhập đơn giá nhập.");
        }
        long gia;
        try {
            gia = Long.parseLong(so);
        } catch (NumberFormatException e) {
            throw new LoiNghiepVu(ten + ": đơn giá nhập phải là số tiền (đồng).");
        }
        if (gia <= 0) {
            throw new LoiNghiepVu(ten + ": đơn giá nhập phải lớn hơn 0.");
        }
        return gia;
    }
}
