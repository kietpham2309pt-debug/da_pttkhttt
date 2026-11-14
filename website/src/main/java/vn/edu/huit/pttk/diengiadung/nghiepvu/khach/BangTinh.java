package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.util.List;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;

public class BangTinh {

    private final List<DongGio> dong;
    private final boolean noiThanh;
    private final KhuyenMai maGiam;
    private final long giamMa;
    private final String loiMa;

    public BangTinh(List<DongGio> dong, boolean noiThanh, KhuyenMai maGiam, long giamMa, String loiMa) {
        this.dong = dong;
        this.noiThanh = noiThanh;
        this.maGiam = maGiam;
        this.giamMa = giamMa;
        this.loiMa = loiMa;
    }

    public List<DongGio> getDong() {
        return dong;
    }

    public boolean isNoiThanh() {
        return noiThanh;
    }

    public KhuyenMai getMaGiam() {
        return maGiam;
    }

    public String getLoiMa() {
        return loiMa;
    }

    public int getSoMon() {
        return dong.stream().mapToInt(DongGio::getSoLuong).sum();
    }

    public long getTienHang() {
        return dong.stream().mapToLong(DongGio::getThanhTien).sum();
    }

    public long getGiamSanPham() {
        return dong.stream().mapToLong(DongGio::getGiam).sum();
    }

    public long getGiamMa() {
        return giamMa;
    }

    public long getTienGiam() {
        return getGiamSanPham() + giamMa;
    }

    public long getSauGiam() {
        return getTienHang() - getTienGiam();
    }

    public boolean isCoLapDat() {
        return noiThanh && dong.stream().anyMatch(DongGio::isLapDat);
    }

    public boolean isCoMonCanLap() {
        return dong.stream().anyMatch(DongGio::isCanLap);
    }

    public long getPhiGiao() {
        return 0;
    }

    public long getPhiLapDat() {
        return GiaBUS.phiLapDat(isCoLapDat(), getSauGiam());
    }

    public long getTongTien() {
        return getSauGiam() + getPhiGiao() + getPhiLapDat();
    }

    public long getTienCoc() {
        return GiaBUS.tienCoc(getTongTien());
    }

    public boolean isCoLoi() {
        return dong.stream().anyMatch(d -> d.getLoi() != null);
    }

    public boolean isTrong() {
        return dong.isEmpty();
    }
}
