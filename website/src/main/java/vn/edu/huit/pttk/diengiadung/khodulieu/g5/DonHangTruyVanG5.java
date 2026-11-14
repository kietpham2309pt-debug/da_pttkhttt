package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

public interface DonHangTruyVanG5 extends JpaRepository<DonHang, Long> {

    @Query("select distinct d from DonHang d left join fetch d.chiTiet c left join fetch c.sanPham where d.trangThai = :tt and not exists (select p from PhieuXuat p where p.donHang = d) order by d.ngayDat asc")
    List<DonHang> chuaCoPhieuXuat(@Param("tt") TrangThaiDonHang tt);

    @Query("select distinct d from DonHang d left join fetch d.chiTiet c left join fetch c.sanPham where d.maDonHang = :ma")
    Optional<DonHang> kemChiTiet(@Param("ma") Long ma);

    @Query("select count(p) > 0 from PhieuXuat p where p.donHang.maDonHang = :ma")
    boolean daCoPhieuXuat(@Param("ma") Long ma);
}
