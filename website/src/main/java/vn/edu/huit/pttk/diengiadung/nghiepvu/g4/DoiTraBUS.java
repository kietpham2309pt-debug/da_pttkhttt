package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.DonHangTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.GiaoDichTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuBaoHanhTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuLapDatTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuXuatTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.SerialTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.YeuCauDoiTraTruyVanG4;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.SerialBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.PhieuXuatBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.HinhThucDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiLyDoDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuBaoHanh;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;
import vn.edu.huit.pttk.diengiadung.thucthe.PhuongThucThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDoiTra;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.YeuCauDoiTra;

@Service
public class DoiTraBUS {

    public static final int HAN_DOI_Y = 7;
    public static final int HAN_LOI_NHA_SAN_XUAT = 30;
    public static final int PHAN_TRAM_KHAU_TRU_DOI_Y = 10;
    public static final String TIEN_MAT = "Tiền mặt";
    public static final String CHUYEN_KHOAN = "Chuyển khoản";
    public static final List<String> CACH_HOAN = List.of(TIEN_MAT, CHUYEN_KHOAN);
    private static final String DA_LAP = "Đã lắp";

    public record MayCuaDon(SanPhamSerial serial, long soNgay, boolean daLap, long gia) {
    }

    public record DonDoiTra(DonHang don, List<MayCuaDon> may) {
    }

    public record KetQuaTao(Long maYeuCau, Long maTrung, List<String> khongDat) {
    }

    public record ChiTietYeuCau(YeuCauDoiTra yeuCau, DonHang don, List<MayCuaDon> may, List<String> khongDat,
                                List<PhieuBaoHanh> baoHanh, long tongGia, long phiGoiY, Set<String> cachDaTra,
                                List<PhieuXuat> phieuXuatMayDoi) {
    }

    private final YeuCauDoiTraTruyVanG4 yeuCauDAO;
    private final DonHangTruyVanG4 donDAO;
    private final PhieuXuatTruyVanG4 phieuXuatDAO;
    private final SerialTruyVanG4 serialDAO;
    private final PhieuLapDatTruyVanG4 bienBanDAO;
    private final PhieuBaoHanhTruyVanG4 baoHanhDAO;
    private final GiaoDichTruyVanG4 giaoDichDAO;
    private final DonHangBUS donHangBUS;
    private final SerialBUS serialBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public DoiTraBUS(YeuCauDoiTraTruyVanG4 yeuCauDAO, DonHangTruyVanG4 donDAO, PhieuXuatTruyVanG4 phieuXuatDAO,
                     SerialTruyVanG4 serialDAO, PhieuLapDatTruyVanG4 bienBanDAO, PhieuBaoHanhTruyVanG4 baoHanhDAO,
                     GiaoDichTruyVanG4 giaoDichDAO, DonHangBUS donHangBUS, SerialBUS serialBUS, TaiKhoanBUS taiKhoanBUS) {
        this.yeuCauDAO = yeuCauDAO;
        this.donDAO = donDAO;
        this.phieuXuatDAO = phieuXuatDAO;
        this.serialDAO = serialDAO;
        this.bienBanDAO = bienBanDAO;
        this.baoHanhDAO = baoHanhDAO;
        this.giaoDichDAO = giaoDichDAO;
        this.donHangBUS = donHangBUS;
        this.serialBUS = serialBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @Transactional(readOnly = true)
    public List<YeuCauDoiTra> danhSach(TrangThaiDoiTra trangThai) {
        return yeuCauDAO.theoTrangThai(trangThai);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> demTheoTrangThai() {
        Map<String, Long> kq = new LinkedHashMap<>();
        for (TrangThaiDoiTra t : TrangThaiDoiTra.values()) {
            kq.put(t.name(), 0L);
        }
        for (Object[] r : yeuCauDAO.demTheoTrangThai()) {
            kq.put(((TrangThaiDoiTra) r[0]).name(), (Long) r[1]);
        }
        return kq;
    }

    private DonHang layDon(Long ma) {
        DonHang d = donDAO.layDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn " + G4Chung.maDon(ma)));
        d.getChiTiet().forEach(ct -> Hibernate.initialize(ct.getSanPham()));
        return d;
    }

    private List<MayCuaDon> mayCuaDon(DonHang don, LocalDate denNgay) {
        boolean coBienBan = bienBanDAO.demTheoDon(don) > 0;
        Map<String, MayCuaDon> kq = new LinkedHashMap<>();
        for (PhieuXuat x : phieuXuatDAO.theoDon(don)) {
            for (SanPhamSerial s : x.getSerial()) {
                if (kq.containsKey(s.getSoSerial())) {
                    continue;
                }
                long soNgay = s.getNgayBan() == null ? 0 : ChronoUnit.DAYS.between(s.getNgayBan().toLocalDate(), denNgay);
                boolean daLap = coBienBan && Boolean.TRUE.equals(s.getSanPham().getCanLapDat());
                long gia = s.getSanPham().getGiaBan() == null ? 0 : s.getSanPham().getGiaBan();
                for (ChiTietDonHang ct : don.getChiTiet()) {
                    if (ct.getSanPham().getMaSanPham().equals(s.getSanPham().getMaSanPham())) {
                        gia = ct.getDonGia();
                    }
                }
                kq.put(s.getSoSerial(), new MayCuaDon(s, soNgay, daLap, gia));
            }
        }
        return new ArrayList<>(kq.values());
    }

    @Transactional(readOnly = true)
    public List<DonDoiTra> timDon(String tuKhoa) {
        String tk = G4Chung.rong(tuKhoa);
        List<DonDoiTra> kq = new ArrayList<>();
        if (tk == null) {
            return kq;
        }
        Set<Long> maDon = new LinkedHashSet<>();
        serialDAO.layDayDu(tk.toUpperCase()).ifPresent(s -> {
            if (s.getDonHangBan() != null) {
                maDon.add(s.getDonHangBan().getMaDonHang());
            }
        });
        String so = tk.replaceAll("[\\s.]", "");
        String sdt = so.matches("\\d{4,}") ? "%" + so + "%" : "\u0000";
        Long ma = null;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(?i)^(?:DH|#)?0*(\\d{1,9})$").matcher(so);
        if (m.matches() && (so.length() <= 8 || !so.startsWith("0"))) {
            ma = Long.parseLong(m.group(1));
        }
        donDAO.timDon(null, sdt, ma).forEach(d -> maDon.add(d.getMaDonHang()));
        for (Long m2 : maDon) {
            DonHang d = layDon(m2);
            kq.add(new DonDoiTra(d, mayCuaDon(d, LocalDate.now())));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public DonDoiTra donDeTao(Long maDon) {
        DonHang d = layDon(maDon);
        if (d.getTrangThai() != TrangThaiDonHang.HOAN_THANH) {
            throw new LoiNghiepVu("Chỉ tạo yêu cầu đổi trả cho đơn Hoàn thành; đơn " + G4Chung.maDon(maDon) + " đang \""
                    + d.getTrangThai().getNhan() + "\"");
        }
        return new DonDoiTra(d, mayCuaDon(d, LocalDate.now()));
    }

    public static List<String> doiChieu(LoaiLyDoDoiTra loai, long soNgay, boolean conSeal, boolean daLap) {
        List<String> kq = new ArrayList<>();
        if (loai == LoaiLyDoDoiTra.KHONG_DUNG_MO_TA) {
            return kq;
        }
        if (loai == LoaiLyDoDoiTra.LOI_NHA_SAN_XUAT) {
            if (soNgay > HAN_LOI_NHA_SAN_XUAT) {
                kq.add("Lỗi nhà sản xuất quá " + HAN_LOI_NHA_SAN_XUAT + " ngày (đã " + soNgay
                        + " ngày): nên chuyển sang Tiếp nhận bảo hành");
            }
            return kq;
        }
        if (soNgay > HAN_DOI_Y) {
            kq.add("Quá " + HAN_DOI_Y + " ngày kể từ ngày bán (đã " + soNgay + " ngày)");
        }
        if (!conSeal) {
            kq.add("Máy không còn seal");
        }
        if (daLap) {
            kq.add("Máy đã lắp, chỉ đổi trả khi lỗi nhà sản xuất");
        }
        return kq;
    }

    @Transactional
    public KetQuaTao tao(Long maDon, List<String> soSerial, HinhThucDoiTra hinhThuc, LoaiLyDoDoiTra loaiLyDo, String lyDo,
                         String benNgoai, boolean conSeal, boolean daLapKhaiBao, boolean xinNgoaiLe) {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        DonDoiTra dd = donDeTao(maDon);
        if (soSerial == null || soSerial.isEmpty()) {
            throw new LoiNghiepVu("Chọn chiếc máy khách mang lại");
        }
        if (hinhThuc == null || loaiLyDo == null) {
            throw new LoiNghiepVu("Chọn hình thức và loại lý do");
        }
        String ld = G4Chung.gioiHan(G4Chung.batBuoc(lyDo, "Ghi lý do khách đổi trả"), 500, "Lý do");
        List<MayCuaDon> chon = new ArrayList<>();
        for (String so : soSerial) {
            MayCuaDon m = dd.may().stream().filter(x -> x.serial().getSoSerial().equalsIgnoreCase(so.trim())).findFirst()
                    .orElseThrow(() -> new LoiNghiepVu("Serial " + so + " không khớp đơn " + G4Chung.maDon(maDon)));
            if (m.serial().getTrangThai() != TrangThaiSerial.DA_BAN || m.serial().getDonHangBan() == null
                    || !m.serial().getDonHangBan().getMaDonHang().equals(maDon)) {
                throw new LoiNghiepVu("Serial " + so + " đang \"" + m.serial().getTrangThai().getNhan() + "\", không phải máy Đã bán của đơn");
            }
            List<YeuCauDoiTra> trung = yeuCauDAO.choDuyetCuaSerial(m.serial().getSoSerial());
            if (!trung.isEmpty()) {
                return new KetQuaTao(null, trung.get(0).getMaYeuCau(), List.of());
            }
            chon.add(m);
        }
        List<String> khongDat = new ArrayList<>();
        for (MayCuaDon m : chon) {
            boolean daLap = daLapKhaiBao || m.daLap();
            for (String k : doiChieu(loaiLyDo, m.soNgay(), conSeal, daLap)) {
                khongDat.add(m.serial().getSoSerial() + ": " + k);
            }
        }
        if (!khongDat.isEmpty() && !xinNgoaiLe) {
            return new KetQuaTao(null, null, khongDat);
        }
        boolean daLapBatKy = daLapKhaiBao || chon.stream().anyMatch(MayCuaDon::daLap);
        String bn = G4Chung.rong(benNgoai);
        String tinhTrang = (conSeal ? "Còn seal" : "Mất seal") + "; " + (daLapBatKy ? DA_LAP : "Chưa lắp") + (bn == null ? "" : "; " + bn);
        long tongGia = chon.stream().mapToLong(MayCuaDon::gia).sum();
        YeuCauDoiTra y = new YeuCauDoiTra();
        y.setDonHang(dd.don());
        y.setNgayYeuCau(LocalDateTime.now());
        y.setLoaiLyDo(loaiLyDo);
        y.setLyDo(ld);
        y.setHinhThuc(hinhThuc);
        y.setTinhTrangMay(G4Chung.gioiHan(tinhTrang, 255, "Tình trạng máy"));
        y.setConSeal(conSeal);
        y.setPhiKhauTru(phiGoiY(loaiLyDo, hinhThuc, tongGia));
        y.setSoTienHoan(0L);
        y.setLaNgoaiLe(!khongDat.isEmpty());
        y.setTrangThai(TrangThaiDoiTra.CHO_DUYET);
        y.setNhanVienTiepNhan(nv);
        chon.forEach(m -> y.getSerial().add(m.serial()));
        yeuCauDAO.save(y);
        return new KetQuaTao(y.getMaYeuCau(), null, khongDat);
    }

    public static long phiGoiY(LoaiLyDoDoiTra loai, HinhThucDoiTra hinhThuc, long tongGia) {
        if (loai == LoaiLyDoDoiTra.DOI_Y && hinhThuc == HinhThucDoiTra.TRA_HANG) {
            return tongGia * PHAN_TRAM_KHAU_TRU_DOI_Y / 100;
        }
        return 0;
    }

    private YeuCauDoiTra layYeuCau(Long ma) {
        return yeuCauDAO.layDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy yêu cầu đổi trả #" + ma));
    }

    private Set<String> cachDaTra(DonHang don) {
        Set<String> kq = new LinkedHashSet<>();
        for (GiaoDichThanhToan g : giaoDichDAO.theoDon(don)) {
            if (g.getTrangThai() == TrangThaiGiaoDich.THANH_CONG && g.getLoai() != LoaiGiaoDich.HOAN_TIEN) {
                kq.add(g.getPhuongThuc() != null && g.getPhuongThuc().toLowerCase().contains("tiền mặt") ? TIEN_MAT : CHUYEN_KHOAN);
            }
        }
        if (don.getPhuongThucThanhToan() == PhuongThucThanhToan.TIEN_MAT_KHI_NHAN) {
            kq.add(TIEN_MAT);
        } else if (kq.isEmpty()) {
            kq.add(CHUYEN_KHOAN);
        }
        return kq;
    }

    private List<MayCuaDon> mayCuaYeuCau(YeuCauDoiTra y, DonHang don) {
        LocalDate moc = y.getNgayYeuCau().toLocalDate();
        Set<String> so = new HashSet<>();
        y.getSerial().forEach(s -> so.add(s.getSoSerial()));
        return mayCuaDon(don, moc).stream().filter(m -> so.contains(m.serial().getSoSerial())).toList();
    }

    private List<PhieuXuat> phieuXuatMayDoi(YeuCauDoiTra y, DonHang don) {
        if (y.getNgayDuyet() == null) {
            return List.of();
        }
        String lyDo = PhieuXuatBUS.lyDoDoiMay(y.getMaYeuCau());
        return phieuXuatDAO.theoDon(don).stream().filter(x -> lyDo.equals(x.getLyDoXuat())).toList();
    }

    @Transactional(readOnly = true)
    public ChiTietYeuCau chiTiet(Long ma) {
        YeuCauDoiTra y = layYeuCau(ma);
        DonHang don = layDon(y.getDonHang().getMaDonHang());
        List<MayCuaDon> may = mayCuaYeuCau(y, don);
        boolean daLap = y.getTinhTrangMay() != null && y.getTinhTrangMay().contains(DA_LAP);
        List<String> khongDat = new ArrayList<>();
        for (MayCuaDon m : may) {
            for (String k : doiChieu(y.getLoaiLyDo(), m.soNgay(), Boolean.TRUE.equals(y.getConSeal()), daLap || m.daLap())) {
                khongDat.add(m.serial().getSoSerial() + ": " + k);
            }
        }
        List<String> so = may.stream().map(m -> m.serial().getSoSerial()).toList();
        List<PhieuBaoHanh> bh = so.isEmpty() ? List.of() : baoHanhDAO.theoSerial(so);
        long tongGia = may.stream().mapToLong(MayCuaDon::gia).sum();
        return new ChiTietYeuCau(y, don, may, khongDat, bh, tongGia, phiGoiY(y.getLoaiLyDo(), y.getHinhThuc(), tongGia),
                cachDaTra(don), phieuXuatMayDoi(y, don));
    }

    private void kiemTraLucMo(YeuCauDoiTra y, String lucMo) {
        if (lucMo != null && !lucMo.isBlank() && !lucMo.equals(y.getTrangThai().name())) {
            String ai = y.getNguoiDuyet() != null ? y.getNguoiDuyet().getHoTen() : "người khác";
            String luc = y.getNgayDuyet() != null ? y.getNgayDuyet().format(G4Chung.GIO_NGAY) : "";
            throw new LoiNghiepVu("Yêu cầu đã được " + ai + " xử lý lúc " + luc + " (đang \"" + y.getTrangThai().getNhan() + "\"), không ghi đè");
        }
    }

    @Transactional
    public void duyet(Long ma, String lucMo, Long phiKhauTru, HinhThucDoiTra hinhThuc, String yKienChu) {
        NhanVien ql = taiKhoanBUS.batBuocNhanVien();
        YeuCauDoiTra y = layYeuCau(ma);
        kiemTraLucMo(y, lucMo);
        if (y.getTrangThai() != TrangThaiDoiTra.CHO_DUYET) {
            throw new LoiNghiepVu("Yêu cầu đang \"" + y.getTrangThai().getNhan() + "\", không duyệt được");
        }
        String yk = G4Chung.gioiHan(G4Chung.rong(yKienChu), 255, "Ý kiến chủ cửa hàng");
        if (Boolean.TRUE.equals(y.getLaNgoaiLe()) && yk == null) {
            throw new LoiNghiepVu("Yêu cầu ngoại lệ: ghi ý kiến của chủ cửa hàng trước khi duyệt");
        }
        DonHang don = layDon(y.getDonHang().getMaDonHang());
        List<MayCuaDon> may = mayCuaYeuCau(y, don);
        long tongGia = may.stream().mapToLong(MayCuaDon::gia).sum();
        long phi = phiKhauTru == null ? 0 : phiKhauTru;
        if (phi < 0 || phi > tongGia) {
            throw new LoiNghiepVu("Phí khấu trừ phải từ 0 đến " + G4Chung.tien(tongGia));
        }
        if (hinhThuc != null) {
            y.setHinhThuc(hinhThuc);
        }
        y.setPhiKhauTru(phi);
        y.setSoTienHoan(y.getHinhThuc() == HinhThucDoiTra.TRA_HANG ? tongGia - phi : 0);
        y.setGhiChuYKienChu(yk);
        y.setTrangThai(TrangThaiDoiTra.DA_DUYET);
        y.setNguoiDuyet(ql);
        y.setNgayDuyet(LocalDateTime.now());
        for (SanPhamSerial s : y.getSerial()) {
            serialBUS.doiTrangThai(s, TrangThaiSerial.TRA_VE_KHO);
        }
        yeuCauDAO.save(y);
        if (y.getHinhThuc() == HinhThucDoiTra.DOI_MAY) {
            StringBuilder ds = new StringBuilder();
            for (MayCuaDon m : may) {
                ds.append(ds.length() == 0 ? "" : ", ").append(m.serial().getSanPham().getModel()).append(" thay serial ").append(m.serial().getSoSerial());
            }
            G4Chung.noiGhiChu(don, "Đổi máy (yêu cầu #" + y.getMaYeuCau() + " đã duyệt): thủ kho xuất máy cùng mẫu " + ds);
            donDAO.save(don);
        }
    }

    @Transactional
    public void tuChoi(Long ma, String lucMo, String lyDo, String yKienChu) {
        NhanVien ql = taiKhoanBUS.batBuocNhanVien();
        YeuCauDoiTra y = layYeuCau(ma);
        kiemTraLucMo(y, lucMo);
        if (y.getTrangThai() != TrangThaiDoiTra.CHO_DUYET) {
            throw new LoiNghiepVu("Yêu cầu đang \"" + y.getTrangThai().getNhan() + "\", không từ chối được");
        }
        if (y.getLoaiLyDo() == LoaiLyDoDoiTra.KHONG_DUNG_MO_TA) {
            throw new LoiNghiepVu("Hàng không đúng mô tả bắt buộc nhận lại (TL-20 Đ16): chỉ chọn đổi máy hoặc trả hàng, không từ chối");
        }
        String ld = G4Chung.gioiHan(G4Chung.batBuoc(lyDo, "Nhập lý do từ chối"), 255, "Lý do từ chối");
        String yk = G4Chung.gioiHan(G4Chung.rong(yKienChu), 255, "Ý kiến chủ cửa hàng");
        if (Boolean.TRUE.equals(y.getLaNgoaiLe()) && yk == null) {
            throw new LoiNghiepVu("Yêu cầu ngoại lệ: ghi ý kiến của chủ cửa hàng trước khi từ chối");
        }
        y.setLyDoTuChoi(ld);
        y.setGhiChuYKienChu(yk);
        y.setTrangThai(TrangThaiDoiTra.TU_CHOI);
        y.setNguoiDuyet(ql);
        y.setNgayDuyet(LocalDateTime.now());
        yeuCauDAO.save(y);
    }

    @Transactional
    public String hoanTien(Long ma, String lucMo, String cachHoan, String maGiaoDich) {
        NhanVien kt = taiKhoanBUS.batBuocNhanVien();
        YeuCauDoiTra y = layYeuCau(ma);
        kiemTraLucMo(y, lucMo);
        if (y.getTrangThai() != TrangThaiDoiTra.DA_DUYET || y.getHinhThuc() != HinhThucDoiTra.TRA_HANG) {
            throw new LoiNghiepVu("Chỉ ghi hoàn tiền cho yêu cầu Trả hàng đã duyệt");
        }
        DonHang don = layDon(y.getDonHang().getMaDonHang());
        String cach = G4Chung.batBuoc(cachHoan, "Chọn cách hoàn tiền");
        if (!cachDaTra(don).contains(cach)) {
            throw new LoiNghiepVu("Hoàn tiền theo cách khách đã trả (" + String.join(", ", cachDaTra(don)) + ")");
        }
        String maGd = G4Chung.gioiHan(G4Chung.rong(maGiaoDich), 40, "Mã giao dịch");
        if (y.getSoTienHoan() > 0) {
            GiaoDichThanhToan g = new GiaoDichThanhToan();
            g.setDonHang(don);
            g.setLoai(LoaiGiaoDich.HOAN_TIEN);
            g.setSoTien(y.getSoTienHoan());
            g.setPhuongThuc(cach);
            g.setMaGiaoDichCong(maGd);
            g.setTrangThai(TrangThaiGiaoDich.THANH_CONG);
            g.setThoiDiem(LocalDateTime.now());
            giaoDichDAO.save(g);
        }
        y.setTrangThai(TrangThaiDoiTra.HOAN_TAT);
        yeuCauDAO.save(y);
        if (traToanBo(don)) {
            don.setTrangThaiThanhToan(TrangThaiThanhToan.DA_HOAN_TIEN);
            donHangBUS.doiTrangThai(don, TrangThaiDonHang.DA_TRA_HANG, kt, "Khách trả toàn bộ đơn, đã hoàn tiền (yêu cầu #" + ma + ")");
            return "Đã ghi hoàn " + G4Chung.tien(y.getSoTienHoan()) + ". Khách trả toàn bộ đơn: đơn chuyển sang Đã trả hàng, thanh toán Đã hoàn tiền.";
        }
        return "Đã ghi hoàn " + G4Chung.tien(y.getSoTienHoan()) + " cho yêu cầu #" + ma + ". Khách trả một phần: đơn giữ Hoàn thành.";
    }

    private boolean traToanBo(DonHang don) {
        Set<String> daDoi = new HashSet<>();
        Set<String> daTra = new HashSet<>();
        for (YeuCauDoiTra y : yeuCauDAO.theoDon(don)) {
            if (y.getHinhThuc() == HinhThucDoiTra.DOI_MAY && (y.getTrangThai() == TrangThaiDoiTra.DA_DUYET || y.getTrangThai() == TrangThaiDoiTra.HOAN_TAT)) {
                y.getSerial().forEach(s -> daDoi.add(s.getSoSerial()));
            }
            if (y.getHinhThuc() == HinhThucDoiTra.TRA_HANG && y.getTrangThai() == TrangThaiDoiTra.HOAN_TAT) {
                y.getSerial().forEach(s -> daTra.add(s.getSoSerial()));
            }
        }
        boolean coMay = false;
        for (MayCuaDon m : mayCuaDon(don, LocalDate.now())) {
            String so = m.serial().getSoSerial();
            if (daDoi.contains(so)) {
                continue;
            }
            coMay = true;
            if (!daTra.contains(so)) {
                return false;
            }
        }
        return coMay && don.getTrangThai() == TrangThaiDonHang.HOAN_THANH;
    }

    @Transactional
    public String daGiaoMayDoi(Long ma, String lucMo) {
        YeuCauDoiTra y = layYeuCau(ma);
        kiemTraLucMo(y, lucMo);
        if (y.getTrangThai() != TrangThaiDoiTra.DA_DUYET || y.getHinhThuc() != HinhThucDoiTra.DOI_MAY) {
            throw new LoiNghiepVu("Chỉ ghi giao máy đổi cho yêu cầu Đổi máy đã duyệt");
        }
        DonHang don = layDon(y.getDonHang().getMaDonHang());
        Set<Long> mau = new HashSet<>();
        y.getSerial().forEach(s -> mau.add(s.getSanPham().getMaSanPham()));
        List<SanPhamSerial> mayMoi = new ArrayList<>();
        for (PhieuXuat x : phieuXuatMayDoi(y, don)) {
            for (SanPhamSerial s : x.getSerial()) {
                if (s.getTrangThai() == TrangThaiSerial.DA_XUAT && mau.contains(s.getSanPham().getMaSanPham())) {
                    mayMoi.add(s);
                }
            }
        }
        if (mayMoi.isEmpty()) {
            throw new LoiNghiepVu("Thủ kho chưa lập phiếu xuất máy đổi cùng mẫu cho đơn " + G4Chung.maDon(don.getMaDonHang()));
        }
        LocalDateTime bayGio = LocalDateTime.now();
        StringBuilder ds = new StringBuilder();
        for (SanPhamSerial s : mayMoi) {
            s.setNgayBan(bayGio);
            s.setDonHangBan(don);
            serialBUS.doiTrangThai(s, TrangThaiSerial.DA_BAN);
            ds.append(ds.length() == 0 ? "" : ", ").append(s.getSoSerial());
        }
        y.setTrangThai(TrangThaiDoiTra.HOAN_TAT);
        yeuCauDAO.save(y);
        return "Đã giao máy đổi " + ds + " cho khách; máy mới chuyển sang Đã bán, đơn giữ Hoàn thành.";
    }
}
