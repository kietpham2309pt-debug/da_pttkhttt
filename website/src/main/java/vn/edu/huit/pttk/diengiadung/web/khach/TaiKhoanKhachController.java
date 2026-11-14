package vn.edu.huit.pttk.diengiadung.web.khach;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DonHangKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.FormDangKy;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.TaiKhoanKhachBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;

@Controller
public class TaiKhoanKhachController {

    private final TaiKhoanBUS taiKhoanBUS;
    private final TaiKhoanKhachBUS taiKhoanKhachBUS;
    private final DonHangKhachBUS donHangKhachBUS;

    public TaiKhoanKhachController(TaiKhoanBUS taiKhoanBUS, TaiKhoanKhachBUS taiKhoanKhachBUS, DonHangKhachBUS donHangKhachBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.taiKhoanKhachBUS = taiKhoanKhachBUS;
        this.donHangKhachBUS = donHangKhachBUS;
    }

    @GetMapping("/dang-ky")
    public String dangKy(Model model) {
        if (taiKhoanBUS.tenDangNhapHienTai().isPresent()) {
            return "redirect:/";
        }
        model.addAttribute("f", new FormDangKy());
        model.addAttribute("loiO", new LinkedHashMap<String, String>());
        return "khach/dang-ky";
    }

    @PostMapping("/dang-ky")
    public String dangKy(@ModelAttribute("f") FormDangKy f, Model model) {
        Map<String, String> loi;
        try {
            loi = taiKhoanKhachBUS.dangKy(f);
        } catch (DataIntegrityViolationException e) {
            loi = new LinkedHashMap<>();
            loi.put("tenDangNhap", "Tên đăng nhập này đã có người dùng.");
        }
        if (!loi.isEmpty()) {
            model.addAttribute("loiO", loi);
            model.addAttribute("loi", "Bạn kiểm tra lại các ô báo đỏ bên dưới.");
            return "khach/dang-ky";
        }
        return "redirect:/dang-nhap?daDangKy";
    }

    @GetMapping("/tai-khoan")
    public String taiKhoan(@RequestParam(required = false) Long suaDiaChi, Model model) {
        KhachHang kh = batBuocKhach();
        model.addAttribute("kh", kh);
        model.addAttribute("soDiaChi", taiKhoanKhachBUS.soDiaChi(kh));
        model.addAttribute("dsDon", donHangKhachBUS.donCuaKhach(kh));
        model.addAttribute("canDanhGia", taiKhoanKhachBUS.canDanhGia(kh));
        if (suaDiaChi != null) {
            try {
                model.addAttribute("diaChiSua", taiKhoanKhachBUS.diaChiCuaKhach(kh, suaDiaChi));
            } catch (LoiNghiepVu e) {
                model.addAttribute("loi", e.getMessage());
            }
        }
        return "khach/tai-khoan";
    }

    @PostMapping("/tai-khoan/ho-so")
    public String hoSo(@RequestParam String hoTen, @RequestParam String soDienThoai, @RequestParam(required = false) String email,
                       RedirectAttributes ra) {
        try {
            taiKhoanKhachBUS.capNhatHoSo(batBuocKhach(), hoTen, soDienThoai, email);
            ra.addFlashAttribute("thongBao", "Đã lưu hồ sơ.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tai-khoan";
    }

    @PostMapping({"/tai-khoan/dia-chi", "/tai-khoan/dia-chi/{ma}"})
    public String luuDiaChi(@PathVariable(required = false) Long ma, @RequestParam String hoTenNguoiNhan, @RequestParam String soDienThoai,
                            @RequestParam String diaChi, @RequestParam(defaultValue = "true") Boolean noiThanh,
                            @RequestParam(defaultValue = "false") boolean macDinh, RedirectAttributes ra) {
        try {
            taiKhoanKhachBUS.luuDiaChi(batBuocKhach(), ma, hoTenNguoiNhan, soDienThoai, diaChi, noiThanh, macDinh);
            ra.addFlashAttribute("thongBao", ma == null ? "Đã thêm địa chỉ vào sổ." : "Đã sửa địa chỉ.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            if (ma != null) {
                return "redirect:/tai-khoan?suaDiaChi=" + ma + "#so-dia-chi";
            }
        }
        return "redirect:/tai-khoan#so-dia-chi";
    }

    @PostMapping("/tai-khoan/dia-chi/{ma}/xoa")
    public String xoaDiaChi(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            taiKhoanKhachBUS.xoaDiaChi(batBuocKhach(), ma);
            ra.addFlashAttribute("thongBao", "Đã xoá địa chỉ.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tai-khoan#so-dia-chi";
    }

    @PostMapping("/tai-khoan/dia-chi/{ma}/mac-dinh")
    public String macDinh(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            taiKhoanKhachBUS.datMacDinh(batBuocKhach(), ma);
            ra.addFlashAttribute("thongBao", "Đã đặt làm địa chỉ mặc định.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tai-khoan#so-dia-chi";
    }

    @GetMapping("/danh-gia")
    public String danhGia(@RequestParam(required = false) Long maSanPham, Model model) {
        KhachHang kh = batBuocKhach();
        model.addAttribute("canDanhGia", taiKhoanKhachBUS.canDanhGia(kh).stream()
                .filter(x -> maSanPham == null || x.getMaSanPham().equals(maSanPham)).toList());
        return "khach/danh-gia";
    }

    @PostMapping("/danh-gia")
    public String guiDanhGia(@RequestParam Long maDon, @RequestParam Long maSanPham, @RequestParam(required = false) Integer soSao,
                             @RequestParam(required = false) String noiDung, RedirectAttributes ra) {
        try {
            taiKhoanKhachBUS.danhGia(batBuocKhach(), maDon, maSanPham, soSao, noiDung);
            ra.addFlashAttribute("thongBao", "Cảm ơn bạn đã đánh giá.");
            return "redirect:/san-pham/" + maSanPham;
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/danh-gia";
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("loi", "Bạn đã đánh giá sản phẩm này cho đơn này rồi.");
            return "redirect:/danh-gia";
        }
    }

    private KhachHang batBuocKhach() {
        return taiKhoanBUS.khachHangHienTai().orElseThrow(() -> new LoiNghiepVu("Chức năng này dành cho khách hàng đã đăng nhập."));
    }
}
