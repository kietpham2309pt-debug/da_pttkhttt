package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class DinhDangTien {

    private DinhDangTien() {
    }

    public static String tien(long soTien) {
        DecimalFormatSymbols k = new DecimalFormatSymbols(Locale.ROOT);
        k.setGroupingSeparator('.');
        return new DecimalFormat("#,##0", k).format(soTien) + " ₫";
    }

    public static String maDon(Long ma) {
        return ma == null ? "" : String.format("DH%06d", ma);
    }

    public static Long docMaDon(String chuoi) {
        if (chuoi == null) {
            return null;
        }
        String s = chuoi.trim().toUpperCase(Locale.ROOT);
        if (s.startsWith("DH")) {
            s = s.substring(2);
        }
        if (!s.matches("\\d{1,12}")) {
            return null;
        }
        long ma = Long.parseLong(s);
        return ma > 0 ? ma : null;
    }

    public static String cheDiaChi(String diaChi) {
        if (diaChi == null || diaChi.isBlank()) {
            return "";
        }
        String[] phan = diaChi.split(",");
        if (phan.length <= 2) {
            String dau = phan[0].trim();
            int giu = Math.min(4, dau.length());
            return dau.substring(0, giu) + "***" + (phan.length == 2 ? "," + phan[1] : "");
        }
        StringBuilder sb = new StringBuilder("***");
        for (int i = phan.length - 2; i < phan.length; i++) {
            sb.append(",").append(phan[i]);
        }
        return sb.toString();
    }
}
