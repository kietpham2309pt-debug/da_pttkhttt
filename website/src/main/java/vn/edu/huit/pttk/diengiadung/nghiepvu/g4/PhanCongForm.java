package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiViecGiao;

public class PhanCongForm {

    public static final String NHAN_VIEN = "NHAN_VIEN";
    public static final String VAN_CHUYEN = "VAN_CHUYEN";

    private Long maDon;
    private Long maPhieu;
    private LoaiViecGiao loaiViec;
    private String cachGiao = NHAN_VIEN;
    private Long maNhanVien;
    private String donViVanChuyen;
    private String maVanDon;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayHen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime gioBatDau;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime gioKetThuc;
    private String lyDoTrungLich;

    public Long getMaDon() {
        return maDon;
    }

    public void setMaDon(Long maDon) {
        this.maDon = maDon;
    }

    public Long getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(Long maPhieu) {
        this.maPhieu = maPhieu;
    }

    public LoaiViecGiao getLoaiViec() {
        return loaiViec;
    }

    public void setLoaiViec(LoaiViecGiao loaiViec) {
        this.loaiViec = loaiViec;
    }

    public String getCachGiao() {
        return cachGiao;
    }

    public void setCachGiao(String cachGiao) {
        this.cachGiao = cachGiao;
    }

    public Long getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Long maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getDonViVanChuyen() {
        return donViVanChuyen;
    }

    public void setDonViVanChuyen(String donViVanChuyen) {
        this.donViVanChuyen = donViVanChuyen;
    }

    public String getMaVanDon() {
        return maVanDon;
    }

    public void setMaVanDon(String maVanDon) {
        this.maVanDon = maVanDon;
    }

    public LocalDate getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(LocalDate ngayHen) {
        this.ngayHen = ngayHen;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getLyDoTrungLich() {
        return lyDoTrungLich;
    }

    public void setLyDoTrungLich(String lyDoTrungLich) {
        this.lyDoTrungLich = lyDoTrungLich;
    }
}
