package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuLapDat;

public interface PhieuLapDatTruyVanG4 extends JpaRepository<PhieuLapDat, Long> {

    @Query("select b from PhieuLapDat b where b.phieuGiao.maPhieuGiao = :ma")
    Optional<PhieuLapDat> theoPhieuGiao(@Param("ma") Long maPhieuGiao);

    @Query("select count(b) from PhieuLapDat b where b.phieuGiao.donHang = :don")
    long demTheoDon(@Param("don") DonHang don);
}
