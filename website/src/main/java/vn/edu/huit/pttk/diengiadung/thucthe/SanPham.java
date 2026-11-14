package vn.edu.huit.pttk.diengiadung.thucthe;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "SanPham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maSanPham;

    private String tenSanPham;

    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDanhMuc")
    private DanhMuc danhMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maThuongHieu")
    private ThuongHieu thuongHieu;

    private Long giaBan;

    private Long giaNiemYet;

    private Integer thoiHanBaoHanh;

    private Boolean canLapDat;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    private String anh;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TrangThaiSanPham trangThai;

    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThongSoKyThuat> thongSo = new ArrayList<>();

    public Long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(Long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }

    public ThuongHieu getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public Long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Long giaBan) {
        this.giaBan = giaBan;
    }

    public Long getGiaNiemYet() {
        return giaNiemYet;
    }

    public void setGiaNiemYet(Long giaNiemYet) {
        this.giaNiemYet = giaNiemYet;
    }

    public Integer getThoiHanBaoHanh() {
        return thoiHanBaoHanh;
    }

    public void setThoiHanBaoHanh(Integer thoiHanBaoHanh) {
        this.thoiHanBaoHanh = thoiHanBaoHanh;
    }

    public Boolean getCanLapDat() {
        return canLapDat;
    }

    public void setCanLapDat(Boolean canLapDat) {
        this.canLapDat = canLapDat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public TrangThaiSanPham getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiSanPham trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public List<ThongSoKyThuat> getThongSo() {
        return thongSo;
    }

    public void setThongSo(List<ThongSoKyThuat> thongSo) {
        this.thongSo = thongSo;
    }

}
