package vn.edu.huit.pttk.diengiadung.web.quantri.g1;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.QuanTriTaiKhoanBUS;

@Component
public class ChanTaiKhoanBiKhoa extends OncePerRequestFilter {

    private final QuanTriTaiKhoanBUS taiKhoanBUS;

    public ChanTaiKhoanBiKhoa(QuanTriTaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return uri.startsWith("/assets/") || uri.startsWith("/css/") || uri.startsWith("/img/") || uri.startsWith("/js/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a != null && a.isAuthenticated() && !"anonymousUser".equals(a.getName()) && taiKhoanBUS.dangBiKhoa(a.getName())) {
            SecurityContextHolder.clearContext();
            HttpSession s = req.getSession(false);
            if (s != null) {
                s.invalidate();
            }
            res.sendRedirect("/dang-nhap?daKhoa");
            return;
        }
        chain.doFilter(req, res);
    }
}
