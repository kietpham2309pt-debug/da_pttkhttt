package vn.edu.huit.pttk.diengiadung.web.quantri.g4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g4.DoiTraBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.HinhThucDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiLyDoDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra;

@Controller
@RequestMapping("/quan-tri/doi-tra")
public class DoiTraController {

    private static final Logger LOG = LoggerFactory.getLogger(DoiTraController.class);
    private static final String TAT_CA = "TAT_CA";

    private final DoiTraBUS doiTraBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public DoiTraController(DoiTraBUS doiTraBUS, TaiKhoanBUS taiKhoanBUS) {
        this.doiTraBUS = doiTraBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    private void batBuocVai(String vai, String viec) {
        if (!taiKhoanBUS.coVai(vai)) {
            throw new AccessDeniedException(viec);
        }
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String trangThai, Model model) {
        String chon = trangThai;
        if (chon == null || chon.isBlank()) {
            if (taiKhoanBUS.coVai("QUAN_LY")) {
                chon = TrangThaiDoiTra.CHO_DUYET.name();
            } else if (taiKhoanBUS.coVai("KE_TOAN")) {
                chon = TrangThaiDoiTra.DA_DUYET.name();
            } else {
                chon = TAT_CA;
            }
        }
        TrangThaiDoiTra tt = null;
        if (!TAT_CA.equals(chon)) {
            try {
                tt = TrangThaiDoiTra.valueOf(chon);
            } catch (IllegalArgumentException e) {
                chon = TAT_CA;
            }
        }
        model.addAttribute("ds", doiTraBUS.danhSach(tt));
        model.addAttribute("dem", doiTraBUS.demTheoTrangThai());
        model.addAttribute("dsTrangThai", TrangThaiDoiTra.values());
        model.addAttribute("chon", chon);
        model.addAttribute("banHang", taiKhoanBUS.coVai("NV_BAN_HANG"));
        return "quan-tri/g4/doi-tra";
    }

    private String hienFormTao(Long don, String tuKhoa, Model model) {
        model.addAttribute("tuKhoa", tuKhoa);
        if (don != null) {
            model.addAttribute("dd", doiTraBUS.donDeTao(don));
        } else if (tuKhoa != null && !tuKhoa.isBlank()) {
            model.addAttribute("ketQua", doiTraBUS.timDon(tuKhoa));
        }
        model.addAttribute("dsHinhThuc", HinhThucDoiTra.values());
        model.addAttribute("dsLoaiLyDo", LoaiLyDoDoiTra.values());
        model.addAttribute("hanDoiY", DoiTraBUS.HAN_DOI_Y);
        model.addAttribute("hanLoi", DoiTraBUS.HAN_LOI_NHA_SAN_XUAT);
        return "quan-tri/g4/doi-tra-tao";
    }

    @GetMapping("/tao")
    public String formTao(@RequestParam(required = false) String tuKhoa, @RequestParam(required = false) Long don, Model model,
                          RedirectAttributes ra) {
        batBuocVai("NV_BAN_HANG", "Chỉ nhân viên bán hàng tiếp nhận yêu cầu đổi trả");
        if (!model.containsAttribute("nhap")) {
            model.addAttribute("nhap", new HashMap<String, Object>());
        }
        try {
            return hienFormTao(don, tuKhoa, model);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/doi-tra/tao" + (tuKhoa == null ? "" : "?tuKhoa=" + java.net.URLEncoder.encode(tuKhoa, java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @PostMapping("/tao")
    public String tao(@RequestParam Long don, @RequestParam(required = false) List<String> serial,
                      @RequestParam(required = false) HinhThucDoiTra hinhThuc, @RequestParam(required = false) LoaiLyDoDoiTra loaiLyDo,
                      @RequestParam(required = false) String lyDo, @RequestParam(required = false) String benNgoai,
                      @RequestParam(defaultValue = "false") boolean conSeal, @RequestParam(defaultValue = "false") boolean daLap,
                      @RequestParam(defaultValue = "false") boolean xinNgoaiLe, Model model, RedirectAttributes ra) {
        batBuocVai("NV_BAN_HANG", "Chỉ nhân viên bán hàng tiếp nhận yêu cầu đổi trả");
        Map<String, Object> nhap = new HashMap<>();
        nhap.put("serial", serial == null ? List.of() : serial);
        nhap.put("hinhThuc", hinhThuc == null ? "" : hinhThuc.name());
        nhap.put("loaiLyDo", loaiLyDo == null ? "" : loaiLyDo.name());
        nhap.put("lyDo", lyDo);
        nhap.put("benNgoai", benNgoai);
        nhap.put("conSeal", conSeal);
        nhap.put("daLap", daLap);
        model.addAttribute("nhap", nhap);
        try {
            DoiTraBUS.KetQuaTao kq = doiTraBUS.tao(don, serial, hinhThuc, loaiLyDo, lyDo, benNgoai, conSeal, daLap, xinNgoaiLe);
            if (kq.maTrung() != null) {
                ra.addFlashAttribute("loi", "Chiếc máy đã có yêu cầu đổi trả #" + kq.maTrung() + " đang Chờ duyệt; mở yêu cầu đang có.");
                return "redirect:/quan-tri/doi-tra/" + kq.maTrung();
            }
            if (kq.maYeuCau() == null) {
                model.addAttribute("khongDat", kq.khongDat());
                model.addAttribute("loi", "Không đủ điều kiện đổi trả. Khách xin xét ngoại lệ thì đánh dấu \"Khách xin xét ngoại lệ\" rồi gửi duyệt.");
                return hienFormTao(don, null, model);
            }
            ra.addFlashAttribute("thongBao", "Đã tạo yêu cầu đổi trả #" + kq.maYeuCau() + " (Chờ duyệt)"
                    + (kq.khongDat().isEmpty() ? ": đủ điều kiện." : ": đánh dấu Ngoại lệ, quản lý xin ý kiến chủ cửa hàng."));
            return "redirect:/quan-tri/doi-tra/" + kq.maYeuCau();
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            try {
                return hienFormTao(don, null, model);
            } catch (LoiNghiepVu e2) {
                ra.addFlashAttribute("loi", e.getMessage());
                return "redirect:/quan-tri/doi-tra/tao";
            }
        }
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("ct", doiTraBUS.chiTiet(ma));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/doi-tra";
        }
        model.addAttribute("quanLy", taiKhoanBUS.coVai("QUAN_LY"));
        model.addAttribute("keToan", taiKhoanBUS.coVai("KE_TOAN"));
        model.addAttribute("banHang", taiKhoanBUS.coVai("NV_BAN_HANG"));
        model.addAttribute("dsHinhThuc", HinhThucDoiTra.values());
        model.addAttribute("dsCachHoan", DoiTraBUS.CACH_HOAN);
        return "quan-tri/g4/doi-tra-chi-tiet";
    }

    @PostMapping("/{ma}/duyet")
    public String duyet(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo,
                        @RequestParam(required = false) Long phiKhauTru, @RequestParam(required = false) HinhThucDoiTra hinhThuc,
                        @RequestParam(required = false) String yKienChu, RedirectAttributes ra) {
        batBuocVai("QUAN_LY", "Chỉ quản lý cửa hàng duyệt đổi trả");
        try {
            doiTraBUS.duyet(ma, trangThaiLucMo, phiKhauTru, hinhThuc, yKienChu);
            ra.addFlashAttribute("thongBao", "Đã duyệt yêu cầu #" + ma + "; máy chuyển sang Trả về kho, chờ thủ kho nhận về kho.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        } catch (RuntimeException e) {
            LOG.error("Lỗi duyệt đổi trả", e);
            ra.addFlashAttribute("loi", "Chưa lưu được, thử lại");
        }
        return "redirect:/quan-tri/doi-tra/" + ma;
    }

    @PostMapping("/{ma}/tu-choi")
    public String tuChoi(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo,
                         @RequestParam(required = false) String lyDoTuChoi, @RequestParam(required = false) String yKienChu,
                         RedirectAttributes ra) {
        batBuocVai("QUAN_LY", "Chỉ quản lý cửa hàng từ chối đổi trả");
        try {
            doiTraBUS.tuChoi(ma, trangThaiLucMo, lyDoTuChoi, yKienChu);
            ra.addFlashAttribute("thongBao", "Đã từ chối yêu cầu #" + ma + "; máy giữ Đã bán. Nhân viên bán hàng báo khách.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/doi-tra/" + ma;
    }

    @PostMapping("/{ma}/hoan-tien")
    public String hoanTien(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo,
                           @RequestParam(required = false) String cachHoan, @RequestParam(required = false) String maGiaoDich,
                           RedirectAttributes ra) {
        batBuocVai("KE_TOAN", "Chỉ kế toán ghi hoàn tiền");
        try {
            ra.addFlashAttribute("thongBao", doiTraBUS.hoanTien(ma, trangThaiLucMo, cachHoan, maGiaoDich));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        } catch (RuntimeException e) {
            LOG.error("Lỗi ghi hoàn tiền", e);
            ra.addFlashAttribute("loi", "Chưa lưu được, thử lại");
        }
        return "redirect:/quan-tri/doi-tra/" + ma;
    }

    @PostMapping("/{ma}/giao-may-doi")
    public String giaoMayDoi(@PathVariable Long ma, @RequestParam(required = false) String trangThaiLucMo, RedirectAttributes ra) {
        batBuocVai("NV_BAN_HANG", "Chỉ nhân viên bán hàng ghi đã giao máy đổi");
        try {
            ra.addFlashAttribute("thongBao", doiTraBUS.daGiaoMayDoi(ma, trangThaiLucMo));
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/doi-tra/" + ma;
    }
}
