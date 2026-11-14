package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PhieuLapDat")
public class PhieuLapDat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPhieuLapDat;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhieuGiao")
    private PhieuGiaoHang phieuGiao;

    private String ketQuaChayThu;

    private String tinhTrangCongTrinh;

    private String phatSinh;

    private Long phiLapDat;

    private Long phiVatTu;

    private Boolean khachDaKy;

    private LocalDateTime ngayLap;

    public Long getMaPhieuLapDat() {
        return maPhieuLapDat;
    }

    public void setMaPhieuLapDat(Long maPhieuLapDat) {
        this.maPhieuLapDat = maPhieuLapDat;
    }

    public PhieuGiaoHang getPhieuGiao() {
        return phieuGiao;
    }

    public void setPhieuGiao(PhieuGiaoHang phieuGiao) {
        this.phieuGiao = phieuGiao;
    }

    public String getKetQuaChayThu() {
        return ketQuaChayThu;
    }

    public void setKetQuaChayThu(String ketQuaChayThu) {
        this.ketQuaChayThu = ketQuaChayThu;
    }

    public String getTinhTrangCongTrinh() {
        return tinhTrangCongTrinh;
    }

    public void setTinhTrangCongTrinh(String tinhTrangCongTrinh) {
        this.tinhTrangCongTrinh = tinhTrangCongTrinh;
    }

    public String getPhatSinh() {
        return phatSinh;
    }

    public void setPhatSinh(String phatSinh) {
        this.phatSinh = phatSinh;
    }

    public Long getPhiLapDat() {
        return phiLapDat;
    }

    public void setPhiLapDat(Long phiLapDat) {
        this.phiLapDat = phiLapDat;
    }

    public Long getPhiVatTu() {
        return phiVatTu;
    }

    public void setPhiVatTu(Long phiVatTu) {
        this.phiVatTu = phiVatTu;
    }

    public Boolean getKhachDaKy() {
        return khachDaKy;
    }

    public void setKhachDaKy(Boolean khachDaKy) {
        this.khachDaKy = khachDaKy;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

}
