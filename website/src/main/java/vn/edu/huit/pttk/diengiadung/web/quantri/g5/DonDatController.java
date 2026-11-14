package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.DonDatBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.NhaCungCapBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.TonKhoBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.DonDatHangNCC;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonDat;

@Controller
@RequestMapping("/quan-tri/kho/don-dat")
public class DonDatController {

    private final DonDatBUS donDatBUS;
    private final NhaCungCapBUS nhaCungCapBUS;
    private final TonKhoBUS tonKhoBUS;
    private final QuyenG5 quyen;

    public DonDatController(DonDatBUS donDatBUS, NhaCungCapBUS nhaCungCapBUS, TonKhoBUS tonKhoBUS, QuyenG5 quyen) {
        this.donDatBUS = donDatBUS;
        this.nhaCungCapBUS = nhaCungCapBUS;
        this.tonKhoBUS = tonKhoBUS;
        this.quyen = quyen;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String trangThai, Model model) {
        TrangThaiDonDat tt = null;
        if (trangThai != null && !trangThai.isBlank()) {
            try {
                tt = TrangThaiDonDat.valueOf(trangThai);
            } catch (IllegalArgumentException e) {
                tt = null;
            }
        }
        model.addAttribute("ds", donDatBUS.danhSach(tt));
        model.addAttribute("trangThai", tt);
        model.addAttribute("cacTrangThai", TrangThaiDonDat.values());
        return "quan-tri/g5/don-dat";
    }

    @GetMapping("/lap")
    public String lap(Model model) {
        quyen.batBuoc("THU_KHO");
        moFormLap(model, null, Map.of(), null);
        return "quan-tri/g5/don-dat-lap";
    }

    private void moFormLap(Model model, Long maNcc, Map<Long, String> soLuong, String ghiChu) {
        List<TonKhoBUS.DongTon> ton = tonKhoBUS.tonTheoMau();
        Map<Long, String> sl = new LinkedHashMap<>(soLuong);
        if (soLuong.isEmpty()) {
            for (TonKhoBUS.DongTon d : ton) {
                if (d.duoiToiThieu()) {
                    sl.put(d.maSanPham(), String.valueOf(Math.max(1, TonKhoBUS.TON_TOI_THIEU + 3 - d.trongKho())));
                }
            }
        }
        List<TonKhoBUS.DongTon> sapXep = new java.util.ArrayList<>(ton);
        sapXep.sort((a, b) -> Boolean.compare(b.duoiToiThieu(), a.duoiToiThieu()));
        model.addAttribute("ton", sapXep);
        model.addAttribute("soLuong", sl);
        model.addAttribute("dsNcc", nhaCungCapBUS.dangHopTac());
        model.addAttribute("maNcc", maNcc);
        model.addAttribute("ghiChu", ghiChu);
        model.addAttribute("tonToiThieu", TonKhoBUS.TON_TOI_THIEU);
    }

    static Map<Long, String> docSoLuong(MultiValueMap<String, String> p) {
        Map<Long, String> kq = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : p.entrySet()) {
            if (e.getKey().startsWith("sl_") && !e.getValue().isEmpty()) {
                try {
                    kq.put(Long.parseLong(e.getKey().substring(3)), e.getValue().get(0));
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
        }
        return kq;
    }

    @PostMapping("/lap")
    public String luu(@RequestParam MultiValueMap<String, String> p, Model model, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("THU_KHO");
        Map<Long, String> sl = docSoLuong(p);
        Long maNcc = null;
        try {
            String s = p.getFirst("maNhaCungCap");
            maNcc = s == null || s.isBlank() ? null : Long.parseLong(s);
        } catch (NumberFormatException e) {
            maNcc = null;
        }
        try {
            DonDatHangNCC d = donDatBUS.lapDeXuat(maNcc, sl, p.getFirst("ghiChu"), nv);
            ra.addFlashAttribute("thongBao", "Đã lập đề xuất đặt hàng #" + d.getMaDonDat() + ", chờ quản lý cửa hàng xin ý kiến chủ và gửi nhà cung cấp.");
            return "redirect:/quan-tri/kho/don-dat/" + d.getMaDonDat();
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            moFormLap(model, maNcc, sl, p.getFirst("ghiChu"));
            return "quan-tri/g5/don-dat-lap";
        }
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("x", donDatBUS.xem(ma));
            model.addAttribute("dsNcc", nhaCungCapBUS.dangHopTac());
            return "quan-tri/g5/don-dat-chi-tiet";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/don-dat";
        }
    }

    @PostMapping("/{ma}/gui")
    public String gui(@PathVariable Long ma, @RequestParam MultiValueMap<String, String> p, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("QUAN_LY");
        try {
            String s = p.getFirst("maNhaCungCap");
            Long maNcc = s == null || s.isBlank() ? null : Long.parseLong(s);
            donDatBUS.guiNhaCungCap(ma, maNcc, docSoLuong(p), p.getFirst("yKienChu"), nv);
            ra.addFlashAttribute("thongBao", "Đã gửi đơn đặt hàng #" + ma + " cho nhà cung cấp; thủ kho chờ nhận hàng theo đơn này.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/kho/don-dat/" + ma;
    }

    @PostMapping("/{ma}/huy")
    public String huy(@PathVariable Long ma, @RequestParam(required = false) String lyDo, RedirectAttributes ra) {
        quyen.batBuoc("THU_KHO", "QUAN_LY");
        try {
            donDatBUS.huy(ma, lyDo);
            ra.addFlashAttribute("thongBao", "Đã huỷ đơn đặt hàng #" + ma + ".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/kho/don-dat/" + ma;
    }
}
