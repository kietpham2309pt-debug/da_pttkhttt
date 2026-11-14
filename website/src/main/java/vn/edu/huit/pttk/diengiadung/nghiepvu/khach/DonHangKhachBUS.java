package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.DonHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.GiaoDichTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.HauMaiTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhuyenMaiTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuTrangThaiDon;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhuongThucThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiThanhToan;

@Service
public class DonHangKhachBUS {

    public static final String CACH_CHUYEN_KHOAN = "Chuyển khoản/QR qua cổng";
    public static final String CACH_THE = "Thẻ qua cổng";

    private final DonHangTruyVanKhach donHangTV;
    private final GiaoDichTruyVanKhach giaoDichTV;
    private final HauMaiTruyVanKhach hauMaiTV;
    private final KhuyenMaiTruyVanKhach khuyenMaiTV;
    private final DonHangBUS donHangBUS;

    public DonHangKhachBUS(DonHangTruyVanKhach donHangTV, GiaoDichTruyVanKhach giaoDichTV, HauMaiTruyVanKhach hauMaiTV,
                           KhuyenMaiTruyVanKhach khuyenMaiTV, DonHangBUS donHangBUS) {
        this.donHangTV = donHangTV;
        this.giaoDichTV = giaoDichTV;
        this.hauMaiTV = hauMaiTV;
        this.khuyenMaiTV = khuyenMaiTV;
        this.donHangBUS = donHangBUS;
    }

    public static boolean dangChoCoc(DonHang don, long daTra) {
        return don.getTienCoc() != null && don.getTienCoc() > 0 && daTra < don.getTienCoc();
    }

    public static boolean coTheThanhToan(DonHang don, long daTra) {
        boolean dangMo = don.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN || don.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN;
        boolean conNo = don.getTongTien() - daTra > 0;
        boolean trangThaiTien = don.getTrangThaiThanhToan() != TrangThaiThanhToan.DA_THANH_TOAN
                && don.getTrangThaiThanhToan() != TrangThaiThanhToan.DA_HOAN_TIEN;
        boolean canTraTruoc = don.getPhuongThucThanhToan() == PhuongThucThanhToan.CONG_THANH_TOAN || dangChoCoc(don, daTra);
        return dangMo && conNo && trangThaiTien && canTraTruoc;
    }

    public static boolean coTheHuy(DonHang don) {
        return don.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN;
    }

    public static long soTienPhaiTra(DonHang don, long daTra, boolean traHet) {
        long conLai = Math.max(0, don.getTongTien() - daTra);
        if (dangChoCoc(don, daTra) && !traHet) {
            return Math.min(conLai, don.getTienCoc() - daTra);
        }
        return conLai;
    }

    @Transactional(readOnly = true)
    public Optional<Long> timDon(String soDienThoai, Long maDon) {
        return donHangTV.findById(maDon).filter(d -> d.getSoDienThoaiNhan().equals(soDienThoai)).map(DonHang::getMaDonHang);
    }

    @Transactional(readOnly = true)
    public boolean laChuDon(Long maDon, KhachHang kh) {
        if (kh == null) {
            return false;
        }
        return donHangTV.findById(maDon)
                .map(d -> d.getKhachHang() != null && d.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public ThongTinDon thongTin(Long maDon) {
        DonHang don = donHangTV.layDayDu(maDon).orElse(null);
        if (don == null) {
            return null;
        }
        List<LichSuTrangThaiDon> lichSu = donHangBUS.lichSu(don);
        List<GiaoDichThanhToan> giaoDich = giaoDichTV.findByDonHang_MaDonHangOrderByThoiDiemAsc(maDon);
        List<PhieuGiaoHang> phieu = hauMaiTV.phieuGiao(maDon);
        long tra = 0;
        long hoan = 0;
        for (GiaoDichThanhToan gd : giaoDich) {
            if (gd.getTrangThai() != TrangThaiGiaoDich.THANH_CONG) {
                continue;
            }
            if (gd.getLoai() == LoaiGiaoDich.HOAN_TIEN) {
                hoan += gd.getSoTien();
            } else {
                tra += gd.getSoTien();
            }
        }
        if (don.getKhuyenMai() != null) {
            don.getKhuyenMai().getTenKhuyenMai();
        }
        return new ThongTinDon(don, lichSu, giaoDich, phieu.isEmpty() ? null : phieu.get(0), tra - hoan, hoan);
    }

    @Transactional(readOnly = true)
    public List<DonHang> donCuaKhach(KhachHang kh) {
        return donHangTV.cuaKhach(kh.getMaKhachHang());
    }

    @Transactional
    public GiaoDichThanhToan ghiKetQuaThanhToan(Long maDon, String maCong, boolean traHet, String cachTra, boolean thanhCong) {
        List<GiaoDichThanhToan> daCo = giaoDichTV.findByMaGiaoDichCong(maCong);
        if (!daCo.isEmpty()) {
            return daCo.get(0);
        }
        ThongTinDon tt = thongTin(maDon);
        if (tt == null) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng.");
        }
        DonHang don = tt.getDon();
        if (!coTheThanhToan(don, tt.getDaTra())) {
            throw new LoiNghiepVu("Đơn không cần thanh toán.");
        }
        long soTien = soTienPhaiTra(don, tt.getDaTra(), traHet);
        boolean laCoc = dangChoCoc(don, tt.getDaTra()) && !traHet && soTien < tt.getConLai();
        GiaoDichThanhToan gd = new GiaoDichThanhToan();
        gd.setDonHang(don);
        gd.setLoai(laCoc ? LoaiGiaoDich.COC : LoaiGiaoDich.THANH_TOAN);
        gd.setSoTien(soTien);
        gd.setPhuongThuc(CACH_THE.equals(cachTra) ? CACH_THE : CACH_CHUYEN_KHOAN);
        gd.setMaGiaoDichCong(maCong);
        gd.setTrangThai(thanhCong ? TrangThaiGiaoDich.THANH_CONG : TrangThaiGiaoDich.THAT_BAI);
        gd.setThoiDiem(LocalDateTime.now());
        giaoDichTV.save(gd);
        if (thanhCong) {
            long daTraMoi = tt.getDaTra() + soTien;
            don.setTrangThaiThanhToan(daTraMoi >= don.getTongTien() ? TrangThaiThanhToan.DA_THANH_TOAN : TrangThaiThanhToan.DA_DAT_COC);
            donHangTV.save(don);
        }
        return gd;
    }

    @Transactional
    public void doiSangTienMat(Long maDon) {
        ThongTinDon tt = thongTin(maDon);
        if (tt == null) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng.");
        }
        DonHang don = tt.getDon();
        if (don.getTienCoc() != null && don.getTienCoc() > 0) {
            throw new LoiNghiepVu("Đơn từ " + DinhDangTien.tien(GiaBUS.NGUONG_COC) + " phải đặt cọc trực tuyến, chưa đổi sang tiền mặt được.");
        }
        if (tt.getDaTra() > 0 || !(don.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN || don.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN)) {
            throw new LoiNghiepVu("Đơn này không đổi cách thanh toán được nữa.");
        }
        don.setPhuongThucThanhToan(PhuongThucThanhToan.TIEN_MAT_KHI_NHAN);
        donHangTV.save(don);
    }

    @Transactional
    public void huyDon(Long maDon, String lyDo) {
        if (lyDo == null || lyDo.isBlank()) {
            throw new LoiNghiepVu("Bạn chưa nhập lý do huỷ đơn.");
        }
        String ly = lyDo.trim();
        if (ly.length() > 200) {
            throw new LoiNghiepVu("Lý do huỷ dài quá 200 ký tự.");
        }
        ThongTinDon tt = thongTin(maDon);
        if (tt == null) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng.");
        }
        DonHang don = tt.getDon();
        if (!coTheHuy(don)) {
            throw new LoiNghiepVu("Đơn đã được cửa hàng xác nhận, bạn gọi 0339 999 999 để được hỗ trợ huỷ.");
        }
        don.setLyDoHuy(ly);
        donHangBUS.doiTrangThai(don, TrangThaiDonHang.DA_HUY, null, "Khách huỷ: " + ly);
        if (tt.getDaTra() > 0) {
            String cach = tt.getGiaoDich().stream()
                    .filter(g -> g.getTrangThai() == TrangThaiGiaoDich.THANH_CONG && g.getLoai() != LoaiGiaoDich.HOAN_TIEN)
                    .map(GiaoDichThanhToan::getPhuongThuc).reduce((a, b) -> b).orElse(CACH_CHUYEN_KHOAN);
            GiaoDichThanhToan hoan = new GiaoDichThanhToan();
            hoan.setDonHang(don);
            hoan.setLoai(LoaiGiaoDich.HOAN_TIEN);
            hoan.setSoTien(tt.getDaTra());
            hoan.setPhuongThuc(cach);
            hoan.setMaGiaoDichCong("HOAN-" + maDon + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            hoan.setTrangThai(TrangThaiGiaoDich.THANH_CONG);
            hoan.setThoiDiem(LocalDateTime.now());
            giaoDichTV.save(hoan);
            don.setTrangThaiThanhToan(TrangThaiThanhToan.DA_HOAN_TIEN);
            donHangTV.save(don);
        }
        KhuyenMai km = don.getKhuyenMai();
        if (km != null && km.getLoai() == LoaiKhuyenMai.MA_GIAM_GIA && km.getSoLuotDaDung() != null && km.getSoLuotDaDung() > 0) {
            km.setSoLuotDaDung(km.getSoLuotDaDung() - 1);
            khuyenMaiTV.save(km);
        }
    }
}
