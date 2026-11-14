package vn.edu.huit.pttk.diengiadung.web.quantri.g2;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.BieuMauKhuyenMai;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriKhuyenMaiBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriSanPhamBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;

@Controller
@RequestMapping("/quan-tri/khuyen-mai")
public class QuanTriKhuyenMaiController {

    private final QuanTriKhuyenMaiBUS bus;
    private final QuanTriSanPhamBUS sanPhamBUS;

    public QuanTriKhuyenMaiController(QuanTriKhuyenMaiBUS bus, QuanTriSanPhamBUS sanPhamBUS) {
        this.bus = bus;
        this.sanPhamBUS = sanPhamBUS;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsKhuyenMai", bus.danhSach());
        return "quan-tri/g2/khuyen-mai";
    }

    @GetMapping("/them")
    public String them(Model model) {
        BieuMauKhuyenMai bm = new BieuMauKhuyenMai();
        bm.setNgayBatDau(LocalDate.now());
        bm.setNgayKetThuc(LocalDate.now().plusDays(14));
        return bieuMau(model, bm, QuanTriKhuyenMaiBUS.SAP_CHAY);
    }

    @GetMapping("/{ma}/sua")
    public String sua(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            KhuyenMai km = bus.tim(ma);
            return bieuMau(model, bus.bieuMauSua(ma), QuanTriKhuyenMaiBUS.giaiDoan(km));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/khuyen-mai";
        }
    }

    @PostMapping("/luu")
    public String luu(@ModelAttribute("bm") BieuMauKhuyenMai bm, BindingResult kq, Model model, RedirectAttributes ra) {
        Map<String, String> loiChuyenDoi = new HashMap<>();
        kq.getFieldErrors().forEach(f -> loiChuyenDoi.putIfAbsent(f.getField(),
                f.getField().startsWith("ngay") ? "Ngày không hợp lệ." : "Chỉ nhập số."));
        try {
            boolean themMoi = bm.getMaKhuyenMai() == null;
            KhuyenMai km = bus.luu(bm, loiChuyenDoi);
            ra.addFlashAttribute("thongBao", (themMoi ? "Đã lưu chương trình \"" : "Đã cập nhật chương trình \"") + km.getTenKhuyenMai() + "\".");
            return "redirect:/quan-tri/khuyen-mai";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            String giaiDoan = bm.getMaKhuyenMai() == null ? QuanTriKhuyenMaiBUS.SAP_CHAY : QuanTriKhuyenMaiBUS.giaiDoan(bus.tim(bm.getMaKhuyenMai()));
            if (bm.getMaKhuyenMai() != null && QuanTriKhuyenMaiBUS.DANG_CHAY.equals(giaiDoan)) {
                BieuMauKhuyenMai cu = bus.bieuMauSua(bm.getMaKhuyenMai());
                cu.setTenKhuyenMai(bm.getTenKhuyenMai());
                cu.setNgayKetThuc(bm.getNgayKetThuc());
                cu.setLyDoDuoiGiaNhap(bm.getLyDoDuoiGiaNhap());
                bm = cu;
            }
            return bieuMau(model, bm, giaiDoan);
        }
    }

    @PostMapping("/{ma}/tam-dung")
    public String tamDung(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã tạm dừng \"" + bus.tamDung(ma) + "\".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/khuyen-mai";
    }

    @PostMapping("/{ma}/chay-lai")
    public String chayLai(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã chạy lại \"" + bus.chayLai(ma) + "\".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/khuyen-mai";
    }

    private String bieuMau(Model model, BieuMauKhuyenMai bm, String giaiDoan) {
        model.addAttribute("bm", bm);
        model.addAttribute("giaiDoan", giaiDoan);
        model.addAttribute("chiSuaHan", QuanTriKhuyenMaiBUS.DANG_CHAY.equals(giaiDoan) && bm.getMaKhuyenMai() != null);
        model.addAttribute("dsSanPham", sanPhamBUS.sanPhamChoKhuyenMai());
        if (!model.containsAttribute("loiO")) {
            model.addAttribute("loiO", new HashMap<String, String>());
        }
        return "quan-tri/g2/khuyen-mai-form";
    }
}
