package vn.edu.huit.pttk.diengiadung.nghiepvu.g4;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.DonHangTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.GiaoDichTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.NhanVienTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuGiaoTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuLapDatTruyVanG4;
import vn.edu.huit.pttk.diengiadung.khodulieu.g4.PhieuXuatTruyVanG4;
import vn.edu.huit.pttk.diengiadung.nghiepvu.DonHangBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.SerialBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.TaiKhoanBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.ChiTietDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.DonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.GiaoDichThanhToan;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.LoaiViecGiao;
import vn.edu.huit.pttk.diengiadung.thucthe.NhanVien;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuGiaoHang;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuLapDat;
import vn.edu.huit.pttk.diengiadung.thucthe.PhieuXuat;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiDonHang;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiGiaoDich;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiPhieuGiao;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiThanhToan;

@Service
public class GiaoHangBUS {

    public static final LocalTime GIO_MO_CUA = LocalTime.of(8, 0);
    public static final LocalTime GIO_DONG_CUA = LocalTime.of(20, 0);
    public static final int SO_NGAY_VIEC_DA_XONG = 7;

    public record DonCho(DonHang don, List<String> mayCanLap, List<PhieuGiaoHang> phieuCu) {
    }

    public record KetQuaPhanCong(boolean daLuu, List<String> trung, List<String> nguoiTrong, String khungTrong, Long maPhieu) {
    }

    public record ChiTietPhieu(PhieuGiaoHang phieu, DonHang don, List<PhieuXuat> phieuXuat, PhieuLapDat bienBan,
                               long conPhaiThu, long goiYThu, boolean canBienBan, boolean duocCapNhat) {
    }

    private final DonHangTruyVanG4 donDAO;
    private final PhieuGiaoTruyVanG4 phieuDAO;
    private final PhieuXuatTruyVanG4 phieuXuatDAO;
    private final PhieuLapDatTruyVanG4 bienBanDAO;
    private final GiaoDichTruyVanG4 giaoDichDAO;
    private final NhanVienTruyVanG4 nhanVienDAO;
    private final DonHangBUS donHangBUS;
    private final SerialBUS serialBUS;
    private final TaiKhoanBUS taiKhoanBUS;

    public GiaoHangBUS(DonHangTruyVanG4 donDAO, PhieuGiaoTruyVanG4 phieuDAO, PhieuXuatTruyVanG4 phieuXuatDAO,
                       PhieuLapDatTruyVanG4 bienBanDAO, GiaoDichTruyVanG4 giaoDichDAO, NhanVienTruyVanG4 nhanVienDAO,
                       DonHangBUS donHangBUS, SerialBUS serialBUS, TaiKhoanBUS taiKhoanBUS) {
        this.donDAO = donDAO;
        this.phieuDAO = phieuDAO;
        this.phieuXuatDAO = phieuXuatDAO;
        this.bienBanDAO = bienBanDAO;
        this.giaoDichDAO = giaoDichDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.donHangBUS = donHangBUS;
        this.serialBUS = serialBUS;
        this.taiKhoanBUS = taiKhoanBUS;
    }

    private static boolean laPhieuGiao(PhieuGiaoHang p) {
        return p.getLoaiViec() != LoaiViecGiao.KHAO_SAT;
    }

    private static boolean conHieuLuc(PhieuGiaoHang p) {
        return p.getTrangThai() != TrangThaiPhieuGiao.KHONG_THANH_CONG;
    }

    private static List<String> mayCanLap(DonHang don) {
        List<String> kq = new ArrayList<>();
        for (ChiTietDonHang ct : don.getChiTiet()) {
            if (Boolean.TRUE.equals(ct.getSanPham().getCanLapDat())) {
                kq.add(ct.getTenSanPhamLucBan() + " × " + ct.getSoLuong());
            }
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<DonCho> donChoPhanCong() {
        List<DonCho> kq = new ArrayList<>();
        for (DonHang d : donDAO.theoCacTrangThai(List.of(TrangThaiDonHang.DA_XAC_NHAN, TrangThaiDonHang.DANG_GIAO))) {
            List<PhieuGiaoHang> phieu = phieuDAO.theoDon(d);
            boolean daCo = phieu.stream().anyMatch(p -> laPhieuGiao(p) && conHieuLuc(p));
            if (!daCo) {
                d.getChiTiet().forEach(ct -> Hibernate.initialize(ct.getSanPham()));
                kq.add(new DonCho(d, mayCanLap(d), phieu));
            }
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public List<NhanVien> nhanVienGiaoHang() {
        return nhanVienDAO.theoVai("NV_GIAO_HANG");
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> lich(LocalDate ngay, Long maNhanVien) {
        return phieuDAO.lichNgay(ngay, maNhanVien);
    }

    @Transactional(readOnly = true)
    public DonHang donDayDu(Long maDon) {
        DonHang d = donDAO.layDayDu(maDon).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn " + G4Chung.maDon(maDon)));
        d.getChiTiet().forEach(ct -> Hibernate.initialize(ct.getSanPham()));
        return d;
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> phieuCuaDon(Long maDon) {
        return phieuDAO.theoDon(donDayDu(maDon));
    }

    @Transactional(readOnly = true)
    public PhanCongForm formMoi(Long maDon) {
        DonHang d = donDayDu(maDon);
        PhanCongForm f = new PhanCongForm();
        f.setMaDon(maDon);
        boolean noiThanh = !Boolean.FALSE.equals(d.getNoiThanh());
        f.setCachGiao(noiThanh ? PhanCongForm.NHAN_VIEN : PhanCongForm.VAN_CHUYEN);
        f.setLoaiViec(noiThanh && Boolean.TRUE.equals(d.getCanLapDat()) ? LoaiViecGiao.GIAO_LAP : LoaiViecGiao.GIAO);
        f.setNgayHen(LocalDate.now().plusDays(1));
        f.setGioBatDau(LocalTime.of(8, 0));
        f.setGioKetThuc(LocalTime.of(10, 0));
        return f;
    }

    @Transactional(readOnly = true)
    public PhanCongForm formSua(Long maPhieu) {
        PhieuGiaoHang p = layPhieu(maPhieu);
        if (p.getTrangThai() != TrangThaiPhieuGiao.DA_PHAN_CONG) {
            throw new LoiNghiepVu("Phiếu đã \"" + p.getTrangThai().getNhan() + "\", không đổi lịch được");
        }
        PhanCongForm f = new PhanCongForm();
        f.setMaDon(p.getDonHang().getMaDonHang());
        f.setMaPhieu(maPhieu);
        f.setLoaiViec(p.getLoaiViec());
        f.setCachGiao(p.getNhanVienGiao() != null ? PhanCongForm.NHAN_VIEN : PhanCongForm.VAN_CHUYEN);
        f.setMaNhanVien(p.getNhanVienGiao() == null ? null : p.getNhanVienGiao().getMaNhanVien());
        f.setDonViVanChuyen(p.getDonViVanChuyen());
        f.setMaVanDon(p.getMaVanDon());
        f.setNgayHen(p.getNgayHen());
        f.setGioBatDau(p.getGioBatDau());
        f.setGioKetThuc(p.getGioKetThuc());
        return f;
    }

    private PhieuGiaoHang layPhieu(Long ma) {
        return phieuDAO.layDayDu(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy phiếu giao PG" + ma));
    }

    private List<PhieuGiaoHang> trungLich(Long maNhanVien, LocalDate ngay, LocalTime bd, LocalTime kt, Long boQua) {
        List<PhieuGiaoHang> kq = new ArrayList<>();
        for (PhieuGiaoHang p : phieuDAO.lichNgay(ngay, maNhanVien)) {
            if (conHieuLuc(p) && !p.getMaPhieuGiao().equals(boQua) && p.getGioBatDau().isBefore(kt) && bd.isBefore(p.getGioKetThuc())) {
                kq.add(p);
            }
        }
        return kq;
    }

    private String khungTrongGanNhat(Long maNhanVien, LocalDate ngay, LocalTime bd, LocalTime kt, Long boQua) {
        long phut = Duration.between(bd, kt).toMinutes();
        LocalTime tot = null;
        long lech = Long.MAX_VALUE;
        for (LocalTime t = GIO_MO_CUA; !t.plusMinutes(phut).isAfter(GIO_DONG_CUA); t = t.plusMinutes(30)) {
            if (LocalDateTime.of(ngay, t).isBefore(LocalDateTime.now())) {
                continue;
            }
            if (trungLich(maNhanVien, ngay, t, t.plusMinutes(phut), boQua).isEmpty()) {
                long l = Math.abs(Duration.between(bd, t).toMinutes());
                if (l < lech) {
                    lech = l;
                    tot = t;
                }
            }
            if (t.plusMinutes(30).isBefore(t)) {
                break;
            }
        }
        return tot == null ? "không còn khung trống trong ngày" : tot + "–" + tot.plusMinutes(phut);
    }

    @Transactional
    public KetQuaPhanCong phanCong(PhanCongForm f) {
        NhanVien nguoiXep = taiKhoanBUS.batBuocNhanVien();
        DonHang don = donDayDu(f.getMaDon());
        PhieuGiaoHang phieu = f.getMaPhieu() == null ? new PhieuGiaoHang() : layPhieu(f.getMaPhieu());
        if (f.getMaPhieu() != null) {
            if (!phieu.getDonHang().getMaDonHang().equals(don.getMaDonHang())) {
                throw new LoiNghiepVu("Phiếu không thuộc đơn này");
            }
            if (phieu.getTrangThai() != TrangThaiPhieuGiao.DA_PHAN_CONG) {
                throw new LoiNghiepVu("Phiếu đã \"" + phieu.getTrangThai().getNhan() + "\", không đổi lịch được");
            }
        }
        if (don.getTrangThai() != TrangThaiDonHang.DA_XAC_NHAN && don.getTrangThai() != TrangThaiDonHang.DANG_GIAO) {
            throw new LoiNghiepVu("Đơn " + G4Chung.maDon(don.getMaDonHang()) + " đang \"" + don.getTrangThai().getNhan()
                    + "\", chỉ phân công cho đơn Đã xác nhận hoặc Đang giao");
        }
        if (f.getLoaiViec() == null) {
            throw new LoiNghiepVu("Chọn loại việc");
        }
        if (f.getNgayHen() == null || f.getGioBatDau() == null || f.getGioKetThuc() == null) {
            throw new LoiNghiepVu("Nhập đủ ngày hẹn, giờ bắt đầu và giờ kết thúc");
        }
        if (!f.getGioKetThuc().isAfter(f.getGioBatDau())) {
            throw new LoiNghiepVu("Giờ kết thúc phải sau giờ bắt đầu");
        }
        if (LocalDateTime.of(f.getNgayHen(), f.getGioBatDau()).isBefore(LocalDateTime.now())) {
            throw new LoiNghiepVu("Chọn ngày giờ sau thời điểm hiện tại");
        }
        boolean noiThanh = !Boolean.FALSE.equals(don.getNoiThanh());
        boolean coLap = Boolean.TRUE.equals(don.getCanLapDat()) || !mayCanLap(don).isEmpty();
        boolean vanChuyen = PhanCongForm.VAN_CHUYEN.equals(f.getCachGiao());
        if (!noiThanh && !vanChuyen) {
            throw new LoiNghiepVu("Đơn giao tỉnh phải gửi đơn vị vận chuyển (QĐ2)");
        }
        if (noiThanh && vanChuyen) {
            throw new LoiNghiepVu("Đơn nội thành do nhân viên giao hàng – kỹ thuật của cửa hàng tự giao (QĐ2)");
        }
        if (vanChuyen && f.getLoaiViec() != LoaiViecGiao.GIAO) {
            throw new LoiNghiepVu("Đơn gửi đơn vị vận chuyển chỉ có loại việc Giao hàng, cửa hàng không lắp đặt");
        }
        if (!vanChuyen && coLap && f.getLoaiViec() == LoaiViecGiao.GIAO) {
            throw new LoiNghiepVu("Đơn có máy cần lắp: chọn Giao kèm lắp đặt, hoặc Khảo sát lắp đặt trước");
        }
        if (!coLap && f.getLoaiViec() != LoaiViecGiao.GIAO) {
            throw new LoiNghiepVu("Đơn không có máy cần lắp, chọn loại việc Giao hàng");
        }
        if (f.getLoaiViec() != LoaiViecGiao.KHAO_SAT && (f.getMaPhieu() == null || phieu.getLoaiViec() == LoaiViecGiao.KHAO_SAT)) {
            boolean daCo = phieuDAO.theoDon(don).stream()
                    .anyMatch(p -> laPhieuGiao(p) && conHieuLuc(p) && !p.getMaPhieuGiao().equals(f.getMaPhieu()));
            if (daCo) {
                throw new LoiNghiepVu("Đơn đã có phiếu giao còn hiệu lực, dùng \"Đổi lịch\" trên phiếu đó");
            }
        }
        NhanVien nguoiGiao = null;
        String dvvc = null;
        List<String> trung = new ArrayList<>();
        List<String> nguoiTrong = new ArrayList<>();
        String khungTrong = null;
        if (vanChuyen) {
            dvvc = G4Chung.gioiHan(G4Chung.batBuoc(f.getDonViVanChuyen(), "Nhập tên đơn vị vận chuyển"), 80, "Đơn vị vận chuyển");
        } else {
            if (f.getMaNhanVien() == null) {
                throw new LoiNghiepVu("Chọn nhân viên giao hàng – kỹ thuật phụ trách");
            }
            List<NhanVien> dsGiao = nhanVienDAO.theoVai("NV_GIAO_HANG");
            nguoiGiao = dsGiao.stream().filter(n -> n.getMaNhanVien().equals(f.getMaNhanVien())).findFirst()
                    .orElseThrow(() -> new LoiNghiepVu("Người được chọn không phải nhân viên giao hàng – kỹ thuật"));
            for (PhieuGiaoHang p : trungLich(nguoiGiao.getMaNhanVien(), f.getNgayHen(), f.getGioBatDau(), f.getGioKetThuc(), f.getMaPhieu())) {
                trung.add("Nhân viên " + nguoiGiao.getHoTen() + " đã có đơn " + G4Chung.maDon(p.getDonHang().getMaDonHang())
                        + " lúc " + p.getGioBatDau() + "–" + p.getGioKetThuc() + ", địa chỉ " + p.getDonHang().getDiaChiGiao());
            }
            if (!trung.isEmpty()) {
                for (NhanVien n : dsGiao) {
                    if (!n.getMaNhanVien().equals(nguoiGiao.getMaNhanVien())
                            && trungLich(n.getMaNhanVien(), f.getNgayHen(), f.getGioBatDau(), f.getGioKetThuc(), f.getMaPhieu()).isEmpty()) {
                        nguoiTrong.add(n.getHoTen());
                    }
                }
                khungTrong = khungTrongGanNhat(nguoiGiao.getMaNhanVien(), f.getNgayHen(), f.getGioBatDau(), f.getGioKetThuc(), f.getMaPhieu());
                if (G4Chung.rong(f.getLyDoTrungLich()) == null) {
                    return new KetQuaPhanCong(false, trung, nguoiTrong, khungTrong, null);
                }
            }
        }
        if (f.getMaPhieu() != null) {
            String cu = "Đổi lịch phiếu PG" + phieu.getMaPhieuGiao() + ", lịch cũ: " + phieu.getNgayHen().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " " + phieu.getGioBatDau() + "–" + phieu.getGioKetThuc() + ", "
                    + (phieu.getNhanVienGiao() != null ? phieu.getNhanVienGiao().getHoTen() : phieu.getDonViVanChuyen());
            G4Chung.noiGhiChu(don, cu);
            donDAO.save(don);
        }
        phieu.setDonHang(don);
        phieu.setLoaiViec(f.getLoaiViec());
        phieu.setNhanVienGiao(nguoiGiao);
        phieu.setDonViVanChuyen(dvvc);
        phieu.setMaVanDon(vanChuyen ? G4Chung.gioiHan(G4Chung.rong(f.getMaVanDon()), 40, "Mã vận đơn") : null);
        phieu.setNgayHen(f.getNgayHen());
        phieu.setGioBatDau(f.getGioBatDau());
        phieu.setGioKetThuc(f.getGioKetThuc());
        phieu.setLyDoTrungLich(trung.isEmpty() ? null : G4Chung.gioiHan(G4Chung.rong(f.getLyDoTrungLich()), 255, "Lý do lưu khi trùng lịch"));
        phieu.setNguoiPhanCong(nguoiXep);
        if (phieu.getTrangThai() == null) {
            phieu.setTrangThai(TrangThaiPhieuGiao.DA_PHAN_CONG);
            phieu.setTienThuKhiGiao(0L);
        }
        phieuDAO.save(phieu);
        return new KetQuaPhanCong(true, trung, nguoiTrong, khungTrong, phieu.getMaPhieuGiao());
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> viecCuaToi() {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        return phieuDAO.viecDangCho(nv.getMaNhanVien());
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> viecDaXongCuaToi() {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        return phieuDAO.viecDaXong(nv.getMaNhanVien(), LocalDate.now().minusDays(SO_NGAY_VIEC_DA_XONG));
    }

    @Transactional(readOnly = true)
    public List<PhieuGiaoHang> vanChuyenDangCho() {
        return phieuDAO.vanChuyenDangCho();
    }

    private boolean duocCapNhat(PhieuGiaoHang p) {
        NhanVien nv = taiKhoanBUS.nhanVienHienTai().orElse(null);
        if (nv == null || p.getTrangThai() != TrangThaiPhieuGiao.DA_PHAN_CONG) {
            return false;
        }
        if (p.getNhanVienGiao() != null) {
            return taiKhoanBUS.coVai("NV_GIAO_HANG") && p.getNhanVienGiao().getMaNhanVien().equals(nv.getMaNhanVien());
        }
        return taiKhoanBUS.coVai("NV_BAN_HANG");
    }

    private long daTra(DonHang don) {
        long tra = 0;
        for (GiaoDichThanhToan g : giaoDichDAO.theoDon(don)) {
            if (g.getTrangThai() == TrangThaiGiaoDich.THANH_CONG && g.getLoai() != LoaiGiaoDich.HOAN_TIEN) {
                tra += g.getSoTien();
            }
        }
        return tra;
    }

    private boolean canBienBan(PhieuGiaoHang p, DonHang don) {
        return p.getLoaiViec() == LoaiViecGiao.GIAO_LAP
                || (p.getNhanVienGiao() != null && p.getLoaiViec() == LoaiViecGiao.GIAO && Boolean.TRUE.equals(don.getCanLapDat()));
    }

    @Transactional(readOnly = true)
    public ChiTietPhieu chiTietPhieu(Long maPhieu) {
        PhieuGiaoHang p = layPhieu(maPhieu);
        DonHang don = donDayDu(p.getDonHang().getMaDonHang());
        List<PhieuXuat> px = phieuXuatDAO.theoDon(don);
        PhieuLapDat bb = bienBanDAO.theoPhieuGiao(maPhieu).orElse(null);
        long con = Math.max(0, (don.getTongTien() == null ? 0 : don.getTongTien()) - daTra(don));
        long goiY = con;
        if (bb != null) {
            goiY += bb.getPhiVatTu() + Math.max(0, bb.getPhiLapDat() - (don.getPhiLapDat() == null ? 0 : don.getPhiLapDat()));
        }
        if (p.getLoaiViec() == LoaiViecGiao.KHAO_SAT) {
            goiY = 0;
        }
        return new ChiTietPhieu(p, don, px, bb, con, goiY, canBienBan(p, don), duocCapNhat(p));
    }

    private PhieuGiaoHang phieuDuocCapNhat(Long maPhieu) {
        PhieuGiaoHang p = layPhieu(maPhieu);
        if (p.getTrangThai() != TrangThaiPhieuGiao.DA_PHAN_CONG) {
            throw new LoiNghiepVu("Phiếu PG" + maPhieu + " đã \"" + p.getTrangThai().getNhan() + "\", không cập nhật lại được");
        }
        if (!duocCapNhat(p)) {
            throw new LoiNghiepVu(p.getNhanVienGiao() != null
                    ? "Phiếu này phân công cho " + p.getNhanVienGiao().getHoTen() + ", chỉ người đó cập nhật được"
                    : "Đơn gửi đơn vị vận chuyển do nhân viên bán hàng cập nhật");
        }
        return p;
    }

    @Transactional
    public void luuBienBan(Long maPhieu, String ketQuaChayThu, String tinhTrangCongTrinh, String phatSinh,
                           Long phiLapDat, Long phiVatTu, boolean khachDaKy) {
        PhieuGiaoHang p = phieuDuocCapNhat(maPhieu);
        DonHang don = p.getDonHang();
        if (!canBienBan(p, don)) {
            throw new LoiNghiepVu("Phiếu này không có việc lắp đặt");
        }
        if (p.getNhanVienGiao() == null) {
            throw new LoiNghiepVu("Đơn gửi đơn vị vận chuyển, cửa hàng không lắp đặt");
        }
        long phiLap = phiLapDat == null ? 0 : phiLapDat;
        long phiVt = phiVatTu == null ? 0 : phiVatTu;
        if (phiLap < 0 || phiVt < 0) {
            throw new LoiNghiepVu("Phí lắp và phí vật tư không được âm");
        }
        PhieuLapDat bb = bienBanDAO.theoPhieuGiao(maPhieu).orElseGet(PhieuLapDat::new);
        bb.setPhieuGiao(p);
        bb.setKetQuaChayThu(G4Chung.gioiHan(G4Chung.batBuoc(ketQuaChayThu, "Ghi kết quả chạy thử"), 255, "Kết quả chạy thử"));
        bb.setTinhTrangCongTrinh(G4Chung.gioiHan(G4Chung.rong(tinhTrangCongTrinh), 255, "Tình trạng công trình"));
        bb.setPhatSinh(G4Chung.gioiHan(G4Chung.rong(phatSinh), 255, "Phát sinh"));
        bb.setPhiLapDat(phiLap);
        bb.setPhiVatTu(phiVt);
        bb.setKhachDaKy(khachDaKy);
        bb.setNgayLap(LocalDateTime.now());
        bienBanDAO.save(bb);
    }

    @Transactional
    public String daGiao(Long maPhieu, Long tienThu) {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        PhieuGiaoHang p = phieuDuocCapNhat(maPhieu);
        DonHang don = donDayDu(p.getDonHang().getMaDonHang());
        if (p.getLoaiViec() == LoaiViecGiao.KHAO_SAT) {
            p.setTrangThai(TrangThaiPhieuGiao.DA_GIAO);
            phieuDAO.save(p);
            return "Đã ghi xong buổi khảo sát cho đơn " + G4Chung.maDon(don.getMaDonHang()) + ". Quản lý xếp tiếp buổi giao kèm lắp.";
        }
        if (don.getTrangThai() != TrangThaiDonHang.DANG_GIAO) {
            throw new LoiNghiepVu("Đơn " + G4Chung.maDon(don.getMaDonHang()) + " đang \"" + don.getTrangThai().getNhan()
                    + "\": thủ kho chưa xuất kho nên chưa báo đã giao được");
        }
        long thu = tienThu == null ? 0 : tienThu;
        if (thu < 0) {
            throw new LoiNghiepVu("Tiền thu khi giao không được âm");
        }
        if (canBienBan(p, don)) {
            PhieuLapDat bb = bienBanDAO.theoPhieuGiao(maPhieu).orElse(null);
            if (bb == null || !Boolean.TRUE.equals(bb.getKhachDaKy())) {
                throw new LoiNghiepVu("Đơn có lắp đặt: lập biên bản lắp đặt và có chữ ký khách trước khi báo hoàn thành");
            }
        }
        long con = Math.max(0, (don.getTongTien() == null ? 0 : don.getTongTien()) - daTra(don));
        p.setTienThuKhiGiao(thu);
        p.setTrangThai(TrangThaiPhieuGiao.DA_GIAO);
        phieuDAO.save(p);
        if (thu > 0) {
            GiaoDichThanhToan g = new GiaoDichThanhToan();
            g.setDonHang(don);
            g.setLoai(LoaiGiaoDich.THANH_TOAN);
            g.setSoTien(thu);
            g.setPhuongThuc(p.getDonViVanChuyen() != null ? "Thu hộ qua vận chuyển" : "Tiền mặt khi giao");
            g.setTrangThai(TrangThaiGiaoDich.THANH_CONG);
            g.setThoiDiem(LocalDateTime.now());
            giaoDichDAO.save(g);
        }
        if (thu >= con) {
            don.setTrangThaiThanhToan(TrangThaiThanhToan.DA_THANH_TOAN);
        }
        if (don.getSoHoaDon() == null) {
            don.setSoHoaDon(String.format("HD%07d", don.getMaDonHang()));
        }
        int soMay = 0;
        LocalDateTime bayGio = LocalDateTime.now();
        for (PhieuXuat x : phieuXuatDAO.theoDon(don)) {
            for (SanPhamSerial s : x.getSerial()) {
                if (s.getTrangThai() == TrangThaiSerial.DA_XUAT) {
                    s.setNgayBan(bayGio);
                    s.setDonHangBan(don);
                    serialBUS.doiTrangThai(s, TrangThaiSerial.DA_BAN);
                    soMay++;
                }
            }
        }
        donHangBUS.doiTrangThai(don, TrangThaiDonHang.HOAN_THANH, nv,
                "Đã giao" + (thu > 0 ? ", thu khi giao " + G4Chung.tien(thu) : ""));
        String kq = "Đã ghi giao xong đơn " + G4Chung.maDon(don.getMaDonHang()) + ", " + soMay + " máy chuyển sang Đã bán.";
        if (thu < con) {
            kq += " Còn thiếu " + G4Chung.tien(con - thu) + " so với số phải thu, trạng thái thanh toán giữ nguyên.";
        }
        return kq;
    }

    @Transactional
    public String khongThanhCong(Long maPhieu, String lyDo, boolean henGiaoLai) {
        NhanVien nv = taiKhoanBUS.batBuocNhanVien();
        PhieuGiaoHang p = phieuDuocCapNhat(maPhieu);
        String ld = G4Chung.gioiHan(G4Chung.batBuoc(lyDo, "Ghi lý do không thành công"), 255, "Lý do");
        DonHang don = p.getDonHang();
        p.setTrangThai(TrangThaiPhieuGiao.KHONG_THANH_CONG);
        p.setLyDoKhongThanhCong(ld);
        phieuDAO.save(p);
        if (p.getLoaiViec() == LoaiViecGiao.KHAO_SAT) {
            return "Đã ghi buổi khảo sát không thành công. Quản lý xếp lại lịch.";
        }
        if (henGiaoLai || don.getTrangThai() != TrangThaiDonHang.DANG_GIAO) {
            G4Chung.noiGhiChu(don, (henGiaoLai ? "Hẹn giao lại (phiếu PG" + maPhieu + "): " : "Phiếu PG" + maPhieu + " không thành công: ") + ld);
            donDAO.save(don);
            return "Đã ghi phiếu không thành công, đơn " + G4Chung.maDon(don.getMaDonHang()) + " giữ \""
                    + don.getTrangThai().getNhan() + "\" và quay về danh sách chờ phân công.";
        }
        donHangBUS.doiTrangThai(don, TrangThaiDonHang.GIAO_KHONG_THANH_CONG, nv, "Giao không thành công: " + ld);
        return "Đơn " + G4Chung.maDon(don.getMaDonHang()) + " chuyển sang Giao không thành công; máy chờ thủ kho nhận lại về kho.";
    }
}
