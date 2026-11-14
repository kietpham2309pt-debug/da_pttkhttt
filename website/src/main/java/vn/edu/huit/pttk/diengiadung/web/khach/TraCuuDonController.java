package vn.edu.huit.pttk.diengiadung.web.khach;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DinhDangTien;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DonHangKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhienKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.QuyenDonKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.ThongTinDon;

@Controller
public class TraCuuDonController {

    private final DonHangKhachBUS donHangKhachBUS;
    private final PhienKhach phien;
    private final QuyenDonKhach quyen;

    public TraCuuDonController(DonHangKhachBUS donHangKhachBUS, PhienKhach phien, QuyenDonKhach quyen) {
        this.donHangKhachBUS = donHangKhachBUS;
        this.phien = phien;
        this.quyen = quyen;
    }

    @GetMapping("/tra-cuu-don")
    public String form(Model model) {
        model.addAttribute("loiO", new LinkedHashMap<String, String>());
        if (phien.dangKhoaTra()) {
            model.addAttribute("khoaDen", phien.getKhoaTraDen());
        }
        return "khach/tra-cuu-don";
    }

    @PostMapping("/tra-cuu-don")
    public String tra(@RequestParam(defaultValue = "") String soDienThoai, @RequestParam(defaultValue = "") String maDon, Model model) {
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("maDon", maDon);
        Map<String, String> loi = new LinkedHashMap<>();
        model.addAttribute("loiO", loi);
        if (phien.dangKhoaTra()) {
            model.addAttribute("khoaDen", phien.getKhoaTraDen());
            model.addAttribute("loi", "Bạn tra sai quá 5 lần, chức năng tra cứu tạm khoá 15 phút.");
            return "khach/tra-cuu-don";
        }
        String sdt = soDienThoai.trim();
        if (sdt.isEmpty()) {
            loi.put("soDienThoai", "Bạn chưa nhập số điện thoại.");
        } else if (!sdt.matches("^0\\d{9}$")) {
            loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        Long ma = DinhDangTien.docMaDon(maDon);
        if (maDon.isBlank()) {
            loi.put("maDon", "Bạn chưa nhập mã đơn.");
        } else if (ma == null) {
            loi.put("maDon", "Mã đơn có dạng DH000012 hoặc chỉ số 12.");
        }
        if (!loi.isEmpty()) {
            return "khach/tra-cuu-don";
        }
        Optional<Long> thay;
        try {
            thay = donHangKhachBUS.timDon(sdt, ma);
        } catch (DataAccessException e) {
            model.addAttribute("loi", "Chưa tra được, vui lòng thử lại.");
            return "khach/tra-cuu-don";
        }
        if (thay.isEmpty()) {
            if (phien.ghiTraSai()) {
                model.addAttribute("khoaDen", phien.getKhoaTraDen());
                model.addAttribute("loi", "Bạn tra sai 5 lần liên tiếp, chức năng tra cứu tạm khoá 15 phút.");
            } else {
                model.addAttribute("loi", "Không tìm thấy đơn với số điện thoại và mã đơn này. Còn " + phien.soLanConLai() + " lần thử.");
            }
            return "khach/tra-cuu-don";
        }
        phien.ghiTraDung();
        phien.choXemDon(thay.get());
        return "redirect:/tra-cuu-don/" + thay.get();
    }

    @GetMapping("/tra-cuu-don/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        if (!quyen.duocXem(ma)) {
            ra.addFlashAttribute("loi", "Bạn nhập số điện thoại và mã đơn để xem đơn này.");
            return "redirect:/tra-cuu-don";
        }
        ThongTinDon tt = donHangKhachBUS.thongTin(ma);
        if (tt == null) {
            return "redirect:/tra-cuu-don";
        }
        model.addAttribute("tt", tt);
        model.addAttribute("don", tt.getDon());
        return "khach/chi-tiet-don";
    }

    @PostMapping("/tra-cuu-don/{ma}/huy")
    public String huy(@PathVariable Long ma, @RequestParam(defaultValue = "") String lyDo, RedirectAttributes ra) {
        if (!quyen.duocXem(ma)) {
            return "redirect:/tra-cuu-don";
        }
        try {
            donHangKhachBUS.huyDon(ma, lyDo);
            ra.addFlashAttribute("thongBao", "Đã huỷ đơn " + DinhDangTien.maDon(ma) + ".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tra-cuu-don/" + ma;
    }
}
