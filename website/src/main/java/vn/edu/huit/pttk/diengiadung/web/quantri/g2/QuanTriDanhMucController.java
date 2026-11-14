package vn.edu.huit.pttk.diengiadung.web.quantri.g2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriDanhMucBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.ThuongHieu;

@Controller
public class QuanTriDanhMucController {

    private final QuanTriDanhMucBUS bus;

    public QuanTriDanhMucController(QuanTriDanhMucBUS bus) {
        this.bus = bus;
    }

    @GetMapping("/quan-tri/danh-muc")
    public String danhSach(Model model) {
        model.addAttribute("dsDanhMuc", bus.danhMuc());
        model.addAttribute("dsThuongHieu", bus.thuongHieu());
        model.addAttribute("soSpDanhMuc", bus.soSanPhamTheoDanhMuc());
        model.addAttribute("soSpThuongHieu", bus.soSanPhamTheoThuongHieu());
        return "quan-tri/g2/danh-muc";
    }

    @GetMapping("/quan-tri/danh-muc/them")
    public String themDanhMuc(Model model) {
        DanhMuc dm = new DanhMuc();
        dm.setThuTu(bus.danhMuc().size() + 1);
        return formDanhMuc(model, dm.getMaDanhMuc(), dm.getTenDanhMuc(), String.valueOf(dm.getThuTu()), true);
    }

    @GetMapping("/quan-tri/danh-muc/{ma}/sua")
    public String suaDanhMuc(@PathVariable String ma, Model model, RedirectAttributes ra) {
        try {
            DanhMuc dm = bus.timDanhMuc(ma);
            return formDanhMuc(model, dm.getMaDanhMuc(), dm.getTenDanhMuc(), String.valueOf(dm.getThuTu()), false);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/danh-muc";
        }
    }

    @PostMapping("/quan-tri/danh-muc/luu")
    public String luuDanhMuc(@RequestParam(required = false) String maDanhMuc, @RequestParam(required = false) String tenDanhMuc,
                             @RequestParam(required = false) String thuTu, @RequestParam(defaultValue = "false") boolean themMoi,
                             Model model, RedirectAttributes ra) {
        try {
            Integer tt;
            try {
                tt = Integer.valueOf(thuTu == null ? "" : thuTu.trim());
            } catch (NumberFormatException e) {
                tt = null;
            }
            bus.luuDanhMuc(maDanhMuc, tenDanhMuc, tt, themMoi);
            ra.addFlashAttribute("thongBao", "Đã lưu danh mục \"" + tenDanhMuc.trim() + "\".");
            return "redirect:/quan-tri/danh-muc";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            return formDanhMuc(model, maDanhMuc, tenDanhMuc, thuTu, themMoi);
        }
    }

    @PostMapping("/quan-tri/danh-muc/{ma}/xoa")
    public String xoaDanhMuc(@PathVariable String ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã xoá danh mục \"" + bus.xoaDanhMuc(ma) + "\".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/danh-muc";
    }

    @GetMapping("/quan-tri/thuong-hieu/them")
    public String themThuongHieu(Model model) {
        return formThuongHieu(model, null, "", "");
    }

    @GetMapping("/quan-tri/thuong-hieu/{ma}/sua")
    public String suaThuongHieu(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            ThuongHieu th = bus.timThuongHieu(ma);
            return formThuongHieu(model, th.getMaThuongHieu(), th.getTenThuongHieu(), th.getXuatXu());
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/danh-muc";
        }
    }

    @PostMapping("/quan-tri/thuong-hieu/luu")
    public String luuThuongHieu(@RequestParam(required = false) Long maThuongHieu, @RequestParam(required = false) String tenThuongHieu,
                                @RequestParam(required = false) String xuatXu, Model model, RedirectAttributes ra) {
        try {
            bus.luuThuongHieu(maThuongHieu, tenThuongHieu, xuatXu);
            ra.addFlashAttribute("thongBao", "Đã lưu thương hiệu \"" + tenThuongHieu.trim() + "\".");
            return "redirect:/quan-tri/danh-muc";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            return formThuongHieu(model, maThuongHieu, tenThuongHieu, xuatXu);
        }
    }

    @PostMapping("/quan-tri/thuong-hieu/{ma}/xoa")
    public String xoaThuongHieu(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã xoá thương hiệu \"" + bus.xoaThuongHieu(ma) + "\".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/danh-muc";
    }

    private String formDanhMuc(Model model, String ma, String ten, String thuTu, boolean themMoi) {
        model.addAttribute("maDanhMuc", ma);
        model.addAttribute("tenDanhMuc", ten);
        model.addAttribute("thuTu", thuTu);
        model.addAttribute("themMoi", themMoi);
        datLoiO(model);
        return "quan-tri/g2/danh-muc-form";
    }

    private String formThuongHieu(Model model, Long ma, String ten, String xuatXu) {
        model.addAttribute("maThuongHieu", ma);
        model.addAttribute("tenThuongHieu", ten);
        model.addAttribute("xuatXu", xuatXu);
        datLoiO(model);
        return "quan-tri/g2/thuong-hieu-form";
    }

    private static void datLoiO(Model model) {
        if (!model.containsAttribute("loiO")) {
            Map<String, String> m = new HashMap<>();
            model.addAttribute("loiO", m);
        }
    }
}
