package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiKhuyenMai {
    DANG_CHAY("Đang chạy"),
    TAM_DUNG("Tạm dừng");

    private final String nhan;

    TrangThaiKhuyenMai(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
