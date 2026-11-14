package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maKhuyenMai;

    private String tenKhuyenMai;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private LoaiKhuyenMai loai;

    private String maCode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private KieuGiam kieuGiam;

    private Long mucGiam;

    private Long giamToiDa;

    private Long donToiThieu;

    private String quaTang;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;

    private Integer soLuotToiDa;

    private Integer soLuotDaDung;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiKhuyenMai trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNguoiTao")
    private NhanVien nguoiTao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "KhuyenMaiSanPham", joinColumns = @JoinColumn(name = "maKhuyenMai"), inverseJoinColumns = @JoinColumn(name = "maSanPham"))
    private Set<SanPham> sanPham = new LinkedHashSet<>();

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

    public LoaiKhuyenMai getLoai() {
        return loai;
    }

    public void setLoai(LoaiKhuyenMai loai) {
        this.loai = loai;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public KieuGiam getKieuGiam() {
        return kieuGiam;
    }

    public void setKieuGiam(KieuGiam kieuGiam) {
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

    public Integer getSoLuotDaDung() {
        return soLuotDaDung;
    }

    public void setSoLuotDaDung(Integer soLuotDaDung) {
        this.soLuotDaDung = soLuotDaDung;
    }

    public TrangThaiKhuyenMai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiKhuyenMai trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(NhanVien nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public Set<SanPham> getSanPham() {
        return sanPham;
    }

    public void setSanPham(Set<SanPham> sanPham) {
        this.sanPham = sanPham;
    }

}
