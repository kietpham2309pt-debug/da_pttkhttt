# Bám sát giáo trình: nội dung 4 chương, quy trình làm và các chỗ đã sửa

Bản 0.1 (07/10/2026) · G1 giữ · **cả nhóm đọc trước khi vẽ**

Nguồn: 4 tệp bài giảng của Bộ môn HTTT: Chương 1 Tổng quan về HTTT (20 slide), Chương 1.2 Giới thiệu công cụ UML (30 slide),
Chương 2 Xác định yêu cầu (52 slide), Chương 3 Mô hình hoá nghiệp vụ (78 slide). Số slide ghi dạng `C3-53` = chương 3, slide 53; `C1.2-30` = chương 1.2, slide 30.
Chương 4 đến 7 (chức năng, cấu trúc, thiết kế lớp, thiết kế chức năng) **chưa có slide**; phần đó tạm theo đề cương, có slide thì đối chiếu tiếp.

## 1. Mỗi chương bắt nhóm làm ra gì

| Chương | Nội dung chính | Sản phẩm đưa vào báo cáo | Tệp của nhóm |
|---|---|---|---|
| 1. Tổng quan HTTT | Hệ thống: tập thành phần liên kết, có **ranh giới**, **môi trường**, **đầu vào, đầu ra**, mục đích (C1-4, C1-5). Hệ thống tổ chức: môi trường kinh tế (khách hàng, nhà cung cấp, ngân hàng) và xã hội (nhà nước) (C1-9); hệ thống quản lý gồm hệ **quyết định**, **tác nghiệp**, **thông tin** (C1-10). HTTT gồm **dữ liệu** (tĩnh, động), **xử lý** (tạo thông tin mới, cập nhật, vận chuyển), **con người** (người dùng; người phát triển) (C1-12 đến C1-16). Phân tích viên đi từ tổng thể xuống phân hệ (top-down) (C1-18) | Chương 1 báo cáo: cửa hàng nhìn như một hệ thống (hình đầu vào, đầu ra, ranh giới như C1-5), môi trường, ba hệ con, bảng dữ liệu tĩnh và động, xử lý chính, con người | [`dan-y-bao-cao-cuoi-ky.md`](../../bao-cao/dan-y-bao-cao-cuoi-ky.md) chương 1 |
| 1.2. UML | 9 sơ đồ: 4 cấu trúc (đối tượng, lớp, thành phần, triển khai), 5 hành vi (use case, trình tự, cộng tác, trạng thái, hoạt động) (C1.2-7). 5 khung nhìn: use case, luận lý, xử lý, thực hiện, triển khai (C1.2-8 đến C1.2-16). Khung nhìn luận lý làm 2 bước: **lớp phân tích** rồi **lớp thiết kế** (C1.2-12). Công cụ: **PowerDesigner, Rational Rose** (C1.2-30) | Mục "công cụ và ngôn ngữ mô hình hoá" ở chương 1; bảng sơ đồ nào dùng ở chương nào (mục 4 dưới đây) | [`quy-uoc-ve.md`](quy-uoc-ve.md) mục 1 |
| 2. Xác định yêu cầu | Yêu cầu **chức năng** và **phi chức năng**; bảng yêu cầu dùng để **định nghĩa phạm vi** (C2-4, C2-5). Phân tích yêu cầu 3 bước: yêu cầu hệ thống hiện tại → các cải tiến → yêu cầu hệ thống mới; 3 chiến lược: **tự động hoá, cải tiến, tái thiết kế** quy trình (C2-7, C2-8). Khảo sát: mục tiêu, nội dung (dữ liệu, xử lý, chính sách, tài nguyên, trình độ tin học, phàn nàn, đề xuất), đối tượng (người dùng, tài liệu, chương trình máy tính) (C2-11 đến C2-17). Kỹ thuật: phỏng vấn 7 bước với **mẫu kế hoạch tổng quan**, **bảng hướng dẫn buổi phỏng vấn**, **biên bản câu hỏi – ghi nhận**; loại câu hỏi mở, đóng, thăm dò; bảng câu hỏi; phỏng vấn nhóm; quan sát; phân tích tài liệu; JAD; bản mẫu (C2-18 đến C2-51) | Chương 2 báo cáo: kế hoạch và biên bản phỏng vấn đúng mẫu, kết quả bảng hỏi, biểu mẫu thu được, hiện trạng, chiến lược chọn, bảng yêu cầu chức năng và phi chức năng | [`ke-hoach-phong-van.md`](../../khao-sat/ke-hoach-phong-van.md), [`kich-ban-phong-van.md`](../../khao-sat/kich-ban-phong-van.md), [`bang-hoi-khach-hang.md`](../../khao-sat/bang-hoi-khach-hang.md) |
| 3. Mô hình hoá nghiệp vụ | Mô hình hoá nghiệp vụ không quan tâm phần mềm, không phân biệt việc làm tay hay tự động (C3-3, C3-6). Phạm vi: tự động hoá hoặc cải tiến thì chỉ mô hình **hiện tại** (C3-5). **6 bước** (C3-7): đánh giá hiện trạng tổ chức → xác định thuật ngữ → xác định tác nhân và use case nghiệp vụ → lập mô hình use case → đặc tả use case → xác định thừa tác viên và thực thể. Mô hình hoá quy trình gồm **sơ đồ đối tượng nghiệp vụ** (cấu trúc), **sơ đồ hoạt động** và **sơ đồ tương tác** (hành vi) (C3-42, C3-43) | Chương 3 báo cáo theo 6 bước của C3-7 rồi tới mô hình hoá quy trình: mỗi use case nghiệp vụ có đặc tả Mẫu 1, sơ đồ đối tượng nghiệp vụ, sơ đồ hoạt động, (sơ đồ tuần tự nghiệp vụ) | [`hien-trang-to-chuc.md`](hien-trang-to-chuc.md), [`tac-nhan-va-thuc-the.md`](tac-nhan-va-thuc-the.md), [`mau-dac-ta.md`](mau-dac-ta.md) |

## 2. Quy trình làm việc của nhóm theo đúng thứ tự giáo trình

Các bước theo thứ tự bài giảng; số việc (t…) trên trang tiến độ ghi để đối chiếu. Danh sách thừa tác viên và thực thể (bước 6 của C3-7) đã có bản nháp từ sớm
để các gói dùng chung tên khi đặc tả; G1 chốt lại sau khi các gói đặc tả xong.

| Tuần | Bước theo giáo trình | Ai | Sản phẩm |
|---|---|---|---|
| 8 (đến 11/10) | Phỏng vấn bước 1–4: chọn người, mục tiêu, câu hỏi, chuẩn bị (C2-19) | G1 dẫn, cả nhóm bổ sung câu | Kịch bản 1.1, kế hoạch phỏng vấn tổng quan, bảng hướng dẫn buổi phỏng vấn (t81, t82, t77) |
| 9 (12–18/10) | Phỏng vấn bước 5–7, quan sát, phân tích tài liệu, phát bảng hỏi (C2-19 đến C2-47) | Ít nhất 3 người đi | Biên bản đã gửi cửa hàng xác nhận, ảnh biểu mẫu, kết quả Google Form (t83, t86) |
| 9 | Đánh giá hiện trạng tổ chức: sơ đồ tổ chức, đối tượng liên quan, giới hạn hệ thống, bảng vấn đề (C3-8 đến C3-15) | G1 | `hien-trang-to-chuc.md` viết lại theo cửa hàng thật (t85) |
| 9 | Hiện trạng và yêu cầu: bảng yêu cầu chức năng, phi chức năng; chọn chiến lược (C2-4, C2-8) | Mỗi gói phần mình | Bảng yêu cầu của gói (t84) |
| 10 | Xác định thuật ngữ; xác định tác nhân và use case nghiệp vụ (C3-17 đến C3-30) | G1 chốt, các gói gửi danh sách | `hien-trang-to-chuc.md` mục 5, `tac-nhan-va-thuc-the.md` mục 1, 4 (t91) |
| 10 | Lập mô hình use case nghiệp vụ tổng (C3-31 đến C3-37) | G1 | `uc-nghiep-vu-tong` (t95) |
| 10 | Đặc tả từng use case nghiệp vụ theo Mẫu 1 (C3-38 đến C3-41) | Mỗi gói use case của mình | Theo khuôn ví dụ BUC-07 trong `mau-dac-ta.md` (t92) |
| 10 | Xác định thừa tác viên và thực thể (C3-44 đến C3-50) | G1 chốt | `tac-nhan-va-thuc-the.md` mục 2, 3, 5 (t91) |
| 10–11 | Mô hình hoá quy trình: sơ đồ đối tượng nghiệp vụ, sơ đồ hoạt động, sơ đồ tuần tự nghiệp vụ (C3-42 đến C3-78) | Mỗi gói use case của mình | Theo khuôn ví dụ BUC-07 (t93, t94) |
| 11 trở đi | Chương 4–7 theo đề cương: use case hệ thống, sơ đồ lớp, thiết kế lớp và cơ sở dữ liệu, thiết kế chức năng 3 tầng | Mỗi gói phân hệ của mình | Đối chiếu khi có slide |

**Mỗi người tự vẽ đủ loại sơ đồ của gói mình**, vì đề thi tự luận cuối kỳ (70% điểm học phần) chấm đúng các phần này:
đặc tả quy trình nghiệp vụ 20 điểm, mô hình hoá chức năng 30, mô hình hoá cấu trúc 30, thiết kế chức năng 20.

## 3. Ký hiệu

![Ký hiệu nghiệp vụ](so-do/ky-hieu-nghiep-vu.png)

Chi tiết từng loại sơ đồ (12 ký hiệu sơ đồ hoạt động, cách ghi thông điệp sơ đồ tuần tự…) ở [`quy-uoc-ve.md`](quy-uoc-ve.md) mục 3.

## 4. Sơ đồ UML nào dùng ở phần nào của báo cáo

Khung nhìn xếp theo C1.2-11 đến C1.2-16: khung nhìn luận lý gồm phần tĩnh (lớp, đối tượng) và phần động (tương tác, trạng thái, hoạt động) (C1.2-12);
khung nhìn xử lý được thể hiện cùng các sơ đồ đó (C1.2-13).

| Sơ đồ (C1.2-7) | Khung nhìn | Phần báo cáo | Ai vẽ |
|---|---|---|---|
| Use case nghiệp vụ | Use case (C1.2-11) | Chương 3: mô hình use case nghiệp vụ tổng | G1 |
| Đối tượng (sơ đồ đối tượng nghiệp vụ) | Luận lý, phần tĩnh | Chương 3: mỗi use case nghiệp vụ | Mỗi gói |
| Hoạt động | Luận lý, phần động | Chương 3: mỗi use case nghiệp vụ | Mỗi gói |
| Trình tự, cộng tác (mức nghiệp vụ) | Luận lý, phần động | Chương 3: nếu GV yêu cầu | Mỗi gói |
| Use case hệ thống | Use case (C1.2-11) | Chương 4 | G1 tổng quát, mỗi gói phân rã |
| Lớp (phân tích) | Luận lý, phần tĩnh (bước 1 của C1.2-12) | Chương 5 | Mỗi gói, G1 ghép |
| Trạng thái | Luận lý, phần động | Chương 5: `DonHang`, `SanPhamSerial` | G4, G5 |
| Lớp (thiết kế) | Luận lý, phần tĩnh (bước 2 của C1.2-12) | Chương 6 | Mỗi gói |
| Trình tự (mức hệ thống) | Luận lý, phần động | Chương 7: hiện thực hoá use case | Mỗi gói |
| Thành phần, triển khai | Thực hiện (C1.2-14, C1.2-15), triển khai (C1.2-16) | Chương 7 nếu GV yêu cầu | G1 |

## 5. Đối chiếu: trước đây nhóm làm gì, giáo trình yêu cầu gì, đã sửa gì (07/10/2026)

| Giáo trình | Bản trước | Đã sửa |
|---|---|---|
| Gọi người thực hiện nghiệp vụ là **thừa tác viên** (C3-45) | "Nhân viên nghiệp vụ" | Đổi hết trong tài liệu chung, kịch bản phỏng vấn, báo cáo tiến độ |
| Thừa tác viên chia **2 loại** (C3-46, C3-47); mô tả bằng **5 câu hỏi** (C3-45) | Chỉ có cột "việc chính" | Thêm loại, tác nhân giao tiếp, bảng 5 câu hỏi (`tac-nhan-va-thuc-the.md` mục 2) |
| Thực thể chia **thông tin / vật thể** (C3-48, C3-49) | Không phân nhóm | Thêm cột "Nhóm" |
| Tác nhân ghi `«business actor»`, ranh giới có tên ở đỉnh (C3-21) | Không ghi; ô use case có mã | Vẽ lại sơ đồ use case nghiệp vụ tổng; mã để trong bảng |
| Use case nghiệp vụ tên là **động từ** (C3-27) | "Báo cáo kinh doanh", "Bảo hành" | "Lập báo cáo kinh doanh", "Bảo hành sản phẩm" |
| Mô hình use case nghiệp vụ có **«include», «extend»** (C3-33 đến C3-37) | Không có | Thêm BUC-08 Lắp đặt tận nơi «extend» Giao hàng; BUC-09 Kiểm tra máy theo số serial được Đổi trả, Bảo hành «include» |
| **Đánh giá hiện trạng tổ chức** và **thuật ngữ** là 2 bước đầu (C3-7 đến C3-17) | Chưa có | Thêm `hien-trang-to-chuc.md` (bản giả định, viết lại sau khảo sát) và sơ đồ tổ chức |
| Đặc tả use case nghiệp vụ theo **Mẫu 1**: giới thiệu, các dòng cơ bản, các dòng thay thế (C3-40, C3-41) | Bảng tự đặt "Mục / Nội dung", bước dạng bảng | Viết lại ví dụ BUC-07 đúng Mẫu 1, giữ bảng phụ (tác nhân, ràng buộc, quy tắc) |
| **Sơ đồ đối tượng nghiệp vụ** có bản số (C3-51) | Chưa có | Thêm ví dụ BUC-07 |
| Mỗi bước trong dòng cơ bản bắt đầu bằng **thừa tác viên** (C3-41, C3-58); sơ đồ hoạt động ví dụ chỉ có **luồng thừa tác viên** (C3-59) | Bước đầu, bước cuối lấy chủ cửa hàng làm chủ ngữ; có luồng Chủ cửa hàng | Quản lý cửa hàng tiếp nhận yêu cầu và trình duyệt; bỏ luồng tác nhân |
| Sơ đồ hoạt động: nút quyết định **không chữ**, điều kiện trên nhánh; **nút kết hợp**; **thanh tách, kết hợp**; **dòng đối tượng nét đứt** (C3-53, C3-54) | Câu hỏi viết trong hình thoi, đối tượng nối nét liền, không có xử lý song song | Vẽ lại QT-01: thủ kho kiểm kê song song với kế toán, báo cáo đi bằng dòng đối tượng, nút đối tượng có trạng thái `[đã có đề xuất]`, vòng trình lại đi qua nút kết hợp |
| **Sơ đồ tuần tự nghiệp vụ** (C3-63 đến C3-72) | Chưa có | Thêm ví dụ BUC-07 |
| Phỏng vấn theo **3 mẫu bảng** (C2-22, C2-23, C2-26) | Chỉ có kịch bản câu hỏi | Thêm `khao-sat/ke-hoach-phong-van.md` |

## 6. Chỗ bài giảng chưa thống nhất và cách nhóm chọn

Nên hỏi GV các điểm này; chưa hỏi được thì làm theo cột cuối.

| Điểm | Bài giảng | Nhóm chọn |
|---|---|---|
| Hình thừa tác viên giao tiếp với tác nhân | C3-47, C3-66 dùng **hình biên** cho thừa tác viên giao tiếp với môi trường; nhưng C3-51, C3-72 vẽ Thủ thư (giao tiếp với độc giả) bằng hình thừa tác viên thường | Theo C3-47, C3-66: giao tiếp dùng hình biên, làm bên trong dùng hình tròn có người. GV chỉ dùng một hình thì đổi lại |
| Nút đối tượng trong sơ đồ hoạt động | C3-59 (Rose) vẽ thực thể hình tròn bên cạnh, nối nét đứt vào hành động; bảng C3-54 cho phép hình chữ nhật | Sơ đồ chung dùng hình chữ nhật đặt giữa hai hành động (PlantUML không vẽ được kiểu Rose). Ai vẽ bằng Rose thì làm như C3-59 |
| Luồng (swimlane) | C3-59 chỉ một luồng Thủ thư; C3-57 nói mỗi luồng dành cho "lớp đối tượng, người hay phòng ban" | Mỗi thừa tác viên một luồng; không vẽ luồng tác nhân, tác nhân chỉ là nơi gửi yêu cầu và nhận kết quả như C3-59 |
| Chủ cửa hàng là tác nhân dù ở trong tổ chức | C3-13: có "đối tượng bên trong tổ chức nhưng nằm ngoài hệ thống nghiệp vụ"; C3-26: tác nhân là người kích hoạt, nhận kết quả; nhưng C3-22: tác nhân "không điều khiển hoạt động của hệ thống" mà chủ lại là người quyết định việc kinh doanh | Giữ Chủ cửa hàng là tác nhân của BUC-07 (trong use case này chủ chỉ yêu cầu và nhận báo cáo); nếu GV không đồng ý thì bỏ BUC-07 khỏi sơ đồ tổng |
| Chương 1 trong đề cương có 1.2 Vòng đời phát triển HTTT, 1.4 Một số mô hình tiêu biểu | Slide chương 1 không có hai mục này | Ôn thi đọc thêm giáo trình; báo cáo không cần |
| Slide cuối chương 2 | Ghi "Hết chương 3" | Lỗi đánh máy, không ảnh hưởng |
