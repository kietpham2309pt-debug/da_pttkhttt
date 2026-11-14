package vn.edu.huit.pttk.diengiadung.khodulieu;

import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import java.util.List;

public interface LichSuTrangThaiDonDAO extends JpaRepository<LichSuTrangThaiDon, Long> {

    List<LichSuTrangThaiDon> findByDonHangOrderByThoiDiemAsc(DonHang donHang);
}
