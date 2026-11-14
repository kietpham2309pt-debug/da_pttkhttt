package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

public interface DonHangTruyVanG4 extends JpaRepository<DonHang, Long> {

    @Query("select d from DonHang d where (:tt is null or d.trangThai = :tt) "
            + "and (:sdt is null or d.soDienThoaiNhan like :sdt or d.maDonHang = :ma) order by d.ngayDat desc, d.maDonHang desc")
    List<DonHang> timDon(@Param("tt") TrangThaiDonHang trangThai, @Param("sdt") String soDienThoai, @Param("ma") Long ma);

    @Query("select d.trangThai, count(d) from DonHang d group by d.trangThai")
    List<Object[]> demTheoTrangThai();

    @Query("select distinct d from DonHang d left join fetch d.chiTiet c left join fetch c.sanPham left join fetch d.khuyenMai "
            + "left join fetch d.nhanVienDuyet where d.maDonHang = :ma")
    Optional<DonHang> layDayDu(@Param("ma") Long ma);

    @Query("select d from DonHang d where d.trangThai in :tt order by d.ngayDat asc")
    List<DonHang> theoCacTrangThai(@Param("tt") List<TrangThaiDonHang> trangThai);

    @Query("select coalesce(sum(c.soLuong), 0) from ChiTietDonHang c where c.sanPham.maSanPham = :sp "
            + "and c.donHang.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang.DA_XAC_NHAN and c.donHang.maDonHang <> :ma")
    long soLuongDangGiu(@Param("sp") Long maSanPham, @Param("ma") Long maDonHang);
}
