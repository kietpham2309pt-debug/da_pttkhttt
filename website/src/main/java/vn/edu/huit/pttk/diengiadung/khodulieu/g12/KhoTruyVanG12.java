package vn.edu.huit.pttk.diengiadung.khodulieu.g12;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;

public interface KhoTruyVanG12 extends JpaRepository<SanPhamSerial, String> {

    @Query("select s.sanPham.maSanPham, s.trangThai, count(s) from SanPhamSerial s group by s.sanPham.maSanPham, s.trangThai")
    List<Object[]> demSerialTheoTrangThai();

    @Query("select p from PhieuBaoHanh p left join fetch p.serial s left join fetch s.sanPham "
            + "where p.trangThai <> vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiBaoHanh.DA_TRA_KHACH order by p.ngayNhan")
    List<PhieuBaoHanh> phieuBaoHanhDangMo();

    @Query("select count(p) from PhieuBaoHanh p where p.trangThai <> vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiBaoHanh.DA_TRA_KHACH")
    long demPhieuBaoHanhDangMo();

    @Query("select count(y) from YeuCauDoiTra y where y.trangThai = vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra.CHO_DUYET")
    long demDoiTraChoDuyet();
}
