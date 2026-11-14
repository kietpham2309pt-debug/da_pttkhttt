package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "PhieuBaoHanh")
public class PhieuBaoHanh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPhieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soSerial")
    private SanPhamSerial serial;

    private String hoTenKhach;

    private String soDienThoaiKhach;

    private LocalDateTime ngayNhan;

    private String moTaLoi;

    private String tinhTrangBenNgoai;

    private LocalDate ngayHenTra;

    private Integer lanThu;

    private LocalDateTime ngayGuiHang;

    private LocalDateTime ngayNhanTuHang;

    private LocalDateTime ngayTraKhach;

    private String ketQua;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiBaoHanh trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    public Long getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(Long maPhieu) {
        this.maPhieu = maPhieu;
    }

    public SanPhamSerial getSerial() {
        return serial;
    }

    public void setSerial(SanPhamSerial serial) {
        this.serial = serial;
    }

    public String getHoTenKhach() {
        return hoTenKhach;
    }

    public void setHoTenKhach(String hoTenKhach) {
        this.hoTenKhach = hoTenKhach;
    }

    public String getSoDienThoaiKhach() {
        return soDienThoaiKhach;
    }

    public void setSoDienThoaiKhach(String soDienThoaiKhach) {
        this.soDienThoaiKhach = soDienThoaiKhach;
    }

    public LocalDateTime getNgayNhan() {
        return ngayNhan;
    }

    public void setNgayNhan(LocalDateTime ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public String getMoTaLoi() {
        return moTaLoi;
    }

    public void setMoTaLoi(String moTaLoi) {
        this.moTaLoi = moTaLoi;
    }

    public String getTinhTrangBenNgoai() {
        return tinhTrangBenNgoai;
    }

    public void setTinhTrangBenNgoai(String tinhTrangBenNgoai) {
        this.tinhTrangBenNgoai = tinhTrangBenNgoai;
    }

    public LocalDate getNgayHenTra() {
        return ngayHenTra;
    }

    public void setNgayHenTra(LocalDate ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public Integer getLanThu() {
        return lanThu;
    }

    public void setLanThu(Integer lanThu) {
        this.lanThu = lanThu;
    }

    public LocalDateTime getNgayGuiHang() {
        return ngayGuiHang;
    }

    public void setNgayGuiHang(LocalDateTime ngayGuiHang) {
        this.ngayGuiHang = ngayGuiHang;
    }

    public LocalDateTime getNgayNhanTuHang() {
        return ngayNhanTuHang;
    }

    public void setNgayNhanTuHang(LocalDateTime ngayNhanTuHang) {
        this.ngayNhanTuHang = ngayNhanTuHang;
    }

    public LocalDateTime getNgayTraKhach() {
        return ngayTraKhach;
    }

    public void setNgayTraKhach(LocalDateTime ngayTraKhach) {
        this.ngayTraKhach = ngayTraKhach;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public TrangThaiBaoHanh getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiBaoHanh trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

}
