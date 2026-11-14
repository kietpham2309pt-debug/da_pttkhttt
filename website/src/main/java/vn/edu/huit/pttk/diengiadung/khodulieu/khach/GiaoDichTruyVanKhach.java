package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;

public interface GiaoDichTruyVanKhach extends JpaRepository<GiaoDichThanhToan, Long> {

    List<GiaoDichThanhToan> findByDonHang_MaDonHangOrderByThoiDiemAsc(Long maDonHang);

    List<GiaoDichThanhToan> findByMaGiaoDichCong(String maGiaoDichCong);
}
