package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

public interface SerialTruyVanG4 extends JpaRepository<SanPhamSerial, String> {

    @Query("select count(s) from SanPhamSerial s where s.sanPham.maSanPham = :sp and s.trangThai = :tt")
    long demTheoTrangThai(@Param("sp") Long maSanPham, @Param("tt") TrangThaiSerial trangThai);

    @Query("select s from SanPhamSerial s join fetch s.sanPham left join fetch s.donHangBan where s.soSerial = :so")
    Optional<SanPhamSerial> layDayDu(@Param("so") String soSerial);
}
