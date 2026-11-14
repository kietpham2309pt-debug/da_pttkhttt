package vn.edu.huit.pttk.diengiadung.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.pttk.diengiadung.khodulieu.TaiKhoanDAO;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;

@Controller
public class ChungController {

    private final TaiKhoanBUS taiKhoanBUS;
    private final TaiKhoanDAO taiKhoanDAO;
    private final PasswordEncoder maHoa;

    public ChungController(TaiKhoanBUS taiKhoanBUS, TaiKhoanDAO taiKhoanDAO, PasswordEncoder maHoa) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.taiKhoanDAO = taiKhoanDAO;
        this.maHoa = maHoa;
    }

    @GetMapping("/dang-nhap")
    public String dangNhap() {
        return "dang-nhap";
    }

    @GetMapping("/doi-mat-khau")
    public String doiMatKhau() {
        return "doi-mat-khau";
    }

    @PostMapping("/doi-mat-khau")
    @Transactional
    public String doiMatKhau(@RequestParam String matKhauCu, @RequestParam String matKhauMoi, @RequestParam String nhapLai,
                             Model model, HttpServletRequest req, HttpServletResponse res) {
        TaiKhoan tk = taiKhoanDAO.findByTenDangNhap(taiKhoanBUS.tenDangNhapHienTai().orElseThrow()).orElseThrow();
        if (!maHoa.matches(matKhauCu, tk.getMatKhau())) {
            model.addAttribute("loi", "Mật khẩu hiện tại không đúng.");
            return "doi-mat-khau";
        }
        if (matKhauMoi.length() < 6) {
            model.addAttribute("loi", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return "doi-mat-khau";
        }
        if (!matKhauMoi.equals(nhapLai)) {
            model.addAttribute("loi", "Hai lần nhập mật khẩu mới không giống nhau.");
            return "doi-mat-khau";
        }
        if (maHoa.matches(matKhauMoi, tk.getMatKhau())) {
            model.addAttribute("loi", "Mật khẩu mới phải khác mật khẩu hiện tại.");
            return "doi-mat-khau";
        }
        tk.setMatKhau(maHoa.encode(matKhauMoi));
        tk.setPhaiDoiMatKhau(false);
        taiKhoanDAO.save(tk);
        new SecurityContextLogoutHandler().logout(req, res, SecurityContextHolder.getContext().getAuthentication());
        model.addAttribute("tieuDe", "Đã đổi mật khẩu");
        model.addAttribute("noiDung", "Mời bạn đăng nhập lại bằng mật khẩu mới.");
        model.addAttribute("lienKet", "/dang-nhap");
        model.addAttribute("chuLienKet", "Đăng nhập");
        return "thong-diep";
    }

    @RequestMapping("/khong-co-quyen")
    public String khongCoQuyen(Model model) {
        model.addAttribute("tieuDe", "Không có quyền");
        model.addAttribute("noiDung", "Vai trò của tài khoản này không được dùng chức năng vừa mở.");
        model.addAttribute("lienKet", "/quan-tri");
        model.addAttribute("chuLienKet", "Về trang quản trị");
        return "thong-diep";
    }
}
