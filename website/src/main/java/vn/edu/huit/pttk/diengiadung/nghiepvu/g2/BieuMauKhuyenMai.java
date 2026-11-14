package vn.edu.huit.pttk.diengiadung.nghiepvu.g2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class BieuMauKhuyenMai {

    private Long maKhuyenMai;
    private String tenKhuyenMai;
    private String loai = "GIAM_THEO_SAN_PHAM";
    private String maCode;
    private String kieuGiam = "PHAN_TRAM";
    private Long mucGiam;
    private Long giamToiDa;
    private Long donToiThieu = 0L;
    private String quaTang;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDau;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThuc;
    private Integer soLuotToiDa;
    private List<Long> sanPham = new ArrayList<>();
    private String lyDoDuoiGiaNhap;

    public Long getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(Long maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getKieuGiam() {
        return kieuGiam;
    }

    public void setKieuGiam(String kieuGiam) {
        this.kieuGiam = kieuGiam;
    }

    public Long getMucGiam() {
        return mucGiam;
    }

    public void setMucGiam(Long mucGiam) {
        this.mucGiam = mucGiam;
    }

    public Long getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(Long giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public Long getDonToiThieu() {
        return donToiThieu;
    }

    public void setDonToiThieu(Long donToiThieu) {
        this.donToiThieu = donToiThieu;
    }

    public String getQuaTang() {
        return quaTang;
    }

    public void setQuaTang(String quaTang) {
        this.quaTang = quaTang;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getSoLuotToiDa() {
        return soLuotToiDa;
    }

    public void setSoLuotToiDa(Integer soLuotToiDa) {
        this.soLuotToiDa = soLuotToiDa;
    }

    public List<Long> getSanPham() {
        return sanPham;
    }

    public void setSanPham(List<Long> sanPham) {
        this.sanPham = sanPham == null ? new ArrayList<>() : sanPham;
    }

    public String getLyDoDuoiGiaNhap() {
        return lyDoDuoiGiaNhap;
    }

    public void setLyDoDuoiGiaNhap(String lyDoDuoiGiaNhap) {
        this.lyDoDuoiGiaNhap = lyDoDuoiGiaNhap;
    }
}
