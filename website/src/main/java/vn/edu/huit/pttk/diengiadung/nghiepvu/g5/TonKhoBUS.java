package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SanPhamTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SerialTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class TonKhoBUS {

    public static final int TON_TOI_THIEU = 2;

    public record DongTon(Long maSanPham, String tenSanPham, String model, String danhMuc, Map<TrangThaiSerial, Long> dem) {

        public long so(TrangThaiSerial tt) {
            return dem.getOrDefault(tt, 0L);
        }

        public long trongKho() {
            return so(TrangThaiSerial.TRONG_KHO);
        }

        public boolean duoiToiThieu() {
            return trongKho() <= TON_TOI_THIEU;
        }

        public long tong() {
            return dem.values().stream().mapToLong(Long::longValue).sum();
        }
    }

    public record DongKiemKe(Long maSanPham, String tenSanPham, String model, long heThong, Long thucTe) {

        public Long chenhLech() {
            return thucTe == null ? null : thucTe - heThong;
        }
    }

    private final SanPhamTruyVanG5 sanPhamDAO;
    private final SerialTruyVanG5 serialDAO;

    public TonKhoBUS(SanPhamTruyVanG5 sanPhamDAO, SerialTruyVanG5 serialDAO) {
        this.sanPhamDAO = sanPhamDAO;
        this.serialDAO = serialDAO;
    }

    @Transactional(readOnly = true)
    public List<DongTon> tonTheoMau() {
        Map<Long, Map<TrangThaiSerial, Long>> dem = new HashMap<>();
        for (Object[] r : serialDAO.demTheoMauVaTrangThai()) {
            dem.computeIfAbsent((Long) r[0], k -> new EnumMap<>(TrangThaiSerial.class)).put((TrangThaiSerial) r[1], (Long) r[2]);
        }
        List<DongTon> kq = new ArrayList<>();
        for (SanPham sp : sanPhamDAO.tatCaTheoDanhMuc()) {
            kq.add(new DongTon(sp.getMaSanPham(), sp.getTenSanPham(), sp.getModel(), sp.getDanhMuc().getTenDanhMuc(),
                    dem.getOrDefault(sp.getMaSanPham(), new EnumMap<>(TrangThaiSerial.class))));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> trongKhoTheoMau() {
        Map<Long, Long> kq = new HashMap<>();
        for (Object[] r : serialDAO.demTheoMauVaTrangThai()) {
            if (r[1] == TrangThaiSerial.TRONG_KHO) {
                kq.put((Long) r[0], (Long) r[2]);
            }
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<DongKiemKe> kiemKe(Map<Long, String> soDem) {
        List<DongKiemKe> kq = new ArrayList<>();
        boolean coNhap = false;
        for (DongTon d : tonTheoMau()) {
            String s = soDem.get(d.maSanPham());
            Long thucTe = null;
            if (s != null && !s.isBlank()) {
                try {
                    thucTe = Long.parseLong(s.trim());
                } catch (NumberFormatException e) {
                    throw new LoiNghiepVu("Số đếm của mẫu " + d.model() + " không phải số nguyên.");
                }
                if (thucTe < 0) {
                    throw new LoiNghiepVu("Số đếm của mẫu " + d.model() + " không được âm.");
                }
                coNhap = true;
            }
            kq.add(new DongKiemKe(d.maSanPham(), d.tenSanPham(), d.model(), d.trongKho(), thucTe));
        }
        if (!coNhap) {
            throw new LoiNghiepVu("Chưa nhập số đếm thực tế của mẫu máy nào.");
        }
        return kq;
    }
}
