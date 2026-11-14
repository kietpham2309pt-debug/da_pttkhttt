package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;

public class TheSanPham {

    private final SanPham sanPham;
    private final long giamMoiChiec;
    private final KhuyenMai khuyenMai;
    private final int tonKhaDung;

    public TheSanPham(SanPham sanPham, long giamMoiChiec, KhuyenMai khuyenMai, int tonKhaDung) {
        this.sanPham = sanPham;
        this.giamMoiChiec = giamMoiChiec;
        this.khuyenMai = khuyenMai;
        this.tonKhaDung = tonKhaDung;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public Long getMa() {
        return sanPham.getMaSanPham();
    }

    public String getTen() {
        return sanPham.getTenSanPham();
    }

    public String getHieu() {
        return sanPham.getThuongHieu() == null ? "" : sanPham.getThuongHieu().getTenThuongHieu();
    }

    public String getAnh() {
        return GiaBUS.anhCua(sanPham);
    }

    public long getGiamMoiChiec() {
        return giamMoiChiec;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public Long getGiaBan() {
        return sanPham.getGiaBan();
    }

    public Long getGiaHienTai() {
        return sanPham.getGiaBan() == null ? null : sanPham.getGiaBan() - giamMoiChiec;
    }

    public Long getGiaGach() {
        Long hien = getGiaHienTai();
        if (hien == null) {
            return null;
        }
        Long niemYet = sanPham.getGiaNiemYet();
        if (niemYet != null && niemYet > hien) {
            return niemYet;
        }
        return giamMoiChiec > 0 ? sanPham.getGiaBan() : null;
    }

    public int getPhanTramGiam() {
        Long gach = getGiaGach();
        Long hien = getGiaHienTai();
        if (gach == null || hien == null || gach == 0) {
            return 0;
        }
        return (int) Math.round((gach - hien) * 100.0 / gach);
    }

    public String thongSo(String ten) {
        return SanPhamKhachBUS.giaTriThongSo(this, ten);
    }

    public int getTonKhaDung() {
        return tonKhaDung;
    }

    public boolean isConHang() {
        return tonKhaDung > 0;
    }
}
