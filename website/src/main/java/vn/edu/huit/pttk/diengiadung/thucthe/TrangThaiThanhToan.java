package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiThanhToan {
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_DAT_COC("Đã đặt cọc"),
    DA_THANH_TOAN("Đã thanh toán"),
    DA_HOAN_TIEN("Đã hoàn tiền");

    private final String nhan;

    TrangThaiThanhToan(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
