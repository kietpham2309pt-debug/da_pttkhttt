package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietPhieuNhap")
public class ChiTietPhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maChiTietNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhieuNhap")
    private PhieuNhap phieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    private Integer soLuongChungTu;

    private Integer soLuongThucNhap;

    private Long donGiaNhap;

    public Long getMaChiTietNhap() {
        return maChiTietNhap;
    }

    public void setMaChiTietNhap(Long maChiTietNhap) {
        this.maChiTietNhap = maChiTietNhap;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Integer getSoLuongChungTu() {
        return soLuongChungTu;
    }

    public void setSoLuongChungTu(Integer soLuongChungTu) {
        this.soLuongChungTu = soLuongChungTu;
    }

    public Integer getSoLuongThucNhap() {
        return soLuongThucNhap;
    }

    public void setSoLuongThucNhap(Integer soLuongThucNhap) {
        this.soLuongThucNhap = soLuongThucNhap;
    }

    public Long getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(Long donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

}
