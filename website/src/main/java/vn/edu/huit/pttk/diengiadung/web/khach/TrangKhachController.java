package vn.edu.huit.pttk.diengiadung.web.khach;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.SanPhamKhachBUS;

@Controller
public class TrangKhachController {

    private final SanPhamKhachBUS sanPhamBUS;

    public TrangKhachController(SanPhamKhachBUS sanPhamBUS) {
        this.sanPhamBUS = sanPhamBUS;
    }

    @GetMapping("/")
    public String trangChu(Model model) {
        model.addAttribute("dsDanhMuc", sanPhamBUS.danhMuc());
        model.addAttribute("spMoi", sanPhamBUS.moiNhat(8));
        model.addAttribute("spKhuyenMai", sanPhamBUS.dangKhuyenMai(8));
        return "khach/trang-chu";
    }

    @GetMapping("/chinh-sach/bao-mat")
    public String baoMat() {
        return "khach/bao-mat";
    }

    @GetMapping("/chinh-sach/dieu-khoan")
    public String dieuKhoan() {
        return "khach/dieu-khoan";
    }
}
