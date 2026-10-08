# Hiện trạng và yêu cầu của hệ thống

Bản nháp 0.1 (08/10/2026) · G1 làm cho cả 5 gói · việc t84 · theo chương 2 bài giảng: bảng yêu cầu (C2-4 đến C2-6), chiến lược (C2-7, C2-8), nội dung khảo sát (C2-12 đến C2-14)

Từ 08/10/2026 nhóm trưởng làm toàn bộ phần khảo sát để đỡ tốn thời gian của nhóm. Mỗi gói **đọc lại phần của mình** trước khi đặc tả use case nghiệp vụ ở tuần 10.

**Cột Nguồn** dùng ba loại mã:

| Mã | Nghĩa | Trạng thái |
|---|---|---|
| `TL-xx` | Tài liệu công khai trong [`../../khao-sat/phan-tich-tai-lieu.md`](../../khao-sat/phan-tich-tai-lieu.md) | Đã có |
| `PV A1`… | Câu trong [`../../khao-sat/kich-ban-phong-van.md`](../../khao-sat/kich-ban-phong-van.md) | **Chờ phỏng vấn** (t83) |
| `K1`… | Câu trong [`../../khao-sat/bang-hoi-khach-hang.md`](../../khao-sat/bang-hoi-khach-hang.md) | **Chờ bảng hỏi** (t86) |

> Quy tắc: ô ghi **"chờ PV"** chỉ được thay bằng điều cửa hàng thật nói; số liệu bảng hỏi chỉ lấy từ `xu_ly_bang_hoi.py` chạy trên tệp CSV thật.
> Không viết trước câu trả lời của cửa hàng, không lấy dữ liệu trong `khao-sat/mo-phong/` (AI đóng vai) làm số liệu.
> Yêu cầu nào sau khảo sát không còn nguồn nào thì gạch bỏ, không giữ lại cho đẹp bảng.

## 1. Chiến lược phân tích yêu cầu (C2-7, C2-8)

| Chiến lược | Nghĩa theo bài giảng | Hợp với đề tài không |
|---|---|---|
| Tự động hoá quy trình | Hoạt động không đổi, dùng máy làm thay một số việc | Một phần: ghi sổ kho, cộng báo cáo |
| **Cải tiến quy trình** | Phân tích hệ thống hiện tại, tìm giải pháp cho hệ thống mới | **Dự kiến chọn** |
| Tái thiết kế quy trình | Phá bỏ cách kinh doanh hiện tại, đổi lớn | Không: cửa hàng vẫn bán tại chỗ, chỉ thêm kênh |

Lý do dự kiến: cửa hàng giữ cách bán tại chỗ, **thêm** kênh đặt hàng trực tuyến và **sửa** những bước dự đoán là hay sai (ghi serial bằng tay, đối chiếu tiền thu khi giao cuối tháng, tra bảo hành bằng sổ: **giả định**, vấn đề 1–3 ở `hien-trang-to-chuc.md`, chờ PV A5, F1).
Ngành đã có sẵn cách làm chuẩn để học theo (TL mục 3: gọi xác nhận đơn, tách công lắp và vật tư, phân loại đổi trả). Chốt sau phỏng vấn câu **PV F1, F2** và ghi lý do.

## 2. Hiện trạng theo từng gói (C2-12 đến C2-14)

Cột **Ngành** lấy từ tài liệu. Cột **Cửa hàng khảo sát** để trống tới khi phỏng vấn; câu hỏi tương ứng ghi sẵn.

### G1 · Tổ chức, tin học hoá, báo cáo

| Nội dung | Ngành (tài liệu) | Cửa hàng khảo sát |
|---|---|---|
| Cơ cấu, ai kiêm vai nào | Nguyễn Kim có trung tâm bảo hành riêng (TL-09); Điện Máy Xanh có thợ lắp mang tên chuỗi (TL-06); Kim Quốc Tiến để nhân viên của mình đi lắp (TL-14) | Chờ PV A2 |
| Ghi chép đang dùng | Hộ kinh doanh từ 2026 phải tự kê khai thuế, ghi sổ theo Thông tư 152/2025 (TL-27, TL-30) | Chờ PV A3 |
| Báo cáo cuối ngày, cuối tháng | Sổ chi tiết hàng hoá S2d-HKD: nhập, xuất, tồn theo số lượng và tiền (TL-30) | Chờ PV A4 |
| Sai sót và truy vết | Không có tài liệu công khai | Chờ PV A5, F1 |
| Trình độ tin học người dùng (C2-14) | Không có tài liệu công khai | Chờ PV F3 |

### G2 · Sản phẩm, giá, khuyến mãi

| Nội dung | Ngành (tài liệu) | Cửa hàng khảo sát |
|---|---|---|
| Thông tin khách hỏi | 4/4 website lọc theo hãng và thông số (công suất, dung tích), có so sánh và đánh giá; ít nhất 2/4 có hỏi đáp (TL-19) | Chờ PV B2 |
| Đưa mẫu máy mới lên bán | Không có tài liệu công khai về quy trình nội bộ | Chờ PV B1 |
| Giá | Giá niêm yết đã gồm VAT (TL-10, TL-11) | Chờ PV B3, B6 |
| Khuyến mãi | 4/4 có mã giảm giá và khuyến mãi theo sản phẩm (TL-19) | Chờ PV B4 |
| Nhãn năng lượng | Máy lạnh, tủ lạnh, nồi cơm, bình nóng lạnh bắt buộc dán nhãn (TL-31) | Chờ PV B2 |

### G3 · Bán hàng, đặt hàng, thanh toán

| Nội dung | Ngành (tài liệu) | Cửa hàng khảo sát |
|---|---|---|
| Kênh khách đặt | Chuỗi lớn: website, ứng dụng, cửa hàng; giá trung bình mỗi sản phẩm điện gia dụng trên sàn khoảng 323 nghìn (TL-34), nhóm suy ra sàn chủ yếu bán đồ nhỏ (suy luận của nhóm) | Chờ PV C1; K5, K6 |
| Đặt hàng | Đặt bằng họ tên, số điện thoại, địa chỉ; MediaMart ghi rõ không cần tài khoản (TL-18); xác nhận đơn qua điện thoại (TL-02, TL-11) | Chờ PV C2 |
| Thanh toán | Tiền mặt khi nhận, chuyển khoản hoặc QR, thẻ, trả góp (TL-05, TL-13); một nơi bắt đặt trước 20–30% với đơn trả tiền khi nhận (TL-17) | Chờ PV C3; K10 |
| Huỷ đơn, bỏ đơn | Nhà bán được huỷ đơn, hoàn đủ tiền (TL-13); nơi có cọc thì giữ cọc khi khách bỏ đơn (TL-17) | Chờ PV C4, C6 |
| Khách hỏi đơn tới đâu | Chuỗi lớn cho tra đơn bằng số điện thoại + OTP (TL-04, TL-10) | Chờ PV C7 |

### G4 · Xử lý đơn, giao hàng, lắp đặt, đổi trả

| Nội dung | Ngành (tài liệu) | Cửa hàng khảo sát |
|---|---|---|
| Xác nhận đơn, hẹn giờ | Cả 4 nơi gọi điện cho khách trước khi giao: xác nhận đơn (TL-02, TL-10) hoặc hẹn giờ giao (TL-11, TL-14); trên các trang đọc được không thấy nơi nào cho khách tự chọn khung giờ giao lắp tại nhà (TL-18) | Chờ PV D1, D6; K13 |
| Phí giao | Miễn phí theo ngưỡng giá trị đơn và khoảng cách (TL-01, TL-07, TL-11, TL-14) | Chờ PV D3 |
| Lắp đặt | Tách công lắp và vật tư theo bảng giá; việc ngoài danh mục khảo sát rồi báo giá (TL-01, TL-08) | Chờ PV D2 |
| Thu tiền khi giao | Hoá đơn lập lúc giao, kể cả khi chưa thu tiền (TL-26) | Chờ PV D5 |
| Đổi trả | Phân biệt lỗi nhà sản xuất và không lỗi, phí khấu trừ theo %; cần biên bản lỗi; luật bắt nhận lại hàng không đúng mô tả (TL-03, TL-12, TL-15, TL-20 Đ16) | Chờ PV D4; K14 |

### G5 · Nhập hàng, kho, bảo hành

| Nội dung | Ngành (tài liệu) | Cửa hàng khảo sát |
|---|---|---|
| Nhập hàng | Mẫu phiếu nhập 01-VT: số lượng theo chứng từ và thực nhập (TL-28, TL-29) | Chờ PV E1, E2 |
| Ghi serial | Serial trên máy phải khớp phiếu bảo hành (TL-16, TL-18) | Chờ PV E2, E5 |
| Xuất kho | Mẫu phiếu xuất 02-VT: lý do xuất, số lượng yêu cầu và thực xuất (TL-28) | Chờ PV D1, E5 |
| Kiểm kê | Sổ S2d-HKD (TL-30) | Chờ PV E3 |
| Bảo hành | Bảo hành gốc của hãng; tra cứu bằng số điện thoại, IMEI, mã phiếu; luật: giao văn bản tiếp nhận, sửa 3 lần không được thì đổi mới hoặc hoàn tiền (TL-04, TL-16, TL-18, TL-22 Đ30) | Chờ PV E4; K15 |

## 3. Bảng yêu cầu chức năng (C2-4)

Cột **Ưu tiên** là tạm tính, xét lại sau khi có PV F2 và câu K8 của bảng hỏi.

### G1 · Quản trị hệ thống và báo cáo

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 1 | YC-HT-01 | Nhân viên đăng nhập; mỗi vai chỉ thấy chức năng của vai mình, một người được kiêm nhiều vai | PV A2, A3 | UC-HT-01, UC-HT-03 | Cao |
| 2 | YC-HT-02 | Quản lý tài khoản nhân viên: thêm, khoá, gán vai | PV A2 | UC-HT-02 | Cao |
| 3 | YC-HT-03 | Báo cáo doanh thu theo ngày, tháng, nhóm hàng; dùng được cho tự kê khai thuế của hộ kinh doanh | PV A4; TL-27 | UC-HT-04 | Cao |
| 4 | YC-HT-04 | Báo cáo nhập – xuất – tồn theo mẫu máy (dạng sổ S2d-HKD) và báo cáo máy đang bảo hành | PV A4, E3; TL-30 | UC-HT-05 | Trung bình |
| 5 | YC-HT-05 | Lưu vết ai sửa giá, ai đổi trạng thái đơn, lúc nào | PV A5 | UC-SP-03, UC-DH-03 | Trung bình |
| 6 | YC-HT-06 | Trang thông tin cửa hàng (tên, địa chỉ, mã số thuế, điện thoại) và bộ trang chính sách (giá, giao hàng, đổi trả, bảo hành, thanh toán, bảo mật, khiếu nại); liên kết đặt ở chân trang mọi màn hình | TL-20 Đ11; TL-21 | Chưa có (đề xuất) | Cao |
| 7 | YC-HT-07 | Khách gửi yêu cầu xem, sửa, xoá dữ liệu cá nhân hoặc rút đồng ý; hệ thống đếm hạn xử lý (phản hồi 2 ngày làm việc) | TL-24 Đ4, Đ10; TL-25 Đ5 | Chưa có (đề xuất) | Thấp |
| 8 | YC-HT-08 | Khách gửi khiếu nại trực tuyến, có mã và trạng thái; báo đã tiếp nhận trong 3 ngày làm việc | TL-22 Đ31; TL-23 Đ22 | Chưa có (đề xuất) | Trung bình |
| 9 | YC-HT-09 | Nhân viên và khách tự đổi mật khẩu; đổi xong thì các phiên đăng nhập cũ hết hiệu lực | Yêu cầu hệ thống (C2-3), không cần hỏi cửa hàng; đi cùng YC-PCN-02 | UC-HT-06 | Thấp |

### G2 · Sản phẩm và khuyến mãi

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 10 | YC-SP-01 | Quản lý danh mục theo nhóm hàng và thương hiệu | PV A1, B1; TL-19 | UC-SP-01 | Cao |
| 11 | YC-SP-02 | Mỗi mẫu máy có thông số kỹ thuật lọc được (công suất, dung tích, kích thước, kích thước khoét đá với bếp âm), ảnh, thời hạn bảo hành, nhãn năng lượng | PV B2; TL-19, TL-31; K8 | UC-SP-02 | Cao |
| 12 | YC-SP-03 | Khách tìm theo tên hoặc model; lọc theo khoảng giá, hãng, thông số | TL-19; K9 | UC-SP-05 | Cao |
| 13 | YC-SP-04 | Khách so sánh 2–3 mẫu máy theo thông số | TL-19; K8 | UC-SP-06 | Trung bình |
| 14 | YC-SP-05 | Giá bán hiển thị đã gồm VAT; phí giao và lắp ghi riêng | TL-10, TL-11; TL-20 Đ12 | UC-SP-03 | Cao |
| 15 | YC-SP-06 | Khuyến mãi theo mẫu máy và mã giảm giá có hạn dùng; ghi người duyệt | PV B4; TL-19 | UC-SP-04 | Trung bình |
| 16 | YC-SP-07 | Chỉ khách đã nhận hàng mới được đánh giá; có hỏi đáp dưới trang sản phẩm | PV B7; TL-19 | UC-SP-07 | Thấp |
| 17 | YC-SP-08 | Thông tin mẫu máy đã đăng giữ ít nhất 1 năm; đổi giá không làm đổi giá trên đơn cũ | TL-20 Đ16 | UC-SP-02, UC-SP-03 | Trung bình |

### G3 · Mua hàng trực tuyến và thanh toán

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 18 | YC-MH-01 | Đặt hàng chỉ với họ tên, số điện thoại, địa chỉ, không bắt tạo tài khoản | TL-18; K11 | UC-MH-04 | Cao |
| 19 | YC-MH-02 | Khách muốn thì tạo tài khoản để lưu địa chỉ và xem đơn cũ | PV C5; K11 | UC-MH-01, UC-MH-02 | Trung bình |
| 20 | YC-MH-03 | Màn hình xác nhận đơn hiện đủ sản phẩm, số lượng, thời hạn giao, khuyến mãi, tiền hàng, phí giao, phí lắp, cách thanh toán; khách sửa được rồi bấm đồng ý điều kiện giao dịch | TL-20 Đ12 | UC-MH-04 | Cao |
| 21 | YC-MH-04 | Ô đồng ý xử lý dữ liệu **không tích sẵn**, tách mục đích giao hàng và bảo hành với mục đích quảng cáo; lưu lại lần đồng ý | TL-24 Đ9; TL-25 Đ6 | UC-MH-04 | Cao |
| 22 | YC-MH-05 | Thanh toán: tiền mặt khi nhận, chuyển khoản hoặc QR, thẻ; trả góp qua công ty tài chính nếu cửa hàng có | PV C3; TL-05, TL-13; K10 | UC-MH-05 | Cao |
| 23 | YC-MH-06 | Đặt cọc một phần (TL-17: đơn trả tiền khi nhận đặt trước 20–30%); cửa hàng khảo sát có cọc không, đơn nào phải cọc thì hỏi PV; ghi số tiền cọc trên đơn | PV C3, C6; TL-17 | UC-MH-05 | Chờ PV |
| 24 | YC-MH-07 | Áp một mã giảm giá còn hạn vào đơn | PV B4; TL-19 | UC-MH-08 | Trung bình |
| 25 | YC-MH-08 | Tra đơn bằng số điện thoại kèm mã đơn | PV C7; TL-04, TL-10 | UC-MH-06 | Cao |
| 26 | YC-MH-09 | Huỷ đơn khi chưa xuất kho; hoàn đủ tiền đã thu | PV C4; TL-13 | UC-MH-07 | Trung bình |
| 27 | YC-MH-10 | Khách xem, tải, in hoá đơn và chứng từ của đơn | TL-22 Đ29; TL-26 | UC-MH-06 | Trung bình |
| 28 | YC-MH-11 | Giỏ hàng: thêm nhiều mẫu máy, sửa số lượng, chọn có lắp đặt hay không; giữ giỏ khi chưa đăng nhập | TL-19; K12 | UC-MH-03 | Cao |

### G4 · Xử lý đơn, giao hàng, lắp đặt, đổi trả

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 29 | YC-DH-01 | Đơn mới hiện ở đầu danh sách "chờ xác nhận"; nhân viên gọi khách rồi xác nhận hoặc huỷ, ghi lý do | PV D1; TL-02, TL-10, TL-11, TL-14 | UC-DH-01 | Cao |
| 30 | YC-DH-02 | Hẹn ngày và khung giờ giao lắp với khách; xếp lịch thợ không trùng giờ | PV D6; TL-14; K13 | UC-DH-02 | Cao |
| 31 | YC-DH-03 | Phí giao tính theo ngưỡng giá trị đơn và khoảng cách (G4 đặt mức phí), hiện cho khách trước khi đặt (G3) | PV D3; TL-01, TL-07, TL-11, TL-14; K12 | UC-MH-04 | Trung bình |
| 32 | YC-DH-04 | Cập nhật trạng thái giao: đang giao, đã giao, giao không thành công (kèm lý do) | PV D1, D3 | UC-DH-03 | Cao |
| 33 | YC-DH-05 | Biên bản lắp đặt ghi công lắp và vật tư phát sinh theo bảng giá, khách ký xác nhận | PV D2; TL-01, TL-08, TL-14 | UC-DH-05 | Trung bình |
| 34 | YC-DH-06 | Ghi tiền thu khi giao theo từng đơn, quản lý xác nhận trong ngày; đơn đã giao thì sinh yêu cầu lập hoá đơn dù chưa thu tiền | PV D5; TL-26 | UC-DH-04 | Cao |
| 35 | YC-DH-07 | Yêu cầu đổi trả ghi loại lý do (lỗi nhà sản xuất, không đúng mô tả, đổi ý), phí khấu trừ, người duyệt; lý do "không đúng mô tả" thì bắt buộc nhận lại | PV D4; TL-03, TL-12, TL-15, TL-20 Đ16 | UC-DH-06 | Cao |
| 36 | YC-DH-08 | Hoàn tiền theo cách khách đã trả (luật bắt buộc khi khách chấm dứt hợp đồng vì thông tin thiếu hoặc sai; trường hợp khác theo chính sách cửa hàng) | PV D4; TL-03; TL-22 Đ38 k3, k4 | UC-DH-06 | Trung bình |

### G5 · Kho, nhà cung cấp, bảo hành

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 37 | YC-KB-01 | Quản lý nhà cung cấp và đơn đặt hàng nhà cung cấp | PV E1 | UC-KB-01, UC-KB-02 | Trung bình |
| 38 | YC-KB-02 | Phiếu nhập theo mẫu 01-VT (số lượng theo chứng từ và thực nhập) và ghi số serial từng chiếc máy | PV E2; TL-28, TL-29 | UC-KB-03 | Cao |
| 39 | YC-KB-03 | Phiếu xuất theo mẫu 02-VT (lý do xuất, số lượng yêu cầu và thực xuất); chọn đúng serial đi theo đơn | PV D1, E5; TL-28 | UC-KB-04 | Cao |
| 40 | YC-KB-04 | Phiếu nhập, xuất đã duyệt thì khoá; sai thì lập phiếu điều chỉnh | TL-28 (TT 99 Đ10) | UC-KB-03, UC-KB-04 | Trung bình |
| 41 | YC-KB-05 | Kiểm kê, so số trên hệ thống với số đếm; cảnh báo mẫu máy dưới mức tồn tối thiểu | PV E1, E3; TL-30 | UC-KB-05 | Trung bình |
| 42 | YC-KB-06 | Khách và nhân viên tra bảo hành theo **số serial** hoặc số điện thoại: ra ngày bán, hạn bảo hành, các lần bảo hành | PV E4; TL-04, TL-16, TL-18; K15 | UC-KB-07 | Cao |
| 43 | YC-KB-07 | Phiếu tiếp nhận bảo hành có lỗi, ngày hẹn trả; đếm số lần bảo hành của chiếc máy, cảnh báo ở lần thứ 3; thời gian sửa không tính vào hạn | PV E4; TL-22 Đ30 | UC-KB-06 | Cao |
| 44 | YC-KB-08 | Theo dõi máy đang gửi trung tâm bảo hành hãng (ngày gửi, ngày nhận lại) | PV E4; TL-16 | UC-KB-06 | Trung bình |
| 45 | YC-KB-09 | Nhận lại máy về kho khi giao không thành hoặc khách đổi trả; ghi tình trạng máy | PV C6, D4 | UC-KB-08 | Trung bình |

## 4. Yêu cầu phi chức năng (C2-4)

| STT | Mã | Yêu cầu | Nguồn | Ưu tiên |
|---|---|---|---|---|
| 1 | YC-PCN-01 | Không xoá cứng đơn hàng; giữ dữ liệu hợp đồng ít nhất 3 năm (hộ kinh doanh được giữ ít nhất 1 năm, chỉ trong 5 năm đầu kể từ khi thành lập); tài liệu kế toán của hộ ít nhất 5 năm | TL-20 Đ16; TL-30 | Cao |
| 2 | YC-PCN-02 | Mật khẩu lưu dạng băm; chỉ vai được giao mới xem số điện thoại, địa chỉ khách | TL-24; TL-25 | Cao |
| 3 | YC-PCN-03 | Giao diện tiếng Việt, ít bước, dùng được trên điện thoại cho nhân viên giao hàng | PV F3 (C2-14: trình độ tin học) | Cao |
| 4 | YC-PCN-04 | Hoá đơn lấy từ phần mềm hoá đơn điện tử hoặc máy tính tiền của cửa hàng; website chỉ lưu số, ký hiệu và đường dẫn tra cứu, không tự phát hành | TL-26 | Trung bình |
| 5 | YC-PCN-05 | Trang danh sách và chi tiết sản phẩm mở xong trong khoảng 3 giây trên điện thoại 4G | Mục tiêu nhóm tự đặt (bảng hỏi và phỏng vấn không có câu riêng) | Trung bình |
| 6 | YC-PCN-06 | Chi phí tên miền, chỗ đặt website, phí cổng thanh toán nằm trong mức cửa hàng chấp nhận | PV F4 | Chờ PV |

## 5. Yêu cầu chuyển đổi (C2-6)

| STT | Mã | Yêu cầu | Nguồn |
|---|---|---|---|
| 1 | YC-CD-01 | Nhập danh sách mẫu máy, tồn kho và số serial đang có từ sổ hoặc Excel trước khi dùng | PV A3 |
| 2 | YC-CD-02 | Hướng dẫn nhân viên dùng từng chức năng theo vai | PV F3 |
| 3 | YC-CD-03 | Nộp hồ sơ thông báo website thương mại điện tử qua Cổng dịch vụ công quốc gia trước khi mở chức năng đặt hàng; gắn biểu tượng xác nhận | TL-20 Đ14; TL-21 |

## 6. Phân loại yêu cầu theo bài giảng (C2-6)

| Loại | Gồm | Ở đâu trong tệp |
|---|---|---|
| Yêu cầu nghiệp vụ | Mở thêm kênh bán trực tuyến cho khách ở xa; tra bảo hành nhanh theo chiếc máy; biết doanh thu, tồn kho không phải cộng tay (giả định, chờ PV F1, F2) | Mục 1, vấn đề 1–3 ở `hien-trang-to-chuc.md` |
| Yêu cầu của các bên liên quan | Chủ: báo cáo, quyết định; quản lý: duyệt giá, đổi trả, xếp lịch; nhân viên: thao tác hằng ngày; khách: xem, đặt, tra đơn, tra bảo hành | Bảng đối tượng liên quan ở `hien-trang-to-chuc.md` mục 2 |
| Yêu cầu giải pháp: chức năng | 45 yêu cầu YC-HT, YC-SP, YC-MH, YC-DH, YC-KB | Mục 3 |
| Yêu cầu giải pháp: phi chức năng | YC-PCN-01 đến 06 | Mục 4 |
| Yêu cầu chuyển đổi | YC-CD-01 đến 03 | Mục 5 |

## 7. Việc còn lại sau khảo sát

1. Sau buổi phỏng vấn: thay mọi ô "Chờ PV" ở mục 2 bằng lời cửa hàng; yêu cầu nào cửa hàng nói không cần thì gạch bỏ; thêm yêu cầu mới cửa hàng nêu ở PV F2.
2. Sau khi đóng form: chạy `xu_ly_bang_hoi.py`, điền tỷ lệ vào cột Nguồn (dạng "K11: …% chỉ muốn nhập số điện thoại", số lấy từ `ket-qua-bang-hoi.md`), xét lại cột Ưu tiên theo K8.
3. Ba yêu cầu chưa có use case (YC-HT-06, 07, 08) đưa ra họp chốt từ điển lớp (t112): thêm use case hay để ngoài phạm vi.
