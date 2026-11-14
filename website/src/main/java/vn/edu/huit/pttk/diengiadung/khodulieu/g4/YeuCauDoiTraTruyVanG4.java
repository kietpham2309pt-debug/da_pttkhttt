package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.YeuCauDoiTra;

public interface YeuCauDoiTraTruyVanG4 extends JpaRepository<YeuCauDoiTra, Long> {

    String NAP = "select distinct y from YeuCauDoiTra y join fetch y.donHang left join fetch y.serial s left join fetch s.sanPham "
            + "left join fetch y.nhanVienTiepNhan left join fetch y.nguoiDuyet ";

    @Query(NAP + "where (:tt is null or y.trangThai = :tt) order by y.ngayYeuCau desc")
    List<YeuCauDoiTra> theoTrangThai(@Param("tt") TrangThaiDoiTra trangThai);

    @Query(NAP + "where y.maYeuCau = :ma")
    Optional<YeuCauDoiTra> layDayDu(@Param("ma") Long ma);

    @Query(NAP + "where y.donHang = :don order by y.ngayYeuCau")
    List<YeuCauDoiTra> theoDon(@Param("don") DonHang don);

    @Query("select y from YeuCauDoiTra y join y.serial s where s.soSerial = :so "
            + "and y.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra.CHO_DUYET")
    List<YeuCauDoiTra> choDuyetCuaSerial(@Param("so") String soSerial);

    @Query("select y.trangThai, count(y) from YeuCauDoiTra y group by y.trangThai")
    List<Object[]> demTheoTrangThai();
}
