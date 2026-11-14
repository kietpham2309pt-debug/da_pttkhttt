package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import org.springframework.stereotype.Component;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;

@Component
public class QuyenDonKhach {

    private final PhienKhach phien;
    private final TaiKhoanBUS taiKhoanBUS;
    private final DonHangKhachBUS donHangKhachBUS;

    public QuyenDonKhach(PhienKhach phien, TaiKhoanBUS taiKhoanBUS, DonHangKhachBUS donHangKhachBUS) {
        this.phien = phien;
        this.taiKhoanBUS = taiKhoanBUS;
        this.donHangKhachBUS = donHangKhachBUS;
    }

    public boolean duocXem(Long maDon) {
        if (maDon == null) {
            return false;
        }
        if (phien.duocXemDon(maDon)) {
            return true;
        }
        return donHangKhachBUS.laChuDon(maDon, taiKhoanBUS.khachHangHienTai().orElse(null));
    }
}
