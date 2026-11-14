package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackageClasses = XuLyLoiG5.class)
public class XuLyLoiG5 {

    @ExceptionHandler(QuyenG5.KhongCoQuyen.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String khongCoQuyen(Model model) {
        model.addAttribute("tieuDe", "Không có quyền");
        model.addAttribute("noiDung", "Vai trò của tài khoản này không được dùng chức năng vừa mở.");
        model.addAttribute("lienKet", "/quan-tri");
        model.addAttribute("chuLienKet", "Về trang quản trị");
        return "thong-diep";
    }
}
