package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.NhanLaiBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;

@Controller
@RequestMapping("/quan-tri/kho/nhan-lai")
public class NhanLaiController {

    private final NhanLaiBUS nhanLaiBUS;
    private final QuyenG5 quyen;

    public NhanLaiController(NhanLaiBUS nhanLaiBUS, QuyenG5 quyen) {
        this.nhanLaiBUS = nhanLaiBUS;
        this.quyen = quyen;
    }

    @GetMapping
    public String xem(Model model) {
        model.addAttribute("giaoKhongThanh", nhanLaiBUS.mayGiaoKhongThanh());
        model.addAttribute("traVe", nhanLaiBUS.mayTraVe());
        return "quan-tri/g5/nhan-lai";
    }

    @PostMapping
    public String nhan(@RequestParam String soSerial, @RequestParam(required = false) String ketQua,
                       @RequestParam(required = false) String tinhTrang, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("THU_KHO");
        try {
            ra.addFlashAttribute("thongBao", nhanLaiBUS.nhanVe(soSerial, ketQua, tinhTrang, nv));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/kho/nhan-lai";
    }
}
