package vn.edu.huit.pttk.diengiadung.web;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("dd")
public class DinhDang {

    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String tien(Long soTien) {
        if (soTien == null) {
            return "Chưa có giá";
        }
        DecimalFormatSymbols k = new DecimalFormatSymbols(Locale.ROOT);
        k.setGroupingSeparator('.');
        return new DecimalFormat("#,##0", k).format(soTien) + " ₫";
    }

    public String ngay(LocalDate ngay) {
        return ngay == null ? "" : ngay.format(NGAY);
    }

    public String ngay(LocalDateTime ngay) {
        return ngay == null ? "" : ngay.format(NGAY);
    }

    public String ngayGio(LocalDateTime ngay) {
        return ngay == null ? "" : ngay.format(NGAY_GIO);
    }

    public String maDon(Long ma) {
        return ma == null ? "" : String.format("DH%06d", ma);
    }

    public String che(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.length() < 7) {
            return soDienThoai;
        }
        return soDienThoai.substring(0, 3) + "****" + soDienThoai.substring(soDienThoai.length() - 3);
    }
}
