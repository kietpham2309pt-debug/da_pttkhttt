package vn.edu.huit.pttk.diengiadung.thucthe;

public enum LoaiKhuyenMai {
    GIAM_THEO_SAN_PHAM("Giảm theo sản phẩm"),
    MA_GIAM_GIA("Mã giảm giá");

    private final String nhan;

    LoaiKhuyenMai(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
