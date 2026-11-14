package vn.edu.huit.pttk.diengiadung.nghiepvu.g2;

import java.util.ArrayList;
import java.util.List;

public class BieuMauSanPham {

    private Long maSanPham;
    private String tenSanPham;
    private String model;
    private String maDanhMuc;
    private Long maThuongHieu;
    private Integer thoiHanBaoHanh = 12;
    private boolean canLapDat;
    private String moTa;
    private String anh;
    private List<DongThongSo> thongSo = new ArrayList<>();

    public Long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(Long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public Long getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(Long maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public Integer getThoiHanBaoHanh() {
        return thoiHanBaoHanh;
    }

    public void setThoiHanBaoHanh(Integer thoiHanBaoHanh) {
        this.thoiHanBaoHanh = thoiHanBaoHanh;
    }

    public boolean isCanLapDat() {
        return canLapDat;
    }

    public void setCanLapDat(boolean canLapDat) {
        this.canLapDat = canLapDat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public List<DongThongSo> getThongSo() {
        return thongSo;
    }

    public void setThongSo(List<DongThongSo> thongSo) {
        this.thongSo = thongSo == null ? new ArrayList<>() : thongSo;
    }

    public static class DongThongSo {

        private String ten;
        private String giaTri;

        public DongThongSo() {
        }

        public DongThongSo(String ten, String giaTri) {
            this.ten = ten;
            this.giaTri = giaTri;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public String getGiaTri() {
            return giaTri;
        }

        public void setGiaTri(String giaTri) {
            this.giaTri = giaTri;
        }
    }
}
