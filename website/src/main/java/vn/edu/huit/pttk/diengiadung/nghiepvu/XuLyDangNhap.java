package vn.edu.huit.pttk.diengiadung.nghiepvu;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.TaiKhoanDAO;
import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;

@Component
public class XuLyDangNhap implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    public static final int SO_LAN_SAI_TOI_DA = 5;
    public static final int PHUT_KHOA = 15;

    private final TaiKhoanDAO taiKhoanDAO;

    public XuLyDangNhap(TaiKhoanDAO taiKhoanDAO) {
        this.taiKhoanDAO = taiKhoanDAO;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        TaiKhoan tk = taiKhoanDAO.findByTenDangNhap(auth.getName()).orElseThrow();
        tk.setSoLanSaiLienTiep(0);
        tk.setKhoaDenLuc(null);
        taiKhoanDAO.save(tk);
        if (Boolean.TRUE.equals(tk.getPhaiDoiMatKhau())) {
            res.sendRedirect("/doi-mat-khau?lanDau");
            return;
        }
        boolean laKhach = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_KHACH_HANG"));
        String tiep = req.getParameter("tiep");
        if (tiep != null && tiep.startsWith("/") && !tiep.startsWith("//")) {
            res.sendRedirect(tiep);
        } else {
            res.sendRedirect(laKhach ? "/" : "/quan-tri");
        }
    }

    @Override
    @Transactional
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        if (ex instanceof LockedException) {
            res.sendRedirect("/dang-nhap?tamKhoa");
            return;
        }
        if (ex instanceof DisabledException) {
            res.sendRedirect("/dang-nhap?daKhoa");
            return;
        }
        String ten = req.getParameter("tenDangNhap");
        Optional<TaiKhoan> tk = ten == null ? Optional.empty() : taiKhoanDAO.findByTenDangNhap(ten.trim());
        if (tk.isPresent()) {
            TaiKhoan t = tk.get();
            int lan = (t.getSoLanSaiLienTiep() == null ? 0 : t.getSoLanSaiLienTiep()) + 1;
            t.setSoLanSaiLienTiep(lan);
            if (lan >= SO_LAN_SAI_TOI_DA) {
                t.setKhoaDenLuc(LocalDateTime.now().plusMinutes(PHUT_KHOA));
                t.setSoLanSaiLienTiep(0);
                taiKhoanDAO.save(t);
                res.sendRedirect("/dang-nhap?tamKhoa");
                return;
            }
            taiKhoanDAO.save(t);
        }
        res.sendRedirect("/dang-nhap?sai");
    }
}
