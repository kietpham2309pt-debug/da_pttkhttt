package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiSanPham {
    CHUA_MO_BAN("Chưa mở bán"),
    DANG_BAN("Đang bán"),
    NGUNG_BAN("Ngừng bán");

    private final String nhan;

    TrangThaiSanPham(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
