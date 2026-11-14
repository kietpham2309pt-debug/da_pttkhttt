package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.DonDatTruyVanG5;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.SanPhamTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonDatHangNCC;
import vn.edu.huit.pttk.diengiadung.thucthe.DonDatHangNCC;
import vn.edu.huit.pttk.diengiadung.thucthe.NhaCungCap;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonDat;

@Service
public class DonDatBUS {

    public record DongDat(Long maSanPham, String tenSanPham, String model, int soLuong, long daNhap, long trongKho) {

        public long conThieu() {
            return Math.max(0, soLuong - daNhap);
        }
    }

    public record DonDatXem(DonDatHangNCC don, List<DongDat> dong) {

        public boolean daNhanDu() {
            return dong.stream().allMatch(d -> d.conThieu() == 0);
        }
    }

    private final DonDatTruyVanG5 donDatDAO;
    private final SanPhamTruyVanG5 sanPhamDAO;
    private final NhaCungCapBUS nhaCungCapBUS;
    private final TonKhoBUS tonKhoBUS;

    public DonDatBUS(DonDatTruyVanG5 donDatDAO, SanPhamTruyVanG5 sanPhamDAO, NhaCungCapBUS nhaCungCapBUS, TonKhoBUS tonKhoBUS) {
        this.donDatDAO = donDatDAO;
        this.sanPhamDAO = sanPhamDAO;
        this.nhaCungCapBUS = nhaCungCapBUS;
        this.tonKhoBUS = tonKhoBUS;
    }

    @Transactional(readOnly = true)
    public List<DonDatHangNCC> danhSach(TrangThaiDonDat tt) {
        return tt == null ? donDatDAO.danhSach() : donDatDAO.theoTrangThai(tt);
    }

    @Transactional(readOnly = true)
    public List<DonDatHangNCC> choNhap() {
        return donDatDAO.theoTrangThai(TrangThaiDonDat.DA_GUI);
    }

    @Transactional(readOnly = true)
    public DonDatXem xem(Long ma) {
        DonDatHangNCC d = donDatDAO.chiTiet(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn đặt hàng #" + ma));
        Map<Long, Long> daNhap = daNhapTheoMau(ma);
        Map<Long, Long> ton = tonKhoBUS.trongKhoTheoMau();
        List<DongDat> dong = new ArrayList<>();
        for (ChiTietDonDatHangNCC c : d.getChiTiet()) {
            SanPham sp = c.getSanPham();
            dong.add(new DongDat(sp.getMaSanPham(), sp.getTenSanPham(), sp.getModel(), c.getSoLuong(),
                    daNhap.getOrDefault(sp.getMaSanPham(), 0L), ton.getOrDefault(sp.getMaSanPham(), 0L)));
        }
        dong.sort((a, b) -> a.tenSanPham().compareToIgnoreCase(b.tenSanPham()));
        return new DonDatXem(d, dong);
    }

    Map<Long, Long> daNhapTheoMau(Long maDonDat) {
        Map<Long, Long> kq = new HashMap<>();
        for (Object[] r : donDatDAO.daNhapTheoMau(maDonDat)) {
            kq.put((Long) r[0], ((Number) r[1]).longValue());
        }
        return kq;
    }

    @Transactional
    public DonDatHangNCC lapDeXuat(Long maNhaCungCap, Map<Long, String> soLuong, String ghiChu, NhanVien nguoiLap) {
        if (maNhaCungCap == null) {
            throw new LoiNghiepVu("Chưa chọn nhà cung cấp.");
        }
        NhaCungCap ncc = nhaCungCapBUS.tim(maNhaCungCap);
        if (!Boolean.TRUE.equals(ncc.getDangHopTac())) {
            throw new LoiNghiepVu("Nhà cung cấp \"" + ncc.getTenNhaCungCap() + "\" đã ngừng hợp tác.");
        }
        Map<Long, Integer> dong = docSoLuong(soLuong);
        if (dong.isEmpty()) {
            throw new LoiNghiepVu("Đơn đặt hàng phải có ít nhất một mẫu máy với số lượng lớn hơn 0.");
        }
        DonDatHangNCC d = new DonDatHangNCC();
        d.setNhaCungCap(ncc);
        d.setNgayLap(LocalDateTime.now());
        d.setTrangThai(TrangThaiDonDat.DE_XUAT);
        d.setNguoiLap(nguoiLap);
        d.setGhiChu(cat(ghiChu == null || ghiChu.isBlank() ? null : ghiChu.trim(), 255));
        for (Map.Entry<Long, Integer> e : dong.entrySet()) {
            ChiTietDonDatHangNCC c = new ChiTietDonDatHangNCC();
            c.setDonDat(d);
            c.setSanPham(timSanPham(e.getKey()));
            c.setSoLuong(e.getValue());
            d.getChiTiet().add(c);
        }
        return donDatDAO.save(d);
    }

    @Transactional
    public void guiNhaCungCap(Long ma, Long maNhaCungCap, Map<Long, String> soLuong, String yKienChu, NhanVien quanLy) {
        DonDatHangNCC d = donDatDAO.chiTiet(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn đặt hàng #" + ma));
        if (d.getTrangThai() != TrangThaiDonDat.DE_XUAT) {
            throw new LoiNghiepVu("Đơn đặt hàng #" + ma + " đang ở trạng thái \"" + d.getTrangThai().getNhan() + "\", không gửi được.");
        }
        if (yKienChu == null || yKienChu.isBlank()) {
            throw new LoiNghiepVu("Phải ghi ý kiến của chủ cửa hàng trước khi gửi nhà cung cấp (đơn đặt hàng phải được chủ duyệt).");
        }
        if (maNhaCungCap != null && !maNhaCungCap.equals(d.getNhaCungCap().getMaNhaCungCap())) {
            NhaCungCap ncc = nhaCungCapBUS.tim(maNhaCungCap);
            if (!Boolean.TRUE.equals(ncc.getDangHopTac())) {
                throw new LoiNghiepVu("Nhà cung cấp \"" + ncc.getTenNhaCungCap() + "\" đã ngừng hợp tác.");
            }
            d.setNhaCungCap(ncc);
        }
        if (soLuong != null && !soLuong.isEmpty()) {
            Map<Long, Integer> moi = docSoLuong(soLuong);
            Iterator<ChiTietDonDatHangNCC> it = d.getChiTiet().iterator();
            while (it.hasNext()) {
                ChiTietDonDatHangNCC c = it.next();
                Long maSp = c.getSanPham().getMaSanPham();
                if (soLuong.containsKey(maSp)) {
                    Integer sl = moi.get(maSp);
                    if (sl == null) {
                        it.remove();
                    } else {
                        c.setSoLuong(sl);
                    }
                }
            }
            if (d.getChiTiet().isEmpty()) {
                throw new LoiNghiepVu("Đơn đặt hàng phải còn ít nhất một mẫu máy.");
            }
        }
        if (Boolean.FALSE.equals(d.getNhaCungCap().getDangHopTac())) {
            throw new LoiNghiepVu("Nhà cung cấp của đơn đã ngừng hợp tác, hãy chọn nhà cung cấp khác.");
        }
        d.setNguoiGui(quanLy);
        d.setTrangThai(TrangThaiDonDat.DA_GUI);
        d.setGhiChu(noi(d.getGhiChu(), "Ý kiến chủ: " + yKienChu.trim()));
        donDatDAO.save(d);
    }

    @Transactional
    public void huy(Long ma, String lyDo) {
        DonDatHangNCC d = donDatDAO.findById(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn đặt hàng #" + ma));
        if (d.getTrangThai() != TrangThaiDonDat.DE_XUAT && d.getTrangThai() != TrangThaiDonDat.DA_GUI) {
            throw new LoiNghiepVu("Đơn đặt hàng #" + ma + " đang ở trạng thái \"" + d.getTrangThai().getNhan() + "\", không huỷ được.");
        }
        if (d.getTrangThai() == TrangThaiDonDat.DA_GUI && !daNhapTheoMau(ma).isEmpty()) {
            throw new LoiNghiepVu("Đơn đặt hàng #" + ma + " đã có phiếu nhập, không huỷ được.");
        }
        if (lyDo == null || lyDo.isBlank()) {
            throw new LoiNghiepVu("Phải ghi lý do huỷ đơn đặt hàng.");
        }
        d.setTrangThai(TrangThaiDonDat.DA_HUY);
        d.setGhiChu(noi(d.getGhiChu(), "Huỷ: " + lyDo.trim()));
        donDatDAO.save(d);
    }

    @Transactional
    public boolean capNhatSauNhap(Long maDonDat) {
        DonDatHangNCC d = donDatDAO.chiTiet(maDonDat).orElseThrow();
        Map<Long, Long> daNhap = daNhapTheoMau(maDonDat);
        boolean du = d.getChiTiet().stream()
                .allMatch(c -> daNhap.getOrDefault(c.getSanPham().getMaSanPham(), 0L) >= c.getSoLuong());
        if (du && d.getTrangThai() == TrangThaiDonDat.DA_GUI) {
            d.setTrangThai(TrangThaiDonDat.DA_NHAN);
            donDatDAO.save(d);
        }
        return du;
    }

    private SanPham timSanPham(Long ma) {
        return sanPhamDAO.findById(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy mẫu máy #" + ma));
    }

    private Map<Long, Integer> docSoLuong(Map<Long, String> soLuong) {
        Map<Long, Integer> kq = new java.util.LinkedHashMap<>();
        if (soLuong == null) {
            return kq;
        }
        for (Map.Entry<Long, String> e : soLuong.entrySet()) {
            String s = e.getValue() == null ? "" : e.getValue().trim();
            if (s.isEmpty()) {
                continue;
            }
            int sl;
            try {
                sl = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                throw new LoiNghiepVu("Số lượng \"" + s + "\" không phải số nguyên.");
            }
            if (sl < 0 || sl > 999) {
                throw new LoiNghiepVu("Số lượng phải từ 0 đến 999.");
            }
            if (sl > 0) {
                kq.put(e.getKey(), sl);
            }
        }
        return kq;
    }

    static String noi(String cu, String them) {
        String s = cu == null || cu.isBlank() ? them : cu + " | " + them;
        return cat(s, 255);
    }

    static String cat(String s, int toiDa) {
        return s == null || s.length() <= toiDa ? s : s.substring(0, toiDa);
    }
}
