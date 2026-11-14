package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

public interface SerialTruyVanG5 extends JpaRepository<SanPhamSerial, String> {

    @Query("select s.sanPham.maSanPham, s.trangThai, count(s) from SanPhamSerial s group by s.sanPham.maSanPham, s.trangThai")
    List<Object[]> demTheoMauVaTrangThai();

    @Query("select s from SanPhamSerial s join fetch s.sanPham left join fetch s.donHangBan where s.sanPham.maSanPham = :ma and s.trangThai = :tt order by s.ngayNhap asc, s.soSerial asc")
    List<SanPhamSerial> theoMauVaTrangThai(@Param("ma") Long ma, @Param("tt") TrangThaiSerial tt);

    @Query("select s from SanPhamSerial s join fetch s.sanPham left join fetch s.donHangBan where s.trangThai = :tt order by s.ngayNhap asc, s.soSerial asc")
    List<SanPhamSerial> theoTrangThai(@Param("tt") TrangThaiSerial tt);

    @Query("select s from SanPhamSerial s join fetch s.sanPham left join fetch s.donHangBan where s.soSerial = :so")
    Optional<SanPhamSerial> dayDu(@Param("so") String so);

    @Query("select s from SanPhamSerial s join fetch s.sanPham left join fetch s.donHangBan where s.soSerial in :ds")
    List<SanPhamSerial> dayDuNhieu(@Param("ds") Collection<String> ds);

    @Query("select s from SanPhamSerial s join fetch s.sanPham join fetch s.donHangBan d left join d.khachHang k where d.soDienThoaiNhan = :sdt or k.soDienThoai = :sdt order by s.ngayBan desc, s.soSerial asc")
    List<SanPhamSerial> theoSoDienThoai(@Param("sdt") String sdt);

    @Query("select s from SanPhamSerial s join fetch s.sanPham join fetch s.donHangBan d where d.maDonHang = :ma order by s.soSerial asc")
    List<SanPhamSerial> theoDonHang(@Param("ma") Long ma);

    @Query("select s.soSerial, s.chiTietNhap.phieuNhap.maPhieuNhap from SanPhamSerial s where s.soSerial in :ds")
    List<Object[]> phieuNhapCuaSerial(@Param("ds") Collection<String> ds);

    @Query("select s from SanPhamSerial s left join fetch s.donHangBan where s.chiTietNhap.phieuNhap.maPhieuNhap = :ma order by s.soSerial asc")
    List<SanPhamSerial> theoPhieuNhap(@Param("ma") Long ma);
}
