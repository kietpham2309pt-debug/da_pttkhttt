package vn.edu.huit.pttk.diengiadung.khodulieu;

import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaiKhoanDAO extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);
}
