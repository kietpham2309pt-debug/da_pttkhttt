package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonDatHangNCC;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonDat;

public interface DonDatTruyVanG5 extends JpaRepository<DonDatHangNCC, Long> {

    @Query("select distinct d from DonDatHangNCC d join fetch d.nhaCungCap join fetch d.nguoiLap left join fetch d.nguoiGui left join fetch d.chiTiet order by d.ngayLap desc, d.maDonDat desc")
    List<DonDatHangNCC> danhSach();

    @Query("select distinct d from DonDatHangNCC d join fetch d.nhaCungCap join fetch d.nguoiLap left join fetch d.nguoiGui left join fetch d.chiTiet where d.trangThai = :tt order by d.ngayLap asc, d.maDonDat asc")
    List<DonDatHangNCC> theoTrangThai(@Param("tt") TrangThaiDonDat tt);

    @Query("select distinct d from DonDatHangNCC d join fetch d.nhaCungCap join fetch d.nguoiLap left join fetch d.nguoiGui left join fetch d.chiTiet c left join fetch c.sanPham where d.maDonDat = :ma")
    java.util.Optional<DonDatHangNCC> chiTiet(@Param("ma") Long ma);

    @Query("select c.sanPham.maSanPham, sum(c.soLuongThucNhap) from ChiTietPhieuNhap c where c.phieuNhap.donDat.maDonDat = :ma group by c.sanPham.maSanPham")
    List<Object[]> daNhapTheoMau(@Param("ma") Long ma);
}
