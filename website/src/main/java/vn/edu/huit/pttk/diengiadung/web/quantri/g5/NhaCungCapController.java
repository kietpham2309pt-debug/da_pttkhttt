package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.NhaCungCapBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhaCungCap;

@Controller
@RequestMapping("/quan-tri/kho/nha-cung-cap")
public class NhaCungCapController {

    private final NhaCungCapBUS nhaCungCapBUS;

    public NhaCungCapController(NhaCungCapBUS nhaCungCapBUS) {
        this.nhaCungCapBUS = nhaCungCapBUS;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("ds", nhaCungCapBUS.tatCa());
        return "quan-tri/g5/nha-cung-cap";
    }

    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("ncc", new NhaCungCap());
        return "quan-tri/g5/nha-cung-cap-form";
    }

    @GetMapping("/{ma}/sua")
    public String sua(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("ncc", nhaCungCapBUS.tim(ma));
            return "quan-tri/g5/nha-cung-cap-form";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/nha-cung-cap";
        }
    }

    @PostMapping("/luu")
    public String luu(@RequestParam(required = false) Long ma, @RequestParam String tenNhaCungCap,
                      @RequestParam String soDienThoai, @RequestParam(required = false) String email,
                      @RequestParam(required = false) String diaChi, Model model, RedirectAttributes ra) {
        try {
            NhaCungCap n = nhaCungCapBUS.luu(ma, tenNhaCungCap, soDienThoai, email, diaChi);
            ra.addFlashAttribute("thongBao", (ma == null ? "Đã thêm" : "Đã cập nhật") + " nhà cung cấp \"" + n.getTenNhaCungCap() + "\".");
            return "redirect:/quan-tri/kho/nha-cung-cap";
        } catch (LoiNghiepVu e) {
            NhaCungCap n = new NhaCungCap();
            n.setMaNhaCungCap(ma);
            n.setTenNhaCungCap(tenNhaCungCap);
            n.setSoDienThoai(soDienThoai);
            n.setEmail(email);
            n.setDiaChi(diaChi);
            model.addAttribute("ncc", n);
            model.addAttribute("loi", e.getMessage());
            return "quan-tri/g5/nha-cung-cap-form";
        }
    }

    @PostMapping("/{ma}/ngung")
    public String ngung(@PathVariable Long ma, RedirectAttributes ra) {
        return doiHopTac(ma, false, ra);
    }

    @PostMapping("/{ma}/hop-tac")
    public String hopTac(@PathVariable Long ma, RedirectAttributes ra) {
        return doiHopTac(ma, true, ra);
    }

    private String doiHopTac(Long ma, boolean co, RedirectAttributes ra) {
        try {
            nhaCungCapBUS.doiHopTac(ma, co);
            ra.addFlashAttribute("thongBao", co ? "Đã hợp tác lại với nhà cung cấp." : "Đã chuyển nhà cung cấp sang ngừng hợp tác; không lập đơn đặt hàng mới với nhà cung cấp này.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/kho/nha-cung-cap";
    }

    @PostMapping("/{ma}/xoa")
    public String xoa(@PathVariable Long ma, RedirectAttributes ra) {
        try {
            nhaCungCapBUS.xoa(ma);
            ra.addFlashAttribute("thongBao", "Đã xoá nhà cung cấp.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/kho/nha-cung-cap";
    }
}
