package vn.edu.huit.pttk.diengiadung.web.khach;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.GioHang;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DatHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.GiaBUS;

@Controller
public class GioHangKhachController {

    private final GioHang gio;
    private final DatHangBUS datHangBUS;

    public GioHangKhachController(GioHang gio, DatHangBUS datHangBUS) {
        this.gio = gio;
        this.datHangBUS = datHangBUS;
    }

    @GetMapping("/gio-hang")
    public String xem(Model model) {
        model.addAttribute("bang", datHangBUS.tinh(gio, true, null));
        model.addAttribute("nguongMienPhiLap", GiaBUS.NGUONG_MIEN_PHI_LAP);
        model.addAttribute("phiLapDonNho", GiaBUS.PHI_LAP_DON_NHO);
        model.addAttribute("nguongCoc", GiaBUS.NGUONG_COC);
        return "khach/gio-hang";
    }

    @PostMapping("/gio-hang/them")
    public String them(@RequestParam Long maSanPham, @RequestParam(defaultValue = "1") int soLuong,
                       @RequestParam(defaultValue = "false") boolean lapDat, @RequestParam(required = false) String muaNgay,
                       RedirectAttributes ra) {
        try {
            datHangBUS.themVaoGio(gio, maSanPham, soLuong, lapDat);
            ra.addFlashAttribute("thongBao", "Đã thêm vào giỏ hàng.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/san-pham/" + maSanPham;
        }
        return muaNgay != null ? "redirect:/dat-hang" : "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/sua")
    public String sua(@RequestParam Long maSanPham, @RequestParam(required = false) Integer soLuong,
                      @RequestParam(required = false) Integer thayDoi, RedirectAttributes ra) {
        int cu = gio.getDong().getOrDefault(maSanPham, 0);
        int moi = soLuong != null ? soLuong : cu + (thayDoi == null ? 0 : thayDoi);
        try {
            datHangBUS.suaSoLuong(gio, maSanPham, moi);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/lap-dat")
    public String lapDat(@RequestParam Long maSanPham, @RequestParam(defaultValue = "false") boolean co) {
        gio.datLapDat(maSanPham, co);
        return "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/xoa")
    public String xoa(@RequestParam Long maSanPham, @RequestParam(required = false) String ve, RedirectAttributes ra) {
        gio.xoa(maSanPham);
        ra.addFlashAttribute("thongBao", "Đã bỏ sản phẩm khỏi giỏ hàng.");
        return "dat-hang".equals(ve) && !gio.isTrong() ? "redirect:/dat-hang" : "redirect:/gio-hang";
    }
}
