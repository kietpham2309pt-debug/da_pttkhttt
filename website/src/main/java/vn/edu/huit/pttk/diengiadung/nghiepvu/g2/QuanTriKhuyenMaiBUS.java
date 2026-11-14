package vn.edu.huit.pttk.diengiadung.nghiepvu.g2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.KhuyenMaiTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.SanPhamTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.thucthe.KhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.KieuGiam;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiKhuyenMai;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiKhuyenMai;

@Service
public class QuanTriKhuyenMaiBUS {

    public static final String SAP_CHAY = "Sắp chạy";
    public static final String DANG_CHAY = "Đang chạy";
    public static final String DA_KET_THUC = "Đã kết thúc";
    public static final String TAM_DUNG = "Tạm dừng";
    private static final String DAU_LY_DO = " (dưới giá nhập: ";

    private final KhuyenMaiTruyVanG12 truyVan;
    private final SanPhamTruyVanG12 sanPhamTruyVan;
    private final QuanTriSanPhamBUS sanPhamBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public QuanTriKhuyenMaiBUS(KhuyenMaiTruyVanG12 truyVan, SanPhamTruyVanG12 sanPhamTruyVan, QuanTriSanPhamBUS sanPhamBUS,
                               TaiKhoanBUS taiKhoanBUS) {
        this.truyVan = truyVan;
        this.sanPhamTruyVan = sanPhamTruyVan;
        this.sanPhamBUS = sanPhamBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    public static String giaiDoan(KhuyenMai km) {
        LocalDate homNay = LocalDate.now();
        if (homNay.isBefore(km.getNgayBatDau())) {
            return SAP_CHAY;
        }
        if (homNay.isAfter(km.getNgayKetThuc())) {
            return DA_KET_THUC;
        }
        return DANG_CHAY;
    }

    public static String nhanTrangThai(KhuyenMai km) {
        String gd = giaiDoan(km);
        if (km.getTrangThai() == TrangThaiKhuyenMai.TAM_DUNG && !gd.equals(DA_KET_THUC)) {
            return TAM_DUNG;
        }
        return gd;
    }

    @Transactional(readOnly = true)
    public List<DongKhuyenMai> danhSach() {
        List<DongKhuyenMai> ds = new ArrayList<>();
        for (KhuyenMai km : truyVan.layTatCa()) {
            ds.add(new DongKhuyenMai(km, nhanTrangThai(km), giaiDoan(km), km.getNguoiTao().getHoTen()));
        }
        return ds;
    }

    @Transactional(readOnly = true)
    public List<DongKhuyenMai> dangChay() {
        return danhSach().stream().filter(d -> d.trangThai().equals(DANG_CHAY)).toList();
    }

    @Transactional(readOnly = true)
    public KhuyenMai tim(Long ma) {
        return truyVan.timDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy chương trình khuyến mãi."));
    }

    @Transactional(readOnly = true)
    public BieuMauKhuyenMai bieuMauSua(Long ma) {
        KhuyenMai km = tim(ma);
        BieuMauKhuyenMai bm = new BieuMauKhuyenMai();
        bm.setMaKhuyenMai(km.getMaKhuyenMai());
        bm.setTenKhuyenMai(km.getTenKhuyenMai());
        bm.setLoai(km.getLoai().name());
        bm.setMaCode(km.getMaCode());
        bm.setKieuGiam(km.getKieuGiam().name());
        bm.setMucGiam(km.getMucGiam());
        bm.setGiamToiDa(km.getGiamToiDa());
        bm.setDonToiThieu(km.getDonToiThieu());
        bm.setQuaTang(km.getQuaTang());
        bm.setNgayBatDau(km.getNgayBatDau());
        bm.setNgayKetThuc(km.getNgayKetThuc());
        bm.setSoLuotToiDa(km.getSoLuotToiDa());
        bm.setSanPham(km.getSanPham().stream().map(SanPham::getMaSanPham).toList());
        return bm;
    }

    @Transactional
    public KhuyenMai luu(BieuMauKhuyenMai bm, Map<String, String> loiChuyenDoi) {
        boolean themMoi = bm.getMaKhuyenMai() == null;
        KhuyenMai km = themMoi ? new KhuyenMai() : tim(bm.getMaKhuyenMai());
        Map<String, String> loi = new LinkedHashMap<>(loiChuyenDoi);
        String giaiDoan = themMoi ? SAP_CHAY : giaiDoan(km);
        if (giaiDoan.equals(DA_KET_THUC)) {
            throw new LoiNghiepVu("Chương trình đã kết thúc, không sửa được.");
        }
        boolean chiSuaTenVaNgayKetThuc = giaiDoan.equals(DANG_CHAY);
        if (chiSuaTenVaNgayKetThuc) {
            BieuMauKhuyenMai cu = bieuMauSua(km.getMaKhuyenMai());
            String ten = bm.getTenKhuyenMai();
            LocalDate ketThuc = bm.getNgayKetThuc();
            String lyDo = bm.getLyDoDuoiGiaNhap();
            bm = cu;
            bm.setTenKhuyenMai(ten);
            bm.setNgayKetThuc(ketThuc);
            bm.setLyDoDuoiGiaNhap(lyDo);
            loi.keySet().retainAll(List.of("tenKhuyenMai", "ngayKetThuc"));
        }
        String ten = cat(bm.getTenKhuyenMai());
        if (ten.isEmpty()) {
            loi.putIfAbsent("tenKhuyenMai", "Nhập tên chương trình.");
        } else if (ten.length() > 150) {
            loi.putIfAbsent("tenKhuyenMai", "Tên tối đa 150 ký tự.");
        }
        LoaiKhuyenMai loai = docEnum(LoaiKhuyenMai.class, bm.getLoai());
        if (loai == null) {
            loi.putIfAbsent("loai", "Chọn loại khuyến mãi.");
        }
        KieuGiam kieu = docEnum(KieuGiam.class, bm.getKieuGiam());
        if (kieu == null) {
            loi.putIfAbsent("kieuGiam", "Chọn kiểu giảm.");
        }
        if (bm.getMucGiam() == null || bm.getMucGiam() <= 0) {
            loi.putIfAbsent("mucGiam", "Mức giảm phải lớn hơn 0.");
        } else if (kieu == KieuGiam.PHAN_TRAM && bm.getMucGiam() > 100) {
            loi.putIfAbsent("mucGiam", "Giảm theo phần trăm không quá 100.");
        }
        if (bm.getGiamToiDa() != null && bm.getGiamToiDa() <= 0) {
            loi.putIfAbsent("giamToiDa", "Giảm tối đa phải lớn hơn 0 hoặc để trống.");
        }
        if (bm.getDonToiThieu() == null) {
            bm.setDonToiThieu(0L);
        }
        if (bm.getDonToiThieu() < 0) {
            loi.putIfAbsent("donToiThieu", "Đơn tối thiểu không được âm.");
        }
        String qua = cat(bm.getQuaTang());
        if (qua.length() > 150) {
            loi.putIfAbsent("quaTang", "Tên quà tối đa 150 ký tự.");
        }
        if (bm.getNgayBatDau() == null) {
            loi.putIfAbsent("ngayBatDau", "Chọn ngày bắt đầu.");
        } else if (themMoi && bm.getNgayBatDau().isBefore(LocalDate.now())) {
            loi.putIfAbsent("ngayBatDau", "Ngày bắt đầu không được trước hôm nay.");
        }
        if (bm.getNgayKetThuc() == null) {
            loi.putIfAbsent("ngayKetThuc", "Chọn ngày kết thúc.");
        } else if (bm.getNgayBatDau() != null && bm.getNgayKetThuc().isBefore(bm.getNgayBatDau())) {
            loi.putIfAbsent("ngayKetThuc", "Ngày kết thúc không được trước ngày bắt đầu.");
        } else if (chiSuaTenVaNgayKetThuc && bm.getNgayKetThuc().isBefore(LocalDate.now())) {
            loi.putIfAbsent("ngayKetThuc", "Chương trình đang chạy: ngày kết thúc không được trước hôm nay.");
        }
        if (bm.getSoLuotToiDa() != null && bm.getSoLuotToiDa() <= 0) {
            loi.putIfAbsent("soLuotToiDa", "Số lượt tối đa phải lớn hơn 0 hoặc để trống.");
        } else if (bm.getSoLuotToiDa() != null && km.getSoLuotDaDung() != null && bm.getSoLuotToiDa() < km.getSoLuotDaDung()) {
            loi.putIfAbsent("soLuotToiDa", "Đã dùng " + km.getSoLuotDaDung() + " lượt, số lượt tối đa không được nhỏ hơn.");
        }
        String maCode = null;
        List<SanPham> sanPham = new ArrayList<>();
        if (loai == LoaiKhuyenMai.MA_GIAM_GIA) {
            maCode = cat(bm.getMaCode()).toUpperCase(Locale.ROOT);
            if (maCode.isEmpty()) {
                loi.putIfAbsent("maCode", "Nhập mã giảm giá.");
            } else if (!maCode.matches("[A-Z0-9_-]{3,30}")) {
                loi.putIfAbsent("maCode", "Mã 3–30 ký tự, chỉ gồm chữ không dấu, số, gạch ngang, gạch dưới.");
            } else if (truyVan.existsByMaCodeIgnoreCaseAndMaKhuyenMaiNot(maCode, themMoi ? -1L : km.getMaKhuyenMai())) {
                loi.putIfAbsent("maCode", "Mã giảm giá đã tồn tại.");
            }
        } else if (loai == LoaiKhuyenMai.GIAM_THEO_SAN_PHAM) {
            if (bm.getSanPham().isEmpty()) {
                loi.putIfAbsent("sanPham", "Chọn ít nhất một sản phẩm áp dụng.");
            } else {
                sanPham = sanPhamTruyVan.findAllById(new LinkedHashSet<>(bm.getSanPham()));
            }
        }
        if (loi.isEmpty() && loai == LoaiKhuyenMai.GIAM_THEO_SAN_PHAM) {
            List<String> trung = new ArrayList<>();
            for (Object[] r : truyVan.trungThoiGian(sanPham.stream().map(SanPham::getMaSanPham).toList(), loai,
                    bm.getNgayBatDau(), bm.getNgayKetThuc(), themMoi ? -1L : km.getMaKhuyenMai())) {
                trung.add("\"" + r[0] + "\" đang có chương trình \"" + r[1] + "\"");
            }
            if (!trung.isEmpty()) {
                loi.put("sanPham", "Trùng thời gian với khuyến mãi cùng loại: " + String.join("; ", trung)
                        + ". Bỏ mẫu máy đó khỏi phạm vi hoặc đổi ngày.");
            }
        }
        List<String> duoiGiaNhap = new ArrayList<>();
        if (loi.isEmpty() && loai == LoaiKhuyenMai.GIAM_THEO_SAN_PHAM) {
            for (SanPham sp : sanPham) {
                if (sp.getGiaBan() == null) {
                    continue;
                }
                long giam = kieu == KieuGiam.PHAN_TRAM ? sp.getGiaBan() * bm.getMucGiam() / 100 : bm.getMucGiam();
                if (bm.getGiamToiDa() != null) {
                    giam = Math.min(giam, bm.getGiamToiDa());
                }
                long giaSau = sp.getGiaBan() - giam;
                if (giaSau <= 0) {
                    loi.put("mucGiam", "Mức giảm làm giá của \"" + sp.getTenSanPham() + "\" về 0 hoặc âm.");
                    break;
                }
                Long giaNhap = sanPhamBUS.giaNhapGanNhat(sp.getMaSanPham());
                if (giaNhap != null && giaSau < giaNhap) {
                    duoiGiaNhap.add("\"" + sp.getTenSanPham() + "\": giá sau giảm " + QuanTriSanPhamBUS.tien(giaSau)
                            + " < giá nhập gần nhất " + QuanTriSanPhamBUS.tien(giaNhap));
                }
            }
        }
        String lyDo = cat(bm.getLyDoDuoiGiaNhap());
        if (loi.isEmpty() && !duoiGiaNhap.isEmpty() && !ten.contains(DAU_LY_DO)) {
            if (lyDo.isEmpty()) {
                loi.put("lyDoDuoiGiaNhap", "Cảnh báo giá sau giảm thấp hơn giá nhập gần nhất: " + String.join("; ", duoiGiaNhap)
                        + ". Ghi lý do để lưu (lý do được ghi kèm tên chương trình để chủ cửa hàng xem lại).");
            } else {
                String tenMoi = ten + DAU_LY_DO + lyDo + ")";
                if (tenMoi.length() > 150) {
                    loi.put("lyDoDuoiGiaNhap", "Tên chương trình kèm lý do vượt 150 ký tự, ghi lý do ngắn hơn.");
                } else {
                    ten = tenMoi;
                }
            }
        }
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        km.setTenKhuyenMai(ten);
        km.setLoai(loai);
        km.setMaCode(maCode);
        km.setKieuGiam(kieu);
        km.setMucGiam(bm.getMucGiam());
        km.setGiamToiDa(bm.getGiamToiDa());
        km.setDonToiThieu(bm.getDonToiThieu());
        km.setQuaTang(qua.isEmpty() ? null : qua);
        km.setNgayBatDau(bm.getNgayBatDau());
        km.setNgayKetThuc(bm.getNgayKetThuc());
        km.setSoLuotToiDa(bm.getSoLuotToiDa());
        km.getSanPham().clear();
        km.getSanPham().addAll(sanPham);
        if (themMoi) {
            km.setSoLuotDaDung(0);
            km.setTrangThai(TrangThaiKhuyenMai.DANG_CHAY);
            km.setNguoiTao(taiKhoanBUS.batBuocNhanVien());
        }
        return truyVan.save(km);
    }

    @Transactional
    public String tamDung(Long ma) {
        KhuyenMai km = tim(ma);
        if (km.getTrangThai() == TrangThaiKhuyenMai.TAM_DUNG) {
            throw new LoiNghiepVu("Chương trình đang tạm dừng.");
        }
        if (giaiDoan(km).equals(DA_KET_THUC)) {
            throw new LoiNghiepVu("Chương trình đã kết thúc.");
        }
        km.setTrangThai(TrangThaiKhuyenMai.TAM_DUNG);
        truyVan.save(km);
        return km.getTenKhuyenMai();
    }

    @Transactional
    public String chayLai(Long ma) {
        KhuyenMai km = tim(ma);
        if (km.getTrangThai() != TrangThaiKhuyenMai.TAM_DUNG) {
            throw new LoiNghiepVu("Chương trình không ở trạng thái tạm dừng.");
        }
        if (giaiDoan(km).equals(DA_KET_THUC)) {
            throw new LoiNghiepVu("Chương trình đã kết thúc, không chạy lại được.");
        }
        if (km.getLoai() == LoaiKhuyenMai.GIAM_THEO_SAN_PHAM && !km.getSanPham().isEmpty()) {
            List<Object[]> trung = truyVan.trungThoiGian(km.getSanPham().stream().map(SanPham::getMaSanPham).toList(), km.getLoai(),
                    km.getNgayBatDau(), km.getNgayKetThuc(), km.getMaKhuyenMai());
            if (!trung.isEmpty()) {
                throw new LoiNghiepVu("Không chạy lại được: \"" + trung.get(0)[0] + "\" đang có chương trình \"" + trung.get(0)[1]
                        + "\" cùng loại trùng thời gian.");
            }
        }
        km.setTrangThai(TrangThaiKhuyenMai.DANG_CHAY);
        truyVan.save(km);
        return km.getTenKhuyenMai();
    }

    private static <E extends Enum<E>> E docEnum(Class<E> lop, String gt) {
        if (gt == null) {
            return null;
        }
        try {
            return Enum.valueOf(lop, gt);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String cat(String s) {
        return s == null ? "" : s.trim();
    }

    public record DongKhuyenMai(KhuyenMai km, String trangThai, String giaiDoan, String nguoiTao) {
    }
}
