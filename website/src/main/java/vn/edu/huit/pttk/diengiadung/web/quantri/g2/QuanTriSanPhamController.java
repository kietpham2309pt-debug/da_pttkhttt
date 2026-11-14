package vn.edu.huit.pttk.diengiadung.web.quantri.g2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.BieuMauSanPham;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriDanhMucBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g2.QuanTriSanPhamBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

@Controller
@RequestMapping("/quan-tri/san-pham")
public class QuanTriSanPhamController {

    private final QuanTriSanPhamBUS bus;
    private final QuanTriDanhMucBUS danhMucBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public QuanTriSanPhamController(QuanTriSanPhamBUS bus, QuanTriDanhMucBUS danhMucBUS, TaiKhoanBUS taiKhoanBUS) {
        this.bus = bus;
        this.danhMucBUS = danhMucBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String danhMuc, @RequestParam(required = false) Long thuongHieu,
                           @RequestParam(required = false) String trangThai, @RequestParam(required = false) String tuKhoa, Model model) {
        model.addAttribute("dsSanPham", bus.loc(danhMuc, thuongHieu, trangThai, tuKhoa));
        model.addAttribute("dsDanhMuc", danhMucBUS.danhMuc());
        model.addAttribute("dsThuongHieu", danhMucBUS.thuongHieu());
        model.addAttribute("dsTrangThai", TrangThaiSanPham.values());
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("thuongHieu", thuongHieu);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("tuKhoa", tuKhoa);
        return "quan-tri/g2/san-pham";
    }

    @GetMapping("/them")
    public String them(Model model) {
        BieuMauSanPham bm = new BieuMauSanPham();
        return bieuMau(model, bm, null);
    }

    @GetMapping("/{ma}/sua")
    public String sua(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            SanPham sp = bus.tim(ma);
            return bieuMau(model, bus.bieuMauSua(ma), sp);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/san-pham";
        }
    }

    @PostMapping("/luu")
    public String luu(@ModelAttribute("bm") BieuMauSanPham bm, BindingResult kq, Model model, RedirectAttributes ra) {
        if (kq.hasFieldErrors("thoiHanBaoHanh")) {
            bm.setThoiHanBaoHanh(null);
        }
        try {
            boolean themMoi = bm.getMaSanPham() == null;
            SanPham sp = bus.luu(bm);
            ra.addFlashAttribute("thongBao", themMoi
                    ? "Đã lưu mẫu máy \"" + sp.getTenSanPham() + "\", chờ Quản lý cửa hàng đặt giá bán."
                    : "Đã cập nhật mẫu máy \"" + sp.getTenSanPham() + "\".");
            return "redirect:/quan-tri/san-pham";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            return bieuMau(model, bm, bm.getMaSanPham() == null ? null : bus.tim(bm.getMaSanPham()));
        }
    }

    @PostMapping("/{ma}/ngung-ban")
    public String ngungBan(@PathVariable Long ma, RedirectAttributes ra) {
        chiQuanLy();
        try {
            ra.addFlashAttribute("thongBao", "Đã ngừng bán \"" + bus.ngungBan(ma) + "\"; mẫu máy ẩn khỏi trang khách, dữ liệu vẫn giữ.");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/san-pham";
    }

    @PostMapping("/{ma}/mo-ban")
    public String moBan(@PathVariable Long ma, RedirectAttributes ra) {
        chiQuanLy();
        try {
            ra.addFlashAttribute("thongBao", "Đã mở bán \"" + bus.moBan(ma) + "\".");
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/quan-tri/san-pham";
    }

    @GetMapping("/{ma}/gia")
    public String gia(@PathVariable Long ma, Model model, RedirectAttributes ra) {
        try {
            return trangGia(model, bus.tim(ma), null, null, null);
        } catch (LoiNghiepVu e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/quan-tri/san-pham";
        }
    }

    @PostMapping("/{ma}/gia")
    public String capNhatGia(@PathVariable Long ma, @RequestParam(required = false) String giaMoi,
                             @RequestParam(required = false) String giaNiemYet, @RequestParam(required = false) String lyDo,
                             Model model, RedirectAttributes ra) {
        chiQuanLy();
        try {
            Long gm = docSo(giaMoi);
            Long gny = docSo(giaNiemYet);
            if (gm == null && giaMoi != null && !giaMoi.isBlank()) {
                throw new LoiNhapLieu(Map.of("giaMoi", "Giá bán mới phải là số tiền (chỉ nhập chữ số)."));
            }
            if (gny == null && giaNiemYet != null && !giaNiemYet.isBlank()) {
                throw new LoiNhapLieu(Map.of("giaNiemYet", "Giá niêm yết phải là số tiền (chỉ nhập chữ số)."));
            }
            var ls = bus.capNhatGia(ma, gm, gny, lyDo);
            ra.addFlashAttribute("thongBao", Boolean.TRUE.equals(ls.getDuoiGiaNhap())
                    ? "Đã cập nhật giá (thấp hơn giá nhập gần nhất, đã lưu vết lý do)."
                    : "Đã cập nhật giá bán.");
            return "redirect:/quan-tri/san-pham/" + ma + "/gia";
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            if (e instanceof LoiNhapLieu l) {
                model.addAttribute("loiO", l.getLoiO());
            }
            return trangGia(model, bus.tim(ma), giaMoi, giaNiemYet, lyDo);
        }
    }

    private String trangGia(Model model, SanPham sp, String giaMoi, String giaNiemYet, String lyDo) {
        model.addAttribute("sp", sp);
        model.addAttribute("giaNhap", bus.giaNhapGanNhat(sp.getMaSanPham()));
        model.addAttribute("lichSu", bus.lichSuGia(sp.getMaSanPham()));
        model.addAttribute("giaMoi", giaMoi);
        model.addAttribute("giaNiemYet", giaNiemYet);
        model.addAttribute("lyDo", lyDo);
        if (!model.containsAttribute("loiO")) {
            model.addAttribute("loiO", new HashMap<String, String>());
        }
        return "quan-tri/g2/gia";
    }

    private String bieuMau(Model model, BieuMauSanPham bm, SanPham sp) {
        bm.getThongSo().removeIf(java.util.Objects::isNull);
        while (bm.getThongSo().size() < 3) {
            bm.getThongSo().add(new BieuMauSanPham.DongThongSo());
        }
        model.addAttribute("bm", bm);
        model.addAttribute("sp", sp);
        model.addAttribute("dsDanhMuc", danhMucBUS.danhMuc());
        model.addAttribute("dsThuongHieu", danhMucBUS.thuongHieu());
        model.addAttribute("goiY", bus.thongSoGoiY());
        if (!model.containsAttribute("loiO")) {
            model.addAttribute("loiO", new HashMap<String, String>());
        }
        return "quan-tri/g2/san-pham-form";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String khongCoQuyen(Model model) {
        model.addAttribute("tieuDe", "Không có quyền");
        model.addAttribute("noiDung", "Chỉ Quản lý cửa hàng được đặt giá, mở bán, ngừng bán mẫu máy.");
        model.addAttribute("lienKet", "/quan-tri/san-pham");
        model.addAttribute("chuLienKet", "Về danh sách sản phẩm");
        return "thong-diep";
    }

    private void chiQuanLy() {
        if (!taiKhoanBUS.coVai("QUAN_LY")) {
            throw new AccessDeniedException("Chỉ Quản lý cửa hàng");
        }
    }

    private static Long docSo(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        String t = s.replace(".", "").replace(",", "").replace(" ", "").replace("₫", "").replace("đ", "");
        try {
            return Long.parseLong(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
