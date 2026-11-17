package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.DonHangTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.GiaoDichTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuGiaoTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuXuatTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.SerialTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.YeuCauDoiTraTruyVanG4;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.YeuCauDoiTra;

@Service
public class DonHangQuanTriBUS {

    public static final long NGUONG_XIN_COC = 10_000_000L;
    public static final long PHUT_GOI_TOI_DA = 30;
    public static final int SO_LAN_GOI_TOI_DA = 3;
    public static final List<String> LY_DO_HUY = List.of("Khách huỷ", "Không liên lạc được", "Hết hàng", "Khác");
    private static final String DAU_GOI_LAI = "Gọi lại lần ";
    private static final Pattern MA_DON = Pattern.compile("(?i)^(?:DH|#)?\\s*0*(\\d{1,9})$");

    public record DongDon(DonHang don, long phutCho, int soLanGoi, boolean treHan) {
    }

    public record DongSanPham(ChiTietDonHang ct, long trongKho, long dangGiu, long coTheBan, boolean du) {
    }

    public record ChiTietDon(DonHang don, List<DongSanPham> sanPham, List<LichSuTrangThaiDon> lichSu,
                             List<PhieuGiaoHang> phieuGiao, List<PhieuXuat> phieuXuat, List<GiaoDichThanhToan> giaoDich,
                             List<YeuCauDoiTra> yeuCau, int soLanGoi, long phutCho, boolean coTheHuy,
                             boolean canXinCoc, long cocTu, long cocDen, boolean thieuHang) {
    }

    private final DonHangTruyVanG4 donDAO;
    private final PhieuGiaoTruyVanG4 phieuGiaoDAO;
    private final PhieuXuatTruyVanG4 phieuXuatDAO;
    private final SerialTruyVanG4 serialDAO;
    private final GiaoDichTruyVanG4 giaoDichDAO;
    private final YeuCauDoiTraTruyVanG4 yeuCauDAO;
    private final DonHangBUS donHangBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public DonHangQuanTriBUS(DonHangTruyVanG4 donDAO, PhieuGiaoTruyVanG4 phieuGiaoDAO, PhieuXuatTruyVanG4 phieuXuatDAO,
                             SerialTruyVanG4 serialDAO, GiaoDichTruyVanG4 giaoDichDAO, YeuCauDoiTraTruyVanG4 yeuCauDAO,
                             DonHangBUS donHangBUS, TaiKhoanBUS taiKhoanBUS) {
        this.donDAO = donDAO;
        this.phieuGiaoDAO = phieuGiaoDAO;
        this.phieuXuatDAO = phieuXuatDAO;
        this.serialDAO = serialDAO;
        this.giaoDichDAO = giaoDichDAO;
        this.yeuCauDAO = yeuCauDAO;
        this.donHangBUS = donHangBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    public static int soLanGoi(DonHang don) {
        String g = don.getGhiChu();
        if (g == null) {
            return 0;
        }
        int dem = 0;
        int i = g.indexOf(DAU_GOI_LAI);
        while (i >= 0) {
            dem++;
            i = g.indexOf(DAU_GOI_LAI, i + 1);
        }
        return dem;
    }

    public static long phutCho(DonHang don) {
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN || don.getNgayDat() == null) {
            return 0;
        }
        return Math.max(0, Duration.between(don.getNgayDat(), LocalDateTime.now()).toMinutes());
    }

    @Transactional(readOnly = true)
    public List<DongDon> danhSach(TrangThaiDonHang trangThai, String tuKhoa) {
        String sdt = null;
        Long ma = null;
        String tk = G4Chung.rong(tuKhoa);
        if (tk != null) {
            String so = tk.replaceAll("[\\s.]", "");
            sdt = so.matches("\\d{4,}") ? "%" + so + "%" : "\u0000";
            Matcher m = MA_DON.matcher(so);
            if (m.matches() && (so.length() <= 8 || !so.startsWith("0"))) {
                ma = Long.parseLong(m.group(1));
            }
        }
        List<DongDon> kq = new ArrayList<>();
        for (DonHang d : donDAO.timDon(trangThai, sdt, ma)) {
            long phut = phutCho(d);
            kq.add(new DongDon(d, phut, soLanGoi(d), phut > PHUT_GOI_TOI_DA));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public Map<String, Long> demTheoTrangThai() {
        Map<String, Long> kq = new LinkedHashMap<>();
        for (TrangThaiDonHang t : TrangThaiDonHang.values()) {
            kq.put(t.name(), 0L);
        }
        for (Object[] r : donDAO.demTheoTrangThai()) {
            kq.put(((TrangThaiDonHang) r[0]).name(), (Long) r[1]);
        }
        return kq;
    }

    private DonHang layDon(Long ma) {
        return donDAO.layDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn " + G4Chung.maDon(ma)));
    }

    @Transactional(readOnly = true)
    public ChiTietDon chiTiet(Long ma) {
        DonHang don = layDon(ma);
        Hibernate.initialize(don.getKhachHang());
        List<DongSanPham> sp = new ArrayList<>();
        boolean thieu = false;
        for (ChiTietDonHang ct : don.getChiTiet()) {
            long trongKho = serialDAO.demTheoTrangThai(ct.getSanPham().getMaSanPham(), TrangThaiSerial.TRONG_KHO);
            long dangGiu = donDAO.soLuongDangGiu(ct.getSanPham().getMaSanPham(), don.getMaDonHang());
            long coThe = Math.max(0, trongKho - dangGiu);
            boolean du = coThe >= ct.getSoLuong();
            thieu |= !du;
            sp.add(new DongSanPham(ct, trongKho, dangGiu, coThe, du));
        }
        List<LichSuTrangThaiDon> lichSu = donHangBUS.lichSu(don);
        lichSu.forEach(l -> Hibernate.initialize(l.getNhanVien()));
        List<PhieuGiaoHang> phieuGiao = phieuGiaoDAO.theoDon(don);
        List<PhieuXuat> phieuXuat = phieuXuatDAO.theoDon(don);
        List<GiaoDichThanhToan> giaoDich = giaoDichDAO.theoDon(don);
        List<YeuCauDoiTra> yeuCau = yeuCauDAO.theoDon(don);
        boolean coTheHuy = don.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN
                || (don.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN && phieuXuat.isEmpty());
        boolean xinCoc = don.getTongTien() != null && don.getTongTien() > NGUONG_XIN_COC
                && (don.getTienCoc() == null || don.getTienCoc() == 0);
        long tong = don.getTongTien() == null ? 0 : don.getTongTien();
        return new ChiTietDon(don, sp, lichSu, phieuGiao, phieuXuat, giaoDich, yeuCau, soLanGoi(don), phutCho(don),
                coTheHuy, xinCoc, tong / 10, tong / 5, thieu);
    }

    private void kiemTraDangXuLy(DonHang don, String trangThaiLucMo) {
        G4Chung.kiemTraTrangThaiLucMo(trangThaiLucMo, don.getTrangThai(), "Đơn " + G4Chung.maDon(don.getMaDonHang()),
                don.getTrangThai().getNhan());
    }

    @Transactional
    public void luuGhiChu(Long ma, String ghiChu, String diaChi, boolean noiThanh) {
        DonHang don = layDon(ma);
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN && don.getTrangThai() != TrangThaiDonHang.DA_XAC_NHAN) {
            throw new LoiNghiepVu("Đơn đang \"" + don.getTrangThai().getNhan() + "\", không sửa ghi chú, địa chỉ ở đây được");
        }
        don.setDiaChiGiao(G4Chung.gioiHan(G4Chung.batBuoc(diaChi, "Địa chỉ giao không được để trống"), 255, "Địa chỉ giao"));
        don.setGhiChu(G4Chung.gioiHan(G4Chung.rong(ghiChu), 500, "Ghi chú"));
        don.setNoiThanh(noiThanh);
        donDAO.save(don);
    }

    @Transactional
    public int goiLaiSau(Long ma, String trangThaiLucMo) {
        DonHang don = layDon(ma);
        kiemTraDangXuLy(don, trangThaiLucMo);
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN) {
            throw new LoiNghiepVu("Chỉ ghi gọi lại cho đơn Chờ xác nhận");
        }
        int lan = soLanGoi(don) + 1;
        G4Chung.noiGhiChu(don, DAU_GOI_LAI + lan + ": khách chưa nghe máy");
        donDAO.save(don);
        return lan;
    }

    @Transactional
    public void xacNhan(Long ma, String trangThaiLucMo) {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        DonHang don = layDon(ma);
        kiemTraDangXuLy(don, trangThaiLucMo);
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN) {
            throw new LoiNghiepVu("Đơn " + G4Chung.maDon(ma) + " đã ở trạng thái \"" + don.getTrangThai().getNhan() + "\"");
        }
        List<String> thieu = new ArrayList<>();
        for (ChiTietDonHang ct : don.getChiTiet()) {
            long trongKho = serialDAO.demTheoTrangThai(ct.getSanPham().getMaSanPham(), TrangThaiSerial.TRONG_KHO);
            long coThe = Math.max(0, trongKho - donDAO.soLuongDangGiu(ct.getSanPham().getMaSanPham(), ma));
            if (coThe < ct.getSoLuong()) {
                thieu.add(ct.getTenSanPhamLucBan() + " (cần " + ct.getSoLuong() + ", còn " + coThe + ")");
            }
        }
        if (!thieu.isEmpty()) {
            throw new LoiNghiepVu("Không đủ hàng: " + String.join("; ", thieu)
                    + ". Khách đồng ý chờ thì ghi ngày dự kiến có hàng vào ghi chú; không chờ thì huỷ với lý do Hết hàng.");
        }
        don.setNhanVienDuyet(nv);
        donHangBUS.doiTrangThai(don, TrangThaiDonHang.DA_XAC_NHAN, nv, "Đã gọi khách xác nhận");
    }

    @Transactional
    public long huy(Long ma, String trangThaiLucMo, String loaiLyDo, String chiTietLyDo) {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        DonHang don = layDon(ma);
        kiemTraDangXuLy(don, trangThaiLucMo);
        String loai = G4Chung.batBuoc(loaiLyDo, "Chọn lý do huỷ đơn");
        if (!LY_DO_HUY.contains(loai)) {
            throw new LoiNghiepVu("Lý do huỷ không hợp lệ");
        }
        String ct = G4Chung.rong(chiTietLyDo);
        if ("Khác".equals(loai) && ct == null) {
            throw new LoiNghiepVu("Lý do \"Khác\" phải ghi rõ chi tiết");
        }
        if (don.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN && phieuXuatDAO.demTheoDon(ma) > 0) {
            throw new LoiNghiepVu("Đơn đã có phiếu xuất kho, không huỷ ở đây được");
        }
        if (don.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN && don.getTrangThai() != TrangThaiDonHang.DA_XAC_NHAN) {
            throw new LoiNghiepVu("Đơn " + G4Chung.maDon(ma) + " đang \"" + don.getTrangThai().getNhan() + "\", không huỷ được");
        }
        String lyDo = G4Chung.gioiHan(ct == null ? loai : loai + ": " + ct, 255, "Lý do huỷ");
        don.setLyDoHuy(lyDo);
        long hoan = 0;
        String cach = "Hoàn theo cách khách đã trả";
        for (GiaoDichThanhToan gd : giaoDichDAO.theoDon(don)) {
            if (gd.getTrangThai() != TrangThaiGiaoDich.THANH_CONG) {
                continue;
            }
            if (gd.getLoai() == LoaiGiaoDich.HOAN_TIEN) {
                hoan -= gd.getSoTien();
            } else {
                hoan += gd.getSoTien();
                cach = gd.getPhuongThuc() == null ? cach : gd.getPhuongThuc();
            }
        }
        if (hoan > 0) {
            GiaoDichThanhToan g = new GiaoDichThanhToan();
            g.setDonHang(don);
            g.setLoai(LoaiGiaoDich.HOAN_TIEN);
            g.setSoTien(hoan);
            g.setPhuongThuc(cach.length() > 30 ? cach.substring(0, 30) : cach);
            g.setTrangThai(TrangThaiGiaoDich.THANH_CONG);
            g.setThoiDiem(LocalDateTime.now());
            giaoDichDAO.save(g);
            don.setTrangThaiThanhToan(TrangThaiThanhToan.DA_HOAN_TIEN);
        }
        for (PhieuGiaoHang p : phieuGiaoDAO.theoDon(don)) {
            if (p.getTrangThai() == TrangThaiPhieuGiao.DA_PHAN_CONG) {
                p.setTrangThai(TrangThaiPhieuGiao.KHONG_THANH_CONG);
                String lyDoPhieu = "Đơn đã huỷ: " + lyDo;
                p.setLyDoKhongThanhCong(lyDoPhieu.length() > 255 ? lyDoPhieu.substring(0, 255) : lyDoPhieu);
                phieuGiaoDAO.save(p);
            }
        }
        KhuyenMai km = don.getKhuyenMai();
        if (km != null && km.getLoai() == LoaiKhuyenMai.MA_GIAM_GIA && km.getSoLuotDaDung() != null && km.getSoLuotDaDung() > 0) {
            km.setSoLuotDaDung(km.getSoLuotDaDung() - 1);
        }
        donHangBUS.doiTrangThai(don, TrangThaiDonHang.DA_HUY, nv, "Huỷ đơn: " + lyDo);
        return Math.max(0, hoan);
    }
}
