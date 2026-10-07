# Quy ước vẽ sơ đồ và đặt tên

Bản nháp 0.1 · G1 giữ · áp dụng cho cả 5 gói

## 1. Công cụ

- **Chờ GV chốt** (đang hỏi). Đề cương nêu IBM Rational Rose, PowerDesigner và công cụ online; bài nhóm HUIT năm 2024 vẽ bằng Rational Rose.
- Trong lúc chờ: mỗi người vẽ bằng công cụ mình quen (draw.io, StarUML, Visual Paradigm Online), **miễn đúng ký hiệu và đúng tên trong các tệp chung**. Nộp vào repo cả tệp gốc lẫn ảnh PNG.
- Sơ đồ chung trong `so-do/` viết bằng PlantUML (dạng chữ, sửa và so sánh được trên GitHub). Ai muốn sửa thì sửa tệp `.puml` rồi chạy lại, hoặc báo G1.

## 2. Mã và tên

| Đối tượng | Mã | Tên | Ví dụ |
|---|---|---|---|
| Use case nghiệp vụ | `BUC-01`… | Động từ + việc | BUC-05 Nhập hàng |
| Quy trình nghiệp vụ | `QT-01`… | Trùng ý với use case nghiệp vụ | QT-05 Đặt hàng nhà cung cấp và nhập kho |
| Use case hệ thống | `UC-<gói>-01`… với gói `HT`, `SP`, `MH`, `DH`, `KB` | Động từ + bổ ngữ, nhìn từ phía người dùng | UC-KB-03 Lập phiếu nhập kho |
| Tác nhân | | Danh từ chỉ **vai trò**, không phải tên người | Thủ kho (không ghi "chú Tư") |
| Lớp | | PascalCase không dấu, số ít | `PhieuNhap`, `ChiTietDonHang` |
| Thuộc tính | | camelCase không dấu | `soSerial`, `ngayDat`, `giaBan` |
| Phương thức | | camelCase, bắt đầu bằng động từ | `tinhTongTien()`, `doiTrangThai()` |
| Hình trong báo cáo | | `Hình <chương>.<số>: <tên>` dưới hình | Hình 2.1: Sơ đồ use case nghiệp vụ tổng |

Cùng một khái niệm chỉ có **một tên** trong cả báo cáo. Ví dụ đã chốt "Nhân viên giao hàng – kỹ thuật" thì không viết lúc "thợ", lúc "kỹ thuật viên".

## 3. Ký hiệu theo từng loại sơ đồ

**Use case nghiệp vụ (chương 3 bài giảng)**
- Tác nhân nghiệp vụ (bên ngoài cửa hàng) và use case nghiệp vụ dùng **ký hiệu có gạch chéo** (business actor, business use case).
- Nhân viên nghiệp vụ và thực thể nghiệp vụ chỉ xuất hiện trong phần hiện thực hoá, không đặt trên sơ đồ use case nghiệp vụ tổng.

**Sơ đồ hoạt động có làn (quy trình nghiệp vụ)**
- Mỗi làn là **một vai** (nhân viên nghiệp vụ hoặc tác nhân nghiệp vụ). Ở mức nghiệp vụ hiện trạng **không có làn "Hệ thống"**.
- Có nút bắt đầu, nút kết thúc; mỗi nút rẽ nhánh ghi điều kiện trong ngoặc vuông: `[còn hàng]`, `[hết hàng]`.
- Tên hoạt động bắt đầu bằng động từ: "Kiểm đếm hàng", không viết "Việc kiểm đếm".
- Giấy tờ (thực thể nghiệp vụ) vẽ thành nút đối tượng (ô chữ nhật góc vuông) đặt **sau** hoạt động tạo ra nó hoặc **trước** hoạt động dùng nó, không nối thẳng từ nút bắt đầu: `[Phiếu nhập kho]`. Trong PlantUML viết `:Phiếu nhập kho; <<object>>`.

**Use case hệ thống (chương 4)**
- `include`: use case gốc **luôn luôn** gọi use case kia, và use case kia cũng là một việc người dùng làm được riêng. Ví dụ UC-KB-06 "Tiếp nhận bảo hành" include UC-KB-07 "Tra cứu bảo hành". Không biến một bước tính toán nội bộ thành use case.
- `extend`: chỉ gọi khi **có điều kiện**. Ví dụ UC-MH-08 "Áp mã giảm giá" extend UC-MH-04 "Đặt hàng".
- **Không** vẽ include "Đăng nhập" vào từng use case. Ghi "đã đăng nhập" ở điều kiện trước của đặc tả.
- Quan hệ tổng quát hoá giữa tác nhân lấy đúng theo `tac-nhan-va-thuc-the.md` mục 5. Tác nhân trừu tượng (Nhân viên) ghi kèm `{abstract}`.

**Sơ đồ lớp (chương 5–6)**
- Mức phân tích: tên lớp, thuộc tính, phương thức chính, mối kết hợp có **bản số hai đầu** (`1`, `0..1`, `*`, `1..*`). Thuộc tính dẫn xuất (tính ra từ dữ liệu khác) ghi dấu `/` phía trước: `/tongTien`.
- Ghi gói sở hữu bằng cách **gom lớp vào gói** (package `G2`…), không dùng stereotype `«G2»` (stereotype dành cho phân loại như `«entity»`, `«boundary»`).
- Mức thiết kế: thêm kiểu dữ liệu, phạm vi truy cập (`-` riêng, `+` công khai, `#` bảo vệ), chiều điều hướng.
- Lớp kết hợp (như `ChiTietDonHang`) vẽ bằng nét đứt nối vào giữa mối kết hợp, hoặc tách thành lớp riêng có hai mối kết hợp. Cả nhóm dùng **cách tách lớp riêng**.

**Sơ đồ tuần tự (chương 7)**
- Thứ tự đối tượng từ trái sang phải: tác nhân → lớp giao diện → lớp nghiệp vụ (`...BUS`) → lớp truy cập dữ liệu (`...DAO`) → lớp thực thể.
- Luồng thay thế vẽ bằng khối `alt`, lặp bằng `loop`.

## 4. Cỡ chữ khi đưa vào báo cáo

Báo cáo in A4, phần chữ rộng tối đa 15 cm. Chữ trong sơ đồ in ra phải **từ 9 pt trở lên**:
- Sơ đồ quá rộng thì tách: một sơ đồ tổng quát + các sơ đồ phân rã.
- Use case quá 12 ca trên một sơ đồ thì tách theo tác nhân hoặc theo nhóm chức năng.
- Xuất ảnh PNG độ phân giải cao (draw.io: Export → PNG, zoom 200%).
