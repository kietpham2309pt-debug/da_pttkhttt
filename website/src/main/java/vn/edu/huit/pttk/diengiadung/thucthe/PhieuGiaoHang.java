package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "PhieuGiaoHang")
public class PhieuGiaoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPhieuGiao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private LoaiViecGiao loaiViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVienGiao")
    private NhanVien nhanVienGiao;

    private String donViVanChuyen;

    private String maVanDon;

    private LocalDate ngayHen;

    private LocalTime gioBatDau;

    private LocalTime gioKetThuc;

    private String lyDoTrungLich;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiPhieuGiao trangThai;

    private String lyDoKhongThanhCong;

    private Long tienThuKhiGiao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKeToanXacNhan")
    private NhanVien keToanXacNhan;

    private LocalDateTime thoiDiemXacNhanTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNguoiPhanCong")
    private NhanVien nguoiPhanCong;

    public Long getMaPhieuGiao() {
        return maPhieuGiao;
    }

    public void setMaPhieuGiao(Long maPhieuGiao) {
        this.maPhieuGiao = maPhieuGiao;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public LoaiViecGiao getLoaiViec() {
        return loaiViec;
    }

    public void setLoaiViec(LoaiViecGiao loaiViec) {
        this.loaiViec = loaiViec;
    }

    public NhanVien getNhanVienGiao() {
        return nhanVienGiao;
    }

    public void setNhanVienGiao(NhanVien nhanVienGiao) {
        this.nhanVienGiao = nhanVienGiao;
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

    public TrangThaiPhieuGiao getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhieuGiao trangThai) {
        this.trangThai = trangThai;
    }

    public String getLyDoKhongThanhCong() {
        return lyDoKhongThanhCong;
    }

    public void setLyDoKhongThanhCong(String lyDoKhongThanhCong) {
        this.lyDoKhongThanhCong = lyDoKhongThanhCong;
    }

    public Long getTienThuKhiGiao() {
        return tienThuKhiGiao;
    }

    public void setTienThuKhiGiao(Long tienThuKhiGiao) {
        this.tienThuKhiGiao = tienThuKhiGiao;
    }

    public NhanVien getKeToanXacNhan() {
        return keToanXacNhan;
    }

    public void setKeToanXacNhan(NhanVien keToanXacNhan) {
        this.keToanXacNhan = keToanXacNhan;
    }

    public LocalDateTime getThoiDiemXacNhanTien() {
        return thoiDiemXacNhanTien;
    }

    public void setThoiDiemXacNhanTien(LocalDateTime thoiDiemXacNhanTien) {
        this.thoiDiemXacNhanTien = thoiDiemXacNhanTien;
    }

    public NhanVien getNguoiPhanCong() {
        return nguoiPhanCong;
    }

    public void setNguoiPhanCong(NhanVien nguoiPhanCong) {
        this.nguoiPhanCong = nguoiPhanCong;
    }

}
