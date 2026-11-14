package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "LichSuTrangThaiDon")
public class LichSuTrangThaiDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLichSu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiDonHang trangThaiCu;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiDonHang trangThaiMoi;

    private LocalDateTime thoiDiem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    private String ghiChu;

    public Long getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(Long maLichSu) {
        this.maLichSu = maLichSu;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public TrangThaiDonHang getTrangThaiCu() {
        return trangThaiCu;
    }

    public void setTrangThaiCu(TrangThaiDonHang trangThaiCu) {
        this.trangThaiCu = trangThaiCu;
    }

    public TrangThaiDonHang getTrangThaiMoi() {
        return trangThaiMoi;
    }

    public void setTrangThaiMoi(TrangThaiDonHang trangThaiMoi) {
        this.trangThaiMoi = trangThaiMoi;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
