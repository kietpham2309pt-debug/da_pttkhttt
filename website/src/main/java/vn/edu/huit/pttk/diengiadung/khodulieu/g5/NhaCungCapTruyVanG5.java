package vn.edu.huit.pttk.diengiadung.khodulieu.g5;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.NhaCungCap;

public interface NhaCungCapTruyVanG5 extends JpaRepository<NhaCungCap, Long> {

    List<NhaCungCap> findAllByOrderByDangHopTacDescTenNhaCungCapAsc();

    List<NhaCungCap> findByDangHopTacTrueOrderByTenNhaCungCapAsc();

    boolean existsByTenNhaCungCapIgnoreCase(String ten);

    boolean existsByTenNhaCungCapIgnoreCaseAndMaNhaCungCapNot(String ten, Long ma);

    @Query("select count(p) from PhieuNhap p where p.nhaCungCap.maNhaCungCap = :ma")
    long demPhieuNhap(@Param("ma") Long ma);

    @Query("select count(d) from DonDatHangNCC d where d.nhaCungCap.maNhaCungCap = :ma")
    long demDonDat(@Param("ma") Long ma);
}
