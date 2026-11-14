package vn.edu.huit.pttk.diengiadung.thucthe;

public enum TrangThaiBaoHanh {
    DA_TIEP_NHAN("Đã tiếp nhận"),
    DA_GUI_HANG("Đã gửi hãng"),
    DA_NHAN_VE("Đã nhận về"),
    DA_TRA_KHACH("Đã trả khách");

    private final String nhan;

    TrangThaiBaoHanh(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
