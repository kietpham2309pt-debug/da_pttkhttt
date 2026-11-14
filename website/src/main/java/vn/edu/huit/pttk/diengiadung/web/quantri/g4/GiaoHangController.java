package vn.edu.huit.pttk.diengiadung.web.quantri.g4;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.G4Chung;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.GiaoHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.PhanCongForm;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiViecGiao;

@Controller
@RequestMapping("/quan-tri/giao-hang")
public class GiaoHangController {

    private static final Logger LOG = LoggerFactory.getLogger(GiaoHangController.class);

    private final GiaoHangBUS giaoBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public GiaoHangController(GiaoHangBUS giaoBUS, TaiKhoanBUS taiKhoanBUS) {
        this.giaoBUS = giaoBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    private void batBuocQuanLy() {
        if (!taiKhoanBUS.coVai("QUAN_LY")) {
            throw new AccessDeniedException("Chỉ quản lý cửa hàng phân công giao hàng");
        }
    }

    @GetMapping
    public String trangChinh(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                             @RequestParam(required = false) Long nv, Model model) {
        LocalDate n = ngay == null ? LocalDate.now() : ngay;
        boolean quanLy = taiKhoanBUS.coVai("QUAN_LY");
        boolean giaoHang = taiKhoanBUS.coVai("NV_GIAO_HANG");
        boolean banHang = taiKhoanBUS.coVai("NV_BAN_HANG");
        model.addAttribute("quanLy", quanLy);
        model.addAttribute("giaoHang", giaoHang);
        model.addAttribute("banHang", banHang);
        model.addAttribute("ngay", n);
        model.addAttribute("nv", nv);
        model.addAttribute("dsNhanVien", giaoBUS.nhanVienGiaoHang());
        model.addAttribute("lich", giaoBUS.lich(n, nv));
        if (quanLy) {
            model.addAttribute("donCho", giaoBUS.donChoPhanCong());
        }
        if (giaoHang) {
            model.addAttribute("viecCuaToi", giaoBUS.viecCuaToi());
            model.addAttribute("viecDaXong", giaoBUS.viecDaXongCuaToi());
        }
        if (banHang) {
            model.addAttribute("vanChuyen", giaoBUS.vanChuyenDangCho());
        }
        return "quan-tri/g4/giao-hang";
    }

    private String hienForm(PhanCongForm f, Model model) {
        model.addAttribute("f", f);
        model.addAttribute("don", giaoBUS.donDayDu(f.getMaDon()));
        model.addAttribute("phieuCu", giaoBUS.phieuCuaDon(f.getMaDon()));
        model.addAttribute("dsNhanVien", giaoBUS.nhanVienGiaoHang());
        model.addAttribute("dsLoai", LoaiViecGiao.values());
        if (f.getNgayHen() != null) {
            model.addAttribute("lichNgay", giaoBUS.lich(f.getNgayHen(), null));
        }
        return "quan-tri/g4/phan-cong";
    }

    @GetMapping("/phan-cong")
    public String formPhanCong(@RequestParam Long don, Model model, RedirectAttributes ra) {
        batBuocQuanLy();
        try {
            return hienForm(giaoBUS.formMoi(don), model);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/giao-hang";
        }
    }

    @GetMapping("/phieu/{ma}/doi-lich")
    public String formDoiLich(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        batBuocQuanLy();
        try {
            return hienForm(giaoBUS.formSua(ma), model);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/giao-hang/phieu/" + ma;
        }
    }

    @PostMapping("/phan-cong")
    public String phanCong(@ModelAttribute PhanCongForm f, BindingResult loiNhap, Model model, RedirectAttributes ra) {
        batBuocQuanLy();
        if (f.getMaDon() == null) {
            ra.addFlashAttribute("loi", "Chưa chọn đơn để phân công");
            return "redirect:/quan-tri/giao-hang";
        }
        try {
            GiaoHangBUS.KetQuaPhanCong kq = giaoBUS.phanCong(f);
            if (!kq.daLuu()) {
                model.addAttribute("kq", kq);
                return hienForm(f, model);
            }
            String tb = (f.getMaPhieu() == null ? "Đã lưu lịch phiếu PG" : "Đã đổi lịch phiếu PG") + kq.maPhieu()
                    + " cho đơn " + G4Chung.maDon(f.getMaDon()) + ".";
            if (!kq.trung().isEmpty()) {
                tb += " Lưu khi trùng giờ, đã ghi lý do.";
            }
            ra.addFlashAttribute("thongBao", tb);
            return "redirect:/quan-tri/giao-hang?ngay=" + f.getNgayHen();
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            return hienForm(f, model);
        }
    }

    @GetMapping("/phieu/{ma}")
    public String chiTietPhieu(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("ct", giaoBUS.chiTietPhieu(ma));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/giao-hang";
        }
        model.addAttribute("quanLy", taiKhoanBUS.coVai("QUAN_LY"));
        return "quan-tri/g4/phieu-giao";
    }

    @PostMapping("/phieu/{ma}/bien-ban")
    public String bienBan(@PathVariable Long ma, @RequestParam(required = false) String ketQuaChayThu,
                          @RequestParam(required = false) String tinhTrangCongTrinh, @RequestParam(required = false) String phatSinh,
                          @RequestParam(required = false) Long phiLapDat, @RequestParam(required = false) Long phiVatTu,
                          @RequestParam(defaultValue = "false") boolean khachDaKy, RedirectAttributes ra) {
        try {
            giaoBUS.luuBienBan(ma, ketQuaChayThu, tinhTrangCongTrinh, phatSinh, phiLapDat, phiVatTu, khachDaKy);
            ra.addFlashAttribute("thongBao", "Đã lưu biên bản lắp đặt" + (khachDaKy ? " (khách đã ký)." : ". Khách chưa ký: chưa báo hoàn thành được."));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/giao-hang/phieu/" + ma;
    }

    @PostMapping("/phieu/{ma}/da-giao")
    public String daGiao(@PathVariable Long ma, @RequestParam(required = false) Long tienThu, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", giaoBUS.daGiao(ma, tienThu));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        } catch (RuntimeException e) {
            LOG.error("Lỗi ghi đã giao", e);
            ra.addFlashAttribute("loi", "Chưa lưu được, thử lại");
        }
        return "redirect:/quan-tri/giao-hang/phieu/" + ma;
    }

    @PostMapping("/phieu/{ma}/khong-thanh-cong")
    public String khongThanhCong(@PathVariable Long ma, @RequestParam(required = false) String lyDo,
                                 @RequestParam(defaultValue = "false") boolean henGiaoLai, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", giaoBUS.khongThanhCong(ma, lyDo, henGiaoLai));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/giao-hang/phieu/" + ma;
    }
}
