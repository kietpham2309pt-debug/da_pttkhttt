package vn.edu.huit.pttk.diengiadung.thucthe;

public enum KieuGiam {
    PHAN_TRAM("Phần trăm"),
    SO_TIEN("Số tiền");

    private final String nhan;

    KieuGiam(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
