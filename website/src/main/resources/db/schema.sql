CREATE TABLE IF NOT EXISTS VaiTro (
    maVaiTro VARCHAR(30) NOT NULL,
    tenVaiTro VARCHAR(60) NOT NULL,
    PRIMARY KEY (maVaiTro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS TaiKhoan (
    maTaiKhoan BIGINT NOT NULL AUTO_INCREMENT,
    tenDangNhap VARCHAR(50) NOT NULL,
    matKhau VARCHAR(100) NOT NULL,
    trangThai VARCHAR(20) NOT NULL DEFAULT 'DANG_DUNG',
    ngayTao DATETIME NOT NULL,
    soLanSaiLienTiep INT NOT NULL DEFAULT 0,
    khoaDenLuc DATETIME NULL,
    phaiDoiMatKhau BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (maTaiKhoan),
    UNIQUE KEY uq_TaiKhoan_tenDangNhap (tenDangNhap),
    CONSTRAINT ck_TaiKhoan_trangThai CHECK (trangThai IN ('DANG_DUNG','DA_KHOA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS TaiKhoanVaiTro (
    maTaiKhoan BIGINT NOT NULL,
    maVaiTro VARCHAR(30) NOT NULL,
    PRIMARY KEY (maTaiKhoan, maVaiTro),
    CONSTRAINT fk_TKVT_TaiKhoan FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan (maTaiKhoan),
    CONSTRAINT fk_TKVT_VaiTro FOREIGN KEY (maVaiTro) REFERENCES VaiTro (maVaiTro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS NhanVien (
    maNhanVien BIGINT NOT NULL AUTO_INCREMENT,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    maTaiKhoan BIGINT NOT NULL,
    PRIMARY KEY (maNhanVien),
    UNIQUE KEY uq_NhanVien_maTaiKhoan (maTaiKhoan),
    CONSTRAINT fk_NhanVien_TaiKhoan FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan (maTaiKhoan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS KhachHang (
    maKhachHang BIGINT NOT NULL AUTO_INCREMENT,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    email VARCHAR(100) NULL,
    ngayDangKy DATETIME NOT NULL,
    maTaiKhoan BIGINT NOT NULL,
    PRIMARY KEY (maKhachHang),
    UNIQUE KEY uq_KhachHang_maTaiKhoan (maTaiKhoan),
    CONSTRAINT fk_KhachHang_TaiKhoan FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan (maTaiKhoan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS DiaChiGiaoHang (
    maDiaChi BIGINT NOT NULL AUTO_INCREMENT,
    maKhachHang BIGINT NOT NULL,
    hoTenNguoiNhan VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    diaChi VARCHAR(255) NOT NULL,
    noiThanh BIT(1) NOT NULL DEFAULT b'1',
    macDinh BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (maDiaChi),
    CONSTRAINT fk_DiaChi_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang (maKhachHang)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS DanhMuc (
    maDanhMuc VARCHAR(30) NOT NULL,
    tenDanhMuc VARCHAR(100) NOT NULL,
    thuTu INT NOT NULL DEFAULT 0,
    PRIMARY KEY (maDanhMuc),
    UNIQUE KEY uq_DanhMuc_ten (tenDanhMuc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ThuongHieu (
    maThuongHieu BIGINT NOT NULL AUTO_INCREMENT,
    tenThuongHieu VARCHAR(60) NOT NULL,
    xuatXu VARCHAR(60) NULL,
    PRIMARY KEY (maThuongHieu),
    UNIQUE KEY uq_ThuongHieu_ten (tenThuongHieu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS SanPham (
    maSanPham BIGINT NOT NULL AUTO_INCREMENT,
    tenSanPham VARCHAR(200) NOT NULL,
    model VARCHAR(60) NOT NULL,
    maDanhMuc VARCHAR(30) NOT NULL,
    maThuongHieu BIGINT NOT NULL,
    giaBan BIGINT NULL,
    giaNiemYet BIGINT NULL,
    thoiHanBaoHanh INT NOT NULL DEFAULT 12,
    canLapDat BIT(1) NOT NULL DEFAULT b'0',
    moTa TEXT NULL,
    anh VARCHAR(255) NULL,
    trangThai VARCHAR(20) NOT NULL DEFAULT 'CHUA_MO_BAN',
    ngayTao DATETIME NOT NULL,
    PRIMARY KEY (maSanPham),
    UNIQUE KEY uq_SanPham_model (model),
    CONSTRAINT fk_SanPham_DanhMuc FOREIGN KEY (maDanhMuc) REFERENCES DanhMuc (maDanhMuc),
    CONSTRAINT fk_SanPham_ThuongHieu FOREIGN KEY (maThuongHieu) REFERENCES ThuongHieu (maThuongHieu),
    CONSTRAINT ck_SanPham_trangThai CHECK (trangThai IN ('CHUA_MO_BAN','DANG_BAN','NGUNG_BAN')),
    CONSTRAINT ck_SanPham_gia CHECK (giaBan IS NULL OR giaBan > 0),
    CONSTRAINT ck_SanPham_baoHanh CHECK (thoiHanBaoHanh >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ThongSoKyThuat (
    maThongSo BIGINT NOT NULL AUTO_INCREMENT,
    maSanPham BIGINT NOT NULL,
    tenThongSo VARCHAR(80) NOT NULL,
    giaTri VARCHAR(120) NOT NULL,
    PRIMARY KEY (maThongSo),
    CONSTRAINT fk_ThongSo_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS LichSuGia (
    maLichSu BIGINT NOT NULL AUTO_INCREMENT,
    maSanPham BIGINT NOT NULL,
    giaCu BIGINT NULL,
    giaMoi BIGINT NOT NULL,
    lyDo VARCHAR(255) NULL,
    duoiGiaNhap BIT(1) NOT NULL DEFAULT b'0',
    thoiDiem DATETIME NOT NULL,
    maNhanVien BIGINT NOT NULL,
    PRIMARY KEY (maLichSu),
    CONSTRAINT fk_LichSuGia_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT fk_LichSuGia_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien (maNhanVien)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS KhuyenMai (
    maKhuyenMai BIGINT NOT NULL AUTO_INCREMENT,
    tenKhuyenMai VARCHAR(150) NOT NULL,
    loai VARCHAR(25) NOT NULL,
    maCode VARCHAR(30) NULL,
    kieuGiam VARCHAR(15) NOT NULL,
    mucGiam BIGINT NOT NULL,
    giamToiDa BIGINT NULL,
    donToiThieu BIGINT NOT NULL DEFAULT 0,
    quaTang VARCHAR(150) NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    soLuotToiDa INT NULL,
    soLuotDaDung INT NOT NULL DEFAULT 0,
    trangThai VARCHAR(15) NOT NULL DEFAULT 'DANG_CHAY',
    maNguoiTao BIGINT NOT NULL,
    PRIMARY KEY (maKhuyenMai),
    UNIQUE KEY uq_KhuyenMai_maCode (maCode),
    CONSTRAINT fk_KhuyenMai_NhanVien FOREIGN KEY (maNguoiTao) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_KhuyenMai_loai CHECK (loai IN ('GIAM_THEO_SAN_PHAM','MA_GIAM_GIA')),
    CONSTRAINT ck_KhuyenMai_kieu CHECK (kieuGiam IN ('PHAN_TRAM','SO_TIEN')),
    CONSTRAINT ck_KhuyenMai_trangThai CHECK (trangThai IN ('DANG_CHAY','TAM_DUNG')),
    CONSTRAINT ck_KhuyenMai_ngay CHECK (ngayKetThuc >= ngayBatDau),
    CONSTRAINT ck_KhuyenMai_muc CHECK (mucGiam > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS KhuyenMaiSanPham (
    maKhuyenMai BIGINT NOT NULL,
    maSanPham BIGINT NOT NULL,
    PRIMARY KEY (maKhuyenMai, maSanPham),
    CONSTRAINT fk_KMSP_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai (maKhuyenMai) ON DELETE CASCADE,
    CONSTRAINT fk_KMSP_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS DonHang (
    maDonHang BIGINT NOT NULL AUTO_INCREMENT,
    maKhachHang BIGINT NULL,
    hoTenNguoiNhan VARCHAR(100) NOT NULL,
    soDienThoaiNhan VARCHAR(15) NOT NULL,
    email VARCHAR(100) NULL,
    diaChiGiao VARCHAR(255) NOT NULL,
    noiThanh BIT(1) NOT NULL DEFAULT b'1',
    canLapDat BIT(1) NOT NULL DEFAULT b'0',
    ngayDat DATETIME NOT NULL,
    ngayHoanThanh DATETIME NULL,
    tienHang BIGINT NOT NULL,
    tienGiam BIGINT NOT NULL DEFAULT 0,
    phiGiao BIGINT NOT NULL DEFAULT 0,
    phiLapDat BIGINT NOT NULL DEFAULT 0,
    tongTien BIGINT NOT NULL,
    tienCoc BIGINT NOT NULL DEFAULT 0,
    phuongThucThanhToan VARCHAR(20) NOT NULL,
    trangThaiThanhToan VARCHAR(20) NOT NULL DEFAULT 'CHUA_THANH_TOAN',
    trangThai VARCHAR(25) NOT NULL DEFAULT 'CHO_XAC_NHAN',
    maKhuyenMai BIGINT NULL,
    maNhanVienDuyet BIGINT NULL,
    ghiChu VARCHAR(500) NULL,
    lyDoHuy VARCHAR(255) NULL,
    soHoaDon VARCHAR(30) NULL,
    dongYXuLyDuLieu BIT(1) NOT NULL DEFAULT b'0',
    dongYQuangCao BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (maDonHang),
    KEY ix_DonHang_soDienThoai (soDienThoaiNhan),
    KEY ix_DonHang_trangThai (trangThai),
    CONSTRAINT fk_DonHang_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang (maKhachHang),
    CONSTRAINT fk_DonHang_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai (maKhuyenMai),
    CONSTRAINT fk_DonHang_NhanVien FOREIGN KEY (maNhanVienDuyet) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_DonHang_trangThai CHECK (trangThai IN ('CHO_XAC_NHAN','DA_XAC_NHAN','DANG_GIAO','HOAN_THANH','GIAO_KHONG_THANH_CONG','DA_HUY','DA_TRA_HANG')),
    CONSTRAINT ck_DonHang_ttThanhToan CHECK (trangThaiThanhToan IN ('CHUA_THANH_TOAN','DA_DAT_COC','DA_THANH_TOAN','DA_HOAN_TIEN')),
    CONSTRAINT ck_DonHang_phuongThuc CHECK (phuongThucThanhToan IN ('TIEN_MAT_KHI_NHAN','CONG_THANH_TOAN')),
    CONSTRAINT ck_DonHang_tien CHECK (tienHang >= 0 AND tienGiam >= 0 AND tongTien >= 0 AND tienCoc >= 0),
    CONSTRAINT ck_DonHang_dongY CHECK (dongYXuLyDuLieu = b'1')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ChiTietDonHang (
    maChiTiet BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    maSanPham BIGINT NOT NULL,
    tenSanPhamLucBan VARCHAR(200) NOT NULL,
    soLuong INT NOT NULL,
    donGia BIGINT NOT NULL,
    PRIMARY KEY (maChiTiet),
    CONSTRAINT fk_CTDH_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang) ON DELETE CASCADE,
    CONSTRAINT fk_CTDH_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT ck_CTDH_soLuong CHECK (soLuong > 0),
    CONSTRAINT ck_CTDH_donGia CHECK (donGia >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS GiaoDichThanhToan (
    maGiaoDich BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    loai VARCHAR(15) NOT NULL,
    soTien BIGINT NOT NULL,
    phuongThuc VARCHAR(30) NOT NULL,
    maGiaoDichCong VARCHAR(40) NULL,
    trangThai VARCHAR(15) NOT NULL,
    thoiDiem DATETIME NOT NULL,
    PRIMARY KEY (maGiaoDich),
    UNIQUE KEY uq_GiaoDich_maCong (maGiaoDichCong),
    CONSTRAINT fk_GiaoDich_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang),
    CONSTRAINT ck_GiaoDich_loai CHECK (loai IN ('COC','THANH_TOAN','HOAN_TIEN')),
    CONSTRAINT ck_GiaoDich_trangThai CHECK (trangThai IN ('THANH_CONG','THAT_BAI')),
    CONSTRAINT ck_GiaoDich_soTien CHECK (soTien > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS LichSuTrangThaiDon (
    maLichSu BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    trangThaiCu VARCHAR(25) NULL,
    trangThaiMoi VARCHAR(25) NOT NULL,
    thoiDiem DATETIME NOT NULL,
    maNhanVien BIGINT NULL,
    ghiChu VARCHAR(255) NULL,
    PRIMARY KEY (maLichSu),
    CONSTRAINT fk_LSTT_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang) ON DELETE CASCADE,
    CONSTRAINT fk_LSTT_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien (maNhanVien)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS DanhGia (
    maDanhGia BIGINT NOT NULL AUTO_INCREMENT,
    maSanPham BIGINT NOT NULL,
    maKhachHang BIGINT NOT NULL,
    maDonHang BIGINT NOT NULL,
    soSao INT NOT NULL,
    noiDung VARCHAR(1000) NULL,
    ngayDanhGia DATETIME NOT NULL,
    PRIMARY KEY (maDanhGia),
    UNIQUE KEY uq_DanhGia_don_sp (maDonHang, maSanPham),
    CONSTRAINT fk_DanhGia_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT fk_DanhGia_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang (maKhachHang),
    CONSTRAINT fk_DanhGia_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang),
    CONSTRAINT ck_DanhGia_soSao CHECK (soSao BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PhieuGiaoHang (
    maPhieuGiao BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    loaiViec VARCHAR(15) NOT NULL,
    maNhanVienGiao BIGINT NULL,
    donViVanChuyen VARCHAR(80) NULL,
    maVanDon VARCHAR(40) NULL,
    ngayHen DATE NOT NULL,
    gioBatDau TIME NOT NULL,
    gioKetThuc TIME NOT NULL,
    lyDoTrungLich VARCHAR(255) NULL,
    trangThai VARCHAR(25) NOT NULL DEFAULT 'DA_PHAN_CONG',
    lyDoKhongThanhCong VARCHAR(255) NULL,
    tienThuKhiGiao BIGINT NOT NULL DEFAULT 0,
    maKeToanXacNhan BIGINT NULL,
    thoiDiemXacNhanTien DATETIME NULL,
    maNguoiPhanCong BIGINT NOT NULL,
    PRIMARY KEY (maPhieuGiao),
    KEY ix_PhieuGiao_lich (maNhanVienGiao, ngayHen),
    CONSTRAINT fk_PhieuGiao_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang),
    CONSTRAINT fk_PhieuGiao_NVGiao FOREIGN KEY (maNhanVienGiao) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT fk_PhieuGiao_KeToan FOREIGN KEY (maKeToanXacNhan) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT fk_PhieuGiao_QuanLy FOREIGN KEY (maNguoiPhanCong) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_PhieuGiao_loai CHECK (loaiViec IN ('GIAO','GIAO_LAP','KHAO_SAT')),
    CONSTRAINT ck_PhieuGiao_trangThai CHECK (trangThai IN ('DA_PHAN_CONG','DA_GIAO','KHONG_THANH_CONG')),
    CONSTRAINT ck_PhieuGiao_gio CHECK (gioKetThuc > gioBatDau),
    CONSTRAINT ck_PhieuGiao_nguoiGiao CHECK (maNhanVienGiao IS NOT NULL OR donViVanChuyen IS NOT NULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PhieuLapDat (
    maPhieuLapDat BIGINT NOT NULL AUTO_INCREMENT,
    maPhieuGiao BIGINT NOT NULL,
    ketQuaChayThu VARCHAR(255) NOT NULL,
    tinhTrangCongTrinh VARCHAR(255) NULL,
    phatSinh VARCHAR(255) NULL,
    phiLapDat BIGINT NOT NULL DEFAULT 0,
    phiVatTu BIGINT NOT NULL DEFAULT 0,
    khachDaKy BIT(1) NOT NULL DEFAULT b'0',
    ngayLap DATETIME NOT NULL,
    PRIMARY KEY (maPhieuLapDat),
    UNIQUE KEY uq_PhieuLapDat_phieuGiao (maPhieuGiao),
    CONSTRAINT fk_PhieuLapDat_PhieuGiao FOREIGN KEY (maPhieuGiao) REFERENCES PhieuGiaoHang (maPhieuGiao),
    CONSTRAINT ck_PhieuLapDat_phi CHECK (phiLapDat >= 0 AND phiVatTu >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS NhaCungCap (
    maNhaCungCap BIGINT NOT NULL AUTO_INCREMENT,
    tenNhaCungCap VARCHAR(150) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    email VARCHAR(100) NULL,
    diaChi VARCHAR(255) NULL,
    dangHopTac BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (maNhaCungCap),
    UNIQUE KEY uq_NhaCungCap_ten (tenNhaCungCap)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS DonDatHangNCC (
    maDonDat BIGINT NOT NULL AUTO_INCREMENT,
    maNhaCungCap BIGINT NOT NULL,
    ngayLap DATETIME NOT NULL,
    trangThai VARCHAR(15) NOT NULL DEFAULT 'DE_XUAT',
    maNguoiLap BIGINT NOT NULL,
    maNguoiGui BIGINT NULL,
    ghiChu VARCHAR(255) NULL,
    PRIMARY KEY (maDonDat),
    CONSTRAINT fk_DonDat_NCC FOREIGN KEY (maNhaCungCap) REFERENCES NhaCungCap (maNhaCungCap),
    CONSTRAINT fk_DonDat_NguoiLap FOREIGN KEY (maNguoiLap) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT fk_DonDat_NguoiGui FOREIGN KEY (maNguoiGui) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_DonDat_trangThai CHECK (trangThai IN ('DE_XUAT','DA_GUI','DA_NHAN','DA_HUY'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ChiTietDonDatHangNCC (
    maDonDat BIGINT NOT NULL,
    maSanPham BIGINT NOT NULL,
    soLuong INT NOT NULL,
    PRIMARY KEY (maDonDat, maSanPham),
    CONSTRAINT fk_CTDD_DonDat FOREIGN KEY (maDonDat) REFERENCES DonDatHangNCC (maDonDat) ON DELETE CASCADE,
    CONSTRAINT fk_CTDD_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT ck_CTDD_soLuong CHECK (soLuong > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PhieuNhap (
    maPhieuNhap BIGINT NOT NULL AUTO_INCREMENT,
    maNhaCungCap BIGINT NOT NULL,
    maDonDat BIGINT NULL,
    soHoaDonNCC VARCHAR(30) NULL,
    ngayNhap DATETIME NOT NULL,
    maThuKho BIGINT NOT NULL,
    ghiChu VARCHAR(255) NULL,
    PRIMARY KEY (maPhieuNhap),
    CONSTRAINT fk_PhieuNhap_NCC FOREIGN KEY (maNhaCungCap) REFERENCES NhaCungCap (maNhaCungCap),
    CONSTRAINT fk_PhieuNhap_DonDat FOREIGN KEY (maDonDat) REFERENCES DonDatHangNCC (maDonDat),
    CONSTRAINT fk_PhieuNhap_ThuKho FOREIGN KEY (maThuKho) REFERENCES NhanVien (maNhanVien)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ChiTietPhieuNhap (
    maChiTietNhap BIGINT NOT NULL AUTO_INCREMENT,
    maPhieuNhap BIGINT NOT NULL,
    maSanPham BIGINT NOT NULL,
    soLuongChungTu INT NOT NULL,
    soLuongThucNhap INT NOT NULL,
    donGiaNhap BIGINT NOT NULL,
    PRIMARY KEY (maChiTietNhap),
    CONSTRAINT fk_CTPN_PhieuNhap FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhap (maPhieuNhap),
    CONSTRAINT fk_CTPN_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT ck_CTPN_soLuong CHECK (soLuongChungTu > 0 AND soLuongThucNhap >= 0),
    CONSTRAINT ck_CTPN_donGia CHECK (donGiaNhap > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS SanPhamSerial (
    soSerial VARCHAR(50) NOT NULL,
    maSanPham BIGINT NOT NULL,
    maChiTietNhap BIGINT NOT NULL,
    ngayNhap DATETIME NOT NULL,
    ngayBan DATETIME NULL,
    maDonHangBan BIGINT NULL,
    trangThai VARCHAR(25) NOT NULL DEFAULT 'TRONG_KHO',
    PRIMARY KEY (soSerial),
    KEY ix_Serial_sp_trangThai (maSanPham, trangThai),
    CONSTRAINT fk_Serial_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham (maSanPham),
    CONSTRAINT fk_Serial_CTPN FOREIGN KEY (maChiTietNhap) REFERENCES ChiTietPhieuNhap (maChiTietNhap),
    CONSTRAINT fk_Serial_DonHang FOREIGN KEY (maDonHangBan) REFERENCES DonHang (maDonHang),
    CONSTRAINT ck_Serial_trangThai CHECK (trangThai IN ('TRONG_KHO','DA_XUAT','DA_BAN','DANG_BAO_HANH','TRA_VE_KHO','LOI_CHO_GUI_HANG'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PhieuXuat (
    maPhieuXuat BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    ngayXuat DATETIME NOT NULL,
    lyDoXuat VARCHAR(100) NOT NULL,
    maThuKho BIGINT NOT NULL,
    PRIMARY KEY (maPhieuXuat),
    CONSTRAINT fk_PhieuXuat_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang),
    CONSTRAINT fk_PhieuXuat_ThuKho FOREIGN KEY (maThuKho) REFERENCES NhanVien (maNhanVien)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ChiTietPhieuXuat (
    maPhieuXuat BIGINT NOT NULL,
    soSerial VARCHAR(50) NOT NULL,
    PRIMARY KEY (maPhieuXuat, soSerial),
    CONSTRAINT fk_CTPX_PhieuXuat FOREIGN KEY (maPhieuXuat) REFERENCES PhieuXuat (maPhieuXuat),
    CONSTRAINT fk_CTPX_Serial FOREIGN KEY (soSerial) REFERENCES SanPhamSerial (soSerial)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS YeuCauDoiTra (
    maYeuCau BIGINT NOT NULL AUTO_INCREMENT,
    maDonHang BIGINT NOT NULL,
    ngayYeuCau DATETIME NOT NULL,
    loaiLyDo VARCHAR(25) NOT NULL,
    lyDo VARCHAR(500) NOT NULL,
    hinhThuc VARCHAR(10) NOT NULL,
    tinhTrangMay VARCHAR(255) NOT NULL,
    conSeal BIT(1) NOT NULL DEFAULT b'0',
    phiKhauTru BIGINT NOT NULL DEFAULT 0,
    soTienHoan BIGINT NOT NULL DEFAULT 0,
    laNgoaiLe BIT(1) NOT NULL DEFAULT b'0',
    ghiChuYKienChu VARCHAR(255) NULL,
    trangThai VARCHAR(15) NOT NULL DEFAULT 'CHO_DUYET',
    maNhanVienTiepNhan BIGINT NOT NULL,
    maNguoiDuyet BIGINT NULL,
    ngayDuyet DATETIME NULL,
    lyDoTuChoi VARCHAR(255) NULL,
    PRIMARY KEY (maYeuCau),
    CONSTRAINT fk_DoiTra_DonHang FOREIGN KEY (maDonHang) REFERENCES DonHang (maDonHang),
    CONSTRAINT fk_DoiTra_TiepNhan FOREIGN KEY (maNhanVienTiepNhan) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT fk_DoiTra_Duyet FOREIGN KEY (maNguoiDuyet) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_DoiTra_loai CHECK (loaiLyDo IN ('LOI_NHA_SAN_XUAT','KHONG_DUNG_MO_TA','DOI_Y')),
    CONSTRAINT ck_DoiTra_hinhThuc CHECK (hinhThuc IN ('DOI_MAY','TRA_HANG')),
    CONSTRAINT ck_DoiTra_trangThai CHECK (trangThai IN ('CHO_DUYET','DA_DUYET','TU_CHOI','HOAN_TAT')),
    CONSTRAINT ck_DoiTra_tien CHECK (phiKhauTru >= 0 AND soTienHoan >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ChiTietDoiTra (
    maYeuCau BIGINT NOT NULL,
    soSerial VARCHAR(50) NOT NULL,
    PRIMARY KEY (maYeuCau, soSerial),
    CONSTRAINT fk_CTDT_YeuCau FOREIGN KEY (maYeuCau) REFERENCES YeuCauDoiTra (maYeuCau),
    CONSTRAINT fk_CTDT_Serial FOREIGN KEY (soSerial) REFERENCES SanPhamSerial (soSerial)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PhieuBaoHanh (
    maPhieu BIGINT NOT NULL AUTO_INCREMENT,
    soSerial VARCHAR(50) NOT NULL,
    hoTenKhach VARCHAR(100) NOT NULL,
    soDienThoaiKhach VARCHAR(15) NOT NULL,
    ngayNhan DATETIME NOT NULL,
    moTaLoi VARCHAR(500) NOT NULL,
    tinhTrangBenNgoai VARCHAR(255) NOT NULL,
    ngayHenTra DATE NOT NULL,
    lanThu INT NOT NULL,
    ngayGuiHang DATETIME NULL,
    ngayNhanTuHang DATETIME NULL,
    ngayTraKhach DATETIME NULL,
    ketQua VARCHAR(255) NULL,
    trangThai VARCHAR(15) NOT NULL DEFAULT 'DA_TIEP_NHAN',
    maNhanVien BIGINT NOT NULL,
    PRIMARY KEY (maPhieu),
    CONSTRAINT fk_BaoHanh_Serial FOREIGN KEY (soSerial) REFERENCES SanPhamSerial (soSerial),
    CONSTRAINT fk_BaoHanh_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien (maNhanVien),
    CONSTRAINT ck_BaoHanh_trangThai CHECK (trangThai IN ('DA_TIEP_NHAN','DA_GUI_HANG','DA_NHAN_VE','DA_TRA_KHACH')),
    CONSTRAINT ck_BaoHanh_lanThu CHECK (lanThu >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
