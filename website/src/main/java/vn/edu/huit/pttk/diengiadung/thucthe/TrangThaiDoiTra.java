package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiDoiTra {
    CHO_DUYET("Chờ duyệt"),
    DA_DUYET("Đã duyệt"),
    TU_CHOI("Từ chối"),
    HOAN_TAT("Hoàn tất");

    private final String nhan;

    TrangThaiDoiTra(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
