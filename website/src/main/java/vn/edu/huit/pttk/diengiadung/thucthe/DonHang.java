package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "DonHang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDonHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    private String hoTenNguoiNhan;

    private String soDienThoaiNhan;

    private String email;

    private String diaChiGiao;

    private Boolean noiThanh;

    private Boolean canLapDat;

    private LocalDateTime ngayDat;

    private LocalDateTime ngayHoanThanh;

    private Long tienHang;

    private Long tienGiam;

    private Long phiGiao;

    private Long phiLapDat;

    private Long tongTien;

    private Long tienCoc;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private PhuongThucThanhToan phuongThucThanhToan;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiThanhToan trangThaiThanhToan;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiDonHang trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhuyenMai")
    private KhuyenMai khuyenMai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVienDuyet")
    private NhanVien nhanVienDuyet;

    private String ghiChu;

    private String lyDoHuy;

    private String soHoaDon;

    private Boolean dongYXuLyDuLieu;

    private Boolean dongYQuangCao;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonHang> chiTiet = new ArrayList<>();

    public Long getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(Long maDonHang) {
        this.maDonHang = maDonHang;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getHoTenNguoiNhan() {
        return hoTenNguoiNhan;
    }

    public void setHoTenNguoiNhan(String hoTenNguoiNhan) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
    }

    public String getSoDienThoaiNhan() {
        return soDienThoaiNhan;
    }

    public void setSoDienThoaiNhan(String soDienThoaiNhan) {
        this.soDienThoaiNhan = soDienThoaiNhan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChiGiao() {
        return diaChiGiao;
    }

    public void setDiaChiGiao(String diaChiGiao) {
        this.diaChiGiao = diaChiGiao;
    }

    public Boolean getNoiThanh() {
        return noiThanh;
    }

    public void setNoiThanh(Boolean noiThanh) {
        this.noiThanh = noiThanh;
    }

    public Boolean getCanLapDat() {
        return canLapDat;
    }

    public void setCanLapDat(Boolean canLapDat) {
        this.canLapDat = canLapDat;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public Long getTienHang() {
        return tienHang;
    }

    public void setTienHang(Long tienHang) {
        this.tienHang = tienHang;
    }

    public Long getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(Long tienGiam) {
        this.tienGiam = tienGiam;
    }

    public Long getPhiGiao() {
        return phiGiao;
    }

    public void setPhiGiao(Long phiGiao) {
        this.phiGiao = phiGiao;
    }

    public Long getPhiLapDat() {
        return phiLapDat;
    }

    public void setPhiLapDat(Long phiLapDat) {
        this.phiLapDat = phiLapDat;
    }

    public Long getTongTien() {
        return tongTien;
    }

    public void setTongTien(Long tongTien) {
        this.tongTien = tongTien;
    }

    public Long getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(Long tienCoc) {
        this.tienCoc = tienCoc;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public TrangThaiThanhToan getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(TrangThaiThanhToan trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public TrangThaiDonHang getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDonHang trangThai) {
        this.trangThai = trangThai;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public NhanVien getNhanVienDuyet() {
        return nhanVienDuyet;
    }

    public void setNhanVienDuyet(NhanVien nhanVienDuyet) {
        this.nhanVienDuyet = nhanVienDuyet;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getLyDoHuy() {
        return lyDoHuy;
    }

    public void setLyDoHuy(String lyDoHuy) {
        this.lyDoHuy = lyDoHuy;
    }

    public String getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(String soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public Boolean getDongYXuLyDuLieu() {
        return dongYXuLyDuLieu;
    }

    public void setDongYXuLyDuLieu(Boolean dongYXuLyDuLieu) {
        this.dongYXuLyDuLieu = dongYXuLyDuLieu;
    }

    public Boolean getDongYQuangCao() {
        return dongYQuangCao;
    }

    public void setDongYQuangCao(Boolean dongYQuangCao) {
        this.dongYQuangCao = dongYQuangCao;
    }

    public List<ChiTietDonHang> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietDonHang> chiTiet) {
        this.chiTiet = chiTiet;
    }

}
