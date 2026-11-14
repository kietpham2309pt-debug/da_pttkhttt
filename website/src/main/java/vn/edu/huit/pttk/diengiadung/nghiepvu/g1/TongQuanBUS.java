package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.DonHangTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.KhoTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.NhanVienTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.SanPhamTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan;

@Service
public class TongQuanBUS {

    private final TaiKhoanBUS taiKhoanBUS;
    private final DonHangTruyVanG12 donHangTruyVan;
    private final KhoTruyVanG12 khoTruyVan;
    private final NhanVienTruyVanG12 nhanVienTruyVan;
    private final SanPhamTruyVanG12 sanPhamTruyVan;
    private final BaoCaoBUS baoCaoBUS;

    public TongQuanBUS(TaiKhoanBUS taiKhoanBUS, DonHangTruyVanG12 donHangTruyVan, KhoTruyVanG12 khoTruyVan,
                       NhanVienTruyVanG12 nhanVienTruyVan, SanPhamTruyVanG12 sanPhamTruyVan, BaoCaoBUS baoCaoBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.donHangTruyVan = donHangTruyVan;
        this.khoTruyVan = khoTruyVan;
        this.nhanVienTruyVan = nhanVienTruyVan;
        this.sanPhamTruyVan = sanPhamTruyVan;
        this.baoCaoBUS = baoCaoBUS;
    }

    @Transactional(readOnly = true)
    public List<OSo> oSoTheoVai() {
        boolean banHang = taiKhoanBUS.coVai("NV_BAN_HANG");
        boolean quanLy = taiKhoanBUS.coVai("QUAN_LY");
        boolean giaoHang = taiKhoanBUS.coVai("NV_GIAO_HANG");
        boolean keToan = taiKhoanBUS.coVai("KE_TOAN");
        boolean thuKho = taiKhoanBUS.coVai("THU_KHO");
        boolean quanTri = taiKhoanBUS.coVai("QUAN_TRI");
        List<OSo> ds = new ArrayList<>();
        if (banHang || quanLy) {
            long n = donHangTruyVan.countByTrangThai(TrangThaiDonHang.CHO_XAC_NHAN);
            ds.add(new OSo("Đơn chờ xác nhận", n, false, "/quan-tri/don-hang?trangThai=CHO_XAC_NHAN", n > 0));
        }
        if (banHang || quanLy || giaoHang) {
            long n = donHangTruyVan.countByTrangThai(TrangThaiDonHang.DANG_GIAO);
            ds.add(new OSo("Đơn đang giao", n, false, "/quan-tri/giao-hang", false));
        }
        if (quanLy || keToan) {
            ds.add(new OSo("Doanh thu tháng này", baoCaoBUS.doanhThuThangNay(), true, "/quan-tri/bao-cao/doanh-thu", false));
        }
        if (thuKho || quanLy || keToan) {
            long n = baoCaoBUS.mauSapHet().size();
            String lk = (thuKho || quanLy) ? "/quan-tri/kho/ton-kho" : "/quan-tri/bao-cao/ton-kho";
            ds.add(new OSo("Mẫu sắp hết (≤ " + BaoCaoBUS.MUC_TON_TOI_THIEU + " máy)", n, false, lk, n > 0));
        }
        if (quanLy) {
            long n = sanPhamTruyVan.countByTrangThai(TrangThaiSanPham.CHUA_MO_BAN);
            ds.add(new OSo("Mẫu chờ đặt giá", n, false, "/quan-tri/san-pham?trangThai=CHUA_MO_BAN", n > 0));
        }
        if (banHang || quanLy || keToan) {
            long n = khoTruyVan.demDoiTraChoDuyet();
            ds.add(new OSo("Đổi trả chờ duyệt", n, false, "/quan-tri/doi-tra", n > 0));
        }
        if (banHang || thuKho) {
            long n = khoTruyVan.demPhieuBaoHanhDangMo();
            ds.add(new OSo("Phiếu bảo hành đang mở", n, false, "/quan-tri/bao-hanh", false));
        }
        if (quanTri) {
            long dangDung = nhanVienTruyVan.layTatCaKemTaiKhoan().stream()
                    .filter(nv -> nv.getTaiKhoan().getTrangThai() == TrangThaiTaiKhoan.DANG_DUNG).count();
            ds.add(new OSo("Tài khoản nhân viên đang dùng", dangDung, false, "/quan-tri/tai-khoan", false));
        }
        return ds;
    }

    public record OSo(String nhan, long giaTri, boolean laTien, String lienKet, boolean nhac) {
    }
}
