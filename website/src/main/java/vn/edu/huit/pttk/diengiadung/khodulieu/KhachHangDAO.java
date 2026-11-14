package vn.edu.huit.pttk.diengiadung.khodulieu;

import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KhachHangDAO extends JpaRepository<KhachHang, Long> {

    Optional<KhachHang> findByTaiKhoan_TenDangNhap(String tenDangNhap);
}
