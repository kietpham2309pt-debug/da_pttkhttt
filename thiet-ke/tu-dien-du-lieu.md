# Từ điển dữ liệu

Sinh tự động từ `website/src/main/resources/db/schema.sql` (MySQL 8, `utf8mb4_unicode_ci`). Kiểu `BIT(1)` là đúng/sai; tiền lưu `BIGINT` theo đồng.

Khoá: **PK** khoá chính, **FK → Bảng** khoá ngoại.


## G1 · Quản trị hệ thống


### VaiTro

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maVaiTro | VARCHAR(30) | PK | Không |  | Mã vai trò |
| tenVaiTro | VARCHAR(60) |  | Không |  | Tên vai trò |

### TaiKhoan

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maTaiKhoan | BIGINT | PK | Không | Tự tăng | Mã tài khoản |
| tenDangNhap | VARCHAR(50) |  | Không | UNIQUE | Tên đăng nhập, duy nhất |
| matKhau | VARCHAR(100) |  | Không |  | Mật khẩu đã băm (BCrypt) |
| trangThai | VARCHAR(20) |  | Không | CHECK trangThai IN ('DANG_DUNG','DA_KHOA'); Mặc định 'DANG_DUNG' | Trạng thái |
| ngayTao | DATETIME |  | Không |  | Thời điểm tạo |
| soLanSaiLienTiep | INT |  | Không | Mặc định 0 | Số lần nhập sai mật khẩu liên tiếp |
| khoaDenLuc | DATETIME |  | Có |  | Thời điểm hết tạm khoá đăng nhập |
| phaiDoiMatKhau | BIT(1) |  | Không | Mặc định 0 | Phải đổi mật khẩu ở lần đăng nhập tới |

### NhanVien

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maNhanVien | BIGINT | PK | Không | Tự tăng | Mã nhân viên |
| hoTen | VARCHAR(100) |  | Không |  | Họ tên |
| soDienThoai | VARCHAR(15) |  | Không |  | Số điện thoại |
| maTaiKhoan | BIGINT | FK → TaiKhoan | Không | UNIQUE | Mã tài khoản |

## G2 · Sản phẩm và khuyến mãi


### DanhMuc

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDanhMuc | VARCHAR(30) | PK | Không |  | Mã danh mục |
| tenDanhMuc | VARCHAR(100) |  | Không | UNIQUE | Tên danh mục |
| thuTu | INT |  | Không | Mặc định 0 | Thứ tự hiển thị |

### ThuongHieu

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maThuongHieu | BIGINT | PK | Không | Tự tăng | Mã thương hiệu |
| tenThuongHieu | VARCHAR(60) |  | Không | UNIQUE | Tên thương hiệu |
| xuatXu | VARCHAR(60) |  | Có |  | Xuất xứ thương hiệu |

### SanPham

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maSanPham | BIGINT | PK | Không | Tự tăng | Mã mẫu máy |
| tenSanPham | VARCHAR(200) |  | Không |  | Tên mẫu máy |
| model | VARCHAR(60) |  | Không | UNIQUE | Mã model của hãng, duy nhất |
| maDanhMuc | VARCHAR(30) | FK → DanhMuc | Không |  | Mã danh mục |
| maThuongHieu | BIGINT | FK → ThuongHieu | Không |  | Mã thương hiệu |
| giaBan | BIGINT |  | Có | CHECK giaBan IS NULL OR giaBan > 0 | Giá bán hiện tại (đã gồm VAT) |
| giaNiemYet | BIGINT |  | Có |  | Giá niêm yết của hãng |
| thoiHanBaoHanh | INT |  | Không | CHECK thoiHanBaoHanh >= 0; Mặc định 12 | Thời hạn bảo hành (tháng) |
| canLapDat | BIT(1) |  | Không | Mặc định 0 | Có cần lắp đặt tận nơi hay không |
| moTa | TEXT |  | Có |  | Mô tả chi tiết |
| anh | VARCHAR(255) |  | Có |  | Đường dẫn ảnh đại diện của mẫu máy |
| trangThai | VARCHAR(20) |  | Không | CHECK trangThai IN ('CHUA_MO_BAN','DANG_BAN','NGUNG_BAN'); Mặc định 'CHUA_MO_BAN' | Trạng thái |
| ngayTao | DATETIME |  | Không |  | Thời điểm tạo |

### ThongSoKyThuat

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maThongSo | BIGINT | PK | Không | Tự tăng | Mã thông số |
| maSanPham | BIGINT | FK → SanPham | Không | Xoá dây chuyền | Mã mẫu máy |
| tenThongSo | VARCHAR(80) |  | Không |  | Tên thông số kỹ thuật |
| giaTri | VARCHAR(120) |  | Không |  | Giá trị thông số |

### LichSuGia

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maLichSu | BIGINT | PK | Không | Tự tăng | Mã dòng lịch sử |
| maSanPham | BIGINT | FK → SanPham | Không |  | Mã mẫu máy |
| giaCu | BIGINT |  | Có |  | Giá trước khi đổi |
| giaMoi | BIGINT |  | Không |  | Giá sau khi đổi |
| lyDo | VARCHAR(255) |  | Có |  | Lý do |
| duoiGiaNhap | BIT(1) |  | Không | Mặc định 0 | Giá mới thấp hơn giá nhập gần nhất |
| thoiDiem | DATETIME |  | Không |  | Thời điểm ghi |
| maNhanVien | BIGINT | FK → NhanVien | Không |  | Mã nhân viên |

### KhuyenMai

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maKhuyenMai | BIGINT | PK | Không | Tự tăng | Mã khuyến mãi |
| tenKhuyenMai | VARCHAR(150) |  | Không |  | Tên chương trình khuyến mãi |
| loai | VARCHAR(25) |  | Không | CHECK loai IN ('GIAM_THEO_SAN_PHAM','MA_GIAM_GIA') | Loại |
| maCode | VARCHAR(30) |  | Có | UNIQUE | Mã giảm giá khách nhập, duy nhất |
| kieuGiam | VARCHAR(15) |  | Không | CHECK kieuGiam IN ('PHAN_TRAM','SO_TIEN') | Kiểu giảm: phần trăm hay số tiền |
| mucGiam | BIGINT |  | Không | CHECK mucGiam > 0 | Mức giảm (phần trăm hoặc số tiền) |
| giamToiDa | BIGINT |  | Có |  | Số tiền giảm tối đa với kiểu giảm phần trăm |
| donToiThieu | BIGINT |  | Không | Mặc định 0 | Giá trị đơn tối thiểu để áp mã giảm giá |
| quaTang | VARCHAR(150) |  | Có |  | Quà tặng kèm |
| ngayBatDau | DATE |  | Không | CHECK ngayKetThuc >= ngayBatDau | Ngày bắt đầu áp dụng |
| ngayKetThuc | DATE |  | Không | CHECK ngayKetThuc >= ngayBatDau | Ngày kết thúc áp dụng |
| soLuotToiDa | INT |  | Có |  | Số lượt dùng tối đa của mã |
| soLuotDaDung | INT |  | Không | Mặc định 0 | Số lượt mã đã được dùng |
| trangThai | VARCHAR(15) |  | Không | CHECK trangThai IN ('DANG_CHAY','TAM_DUNG'); Mặc định 'DANG_CHAY' | Trạng thái |
| maNguoiTao | BIGINT | FK → NhanVien | Không |  | Nhân viên tạo |

### DanhGia

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDanhGia | BIGINT | PK | Không | Tự tăng | Mã đánh giá |
| maSanPham | BIGINT | FK → SanPham | Không | UNIQUE (cùng maDonHang) | Mã mẫu máy |
| maKhachHang | BIGINT | FK → KhachHang | Không |  | Mã khách hàng |
| maDonHang | BIGINT | FK → DonHang | Không | UNIQUE (cùng maSanPham) | Mã đơn hàng |
| soSao | INT |  | Không | CHECK soSao BETWEEN 1 AND 5 | Số sao đánh giá (1–5) |
| noiDung | VARCHAR(1000) |  | Có |  | Nội dung |
| ngayDanhGia | DATETIME |  | Không |  | Ngày đánh giá |

## G3 · Mua hàng và thanh toán


### KhachHang

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maKhachHang | BIGINT | PK | Không | Tự tăng | Mã khách hàng |
| hoTen | VARCHAR(100) |  | Không |  | Họ tên |
| soDienThoai | VARCHAR(15) |  | Không |  | Số điện thoại |
| email | VARCHAR(100) |  | Có |  | Địa chỉ email |
| ngayDangKy | DATETIME |  | Không |  | Ngày đăng ký tài khoản |
| maTaiKhoan | BIGINT | FK → TaiKhoan | Không | UNIQUE | Mã tài khoản |

### DiaChiGiaoHang

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDiaChi | BIGINT | PK | Không | Tự tăng | Mã địa chỉ |
| maKhachHang | BIGINT | FK → KhachHang | Không |  | Mã khách hàng |
| hoTenNguoiNhan | VARCHAR(100) |  | Không |  | Họ tên người nhận hàng |
| soDienThoai | VARCHAR(15) |  | Không |  | Số điện thoại |
| diaChi | VARCHAR(255) |  | Không |  | Địa chỉ |
| noiThanh | BIT(1) |  | Không | Mặc định 1 | Giao trong nội thành (cửa hàng tự giao) |
| macDinh | BIT(1) |  | Không | Mặc định 0 | Địa chỉ mặc định |

### DonHang

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDonHang | BIGINT | PK | Không | Tự tăng | Mã đơn hàng |
| maKhachHang | BIGINT | FK → KhachHang | Có |  | Mã khách hàng |
| hoTenNguoiNhan | VARCHAR(100) |  | Không |  | Họ tên người nhận hàng |
| soDienThoaiNhan | VARCHAR(15) |  | Không |  | Số điện thoại người nhận, dùng tra đơn |
| email | VARCHAR(100) |  | Có |  | Địa chỉ email |
| diaChiGiao | VARCHAR(255) |  | Không |  | Địa chỉ giao hàng của đơn |
| noiThanh | BIT(1) |  | Không | Mặc định 1 | Giao trong nội thành (cửa hàng tự giao) |
| canLapDat | BIT(1) |  | Không | Mặc định 0 | Có cần lắp đặt tận nơi hay không |
| ngayDat | DATETIME |  | Không |  | Thời điểm đặt hàng |
| ngayHoanThanh | DATETIME |  | Có |  | Thời điểm đơn hoàn thành, mốc tính doanh thu |
| tienHang | BIGINT |  | Không | CHECK tienHang >= 0 AND tienGiam >= 0 AND tongTien >= 0 AND tienCoc >= 0 | Tổng tiền hàng |
| tienGiam | BIGINT |  | Không | CHECK tienHang >= 0 AND tienGiam >= 0 AND tongTien >= 0 AND tienCoc >= 0; Mặc định 0 | Tiền giảm do khuyến mãi |
| phiGiao | BIGINT |  | Không | Mặc định 0 | Phí giao hàng |
| phiLapDat | BIGINT |  | Không | Mặc định 0 | Phí lắp đặt |
| tongTien | BIGINT |  | Không | CHECK tienHang >= 0 AND tienGiam >= 0 AND tongTien >= 0 AND tienCoc >= 0 | Tổng tiền phải trả |
| tienCoc | BIGINT |  | Không | CHECK tienHang >= 0 AND tienGiam >= 0 AND tongTien >= 0 AND tienCoc >= 0; Mặc định 0 | Tiền cọc đã thu hoặc phải thu |
| phuongThucThanhToan | VARCHAR(20) |  | Không | CHECK phuongThucThanhToan IN ('TIEN_MAT_KHI_NHAN','CONG_THANH_TOAN') | Cách khách chọn trả tiền |
| trangThaiThanhToan | VARCHAR(20) |  | Không | CHECK trangThaiThanhToan IN ('CHUA_THANH_TOAN','DA_DAT_COC','DA_THANH_TOAN','DA_HOAN_TIEN'); Mặc định 'CHUA_THANH_TOAN' | Trạng thái thanh toán của đơn |
| trangThai | VARCHAR(25) |  | Không | CHECK trangThai IN ('CHO_XAC_NHAN','DA_XAC_NHAN','DANG_GIAO','HOAN_THANH','GIAO_KHONG_THANH_CONG','DA_HUY','DA_TRA_HANG'); Mặc định 'CHO_XAC_NHAN' | Trạng thái |
| maKhuyenMai | BIGINT | FK → KhuyenMai | Có |  | Mã khuyến mãi |
| maNhanVienDuyet | BIGINT | FK → NhanVien | Có |  | Nhân viên bán hàng xác nhận đơn |
| ghiChu | VARCHAR(500) |  | Có |  | Ghi chú |
| lyDoHuy | VARCHAR(255) |  | Có |  | Lý do huỷ đơn |
| soHoaDon | VARCHAR(30) |  | Có |  | Số hoá đơn điện tử đã phát hành |
| dongYXuLyDuLieu | BIT(1) |  | Không | CHECK dongYXuLyDuLieu = 1; Mặc định 0 | Khách đồng ý xử lý dữ liệu để giao hàng, bảo hành |
| dongYQuangCao | BIT(1) |  | Không | Mặc định 0 | Khách đồng ý nhận quảng cáo (không tích sẵn) |

### ChiTietDonHang

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maChiTiet | BIGINT | PK | Không | Tự tăng | Mã dòng chi tiết |
| maDonHang | BIGINT | FK → DonHang | Không | Xoá dây chuyền | Mã đơn hàng |
| maSanPham | BIGINT | FK → SanPham | Không |  | Mã mẫu máy |
| tenSanPhamLucBan | VARCHAR(200) |  | Không |  | Tên mẫu máy lúc bán (giữ nguyên khi tên đổi) |
| soLuong | INT |  | Không | CHECK soLuong > 0 | Số lượng |
| donGia | BIGINT |  | Không | CHECK donGia >= 0 | Đơn giá lúc đặt (không đổi khi giá bán đổi) |

### GiaoDichThanhToan

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maGiaoDich | BIGINT | PK | Không | Tự tăng | Mã giao dịch |
| maDonHang | BIGINT | FK → DonHang | Không |  | Mã đơn hàng |
| loai | VARCHAR(15) |  | Không | CHECK loai IN ('COC','THANH_TOAN','HOAN_TIEN') | Loại |
| soTien | BIGINT |  | Không | CHECK soTien > 0 | Số tiền |
| phuongThuc | VARCHAR(30) |  | Không |  | Cách trả tiền của giao dịch |
| maGiaoDichCong | VARCHAR(40) |  | Có | UNIQUE | Mã giao dịch cổng thanh toán trả về, chống ghi trùng |
| trangThai | VARCHAR(15) |  | Không | CHECK trangThai IN ('THANH_CONG','THAT_BAI') | Trạng thái |
| thoiDiem | DATETIME |  | Không |  | Thời điểm ghi |

## G4 · Đơn hàng, giao lắp, đổi trả


### LichSuTrangThaiDon

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maLichSu | BIGINT | PK | Không | Tự tăng | Mã dòng lịch sử |
| maDonHang | BIGINT | FK → DonHang | Không | Xoá dây chuyền | Mã đơn hàng |
| trangThaiCu | VARCHAR(25) |  | Có |  | Trạng thái trước khi đổi |
| trangThaiMoi | VARCHAR(25) |  | Không |  | Trạng thái sau khi đổi |
| thoiDiem | DATETIME |  | Không |  | Thời điểm ghi |
| maNhanVien | BIGINT | FK → NhanVien | Có |  | Mã nhân viên |
| ghiChu | VARCHAR(255) |  | Có |  | Ghi chú |

### PhieuGiaoHang

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maPhieuGiao | BIGINT | PK | Không | Tự tăng | Mã phiếu giao hàng |
| maDonHang | BIGINT | FK → DonHang | Không |  | Mã đơn hàng |
| loaiViec | VARCHAR(15) |  | Không | CHECK loaiViec IN ('GIAO','GIAO_LAP','KHAO_SAT') | Loại việc: giao, giao kèm lắp, khảo sát |
| maNhanVienGiao | BIGINT | FK → NhanVien | Có | CHECK maNhanVienGiao IS NOT NULL OR donViVanChuyen IS NOT NULL | Nhân viên giao hàng – kỹ thuật |
| donViVanChuyen | VARCHAR(80) |  | Có | CHECK maNhanVienGiao IS NOT NULL OR donViVanChuyen IS NOT NULL | Tên đơn vị vận chuyển (đơn đi tỉnh) |
| maVanDon | VARCHAR(40) |  | Có |  | Mã vận đơn của đơn vị vận chuyển |
| ngayHen | DATE |  | Không |  | Ngày hẹn giao, lắp |
| gioBatDau | TIME |  | Không | CHECK gioKetThuc > gioBatDau | Giờ bắt đầu khung hẹn |
| gioKetThuc | TIME |  | Không | CHECK gioKetThuc > gioBatDau | Giờ kết thúc khung hẹn |
| lyDoTrungLich | VARCHAR(255) |  | Có |  | Lý do vẫn xếp khi trùng giờ |
| trangThai | VARCHAR(25) |  | Không | CHECK trangThai IN ('DA_PHAN_CONG','DA_GIAO','KHONG_THANH_CONG'); Mặc định 'DA_PHAN_CONG' | Trạng thái |
| lyDoKhongThanhCong | VARCHAR(255) |  | Có |  | Lý do giao không thành công |
| tienThuKhiGiao | BIGINT |  | Không | Mặc định 0 | Tiền nhân viên thu khi giao |
| maKeToanXacNhan | BIGINT | FK → NhanVien | Có |  | Kế toán xác nhận tiền thu |
| thoiDiemXacNhanTien | DATETIME |  | Có |  | Thời điểm kế toán xác nhận đã nhận tiền |
| maNguoiPhanCong | BIGINT | FK → NhanVien | Không |  | Quản lý phân công |

### PhieuLapDat

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maPhieuLapDat | BIGINT | PK | Không | Tự tăng | Mã biên bản lắp đặt |
| maPhieuGiao | BIGINT | FK → PhieuGiaoHang | Không | UNIQUE | Mã phiếu giao hàng |
| ketQuaChayThu | VARCHAR(255) |  | Không |  | Kết quả chạy thử sau khi lắp |
| tinhTrangCongTrinh | VARCHAR(255) |  | Có |  | Tình trạng chỗ lắp đặt |
| phatSinh | VARCHAR(255) |  | Có |  | Việc, vật tư phát sinh khi lắp |
| phiLapDat | BIGINT |  | Không | CHECK phiLapDat >= 0 AND phiVatTu >= 0; Mặc định 0 | Phí lắp đặt |
| phiVatTu | BIGINT |  | Không | CHECK phiLapDat >= 0 AND phiVatTu >= 0; Mặc định 0 | Phí vật tư phát sinh |
| khachDaKy | BIT(1) |  | Không | Mặc định 0 | Khách đã ký biên bản |
| ngayLap | DATETIME |  | Không |  | Thời điểm lập |

### YeuCauDoiTra

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maYeuCau | BIGINT | PK | Không | Tự tăng | Mã yêu cầu đổi trả |
| maDonHang | BIGINT | FK → DonHang | Không |  | Mã đơn hàng |
| ngayYeuCau | DATETIME |  | Không |  | Thời điểm khách yêu cầu |
| loaiLyDo | VARCHAR(25) |  | Không | CHECK loaiLyDo IN ('LOI_NHA_SAN_XUAT','KHONG_DUNG_MO_TA','DOI_Y') | Loại lý do đổi trả |
| lyDo | VARCHAR(500) |  | Không |  | Lý do |
| hinhThuc | VARCHAR(10) |  | Không | CHECK hinhThuc IN ('DOI_MAY','TRA_HANG') | Hình thức xử lý: đổi máy hay trả hàng |
| tinhTrangMay | VARCHAR(255) |  | Không |  | Tình trạng máy khi nhận lại |
| conSeal | BIT(1) |  | Không | Mặc định 0 | Máy còn niêm phong của hãng khi khách yêu cầu đổi trả |
| phiKhauTru | BIGINT |  | Không | CHECK phiKhauTru >= 0 AND soTienHoan >= 0; Mặc định 0 | Phí khấu trừ khi đổi trả |
| soTienHoan | BIGINT |  | Không | CHECK phiKhauTru >= 0 AND soTienHoan >= 0; Mặc định 0 | Số tiền hoàn cho khách |
| laNgoaiLe | BIT(1) |  | Không | Mặc định 0 | Yêu cầu ngoài quy định, phải xin ý kiến chủ |
| ghiChuYKienChu | VARCHAR(255) |  | Có |  | Ý kiến của chủ cửa hàng với trường hợp ngoại lệ |
| trangThai | VARCHAR(15) |  | Không | CHECK trangThai IN ('CHO_DUYET','DA_DUYET','TU_CHOI','HOAN_TAT'); Mặc định 'CHO_DUYET' | Trạng thái |
| maNhanVienTiepNhan | BIGINT | FK → NhanVien | Không |  | Nhân viên bán hàng tiếp nhận |
| maNguoiDuyet | BIGINT | FK → NhanVien | Có |  | Nhân viên duyệt |
| ngayDuyet | DATETIME |  | Có |  | Thời điểm duyệt |
| lyDoTuChoi | VARCHAR(255) |  | Có |  | Lý do từ chối yêu cầu |

## G5 · Kho, nhà cung cấp, bảo hành


### NhaCungCap

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maNhaCungCap | BIGINT | PK | Không | Tự tăng | Mã nhà cung cấp |
| tenNhaCungCap | VARCHAR(150) |  | Không | UNIQUE | Tên nhà cung cấp |
| soDienThoai | VARCHAR(15) |  | Không |  | Số điện thoại |
| email | VARCHAR(100) |  | Có |  | Địa chỉ email |
| diaChi | VARCHAR(255) |  | Có |  | Địa chỉ |
| dangHopTac | BIT(1) |  | Không | Mặc định 1 | Cửa hàng còn hợp tác với nhà cung cấp |

### DonDatHangNCC

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDonDat | BIGINT | PK | Không | Tự tăng | Mã đơn đặt hàng |
| maNhaCungCap | BIGINT | FK → NhaCungCap | Không |  | Mã nhà cung cấp |
| ngayLap | DATETIME |  | Không |  | Thời điểm lập |
| trangThai | VARCHAR(15) |  | Không | CHECK trangThai IN ('DE_XUAT','DA_GUI','DA_NHAN','DA_HUY'); Mặc định 'DE_XUAT' | Trạng thái |
| maNguoiLap | BIGINT | FK → NhanVien | Không |  | Thủ kho lập đề xuất |
| maNguoiGui | BIGINT | FK → NhanVien | Có |  | Quản lý gửi đơn cho nhà cung cấp |
| ghiChu | VARCHAR(255) |  | Có |  | Ghi chú |

### ChiTietDonDatHangNCC

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maDonDat | BIGINT | PK, FK → DonDatHangNCC | Không | Xoá dây chuyền | Mã đơn đặt hàng |
| maSanPham | BIGINT | PK, FK → SanPham | Không |  | Mã mẫu máy |
| soLuong | INT |  | Không | CHECK soLuong > 0 | Số lượng |

### PhieuNhap

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maPhieuNhap | BIGINT | PK | Không | Tự tăng | Mã phiếu nhập |
| maNhaCungCap | BIGINT | FK → NhaCungCap | Không |  | Mã nhà cung cấp |
| maDonDat | BIGINT | FK → DonDatHangNCC | Có |  | Mã đơn đặt hàng |
| soHoaDonNCC | VARCHAR(30) |  | Có |  | Số hoá đơn của nhà cung cấp |
| ngayNhap | DATETIME |  | Không |  | Thời điểm nhập kho |
| maThuKho | BIGINT | FK → NhanVien | Không |  | Thủ kho lập phiếu |
| ghiChu | VARCHAR(255) |  | Có |  | Ghi chú |

### ChiTietPhieuNhap

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maChiTietNhap | BIGINT | PK | Không | Tự tăng | Dòng phiếu nhập đã nhập chiếc máy |
| maPhieuNhap | BIGINT | FK → PhieuNhap | Không |  | Mã phiếu nhập |
| maSanPham | BIGINT | FK → SanPham | Không |  | Mã mẫu máy |
| soLuongChungTu | INT |  | Không | CHECK soLuongChungTu > 0 AND soLuongThucNhap >= 0 | Số lượng theo chứng từ (mẫu 01-VT) |
| soLuongThucNhap | INT |  | Không | CHECK soLuongChungTu > 0 AND soLuongThucNhap >= 0 | Số lượng thực nhập (mẫu 01-VT) |
| donGiaNhap | BIGINT |  | Không | CHECK donGiaNhap > 0 | Đơn giá nhập của mẫu máy trên phiếu nhập |

### SanPhamSerial

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| soSerial | VARCHAR(50) | PK | Không |  | Số serial hãng in trên chiếc máy |
| maSanPham | BIGINT | FK → SanPham | Không |  | Mã mẫu máy |
| maChiTietNhap | BIGINT | FK → ChiTietPhieuNhap | Không |  | Dòng phiếu nhập đã nhập chiếc máy |
| ngayNhap | DATETIME |  | Không |  | Thời điểm nhập kho |
| ngayBan | DATETIME |  | Có |  | Ngày bán (giao xong), mốc tính bảo hành và đổi trả |
| maDonHangBan | BIGINT | FK → DonHang | Có |  | Đơn hàng đã bán chiếc máy |
| trangThai | VARCHAR(25) |  | Không | CHECK trangThai IN ('TRONG_KHO','DA_XUAT','DA_BAN','DANG_BAO_HANH','TRA_VE_KHO','LOI_CHO_GUI_HANG'); Mặc định 'TRONG_KHO' | Trạng thái |

### PhieuXuat

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maPhieuXuat | BIGINT | PK | Không | Tự tăng | Mã phiếu xuất |
| maDonHang | BIGINT | FK → DonHang | Không |  | Mã đơn hàng |
| ngayXuat | DATETIME |  | Không |  | Thời điểm xuất kho |
| lyDoXuat | VARCHAR(100) |  | Không |  | Lý do xuất kho |
| maThuKho | BIGINT | FK → NhanVien | Không |  | Thủ kho lập phiếu |

### PhieuBaoHanh

| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |
|---|---|---|---|---|---|
| maPhieu | BIGINT | PK | Không | Tự tăng | Mã phiếu tiếp nhận bảo hành |
| soSerial | VARCHAR(50) | FK → SanPhamSerial | Không |  | Số serial hãng in trên chiếc máy |
| hoTenKhach | VARCHAR(100) |  | Không |  | Họ tên khách mang máy tới bảo hành |
| soDienThoaiKhach | VARCHAR(15) |  | Không |  | Số điện thoại khách bảo hành |
| ngayNhan | DATETIME |  | Không |  | Thời điểm tiếp nhận máy |
| moTaLoi | VARCHAR(500) |  | Không |  | Mô tả lỗi khách báo |
| tinhTrangBenNgoai | VARCHAR(255) |  | Không |  | Tình trạng bên ngoài của máy khi nhận |
| ngayHenTra | DATE |  | Không |  | Ngày hẹn trả máy cho khách |
| lanThu | INT |  | Không | CHECK lanThu >= 1 | Lần bảo hành thứ mấy của chiếc máy |
| ngayGuiHang | DATETIME |  | Có |  | Ngày gửi máy cho trung tâm bảo hành hãng |
| ngayNhanTuHang | DATETIME |  | Có |  | Ngày nhận máy về từ hãng |
| ngayTraKhach | DATETIME |  | Có |  | Ngày trả máy cho khách |
| ketQua | VARCHAR(255) |  | Có |  | Kết quả sửa chữa của hãng |
| trangThai | VARCHAR(15) |  | Không | CHECK trangThai IN ('DA_TIEP_NHAN','DA_GUI_HANG','DA_NHAN_VE','DA_TRA_KHACH'); Mặc định 'DA_TIEP_NHAN' | Trạng thái |
| maNhanVien | BIGINT | FK → NhanVien | Không |  | Mã nhân viên |
