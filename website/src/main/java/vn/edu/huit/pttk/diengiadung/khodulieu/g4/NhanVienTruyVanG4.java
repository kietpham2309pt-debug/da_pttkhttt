package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;

public interface NhanVienTruyVanG4 extends JpaRepository<NhanVien, Long> {

    @Query("select distinct n from NhanVien n join n.taiKhoan t join t.vaiTro v where v.maVaiTro = :vai "
            + "and t.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan.DANG_DUNG order by n.hoTen")
    List<NhanVien> theoVai(@Param("vai") String maVaiTro);
}
