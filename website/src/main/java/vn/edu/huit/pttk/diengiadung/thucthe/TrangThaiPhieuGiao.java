package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiPhieuGiao {
    DA_PHAN_CONG("Đã phân công"),
    DA_GIAO("Đã giao"),
    KHONG_THANH_CONG("Không thành công");

    private final String nhan;

    TrangThaiPhieuGiao(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
