package vn.edu.huit.pttk.diengiadung.thucthe;

public enum HinhThucDoiTra {
    DOI_MAY("Đổi máy"),
    TRA_HANG("Trả hàng");

    private final String nhan;

    HinhThucDoiTra(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
