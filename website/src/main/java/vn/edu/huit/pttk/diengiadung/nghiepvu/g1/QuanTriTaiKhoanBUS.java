package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.NhanVienDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.TaiKhoanDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.VaiTroDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.g12.NhanVienTruyVanG12;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan;
import vn.edu.huit.pttk.diengiadung.thucthe.VaiTro;

@Service
public class QuanTriTaiKhoanBUS {

    public static final List<String> VAI_NHAN_VIEN = List.of("NV_BAN_HANG", "THU_KHO", "NV_GIAO_HANG", "QUAN_LY", "KE_TOAN", "QUAN_TRI");
    private static final String KY_TU_MAT_KHAU = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final SecureRandom NGAU_NHIEN = new SecureRandom();

    private final NhanVienTruyVanG12 nhanVienTruyVan;
    private final NhanVienDAO nhanVienDAO;
    private final TaiKhoanDAO taiKhoanDAO;
    private final VaiTroDAO vaiTroDAO;
    private final TaiKhoanBUS taiKhoanBUS;
    private final PasswordEncoder maHoa;

    public QuanTriTaiKhoanBUS(NhanVienTruyVanG12 nhanVienTruyVan, NhanVienDAO nhanVienDAO, TaiKhoanDAO taiKhoanDAO,
                              VaiTroDAO vaiTroDAO, TaiKhoanBUS taiKhoanBUS, PasswordEncoder maHoa) {
        this.nhanVienTruyVan = nhanVienTruyVan;
        this.nhanVienDAO = nhanVienDAO;
        this.taiKhoanDAO = taiKhoanDAO;
        this.vaiTroDAO = vaiTroDAO;
        this.taiKhoanBUS = taiKhoanBUS;
        this.maHoa = maHoa;
    }

    @Transactional(readOnly = true)
    public List<NhanVien> danhSach(String tuKhoa) {
        List<NhanVien> ds = nhanVienTruyVan.layTatCaKemTaiKhoan();
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return ds;
        }
        String k = tuKhoa.trim().toLowerCase(Locale.ROOT);
        return ds.stream().filter(nv -> nv.getHoTen().toLowerCase(Locale.ROOT).contains(k)
                || nv.getTaiKhoan().getTenDangNhap().toLowerCase(Locale.ROOT).contains(k)
                || nv.getSoDienThoai().contains(k)).toList();
    }

    @Transactional(readOnly = true)
    public List<VaiTro> vaiChoNhanVien() {
        return VAI_NHAN_VIEN.stream().map(ma -> vaiTroDAO.findById(ma).orElseThrow()).toList();
    }

    @Transactional(readOnly = true)
    public BieuMauNhanVien bieuMauSua(Long maNhanVien) {
        NhanVien nv = timNhanVien(maNhanVien);
        BieuMauNhanVien bm = new BieuMauNhanVien();
        bm.setMaNhanVien(nv.getMaNhanVien());
        bm.setHoTen(nv.getHoTen());
        bm.setSoDienThoai(nv.getSoDienThoai());
        bm.setTenDangNhap(nv.getTaiKhoan().getTenDangNhap());
        bm.setVaiTro(nv.getTaiKhoan().getVaiTro().stream().map(VaiTro::getMaVaiTro).toList());
        return bm;
    }

    @Transactional
    public String themNhanVien(BieuMauNhanVien bm) {
        Map<String, String> loi = kiemTra(bm, true);
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        String matKhauTam = sinhMatKhauTam();
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(bm.getTenDangNhap().trim());
        tk.setMatKhau(maHoa.encode(matKhauTam));
        tk.setTrangThai(TrangThaiTaiKhoan.DANG_DUNG);
        tk.setNgayTao(LocalDateTime.now());
        tk.setSoLanSaiLienTiep(0);
        tk.setPhaiDoiMatKhau(true);
        tk.setVaiTro(layVai(bm.getVaiTro()));
        taiKhoanDAO.save(tk);
        NhanVien nv = new NhanVien();
        nv.setHoTen(bm.getHoTen().trim());
        nv.setSoDienThoai(bm.getSoDienThoai().trim());
        nv.setTaiKhoan(tk);
        nhanVienDAO.save(nv);
        return matKhauTam;
    }

    @Transactional
    public void suaNhanVien(BieuMauNhanVien bm) {
        NhanVien nv = timNhanVien(bm.getMaNhanVien());
        Map<String, String> loi = kiemTra(bm, false);
        if (!loi.isEmpty()) {
            throw new LoiNhapLieu(loi);
        }
        TaiKhoan tk = nv.getTaiKhoan();
        boolean dangLaQuanTri = tk.getVaiTro().stream().anyMatch(v -> v.getMaVaiTro().equals("QUAN_TRI"));
        if (dangLaQuanTri && !bm.getVaiTro().contains("QUAN_TRI") && tk.getTrangThai() == TrangThaiTaiKhoan.DANG_DUNG
                && nhanVienTruyVan.demQuanTriKhac(TrangThaiTaiKhoan.DANG_DUNG, tk.getMaTaiKhoan()) == 0) {
            throw new LoiNghiepVu("Phải còn ít nhất một tài khoản Quản trị hệ thống đang dùng, không bỏ được vai Quản trị hệ thống của tài khoản này.");
        }
        nv.setHoTen(bm.getHoTen().trim());
        nv.setSoDienThoai(bm.getSoDienThoai().trim());
        tk.setVaiTro(layVai(bm.getVaiTro()));
        taiKhoanDAO.save(tk);
        nhanVienDAO.save(nv);
    }

    @Transactional
    public String khoa(Long maNhanVien) {
        NhanVien nv = timNhanVien(maNhanVien);
        TaiKhoan tk = nv.getTaiKhoan();
        if (tk.getTrangThai() == TrangThaiTaiKhoan.DA_KHOA) {
            throw new LoiNghiepVu("Tài khoản " + tk.getTenDangNhap() + " đã ở trạng thái Đã khoá.");
        }
        String toi = taiKhoanBUS.tenDangNhapHienTai().orElse("");
        if (tk.getTenDangNhap().equals(toi)) {
            throw new LoiNghiepVu("Không được tự khoá tài khoản đang dùng của chính mình.");
        }
        boolean laQuanTri = tk.getVaiTro().stream().anyMatch(v -> v.getMaVaiTro().equals("QUAN_TRI"));
        if (laQuanTri && nhanVienTruyVan.demQuanTriKhac(TrangThaiTaiKhoan.DANG_DUNG, tk.getMaTaiKhoan()) == 0) {
            throw new LoiNghiepVu("Phải còn ít nhất một tài khoản Quản trị hệ thống đang dùng.");
        }
        tk.setTrangThai(TrangThaiTaiKhoan.DA_KHOA);
        taiKhoanDAO.save(tk);
        return tk.getTenDangNhap();
    }

    @Transactional
    public String moKhoa(Long maNhanVien) {
        NhanVien nv = timNhanVien(maNhanVien);
        TaiKhoan tk = nv.getTaiKhoan();
        if (tk.getTrangThai() != TrangThaiTaiKhoan.DA_KHOA) {
            throw new LoiNghiepVu("Tài khoản " + tk.getTenDangNhap() + " đang dùng, không cần mở khoá.");
        }
        tk.setTrangThai(TrangThaiTaiKhoan.DANG_DUNG);
        tk.setSoLanSaiLienTiep(0);
        tk.setKhoaDenLuc(null);
        taiKhoanDAO.save(tk);
        return tk.getTenDangNhap();
    }

    @Transactional
    public String[] datLaiMatKhau(Long maNhanVien) {
        NhanVien nv = timNhanVien(maNhanVien);
        TaiKhoan tk = nv.getTaiKhoan();
        String matKhauTam = sinhMatKhauTam();
        tk.setMatKhau(maHoa.encode(matKhauTam));
        tk.setPhaiDoiMatKhau(true);
        tk.setSoLanSaiLienTiep(0);
        tk.setKhoaDenLuc(null);
        taiKhoanDAO.save(tk);
        return new String[]{tk.getTenDangNhap(), matKhauTam};
    }

    @Transactional(readOnly = true)
    public boolean dangBiKhoa(String tenDangNhap) {
        return nhanVienTruyVan.trangThaiTheoTen(tenDangNhap).map(t -> t == TrangThaiTaiKhoan.DA_KHOA).orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean phaiDoiMatKhau(String tenDangNhap) {
        return nhanVienTruyVan.phaiDoiMatKhauTheoTen(tenDangNhap).map(Boolean.TRUE::equals).orElse(false);
    }

    private NhanVien timNhanVien(Long ma) {
        if (ma == null) {
            throw new LoiNghiepVu("Không tìm thấy nhân viên.");
        }
        return nhanVienTruyVan.timKemTaiKhoan(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy nhân viên."));
    }

    private Map<String, String> kiemTra(BieuMauNhanVien bm, boolean themMoi) {
        Map<String, String> loi = new LinkedHashMap<>();
        if (bm.getHoTen() == null || bm.getHoTen().isBlank()) {
            loi.put("hoTen", "Nhập họ tên.");
        } else if (bm.getHoTen().trim().length() > 100) {
            loi.put("hoTen", "Họ tên tối đa 100 ký tự.");
        }
        if (bm.getSoDienThoai() == null || bm.getSoDienThoai().isBlank()) {
            loi.put("soDienThoai", "Nhập số điện thoại.");
        } else if (!bm.getSoDienThoai().trim().matches("0\\d{9}")) {
            loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.");
        }
        if (themMoi) {
            String ten = bm.getTenDangNhap() == null ? "" : bm.getTenDangNhap().trim();
            if (ten.isEmpty()) {
                loi.put("tenDangNhap", "Nhập tên đăng nhập.");
            } else if (!ten.matches("[a-zA-Z0-9._]{3,50}")) {
                loi.put("tenDangNhap", "Tên đăng nhập 3–50 ký tự, chỉ gồm chữ không dấu, số, dấu chấm, gạch dưới.");
            } else if (taiKhoanDAO.existsByTenDangNhap(ten)) {
                loi.put("tenDangNhap", "Tên đăng nhập đã tồn tại.");
            }
        }
        if (bm.getVaiTro().isEmpty()) {
            loi.put("vaiTro", "Chọn ít nhất một vai.");
        } else if (!VAI_NHAN_VIEN.containsAll(bm.getVaiTro())) {
            loi.put("vaiTro", "Vai không hợp lệ.");
        }
        return loi;
    }

    private Set<VaiTro> layVai(List<String> ma) {
        Set<VaiTro> ds = new LinkedHashSet<>();
        for (String m : VAI_NHAN_VIEN) {
            if (ma.contains(m)) {
                ds.add(vaiTroDAO.findById(m).orElseThrow());
            }
        }
        return ds;
    }

    private String sinhMatKhauTam() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(KY_TU_MAT_KHAU.charAt(NGAU_NHIEN.nextInt(KY_TU_MAT_KHAU.length())));
        }
        return sb.toString();
    }
}
