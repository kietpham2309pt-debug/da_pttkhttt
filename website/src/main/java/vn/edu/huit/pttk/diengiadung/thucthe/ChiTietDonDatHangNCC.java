package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ChiTietDonDatHangNCC")
@IdClass(ChiTietDonDatHangNCC.Khoa.class)
public class ChiTietDonDatHangNCC {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDonDat")
    private DonDatHangNCC donDat;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    private Integer soLuong;

    public DonDatHangNCC getDonDat() {
        return donDat;
    }

    public void setDonDat(DonDatHangNCC donDat) {
        this.donDat = donDat;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }


    public static class Khoa implements Serializable {
        private Long donDat;
        private Long sanPham;

        public Khoa() {
        }

        public Long getDonDat() {
            return donDat;
        }

        public void setDonDat(Long donDat) {
            this.donDat = donDat;
        }

        public Long getSanPham() {
            return sanPham;
        }

        public void setSanPham(Long sanPham) {
            this.sanPham = sanPham;
        }

        @Override
        public boolean equals(Object x) {
            if (this == x) return true;
            if (!(x instanceof Khoa o)) return false;
            return Objects.equals(donDat, o.donDat) && Objects.equals(sanPham, o.sanPham);
        }

        @Override
        public int hashCode() {
            return Objects.hash(donDat, sanPham);
        }
    }
}
