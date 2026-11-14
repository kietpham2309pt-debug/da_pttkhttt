package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "SanPhamSerial")
public class SanPhamSerial {

    @Id
    private String soSerial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maChiTietNhap")
    private ChiTietPhieuNhap chiTietNhap;

    private LocalDateTime ngayNhap;

    private LocalDateTime ngayBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHangBan")
    private DonHang donHangBan;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiSerial trangThai;

    public String getSoSerial() {
        return soSerial;
    }

    public void setSoSerial(String soSerial) {
        this.soSerial = soSerial;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public ChiTietPhieuNhap getChiTietNhap() {
        return chiTietNhap;
    }

    public void setChiTietNhap(ChiTietPhieuNhap chiTietNhap) {
        this.chiTietNhap = chiTietNhap;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public LocalDateTime getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(LocalDateTime ngayBan) {
        this.ngayBan = ngayBan;
    }

    public DonHang getDonHangBan() {
        return donHangBan;
    }

    public void setDonHangBan(DonHang donHangBan) {
        this.donHangBan = donHangBan;
    }

    public TrangThaiSerial getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiSerial trangThai) {
        this.trangThai = trangThai;
    }

}
