package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DanhMucDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.ThuongHieuDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhachHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.SanPhamTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhGia;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPham;
import vn.edu.huit.pttk.diengiadung.thucthe.ThongSoKyThuat;
import vn.edu.huit.pttk.diengiadung.thucthe.ThuongHieu;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSanPham;

@Service
public class SanPhamKhachBUS {

    public static final int SO_MOI_TRANG = 12;

    private final SanPhamTruyVanKhach sanPhamTV;
    private final KhachHangTruyVanKhach khachTV;
    private final DanhMucDAO danhMucDAO;
    private final ThuongHieuDAO thuongHieuDAO;
    private final GiaBUS giaBUS;

    public SanPhamKhachBUS(SanPhamTruyVanKhach sanPhamTV, KhachHangTruyVanKhach khachTV, DanhMucDAO danhMucDAO,
                           ThuongHieuDAO thuongHieuDAO, GiaBUS giaBUS) {
        this.sanPhamTV = sanPhamTV;
        this.khachTV = khachTV;
        this.danhMucDAO = danhMucDAO;
        this.thuongHieuDAO = thuongHieuDAO;
        this.giaBUS = giaBUS;
    }

    @Transactional(readOnly = true)
    public List<DanhMuc> danhMuc() {
        return danhMucDAO.findAll(Sort.by("thuTu"));
    }

    @Transactional(readOnly = true)
    public List<ThuongHieu> thuongHieu() {
        return thuongHieuDAO.findAll(Sort.by("tenThuongHieu"));
    }

    @Transactional(readOnly = true)
    public List<TheSanPham> moiNhat(int soLuong) {
        return giaBUS.dongThe(sanPhamTV.moiNhat(TrangThaiSanPham.DANG_BAN, PageRequest.of(0, soLuong)));
    }

    @Transactional(readOnly = true)
    public List<TheSanPham> dangKhuyenMai(int soLuong) {
        List<TheSanPham> tatCa = giaBUS.dongThe(sanPhamTV.moiNhat(TrangThaiSanPham.DANG_BAN, PageRequest.of(0, 500)));
        return tatCa.stream().filter(t -> t.getGiamMoiChiec() > 0)
                .sorted(Comparator.comparingInt(TheSanPham::getPhanTramGiam).reversed())
                .limit(soLuong).toList();
    }

    @Transactional(readOnly = true)
    public Page<TheSanPham> timKiem(LocSanPham loc) {
        loc.chuanHoa();
        Specification<SanPham> dieuKien = (r, q, cb) -> {
            List<Predicate> p = new ArrayList<>();
            p.add(cb.equal(r.get("trangThai"), TrangThaiSanPham.DANG_BAN));
            p.add(cb.isNotNull(r.get("giaBan")));
            if (loc.getDanhMuc() != null) {
                p.add(cb.equal(r.get("danhMuc").get("maDanhMuc"), loc.getDanhMuc()));
            }
            if (!loc.getThuongHieu().isEmpty()) {
                p.add(r.get("thuongHieu").get("maThuongHieu").in(loc.getThuongHieu()));
            }
            if (loc.getGiaTu() != null) {
                p.add(cb.greaterThanOrEqualTo(r.get("giaBan"), loc.getGiaTu()));
            }
            if (loc.getGiaDen() != null) {
                p.add(cb.lessThanOrEqualTo(r.get("giaBan"), loc.getGiaDen()));
            }
            if (loc.getTuKhoa() != null) {
                String mau = "%" + loc.getTuKhoa().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
                p.add(cb.or(cb.like(r.get("tenSanPham"), mau, '\\'), cb.like(r.get("model"), mau, '\\')));
            }
            return cb.and(p.toArray(new Predicate[0]));
        };
        Sort sapXep = switch (loc.getSapXep()) {
            case "gia-tang" -> Sort.by(Sort.Order.asc("giaBan"), Sort.Order.asc("maSanPham"));
            case "gia-giam" -> Sort.by(Sort.Order.desc("giaBan"), Sort.Order.asc("maSanPham"));
            case "ten" -> Sort.by(Sort.Order.asc("tenSanPham"));
            default -> Sort.by(Sort.Order.desc("ngayTao"), Sort.Order.desc("maSanPham"));
        };
        Page<SanPham> trang = sanPhamTV.findAll(dieuKien, PageRequest.of(loc.getTrang() - 1, SO_MOI_TRANG, sapXep));
        if (trang.getContent().isEmpty() && loc.getTrang() > 1 && trang.getTotalPages() > 0) {
            loc.setTrang(trang.getTotalPages());
            trang = sanPhamTV.findAll(dieuKien, PageRequest.of(loc.getTrang() - 1, SO_MOI_TRANG, sapXep));
        }
        return new PageImpl<>(giaBUS.dongThe(trang.getContent()), trang.getPageable(), trang.getTotalElements());
    }

    @Transactional(readOnly = true)
    public TheSanPham layThe(Long ma) {
        List<SanPham> ds = sanPhamTV.theoMa(List.of(ma));
        if (ds.isEmpty()) {
            return null;
        }
        return giaBUS.dongThe(ds).get(0);
    }

    @Transactional(readOnly = true)
    public ChiTietSanPham chiTiet(Long ma) {
        List<SanPham> ds = sanPhamTV.theoMa(List.of(ma));
        if (ds.isEmpty() || ds.get(0).getTrangThai() == TrangThaiSanPham.CHUA_MO_BAN) {
            return null;
        }
        SanPham sp = ds.get(0);
        TheSanPham the = giaBUS.dongThe(ds).get(0);
        List<ThongSoKyThuat> thongSo = new ArrayList<>(sp.getThongSo());
        List<DanhGia> danhGia = khachTV.danhGiaCuaSanPham(ma);
        List<TheSanPham> cungNhom = giaBUS.dongThe(sanPhamTV.cungNhom(sp.getDanhMuc().getMaDanhMuc(), ma, TrangThaiSanPham.DANG_BAN, PageRequest.of(0, 4)));
        return new ChiTietSanPham(the, dsAnh(sp), thongSo, danhGia, cungNhom);
    }

    @Transactional(readOnly = true)
    public List<TheSanPham> soSanh(List<Long> dsMa) {
        List<Long> ma = new ArrayList<>(new LinkedHashSet<>(dsMa));
        if (ma.size() > 3) {
            ma = ma.subList(0, 3);
        }
        Map<Long, SanPham> theoMa = new LinkedHashMap<>();
        for (SanPham sp : sanPhamTV.theoMa(ma)) {
            theoMa.put(sp.getMaSanPham(), sp);
        }
        List<SanPham> coThuTu = new ArrayList<>();
        for (Long m : ma) {
            SanPham sp = theoMa.get(m);
            if (sp != null && sp.getTrangThai() != TrangThaiSanPham.CHUA_MO_BAN) {
                sp.getThongSo().size();
                coThuTu.add(sp);
            }
        }
        return giaBUS.dongThe(coThuTu);
    }

    public static List<String> tenThongSoChung(List<TheSanPham> ds) {
        LinkedHashSet<String> ten = new LinkedHashSet<>();
        for (TheSanPham t : ds) {
            for (ThongSoKyThuat ts : t.getSanPham().getThongSo()) {
                ten.add(ts.getTenThongSo());
            }
        }
        return new ArrayList<>(ten);
    }

    public static String giaTriThongSo(TheSanPham t, String ten) {
        for (ThongSoKyThuat ts : t.getSanPham().getThongSo()) {
            if (ts.getTenThongSo().equals(ten)) {
                return ts.getGiaTri();
            }
        }
        return "—";
    }

    private List<String> dsAnh(SanPham sp) {
        List<String> ds = new ArrayList<>();
        String chinh = GiaBUS.anhCua(sp);
        ds.add(chinh);
        if (chinh.matches(".*/1\\.jpg$")) {
            String goc = chinh.substring(0, chinh.length() - "1.jpg".length());
            for (int i = 2; i <= 6; i++) {
                String duongDan = goc + i + ".jpg";
                if (new ClassPathResource("static" + duongDan).exists()) {
                    ds.add(duongDan);
                }
            }
        }
        return ds;
    }
}
