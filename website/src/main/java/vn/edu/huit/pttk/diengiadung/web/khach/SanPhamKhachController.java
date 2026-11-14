package vn.edu.huit.pttk.diengiadung.web.khach;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.ChiTietSanPham;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.LocSanPham;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhienKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.SanPhamKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.TaiKhoanKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.TheSanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;

@Controller
public class SanPhamKhachController {

    private final SanPhamKhachBUS sanPhamBUS;
    private final PhienKhach phien;
    private final TaiKhoanBUS taiKhoanBUS;
    private final TaiKhoanKhachBUS taiKhoanKhachBUS;

    public SanPhamKhachController(SanPhamKhachBUS sanPhamBUS, PhienKhach phien, TaiKhoanBUS taiKhoanBUS, TaiKhoanKhachBUS taiKhoanKhachBUS) {
        this.sanPhamBUS = sanPhamBUS;
        this.phien = phien;
        this.taiKhoanBUS = taiKhoanBUS;
        this.taiKhoanKhachBUS = taiKhoanKhachBUS;
    }

    @GetMapping("/san-pham")
    public String danhSach(@ModelAttribute("loc") LocSanPham loc, Model model) {
        Page<TheSanPham> kq = sanPhamBUS.timKiem(loc);
        List<DanhMuc> dsDanhMuc = sanPhamBUS.danhMuc();
        DanhMuc dm = dsDanhMuc.stream().filter(d -> d.getMaDanhMuc().equals(loc.getDanhMuc())).findFirst().orElse(null);
        model.addAttribute("kq", kq);
        model.addAttribute("dsDanhMuc", dsDanhMuc);
        model.addAttribute("dsThuongHieu", sanPhamBUS.thuongHieu());
        model.addAttribute("danhMucChon", dm);
        UriComponentsBuilder b = UriComponentsBuilder.fromPath("/san-pham");
        if (loc.getDanhMuc() != null) {
            b.queryParam("danhMuc", loc.getDanhMuc());
        }
        if (loc.getTuKhoa() != null) {
            b.queryParam("tuKhoa", loc.getTuKhoa());
        }
        for (Long th : loc.getThuongHieu()) {
            b.queryParam("thuongHieu", th);
        }
        if (loc.getGiaTu() != null) {
            b.queryParam("giaTu", loc.getGiaTu());
        }
        if (loc.getGiaDen() != null) {
            b.queryParam("giaDen", loc.getGiaDen());
        }
        b.queryParam("sapXep", loc.getSapXep());
        String goc = b.encode().build().toUriString();
        model.addAttribute("duongDanTrang", goc + "&trang=");
        UriComponentsBuilder boLoc = UriComponentsBuilder.fromPath("/san-pham");
        if (loc.getDanhMuc() != null) {
            boLoc.queryParam("danhMuc", loc.getDanhMuc());
        }
        if (loc.getTuKhoa() != null) {
            boLoc.queryParam("tuKhoa", loc.getTuKhoa());
        }
        model.addAttribute("duongDanBoLoc", boLoc.encode().build().toUriString());
        return "khach/danh-sach";
    }

    @GetMapping("/san-pham/{ma}")
    public String chiTiet(@PathVariable Long ma, Model model) {
        ChiTietSanPham ct = sanPhamBUS.chiTiet(ma);
        if (ct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("ct", ct);
        model.addAttribute("t", ct.getThe());
        model.addAttribute("dangSoSanh", phien.getSoSanh());
        boolean coTheDanhGia = taiKhoanBUS.khachHangHienTai()
                .map(kh -> taiKhoanKhachBUS.canDanhGia(kh).stream().anyMatch(x -> x.getMaSanPham().equals(ma)))
                .orElse(false);
        model.addAttribute("coTheDanhGia", coTheDanhGia);
        return "khach/chi-tiet";
    }

    @GetMapping("/so-sanh")
    public String soSanh(@RequestParam(name = "ma", required = false) List<Long> ma, Model model) {
        List<Long> ds = ma == null || ma.isEmpty() ? phien.getSoSanh() : new ArrayList<>(ma);
        if (ma != null && !ma.isEmpty()) {
            phien.datSoSanh(ds);
        }
        if (ds.size() > PhienKhach.SO_MAU_SO_SANH) {
            model.addAttribute("loi", "Chỉ so sánh tối đa 3 mẫu máy một lần, hệ thống lấy 3 mẫu đầu.");
        }
        List<TheSanPham> sp = sanPhamBUS.soSanh(ds);
        model.addAttribute("dsSoSanh", sp);
        model.addAttribute("tenThongSo", SanPhamKhachBUS.tenThongSoChung(sp));
        return "khach/so-sanh";
    }

    @PostMapping("/so-sanh/them")
    public String themSoSanh(@RequestParam Long ma, RedirectAttributes ra) {
        if (!phien.themSoSanh(ma)) {
            ra.addFlashAttribute("loi", "Đã đủ 3 mẫu máy để so sánh. Bỏ bớt một mẫu rồi thêm lại.");
        }
        return "redirect:/so-sanh";
    }

    @PostMapping("/so-sanh/bo")
    public String boSoSanh(@RequestParam Long ma) {
        phien.boSoSanh(ma);
        return "redirect:/so-sanh";
    }
}
