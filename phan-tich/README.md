# Phân tích và thiết kế

Bản nháp 0.1 (07/10/2026). Mọi thứ trong thư mục này là **bản nháp trước khảo sát**: dựng từ đề tài và các tài liệu công khai,
sẽ chỉnh lại sau khi phỏng vấn cửa hàng thật (tuần 8–9).

## Phần dùng chung (thư mục `chung/`, G1 giữ)

Bốn gói kia **dùng đúng tên, mã, thuộc tính trong các tệp này**. Muốn đổi thì báo nhóm, không tự sửa trong phần của mình.

| Tệp | Nội dung | Gói phải đọc |
|---|---|---|
| [`quy-uoc-ve.md`](chung/quy-uoc-ve.md) | Công cụ, ký hiệu, cách đặt tên và mã, cỡ chữ khi in | Cả 5 |
| [`tac-nhan-va-thuc-the.md`](chung/tac-nhan-va-thuc-the.md) | Tác nhân nghiệp vụ, nhân viên nghiệp vụ, thực thể nghiệp vụ, use case nghiệp vụ tổng, tác nhân hệ thống | Cả 5 |
| [`danh-muc-use-case.md`](chung/danh-muc-use-case.md) | Use case tổng quát và danh mục 35 use case theo 5 phân hệ, tác nhân của từng use case | Cả 5 |
| [`lop-va-trang-thai-chung.md`](chung/lop-va-trang-thai-chung.md) | Lớp dùng chung, mối kết hợp, trạng thái đơn hàng và trạng thái chiếc máy theo serial | G2, G3, G4, G5 |
| [`mau-dac-ta.md`](chung/mau-dac-ta.md) | Mẫu đặc tả quy trình và đặc tả use case, kèm ví dụ của G1 | Cả 5 |
| `chung/so-do/` | Mã nguồn PlantUML (`.puml`) và ảnh (`.png`) của các sơ đồ chung | |

## Phần riêng của từng gói

Mỗi gói tạo thư mục `g2/`, `g3/`, `g4/`, `g5/` (G1 dùng `g1/`) và tự làm:
quy trình nghiệp vụ (đặc tả + sơ đồ hoạt động), sơ đồ use case phân hệ, đặc tả 3 use case chính, sơ đồ lớp của gói,
thiết kế 2 use case (sơ đồ lớp hiện thực hoá + sơ đồ tuần tự), màn hình trong `giao-dien/`.
Lịch và hạn từng việc xem trang tiến độ của nhóm.
