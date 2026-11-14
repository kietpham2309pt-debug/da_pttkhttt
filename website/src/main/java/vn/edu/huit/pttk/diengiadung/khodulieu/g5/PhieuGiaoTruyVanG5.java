package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;

public interface PhieuGiaoTruyVanG5 extends JpaRepository<PhieuGiaoHang, Long> {

    @Query("select g from PhieuGiaoHang g left join fetch g.nhanVienGiao where g.donHang.maDonHang in :ds order by g.maPhieuGiao desc")
    List<PhieuGiaoHang> theoDon(@Param("ds") Collection<Long> ds);
}
