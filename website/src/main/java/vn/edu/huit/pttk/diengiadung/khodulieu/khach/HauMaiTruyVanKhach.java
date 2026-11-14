package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;

public interface HauMaiTruyVanKhach extends JpaRepository<PhieuBaoHanh, Long> {

    @Query("select p from PhieuBaoHanh p where p.serial.soSerial = :so order by p.ngayNhan asc")
    List<PhieuBaoHanh> phieuBaoHanh(@Param("so") String soSerial);

    @Query("select p from PhieuGiaoHang p where p.donHang.maDonHang = :ma order by p.maPhieuGiao desc")
    List<PhieuGiaoHang> phieuGiao(@Param("ma") Long maDonHang);
}
