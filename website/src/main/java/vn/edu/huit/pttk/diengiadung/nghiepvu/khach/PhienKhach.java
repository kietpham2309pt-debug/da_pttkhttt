package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class PhienKhach implements Serializable {

    public static final int SO_LAN_TRA_SAI_TOI_DA = 5;
    public static final int PHUT_KHOA_TRA = 15;
    public static final int SO_MAU_SO_SANH = 3;
    public static final int PHUT_HIEU_LUC_THANH_TOAN = 15;

    private final Set<Long> donDuocXem = new LinkedHashSet<>();
    private final List<Long> soSanh = new ArrayList<>();
    private final Map<String, YeuCauThanhToan> yeuCauThanhToan = new HashMap<>();
    private int soLanTraSai;
    private LocalDateTime khoaTraDen;
    private PhieuDatHang banNhap;
    private String maLuotDat;
    private Long donVuaDat;

    public synchronized void choXemDon(Long maDon) {
        donDuocXem.add(maDon);
    }

    public synchronized boolean duocXemDon(Long maDon) {
        return donDuocXem.contains(maDon);
    }

    public synchronized boolean dangKhoaTra() {
        if (khoaTraDen != null && khoaTraDen.isAfter(LocalDateTime.now())) {
            return true;
        }
        khoaTraDen = null;
        return false;
    }

    public synchronized LocalDateTime getKhoaTraDen() {
        return khoaTraDen;
    }

    public synchronized boolean ghiTraSai() {
        soLanTraSai++;
        if (soLanTraSai >= SO_LAN_TRA_SAI_TOI_DA) {
            soLanTraSai = 0;
            khoaTraDen = LocalDateTime.now().plusMinutes(PHUT_KHOA_TRA);
            return true;
        }
        return false;
    }

    public synchronized int soLanConLai() {
        return SO_LAN_TRA_SAI_TOI_DA - soLanTraSai;
    }

    public synchronized void ghiTraDung() {
        soLanTraSai = 0;
    }

    public synchronized List<Long> getSoSanh() {
        return new ArrayList<>(soSanh);
    }

    public synchronized boolean themSoSanh(Long ma) {
        if (soSanh.contains(ma)) {
            return true;
        }
        if (soSanh.size() >= SO_MAU_SO_SANH) {
            return false;
        }
        soSanh.add(ma);
        return true;
    }

    public synchronized void boSoSanh(Long ma) {
        soSanh.remove(ma);
    }

    public synchronized void datSoSanh(List<Long> ds) {
        soSanh.clear();
        for (Long ma : ds) {
            if (!soSanh.contains(ma) && soSanh.size() < SO_MAU_SO_SANH) {
                soSanh.add(ma);
            }
        }
    }

    public synchronized PhieuDatHang getBanNhap() {
        return banNhap;
    }

    public synchronized void setBanNhap(PhieuDatHang banNhap) {
        this.banNhap = banNhap;
    }

    public synchronized String capMaLuotDat() {
        maLuotDat = UUID.randomUUID().toString();
        return maLuotDat;
    }

    public synchronized boolean dungMaLuotDat(String ma) {
        if (ma == null || !ma.equals(maLuotDat)) {
            return false;
        }
        maLuotDat = null;
        return true;
    }

    public synchronized void traLaiMaLuotDat(String ma) {
        maLuotDat = ma;
    }

    public synchronized Long getDonVuaDat() {
        return donVuaDat;
    }

    public synchronized void setDonVuaDat(Long donVuaDat) {
        this.donVuaDat = donVuaDat;
    }

    public synchronized YeuCauThanhToan capYeuCau(Long maDon) {
        String ma = "MOCK-" + maDon + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        YeuCauThanhToan yc = new YeuCauThanhToan(ma, maDon, LocalDateTime.now().plusMinutes(PHUT_HIEU_LUC_THANH_TOAN));
        yeuCauThanhToan.put(ma, yc);
        return yc;
    }

    public synchronized YeuCauThanhToan layYeuCau(String maCong) {
        return maCong == null ? null : yeuCauThanhToan.get(maCong);
    }

    public static class YeuCauThanhToan implements Serializable {

        private final String maCong;
        private final Long maDon;
        private final LocalDateTime hetHan;

        public YeuCauThanhToan(String maCong, Long maDon, LocalDateTime hetHan) {
            this.maCong = maCong;
            this.maDon = maDon;
            this.hetHan = hetHan;
        }

        public String getMaCong() {
            return maCong;
        }

        public Long getMaDon() {
            return maDon;
        }

        public LocalDateTime getHetHan() {
            return hetHan;
        }

        public boolean isHetHan() {
            return hetHan.isBefore(LocalDateTime.now());
        }
    }
}
