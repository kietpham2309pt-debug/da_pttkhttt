package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maTaiKhoan;

    private String tenDangNhap;

    private String matKhau;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiTaiKhoan trangThai;

    private LocalDateTime ngayTao;

    private Integer soLanSaiLienTiep;

    private LocalDateTime khoaDenLuc;

    private Boolean phaiDoiMatKhau;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TaiKhoanVaiTro", joinColumns = @JoinColumn(name = "maTaiKhoan"), inverseJoinColumns = @JoinColumn(name = "maVaiTro"))
    private Set<VaiTro> vaiTro = new LinkedHashSet<>();

    public Long getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public TrangThaiTaiKhoan getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiTaiKhoan trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Integer getSoLanSaiLienTiep() {
        return soLanSaiLienTiep;
    }

    public void setSoLanSaiLienTiep(Integer soLanSaiLienTiep) {
        this.soLanSaiLienTiep = soLanSaiLienTiep;
    }

    public LocalDateTime getKhoaDenLuc() {
        return khoaDenLuc;
    }

    public void setKhoaDenLuc(LocalDateTime khoaDenLuc) {
        this.khoaDenLuc = khoaDenLuc;
    }

    public Boolean getPhaiDoiMatKhau() {
        return phaiDoiMatKhau;
    }

    public void setPhaiDoiMatKhau(Boolean phaiDoiMatKhau) {
        this.phaiDoiMatKhau = phaiDoiMatKhau;
    }

    public Set<VaiTro> getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(Set<VaiTro> vaiTro) {
        this.vaiTro = vaiTro;
    }

}
