package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.PhieuBaoHanhTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SerialTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.SerialBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class BaoHanhBUS {

    public static final String GUI_HANG = "GUI_HANG";
    public static final String LAP_DAT = "LAP_DAT";
    public static final String TAI_NHA = "TAI_NHA";
    public static final String NHAN_LAP_DAT = "[Lỗi do lắp đặt] ";
    public static final String NHAN_TAI_NHA = "[Bảo hành tại nhà] ";
    public static final String NHAN_CO_PHI = "[Sửa có phí] ";
    public static final int SO_LAN_TOI_DA = 3;

    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public record MayTraCuu(SanPhamSerial may, DonHang don, LocalDate ngayBan, LocalDate hanGoc, long soNgayOHang,
                            LocalDate hanBaoHanh, List<PhieuBaoHanh> lichSu) {

        public boolean conHan() {
            return hanBaoHanh != null && !LocalDate.now().isAfter(hanBaoHanh);
        }

        public int soLanDaBaoHanh() {
            return lichSu.size();
        }

        public int lanTiepTheo() {
            return lichSu.size() + 1;
        }

        public PhieuBaoHanh phieuDangMo() {
            return lichSu.stream().filter(p -> p.getTrangThai() != TrangThaiBaoHanh.DA_TRA_KHACH).findFirst().orElse(null);
        }

        public boolean quaSoLan() {
            return lichSu.size() >= SO_LAN_TOI_DA;
        }

        public boolean lanThuBa() {
            return lichSu.size() == SO_LAN_TOI_DA - 1;
        }

        public boolean duocTiepNhan() {
            return may.getTrangThai() == TrangThaiSerial.DA_BAN && !quaSoLan();
        }

        public long ngayOHang(PhieuBaoHanh p) {
            return BaoHanhBUS.soNgayOHang(p);
        }
    }

    public record PhieuMo(PhieuBaoHanh phieu, String loai, boolean quaHan, long soNgayOHang) {

        public String nhanLoai() {
            return BaoHanhBUS.nhanLoai(loai);
        }
    }

    private final PhieuBaoHanhTruyVanG5 phieuDAO;
    private final SerialTruyVanG5 serialDAO;
    private final SerialBUS serialBUS;

    public BaoHanhBUS(PhieuBaoHanhTruyVanG5 phieuDAO, SerialTruyVanG5 serialDAO, SerialBUS serialBUS) {
        this.phieuDAO = phieuDAO;
        this.serialDAO = serialDAO;
        this.serialBUS = serialBUS;
    }

    public static String loaiXuLy(PhieuBaoHanh p) {
        String m = p.getMoTaLoi() == null ? "" : p.getMoTaLoi();
        if (m.startsWith(NHAN_LAP_DAT)) {
            return LAP_DAT;
        }
        if (m.startsWith(NHAN_TAI_NHA)) {
            return TAI_NHA;
        }
        return GUI_HANG;
    }

    public static String nhanLoai(String loai) {
        return switch (loai) {
            case LAP_DAT -> "Lỗi do lắp đặt";
            case TAI_NHA -> "Bảo hành tại nhà";
            default -> "Gửi hãng";
        };
    }

    public static long soNgayOHang(List<PhieuBaoHanh> ds) {
        long n = 0;
        for (PhieuBaoHanh p : ds) {
            n += soNgayOHang(p);
        }
        return n;
    }

    public static long soNgayOHang(PhieuBaoHanh p) {
        if (p.getNgayGuiHang() == null) {
            return 0;
        }
        LocalDateTime den = p.getNgayNhanTuHang() != null ? p.getNgayNhanTuHang() : LocalDateTime.now();
        return Math.max(0, ChronoUnit.DAYS.between(p.getNgayGuiHang().toLocalDate(), den.toLocalDate()));
    }

    @Transactional(readOnly = true)
    public List<MayTraCuu> traCuu(String loai, String tuKhoa) {
        String k = tuKhoa == null ? "" : tuKhoa.trim();
        if (k.isEmpty()) {
            throw new LoiNghiepVu("Nhập số serial, số điện thoại người mua hoặc mã đơn để tra.");
        }
        List<SanPhamSerial> ds;
        if ("sdt".equals(loai)) {
            ds = serialDAO.theoSoDienThoai(k.replaceAll("[\\s.]", ""));
        } else if ("don".equals(loai)) {
            String so = k.toUpperCase().replaceFirst("^#", "").replaceFirst("^DH", "");
            try {
                ds = serialDAO.theoDonHang(Long.parseLong(so));
            } catch (NumberFormatException e) {
                throw new LoiNghiepVu("Mã đơn \"" + k + "\" không hợp lệ (ví dụ DH000001).");
            }
        } else {
            ds = serialDAO.dayDu(k.toUpperCase()).map(List::of).orElse(List.of());
        }
        List<MayTraCuu> kq = new ArrayList<>();
        for (SanPhamSerial s : ds) {
            if (s.getNgayBan() == null || s.getDonHangBan() == null) {
                continue;
            }
            kq.add(dung(s));
        }
        if (kq.isEmpty()) {
            throw new LoiNghiepVu("Không tìm thấy máy do cửa hàng bán. Hướng dẫn khách tới nơi bán hoặc trung tâm bảo hành của hãng.");
        }
        return kq;
    }

    private MayTraCuu dung(SanPhamSerial s) {
        List<PhieuBaoHanh> lichSu = phieuDAO.theoSerial(s.getSoSerial());
        LocalDate ngayBan = s.getNgayBan().toLocalDate();
        int thang = s.getSanPham().getThoiHanBaoHanh() == null ? 0 : s.getSanPham().getThoiHanBaoHanh();
        LocalDate hanGoc = ngayBan.plusMonths(thang);
        long oHang = soNgayOHang(lichSu);
        return new MayTraCuu(s, s.getDonHangBan(), ngayBan, hanGoc, oHang, hanGoc.plusDays(oHang), lichSu);
    }

    @Transactional(readOnly = true)
    public MayTraCuu mot(String soSerial) {
        List<MayTraCuu> ds = traCuu("serial", soSerial);
        return ds.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public PhieuBaoHanh tiepNhan(String soSerial, String hoTen, String soDienThoai, String moTaLoi, String tinhTrang,
                                 String ngayHenTra, String loai, boolean dongYCoPhi, NhanVien nhanVien) {
        MayTraCuu m = mot(soSerial);
        PhieuBaoHanh dangMo = m.phieuDangMo();
        if (dangMo != null) {
            throw new LoiNghiepVu("Máy đang bảo hành theo phiếu #" + dangMo.getMaPhieu() + ", không tạo phiếu mới.");
        }
        if (m.may().getTrangThai() != TrangThaiSerial.DA_BAN) {
            throw new LoiNghiepVu("Máy " + m.may().getSoSerial() + " đang \"" + m.may().getTrangThai().getNhan()
                    + "\", chỉ tiếp nhận bảo hành máy ở trạng thái Đã bán.");
        }
        if (m.quaSoLan()) {
            throw new LoiNghiepVu("Máy đã bảo hành " + m.soLanDaBaoHanh()
                    + " lần mà vẫn lỗi: không gửi hãng nữa, chuyển sang đổi máy mới hoặc hoàn tiền (Xử lý đổi trả).");
        }
        if (!m.conHan() && !dongYCoPhi) {
            throw new LoiNghiepVu("Hết hạn bảo hành từ ngày " + m.hanBaoHanh().plusDays(1).format(NGAY)
                    + ". Chỉ nhận sửa có phí theo báo giá của trung tâm bảo hành hãng khi khách đồng ý.");
        }
        String ten = hoTen == null ? "" : hoTen.trim();
        String sdt = soDienThoai == null ? "" : soDienThoai.replaceAll("[\\s.]", "");
        String loi = moTaLoi == null ? "" : moTaLoi.trim();
        String tt = tinhTrang == null ? "" : tinhTrang.trim();
        if (ten.isEmpty() || ten.length() > 100) {
            throw new LoiNghiepVu("Họ tên khách bắt buộc, tối đa 100 ký tự.");
        }
        if (!sdt.matches("0\\d{9,10}")) {
            throw new LoiNghiepVu("Số điện thoại khách phải gồm 10–11 chữ số, bắt đầu bằng 0.");
        }
        if (loi.isEmpty() || loi.length() > 400) {
            throw new LoiNghiepVu("Mô tả lỗi theo lời khách bắt buộc, tối đa 400 ký tự.");
        }
        if (tt.isEmpty() || tt.length() > 255) {
            throw new LoiNghiepVu("Tình trạng bên ngoài (trầy, móp, thiếu phụ kiện…) bắt buộc, tối đa 255 ký tự.");
        }
        LocalDate hen;
        try {
            hen = LocalDate.parse(ngayHenTra == null ? "" : ngayHenTra.trim());
        } catch (RuntimeException e) {
            throw new LoiNghiepVu("Chưa chọn ngày hẹn trả.");
        }
        if (hen.isBefore(LocalDate.now())) {
            throw new LoiNghiepVu("Ngày hẹn trả không được trước hôm nay.");
        }
        String l = LAP_DAT.equals(loai) || TAI_NHA.equals(loai) ? loai : GUI_HANG;
        String tienTo = (LAP_DAT.equals(l) ? NHAN_LAP_DAT : TAI_NHA.equals(l) ? NHAN_TAI_NHA : "")
                + (!m.conHan() ? NHAN_CO_PHI : "");
        PhieuBaoHanh p = new PhieuBaoHanh();
        p.setSerial(m.may());
        p.setHoTenKhach(ten);
        p.setSoDienThoaiKhach(sdt);
        p.setNgayNhan(LocalDateTime.now());
        p.setMoTaLoi(tienTo + loi);
        p.setTinhTrangBenNgoai(tt);
        p.setNgayHenTra(hen);
        p.setLanThu(m.lanTiepTheo());
        p.setTrangThai(TrangThaiBaoHanh.DA_TIEP_NHAN);
        p.setNhanVien(nhanVien);
        phieuDAO.save(p);
        serialBUS.doiTrangThai(m.may(), TrangThaiSerial.DANG_BAO_HANH);
        return p;
    }

    @Transactional(readOnly = true)
    public PhieuBaoHanh phieu(Long ma) {
        return phieuDAO.chiTiet(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy phiếu bảo hành #" + ma));
    }

    @Transactional(readOnly = true)
    public List<PhieuMo> dangMo() {
        LocalDate homNay = LocalDate.now();
        List<PhieuMo> kq = new ArrayList<>();
        for (PhieuBaoHanh p : phieuDAO.khacTrangThai(TrangThaiBaoHanh.DA_TRA_KHACH)) {
            kq.add(new PhieuMo(p, loaiXuLy(p), p.getNgayHenTra().isBefore(homNay), soNgayOHang(p)));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<PhieuBaoHanh> daTraGanDay() {
        return phieuDAO.ganDayTheoTrangThai(TrangThaiBaoHanh.DA_TRA_KHACH);
    }

    private PhieuBaoHanh phieuDung(Long ma, TrangThaiBaoHanh canCo) {
        PhieuBaoHanh p = phieu(ma);
        if (p.getTrangThai() != canCo) {
            throw new LoiNghiepVu("Phiếu #" + ma + " đã được cập nhật sang \"" + p.getTrangThai().getNhan()
                    + "\" trước đó, không ghi đè.");
        }
        return p;
    }

    private static LocalDateTime ngayGio(String ngay, LocalDateTime khongTruoc, String ten) {
        LocalDate d;
        if (ngay == null || ngay.isBlank()) {
            d = LocalDate.now();
        } else {
            try {
                d = LocalDate.parse(ngay.trim());
            } catch (RuntimeException e) {
                throw new LoiNghiepVu(ten + " không hợp lệ.");
            }
        }
        if (d.isAfter(LocalDate.now())) {
            throw new LoiNghiepVu(ten + " không được sau hôm nay.");
        }
        if (khongTruoc != null && d.isBefore(khongTruoc.toLocalDate())) {
            throw new LoiNghiepVu(ten + " không được trước ngày " + khongTruoc.toLocalDate().format(NGAY) + ".");
        }
        return d.equals(LocalDate.now()) ? LocalDateTime.now() : d.atTime(LocalTime.of(8, 0));
    }

    @Transactional(rollbackFor = Exception.class)
    public void guiHang(Long ma, String ngay) {
        PhieuBaoHanh p = phieuDung(ma, TrangThaiBaoHanh.DA_TIEP_NHAN);
        if (!GUI_HANG.equals(loaiXuLy(p))) {
            throw new LoiNghiepVu("Phiếu #" + ma + " là \"" + nhanLoai(loaiXuLy(p)) + "\", không đưa vào danh sách gửi hãng.");
        }
        p.setNgayGuiHang(ngayGio(ngay, p.getNgayNhan(), "Ngày gửi hãng"));
        p.setTrangThai(TrangThaiBaoHanh.DA_GUI_HANG);
        phieuDAO.save(p);
    }

    @Transactional(rollbackFor = Exception.class)
    public void nhanVe(Long ma, String ngay, String ketQua, String ghiChu) {
        PhieuBaoHanh p = phieuDung(ma, TrangThaiBaoHanh.DA_GUI_HANG);
        String kq;
        if ("SUA_XONG".equals(ketQua)) {
            kq = "Hãng đã sửa xong";
        } else if ("KHONG_SUA_DUOC".equals(ketQua)) {
            kq = "Hãng không sửa được";
        } else {
            throw new LoiNghiepVu("Chọn kết quả hãng trả về.");
        }
        if (ghiChu != null && !ghiChu.isBlank()) {
            kq += ": " + ghiChu.trim();
        }
        p.setNgayNhanTuHang(ngayGio(ngay, p.getNgayGuiHang(), "Ngày nhận về"));
        p.setKetQua(DonDatBUS.noi(p.getKetQua(), kq));
        p.setTrangThai(TrangThaiBaoHanh.DA_NHAN_VE);
        phieuDAO.save(p);
    }

    @Transactional(rollbackFor = Exception.class)
    public void traKhach(Long ma, String ketQua) {
        PhieuBaoHanh p = phieu(ma);
        String loai = loaiXuLy(p);
        boolean xuLyTaiCho = !GUI_HANG.equals(loai);
        if (xuLyTaiCho) {
            if (p.getTrangThai() != TrangThaiBaoHanh.DA_TIEP_NHAN) {
                throw new LoiNghiepVu("Phiếu #" + ma + " đã được cập nhật sang \"" + p.getTrangThai().getNhan() + "\" trước đó, không ghi đè.");
            }
            if (ketQua == null || ketQua.isBlank()) {
                throw new LoiNghiepVu("Ghi kết quả sửa (" + nhanLoai(loai).toLowerCase() + ") trước khi đóng phiếu.");
            }
        } else if (p.getTrangThai() != TrangThaiBaoHanh.DA_NHAN_VE) {
            throw new LoiNghiepVu("Phiếu #" + ma + " đang \"" + p.getTrangThai().getNhan() + "\": chỉ trả khách khi máy đã nhận về từ hãng.");
        }
        if (ketQua != null && !ketQua.isBlank()) {
            p.setKetQua(DonDatBUS.noi(p.getKetQua(), ketQua.trim()));
        }
        p.setNgayTraKhach(LocalDateTime.now());
        p.setTrangThai(TrangThaiBaoHanh.DA_TRA_KHACH);
        phieuDAO.save(p);
        serialBUS.doiTrangThai(p.getSerial(), TrangThaiSerial.DA_BAN);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doiHen(Long ma, String ngayMoi) {
        PhieuBaoHanh p = phieu(ma);
        if (p.getTrangThai() == TrangThaiBaoHanh.DA_TRA_KHACH) {
            throw new LoiNghiepVu("Phiếu #" + ma + " đã trả khách.");
        }
        LocalDate moi;
        try {
            moi = LocalDate.parse(ngayMoi == null ? "" : ngayMoi.trim());
        } catch (RuntimeException e) {
            throw new LoiNghiepVu("Chưa chọn ngày hẹn mới.");
        }
        if (moi.isBefore(LocalDate.now())) {
            throw new LoiNghiepVu("Ngày hẹn mới không được trước hôm nay.");
        }
        p.setKetQua(DonDatBUS.noi(p.getKetQua(), "Hẹn cũ " + p.getNgayHenTra().format(NGAY) + " đổi sang " + moi.format(NGAY)));
        p.setNgayHenTra(moi);
        phieuDAO.save(p);
    }

    public Optional<String> canhBao(MayTraCuu m) {
        if (m.quaSoLan()) {
            return Optional.of("Máy đã bảo hành " + m.soLanDaBaoHanh() + " lần: không gửi hãng nữa, chuyển sang đổi máy mới hoặc hoàn tiền.");
        }
        if (m.lanThuBa()) {
            return Optional.of("Máy đã bảo hành 2 lần, đây là lần thứ 3: lần này sửa không được thì đổi máy mới hoặc hoàn tiền.");
        }
        return Optional.empty();
    }
}
