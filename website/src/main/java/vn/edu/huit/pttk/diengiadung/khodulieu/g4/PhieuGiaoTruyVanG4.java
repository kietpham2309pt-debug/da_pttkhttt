package vn.edu.huit.pttk.diengiadung.khodulieu.g4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;

public interface PhieuGiaoTruyVanG4 extends JpaRepository<PhieuGiaoHang, Long> {

    String NAP = "select p from PhieuGiaoHang p join fetch p.donHang left join fetch p.nhanVienGiao nvg left join fetch p.nguoiPhanCong left join fetch p.keToanXacNhan ";

    @Query(NAP + "where p.maPhieuGiao = :ma")
    Optional<PhieuGiaoHang> layDayDu(@Param("ma") Long ma);

    @Query(NAP + "where p.donHang = :don order by p.maPhieuGiao")
    List<PhieuGiaoHang> theoDon(@Param("don") DonHang don);

    @Query(NAP + "where p.ngayHen = :ngay and (:nv is null or p.nhanVienGiao.maNhanVien = :nv) order by nvg.hoTen, p.gioBatDau")
    List<PhieuGiaoHang> lichNgay(@Param("ngay") LocalDate ngay, @Param("nv") Long maNhanVien);

    @Query(NAP + "where p.nhanVienGiao.maNhanVien = :nv and p.ngayHen = :ngay "
            + "and p.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_PHAN_CONG order by p.gioBatDau")
    List<PhieuGiaoHang> viecCuaNhanVienTrongNgay(@Param("nv") Long maNhanVien, @Param("ngay") LocalDate ngay);

    @Query(NAP + "where p.nhanVienGiao.maNhanVien = :nv "
            + "and p.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_PHAN_CONG order by p.ngayHen, p.gioBatDau")
    List<PhieuGiaoHang> viecDangCho(@Param("nv") Long maNhanVien);

    @Query(NAP + "where p.nhanVienGiao.maNhanVien = :nv "
            + "and p.trangThai <> vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_PHAN_CONG and p.ngayHen >= :tu order by p.ngayHen desc, p.gioBatDau desc")
    List<PhieuGiaoHang> viecDaXong(@Param("nv") Long maNhanVien, @Param("tu") LocalDate tuNgay);

    @Query(NAP + "where p.donViVanChuyen is not null "
            + "and p.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_PHAN_CONG order by p.ngayHen")
    List<PhieuGiaoHang> vanChuyenDangCho();

    @Query(NAP + "where p.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao.DA_GIAO "
            + "and p.tienThuKhiGiao > 0 and p.keToanXacNhan is null order by p.ngayHen, nvg.hoTen, p.maPhieuGiao")
    List<PhieuGiaoHang> tienChuaXacNhan();

    @Query(NAP + "where p.keToanXacNhan is not null and p.thoiDiemXacNhanTien >= :tu order by p.thoiDiemXacNhanTien desc")
    List<PhieuGiaoHang> tienDaXacNhan(@Param("tu") LocalDateTime tu);
}
