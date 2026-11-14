package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;

public final class G4Chung {

    public static final DateTimeFormatter GIO_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int GHI_CHU_TOI_DA = 500;

    private G4Chung() {
    }

    public static String maDon(Long ma) {
        return ma == null ? "" : String.format("DH%06d", ma);
    }

    public static String tien(long soTien) {
        return String.format("%,d", soTien).replace(',', '.') + " ₫";
    }

    public static String rong(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    public static String batBuoc(String s, String thongBao) {
        String g = rong(s);
        if (g == null) {
            throw new LoiNghiepVu(thongBao);
        }
        return g;
    }

    public static String gioiHan(String s, int toiDa, String tenTruong) {
        if (s != null && s.length() > toiDa) {
            throw new LoiNghiepVu(tenTruong + " dài quá " + toiDa + " ký tự");
        }
        return s;
    }

    public static void noiGhiChu(DonHang don, String them) {
        String cu = don.getGhiChu() == null ? "" : don.getGhiChu().trim();
        String moi = (cu.isEmpty() ? "" : cu + "\n") + "[" + LocalDateTime.now().format(GIO_NGAY) + "] " + them;
        if (moi.length() > GHI_CHU_TOI_DA) {
            moi = moi.substring(moi.length() - GHI_CHU_TOI_DA);
        }
        don.setGhiChu(moi);
    }

    public static void kiemTraTrangThaiLucMo(String lucMo, Enum<?> hienTai, String doiTuong, String nhanHienTai) {
        if (lucMo != null && !lucMo.isBlank() && !lucMo.equals(hienTai.name())) {
            throw new LoiNghiepVu(doiTuong + " đã ở trạng thái \"" + nhanHienTai + "\" (người khác vừa xử lý), không ghi đè");
        }
    }
}
