package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.ThuongHieu;

public interface DanhMucTruyVanG12 extends JpaRepository<DanhMuc, String> {

    @Query("select dm from DanhMuc dm order by dm.thuTu, dm.tenDanhMuc")
    List<DanhMuc> layTheoThuTu();

    @Query("select th from ThuongHieu th order by th.tenThuongHieu")
    List<ThuongHieu> layThuongHieu();

    @Query("select count(dm) from DanhMuc dm where lower(dm.tenDanhMuc) = lower(:ten) and dm.maDanhMuc <> :tru")
    long demTrungTenDanhMuc(@Param("ten") String ten, @Param("tru") String tru);

    @Query("select count(th) from ThuongHieu th where lower(th.tenThuongHieu) = lower(:ten) and th.maThuongHieu <> :tru")
    long demTrungTenThuongHieu(@Param("ten") String ten, @Param("tru") Long tru);

    @Query("select sp.danhMuc.maDanhMuc, count(sp) from SanPham sp group by sp.danhMuc.maDanhMuc")
    List<Object[]> demSanPhamTheoDanhMuc();

    @Query("select sp.thuongHieu.maThuongHieu, count(sp) from SanPham sp group by sp.thuongHieu.maThuongHieu")
    List<Object[]> demSanPhamTheoThuongHieu();
}
