package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.LichSuTrangThaiDonDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.DonHangTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.PhieuXuatTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SerialTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.YeuCauDoiTraTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.SerialBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class NhanLaiBUS {

    public static final String CON_TOT = "TOT";
    public static final String BI_LOI = "LOI";

    public record MayCanNhan(SanPhamSerial may, DonHang don, Long maPhieuXuat, Long maYeuCau) {
    }

    private final PhieuXuatTruyVanG5 phieuXuatDAO;
    private final SerialTruyVanG5 serialDAO;
    private final DonHangTruyVanG5 donHangDAO;
    private final YeuCauDoiTraTruyVanG5 yeuCauDAO;
    private final LichSuTrangThaiDonDAO lichSuDAO;
    private final SerialBUS serialBUS;

    public NhanLaiBUS(PhieuXuatTruyVanG5 phieuXuatDAO, SerialTruyVanG5 serialDAO, DonHangTruyVanG5 donHangDAO,
                      YeuCauDoiTraTruyVanG5 yeuCauDAO, LichSuTrangThaiDonDAO lichSuDAO, SerialBUS serialBUS) {
        this.phieuXuatDAO = phieuXuatDAO;
        this.serialDAO = serialDAO;
        this.donHangDAO = donHangDAO;
        this.yeuCauDAO = yeuCauDAO;
        this.lichSuDAO = lichSuDAO;
        this.serialBUS = serialBUS;
    }

    @Transactional(readOnly = true)
    public List<MayCanNhan> mayGiaoKhongThanh() {
        List<MayCanNhan> kq = new ArrayList<>();
        for (Object[] r : phieuXuatDAO.serialCuaDonTheoTrangThai(TrangThaiSerial.DA_XUAT, TrangThaiDonHang.GIAO_KHONG_THANH_CONG)) {
            SanPhamSerial s = serialDAO.dayDu((String) r[0]).orElseThrow();
            DonHang d = donHangDAO.findById((Long) r[1]).orElseThrow();
            kq.add(new MayCanNhan(s, d, (Long) r[2], null));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<MayCanNhan> mayTraVe() {
        List<MayCanNhan> kq = new ArrayList<>();
        for (SanPhamSerial s : serialDAO.theoTrangThai(TrangThaiSerial.TRA_VE_KHO)) {
            List<Long> yc = yeuCauDAO.yeuCauCuaSerial(s.getSoSerial());
            kq.add(new MayCanNhan(s, s.getDonHangBan(), null, yc.isEmpty() ? null : yc.get(0)));
        }
        return kq;
    }

    @Transactional(rollbackFor = Exception.class)
    public String nhanVe(String soSerial, String ketQua, String tinhTrang, NhanVien thuKho) {
        SanPhamSerial s = serialDAO.dayDu(soSerial == null ? "" : soSerial.trim())
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy máy có serial " + soSerial + "."));
        String tt = tinhTrang == null ? "" : tinhTrang.trim();
        if (tt.isEmpty()) {
            throw new LoiNghiepVu("Phải ghi tình trạng máy khi nhận lại về kho.");
        }
        if (tt.length() > 150) {
            throw new LoiNghiepVu("Tình trạng máy dài quá 150 ký tự.");
        }
        DonHang don;
        TrangThaiSerial moi;
        if (s.getTrangThai() == TrangThaiSerial.DA_XUAT) {
            don = mayGiaoKhongThanh().stream().filter(m -> m.may().getSoSerial().equals(s.getSoSerial()))
                    .map(MayCanNhan::don).findFirst()
                    .orElseThrow(() -> new LoiNghiepVu("Máy " + s.getSoSerial() + " không thuộc đơn giao không thành công nào."));
            don = donHangDAO.findById(don.getMaDonHang()).orElseThrow();
            moi = TrangThaiSerial.TRONG_KHO;
            s.setDonHangBan(null);
            s.setNgayBan(null);
        } else if (s.getTrangThai() == TrangThaiSerial.TRA_VE_KHO) {
            don = s.getDonHangBan();
            if (CON_TOT.equals(ketQua)) {
                moi = TrangThaiSerial.TRONG_KHO;
                s.setDonHangBan(null);
                s.setNgayBan(null);
            } else if (BI_LOI.equals(ketQua)) {
                moi = TrangThaiSerial.LOI_CHO_GUI_HANG;
            } else {
                throw new LoiNghiepVu("Chọn kết quả kiểm tra: máy còn tốt hay máy lỗi.");
            }
        } else {
            throw new LoiNghiepVu("Máy " + s.getSoSerial() + " đang \"" + s.getTrangThai().getNhan() + "\", không phải máy chờ nhận lại.");
        }
        serialBUS.doiTrangThai(s, moi);
        if (don != null) {
            LichSuTrangThaiDon ls = new LichSuTrangThaiDon();
            ls.setDonHang(don);
            ls.setTrangThaiCu(don.getTrangThai());
            ls.setTrangThaiMoi(don.getTrangThai());
            ls.setThoiDiem(LocalDateTime.now());
            ls.setNhanVien(thuKho);
            ls.setGhiChu(DonDatBUS.cat("Kho nhận lại máy " + s.getSoSerial() + " (" + moi.getNhan() + "), tình trạng: " + tt, 255));
            lichSuDAO.save(ls);
        }
        return "Đã nhận máy " + s.getSoSerial() + " về kho: " + moi.getNhan() + ". Tình trạng: " + tt + ".";
    }
}
