# Bảng mô tả lớp

Sinh từ cùng nguồn với lớp thực thể của website, nên luôn khớp sơ đồ lớp và CSDL. Thuộc tính tham chiếu (mối kết hợp) ghi ở cột Kiểu bằng tên lớp.


## G1 · Quản trị hệ thống


### VaiTro

Vai trò trên website (khách hàng, nhân viên bán hàng, thủ kho, nhân viên giao hàng – kỹ thuật, quản lý cửa hàng, kế toán, quản trị hệ thống).

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maVaiTro | String | Mã vai trò |
| tenVaiTro | String | Tên vai trò |

### TaiKhoan

Tài khoản đăng nhập của nhân viên hoặc khách hàng; một tài khoản kiêm được nhiều vai.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maTaiKhoan | long | Mã tài khoản |
| tenDangNhap | String | Tên đăng nhập, duy nhất |
| matKhau | String | Mật khẩu đã băm (BCrypt) |
| trangThai | TrangThaiTaiKhoan | Trạng thái |
| ngayTao | LocalDateTime | Thời điểm tạo |
| soLanSaiLienTiep | int | Số lần nhập sai mật khẩu liên tiếp |
| khoaDenLuc | LocalDateTime | Thời điểm hết tạm khoá đăng nhập |
| phaiDoiMatKhau | boolean | Phải đổi mật khẩu ở lần đăng nhập tới |
| vaiTro | Set<VaiTro> | Danh sách VaiTro liên quan |

Phương thức: `kiemTraMatKhau(matKhau : String)` → boolean; `khoa()` → void; `moKhoa()` → void; `ghiSaiMatKhau()` → void; `doiMatKhau(matKhauMoi : String)` → void; `coVai(maVaiTro : String)` → boolean.

### NhanVien

Người làm việc trong cửa hàng giả định; ghi người thực hiện từng nghiệp vụ.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maNhanVien | long | Mã nhân viên |
| hoTen | String | Họ tên |
| soDienThoai | String | Số điện thoại |
| taiKhoan | TaiKhoan | Mã tài khoản |

Phương thức: `capNhatThongTin(hoTen : String, soDienThoai : String)` → void.

## G2 · Sản phẩm và khuyến mãi


### DanhMuc

Nhóm hàng: bếp điện từ, bếp hồng ngoại, máy hút mùi, máy rửa chén, lò nướng, lò vi sóng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maDanhMuc | String | Mã danh mục |
| tenDanhMuc | String | Tên danh mục |
| thuTu | int | Thứ tự hiển thị |

Phương thức: `coSanPham()` → boolean.

### ThuongHieu

Hãng sản xuất.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maThuongHieu | long | Mã thương hiệu |
| tenThuongHieu | String | Tên thương hiệu |
| xuatXu | String | Xuất xứ thương hiệu |

### SanPham

Một mẫu máy (thông tin, giá, thời hạn bảo hành). Khác với chiếc máy cụ thể `SanPhamSerial`.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maSanPham | long | Mã mẫu máy |
| tenSanPham | String | Tên mẫu máy |
| model | String | Mã model của hãng, duy nhất |
| danhMuc | DanhMuc | Mã danh mục |
| thuongHieu | ThuongHieu | Mã thương hiệu |
| giaBan | long | Giá bán hiện tại (đã gồm VAT) |
| giaNiemYet | long | Giá niêm yết của hãng |
| thoiHanBaoHanh | int | Thời hạn bảo hành (tháng) |
| canLapDat | boolean | Có cần lắp đặt tận nơi hay không |
| moTa | String | Mô tả chi tiết |
| anh | String | Đường dẫn ảnh đại diện của mẫu máy |
| trangThai | TrangThaiSanPham | Trạng thái |
| ngayTao | LocalDateTime | Thời điểm tạo |
| thongSo | List<ThongSoKyThuat> | Danh sách ThongSoKyThuat liên quan |

Phương thức: `capNhatGia(giaMoi : long, lyDo : String, nhanVien : NhanVien)` → LichSuGia; `moBan()` → void; `ngungBan()` → void; `soMayTrongKho()` → int; `giaSauKhuyenMai()` → long.

### ThongSoKyThuat

Một dòng thông số của mẫu máy (công suất, kích thước khoét đá…), dùng để lọc và so sánh.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maThongSo | long | Mã thông số |
| sanPham | SanPham | Mã mẫu máy |
| tenThongSo | String | Tên thông số kỹ thuật |
| giaTri | String | Giá trị thông số |

### LichSuGia

Mỗi lần đổi giá: ai đổi, giá cũ, giá mới, lý do, có dưới giá nhập không (YC-SP-09, YC-HT-05).

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maLichSu | long | Mã dòng lịch sử |
| sanPham | SanPham | Mã mẫu máy |
| giaCu | long | Giá trước khi đổi |
| giaMoi | long | Giá sau khi đổi |
| lyDo | String | Lý do |
| duoiGiaNhap | boolean | Giá mới thấp hơn giá nhập gần nhất |
| thoiDiem | LocalDateTime | Thời điểm ghi |
| nhanVien | NhanVien | Mã nhân viên |

### KhuyenMai

Chương trình giảm theo sản phẩm hoặc mã giảm giá; mỗi đơn áp tối đa một mã.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maKhuyenMai | long | Mã khuyến mãi |
| tenKhuyenMai | String | Tên chương trình khuyến mãi |
| loai | LoaiKhuyenMai | Loại |
| maCode | String | Mã giảm giá khách nhập, duy nhất |
| kieuGiam | KieuGiam | Kiểu giảm: phần trăm hay số tiền |
| mucGiam | long | Mức giảm (phần trăm hoặc số tiền) |
| giamToiDa | long | Số tiền giảm tối đa với kiểu giảm phần trăm |
| donToiThieu | long | Giá trị đơn tối thiểu để áp mã giảm giá |
| quaTang | String | Quà tặng kèm |
| ngayBatDau | LocalDate | Ngày bắt đầu áp dụng |
| ngayKetThuc | LocalDate | Ngày kết thúc áp dụng |
| soLuotToiDa | int | Số lượt dùng tối đa của mã |
| soLuotDaDung | int | Số lượt mã đã được dùng |
| trangThai | TrangThaiKhuyenMai | Trạng thái |
| nguoiTao | NhanVien | Nhân viên tạo |
| sanPham | Set<SanPham> | Danh sách SanPham liên quan |

Phương thức: `conHieuLuc(ngay : LocalDate)` → boolean; `tinhTienGiam(tienHang : long)` → long; `tamDung()` → void; `tangLuotDung()` → void.

### DanhGia

Đánh giá của khách đã nhận hàng cho một mẫu máy trong một đơn.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maDanhGia | long | Mã đánh giá |
| sanPham | SanPham | Mã mẫu máy |
| khachHang | KhachHang | Mã khách hàng |
| donHang | DonHang | Mã đơn hàng |
| soSao | int | Số sao đánh giá (1–5) |
| noiDung | String | Nội dung |
| ngayDanhGia | LocalDateTime | Ngày đánh giá |

## G3 · Mua hàng và thanh toán


### KhachHang

Khách đã đăng ký tài khoản. Khách vãng lai không có bản ghi này.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maKhachHang | long | Mã khách hàng |
| hoTen | String | Họ tên |
| soDienThoai | String | Số điện thoại |
| email | String | Địa chỉ email |
| ngayDangKy | LocalDateTime | Ngày đăng ký tài khoản |
| taiKhoan | TaiKhoan | Mã tài khoản |

Phương thức: `capNhatHoSo(hoTen : String, email : String)` → void; `diaChiMacDinh()` → DiaChiGiaoHang.

### DiaChiGiaoHang

Sổ địa chỉ của khách hàng đã đăng ký.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maDiaChi | long | Mã địa chỉ |
| khachHang | KhachHang | Mã khách hàng |
| hoTenNguoiNhan | String | Họ tên người nhận hàng |
| soDienThoai | String | Số điện thoại |
| diaChi | String | Địa chỉ |
| noiThanh | boolean | Giao trong nội thành (cửa hàng tự giao) |
| macDinh | boolean | Địa chỉ mặc định |

Phương thức: `datMacDinh()` → void.

### DonHang

Một lần mua của khách (kể cả khách vãng lai), mang trạng thái đơn và trạng thái thanh toán.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maDonHang | long | Mã đơn hàng |
| khachHang | KhachHang | Mã khách hàng |
| hoTenNguoiNhan | String | Họ tên người nhận hàng |
| soDienThoaiNhan | String | Số điện thoại người nhận, dùng tra đơn |
| email | String | Địa chỉ email |
| diaChiGiao | String | Địa chỉ giao hàng của đơn |
| noiThanh | boolean | Giao trong nội thành (cửa hàng tự giao) |
| canLapDat | boolean | Có cần lắp đặt tận nơi hay không |
| ngayDat | LocalDateTime | Thời điểm đặt hàng |
| ngayHoanThanh | LocalDateTime | Thời điểm đơn hoàn thành, mốc tính doanh thu |
| tienHang | long | Tổng tiền hàng |
| tienGiam | long | Tiền giảm do khuyến mãi |
| phiGiao | long | Phí giao hàng |
| phiLapDat | long | Phí lắp đặt |
| tongTien | long | Tổng tiền phải trả |
| tienCoc | long | Tiền cọc đã thu hoặc phải thu |
| phuongThucThanhToan | PhuongThucThanhToan | Cách khách chọn trả tiền |
| trangThaiThanhToan | TrangThaiThanhToan | Trạng thái thanh toán của đơn |
| trangThai | TrangThaiDonHang | Trạng thái |
| khuyenMai | KhuyenMai | Mã khuyến mãi |
| nhanVienDuyet | NhanVien | Nhân viên bán hàng xác nhận đơn |
| ghiChu | String | Ghi chú |
| lyDoHuy | String | Lý do huỷ đơn |
| soHoaDon | String | Số hoá đơn điện tử đã phát hành |
| dongYXuLyDuLieu | boolean | Khách đồng ý xử lý dữ liệu để giao hàng, bảo hành |
| dongYQuangCao | boolean | Khách đồng ý nhận quảng cáo (không tích sẵn) |
| chiTiet | List<ChiTietDonHang> | Danh sách ChiTietDonHang liên quan |

Phương thức: `tinhTongTien()` → long; `tinhTienCoc()` → long; `doiTrangThai(moi : TrangThaiDonHang, nhanVien : NhanVien)` → void; `huy(lyDo : String)` → void; `apKhuyenMai(km : KhuyenMai)` → void.

### ChiTietDonHang

Một dòng mẫu máy trong đơn, giữ đơn giá và tên lúc bán.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maChiTiet | long | Mã dòng chi tiết |
| donHang | DonHang | Mã đơn hàng |
| sanPham | SanPham | Mã mẫu máy |
| tenSanPhamLucBan | String | Tên mẫu máy lúc bán (giữ nguyên khi tên đổi) |
| soLuong | int | Số lượng |
| donGia | long | Đơn giá lúc đặt (không đổi khi giá bán đổi) |

Phương thức: `thanhTien()` → long.

### GiaoDichThanhToan

Một lần thu cọc, thu tiền hoặc hoàn tiền qua cổng thanh toán hoặc tại cửa hàng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maGiaoDich | long | Mã giao dịch |
| donHang | DonHang | Mã đơn hàng |
| loai | LoaiGiaoDich | Loại |
| soTien | long | Số tiền |
| phuongThuc | String | Cách trả tiền của giao dịch |
| maGiaoDichCong | String | Mã giao dịch cổng thanh toán trả về, chống ghi trùng |
| trangThai | TrangThaiGiaoDich | Trạng thái |
| thoiDiem | LocalDateTime | Thời điểm ghi |

## G4 · Đơn hàng, giao lắp, đổi trả


### LichSuTrangThaiDon

Mỗi lần đơn đổi trạng thái: từ, sang, ai, lúc nào.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maLichSu | long | Mã dòng lịch sử |
| donHang | DonHang | Mã đơn hàng |
| trangThaiCu | TrangThaiDonHang | Trạng thái trước khi đổi |
| trangThaiMoi | TrangThaiDonHang | Trạng thái sau khi đổi |
| thoiDiem | LocalDateTime | Thời điểm ghi |
| nhanVien | NhanVien | Mã nhân viên |
| ghiChu | String | Ghi chú |

### PhieuGiaoHang

Lịch giao, lắp hoặc khảo sát cho một đơn: người phụ trách hoặc đơn vị vận chuyển, ngày giờ hẹn, kết quả, tiền thu khi giao.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maPhieuGiao | long | Mã phiếu giao hàng |
| donHang | DonHang | Mã đơn hàng |
| loaiViec | LoaiViecGiao | Loại việc: giao, giao kèm lắp, khảo sát |
| nhanVienGiao | NhanVien | Nhân viên giao hàng – kỹ thuật |
| donViVanChuyen | String | Tên đơn vị vận chuyển (đơn đi tỉnh) |
| maVanDon | String | Mã vận đơn của đơn vị vận chuyển |
| ngayHen | LocalDate | Ngày hẹn giao, lắp |
| gioBatDau | LocalTime | Giờ bắt đầu khung hẹn |
| gioKetThuc | LocalTime | Giờ kết thúc khung hẹn |
| lyDoTrungLich | String | Lý do vẫn xếp khi trùng giờ |
| trangThai | TrangThaiPhieuGiao | Trạng thái |
| lyDoKhongThanhCong | String | Lý do giao không thành công |
| tienThuKhiGiao | long | Tiền nhân viên thu khi giao |
| keToanXacNhan | NhanVien | Kế toán xác nhận tiền thu |
| thoiDiemXacNhanTien | LocalDateTime | Thời điểm kế toán xác nhận đã nhận tiền |
| nguoiPhanCong | NhanVien | Quản lý phân công |

Phương thức: `trungLichVoi(khac : PhieuGiaoHang)` → boolean; `baoDaGiao(tienThu : long)` → void; `baoKhongThanhCong(lyDo : String)` → void; `xacNhanTienThu(keToan : NhanVien)` → void.

### PhieuLapDat

Biên bản lắp đặt: kết quả chạy thử, phát sinh, phí, chữ ký khách.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maPhieuLapDat | long | Mã biên bản lắp đặt |
| phieuGiao | PhieuGiaoHang | Mã phiếu giao hàng |
| ketQuaChayThu | String | Kết quả chạy thử sau khi lắp |
| tinhTrangCongTrinh | String | Tình trạng chỗ lắp đặt |
| phatSinh | String | Việc, vật tư phát sinh khi lắp |
| phiLapDat | long | Phí lắp đặt |
| phiVatTu | long | Phí vật tư phát sinh |
| khachDaKy | boolean | Khách đã ký biên bản |
| ngayLap | LocalDateTime | Thời điểm lập |

Phương thức: `tongPhi()` → long.

### YeuCauDoiTra

Yêu cầu đổi máy hoặc trả hàng của khách, người duyệt, số tiền hoàn.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maYeuCau | long | Mã yêu cầu đổi trả |
| donHang | DonHang | Mã đơn hàng |
| ngayYeuCau | LocalDateTime | Thời điểm khách yêu cầu |
| loaiLyDo | LoaiLyDoDoiTra | Loại lý do đổi trả |
| lyDo | String | Lý do |
| hinhThuc | HinhThucDoiTra | Hình thức xử lý: đổi máy hay trả hàng |
| tinhTrangMay | String | Tình trạng máy khi nhận lại |
| conSeal | boolean | Máy còn niêm phong của hãng khi khách yêu cầu đổi trả |
| phiKhauTru | long | Phí khấu trừ khi đổi trả |
| soTienHoan | long | Số tiền hoàn cho khách |
| laNgoaiLe | boolean | Yêu cầu ngoài quy định, phải xin ý kiến chủ |
| ghiChuYKienChu | String | Ý kiến của chủ cửa hàng với trường hợp ngoại lệ |
| trangThai | TrangThaiDoiTra | Trạng thái |
| nhanVienTiepNhan | NhanVien | Nhân viên bán hàng tiếp nhận |
| nguoiDuyet | NhanVien | Nhân viên duyệt |
| ngayDuyet | LocalDateTime | Thời điểm duyệt |
| lyDoTuChoi | String | Lý do từ chối yêu cầu |
| serial | Set<SanPhamSerial> | Danh sách SanPhamSerial liên quan |

Phương thức: `trongHanDoiTra(ngay : LocalDate)` → boolean; `duyet(quanLy : NhanVien)` → void; `tuChoi(quanLy : NhanVien, lyDo : String)` → void; `hoanTien(soTien : long)` → void.

## G5 · Kho, nhà cung cấp, bảo hành


### NhaCungCap

Hãng hoặc nhà phân phối bán hàng cho cửa hàng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maNhaCungCap | long | Mã nhà cung cấp |
| tenNhaCungCap | String | Tên nhà cung cấp |
| soDienThoai | String | Số điện thoại |
| email | String | Địa chỉ email |
| diaChi | String | Địa chỉ |
| dangHopTac | boolean | Cửa hàng còn hợp tác với nhà cung cấp |

Phương thức: `ngungHopTac()` → void.

### DonDatHangNCC

Đề xuất và đơn đặt hàng gửi nhà cung cấp.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maDonDat | long | Mã đơn đặt hàng |
| nhaCungCap | NhaCungCap | Mã nhà cung cấp |
| ngayLap | LocalDateTime | Thời điểm lập |
| trangThai | TrangThaiDonDat | Trạng thái |
| nguoiLap | NhanVien | Thủ kho lập đề xuất |
| nguoiGui | NhanVien | Quản lý gửi đơn cho nhà cung cấp |
| ghiChu | String | Ghi chú |
| chiTiet | List<ChiTietDonDatHangNCC> | Danh sách ChiTietDonDatHangNCC liên quan |

Phương thức: `guiNhaCungCap(quanLy : NhanVien)` → void; `huy()` → void.

### ChiTietDonDatHangNCC

Một dòng mẫu máy và số lượng trong đơn đặt hàng.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| donDat | DonDatHangNCC | Thuộc khoá: DonDatHangNCC |
| sanPham | SanPham | Thuộc khoá: SanPham |
| soLuong | int | Số lượng |

### PhieuNhap

Phiếu nhập kho theo mẫu 01-VT.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maPhieuNhap | long | Mã phiếu nhập |
| nhaCungCap | NhaCungCap | Mã nhà cung cấp |
| donDat | DonDatHangNCC | Mã đơn đặt hàng |
| soHoaDonNCC | String | Số hoá đơn của nhà cung cấp |
| ngayNhap | LocalDateTime | Thời điểm nhập kho |
| thuKho | NhanVien | Thủ kho lập phiếu |
| ghiChu | String | Ghi chú |
| chiTiet | List<ChiTietPhieuNhap> | Danh sách ChiTietPhieuNhap liên quan |

Phương thức: `themDong(sp : SanPham, soChungTu : int, soThucNhap : int, donGia : long)` → ChiTietPhieuNhap.

### ChiTietPhieuNhap

Một dòng mẫu máy trong phiếu nhập: số theo chứng từ, số thực nhập, đơn giá nhập.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maChiTietNhap | long | Dòng phiếu nhập đã nhập chiếc máy |
| phieuNhap | PhieuNhap | Mã phiếu nhập |
| sanPham | SanPham | Mã mẫu máy |
| soLuongChungTu | int | Số lượng theo chứng từ (mẫu 01-VT) |
| soLuongThucNhap | int | Số lượng thực nhập (mẫu 01-VT) |
| donGiaNhap | long | Đơn giá nhập của mẫu máy trên phiếu nhập |

Phương thức: `ghiSerial(dsSerial : List<String>)` → void.

### SanPhamSerial

Một chiếc máy cụ thể theo số serial; vòng đời từ nhập kho tới bán, bảo hành, trả về.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| soSerial | String | Số serial hãng in trên chiếc máy |
| sanPham | SanPham | Mã mẫu máy |
| chiTietNhap | ChiTietPhieuNhap | Dòng phiếu nhập đã nhập chiếc máy |
| ngayNhap | LocalDateTime | Thời điểm nhập kho |
| ngayBan | LocalDateTime | Ngày bán (giao xong), mốc tính bảo hành và đổi trả |
| donHangBan | DonHang | Đơn hàng đã bán chiếc máy |
| trangThai | TrangThaiSerial | Trạng thái |

Phương thức: `hanBaoHanh()` → LocalDate; `conBaoHanh(ngay : LocalDate)` → boolean; `doiTrangThai(moi : TrangThaiSerial)` → void.

### PhieuXuat

Phiếu xuất kho cho một đơn, ghi đúng những chiếc máy đã xuất.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maPhieuXuat | long | Mã phiếu xuất |
| donHang | DonHang | Mã đơn hàng |
| ngayXuat | LocalDateTime | Thời điểm xuất kho |
| lyDoXuat | String | Lý do xuất kho |
| thuKho | NhanVien | Thủ kho lập phiếu |
| serial | Set<SanPhamSerial> | Danh sách SanPhamSerial liên quan |

Phương thức: `chonSerial(may : SanPhamSerial)` → void.

### PhieuBaoHanh

Phiếu tiếp nhận bảo hành một chiếc máy, theo dõi gửi hãng và trả khách.

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| maPhieu | long | Mã phiếu tiếp nhận bảo hành |
| serial | SanPhamSerial | Số serial hãng in trên chiếc máy |
| hoTenKhach | String | Họ tên khách mang máy tới bảo hành |
| soDienThoaiKhach | String | Số điện thoại khách bảo hành |
| ngayNhan | LocalDateTime | Thời điểm tiếp nhận máy |
| moTaLoi | String | Mô tả lỗi khách báo |
| tinhTrangBenNgoai | String | Tình trạng bên ngoài của máy khi nhận |
| ngayHenTra | LocalDate | Ngày hẹn trả máy cho khách |
| lanThu | int | Lần bảo hành thứ mấy của chiếc máy |
| ngayGuiHang | LocalDateTime | Ngày gửi máy cho trung tâm bảo hành hãng |
| ngayNhanTuHang | LocalDateTime | Ngày nhận máy về từ hãng |
| ngayTraKhach | LocalDateTime | Ngày trả máy cho khách |
| ketQua | String | Kết quả sửa chữa của hãng |
| trangThai | TrangThaiBaoHanh | Trạng thái |
| nhanVien | NhanVien | Mã nhân viên |

Phương thức: `guiHang(ngay : LocalDateTime)` → void; `nhanVe(ketQua : String)` → void; `traKhach()` → void.
