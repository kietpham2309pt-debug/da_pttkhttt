package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;

public interface GiaoDichTruyVanG4 extends JpaRepository<GiaoDichThanhToan, Long> {

    @Query("select g from GiaoDichThanhToan g where g.donHang = :don order by g.thoiDiem")
    List<GiaoDichThanhToan> theoDon(@Param("don") DonHang don);
}
