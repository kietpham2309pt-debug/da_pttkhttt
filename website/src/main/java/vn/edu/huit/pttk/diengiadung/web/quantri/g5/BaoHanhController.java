package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.BaoHanhBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;

@Controller
@RequestMapping("/quan-tri/bao-hanh")
public class BaoHanhController {

    private final BaoHanhBUS baoHanhBUS;
    private final QuyenG5 quyen;

    public BaoHanhController(BaoHanhBUS baoHanhBUS, QuyenG5 quyen) {
        this.baoHanhBUS = baoHanhBUS;
        this.quyen = quyen;
    }

    @GetMapping
    public String xem(@RequestParam(required = false) String loai, @RequestParam(required = false) String q, Model model) {
        model.addAttribute("loai", loai == null ? "serial" : loai);
        model.addAttribute("q", q);
        if (q != null) {
            try {
                List<BaoHanhBUS.MayTraCuu> kq = baoHanhBUS.traCuu(loai, q);
                model.addAttribute("ketQua", kq);
            } catch (LoiNghiepVu e) {
                model.addAttribute("loi", e.getMessage());
            }
        }
        List<BaoHanhBUS.PhieuMo> mo = baoHanhBUS.dangMo();
        model.addAttribute("dangMo", mo);
        model.addAttribute("soQuaHan", mo.stream().filter(BaoHanhBUS.PhieuMo::quaHan).count());
        model.addAttribute("daTra", baoHanhBUS.daTraGanDay());
        return "quan-tri/g5/bao-hanh";
    }

    @GetMapping("/tiep-nhan")
    public String formTiepNhan(@RequestParam String serial, Model model, RedirectAttributes ra) {
        quyen.batBuoc("NV_BAN_HANG");
        try {
            BaoHanhBUS.MayTraCuu m = baoHanhBUS.mot(serial);
            PhieuBaoHanh dangMo = m.phieuDangMo();
            if (dangMo != null) {
                ra.addFlashAttribute("loi", "Máy đang bảo hành theo phiếu #" + dangMo.getMaPhieu() + ", không tạo phiếu mới.");
                return "redirect:/quan-tri/bao-hanh/" + dangMo.getMaPhieu();
            }
            moForm(model, m);
            model.addAttribute("hoTen", m.don().getHoTenNguoiNhan());
            model.addAttribute("soDienThoai", m.don().getSoDienThoaiNhan());
            model.addAttribute("ngayHenTra", LocalDate.now().plusDays(10).toString());
            model.addAttribute("loaiXuLy", BaoHanhBUS.GUI_HANG);
            return "quan-tri/g5/bao-hanh-tiep-nhan";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/bao-hanh";
        }
    }

    private void moForm(Model model, BaoHanhBUS.MayTraCuu m) {
        model.addAttribute("m", m);
        baoHanhBUS.canhBao(m).ifPresent(c -> model.addAttribute("canhBao", c));
        model.addAttribute("henSomNhat", LocalDate.now().plusDays(7).toString());
        model.addAttribute("henMuonNhat", LocalDate.now().plusDays(15).toString());
        model.addAttribute("homNay", LocalDate.now().toString());
    }

    @PostMapping("/tiep-nhan")
    public String tiepNhan(@RequestParam String serial, @RequestParam(required = false) String hoTen,
                           @RequestParam(required = false) String soDienThoai, @RequestParam(required = false) String moTaLoi,
                           @RequestParam(required = false) String tinhTrang, @RequestParam(required = false) String ngayHenTra,
                           @RequestParam(required = false) String loaiXuLy, @RequestParam(defaultValue = "false") boolean dongYCoPhi,
                           Model model, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("NV_BAN_HANG");
        try {
            PhieuBaoHanh p = baoHanhBUS.tiepNhan(serial, hoTen, soDienThoai, moTaLoi, tinhTrang, ngayHenTra, loaiXuLy, dongYCoPhi, nv);
            String tb = "Đã lập phiếu tiếp nhận bảo hành #" + p.getMaPhieu() + " (lần thứ " + p.getLanThu() + " của máy); máy chuyển Đang bảo hành. In 2 liên, giao khách 1 liên.";
            if (p.getLanThu() >= BaoHanhBUS.SO_LAN_TOI_DA) {
                tb += " Lưu ý: đây là lần bảo hành thứ " + p.getLanThu() + ", lần này sửa không được thì đổi máy mới hoặc hoàn tiền.";
            }
            ra.addFlashAttribute("thongBao", tb);
            return "redirect:/quan-tri/bao-hanh/" + p.getMaPhieu();
        } catch (LoiNghiepVu e) {
            try {
                moForm(model, baoHanhBUS.mot(serial));
            } catch (LoiNghiepVu e2) {
                ra.addFlashAttribute("loi", e2.getMessage());
                return "redirect:/quan-tri/bao-hanh";
            }
            model.addAttribute("loi", e.getMessage());
            model.addAttribute("hoTen", hoTen);
            model.addAttribute("soDienThoai", soDienThoai);
            model.addAttribute("moTaLoi", moTaLoi);
            model.addAttribute("tinhTrang", tinhTrang);
            model.addAttribute("ngayHenTra", ngayHenTra);
            model.addAttribute("loaiXuLy", loaiXuLy);
            model.addAttribute("dongYCoPhi", dongYCoPhi);
            return "quan-tri/g5/bao-hanh-tiep-nhan";
        }
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            PhieuBaoHanh p = baoHanhBUS.phieu(ma);
            model.addAttribute("p", p);
            model.addAttribute("loaiXuLy", BaoHanhBUS.loaiXuLy(p));
            model.addAttribute("nhanLoai", BaoHanhBUS.nhanLoai(BaoHanhBUS.loaiXuLy(p)));
            model.addAttribute("soNgayOHang", BaoHanhBUS.soNgayOHang(p));
            model.addAttribute("quaHan", p.getNgayTraKhach() == null && p.getNgayHenTra().isBefore(LocalDate.now()));
            model.addAttribute("homNay", LocalDate.now().toString());
            return "quan-tri/g5/bao-hanh-chi-tiet";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/bao-hanh";
        }
    }

    @PostMapping("/{ma}/gui-hang")
    public String guiHang(@PathVariable Long ma, @RequestParam(required = false) String ngay, RedirectAttributes ra) {
        quyen.batBuoc("THU_KHO");
        return lam(ma, ra, () -> baoHanhBUS.guiHang(ma, ngay), "Đã ghi ngày gửi máy tới trung tâm bảo hành hãng.");
    }

    @PostMapping("/{ma}/nhan-ve")
    public String nhanVe(@PathVariable Long ma, @RequestParam(required = false) String ngay,
                         @RequestParam(required = false) String ketQua, @RequestParam(required = false) String ghiChu,
                         RedirectAttributes ra) {
        quyen.batBuoc("THU_KHO");
        String tb = "KHONG_SUA_DUOC".equals(ketQua)
                ? "Đã ghi nhận máy về. Hãng không sửa được: báo nhân viên bán hàng chuyển sang đổi máy hoặc hoàn tiền (Xử lý đổi trả)."
                : "Đã ghi nhận máy về từ hãng; báo nhân viên bán hàng gọi khách tới nhận.";
        return lam(ma, ra, () -> baoHanhBUS.nhanVe(ma, ngay, ketQua, ghiChu), tb);
    }

    @PostMapping("/{ma}/tra-khach")
    public String traKhach(@PathVariable Long ma, @RequestParam(required = false) String ketQua, RedirectAttributes ra) {
        quyen.batBuoc("NV_BAN_HANG");
        return lam(ma, ra, () -> baoHanhBUS.traKhach(ma, ketQua), "Đã trả máy cho khách; máy chuyển lại Đã bán, ngày bán giữ nguyên.");
    }

    @PostMapping("/{ma}/doi-hen")
    public String doiHen(@PathVariable Long ma, @RequestParam(required = false) String ngayHenTra, RedirectAttributes ra) {
        quyen.batBuoc("NV_BAN_HANG");
        return lam(ma, ra, () -> baoHanhBUS.doiHen(ma, ngayHenTra), "Đã lưu ngày hẹn trả mới; ngày hẹn cũ ghi trong kết quả phiếu.");
    }

    private String lam(Long ma, RedirectAttributes ra, Runnable viec, String thongBao) {
        try {
            viec.run();
            ra.addFlashAttribute("thongBao", thongBao);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/bao-hanh/" + ma;
    }
}
