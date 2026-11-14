package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.util.List;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;

public class ThongTinDon {

    private final DonHang don;
    private final List<LichSuTrangThaiDon> lichSu;
    private final List<GiaoDichThanhToan> giaoDich;
    private final PhieuGiaoHang phieuGiao;
    private final long daTra;
    private final long daHoan;

    public ThongTinDon(DonHang don, List<LichSuTrangThaiDon> lichSu, List<GiaoDichThanhToan> giaoDich, PhieuGiaoHang phieuGiao,
                       long daTra, long daHoan) {
        this.don = don;
        this.lichSu = lichSu;
        this.giaoDich = giaoDich;
        this.phieuGiao = phieuGiao;
        this.daTra = daTra;
        this.daHoan = daHoan;
    }

    public DonHang getDon() {
        return don;
    }

    public List<LichSuTrangThaiDon> getLichSu() {
        return lichSu;
    }

    public List<GiaoDichThanhToan> getGiaoDich() {
        return giaoDich;
    }

    public PhieuGiaoHang getPhieuGiao() {
        return phieuGiao;
    }

    public long getDaTra() {
        return daTra;
    }

    public long getDaHoan() {
        return daHoan;
    }

    public long getConLai() {
        return Math.max(0, don.getTongTien() - daTra);
    }

    public String getDiaChiChe() {
        return DinhDangTien.cheDiaChi(don.getDiaChiGiao());
    }

    public boolean isCoTheThanhToan() {
        return DonHangKhachBUS.coTheThanhToan(don, daTra);
    }

    public boolean isPhaiCoc() {
        return DonHangKhachBUS.dangChoCoc(don, daTra);
    }

    public boolean isCoTheHuy() {
        return DonHangKhachBUS.coTheHuy(don);
    }
}
