package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.DonHangTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.PhieuGiaoTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.PhieuXuatTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SerialTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.YeuCauDoiTraTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.SerialBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.HinhThucDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.YeuCauDoiTra;

@Service
public class PhieuXuatBUS {

    public static final String LOAI_DON = "don";
    public static final String LOAI_DOI = "doi";

    public record DonChoXuat(DonHang don, String nguoiNhanHang, LocalDate ngayHen, LocalTime gioHen) {
    }

    public record YeuCauChoXuat(YeuCauDoiTra yeuCau, String mauMay) {
    }

    public record DongXuat(Long maSanPham, String tenSanPham, String model, int canXuat, List<SanPhamSerial> coSan,
                           Set<String> goiY, boolean chiMayGiuRieng) {

        public boolean thieu() {
            return coSan.size() < canXuat;
        }
    }

    public record PhieuXuatNhap(String loai, Long ma, DonHang don, YeuCauDoiTra yeuCau, String lyDo, String nguoiNhanHang,
                                List<DongXuat> dong) {

        public boolean duMay() {
            return dong.stream().noneMatch(DongXuat::thieu);
        }
    }

    public record DongXuatXem(String tenSanPham, String model, int yeuCau, List<SanPhamSerial> serial) {
    }

    public record PhieuXuatXem(PhieuXuat phieu, String nguoiNhanHang, List<DongXuatXem> dong) {
    }

    private final PhieuXuatTruyVanG5 phieuXuatDAO;
    private final DonHangTruyVanG5 donHangDAO;
    private final SerialTruyVanG5 serialDAO;
    private final YeuCauDoiTraTruyVanG5 yeuCauDAO;
    private final PhieuGiaoTruyVanG5 phieuGiaoDAO;
    private final DonHangBUS donHangBUS;
    private final SerialBUS serialBUS;

    public PhieuXuatBUS(PhieuXuatTruyVanG5 phieuXuatDAO, DonHangTruyVanG5 donHangDAO, SerialTruyVanG5 serialDAO,
                        YeuCauDoiTraTruyVanG5 yeuCauDAO, PhieuGiaoTruyVanG5 phieuGiaoDAO, DonHangBUS donHangBUS, SerialBUS serialBUS) {
        this.phieuXuatDAO = phieuXuatDAO;
        this.donHangDAO = donHangDAO;
        this.serialDAO = serialDAO;
        this.yeuCauDAO = yeuCauDAO;
        this.phieuGiaoDAO = phieuGiaoDAO;
        this.donHangBUS = donHangBUS;
        this.serialBUS = serialBUS;
    }

    public static String lyDoDoiMay(Long maYeuCau) {
        return "Xuất đổi máy cho yêu cầu #" + maYeuCau;
    }

    private static String maDon(Long ma) {
        return String.format("DH%06d", ma);
    }

    @Transactional(readOnly = true)
    public List<DonChoXuat> donChoXuat() {
        List<DonHang> ds = donHangDAO.chuaCoPhieuXuat(TrangThaiDonHang.DA_XAC_NHAN);
        Map<Long, PhieuGiaoHang> giao = phieuGiaoTheoDon(ds.stream().map(DonHang::getMaDonHang).toList());
        List<DonChoXuat> kq = new ArrayList<>();
        for (DonHang d : ds) {
            PhieuGiaoHang g = giao.get(d.getMaDonHang());
            kq.add(new DonChoXuat(d, nguoiNhan(g), g == null ? null : g.getNgayHen(), g == null ? null : g.getGioBatDau()));
        }
        kq.sort(Comparator.comparing(DonChoXuat::ngayHen, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(DonChoXuat::gioHen, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(x -> x.don().getNgayDat()));
        return kq;
    }

    @Transactional(readOnly = true)
    public List<YeuCauChoXuat> yeuCauDoiMayChoXuat() {
        List<YeuCauChoXuat> kq = new ArrayList<>();
        for (YeuCauDoiTra y : yeuCauDAO.theoTrangThaiVaHinhThuc(TrangThaiDoiTra.DA_DUYET, HinhThucDoiTra.DOI_MAY)) {
            if (phieuXuatDAO.existsByLyDoXuat(lyDoDoiMay(y.getMaYeuCau()))) {
                continue;
            }
            String mau = y.getSerial().stream().map(s -> s.getSanPham().getModel()).collect(Collectors.joining(", "));
            kq.add(new YeuCauChoXuat(y, mau));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<PhieuXuat> danhSach() {
        return phieuXuatDAO.danhSach();
    }

    @Transactional(readOnly = true)
    public PhieuXuatXem xem(Long ma) {
        PhieuXuat p = phieuXuatDAO.chiTiet(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy phiếu xuất #" + ma));
        Map<Long, Integer> yeuCau = new HashMap<>();
        boolean theoDon = !p.getLyDoXuat().startsWith("Xuất đổi máy");
        if (theoDon) {
            donHangDAO.kemChiTiet(p.getDonHang().getMaDonHang()).ifPresent(d -> {
                for (ChiTietDonHang c : d.getChiTiet()) {
                    yeuCau.merge(c.getSanPham().getMaSanPham(), c.getSoLuong(), Integer::sum);
                }
            });
        }
        Map<Long, List<SanPhamSerial>> nhom = new LinkedHashMap<>();
        for (SanPhamSerial s : p.getSerial()) {
            nhom.computeIfAbsent(s.getSanPham().getMaSanPham(), k -> new ArrayList<>()).add(s);
        }
        List<DongXuatXem> dong = new ArrayList<>();
        for (List<SanPhamSerial> ds : nhom.values()) {
            SanPham sp = ds.get(0).getSanPham();
            ds.sort(Comparator.comparing(SanPhamSerial::getSoSerial));
            dong.add(new DongXuatXem(sp.getTenSanPham(), sp.getModel(), yeuCau.getOrDefault(sp.getMaSanPham(), ds.size()), ds));
        }
        String nguoiNhan = theoDon ? nguoiNhan(phieuGiaoTheoDon(List.of(p.getDonHang().getMaDonHang())).get(p.getDonHang().getMaDonHang())) : "Khách nhận tại cửa hàng hoặc giao kèm yêu cầu đổi";
        return new PhieuXuatXem(p, nguoiNhan, dong);
    }

    @Transactional(readOnly = true)
    public PhieuXuatNhap taoPhieu(String loai, Long ma) {
        if (LOAI_DOI.equals(loai)) {
            return taoPhieuDoiMay(ma);
        }
        return taoPhieuTheoDon(ma);
    }

    private PhieuXuatNhap taoPhieuTheoDon(Long maDon) {
        DonHang don = donHangDAO.kemChiTiet(maDon).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn " + maDon(maDon)));
        if (don.getTrangThai() != TrangThaiDonHang.DA_XAC_NHAN) {
            throw new LoiNghiepVu("Đơn " + maDon(maDon) + " đã ở trạng thái \"" + don.getTrangThai().getNhan() + "\", không xuất kho được.");
        }
        if (donHangDAO.daCoPhieuXuat(maDon)) {
            throw new LoiNghiepVu("Đơn " + maDon(maDon) + " đã có phiếu xuất kho.");
        }
        Map<Long, Integer> canXuat = new LinkedHashMap<>();
        Map<Long, SanPham> mau = new HashMap<>();
        for (ChiTietDonHang c : don.getChiTiet()) {
            canXuat.merge(c.getSanPham().getMaSanPham(), c.getSoLuong(), Integer::sum);
            mau.put(c.getSanPham().getMaSanPham(), c.getSanPham());
        }
        List<DongXuat> dong = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : canXuat.entrySet()) {
            dong.add(dongXuat(mau.get(e.getKey()), e.getValue(), maDon));
        }
        PhieuGiaoHang g = phieuGiaoTheoDon(List.of(maDon)).get(maDon);
        return new PhieuXuatNhap(LOAI_DON, maDon, don, null, "Xuất bán theo đơn " + maDon(maDon), nguoiNhan(g), dong);
    }

    private PhieuXuatNhap taoPhieuDoiMay(Long maYeuCau) {
        YeuCauDoiTra y = yeuCauDAO.kemSerial(maYeuCau).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy yêu cầu đổi trả #" + maYeuCau));
        if (y.getTrangThai() != TrangThaiDoiTra.DA_DUYET || y.getHinhThuc() != HinhThucDoiTra.DOI_MAY) {
            throw new LoiNghiepVu("Yêu cầu #" + maYeuCau + " không phải yêu cầu đổi máy đã duyệt (đang \"" + y.getTrangThai().getNhan() + "\").");
        }
        if (phieuXuatDAO.existsByLyDoXuat(lyDoDoiMay(maYeuCau))) {
            throw new LoiNghiepVu("Đã xuất máy đổi cho yêu cầu #" + maYeuCau + ".");
        }
        Map<Long, Integer> canXuat = new LinkedHashMap<>();
        Map<Long, SanPham> mau = new HashMap<>();
        for (SanPhamSerial s : y.getSerial()) {
            canXuat.merge(s.getSanPham().getMaSanPham(), 1, Integer::sum);
            mau.put(s.getSanPham().getMaSanPham(), s.getSanPham());
        }
        if (canXuat.isEmpty()) {
            throw new LoiNghiepVu("Yêu cầu #" + maYeuCau + " chưa ghi chiếc máy cần đổi.");
        }
        List<DongXuat> dong = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : canXuat.entrySet()) {
            dong.add(dongXuat(mau.get(e.getKey()), e.getValue(), null));
        }
        return new PhieuXuatNhap(LOAI_DOI, maYeuCau, y.getDonHang(), y, lyDoDoiMay(maYeuCau), "Khách đổi máy", dong);
    }

    private DongXuat dongXuat(SanPham sp, int can, Long maDon) {
        List<SanPhamSerial> trongKho = serialDAO.theoMauVaTrangThai(sp.getMaSanPham(), TrangThaiSerial.TRONG_KHO);
        List<SanPhamSerial> giuChoDon = new ArrayList<>();
        List<SanPhamSerial> tuDo = new ArrayList<>();
        for (SanPhamSerial s : trongKho) {
            DonHang giu = s.getDonHangBan();
            if (giu != null && maDon != null && giu.getMaDonHang().equals(maDon)) {
                giuChoDon.add(s);
            } else if (!dangGiuChoDonKhac(s, maDon)) {
                tuDo.add(s);
            }
        }
        boolean chiGiu = !giuChoDon.isEmpty();
        List<SanPhamSerial> coSan = chiGiu ? giuChoDon : tuDo;
        Set<String> goiY = coSan.stream().limit(can).map(SanPhamSerial::getSoSerial).collect(Collectors.toCollection(LinkedHashSet::new));
        return new DongXuat(sp.getMaSanPham(), sp.getTenSanPham(), sp.getModel(), can, coSan, goiY, chiGiu);
    }

    private static boolean dangGiuChoDonKhac(SanPhamSerial s, Long maDon) {
        DonHang giu = s.getDonHangBan();
        if (giu == null || (maDon != null && giu.getMaDonHang().equals(maDon))) {
            return false;
        }
        return giu.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN || giu.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long luu(String loai, Long ma, List<String> serialChon, NhanVien thuKho) {
        PhieuXuatNhap f = taoPhieu(loai, ma);
        Long maDon = LOAI_DON.equals(loai) ? ma : null;
        if (!f.duMay()) {
            DongXuat d = f.dong().stream().filter(DongXuat::thieu).findFirst().orElseThrow();
            throw new LoiNghiepVu("Mẫu " + d.model() + " còn " + d.coSan().size() + ", đơn cần " + d.canXuat()
                    + ". Chưa lưu phiếu; báo nhân viên bán hàng hẹn lại khách hoặc lập đơn đặt hàng nhà cung cấp.");
        }
        Set<String> chon = new LinkedHashSet<>();
        for (String s : serialChon == null ? List.<String>of() : serialChon) {
            String t = s == null ? "" : s.trim().toUpperCase();
            if (t.isEmpty()) {
                continue;
            }
            if (!chon.add(t)) {
                throw new LoiNghiepVu("Serial " + t + " đã chọn trên phiếu này, không nhận lần hai.");
            }
        }
        Map<String, SanPhamSerial> may = new HashMap<>();
        for (SanPhamSerial s : chon.isEmpty() ? List.<SanPhamSerial>of() : serialDAO.dayDuNhieu(chon)) {
            may.put(s.getSoSerial().toUpperCase(), s);
        }
        Map<Long, DongXuat> dongTheoMau = new HashMap<>();
        for (DongXuat d : f.dong()) {
            dongTheoMau.put(d.maSanPham(), d);
        }
        Map<Long, List<SanPhamSerial>> daChon = new HashMap<>();
        for (String so : chon) {
            SanPhamSerial s = may.get(so);
            if (s == null) {
                throw new LoiNghiepVu("Serial " + so + " không xuất được: không có trong hệ thống.");
            }
            DongXuat d = dongTheoMau.get(s.getSanPham().getMaSanPham());
            if (d == null) {
                throw new LoiNghiepVu("Serial " + so + " không xuất được: thuộc mẫu " + s.getSanPham().getModel() + ", không có trong phiếu.");
            }
            if (s.getTrangThai() != TrangThaiSerial.TRONG_KHO) {
                throw new LoiNghiepVu("Serial " + so + " không xuất được: đang \"" + s.getTrangThai().getNhan() + "\".");
            }
            if (dangGiuChoDonKhac(s, maDon)) {
                throw new LoiNghiepVu("Serial " + so + " không xuất được: đã giữ riêng cho đơn " + maDon(s.getDonHangBan().getMaDonHang()) + ".");
            }
            if (d.chiMayGiuRieng() && (s.getDonHangBan() == null || !s.getDonHangBan().getMaDonHang().equals(maDon))) {
                throw new LoiNghiepVu("Serial " + so + " không xuất được: đơn này có máy đặt riêng, chỉ xuất các máy đã giữ cho đơn.");
            }
            daChon.computeIfAbsent(d.maSanPham(), k -> new ArrayList<>()).add(s);
        }
        for (DongXuat d : f.dong()) {
            int n = daChon.getOrDefault(d.maSanPham(), List.of()).size();
            if (n != d.canXuat()) {
                throw new LoiNghiepVu("Mẫu " + d.model() + " cần " + d.canXuat() + " chiếc, đã chọn " + n + ".");
            }
        }
        LocalDateTime bayGio = LocalDateTime.now();
        DonHang don = f.don();
        PhieuXuat p = new PhieuXuat();
        p.setDonHang(don);
        p.setNgayXuat(bayGio);
        p.setLyDoXuat(f.lyDo());
        p.setThuKho(thuKho);
        for (List<SanPhamSerial> ds : daChon.values()) {
            for (SanPhamSerial s : ds) {
                s.setDonHangBan(don);
                serialBUS.doiTrangThai(s, TrangThaiSerial.DA_XUAT);
                p.getSerial().add(s);
            }
        }
        phieuXuatDAO.save(p);
        if (LOAI_DON.equals(loai)) {
            donHangBUS.doiTrangThai(don, TrangThaiDonHang.DANG_GIAO, thuKho, "Xuất kho theo phiếu xuất #" + p.getMaPhieuXuat());
        }
        return p.getMaPhieuXuat();
    }

    private Map<Long, PhieuGiaoHang> phieuGiaoTheoDon(List<Long> maDon) {
        Map<Long, PhieuGiaoHang> kq = new HashMap<>();
        if (maDon.isEmpty()) {
            return kq;
        }
        for (PhieuGiaoHang g : phieuGiaoDAO.theoDon(maDon)) {
            kq.putIfAbsent(g.getDonHang().getMaDonHang(), g);
        }
        return kq;
    }

    private static String nguoiNhan(PhieuGiaoHang g) {
        if (g == null) {
            return null;
        }
        if (g.getNhanVienGiao() != null) {
            return g.getNhanVienGiao().getHoTen();
        }
        if (g.getDonViVanChuyen() != null) {
            return g.getDonViVanChuyen() + (g.getMaVanDon() == null ? "" : " (vận đơn " + g.getMaVanDon() + ")");
        }
        return null;
    }
}
