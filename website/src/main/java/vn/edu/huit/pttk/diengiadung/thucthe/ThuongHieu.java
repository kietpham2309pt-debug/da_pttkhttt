package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;

@Entity
@Table(name = "ThuongHieu")
public class ThuongHieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maThuongHieu;

    private String tenThuongHieu;

    private String xuatXu;

    public Long getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(Long maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

}
