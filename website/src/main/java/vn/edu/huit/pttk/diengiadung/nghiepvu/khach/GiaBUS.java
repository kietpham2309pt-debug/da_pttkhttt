package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhuyenMaiTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.TonKhoTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.KieuGiam;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class GiaBUS {

    public static final long NGUONG_MIEN_PHI_LAP = 5_000_000L;
    public static final long PHI_LAP_DON_NHO = 250_000L;
    public static final long NGUONG_COC = 10_000_000L;
    public static final int PHAN_TRAM_COC = 10;

    private final KhuyenMaiTruyVanKhach khuyenMaiTV;
    private final TonKhoTruyVanKhach tonKhoTV;

    public GiaBUS(KhuyenMaiTruyVanKhach khuyenMaiTV, TonKhoTruyVanKhach tonKhoTV) {
        this.khuyenMaiTV = khuyenMaiTV;
        this.tonKhoTV = tonKhoTV;
    }

    public static String anhCua(SanPham sp) {
        if (sp.getAnh() != null && !sp.getAnh().isBlank()) {
            return sp.getAnh();
        }
        return "/assets/img/sp-" + (sp.getDanhMuc() == null ? "bep-dien-tu" : sp.getDanhMuc().getMaDanhMuc()) + ".jpg";
    }

    public static long giamMoiChiec(KhuyenMai km, long giaBan) {
        long giam;
        if (km.getKieuGiam() == KieuGiam.PHAN_TRAM) {
            giam = giaBan * km.getMucGiam() / 100;
            if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0) {
                giam = Math.min(giam, km.getGiamToiDa());
            }
        } else {
            giam = km.getMucGiam();
        }
        return Math.max(0, Math.min(giam, giaBan));
    }

    @Transactional(readOnly = true)
    public Map<Long, KhuyenMai> khuyenMaiDangApDung(Collection<SanPham> dsSanPham) {
        Map<Long, KhuyenMai> ketQua = new HashMap<>();
        Map<Long, Long> giamTot = new HashMap<>();
        List<KhuyenMai> dsKm = khuyenMaiTV.dangChay(LoaiKhuyenMai.GIAM_THEO_SAN_PHAM, TrangThaiKhuyenMai.DANG_CHAY, LocalDate.now());
        for (SanPham sp : dsSanPham) {
            if (sp.getGiaBan() == null) {
                continue;
            }
            for (KhuyenMai km : dsKm) {
                boolean ap = km.getSanPham().stream().anyMatch(x -> x.getMaSanPham().equals(sp.getMaSanPham()));
                if (!ap) {
                    continue;
                }
                long giam = giamMoiChiec(km, sp.getGiaBan());
                if (giam > giamTot.getOrDefault(sp.getMaSanPham(), 0L)) {
                    giamTot.put(sp.getMaSanPham(), giam);
                    ketQua.put(sp.getMaSanPham(), km);
                }
            }
        }
        return ketQua;
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> tonKhaDung(Collection<Long> maSanPham) {
        Map<Long, Integer> ketQua = new HashMap<>();
        if (maSanPham.isEmpty()) {
            return ketQua;
        }
        for (Long ma : maSanPham) {
            ketQua.put(ma, 0);
        }
        for (Object[] dong : tonKhoTV.demTrongKho(TrangThaiSerial.TRONG_KHO, maSanPham)) {
            ketQua.put((Long) dong[0], ((Number) dong[1]).intValue());
        }
        for (Object[] dong : tonKhoTV.demDangGiu(maSanPham, List.of(TrangThaiDonHang.CHO_XAC_NHAN, TrangThaiDonHang.DA_XAC_NHAN))) {
            Long ma = (Long) dong[0];
            int giu = dong[1] == null ? 0 : ((Number) dong[1]).intValue();
            ketQua.put(ma, Math.max(0, ketQua.getOrDefault(ma, 0) - giu));
        }
        return ketQua;
    }

    @Transactional(readOnly = true)
    public List<TheSanPham> dongThe(Collection<SanPham> dsSanPham) {
        Map<Long, KhuyenMai> km = khuyenMaiDangApDung(dsSanPham);
        Map<Long, Integer> ton = tonKhaDung(dsSanPham.stream().map(SanPham::getMaSanPham).toList());
        List<TheSanPham> ds = new ArrayList<>();
        for (SanPham sp : dsSanPham) {
            KhuyenMai k = km.get(sp.getMaSanPham());
            long giam = k == null || sp.getGiaBan() == null ? 0 : giamMoiChiec(k, sp.getGiaBan());
            sp.getThuongHieu().getTenThuongHieu();
            sp.getDanhMuc().getTenDanhMuc();
            ds.add(new TheSanPham(sp, giam, k, ton.getOrDefault(sp.getMaSanPham(), 0)));
        }
        return ds;
    }

    public KhuyenMai kiemMaGiamGia(String maCode, long giaTriDon) {
        if (maCode == null || maCode.isBlank()) {
            return null;
        }
        KhuyenMai km = khuyenMaiTV.findFirstByMaCodeIgnoreCaseAndLoai(maCode.trim(), LoaiKhuyenMai.MA_GIAM_GIA)
                .orElseThrow(() -> new LoiNghiepVu("Mã giảm giá không tồn tại."));
        LocalDate homNay = LocalDate.now();
        if (km.getTrangThai() != TrangThaiKhuyenMai.DANG_CHAY) {
            throw new LoiNghiepVu("Mã giảm giá đang tạm dừng.");
        }
        if (km.getNgayBatDau() != null && homNay.isBefore(km.getNgayBatDau())) {
            throw new LoiNghiepVu("Mã giảm giá chưa tới ngày áp dụng.");
        }
        if (km.getNgayKetThuc() != null && homNay.isAfter(km.getNgayKetThuc())) {
            throw new LoiNghiepVu("Mã giảm giá đã hết hạn.");
        }
        if (km.getSoLuotToiDa() != null && km.getSoLuotDaDung() != null && km.getSoLuotDaDung() >= km.getSoLuotToiDa()) {
            throw new LoiNghiepVu("Mã giảm giá đã hết lượt dùng.");
        }
        if (km.getDonToiThieu() != null && giaTriDon < km.getDonToiThieu()) {
            throw new LoiNghiepVu("Mã này áp cho đơn từ " + DinhDangTien.tien(km.getDonToiThieu()) + " trở lên.");
        }
        return km;
    }

    public static long phiLapDat(boolean coLap, long giaTriSauGiam) {
        if (!coLap) {
            return 0;
        }
        return giaTriSauGiam > NGUONG_MIEN_PHI_LAP ? 0 : PHI_LAP_DON_NHO;
    }

    public static long tienCoc(long tongTien) {
        return tongTien > NGUONG_COC ? tongTien * PHAN_TRAM_COC / 100 : 0;
    }
}
