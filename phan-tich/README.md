# Phân tích và thiết kế

Bản nháp 0.2 (07/10/2026, chỉnh theo bài giảng chương 1, 1.2, 2, 3). Mọi thứ trong thư mục này là **bản nháp trước khảo sát**: dựng từ đề tài và các tài liệu công khai,
sẽ chỉnh lại sau khi phỏng vấn cửa hàng thật (tuần 9).

## Phần dùng chung (thư mục `chung/`, G1 giữ)

Bốn gói kia **dùng đúng tên, mã, thuộc tính trong các tệp này**. Muốn đổi thì báo nhóm, không tự sửa trong phần của mình.

| Tệp | Nội dung | Gói phải đọc |
|---|---|---|
| [`doi-chieu-giao-trinh.md`](chung/doi-chieu-giao-trinh.md) | **Đọc đầu tiên.** Mỗi chương bài giảng bắt làm gì, thứ tự làm, sơ đồ nào ở phần nào, chỗ bài giảng chưa thống nhất | Cả 5 |
| [`quy-uoc-ve.md`](chung/quy-uoc-ve.md) | Công cụ, ký hiệu từng loại sơ đồ (kèm số slide), cách đặt tên và mã, cỡ chữ khi in | Cả 5 |
| [`hien-trang-to-chuc.md`](chung/hien-trang-to-chuc.md) | Bước 1, 2 của mô hình hoá nghiệp vụ: sơ đồ tổ chức, đối tượng liên quan, giới hạn hệ thống, bảng vấn đề, thuật ngữ | Cả 5 |
| [`hien-trang-va-yeu-cau.md`](chung/hien-trang-va-yeu-cau.md) | Hiện trạng từng gói, chiến lược, bảng yêu cầu chức năng, phi chức năng, chuyển đổi (chương 2), mỗi yêu cầu có nguồn; nhóm trưởng làm cho cả 5 gói | Cả 5 |
| [`tac-nhan-va-thuc-the.md`](chung/tac-nhan-va-thuc-the.md) | Tác nhân nghiệp vụ, thừa tác viên, thực thể nghiệp vụ, use case nghiệp vụ tổng, tác nhân hệ thống | Cả 5 |
| [`mau-dac-ta.md`](chung/mau-dac-ta.md) | Mẫu đặc tả use case nghiệp vụ (Mẫu 1 + 3 sơ đồ) và đặc tả use case hệ thống, kèm ví dụ của G1 | Cả 5 |
| [`danh-muc-use-case.md`](chung/danh-muc-use-case.md) | Use case tổng quát và danh mục 35 use case theo 5 phân hệ, tác nhân của từng use case | Cả 5 |
| [`lop-va-trang-thai-chung.md`](chung/lop-va-trang-thai-chung.md) | Lớp dùng chung, mối kết hợp, trạng thái đơn hàng và trạng thái chiếc máy theo serial | G2, G3, G4, G5 |
| `chung/so-do/` | Mã nguồn PlantUML (`.puml`), `ve_nghiep_vu.py` (sơ đồ có hình thừa tác viên, thực thể) và ảnh (`.png`) của các sơ đồ chung | |

Vẽ lại ảnh: `java -jar plantuml.jar -charset UTF-8 -tpng *.puml` và `python ve_nghiep_vu.py` trong thư mục `chung/so-do/`.

## Phần riêng của từng gói

Mỗi gói tạo thư mục `g2/`, `g3/`, `g4/`, `g5/` (G1 dùng `g1/`) và tự làm:
use case nghiệp vụ của gói (đặc tả Mẫu 1, sơ đồ đối tượng nghiệp vụ, sơ đồ hoạt động), sơ đồ use case phân hệ, đặc tả 3 use case chính, sơ đồ lớp của gói,
thiết kế 2 use case (sơ đồ lớp hiện thực hoá + sơ đồ tuần tự), màn hình trong `giao-dien/`.
Lịch và hạn từng việc xem trang tiến độ của nhóm.
