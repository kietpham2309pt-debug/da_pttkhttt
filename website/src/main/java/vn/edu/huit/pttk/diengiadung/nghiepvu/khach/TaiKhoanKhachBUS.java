package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.DanhGiaDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.DiaChiGiaoHangDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.TaiKhoanDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.VaiTroDAO;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.DonHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.khodulieu.khach.KhachHangTruyVanKhach;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DanhGia;
import vn.edu.huit.pttk.diengiadung.thucthe.DiaChiGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.KhachHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TaiKhoan;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiTaiKhoan;
import vn.edu.huit.pttk.diengiadung.thucthe.VaiTro;

@Service
public class TaiKhoanKhachBUS {

    private final TaiKhoanDAO taiKhoanDAO;
    private final VaiTroDAO vaiTroDAO;
    private final KhachHangTruyVanKhach khachTV;
    private final DiaChiGiaoHangDAO diaChiDAO;
    private final DanhGiaDAO danhGiaDAO;
    private final DonHangTruyVanKhach donHangTV;
    private final PasswordEncoder maHoa;

    public TaiKhoanKhachBUS(TaiKhoanDAO taiKhoanDAO, VaiTroDAO vaiTroDAO, KhachHangTruyVanKhach khachTV, DiaChiGiaoHangDAO diaChiDAO,
                            DanhGiaDAO danhGiaDAO, DonHangTruyVanKhach donHangTV, PasswordEncoder maHoa) {
        this.taiKhoanDAO = taiKhoanDAO;
        this.vaiTroDAO = vaiTroDAO;
        this.khachTV = khachTV;
        this.diaChiDAO = diaChiDAO;
        this.danhGiaDAO = danhGiaDAO;
        this.donHangTV = donHangTV;
        this.maHoa = maHoa;
    }

    @Transactional
    public Map<String, String> dangKy(FormDangKy f) {
        Map<String, String> loi = f.kiemTra();
        if (!loi.containsKey("tenDangNhap") && taiKhoanDAO.findByTenDangNhap(f.getTenDangNhap().trim()).isPresent()) {
            loi.put("tenDangNhap", "Tên đăng nhập này đã có người dùng.");
        }
        if (!loi.containsKey("soDienThoai") && khachTV.existsBySoDienThoai(f.getSoDienThoai().trim())) {
            loi.put("soDienThoai", "Số điện thoại này đã đăng ký tài khoản.");
        }
        if (!loi.isEmpty()) {
            return loi;
        }
        VaiTro vai = vaiTroDAO.findById("KHACH_HANG").orElseThrow();
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(f.getTenDangNhap().trim());
        tk.setMatKhau(maHoa.encode(f.getMatKhau()));
        tk.setTrangThai(TrangThaiTaiKhoan.DANG_DUNG);
        tk.setNgayTao(LocalDateTime.now());
        tk.setSoLanSaiLienTiep(0);
        tk.setPhaiDoiMatKhau(false);
        tk.getVaiTro().add(vai);
        taiKhoanDAO.save(tk);
        KhachHang kh = new KhachHang();
        kh.setHoTen(f.getHoTen().trim());
        kh.setSoDienThoai(f.getSoDienThoai().trim());
        kh.setEmail(f.getEmail().trim());
        kh.setNgayDangKy(LocalDateTime.now());
        kh.setTaiKhoan(tk);
        khachTV.save(kh);
        return loi;
    }

    @Transactional
    public void capNhatHoSo(KhachHang kh, String hoTen, String soDienThoai, String email) {
        if (hoTen == null || hoTen.trim().length() < 2 || hoTen.trim().length() > 60) {
            throw new LoiNghiepVu("Họ tên cần từ 2 tới 60 ký tự.");
        }
        if (soDienThoai == null || !soDienThoai.trim().matches("^0\\d{9}$")) {
            throw new LoiNghiepVu("Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        if (email != null && !email.isBlank() && !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new LoiNghiepVu("Email không đúng dạng, ví dụ đúng: Huit@edu.com.vn");
        }
        if (khachTV.existsBySoDienThoaiAndMaKhachHangNot(soDienThoai.trim(), kh.getMaKhachHang())) {
            throw new LoiNghiepVu("Số điện thoại này đã dùng cho tài khoản khác.");
        }
        KhachHang k = khachTV.findById(kh.getMaKhachHang()).orElseThrow();
        k.setHoTen(hoTen.trim());
        k.setSoDienThoai(soDienThoai.trim());
        k.setEmail(email == null || email.isBlank() ? null : email.trim());
        khachTV.save(k);
    }

    @Transactional(readOnly = true)
    public List<DiaChiGiaoHang> soDiaChi(KhachHang kh) {
        return khachTV.soDiaChi(kh.getMaKhachHang());
    }

    @Transactional(readOnly = true)
    public DiaChiGiaoHang diaChiCuaKhach(KhachHang kh, Long maDiaChi) {
        DiaChiGiaoHang dc = diaChiDAO.findById(maDiaChi).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy địa chỉ."));
        if (!dc.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang())) {
            throw new LoiNghiepVu("Không tìm thấy địa chỉ.");
        }
        return dc;
    }

    @Transactional
    public void luuDiaChi(KhachHang kh, Long maDiaChi, String hoTen, String soDienThoai, String diaChi, Boolean noiThanh, boolean macDinh) {
        if (hoTen == null || hoTen.trim().length() < 2 || hoTen.trim().length() > 60) {
            throw new LoiNghiepVu("Họ tên người nhận cần từ 2 tới 60 ký tự.");
        }
        if (soDienThoai == null || !soDienThoai.trim().matches("^0\\d{9}$")) {
            throw new LoiNghiepVu("Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0.");
        }
        if (diaChi == null || diaChi.isBlank() || diaChi.trim().length() > 255) {
            throw new LoiNghiepVu("Địa chỉ không được để trống và không dài quá 255 ký tự.");
        }
        DiaChiGiaoHang dc;
        if (maDiaChi == null) {
            dc = new DiaChiGiaoHang();
            dc.setKhachHang(khachTV.findById(kh.getMaKhachHang()).orElseThrow());
        } else {
            dc = diaChiCuaKhach(kh, maDiaChi);
        }
        List<DiaChiGiaoHang> tatCa = khachTV.soDiaChi(kh.getMaKhachHang());
        boolean laMacDinh = macDinh || tatCa.isEmpty() || (maDiaChi != null && Boolean.TRUE.equals(dc.getMacDinh()));
        dc.setHoTenNguoiNhan(hoTen.trim());
        dc.setSoDienThoai(soDienThoai.trim());
        dc.setDiaChi(diaChi.trim());
        dc.setNoiThanh(!Boolean.FALSE.equals(noiThanh));
        dc.setMacDinh(laMacDinh);
        if (laMacDinh) {
            for (DiaChiGiaoHang khac : tatCa) {
                if (!khac.getMaDiaChi().equals(dc.getMaDiaChi()) && Boolean.TRUE.equals(khac.getMacDinh())) {
                    khac.setMacDinh(false);
                    diaChiDAO.save(khac);
                }
            }
        }
        diaChiDAO.save(dc);
    }

    @Transactional
    public void datMacDinh(KhachHang kh, Long maDiaChi) {
        DiaChiGiaoHang chon = diaChiCuaKhach(kh, maDiaChi);
        for (DiaChiGiaoHang dc : khachTV.soDiaChi(kh.getMaKhachHang())) {
            dc.setMacDinh(dc.getMaDiaChi().equals(chon.getMaDiaChi()));
            diaChiDAO.save(dc);
        }
    }

    @Transactional
    public void xoaDiaChi(KhachHang kh, Long maDiaChi) {
        DiaChiGiaoHang dc = diaChiCuaKhach(kh, maDiaChi);
        boolean laMacDinh = Boolean.TRUE.equals(dc.getMacDinh());
        diaChiDAO.delete(dc);
        diaChiDAO.flush();
        if (laMacDinh) {
            List<DiaChiGiaoHang> conLai = khachTV.soDiaChi(kh.getMaKhachHang());
            if (!conLai.isEmpty()) {
                conLai.get(0).setMacDinh(true);
                diaChiDAO.save(conLai.get(0));
            }
        }
    }

    @Transactional(readOnly = true)
    public List<CoTheDanhGia> canDanhGia(KhachHang kh) {
        List<CoTheDanhGia> ds = new ArrayList<>();
        for (DonHang d : donHangTV.cuaKhachTheoTrangThai(kh.getMaKhachHang(), TrangThaiDonHang.HOAN_THANH)) {
            for (ChiTietDonHang ct : d.getChiTiet()) {
                Long maSp = ct.getSanPham().getMaSanPham();
                boolean trung = ds.stream().anyMatch(x -> x.getMaDon().equals(d.getMaDonHang()) && x.getMaSanPham().equals(maSp));
                if (!trung && !khachTV.daDanhGia(d.getMaDonHang(), maSp)) {
                    ds.add(new CoTheDanhGia(d.getMaDonHang(), maSp, ct.getTenSanPhamLucBan(), d.getNgayHoanThanh()));
                }
            }
        }
        return ds;
    }

    @Transactional
    public void danhGia(KhachHang kh, Long maDon, Long maSanPham, Integer soSao, String noiDung) {
        if (soSao == null || soSao < 1 || soSao > 5) {
            throw new LoiNghiepVu("Bạn chọn từ 1 tới 5 sao.");
        }
        if (noiDung != null && noiDung.length() > 1000) {
            throw new LoiNghiepVu("Nội dung đánh giá dài quá 1000 ký tự.");
        }
        DonHang don = donHangTV.layDayDu(maDon).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng."));
        if (don.getKhachHang() == null || !don.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang())) {
            throw new LoiNghiepVu("Đơn này không thuộc tài khoản của bạn.");
        }
        if (don.getTrangThai() != TrangThaiDonHang.HOAN_THANH) {
            throw new LoiNghiepVu("Chỉ đánh giá được sau khi đơn đã hoàn thành (đã nhận hàng).");
        }
        ChiTietDonHang ct = don.getChiTiet().stream().filter(c -> c.getSanPham().getMaSanPham().equals(maSanPham)).findFirst()
                .orElseThrow(() -> new LoiNghiepVu("Sản phẩm này không có trong đơn."));
        if (khachTV.daDanhGia(maDon, maSanPham)) {
            throw new LoiNghiepVu("Bạn đã đánh giá sản phẩm này cho đơn " + DinhDangTien.maDon(maDon) + " rồi.");
        }
        DanhGia dg = new DanhGia();
        dg.setDonHang(don);
        dg.setSanPham(ct.getSanPham());
        dg.setKhachHang(khachTV.findById(kh.getMaKhachHang()).orElseThrow());
        dg.setSoSao(soSao);
        dg.setNoiDung(noiDung == null || noiDung.isBlank() ? null : noiDung.trim());
        dg.setNgayDanhGia(LocalDateTime.now());
        danhGiaDAO.save(dg);
    }

    public static class CoTheDanhGia {

        private final Long maDon;
        private final Long maSanPham;
        private final String tenSanPham;
        private final LocalDateTime ngayHoanThanh;

        public CoTheDanhGia(Long maDon, Long maSanPham, String tenSanPham, LocalDateTime ngayHoanThanh) {
            this.maDon = maDon;
            this.maSanPham = maSanPham;
            this.tenSanPham = tenSanPham;
            this.ngayHoanThanh = ngayHoanThanh;
        }

        public Long getMaDon() {
            return maDon;
        }

        public Long getMaSanPham() {
            return maSanPham;
        }

        public String getTenSanPham() {
            return tenSanPham;
        }

        public LocalDateTime getNgayHoanThanh() {
            return ngayHoanThanh;
        }
    }
}
