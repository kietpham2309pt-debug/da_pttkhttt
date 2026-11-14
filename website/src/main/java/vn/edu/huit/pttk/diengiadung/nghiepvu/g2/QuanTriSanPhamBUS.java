package vn.edu.huit.pttk.diengiadung.nghiepvu.g2;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DanhMucDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.LichSuGiaDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.ThuongHieuDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.SanPhamTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.LichSuGia;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.ThongSoKyThuat;
import vn.edu.huit.pttk.diengiadung.thucthe.ThuongHieu;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

@Service
public class QuanTriSanPhamBUS {

    private final SanPhamTruyVanG12 truyVan;
    private final DanhMucDAO danhMucDAO;
    private final ThuongHieuDAO thuongHieuDAO;
    private final LichSuGiaDAO lichSuGiaDAO;
    private final TaiKhoanBUS taiKhoanBUS;

    public QuanTriSanPhamBUS(SanPhamTruyVanG12 truyVan, DanhMucDAO danhMucDAO, ThuongHieuDAO thuongHieuDAO,
                             LichSuGiaDAO lichSuGiaDAO, TaiKhoanBUS taiKhoanBUS) {
        this.truyVan = truyVan;
        this.danhMucDAO = danhMucDAO;
        this.thuongHieuDAO = thuongHieuDAO;
        this.lichSuGiaDAO = lichSuGiaDAO;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    @Transactional(readOnly = true)
    public List<SanPham> loc(String maDanhMuc, Long maThuongHieu, String trangThai, String tuKhoa) {
        TrangThaiSanPham tt = null;
        if (trangThai != null && !trangThai.isBlank()) {
            try {
                tt = TrangThaiSanPham.valueOf(trangThai);
            } catch (IllegalArgumentException e) {
                tt = null;
            }
        }
        String dm = maDanhMuc == null || maDanhMuc.isBlank() ? null : maDanhMuc;
        String tk = tuKhoa == null || tuKhoa.isBlank() ? null : "%" + tuKhoa.trim().toLowerCase(Locale.ROOT) + "%";
        return truyVan.loc(dm, maThuongHieu, tt, tk);
    }

    @Transactional(readOnly = true)
    public SanPham tim(Long ma) {
        return truyVan.timDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy mẫu máy."));
    }

    @Transactional(readOnly = true)
    public BieuMauSanPham bieuMauSua(Long ma) {
        SanPham sp = tim(ma);
        BieuMauSanPham bm = new BieuMauSanPham();
        bm.setMaSanPham(sp.getMaSanPham());
        bm.setTenSanPham(sp.getTenSanPham());
        bm.setModel(sp.getModel());
        bm.setMaDanhMuc(sp.getDanhMuc().getMaDanhMuc());
        bm.setMaThuongHieu(sp.getThuongHieu().getMaThuongHieu());
        bm.setThoiHanBaoHanh(sp.getThoiHanBaoHanh());
        bm.setCanLapDat(Boolean.TRUE.equals(sp.getCanLapDat()));
        bm.setMoTa(sp.getMoTa());
        bm.setAnh(sp.getAnh());
        List<BieuMauSanPham.DongThongSo> ds = new ArrayList<>();
        for (ThongSoKyThuat ts : sp.getThongSo()) {
            ds.add(new BieuMauSanPham.DongThongSo(ts.getTenThongSo(), ts.getGiaTri()));
        }
        bm.setThongSo(ds);
        return bm;
    }

    @Transactional(readOnly = true)
    public Map<String, List<String>> thongSoGoiY() {
        Map<String, List<String>> m = new LinkedHashMap<>();
        for (Object[] r : truyVan.thongSoThuongGapTheoDanhMuc()) {
            List<String> ds = m.computeIfAbsent((String) r[0], k -> new ArrayList<>());
            if (ds.size() < 8) {
                ds.add((String) r[1]);
            }
        }
        return m;
    }

    @Transactional
    public SanPham luu(BieuMauSanPham bm) {
        boolean themMoi = bm.getMaSanPham() == null;
        SanPham sp = themMoi ? new SanPham() : tim(bm.getMaSanPham());
        Map<String, String> loi = new LinkedHashMap<>();
        String ten = cat(bm.getTenSanPham());
        String model = cat(bm.getModel());
        if (ten.isEmpty()) {
            loi.put("tenSanPham", "Nhập tên mẫu máy.");
        } else if (ten.length() > 200) {
            loi.put("tenSanPham", "Tên tối đa 200 ký tự.");
        }
        if (model.isEmpty()) {
            loi.put("model", "Nhập model.");
        } else if (model.length() > 60) {
            loi.put("model", "Model tối đa 60 ký tự.");
        } else {
            Optional<SanPham> trung = truyVan.findFirstByModelIgnoreCase(model);
            if (trung.isPresent() && !trung.get().getMaSanPham().equals(bm.getMaSanPham())) {
                loi.put("model", "Model này đã có.");
                loi.put("modelTrung", String.valueOf(trung.get().getMaSanPham()));
            }
        }
        DanhMuc dm = bm.getMaDanhMuc() == null ? null : danhMucDAO.findById(bm.getMaDanhMuc()).orElse(null);
        if (dm == null) {
            loi.put("maDanhMuc", "Chọn danh mục.");
        }
        ThuongHieu th = bm.getMaThuongHieu() == null ? null : thuongHieuDAO.findById(bm.getMaThuongHieu()).orElse(null);
        if (th == null) {
            loi.put("maThuongHieu", "Chọn thương hiệu.");
        }
        if (bm.getThoiHanBaoHanh() == null || bm.getThoiHanBaoHanh() < 0 || bm.getThoiHanBaoHanh() > 120) {
            loi.put("thoiHanBaoHanh", "Thời hạn bảo hành là số tháng từ 0 đến 120.");
        }
        String anh = cat(bm.getAnh());
        if (anh.length() > 255) {
            loi.put("anh", "Đường dẫn ảnh tối đa 255 ký tự.");
        } else if (!anh.isEmpty() && !anh.matches("(?i)^(/|https?://)\\S+\\.(jpg|jpeg|png|webp)$")) {
            loi.put("anh", "Đường dẫn ảnh phải bắt đầu bằng / hoặc http và kết thúc bằng .jpg, .jpeg, .png, .webp.");
        }
        List<BieuMauSanPham.DongThongSo> thongSo = new ArrayList<>();
        for (BieuMauSanPham.DongThongSo d : bm.getThongSo()) {
            if (d == null) {
                continue;
            }
            String t = cat(d.getTen());
            String g = cat(d.getGiaTri());
            if (t.isEmpty() && g.isEmpty()) {
                continue;
            }
            if (t.isEmpty() || g.isEmpty()) {
                loi.put("thongSo", "Mỗi dòng thông số phải có cả tên và giá trị.");
            } else if (t.length() > 80 || g.length() > 120) {
                loi.put("thongSo", "Tên thông số tối đa 80 ký tự, giá trị tối đa 120 ký tự.");
            } else if ((t.contains("(W)") || t.contains("(lít)") || t.contains("(mm)") || t.contains("(dB)")) && !g.matches("[0-9 .,/x×–+-]+")) {
                loi.put("thongSo", "Thông số \"" + t + "\" là số đo, chỉ nhập số.");
            }
            thongSo.add(new BieuMauSanPham.DongThongSo(t, g));
        }
        if (dm != null && dm.getMaDanhMuc().startsWith("bep-") && !loi.containsKey("thongSo")) {
            boolean lapAm = thongSo.stream().anyMatch(d -> d.getTen().toLowerCase(Locale.ROOT).contains("kiểu lắp")
                    && d.getGiaTri().toLowerCase(Locale.ROOT).contains("âm"));
            boolean coKhoet = thongSo.stream().anyMatch(d -> d.getTen().toLowerCase(Locale.ROOT).contains("khoét đá"));
            if (lapAm && !coKhoet) {
                loi.put("thongSo", "Cần nhập kích thước khoét đá.");
            }
        }
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        sp.setTenSanPham(ten);
        sp.setModel(model);
        sp.setDanhMuc(dm);
        sp.setThuongHieu(th);
        sp.setThoiHanBaoHanh(bm.getThoiHanBaoHanh());
        sp.setCanLapDat(bm.isCanLapDat());
        String moTa = cat(bm.getMoTa());
        sp.setMoTa(moTa.isEmpty() ? null : moTa);
        sp.setAnh(anh.isEmpty() ? null : anh);
        if (themMoi) {
            sp.setTrangThai(TrangThaiSanPham.CHUA_MO_BAN);
            sp.setNgayTao(LocalDateTime.now());
        }
        sp.getThongSo().clear();
        for (BieuMauSanPham.DongThongSo d : thongSo) {
            ThongSoKyThuat ts = new ThongSoKyThuat();
            ts.setSanPham(sp);
            ts.setTenThongSo(d.getTen());
            ts.setGiaTri(d.getGiaTri());
            sp.getThongSo().add(ts);
        }
        return truyVan.save(sp);
    }

    @Transactional
    public String ngungBan(Long ma) {
        SanPham sp = tim(ma);
        if (sp.getTrangThai() == TrangThaiSanPham.NGUNG_BAN) {
            throw new LoiNghiepVu("Mẫu máy này đã ngừng bán.");
        }
        sp.setTrangThai(TrangThaiSanPham.NGUNG_BAN);
        truyVan.save(sp);
        return sp.getTenSanPham();
    }

    @Transactional
    public String moBan(Long ma) {
        SanPham sp = tim(ma);
        if (sp.getTrangThai() == TrangThaiSanPham.DANG_BAN) {
            throw new LoiNghiepVu("Mẫu máy này đang bán.");
        }
        if (sp.getGiaBan() == null) {
            throw new LoiNghiepVu("Chưa có giá bán: đặt giá cho \"" + sp.getTenSanPham() + "\" trước khi mở bán.");
        }
        sp.setTrangThai(TrangThaiSanPham.DANG_BAN);
        truyVan.save(sp);
        return sp.getTenSanPham();
    }

    @Transactional(readOnly = true)
    public Long giaNhapGanNhat(Long maSanPham) {
        List<Long> ds = truyVan.giaNhapMoiNhat(maSanPham, PageRequest.of(0, 1));
        return ds.isEmpty() ? null : ds.get(0);
    }

    @Transactional(readOnly = true)
    public List<LichSuGia> lichSuGia(Long maSanPham) {
        return truyVan.lichSuGia(maSanPham);
    }

    @Transactional
    public LichSuGia capNhatGia(Long maSanPham, Long giaMoi, Long giaNiemYet, String lyDo) {
        if (!taiKhoanBUS.coVai("QUAN_LY")) {
            throw new LoiNghiepVu("Chỉ Quản lý cửa hàng được sửa giá.");
        }
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        SanPham sp = tim(maSanPham);
        String ly = cat(lyDo);
        Map<String, String> loi = new LinkedHashMap<>();
        if (giaMoi == null || giaMoi <= 0) {
            loi.put("giaMoi", "Giá bán mới phải là số tiền lớn hơn 0.");
        }
        if (giaNiemYet != null && giaNiemYet <= 0) {
            loi.put("giaNiemYet", "Giá niêm yết phải lớn hơn 0 hoặc để trống.");
        } else if (giaNiemYet != null && giaMoi != null && giaNiemYet < giaMoi) {
            loi.put("giaNiemYet", "Giá niêm yết (giá gạch ngang) không được thấp hơn giá bán.");
        }
        if (ly.length() > 255) {
            loi.put("lyDo", "Lý do tối đa 255 ký tự.");
        }
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        if (giaMoi.equals(sp.getGiaBan()) && java.util.Objects.equals(giaNiemYet, sp.getGiaNiemYet())) {
            throw new LoiNghiepVu("Giá mới trùng với giá hiện tại, không có gì để cập nhật.");
        }
        Long giaNhap = giaNhapGanNhat(maSanPham);
        boolean duoiGiaNhap = giaNhap != null && giaMoi < giaNhap;
        if (duoiGiaNhap && ly.isEmpty()) {
            throw new LoiNhapLieu(Map.of("lyDo", "Giá mới " + tien(giaMoi) + " thấp hơn giá nhập gần nhất " + tien(giaNhap)
                    + ". Ghi lý do để lưu, lý do được lưu vết cho chủ cửa hàng xem lại."));
        }
        LichSuGia ls = new LichSuGia();
        ls.setSanPham(sp);
        ls.setGiaCu(sp.getGiaBan());
        ls.setGiaMoi(giaMoi);
        ls.setLyDo(ly.isEmpty() ? null : ly);
        ls.setDuoiGiaNhap(duoiGiaNhap);
        ls.setThoiDiem(LocalDateTime.now());
        ls.setNhanVien(nv);
        lichSuGiaDAO.save(ls);
        sp.setGiaBan(giaMoi);
        sp.setGiaNiemYet(giaNiemYet);
        truyVan.save(sp);
        return ls;
    }

    @Transactional(readOnly = true)
    public List<SanPham> sanPhamChoKhuyenMai() {
        return truyVan.layTruTrangThai(TrangThaiSanPham.NGUNG_BAN);
    }

    static String tien(Long x) {
        DecimalFormatSymbols k = new DecimalFormatSymbols(Locale.ROOT);
        k.setGroupingSeparator('.');
        return new DecimalFormat("#,##0", k).format(x) + " ₫";
    }

    private static String cat(String s) {
        return s == null ? "" : s.trim();
    }
}
