package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuNhap;

public interface PhieuNhapTruyVanG5 extends JpaRepository<PhieuNhap, Long> {

    @Query("select distinct p from PhieuNhap p join fetch p.nhaCungCap join fetch p.thuKho left join fetch p.donDat left join fetch p.chiTiet order by p.ngayNhap desc, p.maPhieuNhap desc")
    List<PhieuNhap> danhSach();

    @Query("select distinct p from PhieuNhap p join fetch p.nhaCungCap join fetch p.thuKho left join fetch p.donDat left join fetch p.chiTiet c left join fetch c.sanPham where p.maPhieuNhap = :ma")
    Optional<PhieuNhap> chiTiet(@Param("ma") Long ma);

    @Query("select c.donGiaNhap from ChiTietPhieuNhap c where c.sanPham.maSanPham = :ma order by c.maChiTietNhap desc limit 1")
    List<Long> donGiaGanNhat(@Param("ma") Long ma);
}
