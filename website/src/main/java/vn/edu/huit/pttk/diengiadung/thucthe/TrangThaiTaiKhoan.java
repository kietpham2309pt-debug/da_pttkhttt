package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiTaiKhoan {
    DANG_DUNG("Đang dùng"),
    DA_KHOA("Đã khoá");

    private final String nhan;

    TrangThaiTaiKhoan(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
