package vn.edu.huit.pttk.diengiadung.web.quantri.g1;

import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.BieuMauNhanVien;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.QuanTriTaiKhoanBUS;

@Controller
@RequestMapping("/quan-tri/tai-khoan")
public class QuanTriTaiKhoanController {

    private final QuanTriTaiKhoanBUS bus;
    private final TaiKhoanBUS taiKhoanBUS;

    public QuanTriTaiKhoanController(QuanTriTaiKhoanBUS bus, TaiKhoanBUS taiKhoanBUS) {
        this.bus = bus;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa, Model model) {
        model.addAttribute("dsNhanVien", bus.danhSach(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("toi", taiKhoanBUS.tenDangNhapHienTai().orElse(""));
        return "quan-tri/g1/tai-khoan";
    }

    @GetMapping("/them")
    public String them(Model model) {
        return bieuMau(model, new BieuMauNhanVien());
    }

    @GetMapping("/{ma}/sua")
    public String sua(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            return bieuMau(model, bus.bieuMauSua(ma));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/tai-khoan";
        }
    }

    @PostMapping("/luu")
    public String luu(@ModelAttribute("bm") BieuMauNhanVien bm, Model model, RedirectAttributes ra) {
        try {
            if (bm.getMaNhanVien() == null) {
                String matKhau = bus.themNhanVien(bm);
                ra.addFlashAttribute("thongBao", "Đã thêm nhân viên " + bm.getHoTen().trim() + ".");
                ra.addFlashAttribute("matKhauTam", matKhau);
                ra.addFlashAttribute("tenMatKhauTam", bm.getTenDangNhap().trim());
            } else {
                bus.suaNhanVien(bm);
                ra.addFlashAttribute("thongBao", "Đã cập nhật nhân viên " + bm.getHoTen().trim() + ".");
            }
            return "redirect:/quan-tri/tai-khoan";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            return bieuMau(model, bm);
        }
    }

    @PostMapping("/{ma}/khoa")
    public String khoa(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã khoá tài khoản " + bus.khoa(ma) + "; các phiên đang mở của tài khoản này bị huỷ.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @PostMapping("/{ma}/mo-khoa")
    public String moKhoa(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã mở khoá tài khoản " + bus.moKhoa(ma) + ".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @PostMapping("/{ma}/dat-lai-mat-khau")
    public String datLai(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            String[] kq = bus.datLaiMatKhau(ma);
            ra.addFlashAttribute("thongBao", "Đã đặt lại mật khẩu cho " + kq[0] + ".");
            ra.addFlashAttribute("tenMatKhauTam", kq[0]);
            ra.addFlashAttribute("matKhauTam", kq[1]);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    private String bieuMau(Model model, BieuMauNhanVien bm) {
        model.addAttribute("bm", bm);
        model.addAttribute("dsVai", bus.vaiChoNhanVien());
        if (!model.containsAttribute("loiO")) {
            model.addAttribute("loiO", new HashMap<String, String>());
        }
        return "quan-tri/g1/tai-khoan-form";
    }
}
