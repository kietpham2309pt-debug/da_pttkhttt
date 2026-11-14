package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.DonHangTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuGiaoTruyVanG4;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao;

@Service
public class TienThuBUS {

    public static final int SO_NGAY_DA_XAC_NHAN = 7;

    public record Nhom(String khoa, LocalDate ngay, String nguoiNop, List<PhieuGiaoHang> phieu, long tong) {
    }

    private final PhieuGiaoTruyVanG4 phieuDAO;
    private final DonHangTruyVanG4 donDAO;
    private final TaiKhoanBUS taiKhoanBUS;

    public TienThuBUS(PhieuGiaoTruyVanG4 phieuDAO, DonHangTruyVanG4 donDAO, TaiKhoanBUS taiKhoanBUS) {
        this.phieuDAO = phieuDAO;
        this.donDAO = donDAO;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    private static String nguoiNop(PhieuGiaoHang p) {
        return p.getNhanVienGiao() != null ? p.getNhanVienGiao().getHoTen() : "Đơn vị vận chuyển: " + p.getDonViVanChuyen();
    }

    @Transactional(readOnly = true)
    public List<Nhom> chuaXacNhan() {
        Map<String, List<PhieuGiaoHang>> nhom = new LinkedHashMap<>();
        for (PhieuGiaoHang p : phieuDAO.tienChuaXacNhan()) {
            nhom.computeIfAbsent(p.getNgayHen() + "|" + nguoiNop(p), k -> new ArrayList<>()).add(p);
        }
        List<Nhom> kq = new ArrayList<>();
        for (Map.Entry<String, List<PhieuGiaoHang>> e : nhom.entrySet()) {
            PhieuGiaoHang dau = e.getValue().get(0);
            long tong = e.getValue().stream().mapToLong(PhieuGiaoHang::getTienThuKhiGiao).sum();
            kq.add(new Nhom(e.getKey(), dau.getNgayHen(), nguoiNop(dau), e.getValue(), tong));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> daXacNhanGanDay() {
        return phieuDAO.tienDaXacNhan(LocalDateTime.now().minusDays(SO_NGAY_DA_XAC_NHAN));
    }

    @Transactional
    public String xacNhan(List<Long> maPhieu, Long soNop, String ghiChu) {
        NhanVien keToan = taiKhoanBUS.batBuocNhanVien();
        if (maPhieu == null || maPhieu.isEmpty()) {
            throw new LoiNghiepVu("Chọn ít nhất một phiếu giao để xác nhận");
        }
        List<PhieuGiaoHang> ds = new ArrayList<>();
        long tong = 0;
        for (Long ma : maPhieu) {
            PhieuGiaoHang p = phieuDAO.layDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy phiếu giao PG" + ma));
            if (p.getKeToanXacNhan() != null) {
                throw new LoiNghiepVu("Phiếu PG" + ma + " đã được " + p.getKeToanXacNhan().getHoTen() + " xác nhận lúc "
                        + p.getThoiDiemXacNhanTien().format(G4Chung.GIO_NGAY) + ", không ghi đè");
            }
            if (p.getTrangThai() != TrangThaiPhieuGiao.DA_GIAO || p.getTienThuKhiGiao() == null || p.getTienThuKhiGiao() <= 0) {
                throw new LoiNghiepVu("Phiếu PG" + ma + " không có tiền thu khi giao cần xác nhận");
            }
            ds.add(p);
            tong += p.getTienThuKhiGiao();
        }
        long nop = soNop == null ? tong : soNop;
        if (nop < 0) {
            throw new LoiNghiepVu("Số tiền nộp không được âm");
        }
        long lech = nop - tong;
        String gc = G4Chung.rong(ghiChu);
        if (lech != 0 && gc == null) {
            throw new LoiNghiepVu("Số nộp " + G4Chung.tien(nop) + " lệch " + (lech > 0 ? "thừa " : "thiếu ") + G4Chung.tien(Math.abs(lech))
                    + " so với tổng " + G4Chung.tien(tong) + ": ghi chú khoản lệch trước khi xác nhận");
        }
        LocalDateTime bayGio = LocalDateTime.now();
        for (PhieuGiaoHang p : ds) {
            p.setKeToanXacNhan(keToan);
            p.setThoiDiemXacNhanTien(bayGio);
            phieuDAO.save(p);
        }
        if (lech != 0) {
            StringBuilder ma = new StringBuilder();
            ds.forEach(p -> ma.append(ma.length() == 0 ? "" : ", ").append("PG").append(p.getMaPhieuGiao()));
            String noiDung = "Kế toán " + keToan.getHoTen() + " nhận " + G4Chung.tien(nop) + ", lệch " + (lech > 0 ? "thừa " : "thiếu ")
                    + G4Chung.tien(Math.abs(lech)) + " so với " + G4Chung.tien(tong) + " (" + ma + "): " + gc;
            for (PhieuGiaoHang p : ds) {
                DonHang d = p.getDonHang();
                G4Chung.noiGhiChu(d, noiDung);
                donDAO.save(d);
            }
            return "Đã xác nhận " + ds.size() + " phiếu, ghi khoản lệch " + G4Chung.tien(Math.abs(lech)) + " vào ghi chú đơn. Báo quản lý cửa hàng.";
        }
        return "Đã xác nhận nhận đủ " + G4Chung.tien(tong) + " của " + ds.size() + " phiếu giao.";
    }
}
