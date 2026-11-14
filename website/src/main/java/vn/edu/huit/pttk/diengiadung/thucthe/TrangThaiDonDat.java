package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiDonDat {
    DE_XUAT("Đề xuất"),
    DA_GUI("Đã gửi"),
    DA_NHAN("Đã nhận"),
    DA_HUY("Đã huỷ");

    private final String nhan;

    TrangThaiDonDat(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
