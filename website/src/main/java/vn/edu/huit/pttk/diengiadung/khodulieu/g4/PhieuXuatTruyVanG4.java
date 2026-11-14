package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;

public interface PhieuXuatTruyVanG4 extends JpaRepository<PhieuXuat, Long> {

    @Query("select distinct x from PhieuXuat x left join fetch x.serial s left join fetch s.sanPham left join fetch x.thuKho "
            + "where x.donHang = :don order by x.ngayXuat")
    List<PhieuXuat> theoDon(@Param("don") DonHang don);

    @Query("select count(x) from PhieuXuat x where x.donHang.maDonHang = :ma")
    long demTheoDon(@Param("ma") Long maDonHang);
}
