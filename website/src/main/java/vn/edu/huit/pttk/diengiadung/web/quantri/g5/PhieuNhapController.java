package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import java.util.List;
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
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.PhieuNhapBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;

@Controller
@RequestMapping("/quan-tri/kho/phieu-nhap")
public class PhieuNhapController {

    private final PhieuNhapBUS phieuNhapBUS;
    private final DonDatBUS donDatBUS;
    private final QuyenG5 quyen;

    public PhieuNhapController(PhieuNhapBUS phieuNhapBUS, DonDatBUS donDatBUS, QuyenG5 quyen) {
        this.phieuNhapBUS = phieuNhapBUS;
        this.donDatBUS = donDatBUS;
        this.quyen = quyen;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("ds", phieuNhapBUS.danhSach());
        model.addAttribute("choNhap", donDatBUS.choNhap());
        return "quan-tri/g5/phieu-nhap";
    }

    @GetMapping("/lap")
    public String lap(@RequestParam(required = false) Long donDat, Model model, RedirectAttributes ra) {
        quyen.batBuoc("THU_KHO");
        if (donDat == null) {
            model.addAttribute("choNhap", donDatBUS.choNhap());
            return "quan-tri/g5/phieu-nhap-chon-don";
        }
        try {
            model.addAttribute("f", phieuNhapBUS.taoNhap(donDat));
            model.addAttribute("mucGiaTriCao", PhieuNhapBUS.MUC_GIA_TRI_CAO);
            return "quan-tri/g5/phieu-nhap-lap";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/phieu-nhap/lap";
        }
    }

    @PostMapping("/lap")
    public String luu(@RequestParam Long maDonDat, @RequestParam MultiValueMap<String, String> p, Model model, RedirectAttributes ra) {
        NhanVien nv = quyen.batBuoc("THU_KHO");
        PhieuNhapBUS.PhieuNhapNhap f;
        try {
            f = phieuNhapBUS.taoNhap(maDonDat);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/phieu-nhap/lap";
        }
        f.setSoHoaDon(p.getFirst("soHoaDon"));
        f.setNgayHoaDon(p.getFirst("ngayHoaDon"));
        f.setNguoiGiao(p.getFirst("nguoiGiao"));
        f.setTinhTrang(p.getFirst("tinhTrang"));
        List<String> maSp = p.getOrDefault("maSanPham", List.of());
        for (int i = 0; i < maSp.size(); i++) {
            String ma = maSp.get(i);
            for (PhieuNhapBUS.DongNhap r : f.getDong()) {
                if (String.valueOf(r.getMaSanPham()).equals(ma)) {
                    r.setChungTu(lay(p, "chungTu", i));
                    r.setThucNhap(lay(p, "thucNhap", i));
                    r.setDonGia(lay(p, "donGia", i));
                    r.setSerial(lay(p, "serial", i));
                    r.setDonRieng(lay(p, "donRieng", i));
                }
            }
        }
        try {
            Long maPhieu = phieuNhapBUS.luu(f, nv);
            ra.addFlashAttribute("thongBao", "Đã lưu phiếu nhập #" + maPhieu + "; các máy đã vào kho ở trạng thái Trong kho. Phiếu đã khoá.");
            return "redirect:/quan-tri/kho/phieu-nhap/" + maPhieu;
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            model.addAttribute("f", f);
            model.addAttribute("mucGiaTriCao", PhieuNhapBUS.MUC_GIA_TRI_CAO);
            return "quan-tri/g5/phieu-nhap-lap";
        }
    }

    private static String lay(MultiValueMap<String, String> p, String ten, int i) {
        List<String> ds = p.get(ten);
        return ds == null || i >= ds.size() ? "" : ds.get(i);
    }

    @GetMapping("/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("x", phieuNhapBUS.xem(ma));
            return "quan-tri/g5/phieu-nhap-chi-tiet";
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/kho/phieu-nhap";
        }
    }
}
