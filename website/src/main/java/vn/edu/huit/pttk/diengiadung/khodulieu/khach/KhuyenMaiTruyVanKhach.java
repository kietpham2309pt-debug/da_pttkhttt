package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiKhuyenMai;

public interface KhuyenMaiTruyVanKhach extends JpaRepository<KhuyenMai, Long> {

    @Query("select distinct k from KhuyenMai k join fetch k.sanPham where k.loai = :loai and k.trangThai = :tt and k.ngayBatDau <= :hom and k.ngayKetThuc >= :hom")
    List<KhuyenMai> dangChay(@Param("loai") LoaiKhuyenMai loai, @Param("tt") TrangThaiKhuyenMai trangThai, @Param("hom") LocalDate homNay);

    Optional<KhuyenMai> findFirstByMaCodeIgnoreCaseAndLoai(String maCode, LoaiKhuyenMai loai);
}
