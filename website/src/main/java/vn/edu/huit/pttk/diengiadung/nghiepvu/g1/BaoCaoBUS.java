package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.DanhMucTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.DonHangTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.KhoTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.SanPhamTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class BaoCaoBUS {

    public static final int MUC_TON_TOI_THIEU = 2;
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter THANG = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DonHangTruyVanG12 donHangTruyVan;
    private final KhoTruyVanG12 khoTruyVan;
    private final SanPhamTruyVanG12 sanPhamTruyVan;
    private final DanhMucTruyVanG12 danhMucTruyVan;

    public BaoCaoBUS(DonHangTruyVanG12 donHangTruyVan, KhoTruyVanG12 khoTruyVan, SanPhamTruyVanG12 sanPhamTruyVan,
                     DanhMucTruyVanG12 danhMucTruyVan) {
        this.donHangTruyVan = donHangTruyVan;
        this.khoTruyVan = khoTruyVan;
        this.sanPhamTruyVan = sanPhamTruyVan;
        this.danhMucTruyVan = danhMucTruyVan;
    }

    public static String tenNhom(String nhom) {
        return switch (nhom) {
            case "thang" -> "Theo tháng";
            case "danh-muc" -> "Theo nhóm hàng";
            default -> "Theo ngày";
        };
    }

    public void kiemTraKhoangNgay(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den)) {
            throw new LoiNghiepVu("Khoảng ngày không hợp lệ.");
        }
    }

    @Transactional(readOnly = true)
    public BaoCaoDoanhThu doanhThu(LocalDate tu, LocalDate den, String nhom) {
        kiemTraKhoangNgay(tu, den);
        if (!List.of("ngay", "thang", "danh-muc").contains(nhom)) {
            nhom = "ngay";
        }
        String ghiChu = null;
        if (tu.plusMonths(12).isBefore(den) && !nhom.equals("thang")) {
            nhom = "thang";
            ghiChu = "Khoảng ngày quá 12 tháng nên báo cáo chỉ nhóm theo tháng.";
        }
        List<DonHang> dsDon = donHangTruyVan.donHoanThanhTrongKhoang(TrangThaiDonHang.HOAN_THANH, tu.atStartOfDay(), den.plusDays(1).atStartOfDay());
        Map<Long, Map<LoaiGiaoDich, Long>> giaoDich = new HashMap<>();
        Map<Long, Long> thuDaXacNhan = new HashMap<>();
        List<BaoCaoDoanhThu.DonChuaXacNhan> chuaXacNhan = new ArrayList<>();
        if (!dsDon.isEmpty()) {
            List<Long> ma = dsDon.stream().map(DonHang::getMaDonHang).toList();
            for (Object[] r : donHangTruyVan.tongGiaoDichTheoDon(ma)) {
                giaoDich.computeIfAbsent((Long) r[0], k -> new EnumMap<>(LoaiGiaoDich.class)).put((LoaiGiaoDich) r[1], ((Number) r[2]).longValue());
            }
            for (Object[] r : donHangTruyVan.phieuDaGiaoTheoDon(ma)) {
                long tien = r[2] == null ? 0 : ((Number) r[2]).longValue();
                if (r[3] == null) {
                    if (tien > 0) {
                        chuaXacNhan.add(new BaoCaoDoanhThu.DonChuaXacNhan((Long) r[0], (Long) r[1], (LocalDate) r[4], tien));
                    }
                } else {
                    thuDaXacNhan.merge((Long) r[0], tien, Long::sum);
                }
            }
        }
        Map<String, long[]> nhomSo = new TreeMap<>();
        Map<String, String> nhan = new HashMap<>();
        for (DonHang d : dsDon) {
            Map<LoaiGiaoDich, Long> gd = giaoDich.getOrDefault(d.getMaDonHang(), Map.of());
            long hoan = gd.getOrDefault(LoaiGiaoDich.HOAN_TIEN, 0L);
            long tienHang = 0;
            for (ChiTietDonHang ct : d.getChiTiet()) {
                tienHang += ct.getDonGia() * ct.getSoLuong();
            }
            if (tienHang == 0) {
                tienHang = soLong(d.getTienHang());
            }
            long tienGiam = soLong(d.getTienGiam());
            long doanhThu = soLong(d.getTongTien()) - hoan;
            long daThu = Math.min(soLong(d.getTongTien()),
                    gd.getOrDefault(LoaiGiaoDich.COC, 0L) + gd.getOrDefault(LoaiGiaoDich.THANH_TOAN, 0L)
                            + thuDaXacNhan.getOrDefault(d.getMaDonHang(), 0L)) - hoan;
            if (nhom.equals("danh-muc")) {
                Map<String, Long> theoDm = new LinkedHashMap<>();
                for (ChiTietDonHang ct : d.getChiTiet()) {
                    DanhMuc dm = ct.getSanPham().getDanhMuc();
                    String khoa = String.format("%05d|%s", dm.getThuTu() == null ? 0 : dm.getThuTu(), dm.getMaDanhMuc());
                    nhan.put(khoa, dm.getTenDanhMuc());
                    theoDm.merge(khoa, ct.getDonGia() * ct.getSoLuong(), Long::sum);
                }
                if (theoDm.isEmpty()) {
                    nhan.put("99999|khac", "Khác");
                    theoDm.put("99999|khac", tienHang);
                }
                long conGiam = tienGiam;
                long conDoanhThu = doanhThu;
                long conDaThu = daThu;
                int i = 0;
                for (Map.Entry<String, Long> e : theoDm.entrySet()) {
                    i++;
                    long giam;
                    long dt;
                    long thu;
                    if (i == theoDm.size()) {
                        giam = conGiam;
                        dt = conDoanhThu;
                        thu = conDaThu;
                    } else {
                        double tiLe = tienHang == 0 ? 0 : (double) e.getValue() / tienHang;
                        giam = Math.round(tienGiam * tiLe);
                        dt = Math.round(doanhThu * tiLe);
                        thu = Math.round(daThu * tiLe);
                        conGiam -= giam;
                        conDoanhThu -= dt;
                        conDaThu -= thu;
                    }
                    cong(nhomSo, e.getKey(), 1, e.getValue(), giam, dt, thu);
                }
            } else {
                LocalDateTime ht = d.getNgayHoanThanh();
                String khoa;
                if (nhom.equals("thang")) {
                    YearMonth ym = YearMonth.from(ht);
                    khoa = ym.toString();
                    nhan.put(khoa, ym.format(THANG));
                } else {
                    khoa = ht.toLocalDate().toString();
                    nhan.put(khoa, ht.toLocalDate().format(NGAY));
                }
                cong(nhomSo, khoa, 1, tienHang, tienGiam, doanhThu, daThu);
            }
        }
        long lonNhat = nhomSo.values().stream().mapToLong(v -> v[3]).max().orElse(0);
        List<BaoCaoDoanhThu.Dong> dong = new ArrayList<>();
        for (Map.Entry<String, long[]> e : nhomSo.entrySet()) {
            long[] v = e.getValue();
            int pt = lonNhat <= 0 ? 0 : (int) Math.max(0, Math.round(v[3] * 100.0 / lonNhat));
            dong.add(new BaoCaoDoanhThu.Dong(nhan.get(e.getKey()), v[0], v[1], v[2], v[3], v[4], pt));
        }
        long tienChua = chuaXacNhan.stream().mapToLong(BaoCaoDoanhThu.DonChuaXacNhan::tienThuKhiGiao).sum();
        long tongTienHang = dong.stream().mapToLong(BaoCaoDoanhThu.Dong::tienHang).sum();
        long tongGiam = dong.stream().mapToLong(BaoCaoDoanhThu.Dong::tienGiam).sum();
        long tongDt = dong.stream().mapToLong(BaoCaoDoanhThu.Dong::doanhThu).sum();
        long tongThu = dong.stream().mapToLong(BaoCaoDoanhThu.Dong::daThu).sum();
        BaoCaoDoanhThu.Dong tong = new BaoCaoDoanhThu.Dong("Tổng cộng", dsDon.size(), tongTienHang, tongGiam, tongDt, tongThu, 100);
        return new BaoCaoDoanhThu(tu, den, nhom, tenNhom(nhom), dong, tong, chuaXacNhan, tienChua, ghiChu, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public long doanhThuThangNay() {
        LocalDate dauThang = LocalDate.now().withDayOfMonth(1);
        return doanhThu(dauThang, LocalDate.now(), "thang").tong().doanhThu();
    }

    public byte[] xuatCsv(BaoCaoDoanhThu bc) {
        StringBuilder sb = new StringBuilder("﻿");
        dongCsv(sb, "Báo cáo doanh thu");
        dongCsv(sb, "Từ ngày", bc.tuNgay().format(NGAY), "Đến ngày", bc.denNgay().format(NGAY));
        dongCsv(sb, "Nhóm", bc.tenNhom());
        dongCsv(sb, "Thời điểm xuất", LocalDateTime.now().format(NGAY_GIO));
        sb.append("\r\n");
        String cot = bc.nhom().equals("danh-muc") ? "Nhóm hàng" : bc.nhom().equals("thang") ? "Tháng" : "Ngày";
        dongCsv(sb, cot, "Số đơn", "Tiền hàng (đ)", "Tiền giảm giá (đ)", "Doanh thu (đ)", "Tiền đã thu (đ)");
        for (BaoCaoDoanhThu.Dong d : bc.dong()) {
            dongCsv(sb, d.nhan(), String.valueOf(d.soDon()), String.valueOf(d.tienHang()), String.valueOf(d.tienGiam()),
                    String.valueOf(d.doanhThu()), String.valueOf(d.daThu()));
        }
        BaoCaoDoanhThu.Dong t = bc.tong();
        dongCsv(sb, t.nhan(), String.valueOf(t.soDon()), String.valueOf(t.tienHang()), String.valueOf(t.tienGiam()),
                String.valueOf(t.doanhThu()), String.valueOf(t.daThu()));
        if (!bc.chuaXacNhan().isEmpty()) {
            sb.append("\r\n");
            dongCsv(sb, "Tiền thu khi giao chưa được kế toán xác nhận (đ)", String.valueOf(bc.tienChuaXacNhan()));
            dongCsv(sb, "Mã đơn", "Mã phiếu giao", "Ngày giao", "Tiền thu khi giao (đ)");
            for (BaoCaoDoanhThu.DonChuaXacNhan d : bc.chuaXacNhan()) {
                dongCsv(sb, String.format("DH%06d", d.maDonHang()), String.valueOf(d.maPhieuGiao()),
                        d.ngayGiao() == null ? "" : d.ngayGiao().format(NGAY), String.valueOf(d.tienThuKhiGiao()));
            }
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional(readOnly = true)
    public List<DongTonKho> tonKho() {
        Map<Long, Map<TrangThaiSerial, Long>> dem = new HashMap<>();
        for (Object[] r : khoTruyVan.demSerialTheoTrangThai()) {
            dem.computeIfAbsent((Long) r[0], k -> new EnumMap<>(TrangThaiSerial.class)).put((TrangThaiSerial) r[1], ((Number) r[2]).longValue());
        }
        List<DongTonKho> ds = new ArrayList<>();
        for (SanPham sp : sanPhamTruyVan.layTatCaKemDanhMuc()) {
            Map<TrangThaiSerial, Long> m = dem.getOrDefault(sp.getMaSanPham(), Map.of());
            long trongKho = m.getOrDefault(TrangThaiSerial.TRONG_KHO, 0L);
            long tongNhap = m.values().stream().mapToLong(Long::longValue).sum();
            boolean sapHet = sp.getTrangThai() != TrangThaiSanPham.NGUNG_BAN && trongKho <= MUC_TON_TOI_THIEU;
            ds.add(new DongTonKho(sp.getMaSanPham(), sp.getTenSanPham(), sp.getModel(), sp.getDanhMuc().getTenDanhMuc(),
                    sp.getTrangThai().getNhan(), tongNhap, trongKho,
                    m.getOrDefault(TrangThaiSerial.DA_XUAT, 0L), m.getOrDefault(TrangThaiSerial.DA_BAN, 0L),
                    m.getOrDefault(TrangThaiSerial.DANG_BAO_HANH, 0L), m.getOrDefault(TrangThaiSerial.TRA_VE_KHO, 0L),
                    m.getOrDefault(TrangThaiSerial.LOI_CHO_GUI_HANG, 0L), sapHet));
        }
        return ds;
    }

    @Transactional(readOnly = true)
    public List<DongTonKho> mauSapHet() {
        return tonKho().stream().filter(DongTonKho::sapHet).sorted(Comparator.comparingLong(DongTonKho::trongKho)).toList();
    }

    @Transactional(readOnly = true)
    public List<PhieuBaoHanh> phieuBaoHanhDangMo() {
        return khoTruyVan.phieuBaoHanhDangMo();
    }

    public List<DanhMuc> danhMuc() {
        return danhMucTruyVan.layTheoThuTu();
    }

    private static void cong(Map<String, long[]> m, String khoa, long soDon, long tienHang, long giam, long dt, long thu) {
        long[] v = m.computeIfAbsent(khoa, k -> new long[5]);
        v[0] += soDon;
        v[1] += tienHang;
        v[2] += giam;
        v[3] += dt;
        v[4] += thu;
    }

    private static long soLong(Long x) {
        return x == null ? 0 : x;
    }

    private static void dongCsv(StringBuilder sb, String... o) {
        for (int i = 0; i < o.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            String s = o[i] == null ? "" : o[i];
            if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
                s = "\"" + s.replace("\"", "\"\"") + "\"";
            }
            sb.append(s);
        }
        sb.append("\r\n");
    }

    public record DongTonKho(Long maSanPham, String tenSanPham, String model, String danhMuc, String trangThai, long tongNhap,
                             long trongKho, long daXuat, long daBan, long dangBaoHanh, long traVeKho, long loiChoGuiHang,
                             boolean sapHet) {
    }
}
