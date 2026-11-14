package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuGia;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

public interface SanPhamTruyVanG12 extends JpaRepository<SanPham, Long> {

    @Query("select sp from SanPham sp join fetch sp.danhMuc dm join fetch sp.thuongHieu th "
            + "where (:dm is null or dm.maDanhMuc = :dm) and (:th is null or th.maThuongHieu = :th) "
            + "and (:tt is null or sp.trangThai = :tt) "
            + "and (:tk is null or lower(sp.tenSanPham) like :tk or lower(sp.model) like :tk) order by sp.maSanPham desc")
    List<SanPham> loc(@Param("dm") String dm, @Param("th") Long th, @Param("tt") TrangThaiSanPham tt, @Param("tk") String tk);

    @Query("select sp from SanPham sp join fetch sp.danhMuc join fetch sp.thuongHieu left join fetch sp.thongSo where sp.maSanPham = :ma")
    Optional<SanPham> timDayDu(@Param("ma") Long ma);

    @Query("select sp from SanPham sp join fetch sp.danhMuc join fetch sp.thuongHieu where sp.trangThai <> :tru order by sp.tenSanPham")
    List<SanPham> layTruTrangThai(@Param("tru") TrangThaiSanPham tru);

    @Query("select sp from SanPham sp join fetch sp.danhMuc join fetch sp.thuongHieu order by sp.danhMuc.thuTu, sp.tenSanPham")
    List<SanPham> layTatCaKemDanhMuc();

    Optional<SanPham> findFirstByModelIgnoreCase(String model);

    long countByTrangThai(TrangThaiSanPham trangThai);

    long countByDanhMuc_MaDanhMuc(String maDanhMuc);

    long countByThuongHieu_MaThuongHieu(Long maThuongHieu);

    @Query("select c.donGiaNhap from ChiTietPhieuNhap c where c.sanPham.maSanPham = :ma order by c.phieuNhap.ngayNhap desc, c.maChiTietNhap desc")
    List<Long> giaNhapMoiNhat(@Param("ma") Long ma, Pageable trang);

    @Query("select l from LichSuGia l join fetch l.nhanVien where l.sanPham.maSanPham = :ma order by l.thoiDiem desc, l.maLichSu desc")
    List<LichSuGia> lichSuGia(@Param("ma") Long ma);

    @Query("select sp.danhMuc.maDanhMuc, ts.tenThongSo, count(ts) from ThongSoKyThuat ts join ts.sanPham sp "
            + "group by sp.danhMuc.maDanhMuc, ts.tenThongSo order by sp.danhMuc.maDanhMuc, count(ts) desc")
    List<Object[]> thongSoThuongGapTheoDanhMuc();
}
