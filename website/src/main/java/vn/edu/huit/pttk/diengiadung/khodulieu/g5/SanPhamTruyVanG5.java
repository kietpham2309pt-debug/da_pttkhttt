package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;

public interface SanPhamTruyVanG5 extends JpaRepository<SanPham, Long> {

    @Query("select s from SanPham s join fetch s.danhMuc dm order by dm.thuTu asc, s.tenSanPham asc")
    List<SanPham> tatCaTheoDanhMuc();
}
