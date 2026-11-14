package vn.edu.huit.pttk.diengiadung.khodulieu.khach;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

public interface SanPhamTruyVanKhach extends JpaRepository<SanPham, Long>, JpaSpecificationExecutor<SanPham> {

    @Query("select s from SanPham s join fetch s.thuongHieu join fetch s.danhMuc where s.trangThai = :tt and s.giaBan is not null order by s.ngayTao desc, s.maSanPham desc")
    List<SanPham> moiNhat(@Param("tt") TrangThaiSanPham trangThai, Pageable trang);

    @Query("select s from SanPham s join fetch s.thuongHieu join fetch s.danhMuc where s.maSanPham in :ma")
    List<SanPham> theoMa(@Param("ma") Collection<Long> ma);

    @Query("select s from SanPham s join fetch s.thuongHieu join fetch s.danhMuc where s.danhMuc.maDanhMuc = :dm and s.maSanPham <> :bo and s.trangThai = :tt and s.giaBan is not null order by s.ngayTao desc")
    List<SanPham> cungNhom(@Param("dm") String maDanhMuc, @Param("bo") Long boMa, @Param("tt") TrangThaiSanPham trangThai, Pageable trang);
}
