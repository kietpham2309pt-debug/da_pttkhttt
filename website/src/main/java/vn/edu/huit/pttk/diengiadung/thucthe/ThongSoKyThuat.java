package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;

@Entity
@Table(name = "ThongSoKyThuat")
public class ThongSoKyThuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maThongSo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    private String tenThongSo;

    private String giaTri;

    public Long getMaThongSo() {
        return maThongSo;
    }

    public void setMaThongSo(Long maThongSo) {
        this.maThongSo = maThongSo;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public String getTenThongSo() {
        return tenThongSo;
    }

    public void setTenThongSo(String tenThongSo) {
        this.tenThongSo = tenThongSo;
    }

    public String getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(String giaTri) {
        this.giaTri = giaTri;
    }

}
