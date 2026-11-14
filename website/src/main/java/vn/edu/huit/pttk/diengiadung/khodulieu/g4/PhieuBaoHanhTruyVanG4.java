package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;

public interface PhieuBaoHanhTruyVanG4 extends JpaRepository<PhieuBaoHanh, Long> {

    @Query("select b from PhieuBaoHanh b where b.serial.soSerial in :so order by b.ngayNhan")
    List<PhieuBaoHanh> theoSerial(@Param("so") List<String> soSerial);
}
