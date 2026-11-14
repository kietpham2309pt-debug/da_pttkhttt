package vn.edu.huit.pttk.diengiadung.thucthe;

public enum LoaiViecGiao {
    GIAO("Giao hàng"),
    GIAO_LAP("Giao kèm lắp đặt"),
    KHAO_SAT("Khảo sát lắp đặt");

    private final String nhan;

    LoaiViecGiao(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
