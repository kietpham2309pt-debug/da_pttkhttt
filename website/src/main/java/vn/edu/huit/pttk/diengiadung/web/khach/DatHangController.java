package vn.edu.huit.pttk.diengiadung.web.khach;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.pttk.diengiadung.nghiepvu.GioHang;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.BangTinh;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DatHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.DonHangKhachBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhienKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.PhieuDatHang;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.QuyenDonKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.khach.ThongTinDon;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;

@Controller
public class DatHangController {

    private final GioHang gio;
    private final PhienKhach phien;
    private final DatHangBUS datHangBUS;
    private final DonHangKhachBUS donHangKhachBUS;
    private final TaiKhoanBUS taiKhoanBUS;
    private final QuyenDonKhach quyen;

    public DatHangController(GioHang gio, PhienKhach phien, DatHangBUS datHangBUS, DonHangKhachBUS donHangKhachBUS,
                             TaiKhoanBUS taiKhoanBUS, QuyenDonKhach quyen) {
        this.gio = gio;
        this.phien = phien;
        this.datHangBUS = datHangBUS;
        this.donHangKhachBUS = donHangKhachBUS;
        this.taiKhoanBUS = taiKhoanBUS;
        this.quyen = quyen;
    }

    @GetMapping("/dat-hang")
    public String moTrang(Model model, RedirectAttributes ra) {
        if (gio.isTrong()) {
            ra.addFlashAttribute("loi", "Giỏ hàng đang trống, bạn chọn sản phẩm trước khi đặt hàng.");
            return "redirect:/gio-hang";
        }
        KhachHang kh = taiKhoanBUS.khachHangHienTai().orElse(null);
        PhieuDatHang phieu = phien.getBanNhap();
        if (phieu == null) {
            phieu = new PhieuDatHang();
            datHangBUS.dienMacDinh(phieu, kh);
        }
        for (Long ma : gio.getDong().keySet()) {
            if (gio.coLapDat(ma) && !phieu.getLapDat().contains(ma)) {
                phieu.getLapDat().add(ma);
            }
        }
        return hienForm(model, phieu, new LinkedHashMap<>(), kh);
    }

    @PostMapping("/dat-hang")
    public String guiForm(@ModelAttribute("phieu") PhieuDatHang phieu, @RequestParam(defaultValue = "xemLai") String nut, Model model,
                          RedirectAttributes ra) {
        if (gio.isTrong()) {
            ra.addFlashAttribute("loi", "Giỏ hàng đang trống.");
            return "redirect:/gio-hang";
        }
        KhachHang kh = taiKhoanBUS.khachHangHienTai().orElse(null);
        datHangBUS.apLapDat(gio, phieu);
        datHangBUS.dienTuDiaChiDaLuu(phieu, kh);
        phien.setBanNhap(phieu);
        Map<String, String> loi = new LinkedHashMap<>();
        BangTinh bang = datHangBUS.tinh(gio, Boolean.TRUE.equals(phieu.getNoiThanh()), phieu.getMaGiamGia());
        if (bang.getLoiMa() != null) {
            loi.put("maGiamGia", bang.getLoiMa());
        }
        if ("apMa".equals(nut)) {
            if (bang.getLoiMa() == null && bang.getMaGiam() != null) {
                model.addAttribute("thongBao", "Đã áp mã " + bang.getMaGiam().getMaCode() + ". Mỗi đơn chỉ dùng một mã, mã mới sẽ thay mã cũ.");
            }
            return hienForm(model, phieu, loi, kh);
        }
        loi.putAll(phieu.kiemTra());
        if (bang.isCoLoi()) {
            loi.put("gio", "Có mẫu máy không đặt được, bạn bỏ khỏi đơn hoặc sửa số lượng ở giỏ hàng.");
        }
        if (!loi.isEmpty()) {
            return hienForm(model, phieu, loi, kh);
        }
        return "redirect:/dat-hang/xac-nhan";
    }

    @GetMapping("/dat-hang/xac-nhan")
    public String xacNhan(Model model, RedirectAttributes ra) {
        PhieuDatHang phieu = phien.getBanNhap();
        if (gio.isTrong()) {
            return "redirect:/gio-hang";
        }
        if (phieu == null || !phieu.kiemTra().isEmpty()) {
            return "redirect:/dat-hang";
        }
        BangTinh bang = datHangBUS.tinh(gio, Boolean.TRUE.equals(phieu.getNoiThanh()), phieu.getMaGiamGia());
        if (bang.isCoLoi() || bang.getLoiMa() != null) {
            return "redirect:/dat-hang";
        }
        return hienXacNhan(model, phieu, bang);
    }

    @PostMapping("/dat-hang/xac-nhan")
    public String datHang(@RequestParam(required = false) String maLuot, @RequestParam(required = false) Long tongDaXem,
                          @RequestParam(defaultValue = "false") boolean dongYDieuKien,
                          @RequestParam(defaultValue = "false") boolean dongYXuLyDuLieu,
                          @RequestParam(defaultValue = "false") boolean dongYQuangCao, Model model) {
        if (!phien.dungMaLuotDat(maLuot)) {
            Long vua = phien.getDonVuaDat();
            if (vua != null && gio.isTrong()) {
                return "redirect:/don-hang/hoan-tat/" + vua;
            }
            return "redirect:/dat-hang/xac-nhan";
        }
        PhieuDatHang phieu = phien.getBanNhap();
        if (phieu == null || gio.isTrong()) {
            return "redirect:/gio-hang";
        }
        KhachHang kh = taiKhoanBUS.khachHangHienTai().orElse(null);
        try {
            DonHang don = datHangBUS.datHang(phieu, gio, kh, dongYDieuKien, dongYXuLyDuLieu, dongYQuangCao, tongDaXem);
            phien.choXemDon(don.getMaDonHang());
            phien.setDonVuaDat(don.getMaDonHang());
            phien.setBanNhap(null);
            return "redirect:/don-hang/hoan-tat/" + don.getMaDonHang();
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
        } catch (DataAccessException e) {
            model.addAttribute("loi", "Chưa đặt được đơn, vui lòng thử lại.");
        }
        if (gio.isTrong()) {
            return "redirect:/gio-hang";
        }
        BangTinh bang = datHangBUS.tinh(gio, Boolean.TRUE.equals(phieu.getNoiThanh()), phieu.getMaGiamGia());
        model.addAttribute("dongYQuangCao", dongYQuangCao);
        return hienXacNhan(model, phieu, bang);
    }

    @GetMapping("/don-hang/hoan-tat/{ma}")
    public String hoanTat(@PathVariable Long ma, Model model) {
        if (!quyen.duocXem(ma)) {
            return "redirect:/tra-cuu-don";
        }
        ThongTinDon tt = donHangKhachBUS.thongTin(ma);
        if (tt == null) {
            return "redirect:/tra-cuu-don";
        }
        model.addAttribute("tt", tt);
        model.addAttribute("don", tt.getDon());
        return "khach/hoan-tat";
    }

    private String hienForm(Model model, PhieuDatHang phieu, Map<String, String> loi, KhachHang kh) {
        BangTinh bang = datHangBUS.tinh(gio, Boolean.TRUE.equals(phieu.getNoiThanh()), loi.containsKey("maGiamGia") ? null : phieu.getMaGiamGia());
        model.addAttribute("phieu", phieu);
        model.addAttribute("bang", bang);
        model.addAttribute("loiO", loi);
        model.addAttribute("soDiaChi", datHangBUS.soDiaChi(kh));
        model.addAttribute("daDangNhap", kh != null);
        if (!loi.isEmpty() && !model.containsAttribute("loi")) {
            model.addAttribute("loi", "Bạn kiểm tra lại các ô báo đỏ bên dưới.");
        }
        return "khach/dat-hang";
    }

    private String hienXacNhan(Model model, PhieuDatHang phieu, BangTinh bang) {
        model.addAttribute("phieu", phieu);
        model.addAttribute("bang", bang);
        model.addAttribute("diaChiDayDu", phieu.diaChiDayDu());
        model.addAttribute("maLuot", phien.capMaLuotDat());
        return "khach/xac-nhan";
    }
}
