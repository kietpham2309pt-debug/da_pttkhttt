package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.PhieuXuatBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;

@Controller
@RequestMapping("/quan-tri/kho/phieu-xuat")
public class PhieuXuatController {

    private final PhieuXuatBUS phieuXuatBUS;
    private final QuyenG5 quyen;

    public PhieuXuatController(PhieuXuatBUS phieuXuatBUS, QuyenG5 quyen) {
        this.phieuXuatBUS = phieuXuatBUS;
        this.quyen = quyen;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("choXuat", phieuXuatBUS.donChoXuat());
        model.addAttribute("doiMay", phieuXuatBUS.yeuCauDoiMayChoXuat());
        model.addAttribute("ds", phieuXuatBUS.danhSach());
        return "quan-tri/g5/phieu-xuat";
    }

    @GetMapping("/lap")
    public String lap(@RequestParam(defaultValue = PhieuXuatBUS.LOAI_DON) String loai, @RequestParam Long ma,
                      Model model, RedirectAttributes ra) {
        quyen.batBuoc("THU_KHO");
        try {
            PhieuXuatBUS.PhieuXuatNhap f = phieuXuatBUS.taoPhieu(loai, ma);
            model.addAttribute("f", f);
            Set<String> chon = new LinkedHashSet<>();
            f.dong().forEach(d -> chon.addAll(d.goiY()));
            model.addAttribute("chon", chon);
            if (!f.duMay()) {
                PhieuXuatBUS.DongXuat d = f.dong().stream().filter(PhieuXuatBUS.DongXuat::thieu).findFirst().orElseThrow();
                model.addAttribute("loi", "Mẫu " + d.model() + " còn " + d.coSan().size() + ", đơn cần " + d.canXuat()
                        + ". Không lưu được phiếu xuất; báo nhân viên bán hàng gọi khách hẹn lại hoặc lập đơn đặt hàng nhà cung cấp.");
            }
            return "quan-tri/g5/phieu-xuat-lap";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/phieu-xuat";
        }
    }

    @PostMapping("/lap")
    public String luu(@RequestParam String loai, @RequestParam Long ma, @RequestParam MultiValueMap<String, String> p,
                      Model model, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("THU_KHO");
        List<String> serial = new ArrayList<>(p.getOrDefault("serial", List.of()));
        String quet = p.getFirst("serialQuet");
        if (quet != null) {
            for (String s : quet.split("[\\r\\n,;]+")) {
                if (!s.isBlank()) {
                    serial.add(s);
                }
            }
        }
        try {
            Long maPhieu = phieuXuatBUS.luu(loai, ma, serial, nv);
            ra.addFlashAttribute("thongBao", PhieuXuatBUS.LOAI_DOI.equals(loai)
                    ? "Đã lưu phiếu xuất #" + maPhieu + " (xuất đổi máy); máy mới đã giao cho khách, đơn giữ trạng thái Hoàn thành."
                    : "Đã lưu phiếu xuất #" + maPhieu + "; các máy chuyển Đã xuất, đơn chuyển Đang giao. Phiếu đã khoá.");
            return "redirect:/quan-tri/kho/phieu-xuat/" + maPhieu;
        } catch (LoiNghiepVu e) {
            try {
                PhieuXuatBUS.PhieuXuatNhap f = phieuXuatBUS.taoPhieu(loai, ma);
                model.addAttribute("f", f);
                Set<String> chon = new LinkedHashSet<>();
                serial.forEach(s -> chon.add(s.trim().toUpperCase()));
                model.addAttribute("chon", chon);
                model.addAttribute("loi", e.getMessage());
                return "quan-tri/g5/phieu-xuat-lap";
            } catch (LoiNghiepVu e2) {
                ra.addFlashAttribute("loi", e.getMessage());
                return "redirect:/quan-tri/kho/phieu-xuat";
            }
        }
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("x", phieuXuatBUS.xem(ma));
            return "quan-tri/g5/phieu-xuat-chi-tiet";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/phieu-xuat";
        }
    }
}
