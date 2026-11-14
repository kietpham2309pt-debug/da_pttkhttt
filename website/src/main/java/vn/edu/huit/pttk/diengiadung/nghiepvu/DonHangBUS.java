package vn.edu.huit.pttk.diengiadung.nghiepvu;

import static vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang.*;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DonHangDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.LichSuTrangThaiDonDAO;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

@Service
public class DonHangBUS {

    private static final Map<TrangThaiDonHang, Set<TrangThaiDonHang>> DUOC_DOI = new EnumMap<>(TrangThaiDonHang.class);

    static {
        DUOC_DOI.put(CHO_XAC_NHAN, EnumSet.of(DA_XAC_NHAN, DA_HUY));
        DUOC_DOI.put(DA_XAC_NHAN, EnumSet.of(DANG_GIAO, DA_HUY));
        DUOC_DOI.put(DANG_GIAO, EnumSet.of(HOAN_THANH, GIAO_KHONG_THANH_CONG));
        DUOC_DOI.put(HOAN_THANH, EnumSet.of(DA_TRA_HANG));
        DUOC_DOI.put(GIAO_KHONG_THANH_CONG, EnumSet.noneOf(TrangThaiDonHang.class));
        DUOC_DOI.put(DA_HUY, EnumSet.noneOf(TrangThaiDonHang.class));
        DUOC_DOI.put(DA_TRA_HANG, EnumSet.noneOf(TrangThaiDonHang.class));
    }

    private final DonHangDAO donHangDAO;
    private final LichSuTrangThaiDonDAO lichSuDAO;

    public DonHangBUS(DonHangDAO donHangDAO, LichSuTrangThaiDonDAO lichSuDAO) {
        this.donHangDAO = donHangDAO;
        this.lichSuDAO = lichSuDAO;
    }

    public static boolean duocDoi(TrangThaiDonHang tu, TrangThaiDonHang sang) {
        return DUOC_DOI.getOrDefault(tu, Set.of()).contains(sang);
    }

    @Transactional
    public void ghiTaoMoi(DonHang don, NhanVien nguoiLam) {
        ghiLichSu(don, null, don.getTrangThai(), nguoiLam, "Tạo đơn");
    }

    @Transactional
    public void doiTrangThai(DonHang don, TrangThaiDonHang moi, NhanVien nguoiLam, String ghiChu) {
        TrangThaiDonHang cu = don.getTrangThai();
        if (!duocDoi(cu, moi)) {
            throw new LoiNghiepVu("Không thể chuyển đơn từ \"" + cu.getNhan() + "\" sang \"" + moi.getNhan() + "\"");
        }
        don.setTrangThai(moi);
        if (moi == HOAN_THANH) {
            don.setNgayHoanThanh(LocalDateTime.now());
        }
        donHangDAO.save(don);
        ghiLichSu(don, cu, moi, nguoiLam, ghiChu);
    }

    private void ghiLichSu(DonHang don, TrangThaiDonHang cu, TrangThaiDonHang moi, NhanVien nguoiLam, String ghiChu) {
        LichSuTrangThaiDon ls = new LichSuTrangThaiDon();
        ls.setDonHang(don);
        ls.setTrangThaiCu(cu);
        ls.setTrangThaiMoi(moi);
        ls.setThoiDiem(LocalDateTime.now());
        ls.setNhanVien(nguoiLam);
        ls.setGhiChu(ghiChu);
        lichSuDAO.save(ls);
    }

    @Transactional(readOnly = true)
    public List<LichSuTrangThaiDon> lichSu(DonHang don) {
        return lichSuDAO.findByDonHangOrderByThoiDiemAsc(don);
    }
}
