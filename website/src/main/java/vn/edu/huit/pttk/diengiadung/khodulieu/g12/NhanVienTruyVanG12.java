package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan;

public interface NhanVienTruyVanG12 extends JpaRepository<NhanVien, Long> {

    @Query("select nv from NhanVien nv join fetch nv.taiKhoan tk order by nv.maNhanVien")
    List<NhanVien> layTatCaKemTaiKhoan();

    @Query("select nv from NhanVien nv join fetch nv.taiKhoan tk where nv.maNhanVien = :ma")
    Optional<NhanVien> timKemTaiKhoan(@Param("ma") Long ma);

    @Query("select count(distinct tk) from TaiKhoan tk join tk.vaiTro v where v.maVaiTro = 'QUAN_TRI' and tk.trangThai = :trangThai and tk.maTaiKhoan <> :tru")
    long demQuanTriKhac(@Param("trangThai") TrangThaiTaiKhoan trangThai, @Param("tru") Long tru);

    @Query("select tk.trangThai from TaiKhoan tk where tk.tenDangNhap = :ten")
    Optional<TrangThaiTaiKhoan> trangThaiTheoTen(@Param("ten") String ten);

    @Query("select tk.phaiDoiMatKhau from TaiKhoan tk where tk.tenDangNhap = :ten")
    Optional<Boolean> phaiDoiMatKhauTheoTen(@Param("ten") String ten);
}
