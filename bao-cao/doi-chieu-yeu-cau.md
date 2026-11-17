# Đối chiếu bài tập nhóm với yêu cầu của giảng viên

Nguồn yêu cầu: đề cương học phần Phân tích thiết kế hệ thống thông tin (0101003615, Mẫu 9b, cập nhật 22/01/2026), mục 5 (nội dung chương 1–7) và mục 7 (đánh giá: bài tập nhóm R1_BTN "Phân tích thiết kế một hệ thống thông tin cụ thể trong thực tế", nộp sau khi học xong chương 7).
Báo cáo đối chiếu: `bao-cao/BTN_Nhom4_WebsiteBanHangDienGiaDung.docx` (và `.pdf`). Kiểm ngày 17/11/2026.

## 1. Nội dung theo chương của đề cương

| Đề cương | Yêu cầu | Trong báo cáo | Mức đáp ứng |
|---|---|---|---|
| Ch.2 Xác định yêu cầu | Khảo sát, xác định yêu cầu | Mục 1, 2; `khao-sat/` (35 tài liệu công khai TL, phỏng vấn mô phỏng MP/MK), 46 yêu cầu chức năng + phi chức năng | Đủ về hình thức; **không có khảo sát cửa hàng thật** (xem mục 3) |
| Ch.3 Mô hình hoá nghiệp vụ | Tác nhân, thừa tác viên, use case nghiệp vụ, đặc tả, sơ đồ đối tượng, hoạt động, tuần tự nghiệp vụ | Mục 3, 4.1, 5.1, 8: 9 BUC, mỗi BUC đủ Mẫu 1 + sơ đồ đối tượng + hoạt động (swimlane) + tuần tự | Đủ |
| Ch.4 Mô hình hoá chức năng | Tác nhân, quan hệ giữa tác nhân, use case, quan hệ include/extend, mô tả use case | Mục 3.4 (tổng quát hoá tác nhân), 4.2–4.4 (35 use case, sơ đồ tổng quát + 5 sơ đồ phân hệ), 5.2 (đặc tả **35/35** use case, mỗi cái kèm sơ đồ hoạt động) | Đủ |
| Ch.5 Mô hình hoá cấu trúc | Đối tượng, lớp, mối kết hợp, lớp kết hợp, tổng hợp/thành phần, tổng quát hoá; xác định lớp (cụm danh từ, phân loại, use case); thuộc tính, phương thức | Mục 9: xác định lớp theo cụm danh từ, sơ đồ lớp phân tích 5 phân hệ + tổng hợp, sơ đồ trạng thái (đơn hàng, chiếc máy, đổi trả, bảo hành), bảng mô tả lớp | Đủ; quan hệ thành phần (◆) và tổng quát hoá có trên sơ đồ, **chưa có quan hệ tổng hợp (◇)** vì nghiệp vụ không có cặp lớp nào hợp kiểu này |
| Ch.6 Thiết kế lớp | Tiên đề, hệ luận; tinh chế thuộc tính, mối kết hợp, phương thức; chuyển lớp và liên kết sang bảng | Mục 10: bảng tiên đề – hệ luận áp dụng, tinh chế lớp, quy tắc chuyển đổi, sơ đồ lớp thiết kế, lược đồ quan hệ 31 bảng, từ điển dữ liệu | Đủ |
| Ch.7 Thiết kế chức năng | Kiến trúc 3 tầng; lớp tầng nghiệp vụ, truy cập dữ liệu, giao diện; sơ đồ lớp hiện thực hoá + sơ đồ tương tác cho use case | Mục 11: kiến trúc 3 tầng, bảng ánh xạ tầng → mã nguồn, **35/35** use case có sơ đồ lớp hiện thực hoá và sơ đồ tuần tự, 61 màn hình + 14 ảnh màn hình | Đủ |
| (thêm) | Bản chạy thử | Mục 12 + `website/` (Spring Boot + MySQL, 35 use case chạy được) | Vượt yêu cầu (đề cương không bắt buộc viết chương trình) |

Tổng số sơ đồ trong báo cáo: khoảng 180 hình.

## 2. Tiêu chí chấm R1_BTN

| Tiêu chí | Tỉ trọng | Phần làm được | Rủi ro |
|---|---|---|---|
| Mô hình hoá nghiệp vụ | 20% | 9 BUC đủ 4 thành phần | Thấp |
| Mô hình hoá chức năng | 10% | 35 use case đủ đặc tả và sơ đồ hoạt động | Thấp |
| Mô hình hoá cấu trúc | 20% | Sơ đồ lớp phân tích, thiết kế, trạng thái, CSDL | Thấp |
| Thiết kế chức năng | 10% | 3 tầng + 35 hiện thực hoá + giao diện | Thấp |
| Tính thực tế và khả thi | 20% | Dữ kiện từ 35 tài liệu thật; bản chạy thử chạy được | **Cao**: đối tượng là cửa hàng giả định, không phỏng vấn cửa hàng thật (câu Q-01) |
| Phối hợp nhóm | 20% | Phân 5 gói G1–G5, trang tiến độ | **Cao**: toàn bộ 22 commit trên GitHub đều của nhóm trưởng; 4 thành viên chưa có đóng góp nào ghi nhận được |

## 3. Việc nên làm trước khi nộp

1. Hỏi giảng viên các câu Q-01 (chấp nhận cửa hàng giả định?), Q-02 (công cụ vẽ bắt buộc là Rational Rose?), Q-06 (có chấm bản chạy thử?), Q-07 (hạn, hình thức nộp).
2. Mỗi thành viên G2–G5 đọc lại phần phân hệ của mình (đặc tả, sơ đồ) và tự commit chỉnh sửa lên GitHub, để có bằng chứng phối hợp; thi cuối kỳ (70%) là tự luận cá nhân về đúng các loại sơ đồ này.
3. Nếu giảng viên bắt buộc Rational Rose: vẽ lại bằng Rose từ các tệp `.puml` (nội dung giữ nguyên).
4. Điền tên giảng viên đầy đủ trên trang bìa (đề cương ghi ThS. Nguyễn Thị Thanh Thủy, nhóm đang ghi "Nguyễn Thị Thúy A").
