package vn.edu.huit.pttk.diengiadung.web.khach;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.BaoHanhKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DinhDangTien;

@Controller
public class BaoHanhKhachController {

    private final BaoHanhKhachBUS baoHanhBUS;

    public BaoHanhKhachController(BaoHanhKhachBUS baoHanhBUS) {
        this.baoHanhBUS = baoHanhBUS;
    }

    @GetMapping("/bao-hanh")
    public String tra(@RequestParam(defaultValue = "serial") String cach, @RequestParam(required = false) String soSerial,
                      @RequestParam(required = false) String soDienThoai, @RequestParam(required = false) String maDon, Model model) {
        model.addAttribute("cach", "sdt".equals(cach) ? "sdt" : "serial");
        model.addAttribute("soSerial", soSerial);
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("maDon", maDon);
        boolean daGui = soSerial != null || soDienThoai != null;
        if (!daGui) {
            return "khach/bao-hanh";
        }
        try {
            if ("sdt".equals(cach)) {
                Long ma = null;
                if (maDon != null && !maDon.isBlank()) {
                    ma = DinhDangTien.docMaDon(maDon);
                    if (ma == null) {
                        throw new LoiNghiepVu("Mã đơn có dạng DH000012 hoặc chỉ số 12.");
                    }
                }
                model.addAttribute("ketQua", baoHanhBUS.traTheoSoDienThoai(soDienThoai, ma));
            } else {
                model.addAttribute("ketQua", baoHanhBUS.traTheoSerial(soSerial));
            }
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
        }
        return "khach/bao-hanh";
    }
}
