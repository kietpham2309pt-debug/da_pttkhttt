package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BaoCaoDoanhThu(LocalDate tuNgay, LocalDate denNgay, String nhom, String tenNhom, List<Dong> dong, Dong tong,
                             List<DonChuaXacNhan> chuaXacNhan, long tienChuaXacNhan, String ghiChu, LocalDateTime thoiDiemLap) {

    public record Dong(String nhan, long soDon, long tienHang, long tienGiam, long doanhThu, long daThu, int phanTram) {
    }

    public record DonChuaXacNhan(Long maDonHang, Long maPhieuGiao, LocalDate ngayGiao, long tienThuKhiGiao) {
    }

    public boolean coDuLieu() {
        return !dong.isEmpty();
    }
}
