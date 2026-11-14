package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiDonHang {
    CHO_XAC_NHAN("Chờ xác nhận"),
    DA_XAC_NHAN("Đã xác nhận"),
    DANG_GIAO("Đang giao"),
    HOAN_THANH("Hoàn thành"),
    GIAO_KHONG_THANH_CONG("Giao không thành công"),
    DA_HUY("Đã huỷ"),
    DA_TRA_HANG("Đã trả hàng");

    private final String nhan;

    TrangThaiDonHang(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
