# Lược đồ quan hệ

Chuyển từ sơ đồ lớp thiết kế theo quy tắc mục 6.3: mỗi lớp thành một bảng; quan hệ 1–n đặt khoá ngoại ở phía nhiều; quan hệ n–n tách bảng trung gian (`TaiKhoanVaiTro`, `KhuyenMaiSanPham`, `ChiTietPhieuXuat`, `ChiTietDoiTra`); lớp kết hợp `ChiTietDonHang`, `ChiTietPhieuNhap`, `ChiTietDonDatHangNCC` thành bảng có khoá riêng hoặc khoá ghép. Gạch dưới là khoá chính, `#` là khoá ngoại.


## G1 · Quản trị hệ thống

- **VaiTro**(<u>maVaiTro</u>, tenVaiTro)
- **TaiKhoan**(<u>maTaiKhoan</u>, tenDangNhap, matKhau, trangThai, ngayTao, soLanSaiLienTiep, khoaDenLuc, phaiDoiMatKhau)
- **NhanVien**(<u>maNhanVien</u>, hoTen, soDienThoai, #maTaiKhoan)
- **TaiKhoanVaiTro**(#<u>maTaiKhoan</u>, #<u>maVaiTro</u>)

## G2 · Sản phẩm và khuyến mãi

- **DanhMuc**(<u>maDanhMuc</u>, tenDanhMuc, thuTu)
- **ThuongHieu**(<u>maThuongHieu</u>, tenThuongHieu, xuatXu)
- **SanPham**(<u>maSanPham</u>, tenSanPham, model, #maDanhMuc, #maThuongHieu, giaBan, giaNiemYet, thoiHanBaoHanh, canLapDat, moTa, anh, trangThai, ngayTao)
- **ThongSoKyThuat**(<u>maThongSo</u>, #maSanPham, tenThongSo, giaTri)
- **LichSuGia**(<u>maLichSu</u>, #maSanPham, giaCu, giaMoi, lyDo, duoiGiaNhap, thoiDiem, #maNhanVien)
- **KhuyenMai**(<u>maKhuyenMai</u>, tenKhuyenMai, loai, maCode, kieuGiam, mucGiam, giamToiDa, donToiThieu, quaTang, ngayBatDau, ngayKetThuc, soLuotToiDa, soLuotDaDung, trangThai, #maNguoiTao)
- **DanhGia**(<u>maDanhGia</u>, #maSanPham, #maKhachHang, #maDonHang, soSao, noiDung, ngayDanhGia)
- **KhuyenMaiSanPham**(#<u>maKhuyenMai</u>, #<u>maSanPham</u>)

## G3 · Mua hàng và thanh toán

- **KhachHang**(<u>maKhachHang</u>, hoTen, soDienThoai, email, ngayDangKy, #maTaiKhoan)
- **DiaChiGiaoHang**(<u>maDiaChi</u>, #maKhachHang, hoTenNguoiNhan, soDienThoai, diaChi, noiThanh, macDinh)
- **DonHang**(<u>maDonHang</u>, #maKhachHang, hoTenNguoiNhan, soDienThoaiNhan, email, diaChiGiao, noiThanh, canLapDat, ngayDat, ngayHoanThanh, tienHang, tienGiam, phiGiao, phiLapDat, tongTien, tienCoc, phuongThucThanhToan, trangThaiThanhToan, trangThai, #maKhuyenMai, #maNhanVienDuyet, ghiChu, lyDoHuy, soHoaDon, dongYXuLyDuLieu, dongYQuangCao)
- **ChiTietDonHang**(<u>maChiTiet</u>, #maDonHang, #maSanPham, tenSanPhamLucBan, soLuong, donGia)
- **GiaoDichThanhToan**(<u>maGiaoDich</u>, #maDonHang, loai, soTien, phuongThuc, maGiaoDichCong, trangThai, thoiDiem)

## G4 · Đơn hàng, giao lắp, đổi trả

- **LichSuTrangThaiDon**(<u>maLichSu</u>, #maDonHang, trangThaiCu, trangThaiMoi, thoiDiem, #maNhanVien, ghiChu)
- **PhieuGiaoHang**(<u>maPhieuGiao</u>, #maDonHang, loaiViec, #maNhanVienGiao, donViVanChuyen, maVanDon, ngayHen, gioBatDau, gioKetThuc, lyDoTrungLich, trangThai, lyDoKhongThanhCong, tienThuKhiGiao, #maKeToanXacNhan, thoiDiemXacNhanTien, #maNguoiPhanCong)
- **PhieuLapDat**(<u>maPhieuLapDat</u>, #maPhieuGiao, ketQuaChayThu, tinhTrangCongTrinh, phatSinh, phiLapDat, phiVatTu, khachDaKy, ngayLap)
- **YeuCauDoiTra**(<u>maYeuCau</u>, #maDonHang, ngayYeuCau, loaiLyDo, lyDo, hinhThuc, tinhTrangMay, conSeal, phiKhauTru, soTienHoan, laNgoaiLe, ghiChuYKienChu, trangThai, #maNhanVienTiepNhan, #maNguoiDuyet, ngayDuyet, lyDoTuChoi)
- **ChiTietDoiTra**(#<u>maYeuCau</u>, #<u>soSerial</u>)

## G5 · Kho, nhà cung cấp, bảo hành

- **NhaCungCap**(<u>maNhaCungCap</u>, tenNhaCungCap, soDienThoai, email, diaChi, dangHopTac)
- **DonDatHangNCC**(<u>maDonDat</u>, #maNhaCungCap, ngayLap, trangThai, #maNguoiLap, #maNguoiGui, ghiChu)
- **ChiTietDonDatHangNCC**(#<u>maDonDat</u>, #<u>maSanPham</u>, soLuong)
- **PhieuNhap**(<u>maPhieuNhap</u>, #maNhaCungCap, #maDonDat, soHoaDonNCC, ngayNhap, #maThuKho, ghiChu)
- **ChiTietPhieuNhap**(<u>maChiTietNhap</u>, #maPhieuNhap, #maSanPham, soLuongChungTu, soLuongThucNhap, donGiaNhap)
- **SanPhamSerial**(<u>soSerial</u>, #maSanPham, #maChiTietNhap, ngayNhap, ngayBan, #maDonHangBan, trangThai)
- **PhieuXuat**(<u>maPhieuXuat</u>, #maDonHang, ngayXuat, lyDoXuat, #maThuKho)
- **PhieuBaoHanh**(<u>maPhieu</u>, #soSerial, hoTenKhach, soDienThoaiKhach, ngayNhan, moTaLoi, tinhTrangBenNgoai, ngayHenTra, lanThu, ngayGuiHang, ngayNhanTuHang, ngayTraKhach, ketQua, trangThai, #maNhanVien)
- **ChiTietPhieuXuat**(#<u>maPhieuXuat</u>, #<u>soSerial</u>)
