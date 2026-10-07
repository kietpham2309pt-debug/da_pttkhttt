# Tác nhân, thực thể và use case nghiệp vụ

Bản nháp 0.2 (đã soát chéo) · G1 giữ · việc t91, t95, t101 · **chỉnh lại sau khi phỏng vấn cửa hàng thật**

Cửa hàng nhỏ thường một người kiêm nhiều việc (chủ kiêm kế toán, thợ kiêm giao hàng). Ta vẫn tách theo **vai**,
còn ai kiêm vai nào thì ghi ở mục giới thiệu cơ cấu tổ chức của báo cáo.

## 1. Tác nhân nghiệp vụ (bên ngoài cửa hàng)

| Tác nhân nghiệp vụ | Là ai | Tham gia |
|---|---|---|
| Khách hàng | Người mua máy về dùng trong gia đình hoặc cho quán, văn phòng | BUC-01, 02, 03, 04, 06 |
| Nhà cung cấp | Hãng hoặc nhà phân phối bán hàng cho cửa hàng | BUC-05 |
| Đơn vị vận chuyển | Bên nhận chở hàng đi xa (cửa hàng không tự giao được) | BUC-03 |
| Trung tâm bảo hành hãng | Nơi sửa máy còn trong hạn bảo hành của hãng | BUC-06 |
| Chủ cửa hàng | Người đứng tên cửa hàng, nhận báo cáo và ra quyết định kinh doanh | BUC-07 |

Ghi chú: để "Chủ cửa hàng" là tác nhân nghiệp vụ của BUC-07 vì chủ là người **nhận kết quả** của việc lập báo cáo,
không trực tiếp làm. Nếu GV yêu cầu chỉ dùng tác nhân bên ngoài tổ chức thì bỏ BUC-07 khỏi sơ đồ tổng và trình bày QT-01 như quy trình hỗ trợ.

## 2. Nhân viên nghiệp vụ (bên trong cửa hàng, dùng làm làn trong sơ đồ hoạt động)

| Nhân viên nghiệp vụ | Việc chính |
|---|---|
| Quản lý cửa hàng | Duyệt giá bán, khuyến mãi, đơn đặt hàng nhà cung cấp, đổi trả; xếp lịch giao lắp |
| Nhân viên bán hàng | Tư vấn, chốt đơn, gọi xác nhận đơn, tiếp nhận đổi trả và bảo hành, chăm sóc khách |
| Thủ kho | Nhận hàng, kiểm đếm, ghi số serial, xuất kho theo đơn, nhận lại máy, kiểm kê |
| Nhân viên giao hàng – kỹ thuật | Khảo sát chỗ lắp, giao hàng, lắp đặt, thu tiền khi giao |
| Kế toán | Thu chi, đối chiếu tiền thu khi giao, lập báo cáo cuối tháng |

## 3. Thực thể nghiệp vụ (giấy tờ, đồ vật cửa hàng đang dùng)

Cột "Dùng ở" khớp với bảng hiện thực hoá ở mục 4.

| Thực thể nghiệp vụ | Tạo ra ở | Dùng ở | Lớp tương ứng ở chương 5 |
|---|---|---|---|
| Sản phẩm (chiếc máy có số serial) | BUC-05 | BUC-01, 04, 06 | `SanPham`, `SanPhamSerial` |
| Bảng giá, chương trình khuyến mãi | BUC-01 | BUC-01 | `KhuyenMai` |
| Đơn hàng | BUC-02 | BUC-03 | `DonHang`, `ChiTietDonHang` |
| Hoá đơn bán hàng | BUC-02 | BUC-04, 07 | `HoaDon` (G3 quyết có tách khỏi `DonHang` không) |
| Phiếu bảo hành | BUC-02 (khi bán) | BUC-06 | `PhieuBaoHanh` |
| Phiếu xuất kho | BUC-03 | BUC-03 | `PhieuXuat` |
| Phiếu giao hàng | BUC-03 | BUC-03 | `PhieuGiaoHang` (G4) |
| Biên bản lắp đặt | BUC-03 | BUC-03 | `PhieuLapDat` (G4) |
| Sổ nộp tiền thu khi giao | BUC-03 | BUC-07 | Không thành lớp; thay bằng `trangThaiThanhToan` của `DonHang` |
| Phiếu đổi trả | BUC-04 | BUC-04 | `YeuCauDoiTra` |
| Đơn đặt hàng nhà cung cấp | BUC-05 | BUC-05 | `DonDatHangNCC` (G5) |
| Phiếu nhập kho | BUC-05 | BUC-07 | `PhieuNhap` |
| Phiếu tiếp nhận bảo hành | BUC-06 | BUC-07 | `PhieuBaoHanh` |
| Báo cáo kinh doanh | BUC-07 | | Không thành lớp, là kết quả truy vấn |

Sau khi phỏng vấn: **chụp được biểu mẫu nào thì ghi tên thật của biểu mẫu đó** vào cột đầu (ví dụ cửa hàng gọi là "phiếu giao nhận" thì dùng đúng chữ đó).

## 4. Use case nghiệp vụ tổng

![Sơ đồ use case nghiệp vụ tổng](so-do/uc-nghiep-vu-tong.png)

| Mã | Use case nghiệp vụ | Quy trình chi tiết | Gói | Tác nhân nghiệp vụ |
|---|---|---|---|---|
| BUC-01 | Giới thiệu sản phẩm và khuyến mãi | QT-02 Đưa sản phẩm mới lên bán và chạy khuyến mãi | G2 | Khách hàng |
| BUC-02 | Bán hàng | QT-03 Đặt hàng và thanh toán | G3 | Khách hàng |
| BUC-03 | Giao hàng và lắp đặt | QT-04 Xử lý đơn, giao hàng và lắp đặt tận nơi | G4 | Khách hàng, Đơn vị vận chuyển |
| BUC-04 | Đổi trả hàng | QT-07 Tiếp nhận và xử lý đổi trả | G4 | Khách hàng |
| BUC-05 | Nhập hàng | QT-05 Đặt hàng nhà cung cấp và nhập kho | G5 | Nhà cung cấp |
| BUC-06 | Bảo hành | QT-06 Tiếp nhận bảo hành theo số serial | G5 | Khách hàng, Trung tâm bảo hành hãng |
| BUC-07 | Báo cáo kinh doanh | QT-01 Lập báo cáo kinh doanh cuối tháng | G1 | Chủ cửa hàng |

G4 có hai quy trình. Nếu khảo sát cho thấy cửa hàng chỉ đổi trả ngay lúc giao thì G4 gộp QT-07 thành nhánh thay thế của QT-04.

### Hiện thực hoá use case nghiệp vụ (ai làm, dùng giấy tờ gì)

Bảng này cho biết **các làn** và **các nút đối tượng** của sơ đồ hoạt động mỗi gói. Gói nào thấy thiếu vai hoặc thiếu giấy tờ sau khi phỏng vấn thì báo G1 để thêm vào mục 2, 3.

| BUC | Nhân viên nghiệp vụ tham gia (làn) | Thực thể nghiệp vụ |
|---|---|---|
| BUC-01 | Quản lý cửa hàng, Nhân viên bán hàng, Thủ kho | Sản phẩm, Bảng giá và khuyến mãi |
| BUC-02 | Nhân viên bán hàng, Kế toán | Đơn hàng, Hoá đơn bán hàng, Phiếu bảo hành |
| BUC-03 | Nhân viên bán hàng, Quản lý cửa hàng, Thủ kho, Nhân viên giao hàng – kỹ thuật | Đơn hàng, Phiếu xuất kho, Phiếu giao hàng, Biên bản lắp đặt, Sổ nộp tiền |
| BUC-04 | Nhân viên bán hàng, Quản lý cửa hàng, Thủ kho, Kế toán | Phiếu đổi trả, Hoá đơn bán hàng, Sản phẩm |
| BUC-05 | Thủ kho, Quản lý cửa hàng, Kế toán | Đơn đặt hàng nhà cung cấp, Phiếu nhập kho, Sản phẩm |
| BUC-06 | Nhân viên bán hàng, Thủ kho | Phiếu bảo hành, Phiếu tiếp nhận bảo hành, Sản phẩm |
| BUC-07 | Kế toán, Thủ kho, Quản lý cửa hàng | Hoá đơn bán hàng, Sổ nộp tiền, Phiếu nhập kho, Phiếu tiếp nhận bảo hành, Báo cáo kinh doanh |

## 5. Tác nhân hệ thống (người dùng website)

![Sơ đồ tác nhân hệ thống](so-do/tac-nhan-he-thong.png)

| Tác nhân | Kế thừa từ | Làm được gì (tóm tắt) | Gói có use case |
|---|---|---|---|
| Khách vãng lai | | Xem, tìm, so sánh sản phẩm; quản lý giỏ hàng; đăng ký; tra cứu bảo hành | G2, G3, G5 |
| Khách hàng | Khách vãng lai | Đăng nhập, đặt hàng, áp mã giảm giá, thanh toán, theo dõi và huỷ đơn, quản lý hồ sơ, đánh giá sản phẩm | G1, G2, G3 |
| Nhân viên (trừu tượng) | | Đăng nhập, đổi mật khẩu | G1 |
| Nhân viên bán hàng | Nhân viên | Cập nhật sản phẩm, duyệt đơn, cập nhật đơn gửi đơn vị vận chuyển, tiếp nhận đổi trả, tiếp nhận bảo hành | G2, G4, G5 |
| Thủ kho | Nhân viên | Quản lý nhà cung cấp, nhập kho, xuất kho, nhận lại máy, kiểm kê | G5 |
| Nhân viên giao hàng – kỹ thuật | Nhân viên | Cập nhật trạng thái giao, lập biên bản lắp đặt | G4 |
| Quản lý cửa hàng | Nhân viên | Danh mục, giá, khuyến mãi, phân công giao lắp, xác nhận tiền thu khi giao, duyệt đổi trả, xem báo cáo | G1, G2, G4 |
| Quản trị hệ thống | Nhân viên | Tài khoản nhân viên, phân quyền | G1 |
| Cổng thanh toán | | Hệ thống bên ngoài nhận tiền trực tuyến | G3 |

**Kế toán và Chủ cửa hàng** không có tác nhân riêng trên website: họ dùng tài khoản có vai **Quản lý cửa hàng**
(xác nhận tiền thu khi giao, xem báo cáo). Nếu khảo sát cho thấy kế toán là người riêng và làm nhiều việc trên hệ thống thì thêm tác nhân Kế toán kế thừa Nhân viên.

**Còn chờ kết quả khảo sát (G3 quyết):** khách vãng lai có được đặt hàng chỉ bằng số điện thoại hay không.
Đợt thử bảng hỏi cho thấy nhiều người ngại tạo tài khoản; câu K11 của Google Form sẽ trả lời câu này.
Nếu cho phép thì UC-MH-04, UC-MH-06, UC-MH-07 nối với Khách vãng lai, và `DonHang` thêm `hoTenNguoiDat`, `soDienThoaiNguoiDat`
(bắt buộc khi đơn không gắn `KhachHang`), để vẫn tra được đơn và bảo hành theo số điện thoại người mua.
