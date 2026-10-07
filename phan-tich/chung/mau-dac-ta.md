# Mẫu đặc tả quy trình và đặc tả use case

Bản nháp 0.1 · G1 giữ · cả 5 gói dùng **đúng thứ tự mục** dưới đây để khi ghép báo cáo không lệch nhau

## 1. Mẫu đặc tả quy trình nghiệp vụ (việc t92)

Chép khung này vào phần của gói mình, thay chữ in nghiêng.

| Mục | Nội dung |
|---|---|
| Mã, tên | *QT-0x Tên quy trình* |
| Gói | *Gx* |
| Mục tiêu | *Quy trình này giúp cửa hàng đạt được gì* |
| Tác nhân nghiệp vụ | *Lấy trong `tac-nhan-va-thuc-the.md` mục 1* |
| Nhân viên nghiệp vụ | *Lấy trong mục 2; mỗi vai là một làn trong sơ đồ hoạt động* |
| Sự kiện bắt đầu | *Việc gì xảy ra thì quy trình chạy* |
| Điều kiện trước | |
| Kết quả | *Khi xong, cái gì đã thay đổi, giấy tờ nào đã có* |
| Biểu mẫu dùng | *Tên thật của biểu mẫu ở cửa hàng, ghi kèm "Hình x.y" nếu có ảnh chụp* |

**Các bước chính**

| Bước | Ai làm | Làm gì | Giấy tờ |
|---|---|---|---|
| 1 | | | |

**Luồng thay thế:** đánh số theo bước gốc, ví dụ `3a. Hàng về thiếu so với đơn đặt: …`

**Quy tắc nghiệp vụ:** `QĐ1`, `QĐ2`… mỗi quy tắc một câu, có số liệu nếu có (ví dụ "đổi trong 7 ngày nếu còn nguyên hộp").

**Vấn đề hiện tại:** những bước chậm, hay sai mà cửa hàng kể. Đây là căn cứ chọn chiến lược ở mục 2.2 (tự động hoá, cải tiến hay tái thiết kế).

### Ví dụ (G1): QT-01 Lập báo cáo kinh doanh cuối tháng

> Ví dụ để xem **cách viết**. Các bước dưới đây là giả định thường gặp; G1 sẽ viết lại theo đúng lời cửa hàng sau khi phỏng vấn.

| Mục | Nội dung |
|---|---|
| Mã, tên | QT-01 Lập báo cáo kinh doanh cuối tháng |
| Gói | G1 |
| Mục tiêu | Chủ cửa hàng biết tháng qua bán được gì, còn bao nhiêu hàng, máy nào đang bảo hành để quyết định nhập hàng và khuyến mãi tháng sau |
| Tác nhân nghiệp vụ | Chủ cửa hàng |
| Nhân viên nghiệp vụ | Kế toán, Thủ kho, Quản lý cửa hàng |
| Sự kiện bắt đầu | Ngày cuối tháng |
| Điều kiện trước | Hoá đơn, phiếu nhập, phiếu tiếp nhận bảo hành trong tháng đã được lưu đủ |
| Kết quả | Có báo cáo kinh doanh tháng; chủ đã ra quyết định nhập hàng, khuyến mãi |
| Biểu mẫu dùng | Hoá đơn bán hàng, Phiếu nhập kho, Phiếu tiếp nhận bảo hành, Báo cáo kinh doanh |

| Bước | Ai làm | Làm gì | Giấy tờ |
|---|---|---|---|
| 1 | Kế toán | Gom hoá đơn bán hàng trong tháng, cộng doanh thu theo nhóm hàng | Hoá đơn bán hàng |
| 2 | Kế toán | Đối chiếu tiền thu khi giao hàng với sổ nộp tiền | Sổ nộp tiền |
| 3 | Thủ kho | Kiểm kê, báo số máy còn trong kho theo từng mẫu | Phiếu nhập kho |
| 4 | Kế toán | Gom phiếu nhập, cộng tiền nhập trong tháng | Phiếu nhập kho |
| 5 | Kế toán | Lập báo cáo: doanh thu, hàng bán chạy, hàng tồn, máy đang bảo hành | Báo cáo kinh doanh |
| 6 | Quản lý cửa hàng | Xem lại báo cáo, ghi đề xuất nhập hàng và khuyến mãi | Báo cáo kinh doanh |
| 7 | Chủ cửa hàng | Duyệt đề xuất, ra quyết định cho tháng sau | Báo cáo kinh doanh |

Luồng thay thế: `2a. Tiền thu lệch với sổ: Kế toán tìm lại hoá đơn của ngày lệch, hỏi người đi giao, ghi chú vào báo cáo.` · `3a. Số kiểm kê lệch với sổ: Thủ kho kiểm lại theo phiếu nhập, phiếu xuất.`

Quy tắc: `QĐ1. Báo cáo chốt trước ngày 5 tháng sau.` · `QĐ2. Doanh thu tính theo ngày giao xong, không theo ngày đặt.`

![Ví dụ sơ đồ hoạt động QT-01](so-do/hd-qt01-bao-cao.png)

## 2. Mẫu đặc tả use case (việc t103)

| Mục | Nội dung |
|---|---|
| Mã, tên | *UC-XX-0x Tên use case* |
| Gói | |
| Tác nhân chính | *Người bắt đầu use case* |
| Tác nhân phụ | *Hệ thống ngoài hoặc người nhận kết quả; không có thì ghi "Không có"* |
| Mô tả | *Một câu: ai làm gì để đạt gì* |
| Điều kiện trước | *Ví dụ: đã đăng nhập với vai trò Thủ kho* |
| Điều kiện sau | *Dữ liệu nào đã được tạo, sửa; trạng thái nào đã đổi* |
| Màn hình | *Tệp trong `giao-dien/`, ví dụ `quan-tri/phieu-nhap.html`* |

**Luồng sự kiện chính** (hai cột để thấy rõ ai làm, hệ thống đáp gì)

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | | |

**Luồng thay thế:** `2a. …` · **Ngoại lệ:** lỗi không lường trước (mất kết nối, dữ liệu trùng) · **Quy tắc nghiệp vụ:** dẫn lại `QĐx` của quy trình nếu có.

### Ví dụ (G1): UC-HT-01 Đăng nhập

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

Quy tắc nghiệp vụ: mật khẩu chỉ lưu dạng đã băm; nhân viên đăng nhập lần đầu phải đổi mật khẩu được cấp.
