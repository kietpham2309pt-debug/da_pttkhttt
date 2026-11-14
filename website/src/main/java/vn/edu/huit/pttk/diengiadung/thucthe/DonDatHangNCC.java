package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "DonDatHangNCC")
public class DonDatHangNCC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDonDat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    private LocalDateTime ngayLap;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiDonDat trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNguoiLap")
    private NhanVien nguoiLap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNguoiGui")
    private NhanVien nguoiGui;

    private String ghiChu;

    @OneToMany(mappedBy = "donDat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonDatHangNCC> chiTiet = new ArrayList<>();

    public Long getMaDonDat() {
        return maDonDat;
    }

    public void setMaDonDat(Long maDonDat) {
        this.maDonDat = maDonDat;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public TrangThaiDonDat getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDonDat trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNguoiLap() {
        return nguoiLap;
    }

    public void setNguoiLap(NhanVien nguoiLap) {
        this.nguoiLap = nguoiLap;
    }

    public NhanVien getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(NhanVien nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<ChiTietDonDatHangNCC> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietDonDatHangNCC> chiTiet) {
        this.chiTiet = chiTiet;
    }

}
