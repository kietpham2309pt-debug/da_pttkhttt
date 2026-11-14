package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.HinhThucDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.YeuCauDoiTra;

public interface YeuCauDoiTraTruyVanG5 extends JpaRepository<YeuCauDoiTra, Long> {

    @Query("select distinct y from YeuCauDoiTra y join fetch y.donHang left join fetch y.serial s left join fetch s.sanPham where y.trangThai = :tt and y.hinhThuc = :ht order by y.maYeuCau asc")
    List<YeuCauDoiTra> theoTrangThaiVaHinhThuc(@Param("tt") TrangThaiDoiTra tt, @Param("ht") HinhThucDoiTra ht);

    @Query("select distinct y from YeuCauDoiTra y join fetch y.donHang left join fetch y.serial s left join fetch s.sanPham where y.maYeuCau = :ma")
    Optional<YeuCauDoiTra> kemSerial(@Param("ma") Long ma);

    @Query("select y.maYeuCau from YeuCauDoiTra y join y.serial s where s.soSerial = :so order by y.ngayYeuCau desc")
    List<Long> yeuCauCuaSerial(@Param("so") String so);
}
