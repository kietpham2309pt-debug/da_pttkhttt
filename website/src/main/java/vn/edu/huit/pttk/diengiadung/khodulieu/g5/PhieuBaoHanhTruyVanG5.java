package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiBaoHanh;

public interface PhieuBaoHanhTruyVanG5 extends JpaRepository<PhieuBaoHanh, Long> {

    @Query("select p from PhieuBaoHanh p join fetch p.serial s join fetch s.sanPham join fetch p.nhanVien where p.trangThai <> :tt order by p.ngayHenTra asc, p.maPhieu asc")
    List<PhieuBaoHanh> khacTrangThai(@Param("tt") TrangThaiBaoHanh tt);

    @Query("select p from PhieuBaoHanh p join fetch p.serial s join fetch s.sanPham join fetch p.nhanVien where p.trangThai = :tt order by p.ngayTraKhach desc, p.maPhieu desc limit 20")
    List<PhieuBaoHanh> ganDayTheoTrangThai(@Param("tt") TrangThaiBaoHanh tt);

    @Query("select p from PhieuBaoHanh p join fetch p.nhanVien where p.serial.soSerial = :so order by p.ngayNhan asc, p.maPhieu asc")
    List<PhieuBaoHanh> theoSerial(@Param("so") String so);

    @Query("select p from PhieuBaoHanh p join fetch p.serial s join fetch s.sanPham left join fetch s.donHangBan join fetch p.nhanVien where p.maPhieu = :ma")
    Optional<PhieuBaoHanh> chiTiet(@Param("ma") Long ma);
}
