package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiGiaoDich {
    THANH_CONG("Thành công"),
    THAT_BAI("Thất bại");

    private final String nhan;

    TrangThaiGiaoDich(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
