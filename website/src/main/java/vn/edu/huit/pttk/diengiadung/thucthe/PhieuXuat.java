package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PhieuXuat")
public class PhieuXuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPhieuXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    private LocalDateTime ngayXuat;

    private String lyDoXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maThuKho")
    private NhanVien thuKho;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ChiTietPhieuXuat", joinColumns = @JoinColumn(name = "maPhieuXuat"), inverseJoinColumns = @JoinColumn(name = "soSerial"))
    private Set<SanPhamSerial> serial = new LinkedHashSet<>();

    public Long getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(Long maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getLyDoXuat() {
        return lyDoXuat;
    }

    public void setLyDoXuat(String lyDoXuat) {
        this.lyDoXuat = lyDoXuat;
    }

    public NhanVien getThuKho() {
        return thuKho;
    }

    public void setThuKho(NhanVien thuKho) {
        this.thuKho = thuKho;
    }

    public Set<SanPhamSerial> getSerial() {
        return serial;
    }

    public void setSerial(Set<SanPhamSerial> serial) {
        this.serial = serial;
    }

}
