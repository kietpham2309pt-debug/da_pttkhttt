package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

public interface DonHangTruyVanKhach extends JpaRepository<DonHang, Long> {

    @Query("select distinct d from DonHang d left join fetch d.chiTiet c left join fetch c.sanPham sp left join fetch sp.thuongHieu left join fetch d.khachHang where d.maDonHang = :ma")
    Optional<DonHang> layDayDu(@Param("ma") Long maDonHang);

    @Query("select d from DonHang d where d.khachHang.maKhachHang = :kh order by d.ngayDat desc")
    List<DonHang> cuaKhach(@Param("kh") Long maKhachHang);

    @Query("select distinct d from DonHang d join fetch d.chiTiet c join fetch c.sanPham where d.khachHang.maKhachHang = :kh and d.trangThai = :tt order by d.ngayDat desc")
    List<DonHang> cuaKhachTheoTrangThai(@Param("kh") Long maKhachHang, @Param("tt") TrangThaiDonHang trangThai);
}
