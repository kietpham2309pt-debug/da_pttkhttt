package vn.edu.huit.pttk.diengiadung.thucthe;

public enum LoaiLyDoDoiTra {
    LOI_NHA_SAN_XUAT("Lỗi nhà sản xuất"),
    KHONG_DUNG_MO_TA("Không đúng mô tả"),
    DOI_Y("Khách đổi ý");

    private final String nhan;

    LoaiLyDoDoiTra(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
