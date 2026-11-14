package vn.edu.huit.pttk.diengiadung.web.khach;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DonHangKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhienKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhienKhach.YeuCauThanhToan;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.QuyenDonKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.ThongTinDon;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich;

@Controller
public class ThanhToanController {

    private final DonHangKhachBUS donHangKhachBUS;
    private final PhienKhach phien;
    private final QuyenDonKhach quyen;

    public ThanhToanController(DonHangKhachBUS donHangKhachBUS, PhienKhach phien, QuyenDonKhach quyen) {
        this.donHangKhachBUS = donHangKhachBUS;
        this.phien = phien;
        this.quyen = quyen;
    }

    @GetMapping("/thanh-toan/{maDon}")
    public String moCong(@PathVariable Long maDon, Model model, RedirectAttributes ra) {
        if (!quyen.duocXem(maDon)) {
            ra.addFlashAttribute("loi", "Bạn tra cứu đơn bằng số điện thoại và mã đơn trước khi thanh toán.");
            return "redirect:/tra-cuu-don";
        }
        ThongTinDon tt = donHangKhachBUS.thongTin(maDon);
        if (tt == null) {
            return "redirect:/tra-cuu-don";
        }
        model.addAttribute("tt", tt);
        model.addAttribute("don", tt.getDon());
        if (!tt.isCoTheThanhToan()) {
            model.addAttribute("khongCan", true);
            return "khach/thanh-toan";
        }
        YeuCauThanhToan yc = phien.capYeuCau(maDon);
        model.addAttribute("yc", yc);
        model.addAttribute("tienCoc", DonHangKhachBUS.soTienPhaiTra(tt.getDon(), tt.getDaTra(), false));
        model.addAttribute("tienHet", DonHangKhachBUS.soTienPhaiTra(tt.getDon(), tt.getDaTra(), true));
        model.addAttribute("cachChuyenKhoan", DonHangKhachBUS.CACH_CHUYEN_KHOAN);
        model.addAttribute("cachThe", DonHangKhachBUS.CACH_THE);
        return "khach/thanh-toan";
    }

    @PostMapping("/thanh-toan/{maDon}")
    public String ketQuaCong(@PathVariable Long maDon, @RequestParam(required = false) String maCong,
                             @RequestParam(defaultValue = "false") boolean traHet, @RequestParam(required = false) String cachTra,
                             @RequestParam(defaultValue = "THAT_BAI") String ketQua, RedirectAttributes ra) {
        if (!quyen.duocXem(maDon)) {
            return "redirect:/tra-cuu-don";
        }
        YeuCauThanhToan yc = phien.layYeuCau(maCong);
        if (yc == null || !yc.getMaDon().equals(maDon)) {
            ra.addFlashAttribute("loi", "Kết quả thanh toán không xác minh được, hệ thống không ghi nhận. Bạn thử thanh toán lại.");
            return "redirect:/thanh-toan/" + maDon;
        }
        if (yc.isHetHan()) {
            ra.addFlashAttribute("loi", "Yêu cầu thanh toán đã hết hạn sau 15 phút, đơn giữ nguyên. Bạn thanh toán lại.");
            return "redirect:/thanh-toan/" + maDon;
        }
        try {
            donHangKhachBUS.ghiKetQuaThanhToan(maDon, maCong, traHet, cachTra, "THANH_CONG".equals(ketQua));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/tra-cuu-don/" + maDon;
        }
        return "redirect:/thanh-toan/" + maDon + "/ket-qua?maCong=" + maCong;
    }

    @GetMapping("/thanh-toan/{maDon}/ket-qua")
    public String ketQua(@PathVariable Long maDon, @RequestParam String maCong, Model model) {
        if (!quyen.duocXem(maDon)) {
            return "redirect:/tra-cuu-don";
        }
        ThongTinDon tt = donHangKhachBUS.thongTin(maDon);
        GiaoDichThanhToan gd = tt == null ? null : tt.getGiaoDich().stream().filter(g -> maCong.equals(g.getMaGiaoDichCong())).findFirst().orElse(null);
        if (gd == null) {
            return "redirect:/thanh-toan/" + maDon;
        }
        model.addAttribute("tt", tt);
        model.addAttribute("don", tt.getDon());
        model.addAttribute("gd", gd);
        model.addAttribute("thanhCong", gd.getTrangThai() == TrangThaiGiaoDich.THANH_CONG);
        return "khach/ket-qua-thanh-toan";
    }

    @PostMapping("/thanh-toan/{maDon}/tien-mat")
    public String doiTienMat(@PathVariable Long maDon, RedirectAttributes ra) {
        if (!quyen.duocXem(maDon)) {
            return "redirect:/tra-cuu-don";
        }
        try {
            donHangKhachBUS.doiSangTienMat(maDon);
            ra.addFlashAttribute("thongBao", "Đã đổi sang trả tiền mặt khi nhận hàng.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tra-cuu-don/" + maDon;
    }
}
