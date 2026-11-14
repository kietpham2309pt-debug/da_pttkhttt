package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DiaChiGiaoHangDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhachHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhuyenMaiTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.DonHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.SanPhamTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.GioHang;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DiaChiGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiThanhToan;

@Service
public class DatHangBUS {

    public static final int SO_LUONG_TOI_DA_MOT_DONG = 20;

    private final SanPhamTruyVanKhach sanPhamTV;
    private final KhuyenMaiTruyVanKhach khuyenMaiTV;
    private final KhachHangTruyVanKhach khachTV;
    private final DonHangTruyVanKhach donHangTV;
    private final DiaChiGiaoHangDAO diaChiDAO;
    private final DonHangBUS donHangBUS;
    private final GiaBUS giaBUS;

    public DatHangBUS(SanPhamTruyVanKhach sanPhamTV, KhuyenMaiTruyVanKhach khuyenMaiTV, KhachHangTruyVanKhach khachTV,
                      DonHangTruyVanKhach donHangTV, DiaChiGiaoHangDAO diaChiDAO, DonHangBUS donHangBUS, GiaBUS giaBUS) {
        this.sanPhamTV = sanPhamTV;
        this.khuyenMaiTV = khuyenMaiTV;
        this.khachTV = khachTV;
        this.donHangTV = donHangTV;
        this.diaChiDAO = diaChiDAO;
        this.donHangBUS = donHangBUS;
        this.giaBUS = giaBUS;
    }

    @Transactional(readOnly = true)
    public void themVaoGio(GioHang gio, Long maSanPham, int soLuong, boolean lapDat) {
        if (soLuong < 1) {
            throw new LoiNghiepVu("Số lượng phải từ 1 trở lên.");
        }
        SanPham sp = sanPhamTV.findById(maSanPham).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy sản phẩm."));
        if (sp.getTrangThai() != TrangThaiSanPham.DANG_BAN || sp.getGiaBan() == null) {
            throw new LoiNghiepVu("Sản phẩm " + sp.getTenSanPham() + " hiện không bán.");
        }
        int ton = giaBUS.tonKhaDung(List.of(maSanPham)).getOrDefault(maSanPham, 0);
        int daCo = gio.getDong().getOrDefault(maSanPham, 0);
        if (ton <= 0) {
            throw new LoiNghiepVu("Sản phẩm " + sp.getTenSanPham() + " đang tạm hết hàng.");
        }
        int tong = daCo + soLuong;
        if (tong > ton) {
            throw new LoiNghiepVu("Sản phẩm " + sp.getTenSanPham() + " chỉ còn " + ton + " chiếc, giỏ của bạn đã có " + daCo + ".");
        }
        if (tong > SO_LUONG_TOI_DA_MOT_DONG) {
            throw new LoiNghiepVu("Mỗi mẫu máy đặt tối đa " + SO_LUONG_TOI_DA_MOT_DONG + " chiếc trên một đơn.");
        }
        gio.them(maSanPham, soLuong, lapDat && Boolean.TRUE.equals(sp.getCanLapDat()));
    }

    @Transactional(readOnly = true)
    public void suaSoLuong(GioHang gio, Long maSanPham, int soLuong) {
        if (soLuong <= 0) {
            gio.xoa(maSanPham);
            return;
        }
        if (soLuong > SO_LUONG_TOI_DA_MOT_DONG) {
            throw new LoiNghiepVu("Mỗi mẫu máy đặt tối đa " + SO_LUONG_TOI_DA_MOT_DONG + " chiếc trên một đơn.");
        }
        int ton = giaBUS.tonKhaDung(List.of(maSanPham)).getOrDefault(maSanPham, 0);
        int cu = gio.getDong().getOrDefault(maSanPham, 0);
        if (soLuong > cu && soLuong > ton) {
            throw new LoiNghiepVu("Mẫu máy này chỉ còn " + ton + " chiếc.");
        }
        gio.datSoLuong(maSanPham, soLuong);
    }

    @Transactional(readOnly = true)
    public BangTinh tinh(GioHang gio, boolean noiThanh, String maGiamGia) {
        Map<Long, Integer> dongGio = gio.getDong();
        List<DongGio> dong = new ArrayList<>();
        if (!dongGio.isEmpty()) {
            Map<Long, SanPham> theoMa = new LinkedHashMap<>();
            for (SanPham sp : sanPhamTV.theoMa(dongGio.keySet())) {
                theoMa.put(sp.getMaSanPham(), sp);
            }
            List<SanPham> coThuTu = new ArrayList<>();
            for (Long ma : dongGio.keySet()) {
                if (theoMa.containsKey(ma)) {
                    coThuTu.add(theoMa.get(ma));
                } else {
                    gio.xoa(ma);
                }
            }
            for (TheSanPham the : giaBUS.dongThe(coThuTu)) {
                dong.add(new DongGio(the, dongGio.get(the.getMa()), gio.coLapDat(the.getMa())));
            }
        }
        long tienHang = dong.stream().mapToLong(DongGio::getThanhTien).sum();
        long giamSp = dong.stream().mapToLong(DongGio::getGiam).sum();
        KhuyenMai ma = null;
        long giamMa = 0;
        String loiMa = null;
        if (maGiamGia != null && !maGiamGia.isBlank() && !dong.isEmpty()) {
            try {
                ma = giaBUS.kiemMaGiamGia(maGiamGia, tienHang - giamSp);
                giamMa = GiaBUS.giamMoiChiec(ma, tienHang - giamSp);
            } catch (LoiNghiepVu e) {
                loiMa = e.getMessage();
            }
        }
        return new BangTinh(dong, noiThanh, ma, giamMa, loiMa);
    }

    @Transactional(readOnly = true)
    public List<DiaChiGiaoHang> soDiaChi(KhachHang kh) {
        return kh == null ? List.of() : khachTV.soDiaChi(kh.getMaKhachHang());
    }

    @Transactional(readOnly = true)
    public void dienTuDiaChiDaLuu(PhieuDatHang phieu, KhachHang kh) {
        if (phieu.getMaDiaChi() == null) {
            return;
        }
        DiaChiGiaoHang dc = diaChiDAO.findById(phieu.getMaDiaChi()).orElse(null);
        if (kh == null || dc == null || !dc.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang())) {
            phieu.setMaDiaChi(null);
            return;
        }
        phieu.setHoTen(dc.getHoTenNguoiNhan());
        phieu.setSoDienThoai(dc.getSoDienThoai());
        phieu.setDiaChi(dc.getDiaChi());
        phieu.setTinhThanh(null);
        phieu.setQuanHuyen(null);
        phieu.setNoiThanh(dc.getNoiThanh());
    }

    @Transactional(readOnly = true)
    public void dienMacDinh(PhieuDatHang phieu, KhachHang kh) {
        if (kh == null) {
            return;
        }
        phieu.setHoTen(kh.getHoTen());
        phieu.setSoDienThoai(kh.getSoDienThoai());
        phieu.setEmail(kh.getEmail());
        List<DiaChiGiaoHang> ds = khachTV.soDiaChi(kh.getMaKhachHang());
        if (!ds.isEmpty()) {
            phieu.setMaDiaChi(ds.get(0).getMaDiaChi());
            dienTuDiaChiDaLuu(phieu, kh);
        }
    }

    public void apLapDat(GioHang gio, PhieuDatHang phieu) {
        for (Long ma : gio.getDong().keySet()) {
            gio.datLapDat(ma, phieu.getLapDat().contains(ma));
        }
    }

    @Transactional
    public DonHang datHang(PhieuDatHang phieu, GioHang gio, KhachHang kh, boolean dongYDieuKien, boolean dongYXuLyDuLieu,
                           boolean dongYQuangCao, Long tongDaXem) {
        if (!dongYDieuKien || !dongYXuLyDuLieu) {
            throw new LoiNghiepVu("Bạn cần đồng ý điều kiện giao dịch và đồng ý cho cửa hàng dùng dữ liệu để giao hàng, bảo hành mới đặt được đơn.");
        }
        Map<String, String> loi = phieu.kiemTra();
        if (!loi.isEmpty()) {
            throw new LoiNghiepVu(loi.values().iterator().next());
        }
        BangTinh bang = tinh(gio, Boolean.TRUE.equals(phieu.getNoiThanh()), phieu.getMaGiamGia());
        if (bang.isTrong()) {
            throw new LoiNghiepVu("Giỏ hàng đang trống.");
        }
        if (bang.isCoLoi()) {
            String ds = bang.getDong().stream().filter(d -> d.getLoi() != null)
                    .map(d -> d.getThe().getTen() + " (" + d.getLoi().toLowerCase() + ")").collect(Collectors.joining(", "));
            throw new LoiNghiepVu("Có mẫu máy không đặt được: " + ds + ". Bạn bỏ khỏi đơn hoặc sửa số lượng ở giỏ hàng.");
        }
        if (bang.getLoiMa() != null) {
            throw new LoiNghiepVu(bang.getLoiMa());
        }
        if (tongDaXem == null || tongDaXem != bang.getTongTien()) {
            throw new LoiNghiepVu("Giá hoặc khuyến mãi vừa thay đổi, tổng tiền mới là " + DinhDangTien.tien(bang.getTongTien()) + ". Bạn xem lại đơn rồi đặt lại.");
        }
        DonHang don = new DonHang();
        don.setKhachHang(kh);
        don.setHoTenNguoiNhan(phieu.getHoTen().trim());
        don.setSoDienThoaiNhan(phieu.getSoDienThoai().trim());
        don.setEmail(phieu.getEmail() == null || phieu.getEmail().isBlank() ? null : phieu.getEmail().trim());
        don.setDiaChiGiao(phieu.diaChiDayDu());
        don.setNoiThanh(bang.isNoiThanh());
        don.setCanLapDat(bang.isCoLapDat());
        don.setNgayDat(LocalDateTime.now());
        don.setTienHang(bang.getTienHang());
        don.setTienGiam(bang.getTienGiam());
        don.setPhiGiao(bang.getPhiGiao());
        don.setPhiLapDat(bang.getPhiLapDat());
        don.setTongTien(bang.getTongTien());
        don.setTienCoc(bang.getTienCoc());
        don.setPhuongThucThanhToan(phieu.phuongThucEnum());
        don.setTrangThaiThanhToan(TrangThaiThanhToan.CHUA_THANH_TOAN);
        don.setTrangThai(TrangThaiDonHang.CHO_XAC_NHAN);
        KhuyenMai km = bang.getMaGiam();
        if (km == null) {
            km = bang.getDong().stream().map(d -> d.getThe().getKhuyenMai()).filter(k -> k != null).findFirst().orElse(null);
        }
        don.setKhuyenMai(km);
        don.setGhiChu(ghiChu(phieu, bang));
        don.setDongYXuLyDuLieu(true);
        don.setDongYQuangCao(dongYQuangCao);
        for (DongGio d : bang.getDong()) {
            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(don);
            ct.setSanPham(d.getThe().getSanPham());
            ct.setTenSanPhamLucBan(d.getThe().getTen());
            ct.setSoLuong(d.getSoLuong());
            ct.setDonGia(d.getDonGia());
            don.getChiTiet().add(ct);
        }
        donHangTV.save(don);
        donHangBUS.ghiTaoMoi(don, null);
        if (bang.getMaGiam() != null) {
            KhuyenMai ma = khuyenMaiTV.findById(bang.getMaGiam().getMaKhuyenMai()).orElseThrow();
            ma.setSoLuotDaDung((ma.getSoLuotDaDung() == null ? 0 : ma.getSoLuotDaDung()) + 1);
            khuyenMaiTV.save(ma);
        }
        if (kh != null && phieu.getMaDiaChi() == null && phieu.isLuuDiaChi()) {
            DiaChiGiaoHang dc = new DiaChiGiaoHang();
            dc.setKhachHang(kh);
            dc.setHoTenNguoiNhan(don.getHoTenNguoiNhan());
            dc.setSoDienThoai(don.getSoDienThoaiNhan());
            dc.setDiaChi(don.getDiaChiGiao());
            dc.setNoiThanh(don.getNoiThanh());
            dc.setMacDinh(khachTV.soDiaChi(kh.getMaKhachHang()).isEmpty());
            diaChiDAO.save(dc);
        }
        gio.xoaHet();
        return don;
    }

    private String ghiChu(PhieuDatHang phieu, BangTinh bang) {
        StringBuilder sb = new StringBuilder();
        if (phieu.getGhiChu() != null && !phieu.getGhiChu().isBlank()) {
            sb.append(phieu.getGhiChu().trim());
        }
        if (bang.isCoLapDat()) {
            String lap = bang.getDong().stream().filter(DongGio::isLapDat).map(d -> d.getThe().getTen()).collect(Collectors.joining(", "));
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("Cần lắp: ").append(lap);
        }
        String kq = sb.toString();
        if (kq.length() > 500) {
            kq = kq.substring(0, 500);
        }
        return kq.isEmpty() ? null : kq;
    }
}
