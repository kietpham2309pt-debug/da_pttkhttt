package vn.edu.huit.pttk.diengiadung.thucthe;

public enum LoaiGiaoDich {
    COC("Đặt cọc"),
    THANH_TOAN("Thanh toán"),
    HOAN_TIEN("Hoàn tiền");

    private final String nhan;

    LoaiGiaoDich(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
