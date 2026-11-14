package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import vn.edu.huit.pttk.diengiadung.thucthe.PhuongThucThanhToan;

public class PhieuDatHang implements Serializable {

    private String hoTen;
    private String soDienThoai;
    private String email;
    private String tinhThanh;
    private String quanHuyen;
    private String diaChi;
    private Boolean noiThanh = Boolean.TRUE;
    private String ghiChu;
    private String phuongThuc = PhuongThucThanhToan.TIEN_MAT_KHI_NHAN.name();
    private String maGiamGia;
    private Long maDiaChi;
    private boolean luuDiaChi;
    private List<Long> lapDat = new ArrayList<>();

    public Map<String, String> kiemTra() {
        Map<String, String> loi = new LinkedHashMap<>();
        if (trong(hoTen)) {
            loi.put("hoTen", "Bạn chưa nhập họ tên người nhận.");
        } else if (hoTen.trim().length() < 2 || hoTen.trim().length() > 60) {
            loi.put("hoTen", "Họ tên cần từ 2 tới 60 ký tự.");
        }
        if (trong(soDienThoai)) {
            loi.put("soDienThoai", "Bạn chưa nhập số điện thoại.");
        } else if (!soDienThoai.trim().matches("^0\\d{9}$")) {
            loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        if (!trong(email) && !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            loi.put("email", "Email không đúng dạng, ví dụ đúng: Huit@edu.com.vn");
        }
        if (maDiaChi == null) {
            if (trong(tinhThanh)) {
                loi.put("tinhThanh", "Bạn chưa nhập tỉnh hoặc thành phố.");
            }
            if (trong(quanHuyen)) {
                loi.put("quanHuyen", "Bạn chưa nhập quận hoặc huyện.");
            }
            if (trong(diaChi)) {
                loi.put("diaChi", "Bạn chưa nhập địa chỉ.");
            } else if (diaChi.trim().length() > 160) {
                loi.put("diaChi", "Địa chỉ dài quá 160 ký tự.");
            }
        }
        if (noiThanh == null) {
            loi.put("noiThanh", "Bạn chưa chọn khu vực giao hàng.");
        }
        if (ghiChu != null && ghiChu.length() > 300) {
            loi.put("ghiChu", "Ghi chú dài quá 300 ký tự.");
        }
        try {
            PhuongThucThanhToan.valueOf(phuongThuc);
        } catch (RuntimeException e) {
            loi.put("phuongThuc", "Bạn chưa chọn cách thanh toán.");
        }
        return loi;
    }

    public String diaChiDayDu() {
        if (maDiaChi != null || trong(tinhThanh)) {
            return diaChi == null ? "" : diaChi.trim();
        }
        return diaChi.trim() + ", " + quanHuyen.trim() + ", " + tinhThanh.trim();
    }

    private static boolean trong(String s) {
        return s == null || s.isBlank();
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(String tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public String getQuanHuyen() {
        return quanHuyen;
    }

    public void setQuanHuyen(String quanHuyen) {
        this.quanHuyen = quanHuyen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Boolean getNoiThanh() {
        return noiThanh;
    }

    public void setNoiThanh(Boolean noiThanh) {
        this.noiThanh = noiThanh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public Long getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(Long maDiaChi) {
        this.maDiaChi = maDiaChi;
    }

    public boolean isLuuDiaChi() {
        return luuDiaChi;
    }

    public void setLuuDiaChi(boolean luuDiaChi) {
        this.luuDiaChi = luuDiaChi;
    }

    public List<Long> getLapDat() {
        return lapDat;
    }

    public void setLapDat(List<Long> lapDat) {
        this.lapDat = lapDat == null ? new ArrayList<>() : lapDat;
    }

    public PhuongThucThanhToan phuongThucEnum() {
        return PhuongThucThanhToan.valueOf(phuongThuc);
    }
}
