package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhGia;
import vn.edu.huit.pttk.diengiadung.thucthe.DiaChiGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;

public interface KhachHangTruyVanKhach extends JpaRepository<KhachHang, Long> {

    boolean existsBySoDienThoai(String soDienThoai);

    boolean existsBySoDienThoaiAndMaKhachHangNot(String soDienThoai, Long maKhachHang);

    @Query("select d from DiaChiGiaoHang d where d.khachHang.maKhachHang = :kh order by d.macDinh desc, d.maDiaChi asc")
    List<DiaChiGiaoHang> soDiaChi(@Param("kh") Long maKhachHang);

    @Query("select d from DanhGia d join fetch d.khachHang where d.sanPham.maSanPham = :sp order by d.ngayDanhGia desc")
    List<DanhGia> danhGiaCuaSanPham(@Param("sp") Long maSanPham);

    @Query("select count(d) > 0 from DanhGia d where d.donHang.maDonHang = :don and d.sanPham.maSanPham = :sp")
    boolean daDanhGia(@Param("don") Long maDonHang, @Param("sp") Long maSanPham);
}
