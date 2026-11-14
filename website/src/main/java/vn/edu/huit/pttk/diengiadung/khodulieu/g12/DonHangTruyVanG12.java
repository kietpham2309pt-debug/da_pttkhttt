package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

public interface DonHangTruyVanG12 extends JpaRepository<DonHang, Long> {

    long countByTrangThai(TrangThaiDonHang trangThai);

    @Query("select distinct d from DonHang d left join fetch d.chiTiet ct left join fetch ct.sanPham sp left join fetch sp.danhMuc "
            + "where d.trangThai = :trangThai and d.ngayHoanThanh >= :tu and d.ngayHoanThanh < :den order by d.ngayHoanThanh")
    List<DonHang> donHoanThanhTrongKhoang(@Param("trangThai") TrangThaiDonHang trangThai,
                                          @Param("tu") LocalDateTime tu, @Param("den") LocalDateTime den);

    @Query("select g.donHang.maDonHang, g.loai, sum(g.soTien) from GiaoDichThanhToan g "
            + "where g.donHang.maDonHang in :ma and g.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich.THANH_CONG "
            + "group by g.donHang.maDonHang, g.loai")
    List<Object[]> tongGiaoDichTheoDon(@Param("ma") Collection<Long> ma);

    @Query("select p.donHang.maDonHang, p.maPhieuGiao, p.tienThuKhiGiao, kt.maNhanVien, p.ngayHen from PhieuGiaoHang p left join p.keToanXacNhan kt "
            + "where p.donHang.maDonHang in :ma and p.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_GIAO")
    List<Object[]> phieuDaGiaoTheoDon(@Param("ma") Collection<Long> ma);
}
