package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.HauMaiTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.TonKhoTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class BaoHanhKhachBUS {

    private final TonKhoTruyVanKhach tonKhoTV;
    private final HauMaiTruyVanKhach hauMaiTV;

    public BaoHanhKhachBUS(TonKhoTruyVanKhach tonKhoTV, HauMaiTruyVanKhach hauMaiTV) {
        this.tonKhoTV = tonKhoTV;
        this.hauMaiTV = hauMaiTV;
    }

    @Transactional(readOnly = true)
    public List<KetQuaBaoHanh> traTheoSerial(String soSerial) {
        if (soSerial == null || soSerial.isBlank()) {
            throw new LoiNghiepVu("Bạn chưa nhập số serial.");
        }
        List<SanPhamSerial> ds = tonKhoTV.theoSoSerial(soSerial.trim());
        if (ds.isEmpty()) {
            throw new LoiNghiepVu("Không tìm thấy máy có số serial này. Bạn xem lại tem serial dán trên máy hoặc trên phiếu bảo hành.");
        }
        SanPhamSerial may = ds.get(0);
        if (may.getNgayBan() == null) {
            throw new LoiNghiepVu("Máy có số serial này chưa bán ra nên chưa có thông tin bảo hành.");
        }
        return List.of(dong(may));
    }

    @Transactional(readOnly = true)
    public List<KetQuaBaoHanh> traTheoSoDienThoai(String soDienThoai, Long maDon) {
        if (soDienThoai == null || !soDienThoai.trim().matches("^0\\d{9}$")) {
            throw new LoiNghiepVu("Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        List<KetQuaBaoHanh> kq = new ArrayList<>();
        for (SanPhamSerial may : tonKhoTV.theoSoDienThoai(soDienThoai.trim())) {
            if (maDon == null || may.getDonHangBan().getMaDonHang().equals(maDon)) {
                kq.add(dong(may));
            }
        }
        if (kq.isEmpty()) {
            throw new LoiNghiepVu("Không tìm thấy máy đã bán theo số điện thoại" + (maDon == null ? "" : " và mã đơn") + " này.");
        }
        return kq;
    }

    private KetQuaBaoHanh dong(SanPhamSerial may) {
        int thang = may.getSanPham().getThoiHanBaoHanh() == null ? 0 : may.getSanPham().getThoiHanBaoHanh();
        LocalDate ngayBan = may.getNgayBan().toLocalDate();
        LocalDate han = ngayBan.plusMonths(thang);
        List<PhieuBaoHanh> phieu = hauMaiTV.phieuBaoHanh(may.getSoSerial());
        Long maDon = may.getDonHangBan() == null ? null : may.getDonHangBan().getMaDonHang();
        boolean daTraLai = may.getTrangThai() != TrangThaiSerial.DA_BAN && may.getTrangThai() != TrangThaiSerial.DANG_BAO_HANH;
        return new KetQuaBaoHanh(may.getSoSerial(), may.getSanPham().getTenSanPham(), may.getSanPham().getModel(),
                may.getSanPham().getThuongHieu().getTenThuongHieu(), maDon, ngayBan, thang, han,
                !daTraLai && !LocalDate.now().isAfter(han), daTraLai, may.getTrangThai().getNhan(), phieu);
    }

    public static class KetQuaBaoHanh {

        private final String soSerial;
        private final String tenSanPham;
        private final String model;
        private final String thuongHieu;
        private final Long maDon;
        private final LocalDate ngayBan;
        private final int thoiHanThang;
        private final LocalDate hanBaoHanh;
        private final boolean conHan;
        private final boolean daTraLai;
        private final String trangThaiMay;
        private final List<PhieuBaoHanh> phieu;

        public KetQuaBaoHanh(String soSerial, String tenSanPham, String model, String thuongHieu, Long maDon, LocalDate ngayBan,
                             int thoiHanThang, LocalDate hanBaoHanh, boolean conHan, boolean daTraLai, String trangThaiMay,
                             List<PhieuBaoHanh> phieu) {
            this.soSerial = soSerial;
            this.tenSanPham = tenSanPham;
            this.model = model;
            this.thuongHieu = thuongHieu;
            this.maDon = maDon;
            this.ngayBan = ngayBan;
            this.thoiHanThang = thoiHanThang;
            this.hanBaoHanh = hanBaoHanh;
            this.conHan = conHan;
            this.daTraLai = daTraLai;
            this.trangThaiMay = trangThaiMay;
            this.phieu = phieu;
        }

        public String getSoSerial() {
            return soSerial;
        }

        public String getTenSanPham() {
            return tenSanPham;
        }

        public String getModel() {
            return model;
        }

        public String getThuongHieu() {
            return thuongHieu;
        }

        public Long getMaDon() {
            return maDon;
        }

        public LocalDate getNgayBan() {
            return ngayBan;
        }

        public int getThoiHanThang() {
            return thoiHanThang;
        }

        public LocalDate getHanBaoHanh() {
            return hanBaoHanh;
        }

        public boolean isConHan() {
            return conHan;
        }

        public boolean isDaTraLai() {
            return daTraLai;
        }

        public String getTrangThaiMay() {
            return trangThaiMay;
        }

        public List<PhieuBaoHanh> getPhieu() {
            return phieu;
        }

        public int getSoLanBaoHanh() {
            return phieu.size();
        }
    }
}
