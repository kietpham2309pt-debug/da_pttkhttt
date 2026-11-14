package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;

public interface KhuyenMaiTruyVanG12 extends JpaRepository<KhuyenMai, Long> {

    @Query("select km from KhuyenMai km join fetch km.nguoiTao order by km.ngayBatDau desc, km.maKhuyenMai desc")
    List<KhuyenMai> layTatCa();

    @Query("select distinct km from KhuyenMai km join fetch km.nguoiTao left join fetch km.sanPham where km.maKhuyenMai = :ma")
    Optional<KhuyenMai> timDayDu(@Param("ma") Long ma);

    boolean existsByMaCodeIgnoreCaseAndMaKhuyenMaiNot(String maCode, Long maKhuyenMai);

    @Query("select sp.tenSanPham, km.tenKhuyenMai from KhuyenMai km join km.sanPham sp "
            + "where sp.maSanPham in :ds and km.loai = :loai and km.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiKhuyenMai.DANG_CHAY "
            + "and km.ngayBatDau <= :den and km.ngayKetThuc >= :tu and km.maKhuyenMai <> :tru")
    List<Object[]> trungThoiGian(@Param("ds") Collection<Long> ds, @Param("loai") LoaiKhuyenMai loai,
                                 @Param("tu") LocalDate tu, @Param("den") LocalDate den, @Param("tru") Long tru);
}
