package vn.edu.huit.pttk.diengiadung.web.quantri.g1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.BaoCaoBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.TongQuanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriKhuyenMaiBUS;

@Controller
public class TongQuanController {

    private final TongQuanBUS tongQuanBUS;
    private final BaoCaoBUS baoCaoBUS;
    private final QuanTriKhuyenMaiBUS khuyenMaiBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public TongQuanController(TongQuanBUS tongQuanBUS, BaoCaoBUS baoCaoBUS, QuanTriKhuyenMaiBUS khuyenMaiBUS, TaiKhoanBUS taiKhoanBUS) {
        this.tongQuanBUS = tongQuanBUS;
        this.baoCaoBUS = baoCaoBUS;
        this.khuyenMaiBUS = khuyenMaiBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @GetMapping("/quan-tri")
    public String tongQuan(Model model) {
        model.addAttribute("oSo", tongQuanBUS.oSoTheoVai());
        taiKhoanBUS.nhanVienHienTai().ifPresent(nv -> model.addAttribute("hoTen", nv.getHoTen()));
        if (taiKhoanBUS.coVai("NV_BAN_HANG") || taiKhoanBUS.coVai("QUAN_LY")) {
            model.addAttribute("khuyenMai", khuyenMaiBUS.dangChay());
        }
        if (taiKhoanBUS.coVai("THU_KHO") || taiKhoanBUS.coVai("QUAN_LY") || taiKhoanBUS.coVai("KE_TOAN")) {
            model.addAttribute("sapHet", baoCaoBUS.mauSapHet());
        }
        return "quan-tri/g1/tong-quan";
    }
}
