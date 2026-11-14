package vn.edu.huit.pttk.diengiadung.web.quantri.g4;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.TienThuBUS;

@Controller
@RequestMapping("/quan-tri/tien-thu")
public class TienThuController {

    private final TienThuBUS tienThuBUS;

    public TienThuController(TienThuBUS tienThuBUS) {
        this.tienThuBUS = tienThuBUS;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("nhom", tienThuBUS.chuaXacNhan());
        model.addAttribute("daXacNhan", tienThuBUS.daXacNhanGanDay());
        model.addAttribute("soNgay", TienThuBUS.SO_NGAY_DA_XAC_NHAN);
        return "quan-tri/g4/tien-thu";
    }

    @PostMapping("/xac-nhan")
    public String xacNhan(@RequestParam(required = false) List<Long> maPhieu, @RequestParam(required = false) Long soNop,
                          @RequestParam(required = false) String ghiChu, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", tienThuBUS.xacNhan(maPhieu, soNop, ghiChu));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/tien-thu";
    }
}
