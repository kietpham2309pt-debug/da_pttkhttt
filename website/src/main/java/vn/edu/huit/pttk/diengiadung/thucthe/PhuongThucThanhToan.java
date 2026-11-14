package vn.edu.huit.pttk.diengiadung.thucthe;

public enum PhuongThucThanhToan {
    TIEN_MAT_KHI_NHAN("Tiền mặt khi nhận hàng"),
    CONG_THANH_TOAN("Thanh toán trực tuyến");

    private final String nhan;

    PhuongThucThanhToan(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
