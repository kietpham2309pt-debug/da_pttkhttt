# G1 · Đặc tả use case hệ thống

Bản 1.0 (14/10/2026) · Phạm Tuấn Kiệt · khuôn theo mục 2 của [`../chung/mau-dac-ta.md`](../chung/mau-dac-ta.md)

## UC-HT-01 Đăng nhập

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-HT-01 Đăng nhập |
| Gói | G1 |
| Tác nhân chính | Nhân viên, Khách hàng |
| Tác nhân phụ | Không có |
| Mô tả | Người dùng nhập tên đăng nhập và mật khẩu để dùng các chức năng theo vai trò của mình |
| Điều kiện trước | Người dùng đã có tài khoản ở trạng thái Đang dùng |
| Điều kiện sau | Người dùng ở trong phiên làm việc; hệ thống biết vai trò để hiện đúng menu |
| Màn hình | `giao-dien/khach/dang-nhap.html` |

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang đăng nhập | Hiện ô tên đăng nhập, ô mật khẩu, ô "ghi nhớ đăng nhập" |
| 2 | Nhập tên đăng nhập, mật khẩu, bấm Đăng nhập | Kiểm tra hai ô không để trống |
| 3 | | Tìm tài khoản theo tên đăng nhập, so mật khẩu đã băm |
| 4 | | Mở phiên làm việc; khách hàng về trang chủ, nhân viên về trang Tổng quan quản trị |

Luồng thay thế:
- `2a.` Để trống ô nào: báo ngay dưới ô đó, không gửi đi.
- `3a.` Sai tên đăng nhập hoặc mật khẩu: báo chung "Tên đăng nhập hoặc mật khẩu không đúng" (không nói rõ sai ô nào), cho nhập lại.
- `3b.` Sai quá 5 lần liên tiếp: khoá đăng nhập 15 phút.
- `3c.` Tài khoản ở trạng thái Đã khoá: báo "Tài khoản đã bị khoá, liên hệ cửa hàng".
- `4a.` Nhân viên đăng nhập lần đầu bằng mật khẩu được cấp: chuyển sang trang đổi mật khẩu (UC-HT-06) trước khi vào trang quản trị.

Ngoại lệ: mất kết nối cơ sở dữ liệu thì báo "Hệ thống đang bận, thử lại sau", không mở phiên.

Quy tắc nghiệp vụ: mật khẩu chỉ lưu dạng đã băm (YC-PCN-02); nhân viên đăng nhập lần đầu phải đổi mật khẩu được cấp; mỗi vai chỉ thấy chức năng của vai mình, một tài khoản kiêm được nhiều vai (YC-HT-01).

## UC-HT-04 Xem báo cáo doanh thu

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-HT-04 Xem báo cáo doanh thu |
| Gói | G1 |
| Tác nhân chính | Quản lý cửa hàng, Kế toán |
| Tác nhân phụ | Không có |
| Mô tả | Quản lý cửa hàng hoặc kế toán xem doanh thu theo ngày, tháng, nhóm hàng để lập báo cáo kinh doanh tháng và tự kê khai thuế |
| Điều kiện trước | Đã đăng nhập với vai Quản lý cửa hàng hoặc Kế toán |
| Điều kiện sau | Không đổi dữ liệu; nếu tác nhân chọn xuất tệp thì có tệp bảng tính của báo cáo đang xem |
| Màn hình | `giao-dien/quan-tri/tong-quan.html` (khung trang tổng quan); trang báo cáo chi tiết thiết kế ở tuần 14 |

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Chọn mục Báo cáo doanh thu | Hiện báo cáo mặc định của tháng hiện tại, nhóm theo ngày |
| 2 | Chọn khoảng ngày và cách nhóm (theo ngày, theo tháng, theo nhóm hàng), bấm Xem | Kiểm tra khoảng ngày hợp lệ |
| 3 | | Lấy các đơn **Hoàn thành** có ngày hoàn thành trong khoảng; trừ các đơn **Đã trả hàng** và các khoản đã hoàn tiền; tính doanh thu theo đơn giá lúc đặt |
| 4 | | Hiện bảng: số đơn, doanh thu, tiền giảm giá, tiền đã thu; dòng tổng; biểu đồ cột theo cách nhóm đã chọn |
| 5 | Bấm Xuất tệp | Tạo tệp bảng tính đúng các cột đang hiện, kèm khoảng ngày và thời điểm xuất |

Luồng thay thế:
- `2a.` Ngày bắt đầu sau ngày kết thúc: báo "Khoảng ngày không hợp lệ", giữ nguyên báo cáo cũ.
- `3a.` Không có đơn nào trong khoảng: hiện "Không có doanh thu trong khoảng đã chọn", không vẽ biểu đồ.
- `4a.` Có đơn đã giao nhưng tiền thu khi giao chưa được kế toán xác nhận: đánh dấu riêng số tiền này ở dòng tổng và liệt kê các đơn đó để kế toán xử lý (UC-DH-04).

Ngoại lệ: khoảng ngày quá 12 tháng thì chỉ cho nhóm theo tháng để trang không tải quá lâu.

Quy tắc nghiệp vụ: QĐ2 của BUC-07 (doanh thu tính theo ngày giao xong, không theo ngày đặt); giá lấy theo `ChiTietDonHang.donGia` lúc đặt (YC-SP-08); QĐ1 của BUC-07 (báo cáo tháng chốt trước ngày 5 tháng sau); YC-HT-03.

## UC-HT-02 Quản lý tài khoản nhân viên

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-HT-02 Quản lý tài khoản nhân viên |
| Gói | G1 |
| Tác nhân chính | Quản trị hệ thống |
| Tác nhân phụ | Không có |
| Mô tả | Quản trị hệ thống thêm, sửa, khoá, mở khoá tài khoản nhân viên và gán một hoặc nhiều vai cho mỗi tài khoản |
| Điều kiện trước | Đã đăng nhập với vai Quản trị hệ thống |
| Điều kiện sau | Tài khoản được tạo hoặc cập nhật; vai được gán; tài khoản bị khoá không đăng nhập được nữa |
| Màn hình | `giao-dien/quan-tri/tai-khoan.html` |

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang Quản lý tài khoản | Hiện danh sách nhân viên: họ tên, số điện thoại, tên đăng nhập, các vai, trạng thái |
| 2 | Bấm Thêm nhân viên | Hiện biểu mẫu trống |
| 3 | Nhập họ tên, số điện thoại, tên đăng nhập; chọn một hoặc nhiều vai (Nhân viên bán hàng, Thủ kho, Nhân viên giao hàng – kỹ thuật, Quản lý cửa hàng, Kế toán, Quản trị hệ thống); bấm Lưu | Kiểm tra các ô bắt buộc và tên đăng nhập chưa có ai dùng |
| 4 | | Tạo nhân viên và tài khoản ở trạng thái Đang dùng, sinh mật khẩu tạm, đánh dấu phải đổi mật khẩu lần đầu |
| 5 | | Hiện mật khẩu tạm đúng một lần để quản trị hệ thống báo cho nhân viên; danh sách có thêm dòng mới |

Luồng thay thế:
- `2a.` Sửa: chọn một nhân viên, đổi họ tên, số điện thoại hoặc các vai rồi Lưu; hệ thống kiểm tra như bước 3 và cập nhật.
- `2b.` Khoá: chọn nhân viên, bấm Khoá, xác nhận; hệ thống đổi trạng thái tài khoản thành Đã khoá và huỷ các phiên đang mở của tài khoản đó.
- `2c.` Mở khoá: chọn tài khoản Đã khoá, bấm Mở khoá; hệ thống đổi lại Đang dùng.
- `2d.` Đặt lại mật khẩu: hệ thống sinh mật khẩu tạm mới, đánh dấu phải đổi mật khẩu, hiện mật khẩu tạm một lần.
- `3a.` Tên đăng nhập đã có người dùng: báo "Tên đăng nhập đã tồn tại", giữ nguyên các ô đã nhập.
- `3b.` Chưa chọn vai nào: báo "Chọn ít nhất một vai".

Ngoại lệ: không cho quản trị hệ thống tự khoá tài khoản đang dùng của chính mình; luôn phải còn ít nhất một tài khoản Quản trị hệ thống ở trạng thái Đang dùng.

Quy tắc nghiệp vụ: một tài khoản kiêm được nhiều vai vì cửa hàng có người kiêm việc (YC-HT-01, `lop-va-trang-thai-chung.md` lớp `VaiTro`); không xoá tài khoản nhân viên đã có thao tác, chỉ khoá, để giữ lưu vết (YC-HT-05); mật khẩu lưu dạng băm (YC-PCN-02).

## Sơ đồ use case phân hệ G1

![Sơ đồ use case phân hệ Quản trị hệ thống và báo cáo](so-do/uc-phan-he-g1.png)
