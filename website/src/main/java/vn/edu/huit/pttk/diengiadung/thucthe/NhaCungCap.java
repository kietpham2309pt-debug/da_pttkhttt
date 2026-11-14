package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;

@Entity
@Table(name = "NhaCungCap")
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maNhaCungCap;

    private String tenNhaCungCap;

    private String soDienThoai;

    private String email;

    private String diaChi;

    private Boolean dangHopTac;

    public Long getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(Long maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Boolean getDangHopTac() {
        return dangHopTac;
    }

    public void setDangHopTac(Boolean dangHopTac) {
        this.dangHopTac = dangHopTac;
    }

}
