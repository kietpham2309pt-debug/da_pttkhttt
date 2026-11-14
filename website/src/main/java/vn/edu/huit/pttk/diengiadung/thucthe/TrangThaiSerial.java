package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiSerial {
    TRONG_KHO("Trong kho"),
    DA_XUAT("Đã xuất"),
    DA_BAN("Đã bán"),
    DANG_BAO_HANH("Đang bảo hành"),
    TRA_VE_KHO("Trả về kho"),
    LOI_CHO_GUI_HANG("Lỗi chờ gửi hãng");

    private final String nhan;

    TrangThaiSerial(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
