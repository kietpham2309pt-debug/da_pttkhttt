package vn.edu.huit.pttk.diengiadung.nghiepvu.g2;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DanhMucDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.ThuongHieuDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.DanhMucTruyVanG12;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.SanPhamTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.LoiNhapLieu;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhMuc;
import vn.edu.huit.pttk.diengiadung.thucthe.ThuongHieu;

@Service
public class QuanTriDanhMucBUS {

    private final DanhMucDAO danhMucDAO;
    private final ThuongHieuDAO thuongHieuDAO;
    private final DanhMucTruyVanG12 truyVan;
    private final SanPhamTruyVanG12 sanPhamTruyVan;

    public QuanTriDanhMucBUS(DanhMucDAO danhMucDAO, ThuongHieuDAO thuongHieuDAO, DanhMucTruyVanG12 truyVan,
                             SanPhamTruyVanG12 sanPhamTruyVan) {
        this.danhMucDAO = danhMucDAO;
        this.thuongHieuDAO = thuongHieuDAO;
        this.truyVan = truyVan;
        this.sanPhamTruyVan = sanPhamTruyVan;
    }

    @Transactional(readOnly = true)
    public List<DanhMuc> danhMuc() {
        return truyVan.layTheoThuTu();
    }

    @Transactional(readOnly = true)
    public List<ThuongHieu> thuongHieu() {
        return truyVan.layThuongHieu();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> soSanPhamTheoDanhMuc() {
        Map<String, Long> m = new HashMap<>();
        for (Object[] r : truyVan.demSanPhamTheoDanhMuc()) {
            m.put((String) r[0], ((Number) r[1]).longValue());
        }
        return m;
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> soSanPhamTheoThuongHieu() {
        Map<Long, Long> m = new HashMap<>();
        for (Object[] r : truyVan.demSanPhamTheoThuongHieu()) {
            m.put((Long) r[0], ((Number) r[1]).longValue());
        }
        return m;
    }

    @Transactional(readOnly = true)
    public DanhMuc timDanhMuc(String ma) {
        return danhMucDAO.findById(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy danh mục."));
    }

    @Transactional(readOnly = true)
    public ThuongHieu timThuongHieu(Long ma) {
        return thuongHieuDAO.findById(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy thương hiệu."));
    }

    @Transactional
    public void luuDanhMuc(String ma, String ten, Integer thuTu, boolean themMoi) {
        String m = ma == null ? "" : ma.trim();
        String t = ten == null ? "" : ten.trim();
        Map<String, String> loi = new LinkedHashMap<>();
        if (themMoi) {
            if (m.isEmpty()) {
                loi.put("maDanhMuc", "Nhập mã danh mục.");
            } else if (!m.matches("[a-z0-9]+(-[a-z0-9]+)*") || m.length() > 30) {
                loi.put("maDanhMuc", "Mã chỉ gồm chữ thường không dấu, số, nối bằng dấu gạch ngang (tối đa 30 ký tự).");
            } else if (danhMucDAO.existsById(m)) {
                loi.put("maDanhMuc", "Mã danh mục đã tồn tại.");
            }
        }
        if (t.isEmpty()) {
            loi.put("tenDanhMuc", "Nhập tên danh mục.");
        } else if (t.length() > 100) {
            loi.put("tenDanhMuc", "Tên tối đa 100 ký tự.");
        } else if (truyVan.demTrungTenDanhMuc(t, themMoi ? "" : m) > 0) {
            loi.put("tenDanhMuc", "Tên danh mục đã có.");
        }
        if (thuTu == null || thuTu < 0) {
            loi.put("thuTu", "Thứ tự là số nguyên từ 0 trở lên.");
        }
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        DanhMuc dm = themMoi ? new DanhMuc() : timDanhMuc(m);
        if (themMoi) {
            dm.setMaDanhMuc(m);
        }
        dm.setTenDanhMuc(t);
        dm.setThuTu(thuTu);
        danhMucDAO.save(dm);
    }

    @Transactional
    public String xoaDanhMuc(String ma) {
        DanhMuc dm = timDanhMuc(ma);
        long n = sanPhamTruyVan.countByDanhMuc_MaDanhMuc(ma);
        if (n > 0) {
            throw new LoiNghiepVu("Không xoá được danh mục \"" + dm.getTenDanhMuc() + "\" vì còn " + n + " sản phẩm.");
        }
        danhMucDAO.delete(dm);
        return dm.getTenDanhMuc();
    }

    @Transactional
    public void luuThuongHieu(Long ma, String ten, String xuatXu) {
        String t = ten == null ? "" : ten.trim();
        String x = xuatXu == null ? "" : xuatXu.trim();
        Map<String, String> loi = new LinkedHashMap<>();
        if (t.isEmpty()) {
            loi.put("tenThuongHieu", "Nhập tên thương hiệu.");
        } else if (t.length() > 60) {
            loi.put("tenThuongHieu", "Tên tối đa 60 ký tự.");
        } else if (truyVan.demTrungTenThuongHieu(t, ma == null ? -1L : ma) > 0) {
            loi.put("tenThuongHieu", "Tên thương hiệu đã có.");
        }
        if (x.length() > 60) {
            loi.put("xuatXu", "Xuất xứ tối đa 60 ký tự.");
        }
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        ThuongHieu th = ma == null ? new ThuongHieu() : timThuongHieu(ma);
        th.setTenThuongHieu(t);
        th.setXuatXu(x.isEmpty() ? null : x);
        thuongHieuDAO.save(th);
    }

    @Transactional
    public String xoaThuongHieu(Long ma) {
        ThuongHieu th = timThuongHieu(ma);
        long n = sanPhamTruyVan.countByThuongHieu_MaThuongHieu(ma);
        if (n > 0) {
            throw new LoiNghiepVu("Không xoá được thương hiệu \"" + th.getTenThuongHieu() + "\" vì còn " + n + " sản phẩm.");
        }
        thuongHieuDAO.delete(th);
        return th.getTenThuongHieu();
    }
}
