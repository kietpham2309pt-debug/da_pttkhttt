package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import org.springframework.stereotype.Component;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;

@Component
public class QuyenG5 {

    public static class KhongCoQuyen extends RuntimeException {

        public KhongCoQuyen() {
            super("Không có quyền");
        }
    }

    private final TaiKhoanBUS taiKhoanBUS;

    public QuyenG5(TaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
    }

    public NhanVien batBuoc(String... vai) {
        for (String v : vai) {
            if (taiKhoanBUS.coVai(v)) {
                return taiKhoanBUS.batBuocNhanVien();
            }
        }
        throw new KhongCoQuyen();
    }
}
