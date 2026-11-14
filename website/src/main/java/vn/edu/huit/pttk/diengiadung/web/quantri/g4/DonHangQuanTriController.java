package vn.edu.huit.pttk.diengiadung.web.quantri.g4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.DonHangQuanTriBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.G4Chung;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;

@Controller
@RequestMapping("/quan-tri/don-hang")
public class DonHangQuanTriController {

    private static final String TAT_CA = "TAT_CA";
    private static final Logger LOG = LoggerFactory.getLogger(DonHangQuanTriController.class);

    private final DonHangQuanTriBUS donBUS;

    public DonHangQuanTriController(DonHangQuanTriBUS donBUS) {
        this.donBUS = donBUS;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String trangThai, @RequestParam(required = false) String tuKhoa, Model model) {
        String chon = trangThai;
        if (chon == null || chon.isBlank()) {
            chon = G4Chung.rong(tuKhoa) != null ? TAT_CA : TrangThaiDonHang.CHO_XAC_NHAN.name();
        }
        TrangThaiDonHang tt = null;
        if (!TAT_CA.equals(chon)) {
            try {
                tt = TrangThaiDonHang.valueOf(chon);
            } catch (IllegalArgumentException e) {
                chon = TAT_CA;
            }
        }
        model.addAttribute("ds", donBUS.danhSach(tt, tuKhoa));
        model.addAttribute("dem", donBUS.demTheoTrangThai());
        model.addAttribute("dsTrangThai", TrangThaiDonHang.values());
        model.addAttribute("chon", chon);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("phutToiDa", DonHangQuanTriBUS.PHUT_GOI_TOI_DA);
        return "quan-tri/g4/don-hang";
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("ct", donBUS.chiTiet(ma));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/don-hang";
        }
        model.addAttribute("lyDoHuy", DonHangQuanTriBUS.LY_DO_HUY);
        model.addAttribute("soLanGoiToiDa", DonHangQuanTriBUS.SO_LAN_GOI_TOI_DA);
        model.addAttribute("phutToiDa", DonHangQuanTriBUS.PHUT_GOI_TOI_DA);
        return "quan-tri/g4/don-hang-chi-tiet";
    }

    @PostMapping("/{ma}/ghi-chu")
    public String luuGhiChu(@PathVariable Long ma, @RequestParam(required = false) String ghiChu, @RequestParam(required = false) String diaChiGiao,
                            @RequestParam(defaultValue = "false") boolean noiThanh, RedirectAttributes ra) {
        try {
            donBUS.luuGhiChu(ma, ghiChu, diaChiGiao, noiThanh);
            ra.addFlashAttribute("thongBao", "Đã lưu ghi chú và địa chỉ giao của đơn " + G4Chung.maDon(ma));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/don-hang/" + ma;
    }

    @PostMapping("/{ma}/goi-lai")
    public String goiLai(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo, RedirectAttributes ra) {
        try {
            int lan = donBUS.goiLaiSau(ma, trangThaiLucMo);
            String tb = "Đã ghi gọi lại lần " + lan + " cho đơn " + G4Chung.maDon(ma) + ", đơn giữ Chờ xác nhận.";
            if (lan >= DonHangQuanTriBUS.SO_LAN_GOI_TOI_DA) {
                tb += " Đã gọi đủ " + DonHangQuanTriBUS.SO_LAN_GOI_TOI_DA + " lần: có thể huỷ với lý do Không liên lạc được.";
            }
            ra.addFlashAttribute("thongBao", tb);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/don-hang/" + ma;
    }

    @PostMapping("/{ma}/xac-nhan")
    public String xacNhan(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo, RedirectAttributes ra) {
        try {
            donBUS.xacNhan(ma, trangThaiLucMo);
            ra.addFlashAttribute("thongBao", "Đã xác nhận đơn " + G4Chung.maDon(ma)
                    + ". Đơn vào danh sách chờ xếp lịch giao lắp và chờ xuất kho.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        } catch (RuntimeException e) {
            LOG.error("Lỗi lưu đơn hàng", e);
            ra.addFlashAttribute("loi", "Chưa lưu được, thử lại");
        }
        return "redirect:/quan-tri/don-hang/" + ma;
    }

    @PostMapping("/{ma}/huy")
    public String huy(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo, @RequestParam(required = false) String loaiLyDo,
                      @RequestParam(required = false) String chiTietLyDo, RedirectAttributes ra) {
        try {
            long hoan = donBUS.huy(ma, trangThaiLucMo, loaiLyDo, chiTietLyDo);
            String tb = "Đã huỷ đơn " + G4Chung.maDon(ma) + ".";
            if (hoan > 0) {
                tb += " Đơn đã nhận cọc: hoàn đủ " + G4Chung.tien(hoan) + " cho khách (đã ghi giao dịch hoàn tiền).";
            }
            ra.addFlashAttribute("thongBao", tb);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        } catch (RuntimeException e) {
            LOG.error("Lỗi lưu đơn hàng", e);
            ra.addFlashAttribute("loi", "Chưa lưu được, thử lại");
        }
        return "redirect:/quan-tri/don-hang/" + ma;
    }
}
