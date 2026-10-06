# Quy tắc dùng bộ giao diện chung

Bộ giao diện lấy từ dự án **Bep Nè** (repo `kietpham2309pt-debug/bepne`, môn Lập trình Web) và phần
quản trị của đồ án môn Công nghệ Java. Cùng một hệ thống: nền tối `#131313`, chữ kem `#EDE7E0`,
màu nhấn vàng đồng `#CBAA7D`, font **Montserrat** nhúng sẵn.

Môn Phân tích thiết kế hệ thống thông tin **không bắt buộc viết chương trình**. Bộ này là trang HTML tĩnh,
dùng để:

1. **Thiết kế giao diện** cho mục 6.3 của báo cáo: mỗi gói làm 2–4 màn hình, chụp ảnh đưa vào báo cáo.
2. **Làm bản mẫu khi khảo sát** (kỹ thuật 2.3.7 "Sử dụng bản mẫu"): mở cho chủ cửa hàng xem rồi hỏi tiếp.
3. **Giữ 5 gói ra cùng một website**: cùng đầu trang, chân trang, nút, bảng, màu trạng thái.

Mở `giao-dien/index.html` bằng Chrome hoặc Edge là chạy, không cần cài gì, không cần mạng.

---

## 1. Ba luật bắt buộc

1. **Không thêm thư viện giao diện.** Không Bootstrap, không Tailwind, không template tải trên mạng.
2. **Không sửa** `assets/css/site.css`, `assets/js/site.js`, `assets/js/khung.js`. Đó là phần gốc chép từ Bep Nè.
   Cần đổi gì thì báo nhóm trưởng.
3. **Cần kiểu mới thì thêm vào cuối `assets/css/ung-dung.css`**, đặt tên tiếng Việt không dấu,
   báo cả nhóm biết. Chỉ dùng `style="..."` cho một hai thuộc tính lặt vặt.

---

## 2. Cấu trúc thư mục

```
giao-dien/
├── index.html              Mục lục: danh sách màn hình theo gói và use case
├── thanh-phan.html         Thành phần: nút, ô nhập, bảng, nhãn, icon kèm đoạn mã để chép
├── khach/                  Màn hình phía khách hàng (G2, G3, một phần G5)
├── quan-tri/               Màn hình phía nhân viên (G1, G2, G4, G5)
├── mau/                    Hai khung trống để chép khi làm màn hình mới
└── assets/
    ├── css/  site.css (gốc) · ung-dung.css (khu quản trị) · tai-lieu.css (chỉ cho index và thanh-phan)
    ├── js/   khoi-dau.js · khung.js · menu-quan-tri.js · site.js (gốc)
    ├── fonts/ img/
```

`khung.js` tự vẽ **bộ icon, đầu trang, chân trang, menu điện thoại** vào mọi trang.
Vì vậy trong tệp HTML chỉ thấy hai chỗ giữ chỗ `<div data-khung="dau-trang">` và `<div data-khung="chan-trang">`.
Đổi đầu trang một lần là cả bộ đổi theo.

---

## 3. Làm một màn hình mới

1. Chép `mau/trang-khach.html` (phía khách) hoặc `mau/trang-quan-tri.html` (phía nhân viên)
   vào thư mục `khach/` hoặc `quan-tri/`. Tên tệp không dấu, nối bằng gạch ngang: `phieu-nhap.html`.
2. Sửa `<title>`. Chỉ thay nội dung **bên trong `<main>`**, giữ nguyên phần `<head>` và các thẻ `<script>` cuối trang.
3. Trang quản trị:
   - đổi `data-chon="ma-muc"` thành mã mục của mình, ví dụ `data-chon="phieu-nhap"`;
   - mở `assets/js/menu-quan-tri.js`, điền tên tệp vào **đúng dòng của mình**: `tep: 'phieu-nhap.html'`.
     Dòng nào còn `tep: ''` thì menu hiện mờ kèm mã gói.
4. Thêm một dòng cho màn hình mới vào bảng trong `index.html`.

Trang **không có banner** (trang quản trị) dùng `<section class="muc muc--dau-trang">` để chừa chỗ cho thanh menu cố định.
Trang có banner dùng khối `hero-sp` như `mau/trang-khach.html`, khối nội dung ngay dưới là `muc muc--sat-banner`.

---

## 4. Màn hình nào thuộc gói nào

| Gói | Có sẵn | Còn phải làm |
|---|---|---|
| G1 Tổng quan, quản trị | `khach/dang-nhap` · `quan-tri/tong-quan` · `quan-tri/tai-khoan` | Phân quyền (UC-HT-03) · Báo cáo doanh thu (UC-HT-04) |
| G2 Sản phẩm, khuyến mãi | `khach/trang-chu` · `khach/san-pham` · `khach/chi-tiet` · `quan-tri/danh-muc` · `quan-tri/danh-muc-form` | Quản lý sản phẩm và form thêm (UC-SP-02) · Khuyến mãi (UC-SP-04) |
| G3 Mua hàng, thanh toán | `khach/gio-hang` · `khach/dat-hang` · `khach/hoan-tat` · `khach/dang-ky` | Theo dõi đơn (UC-MH-06) · Hồ sơ, sổ địa chỉ (UC-MH-02) · Thanh toán trực tuyến (UC-MH-05) |
| G4 Đơn hàng, giao lắp, đổi trả | `quan-tri/don-hang` | Chi tiết và duyệt đơn (UC-DH-01) · Phân công giao lắp (UC-DH-02) · Phiếu đổi trả (UC-DH-06) |
| G5 Kho, bảo hành | Chưa có | Phiếu nhập (UC-KB-03) · Tồn kho (UC-KB-05) · Tiếp nhận bảo hành (UC-KB-06) · Tra cứu bảo hành phía khách (UC-KB-07) |

Mỗi màn hình phải phục vụ **đúng một use case đã đặc tả**. Tên ô nhập, cột bảng lấy theo thuộc tính
trong sơ đồ lớp của gói, để thầy đối chiếu được giao diện với thiết kế.

---

## 5. Thành phần hay dùng

Xem trực tiếp và chép mã ở `thanh-phan.html`. Tóm tắt:

| Việc | Lớp |
|---|---|
| Giới hạn bề ngang | `khung` · `khung--hep` (form) · `khung--rong` (trang quản trị) |
| Tiêu đề trang quản trị | `qt-dau` kèm `qt-dau__nut` cho nút bên phải |
| Nút | `nut` (phụ) · `nut nut--dac` (chính) · `nut--nho` (trong bảng) · `nut--do` (xoá, huỷ) |
| Ô nhập | `o-nhap` bọc cả `label` và `input`; `o-doi` cho hai cột; `goi-y` gợi ý; `loi-nhap` báo lỗi |
| Lựa chọn | `o-tich` (ô tích) · `o-chon__ds` + `o-chon` (chọn một, có mô tả) |
| Bảng | `khoi` > `cuon-ngang` > `table.bang-dl`; cột số thêm `canh-phai`; mã thêm `ma-mo` |
| Nhãn trạng thái | `the-nhan` · `--vang` chờ xử lý · `--ok` xong · `--do` huỷ, lỗi · `--tat` đã khoá |
| Số liệu | `o-so-ds` > `o-so`; `o-so--nhac` cho số cần người xử lý |
| Thông báo | `thong-bao` · `thong-bao--loi` |
| Danh sách rỗng | `trong` |
| Icon | `<svg class="ic"><use href="#ic-gio-hang"></use></svg>`, 44 icon có sẵn |

Màu trạng thái **cố định trên mọi màn hình**: vàng là đang chờ người xử lý, xanh là xong, đỏ là huỷ hoặc lỗi.
Đơn hàng, phiếu nhập, phiếu bảo hành, yêu cầu đổi trả đều theo quy ước này.

---

## 6. Viết chữ trên giao diện

- Tiếng Việt có dấu, viết hoa chữ đầu câu. Không tự viết hoa toàn bộ, chữ hoa do CSS lo.
- Nút ghi đúng việc nó làm: "Lưu phiếu nhập", "Duyệt đơn". Không dùng "Submit", "OK".
- Câu lỗi nói rõ vì sao và làm gì tiếp: "Số serial này đã có trong kho".
- Không dùng dấu gạch ngang dài trong câu hiển thị; dùng dấu phẩy hoặc dấu chấm.
- Tiền viết `18.900.000 ₫`. Ngày giờ viết `07/10/2026 09:15`.
- Dữ liệu mẫu phải giống thật: tên máy có thật trong danh mục, số điện thoại 10 số, mã phiếu có tiền tố (`PN-0012`).
- Không viết chú thích trong mã HTML, CSS, JS.

---

## 7. Chụp màn hình cho báo cáo

1. Mở trang bằng Chrome, bấm `F12`, bấm biểu tượng điện thoại (Toggle device toolbar), chọn **Responsive**, đặt bề ngang **1440**.
2. Bấm `Ctrl + Shift + P`, gõ `screenshot`, chọn **Capture full size screenshot** (cả trang) hoặc **Capture screenshot** (một màn).
3. Đặt tên ảnh theo mã use case: `UC-KB-03_phieu-nhap.png`, lưu vào thư mục `05_ThietKe` của nhóm.
4. Muốn có ảnh bản điện thoại thì đặt bề ngang 400.

---

## 8. Kiểm tra trước khi tạo Pull Request

1. Trang có thanh menu trên đầu, chân trang, menu điện thoại như các trang khác.
2. Mọi liên kết bấm được, không có liên kết dẫn tới trang trắng.
3. Thu cửa sổ còn khoảng 400px vẫn đọc được, không phải cuộn ngang cả trang (bảng thì được cuộn ngang trong khung).
4. Không thêm thư viện, không có lớp lạ ngoài các tệp CSS chung.
5. Nhánh đặt tên `gX-ten-man-hinh` (ví dụ `g5-phieu-nhap`), commit bằng tài khoản GitHub của chính mình.
   Lịch sử commit là bằng chứng cho tiêu chí Phối hợp (20% điểm bài nhóm).
