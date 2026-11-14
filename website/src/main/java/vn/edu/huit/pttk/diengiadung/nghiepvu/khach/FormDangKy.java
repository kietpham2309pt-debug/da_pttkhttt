package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormDangKy {

    private String hoTen;
    private String soDienThoai;
    private String email;
    private String tenDangNhap;
    private String matKhau;
    private String nhapLai;
    private boolean dongY;

    public Map<String, String> kiemTra() {
        Map<String, String> loi = new LinkedHashMap<>();
        if (hoTen == null || hoTen.isBlank()) {
            loi.put("hoTen", "Bạn chưa nhập họ tên.");
        } else if (hoTen.trim().length() < 2 || hoTen.trim().length() > 60) {
            loi.put("hoTen", "Họ tên cần từ 2 tới 60 ký tự.");
        }
        if (soDienThoai == null || soDienThoai.isBlank()) {
            loi.put("soDienThoai", "Bạn chưa nhập số điện thoại.");
        } else if (!soDienThoai.trim().matches("^0\\d{9}$")) {
            loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        if (email == null || email.isBlank()) {
            loi.put("email", "Bạn chưa nhập email.");
        } else if (!email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || email.trim().length() > 100) {
            loi.put("email", "Email không đúng dạng, ví dụ đúng: Huit@edu.com.vn");
        }
        if (tenDangNhap == null || tenDangNhap.isBlank()) {
            loi.put("tenDangNhap", "Bạn chưa nhập tên đăng nhập.");
        } else if (!tenDangNhap.trim().matches("^[A-Za-z0-9._]{4,50}$")) {
            loi.put("tenDangNhap", "Tên đăng nhập từ 4 tới 50 ký tự, chỉ gồm chữ không dấu, số, dấu chấm, gạch dưới.");
        }
        if (matKhau == null || matKhau.length() < 6) {
            loi.put("matKhau", "Mật khẩu cần ít nhất 6 ký tự.");
        } else if (matKhau.length() > 50) {
            loi.put("matKhau", "Mật khẩu dài quá 50 ký tự.");
        }
        if (nhapLai == null || !nhapLai.equals(matKhau)) {
            loi.put("nhapLai", "Hai lần nhập mật khẩu chưa khớp nhau.");
        }
        if (!dongY) {
            loi.put("dongY", "Bạn cần đồng ý với Điều khoản sử dụng và Chính sách bảo mật.");
        }
        return loi;
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

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getNhapLai() {
        return nhapLai;
    }

    public void setNhapLai(String nhapLai) {
        this.nhapLai = nhapLai;
    }

    public boolean isDongY() {
        return dongY;
    }

    public void setDongY(boolean dongY) {
        this.dongY = dongY;
    }
}
