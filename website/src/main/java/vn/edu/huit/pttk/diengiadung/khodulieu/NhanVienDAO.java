package vn.edu.huit.pttk.diengiadung.khodulieu;

import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NhanVienDAO extends JpaRepository<NhanVien, Long> {

    Optional<NhanVien> findByTaiKhoan_TenDangNhap(String tenDangNhap);
}
