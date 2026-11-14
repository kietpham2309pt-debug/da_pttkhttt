package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.util.ArrayList;
import java.util.List;

public class BieuMauNhanVien {

    private Long maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private String tenDangNhap;
    private List<String> vaiTro = new ArrayList<>();

    public Long getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Long maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public List<String> getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(List<String> vaiTro) {
        this.vaiTro = vaiTro == null ? new ArrayList<>() : vaiTro;
    }
}
