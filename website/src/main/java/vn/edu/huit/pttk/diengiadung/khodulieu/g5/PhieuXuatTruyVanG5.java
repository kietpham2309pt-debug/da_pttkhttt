package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

public interface PhieuXuatTruyVanG5 extends JpaRepository<PhieuXuat, Long> {

    @Query("select distinct p from PhieuXuat p join fetch p.donHang join fetch p.thuKho left join fetch p.serial order by p.ngayXuat desc, p.maPhieuXuat desc")
    List<PhieuXuat> danhSach();

    @Query("select distinct p from PhieuXuat p join fetch p.donHang join fetch p.thuKho left join fetch p.serial s left join fetch s.sanPham where p.maPhieuXuat = :ma")
    Optional<PhieuXuat> chiTiet(@Param("ma") Long ma);

    boolean existsByLyDoXuat(String lyDoXuat);

    @Query("select s.soSerial, d.maDonHang, p.maPhieuXuat from PhieuXuat p join p.serial s join p.donHang d where s.trangThai = :ts and d.trangThai = :td order by p.ngayXuat asc")
    List<Object[]> serialCuaDonTheoTrangThai(@Param("ts") TrangThaiSerial ts, @Param("td") TrangThaiDonHang td);
}
