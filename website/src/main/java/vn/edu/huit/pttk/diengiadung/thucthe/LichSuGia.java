package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuGia")
public class LichSuGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLichSu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    private Long giaCu;

    private Long giaMoi;

    private String lyDo;

    private Boolean duoiGiaNhap;

    private LocalDateTime thoiDiem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    public Long getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(Long maLichSu) {
        this.maLichSu = maLichSu;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Long getGiaCu() {
        return giaCu;
    }

    public void setGiaCu(Long giaCu) {
        this.giaCu = giaCu;
    }

    public Long getGiaMoi() {
        return giaMoi;
    }

    public void setGiaMoi(Long giaMoi) {
        this.giaMoi = giaMoi;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public Boolean getDuoiGiaNhap() {
        return duoiGiaNhap;
    }

    public void setDuoiGiaNhap(Boolean duoiGiaNhap) {
        this.duoiGiaNhap = duoiGiaNhap;
    }

    public LocalDateTime getThoiDiem() {
        return thoiDiem;
    }

    public void setThoiDiem(LocalDateTime thoiDiem) {
        this.thoiDiem = thoiDiem;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

}
