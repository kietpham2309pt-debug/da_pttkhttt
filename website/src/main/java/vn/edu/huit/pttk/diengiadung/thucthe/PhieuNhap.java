package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PhieuNhap")
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPhieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonDat")
    private DonDatHangNCC donDat;

    private String soHoaDonNCC;

    private LocalDateTime ngayNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maThuKho")
    private NhanVien thuKho;

    private String ghiChu;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietPhieuNhap> chiTiet = new ArrayList<>();

    public Long getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(Long maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public DonDatHangNCC getDonDat() {
        return donDat;
    }

    public void setDonDat(DonDatHangNCC donDat) {
        this.donDat = donDat;
    }

    public String getSoHoaDonNCC() {
        return soHoaDonNCC;
    }

    public void setSoHoaDonNCC(String soHoaDonNCC) {
        this.soHoaDonNCC = soHoaDonNCC;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public NhanVien getThuKho() {
        return thuKho;
    }

    public void setThuKho(NhanVien thuKho) {
        this.thuKho = thuKho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<ChiTietPhieuNhap> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietPhieuNhap> chiTiet) {
        this.chiTiet = chiTiet;
    }

}
