package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

public class DongGio {

    private final TheSanPham the;
    private final int soLuong;
    private final boolean lapDat;

    public DongGio(TheSanPham the, int soLuong, boolean lapDat) {
        this.the = the;
        this.soLuong = soLuong;
        this.lapDat = lapDat;
    }

    public TheSanPham getThe() {
        return the;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public boolean isLapDat() {
        return lapDat && isCanLap();
    }

    public boolean isCanLap() {
        return Boolean.TRUE.equals(the.getSanPham().getCanLapDat());
    }

    public long getDonGia() {
        return the.getGiaBan() == null ? 0 : the.getGiaBan();
    }

    public long getThanhTien() {
        return getDonGia() * soLuong;
    }

    public long getGiam() {
        return the.getGiamMoiChiec() * soLuong;
    }

    public String getLoi() {
        if (the.getSanPham().getTrangThai() != TrangThaiSanPham.DANG_BAN || the.getGiaBan() == null) {
            return "Đã ngừng bán";
        }
        if (the.getTonKhaDung() <= 0) {
            return "Tạm hết hàng";
        }
        if (soLuong > the.getTonKhaDung()) {
            return "Chỉ còn " + the.getTonKhaDung() + " chiếc";
        }
        return null;
    }
}
