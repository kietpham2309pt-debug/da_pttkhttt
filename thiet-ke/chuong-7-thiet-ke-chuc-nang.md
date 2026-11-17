# Chương 7. Thiết kế chức năng hệ thống

Bản 1.0 (14/11/2026) · nhóm 4 · đối chiếu với mã nguồn website ở `website/` (Spring Boot 3, Thymeleaf, Spring Data JPA, MySQL 8)

Chương này hiện thực hoá 10 use case ★ của danh mục chung (`phan-tich/chung/danh-muc-use-case.md`) trên kiến trúc 3 tầng.
Mọi tên lớp, phương thức, URL trong chương lấy đúng từ mã nguồn; tham số dài được rút gọn bằng dấu `…`.

## 7.1 Kiến trúc 3 tầng

![Kiến trúc 3 tầng của website](so-do/kien-truc-3-tang.png)

*Hình 7.1: Kiến trúc 3 tầng của website*

Người dùng chỉ làm việc với tầng giao diện. Tầng giao diện không gọi thẳng cơ sở dữ liệu mà chuyển yêu cầu cho lớp nghiệp vụ;
lớp nghiệp vụ kiểm tra quy tắc rồi mới gọi tầng truy cập dữ liệu. Nhờ vậy cùng một quy tắc (ví dụ bảng chuyển trạng thái đơn hàng)
chỉ viết một lần ở `DonHangBUS` và được dùng lại ở mọi màn hình đổi trạng thái đơn.

**Bảng 7.1: Ánh xạ tầng sang package và thư mục trong `website/src/main`**

| Tầng | Package / thư mục | Nội dung | Stereotype trên sơ đồ |
|---|---|---|---|
| Giao diện: trang | `resources/templates/khach/`, `templates/quan-tri/g1…g5/`, `templates/dang-nhap.html`, `doi-mat-khau.html`, `thong-diep.html` | 61 trang Thymeleaf; khung chung ở `templates/layout/khung.html`, `layout/quan-tri.html` | `«boundary»` |
| Giao diện: điều khiển trang | `java/…/web/` (`ChungController`), `web/khach/` (8 controller), `web/quantri/g1…g5/` (17 controller) | Nhận yêu cầu HTTP, đọc tham số, gọi BUS, chọn trang trả về | `«boundary»` |
| Nghiệp vụ | `java/…/nghiepvu/` (`DonHangBUS`, `SerialBUS`, `TaiKhoanBUS`, `XuLyDangNhap`, `GioHang`), `nghiepvu/khach/`, `nghiepvu/g1`, `g2`, `g4`, `g5` | Lớp `…BUS` giữ quy tắc nghiệp vụ; lỗi nghiệp vụ ném `LoiNghiepVu` để controller hiện lên trang | `«control»` |
| Bảo mật | `java/…/cauhinh/BaoMat.java`, `web/quantri/g1/ChanTaiKhoanBiKhoa.java` | Cấu hình Spring Security: trang đăng nhập, phân quyền URL theo vai, băm mật khẩu BCrypt | `«control»` |
| Truy cập dữ liệu | `java/…/khodulieu/` (27 interface `…DAO`), `khodulieu/g12`, `g4`, `g5`, `khach` (32 interface `…TruyVan…`) | Interface kế thừa `JpaRepository` của Spring Data JPA; câu truy vấn viết bằng JPQL trong `@Query` | `«DAO»` |
| Thực thể | `java/…/thucthe/` | 27 lớp `@Entity` ứng với 27 lớp thiết kế ở chương 6, 17 kiểu liệt kê trạng thái | `«entity»` |
| Cơ sở dữ liệu | `resources/db/schema.sql`, `resources/db/data.sql` | 31 bảng MySQL, dữ liệu mẫu | |

(`…` là `vn/edu/huit/pttk/diengiadung`.)

**Spring Security.** Lớp cấu hình `BaoMat` khai báo bộ lọc `chuoiLoc()`: trang `/dang-nhap` dùng biểu mẫu tự viết, tên đăng nhập và mật khẩu
được Spring Security kiểm qua `TaiKhoanBUS.loadUserByUsername()` (lớp này cài interface `UserDetailsService`), so mật khẩu bằng `BCryptPasswordEncoder`
nên CSDL chỉ lưu mật khẩu đã băm. Kết quả đăng nhập được chuyển cho `XuLyDangNhap` để đếm số lần sai, khoá 15 phút, bắt đổi mật khẩu lần đầu.
Phân quyền làm theo URL: ví dụ `/quan-tri/bao-cao/**` chỉ cho vai `QUAN_LY`, `KE_TOAN`; `/quan-tri/kho/**` cho `THU_KHO`, `QUAN_LY`.
Những chỗ cần phân quyền mịn hơn (chỉ Quản lý cửa hàng được đặt giá, chỉ Thủ kho lập phiếu nhập) controller kiểm thêm bằng `TaiKhoanBUS.coVai()` hoặc `QuyenG5.batBuoc()`.
Bộ lọc `ChanTaiKhoanBiKhoa` chạy ở mỗi yêu cầu: tài khoản bị quản trị khoá giữa chừng thì phiên đang mở bị huỷ ngay.

**Phiên làm việc (giỏ hàng).** Giỏ hàng không lưu CSDL (đã chốt ở chương 5). Hai lớp `GioHang` và `PhienKhach` mang chú thích `@SessionScope`:
mỗi phiên trình duyệt có một đối tượng riêng. `GioHang` giữ mã mẫu máy, số lượng, có lắp đặt hay không; `PhienKhach` giữ bản nháp phiếu đặt hàng,
mã lượt đặt chống bấm "Đặt hàng" hai lần, danh sách đơn khách được xem, danh sách so sánh và các yêu cầu thanh toán đang chờ cổng trả kết quả.
Đặt hàng xong, `DatHangBUS.datHang()` gọi `GioHang.xoaHet()`.

**Cổng thanh toán mô phỏng.** Website chưa ký hợp đồng với cổng thanh toán thật nên nhóm làm một cổng mô phỏng chạy ngay trong website:
`ThanhToanController.moCong()` xin `PhienKhach.capYeuCau()` một mã giao dịch dạng `MOCK-<mã đơn>-…` có hạn 15 phút rồi hiện trang `khach/thanh-toan.html`
đóng vai trang của cổng (chọn chuyển khoản/QR hoặc thẻ, nút "Thanh toán thành công", "Thanh toán thất bại").
Kết quả gửi về `ketQuaCong()`; controller chỉ nhận khi mã giao dịch có trong phiên, khớp mã đơn và còn hạn, sau đó `DonHangKhachBUS.ghiKetQuaThanhToan()`
ghi `GiaoDichThanhToan` và cập nhật trạng thái thanh toán của đơn. Cùng một mã giao dịch cổng gửi lại nhiều lần thì chỉ ghi một lần.
Khi dùng cổng thật chỉ phải thay trang `thanh-toan.html` và phần xác minh kết quả (chữ ký của cổng), lớp nghiệp vụ giữ nguyên.

## 7.2 Hiện thực hoá use case

Với mỗi use case ★ có hai sơ đồ: sơ đồ lớp hiện thực hoá (chỉ các lớp và phương thức thật sự tham gia) và sơ đồ tuần tự hệ thống
(thứ tự đối tượng: tác nhân → trang → controller → BUS → DAO; thông điệp đánh số, trả về nét đứt, luồng thay thế chính bằng `alt`, lặp bằng `loop`).
Thông điệp có dạng `đối_tượng.phương_thức()` trên một mũi tên tự gọi (như `phien.dungMaLuotDat()`) là lời gọi sang một đối tượng phụ không vẽ thành đường sống riêng cho đỡ rộng.

### 7.2.1 UC-HT-01 Đăng nhập

Đăng nhập dùng cơ chế biểu mẫu của Spring Security; phần riêng của nhóm là `TaiKhoanBUS` (tìm tài khoản, báo khoá) và `XuLyDangNhap` (xử lý sau khi kiểm).

![Sơ đồ lớp hiện thực hoá UC-HT-01](so-do/lht-ht01.png)

*Hình 7.2: Sơ đồ lớp hiện thực hoá UC-HT-01 Đăng nhập*

![Sơ đồ tuần tự UC-HT-01](so-do/tt-ht-ht01.png)

*Hình 7.3: Sơ đồ tuần tự hệ thống UC-HT-01 Đăng nhập*

Quy tắc nghiệp vụ nằm ở: `TaiKhoanBUS.loadUserByUsername()` (tài khoản `DA_KHOA` thì `disabled`, `khoaDenLuc` còn hiệu lực thì `accountLocked`, vai trò thành quyền `ROLE_…`);
`XuLyDangNhap.onAuthenticationFailure()` (sai liên tiếp `SO_LAN_SAI_TOI_DA = 5` lần thì đặt `khoaDenLuc` sau `PHUT_KHOA = 15` phút, báo chung "Tên đăng nhập hoặc mật khẩu không đúng");
`XuLyDangNhap.onAuthenticationSuccess()` (đặt lại bộ đếm, `phaiDoiMatKhau` thì chuyển `/doi-mat-khau?lanDau`, khách về `/`, nhân viên về `/quan-tri`).

### 7.2.2 UC-HT-04 Xem báo cáo doanh thu

Báo cáo không có bảng riêng: `BaoCaoBUS` tổng hợp từ đơn hoàn thành, giao dịch thanh toán và phiếu giao hàng.

![Sơ đồ lớp hiện thực hoá UC-HT-04](so-do/lht-ht04.png)

*Hình 7.4: Sơ đồ lớp hiện thực hoá UC-HT-04 Xem báo cáo doanh thu*

![Sơ đồ tuần tự UC-HT-04](so-do/tt-ht-ht04.png)

*Hình 7.5: Sơ đồ tuần tự hệ thống UC-HT-04 Xem báo cáo doanh thu*

Quy tắc nghiệp vụ nằm ở `BaoCaoBUS`: `kiemTraKhoangNgay()` (ngày bắt đầu sau ngày kết thúc thì báo "Khoảng ngày không hợp lệ."); `doanhThu()` lấy đơn `HOAN_THANH`
theo `ngayHoanThanh` (doanh thu tính theo ngày giao xong, QĐ2 của BUC-07), tiền hàng tính theo `ChiTietDonHang.donGia` lúc đặt, trừ các giao dịch `HOAN_TIEN`,
tách riêng tiền thu khi giao mà kế toán chưa xác nhận (luồng 4a), khoảng ngày quá 12 tháng thì ép nhóm theo tháng; `xuatCsv()` tạo tệp CSV có khoảng ngày và thời điểm xuất.

### 7.2.3 UC-SP-02 Quản lý sản phẩm và thông số kỹ thuật

Nhân viên bán hàng thêm, sửa mẫu máy cùng danh sách thông số trên một biểu mẫu; thông số là thành phần của mẫu máy nên lưu cùng một lần `save(sp)`.

![Sơ đồ lớp hiện thực hoá UC-SP-02](so-do/lht-sp02.png)

*Hình 7.6: Sơ đồ lớp hiện thực hoá UC-SP-02 Quản lý sản phẩm và thông số kỹ thuật*

![Sơ đồ tuần tự UC-SP-02](so-do/tt-ht-sp02.png)

*Hình 7.7: Sơ đồ tuần tự hệ thống UC-SP-02 (thêm mẫu máy)*

Quy tắc nghiệp vụ nằm ở `QuanTriSanPhamBUS.luu()`: ô bắt buộc (tên, model, danh mục, thương hiệu, bảo hành 0–120 tháng), model không trùng
(`findFirstByModelIgnoreCase`, trùng thì trả mã mẫu máy đã có để mở sửa), thông số có đơn vị `(W)`, `(lít)`, `(mm)`, `(dB)` phải là số,
bếp kiểu lắp âm phải có kích thước khoét đá; mẫu máy mới luôn ở `CHUA_MO_BAN`, ô giá chỉ xem. Ngừng bán, mở bán ở `ngungBan()`, `moBan()`
(mở bán bắt buộc đã có giá); đặt giá ở `capNhatGia()` (UC-SP-03) ghi `LichSuGia`.

### 7.2.4 UC-SP-05 Tìm kiếm, lọc sản phẩm

Điều kiện lọc gom vào đối tượng `LocSanPham` (gắn thẳng từ tham số URL), `SanPhamKhachBUS` dựng câu truy vấn động bằng `Specification` của Spring Data JPA.

![Sơ đồ lớp hiện thực hoá UC-SP-05](so-do/lht-sp05.png)

*Hình 7.8: Sơ đồ lớp hiện thực hoá UC-SP-05 Tìm kiếm, lọc sản phẩm*

![Sơ đồ tuần tự UC-SP-05](so-do/tt-ht-sp05.png)

*Hình 7.9: Sơ đồ tuần tự hệ thống UC-SP-05 Tìm kiếm, lọc sản phẩm*

Quy tắc nghiệp vụ nằm ở: `LocSanPham.chuanHoa()` (giá "từ" lớn hơn giá "đến" thì đổi chỗ, luồng 5b; cách sắp xếp lạ thì về "mới nhất");
`SanPhamKhachBUS.timKiem()` (chỉ mẫu máy `DANG_BAN` đã có giá, tên hoặc model chứa từ khoá, 12 mẫu mỗi trang, trang vượt quá thì lùi về trang cuối);
`GiaBUS.dongThe()` (giá sau khuyến mãi theo chương trình `GIAM_THEO_SAN_PHAM` đang chạy, chọn mức giảm tốt nhất; tồn khả dụng = số chiếc `TRONG_KHO`
trừ số đang giữ cho đơn chờ, đã xác nhận chưa xuất kho, bằng 0 thì hiện "Tạm hết hàng").

### 7.2.5 UC-MH-04 Đặt hàng

Đặt hàng đi qua ba trang `dat-hang.html` → `xac-nhan.html` → `hoan-tat.html`; giỏ và bản nháp phiếu nằm trong phiên, chỉ bước cuối mới ghi CSDL.

![Sơ đồ lớp hiện thực hoá UC-MH-04](so-do/lht-mh04.png)

*Hình 7.10: Sơ đồ lớp hiện thực hoá UC-MH-04 Đặt hàng*

![Sơ đồ tuần tự UC-MH-04](so-do/tt-ht-mh04.png)

*Hình 7.11: Sơ đồ tuần tự hệ thống UC-MH-04 Đặt hàng*

Quy tắc nghiệp vụ nằm ở: `PhieuDatHang.kiemTra()` (ô bắt buộc, số điện thoại 10 chữ số bắt đầu bằng 0, email đúng dạng); `DatHangBUS.tinh()` cùng `GiaBUS`
(giá sau khuyến mãi, `kiemMaGiamGia()` cho UC-MH-08 – mỗi đơn một mã, mã mới thay mã cũ; `phiLapDat()` miễn phí khi đơn trên 5 triệu, dưới thì 250 000 đ, chỉ đơn nội thành;
`tienCoc()` 10 % khi tổng trên 10 triệu); `DatHangBUS.datHang()` (bắt buộc hai ô đồng ý, kiểm lại tồn kho và so tổng tiền với `tongDaXem` khách đã xem – khác thì báo luồng 6b,
lưu giá lúc đặt vào `ChiTietDonHang`, trạng thái `CHO_XAC_NHAN`, `CHUA_THANH_TOAN`, tăng lượt dùng mã giảm giá, lưu địa chỉ khi khách chọn, làm trống giỏ);
`DonHangBUS.ghiTaoMoi()` ghi dòng `LichSuTrangThaiDon` đầu tiên; `PhienKhach.dungMaLuotDat()` chống tạo hai đơn khi bấm hai lần. Cả `datHang()` chạy trong một giao dịch (`@Transactional`) nên lỗi thì không có đơn dở dang.

### 7.2.6 UC-MH-05 Thanh toán trực tuyến (cổng mô phỏng)

Trang `thanh-toan.html` vừa là trang chọn cách trả vừa đóng vai trang của cổng thanh toán (mục 7.1).

![Sơ đồ lớp hiện thực hoá UC-MH-05](so-do/lht-mh05.png)

*Hình 7.12: Sơ đồ lớp hiện thực hoá UC-MH-05 Thanh toán trực tuyến*

![Sơ đồ tuần tự UC-MH-05](so-do/tt-ht-mh05.png)

*Hình 7.13: Sơ đồ tuần tự hệ thống UC-MH-05 Thanh toán trực tuyến*

Quy tắc nghiệp vụ nằm ở `DonHangKhachBUS`: `coTheThanhToan()` (đơn đang `CHO_XAC_NHAN` hoặc `DA_XAC_NHAN`, còn nợ, chưa trả đủ, chọn cổng thanh toán hoặc đang chờ cọc – sai thì "Đơn không cần thanh toán", luồng 1a);
`soTienPhaiTra()` (tiền cọc còn thiếu hoặc toàn bộ phần còn lại khi khách chọn trả hết, luồng 1b); `ghiKetQuaThanhToan()` (mã giao dịch cổng đã ghi thì bỏ qua – chống ghi trùng;
ghi `GiaoDichThanhToan` loại `COC` hoặc `THANH_TOAN`, thành công thì đơn sang `DA_DAT_COC` hoặc `DA_THANH_TOAN`); `doiSangTienMat()` (đơn không thuộc diện cọc được đổi sang trả khi nhận, luồng 3a).
Phần xác minh kết quả cổng (mã có trong phiên, đúng đơn, chưa quá 15 phút – luồng 3b, 4a) nằm ở `ThanhToanController.ketQuaCong()` cùng `PhienKhach.layYeuCau()`.

### 7.2.7 UC-DH-01 Duyệt, xác nhận đơn hàng

Nhân viên bán hàng mở đơn chờ, gọi khách rồi bấm "Xác nhận đơn"; mọi lần đổi trạng thái đơn đều đi qua `DonHangBUS.doiTrangThai()`.

![Sơ đồ lớp hiện thực hoá UC-DH-01](so-do/lht-dh01.png)

*Hình 7.14: Sơ đồ lớp hiện thực hoá UC-DH-01 Duyệt, xác nhận đơn hàng*

![Sơ đồ tuần tự UC-DH-01](so-do/tt-ht-dh01.png)

*Hình 7.15: Sơ đồ tuần tự hệ thống UC-DH-01 Duyệt, xác nhận đơn hàng*

Quy tắc nghiệp vụ nằm ở `DonHangQuanTriBUS`: `danhSach()` tính số phút chờ và tô đơn chờ quá `PHUT_GOI_TOI_DA = 30` phút; `xacNhan()` kiểm đơn vẫn ở trạng thái lúc mở
(`trangThaiLucMo`, luồng 4b), mỗi mẫu máy còn đủ chiếc `TRONG_KHO` sau khi trừ số đang giữ cho đơn đã xác nhận khác (luồng 4a), ghi `nhanVienDuyet`;
`goiLaiSau()` đếm số lần gọi (luồng 3a, tối đa `SO_LAN_GOI_TOI_DA = 3`); `huy()` bắt buộc lý do trong danh sách, không huỷ đơn đã có phiếu xuất, ghi giao dịch hoàn cọc (luồng 1b, 3b).
Bảng chuyển trạng thái (Chờ xác nhận → Đã xác nhận hoặc Đã huỷ…) nằm ở `DonHangBUS.duocDoi()`; `doiTrangThai()` lưu đơn và ghi `LichSuTrangThaiDon` kèm nhân viên, thời điểm.

### 7.2.8 UC-DH-06 Xử lý đổi trả, hoàn tiền

Một yêu cầu đổi trả đi qua ba vai: nhân viên bán hàng lập, quản lý cửa hàng duyệt, kế toán ghi hoàn tiền; sơ đồ tuần tự chia ba đoạn tương ứng.

![Sơ đồ lớp hiện thực hoá UC-DH-06](so-do/lht-dh06.png)

*Hình 7.16: Sơ đồ lớp hiện thực hoá UC-DH-06 Xử lý đổi trả, hoàn tiền*

![Sơ đồ tuần tự UC-DH-06](so-do/tt-ht-dh06.png)

*Hình 7.17: Sơ đồ tuần tự hệ thống UC-DH-06 Xử lý đổi trả, hoàn tiền*

Quy tắc nghiệp vụ nằm ở `DoiTraBUS`: `donDeTao()` (chỉ đơn `HOAN_THANH`); `tao()` (serial phải thuộc đơn và đang `DA_BAN` – luồng 2a; đã có yêu cầu chờ duyệt thì mở yêu cầu đó – luồng 2b);
`doiChieu()` (đổi ý: trong `HAN_DOI_Y = 7` ngày, còn seal, chưa lắp; lỗi nhà sản xuất: trong `HAN_LOI_NHA_SAN_XUAT = 30` ngày, quá hạn thì gợi ý bảo hành; không đúng mô tả: luôn nhận);
`phiGoiY()` (đổi ý mà trả hàng thì gợi ý khấu trừ 10 %); `duyet()` (yêu cầu ngoại lệ phải có ý kiến chủ cửa hàng, phí khấu trừ không vượt giá máy, chiếc máy sang `TRA_VE_KHO` qua `SerialBUS.doiTrangThai()`);
`tuChoi()` (bắt buộc lý do, không được từ chối lý do "không đúng mô tả" – luồng 6c); `hoanTien()` (chỉ hoàn theo cách khách đã trả – luồng 9b, khách trả toàn bộ đơn thì đơn sang `DA_TRA_HANG`, thanh toán `DA_HOAN_TIEN`).
Bảng chuyển trạng thái chiếc máy nằm ở `SerialBUS`.

### 7.2.9 UC-KB-03 Lập phiếu nhập kho, ghi số serial

Thủ kho chọn đơn đặt hàng nhà cung cấp đang chờ nhập, hệ thống chép sẵn số còn thiếu và đơn giá gần nhất; mỗi chiếc máy thực nhập một dòng serial.

![Sơ đồ lớp hiện thực hoá UC-KB-03](so-do/lht-kb03.png)

*Hình 7.18: Sơ đồ lớp hiện thực hoá UC-KB-03 Lập phiếu nhập kho, ghi số serial*

![Sơ đồ tuần tự UC-KB-03](so-do/tt-ht-kb03.png)

*Hình 7.19: Sơ đồ tuần tự hệ thống UC-KB-03 Lập phiếu nhập kho, ghi số serial*

Quy tắc nghiệp vụ nằm ở `PhieuNhapBUS`: `taoNhap()` (chỉ nhận hàng cho đơn đặt đã gửi nhà cung cấp – QĐ1 của BUC-05; đánh dấu mẫu giá trị cao từ `MUC_GIA_TRI_CAO = 15 000 000` đ để nhắc bật thử);
`luu()` (số thực nhập không lớn hơn số chứng từ – luồng 3b, không vượt số còn phải nhận của đơn đặt, số serial đúng bằng số thực nhập – luồng 5a, serial không trùng trên phiếu và trong hệ thống – luồng 4a,
có chênh lệch thì bắt ghi tình trạng hàng – luồng 3a, serial đặt riêng cho đơn khách – luồng 4b); phiếu, chi tiết và serial `TRONG_KHO` được `persist` trong một giao dịch, lỗi trùng lúc ghi thì không lưu gì.
`DonDatBUS.capNhatSauNhap()` chuyển đơn đặt sang "Đã nhận" khi nhận đủ.

### 7.2.10 UC-KB-06 Tiếp nhận bảo hành

Tiếp nhận bảo hành luôn bắt đầu bằng tra cứu (`«include»` UC-KB-07): `BaoHanhBUS.traCuu()` dùng chung cho trang nhân viên và tính hạn bảo hành của chiếc máy.

![Sơ đồ lớp hiện thực hoá UC-KB-06](so-do/lht-kb06.png)

*Hình 7.20: Sơ đồ lớp hiện thực hoá UC-KB-06 Tiếp nhận bảo hành*

![Sơ đồ tuần tự UC-KB-06](so-do/tt-ht-kb06.png)

*Hình 7.21: Sơ đồ tuần tự hệ thống UC-KB-06 Tiếp nhận bảo hành*

Quy tắc nghiệp vụ nằm ở `BaoHanhBUS`: `traCuu()` (chỉ máy do cửa hàng bán – luồng 2a; hạn = ngày bán + thời hạn bảo hành của mẫu + tổng số ngày máy nằm ở hãng – QĐ6);
`tiepNhan()` (máy đang có phiếu mở thì không tạo phiếu mới – luồng 3d, máy phải `DA_BAN`, đã bảo hành `SO_LAN_TOI_DA = 3` lần thì chuyển sang đổi trả – luồng 3c,
hết hạn chỉ nhận khi khách đồng ý sửa có phí – luồng 3a, ghi `lanThu`, máy sang `DANG_BAO_HANH`); `canhBao()` nhắc lần bảo hành thứ 3 (luồng 3b);
`guiHang()` chỉ cho phiếu loại gửi hãng (lỗi lắp đặt, bảo hành tại nhà không vào danh sách gửi – luồng 4a, 5a); `nhanVe()`, `traKhach()` (máy về `DA_BAN`, giữ nguyên `ngayBan`).

## 7.3 Thiết kế giao diện

Giao diện dùng chung khung đầu trang, chân trang (`layout/khung.html`) cho phía khách và khung menu quản trị theo vai (`layout/quan-tri.html`).
Menu quản trị chỉ hiện mục mà vai của tài khoản được dùng; mở thẳng URL không đúng vai thì chuyển tới `/khong-co-quyen`.

**Bảng 7.2: Các màn hình của website**

| Màn hình (template) | URL | Use case | Vai dùng |
|---|---|---|---|
| Trang chủ (`khach/trang-chu.html`) | `/` | UC-SP-05, UC-SP-06 | Khách vãng lai, Khách hàng |
| Danh sách, lọc sản phẩm (`khach/danh-sach.html`) | `/san-pham?danhMuc=&tuKhoa=&thuongHieu=&giaTu=&giaDen=&sapXep=&trang=` | UC-SP-05 | Khách vãng lai |
| Chi tiết sản phẩm (`khach/chi-tiet.html`) | `/san-pham/{ma}` | UC-SP-06 | Khách vãng lai |
| So sánh sản phẩm (`khach/so-sanh.html`) | `/so-sanh` | UC-SP-06 | Khách vãng lai |
| Giỏ hàng (`khach/gio-hang.html`) | `/gio-hang` | UC-MH-03 | Khách vãng lai |
| Đặt hàng (`khach/dat-hang.html`) | `/dat-hang` | UC-MH-04, UC-MH-08 | Khách vãng lai |
| Xác nhận đơn (`khach/xac-nhan.html`) | `/dat-hang/xac-nhan` | UC-MH-04 | Khách vãng lai |
| Đặt hàng thành công (`khach/hoan-tat.html`) | `/don-hang/hoan-tat/{ma}` | UC-MH-04 | Khách vãng lai |
| Cổng thanh toán mô phỏng (`khach/thanh-toan.html`) | `/thanh-toan/{maDon}` | UC-MH-05 | Khách vãng lai, Cổng thanh toán |
| Kết quả thanh toán (`khach/ket-qua-thanh-toan.html`) | `/thanh-toan/{maDon}/ket-qua?maCong=` | UC-MH-05 | Khách vãng lai |
| Tra cứu đơn (`khach/tra-cuu-don.html`) | `/tra-cuu-don` | UC-MH-06 | Khách vãng lai |
| Chi tiết đơn của khách (`khach/chi-tiet-don.html`) | `/tra-cuu-don/{ma}` | UC-MH-06, UC-MH-07 | Khách vãng lai |
| Tra cứu bảo hành (`khach/bao-hanh.html`) | `/bao-hanh?cach=serial&soSerial=` | UC-KB-07 | Khách vãng lai |
| Đăng ký (`khach/dang-ky.html`) | `/dang-ky` | UC-MH-01 | Khách vãng lai |
| Tài khoản, sổ địa chỉ (`khach/tai-khoan.html`) | `/tai-khoan` | UC-MH-02 | Khách hàng |
| Đánh giá sản phẩm (`khach/danh-gia.html`) | `/danh-gia` | UC-SP-07 | Khách hàng |
| Chính sách bảo mật, điều khoản (`khach/bao-mat.html`, `khach/dieu-khoan.html`) | `/chinh-sach/bao-mat`, `/chinh-sach/dieu-khoan` | UC-MH-04 (nội dung khách đồng ý) | Khách vãng lai |
| Đăng nhập (`dang-nhap.html`) | `/dang-nhap` | UC-HT-01 | Nhân viên, Khách hàng |
| Đổi mật khẩu (`doi-mat-khau.html`) | `/doi-mat-khau` | UC-HT-06 | Nhân viên, Khách hàng |
| Thông báo, không có quyền (`thong-diep.html`) | `/khong-co-quyen` và sau một số thao tác | Dùng chung | Mọi vai |
| Trang lỗi (`error.html`) | `/error` (Spring Boot tự chuyển) | Dùng chung | Mọi vai |
| Tổng quan quản trị (`quan-tri/g1/tong-quan.html`) | `/quan-tri` | UC-HT-01 (trang sau đăng nhập) | 6 vai nhân viên |
| Tài khoản nhân viên (`quan-tri/g1/tai-khoan.html`, `tai-khoan-form.html`) | `/quan-tri/tai-khoan`, `/them`, `/{ma}/sua` | UC-HT-02, UC-HT-03 | Quản trị hệ thống |
| Báo cáo doanh thu (`quan-tri/g1/doanh-thu.html`) | `/quan-tri/bao-cao/doanh-thu?tuNgay=&denNgay=&nhom=`; xuất `/doanh-thu/xuat` | UC-HT-04 | Quản lý cửa hàng, Kế toán |
| Báo cáo tồn kho, bảo hành (`quan-tri/g1/ton-kho.html`) | `/quan-tri/bao-cao/ton-kho` | UC-HT-05 | Quản lý cửa hàng, Kế toán |
| Danh mục, thương hiệu (`quan-tri/g2/danh-muc.html`, `danh-muc-form.html`, `thuong-hieu-form.html`) | `/quan-tri/danh-muc`, `/quan-tri/danh-muc/them`, `/quan-tri/thuong-hieu/them`, `…/{ma}/sua` | UC-SP-01 | Quản lý cửa hàng |
| Sản phẩm (`quan-tri/g2/san-pham.html`, `san-pham-form.html`) | `/quan-tri/san-pham`, `/them`, `/{ma}/sua` | UC-SP-02 | Nhân viên bán hàng, Quản lý cửa hàng |
| Giá bán (`quan-tri/g2/gia.html`) | `/quan-tri/san-pham/{ma}/gia` | UC-SP-03 | Quản lý cửa hàng (nhân viên bán hàng chỉ xem) |
| Khuyến mãi (`quan-tri/g2/khuyen-mai.html`, `khuyen-mai-form.html`) | `/quan-tri/khuyen-mai`, `/them`, `/{ma}/sua` | UC-SP-04 | Quản lý cửa hàng |
| Đơn hàng (`quan-tri/g4/don-hang.html`) | `/quan-tri/don-hang?trangThai=&tuKhoa=` | UC-DH-01 | Nhân viên bán hàng, Quản lý cửa hàng |
| Chi tiết đơn hàng (`quan-tri/g4/don-hang-chi-tiet.html`) | `/quan-tri/don-hang/{ma}` | UC-DH-01 | Nhân viên bán hàng, Quản lý cửa hàng |
| Lịch giao lắp (`quan-tri/g4/giao-hang.html`) | `/quan-tri/giao-hang?ngay=&nv=` | UC-DH-02, UC-DH-03 | Quản lý cửa hàng, NV giao hàng – kỹ thuật, NV bán hàng |
| Phân công giao lắp (`quan-tri/g4/phan-cong.html`) | `/quan-tri/giao-hang/phan-cong?don={ma}`, `/phieu/{ma}/doi-lich` | UC-DH-02 | Quản lý cửa hàng |
| Phiếu giao hàng, biên bản lắp (`quan-tri/g4/phieu-giao.html`) | `/quan-tri/giao-hang/phieu/{ma}` | UC-DH-03, UC-DH-05 | NV giao hàng – kỹ thuật, NV bán hàng |
| Xác nhận tiền thu (`quan-tri/g4/tien-thu.html`) | `/quan-tri/tien-thu` | UC-DH-04 | Kế toán |
| Đổi trả (`quan-tri/g4/doi-tra.html`, `doi-tra-tao.html`, `doi-tra-chi-tiet.html`) | `/quan-tri/doi-tra`, `/tao`, `/{ma}` | UC-DH-06 | NV bán hàng, Quản lý cửa hàng, Kế toán |
| Nhà cung cấp (`quan-tri/g5/nha-cung-cap.html`, `nha-cung-cap-form.html`) | `/quan-tri/kho/nha-cung-cap`, `/them`, `/{ma}/sua` | UC-KB-01 | Thủ kho, Quản lý cửa hàng |
| Đơn đặt hàng NCC (`quan-tri/g5/don-dat.html`, `don-dat-lap.html`, `don-dat-chi-tiet.html`) | `/quan-tri/kho/don-dat`, `/lap`, `/{ma}` | UC-KB-02 | Thủ kho (lập), Quản lý cửa hàng (gửi) |
| Phiếu nhập (`quan-tri/g5/phieu-nhap.html`, `phieu-nhap-chon-don.html`, `phieu-nhap-lap.html`, `phieu-nhap-chi-tiet.html`) | `/quan-tri/kho/phieu-nhap`, `/lap`, `/lap?donDat={ma}`, `/{ma}` | UC-KB-03 | Thủ kho |
| Phiếu xuất (`quan-tri/g5/phieu-xuat.html`, `phieu-xuat-lap.html`, `phieu-xuat-chi-tiet.html`) | `/quan-tri/kho/phieu-xuat`, `/lap?loai=&ma=`, `/{ma}` | UC-KB-04 | Thủ kho |
| Tồn kho, kiểm kê (`quan-tri/g5/ton-kho.html`) | `/quan-tri/kho/ton-kho` | UC-KB-05 | Thủ kho, Quản lý cửa hàng |
| Nhận lại máy (`quan-tri/g5/nhan-lai.html`) | `/quan-tri/kho/nhan-lai` | UC-KB-08 | Thủ kho |
| Bảo hành (`quan-tri/g5/bao-hanh.html`) | `/quan-tri/bao-hanh?loai=serial&q=` | UC-KB-06, UC-KB-07 | NV bán hàng, Thủ kho |
| Tiếp nhận bảo hành (`quan-tri/g5/bao-hanh-tiep-nhan.html`) | `/quan-tri/bao-hanh/tiep-nhan?serial=` | UC-KB-06 | NV bán hàng |
| Phiếu bảo hành (`quan-tri/g5/bao-hanh-chi-tiet.html`) | `/quan-tri/bao-hanh/{ma}` | UC-KB-06 | NV bán hàng, Thủ kho |

Các tệp `layout/khung.html`, `layout/quan-tri.html`, `khach/manh.html` là mảnh dùng chung (đầu trang, menu, banner); `mau-khach.html`, `quan-tri/mau.html` là khuôn trống để tạo trang mới. Chúng không phải màn hình riêng.

### Màn hình tiêu biểu

![Màn hình trang chủ](man-hinh/trang-chu.png)

*Hình 7.22: Màn hình trang chủ*

![Màn hình danh sách, lọc sản phẩm](man-hinh/danh-sach-loc.png)

*Hình 7.23: Màn hình danh sách và bộ lọc sản phẩm (UC-SP-05)*

![Màn hình chi tiết sản phẩm](man-hinh/chi-tiet-san-pham.png)

*Hình 7.24: Màn hình chi tiết sản phẩm*

![Màn hình giỏ hàng](man-hinh/gio-hang.png)

*Hình 7.25: Màn hình giỏ hàng*

![Màn hình xác nhận đơn hàng](man-hinh/dat-hang-xac-nhan.png)

*Hình 7.26: Màn hình xác nhận đơn trước khi đặt (UC-MH-04)*

![Màn hình cổng thanh toán mô phỏng](man-hinh/cong-thanh-toan.png)

*Hình 7.27: Màn hình cổng thanh toán mô phỏng (UC-MH-05)*

![Màn hình tra cứu đơn](man-hinh/tra-cuu-don.png)

*Hình 7.28: Màn hình kết quả tra cứu đơn hàng (UC-MH-06)*

![Màn hình tra cứu bảo hành](man-hinh/tra-cuu-bao-hanh.png)

*Hình 7.29: Màn hình tra cứu bảo hành của khách (UC-KB-07)*

![Màn hình tổng quan quản trị](man-hinh/tong-quan-quan-tri.png)

*Hình 7.30: Màn hình tổng quan quản trị*

![Màn hình chi tiết đơn hàng](man-hinh/don-hang-chi-tiet.png)

*Hình 7.31: Màn hình chi tiết đơn hàng chờ xác nhận (UC-DH-01)*

![Màn hình phân công giao lắp](man-hinh/phan-cong-giao-lap.png)

*Hình 7.32: Màn hình phân công giao hàng, lắp đặt (UC-DH-02)*

![Màn hình lập phiếu nhập](man-hinh/phieu-nhap.png)

*Hình 7.33: Màn hình lập phiếu nhập kho, ghi serial (UC-KB-03)*

![Màn hình bảo hành](man-hinh/bao-hanh.png)

*Hình 7.34: Màn hình tiếp nhận bảo hành (UC-KB-06)*

![Màn hình báo cáo doanh thu](man-hinh/bao-cao-doanh-thu.png)

*Hình 7.35: Màn hình báo cáo doanh thu (UC-HT-04)*

### Danh sách ảnh màn hình

Chạy website (`website/`: bật MySQL rồi `mvnw.cmd spring-boot:run`), mở `http://localhost:8090`. Mọi tài khoản mẫu trong `data.sql` dùng mật khẩu **`123456`**.
Chụp cửa sổ trình duyệt rộng khoảng 1366 px, lưu PNG vào `thiet-ke/man-hinh/` đúng tên dưới đây. Ảnh có thao tác ghi dữ liệu (đặt đơn, lập phiếu) nên chụp theo thứ tự trong bảng.

| STT | Tên tệp | URL | Tài khoản | Cách dựng màn hình |
|---|---|---|---|---|
| 1 | `trang-chu.png` | `/` | Không đăng nhập | Chụp nửa trên trang (banner, sản phẩm mới, khuyến mãi) |
| 2 | `danh-sach-loc.png` | `/san-pham?danhMuc=bep-dien-tu&thuongHieu=1&thuongHieu=2&sapXep=gia-tang` | Không đăng nhập | Để thấy bộ lọc bên trái đã tích Bosch, Kaff và số kết quả |
| 3 | `chi-tiet-san-pham.png` | `/san-pham/1` | Không đăng nhập | Thấy ảnh, giá, bảng thông số |
| 4 | `gio-hang.png` | `/gio-hang` | Không đăng nhập | Thêm 2 mẫu máy (một mẫu cần lắp) vào giỏ trước |
| 5 | `dat-hang-xac-nhan.png` | `/dat-hang/xac-nhan` | Không đăng nhập | Từ giỏ ở ảnh 4 bấm Tiến hành đặt hàng, nhập thông tin, chọn "Thanh toán trực tuyến", bấm Xem lại đơn; chụp trước khi bấm Đặt hàng (thấy 3 ô đồng ý chưa tích) |
| 6 | `cong-thanh-toan.png` | `/thanh-toan/{mã đơn vừa đặt}` | Không đăng nhập (cùng phiên) | Tích 2 ô đồng ý, bấm Đặt hàng, ở trang hoàn tất bấm Thanh toán ngay; chụp trang cổng (mã MOCK-…, hai nút thành công/thất bại) |
| 7 | `tra-cuu-don.png` | `/tra-cuu-don` → `/tra-cuu-don/7` | Không đăng nhập (phiên mới) | Nhập số điện thoại `0912000007`, mã đơn `7`; chụp trang chi tiết đơn |
| 8 | `tra-cuu-bao-hanh.png` | `/bao-hanh?cach=serial&soSerial=BDT-BOS-001-0001` | Không đăng nhập | Thấy ngày bán, hạn bảo hành |
| 9 | `tong-quan-quan-tri.png` | `/quan-tri` | `quanly` | Thấy các ô số liệu và menu bên trái |
| 10 | `don-hang-chi-tiet.png` | `/quan-tri/don-hang/7` | `banhang` | Đơn Chờ xác nhận: thấy tồn kho từng mẫu, nút Xác nhận, Gọi lại, Huỷ |
| 11 | `phan-cong-giao-lap.png` | `/quan-tri/giao-hang/phan-cong?don=5` | `quanly` | Đơn 5 đã xác nhận, chưa có phiếu giao |
| 12 | `phieu-nhap.png` | `/quan-tri/kho/phieu-nhap/lap?donDat={mã}` | `thukho` (lập đơn đặt), `quanly` (gửi), rồi `thukho` | Dữ liệu mẫu chưa có đơn đặt chờ nhập: `thukho` lập đơn đặt ở `/quan-tri/kho/don-dat/lap`, `quanly` mở đơn đó bấm Gửi nhà cung cấp, `thukho` vào `/quan-tri/kho/phieu-nhap/lap` chọn đơn; nhập số hoá đơn, vài serial rồi chụp trước khi Lưu. Không làm được thì chụp `/quan-tri/kho/phieu-nhap/1` (phiếu đã khoá) |
| 13 | `bao-hanh.png` | `/quan-tri/bao-hanh/tiep-nhan?serial=BDT-BOS-001-0001` | `banhang` | Từ `/quan-tri/bao-hanh?loai=serial&q=BDT-BOS-001-0001` bấm Lập phiếu tiếp nhận; chụp biểu mẫu tiếp nhận |
| 14 | `bao-cao-doanh-thu.png` | `/quan-tri/bao-cao/doanh-thu?tuNgay=2026-10-01&denNgay=2026-11-30&nhom=ngay` | `ketoan` hoặc `quanly` | Thấy bảng, dòng tổng, biểu đồ cột |

Các tài khoản mẫu: `quantri` (Quản trị hệ thống), `quanly` (Quản lý cửa hàng), `banhang` (Nhân viên bán hàng), `thukho` (Thủ kho),
`giaohang1`, `giaohang2` (Nhân viên giao hàng – kỹ thuật), `ketoan` (Kế toán), `khach1` (Khách hàng).
