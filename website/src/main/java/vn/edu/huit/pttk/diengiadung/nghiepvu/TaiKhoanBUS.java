package vn.edu.huit.pttk.diengiadung.nghiepvu;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.KhachHangDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.NhanVienDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.TaiKhoanDAO;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan;

@Service
public class TaiKhoanBUS implements UserDetailsService {

    private final TaiKhoanDAO taiKhoanDAO;
    private final NhanVienDAO nhanVienDAO;
    private final KhachHangDAO khachHangDAO;

    public TaiKhoanBUS(TaiKhoanDAO taiKhoanDAO, NhanVienDAO nhanVienDAO, KhachHangDAO khachHangDAO) {
        this.taiKhoanDAO = taiKhoanDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.khachHangDAO = khachHangDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String ten) {
        TaiKhoan tk = taiKhoanDAO.findByTenDangNhap(ten.trim()).orElseThrow(() -> new UsernameNotFoundException(ten));
        boolean tamKhoa = tk.getKhoaDenLuc() != null && tk.getKhoaDenLuc().isAfter(LocalDateTime.now());
        return User.withUsername(tk.getTenDangNhap())
                .password(tk.getMatKhau())
                .disabled(tk.getTrangThai() == TrangThaiTaiKhoan.DA_KHOA)
                .accountLocked(tamKhoa)
                .authorities(tk.getVaiTro().stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v.getMaVaiTro())).toList())
                .build();
    }

    public Optional<String> tenDangNhapHienTai() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || "anonymousUser".equals(a.getName())) {
            return Optional.empty();
        }
        return Optional.of(a.getName());
    }

    @Transactional(readOnly = true)
    public Optional<NhanVien> nhanVienHienTai() {
        return tenDangNhapHienTai().flatMap(nhanVienDAO::findByTaiKhoan_TenDangNhap);
    }

    @Transactional(readOnly = true)
    public NhanVien batBuocNhanVien() {
        return nhanVienHienTai().orElseThrow(() -> new LoiNghiepVu("Chức năng này chỉ dành cho nhân viên"));
    }

    @Transactional(readOnly = true)
    public Optional<KhachHang> khachHangHienTai() {
        return tenDangNhapHienTai().flatMap(khachHangDAO::findByTaiKhoan_TenDangNhap);
    }

    public boolean coVai(String vai) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return a != null && a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_" + vai));
    }
}
