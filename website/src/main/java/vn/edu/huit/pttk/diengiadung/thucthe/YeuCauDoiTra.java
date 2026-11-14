package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "YeuCauDoiTra")
public class YeuCauDoiTra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maYeuCau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    private LocalDateTime ngayYeuCau;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private LoaiLyDoDoiTra loaiLyDo;

    private String lyDo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private HinhThucDoiTra hinhThuc;

    private String tinhTrangMay;

    private Boolean conSeal;

    private Long phiKhauTru;

    private Long soTienHoan;

    private Boolean laNgoaiLe;

    private String ghiChuYKienChu;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiDoiTra trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVienTiepNhan")
    private NhanVien nhanVienTiepNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNguoiDuyet")
    private NhanVien nguoiDuyet;

    private LocalDateTime ngayDuyet;

    private String lyDoTuChoi;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ChiTietDoiTra", joinColumns = @JoinColumn(name = "maYeuCau"), inverseJoinColumns = @JoinColumn(name = "soSerial"))
    private Set<SanPhamSerial> serial = new LinkedHashSet<>();

    public Long getMaYeuCau() {
        return maYeuCau;
    }

    public void setMaYeuCau(Long maYeuCau) {
        this.maYeuCau = maYeuCau;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public LocalDateTime getNgayYeuCau() {
        return ngayYeuCau;
    }

    public void setNgayYeuCau(LocalDateTime ngayYeuCau) {
        this.ngayYeuCau = ngayYeuCau;
    }

    public LoaiLyDoDoiTra getLoaiLyDo() {
        return loaiLyDo;
    }

    public void setLoaiLyDo(LoaiLyDoDoiTra loaiLyDo) {
        this.loaiLyDo = loaiLyDo;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public HinhThucDoiTra getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(HinhThucDoiTra hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public String getTinhTrangMay() {
        return tinhTrangMay;
    }

    public void setTinhTrangMay(String tinhTrangMay) {
        this.tinhTrangMay = tinhTrangMay;
    }

    public Boolean getConSeal() {
        return conSeal;
    }

    public void setConSeal(Boolean conSeal) {
        this.conSeal = conSeal;
    }

    public Long getPhiKhauTru() {
        return phiKhauTru;
    }

    public void setPhiKhauTru(Long phiKhauTru) {
        this.phiKhauTru = phiKhauTru;
    }

    public Long getSoTienHoan() {
        return soTienHoan;
    }

    public void setSoTienHoan(Long soTienHoan) {
        this.soTienHoan = soTienHoan;
    }

    public Boolean getLaNgoaiLe() {
        return laNgoaiLe;
    }

    public void setLaNgoaiLe(Boolean laNgoaiLe) {
        this.laNgoaiLe = laNgoaiLe;
    }

    public String getGhiChuYKienChu() {
        return ghiChuYKienChu;
    }

    public void setGhiChuYKienChu(String ghiChuYKienChu) {
        this.ghiChuYKienChu = ghiChuYKienChu;
    }

    public TrangThaiDoiTra getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDoiTra trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNhanVienTiepNhan() {
        return nhanVienTiepNhan;
    }

    public void setNhanVienTiepNhan(NhanVien nhanVienTiepNhan) {
        this.nhanVienTiepNhan = nhanVienTiepNhan;
    }

    public NhanVien getNguoiDuyet() {
        return nguoiDuyet;
    }

    public void setNguoiDuyet(NhanVien nguoiDuyet) {
        this.nguoiDuyet = nguoiDuyet;
    }

    public LocalDateTime getNgayDuyet() {
        return ngayDuyet;
    }

    public void setNgayDuyet(LocalDateTime ngayDuyet) {
        this.ngayDuyet = ngayDuyet;
    }

    public String getLyDoTuChoi() {
        return lyDoTuChoi;
    }

    public void setLyDoTuChoi(String lyDoTuChoi) {
        this.lyDoTuChoi = lyDoTuChoi;
    }

    public Set<SanPhamSerial> getSerial() {
        return serial;
    }

    public void setSerial(Set<SanPhamSerial> serial) {
        this.serial = serial;
    }

}
