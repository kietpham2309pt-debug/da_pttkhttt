package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

public interface TonKhoTruyVanKhach extends JpaRepository<SanPhamSerial, String> {

    @Query("select s.sanPham.maSanPham, count(s) from SanPhamSerial s where s.trangThai = :tt and s.sanPham.maSanPham in :ma group by s.sanPham.maSanPham")
    List<Object[]> demTrongKho(@Param("tt") TrangThaiSerial trangThai, @Param("ma") Collection<Long> maSanPham);

    @Query("select c.sanPham.maSanPham, sum(c.soLuong) from ChiTietDonHang c where c.sanPham.maSanPham in :ma and c.donHang.trangThai in :ttDon"
            + " and not exists (select p.maPhieuXuat from PhieuXuat p where p.donHang = c.donHang) group by c.sanPham.maSanPham")
    List<Object[]> demDangGiu(@Param("ma") Collection<Long> maSanPham, @Param("ttDon") Collection<TrangThaiDonHang> trangThaiDon);

    @Query("select s from SanPhamSerial s join fetch s.sanPham sp join fetch sp.thuongHieu left join fetch s.donHangBan where s.soSerial = :so")
    List<SanPhamSerial> theoSoSerial(@Param("so") String soSerial);

    @Query("select s from SanPhamSerial s join fetch s.sanPham sp join fetch sp.thuongHieu join fetch s.donHangBan d where d.soDienThoaiNhan = :sdt and s.ngayBan is not null order by s.ngayBan desc")
    List<SanPhamSerial> theoSoDienThoai(@Param("sdt") String soDienThoai);
}
