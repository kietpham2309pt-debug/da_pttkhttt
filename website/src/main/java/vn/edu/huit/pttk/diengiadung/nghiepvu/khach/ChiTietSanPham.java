package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.util.List;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhGia;
import vn.edu.huit.pttk.diengiadung.thucthe.ThongSoKyThuat;

public class ChiTietSanPham {

    private final TheSanPham the;
    private final List<String> anh;
    private final List<ThongSoKyThuat> thongSo;
    private final List<DanhGia> danhGia;
    private final List<TheSanPham> cungNhom;

    public ChiTietSanPham(TheSanPham the, List<String> anh, List<ThongSoKyThuat> thongSo, List<DanhGia> danhGia, List<TheSanPham> cungNhom) {
        this.the = the;
        this.anh = anh;
        this.thongSo = thongSo;
        this.danhGia = danhGia;
        this.cungNhom = cungNhom;
    }

    public TheSanPham getThe() {
        return the;
    }

    public List<String> getAnh() {
        return anh;
    }

    public List<ThongSoKyThuat> getThongSo() {
        return thongSo;
    }

    public List<DanhGia> getDanhGia() {
        return danhGia;
    }

    public List<TheSanPham> getCungNhom() {
        return cungNhom;
    }

    public List<String> getDoanMoTa() {
        String moTa = the.getSanPham().getMoTa();
        if (moTa == null || moTa.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(moTa.split("\\\\n|\\r?\\n")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    public String getDiemTrungBinh() {
        if (danhGia.isEmpty()) {
            return null;
        }
        double tb = danhGia.stream().mapToInt(DanhGia::getSoSao).average().orElse(0);
        return String.format("%.1f", tb).replace('.', ',');
    }
}
