package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "GiaoDichThanhToan")
public class GiaoDichThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maGiaoDich;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private LoaiGiaoDich loai;

    private Long soTien;

    private String phuongThuc;

    private String maGiaoDichCong;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiGiaoDich trangThai;

    private LocalDateTime thoiDiem;

    public Long getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(Long maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public LoaiGiaoDich getLoai() {
        return loai;
    }

    public void setLoai(LoaiGiaoDich loai) {
        this.loai = loai;
    }

    public Long getSoTien() {
        return soTien;
    }

    public void setSoTien(Long soTien) {
        this.soTien = soTien;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getMaGiaoDichCong() {
        return maGiaoDichCong;
    }

    public void setMaGiaoDichCong(String maGiaoDichCong) {
        this.maGiaoDichCong = maGiaoDichCong;
    }

    public TrangThaiGiaoDich getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiGiaoDich trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getThoiDiem() {
        return thoiDiem;
    }

    public void setThoiDiem(LocalDateTime thoiDiem) {
        this.thoiDiem = thoiDiem;
    }

}
