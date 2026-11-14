package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietDonHang")
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    private String tenSanPhamLucBan;

    private Integer soLuong;

    private Long donGia;

    public Long getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(Long maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public String getTenSanPhamLucBan() {
        return tenSanPhamLucBan;
    }

    public void setTenSanPhamLucBan(String tenSanPhamLucBan) {
        this.tenSanPhamLucBan = tenSanPhamLucBan;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Long getDonGia() {
        return donGia;
    }

    public void setDonGia(Long donGia) {
        this.donGia = donGia;
    }

}
