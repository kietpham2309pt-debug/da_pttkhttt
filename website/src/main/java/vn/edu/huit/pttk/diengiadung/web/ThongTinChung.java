package vn.edu.huit.pttk.diengiadung.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.huit.pttk.diengiadung.nghiepvu.GioHang;

@ControllerAdvice
public class ThongTinChung {

    private final GioHang gioHang;

    public ThongTinChung(GioHang gioHang) {
        this.gioHang = gioHang;
    }

    @ModelAttribute("soLuongGio")
    public int soLuongGio() {
        try {
            return gioHang.getTongSoLuong();
        } catch (RuntimeException e) {
            return 0;
        }
    }
}
