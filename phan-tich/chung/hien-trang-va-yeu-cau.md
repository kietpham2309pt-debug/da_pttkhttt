# Hiện trạng và yêu cầu của hệ thống

Bản 0.2 (14/10/2026) · G1 làm cho cả 5 gói · việc t84 · theo chương 2 bài giảng: bảng yêu cầu (C2-4 đến C2-6), chiến lược (C2-7, C2-8), nội dung khảo sát (C2-12 đến C2-14)

Từ 08/10/2026 nhóm trưởng làm toàn bộ phần khảo sát để đỡ tốn thời gian của nhóm. Mỗi gói **đọc lại phần của mình** trước khi đặc tả use case nghiệp vụ ở tuần 10.

Từ 14/10/2026 nhóm **không phỏng vấn cửa hàng thật**. Hiện trạng viết theo **cửa hàng giả định** dựng từ phỏng vấn mô phỏng,
cách dựng và lý do ở [`../../khao-sat/tinh-huong-gia-dinh.md`](../../khao-sat/tinh-huong-gia-dinh.md).

**Cột Nguồn** dùng ba loại mã:

| Mã | Nghĩa | Loại |
|---|---|---|
| `TL-xx` | Tài liệu công khai trong [`../../khao-sat/phan-tich-tai-lieu.md`](../../khao-sat/phan-tich-tai-lieu.md) | Dữ kiện thật |
| `MP1`, `MP2`, `MP3` + mã câu | Vai mô phỏng cô Hạnh, anh Duy, chị Trang trong [`../../khao-sat/mo-phong/phong-van-thu.md`](../../khao-sat/mo-phong/phong-van-thu.md); mã câu theo kịch bản bản 1.2, riêng câu F theo bản 1.0 đã hỏi (F3 cũ gồm cả F3, F4 của bản 1.2) | **Giả định** |
| `MK` + mã câu | 10 vai khách mô phỏng điền bảng hỏi, mã câu theo bảng hỏi bản 1.1 ([`tinh-huong-gia-dinh.md`](../../khao-sat/tinh-huong-gia-dinh.md) mục 5) | **Giả định**, không có giá trị thống kê |

> Quy tắc: nội dung lấy từ `MP`, `MK` luôn viết là của **cửa hàng giả định** hoặc **vai mô phỏng**, không viết "cửa hàng cho biết", "khách hàng trả lời".
> Số liệu `MK` chỉ ghi số phiếu trên 10, không đổi ra phần trăm, không vẽ biểu đồ.

## 1. Chiến lược phân tích yêu cầu (C2-7, C2-8)

| Chiến lược | Nghĩa theo bài giảng | Hợp với đề tài không |
|---|---|---|
| Tự động hoá quy trình | Hoạt động không đổi, dùng máy làm thay một số việc | Một phần: ghép báo cáo cuối tháng, đối chiếu tiền thu khi giao |
| **Cải tiến quy trình** | Phân tích hệ thống hiện tại, tìm giải pháp cho hệ thống mới | **Chọn** |
| Tái thiết kế quy trình | Phá bỏ cách kinh doanh hiện tại, đổi lớn | Không: cửa hàng giả định vẫn bán tại chỗ và qua mạng xã hội, chỉ thêm kênh website |

Lý do chọn cải tiến: cửa hàng giả định giữ cách bán đang có, **thêm** kênh đặt hàng trên website riêng (`MP2 C1`: muốn kéo khách từ sàn về vì phí sàn cao)
và **sửa** năm chỗ đang hay sai ở mục 4 của `hien-trang-to-chuc.md`: giá và khuyến mãi lệch giữa các kênh, khách đặt rồi không nhận, trùng lịch lắp,
serial ghi thiếu, tiền thu khi giao ghi sổ tay (`MP2 B6`, `C6`, `D6`, `E2`, `D5`, `F1`; `MP3 F1`). Không tái thiết kế vì vai người bán mô phỏng ngại làm rối quy trình đang chạy và ngại nhập hàng hai nơi (`MP3 F2`; `MP2 F2`).
Ngành đã có sẵn cách làm chuẩn để học theo (TL mục 3: gọi xác nhận đơn, tách công lắp và vật tư, phân loại đổi trả).

## 2. Hiện trạng theo từng gói (C2-12 đến C2-14)

Cột **Ngành** lấy từ tài liệu thật. Cột **Cửa hàng giả định** lấy từ phỏng vấn mô phỏng.

### G1 · Tổ chức, tin học hoá, báo cáo

| Nội dung | Ngành (tài liệu) | Cửa hàng giả định (mô phỏng) |
|---|---|---|
| Cơ cấu, ai kiêm vai nào | Nguyễn Kim có trung tâm bảo hành riêng (TL-09); Điện Máy Xanh có thợ lắp mang tên chuỗi (TL-06); Kim Quốc Tiến để nhân viên của mình đi lắp (TL-14) | Chủ và 12 người: 1 quản lý, 5 bán hàng (1 người kiêm tiếp nhận bảo hành), 1 thủ kho kiêm soạn hàng giao, 1 kế toán, 4 giao hàng – kỹ thuật (`MP3 A2`). Cửa hàng nhỏ hơn thì thủ kho kiêm kế toán, thợ phụ giao hàng (`MP2 A2`) |
| Ghi chép đang dùng | Hộ kinh doanh từ 2026 phải tự kê khai thuế, ghi sổ theo Thông tư 152/2025 (TL-27, TL-30) | Phần mềm bán hàng chưa phân quyền kỹ, ai cũng sửa được giá; lịch lắp trên Google Sheets; báo đơn trong nhóm Zalo nội bộ (`MP2 A3`). Tiệm gia đình ghi sổ tay (`MP1 A3`) |
| Báo cáo cuối ngày, cuối tháng | Sổ chi tiết hàng hoá S2d-HKD: nhập, xuất, tồn theo số lượng và tiền (TL-30) | Cuối ngày xem doanh thu theo kênh, số đơn chốt, số đơn hoàn; cuối tháng thêm tồn kho. Kế toán xuất ra Excel ghép với số của sàn, mất 3–4 tiếng vì hay lệch (`MP2 A4`) |
| Sai sót và truy vết | Không có tài liệu công khai | Lệch 2 máy hút mùi không biết ai xuất vì chỉ báo trong Zalo; một nhân viên sửa giá xuống dưới giá vốn, phát hiện muộn (`MP2 A5`). Nơi có phần mềm lưu người thao tác thì tra ra trong một buổi (`MP3 A5`) |
| Trình độ tin học người dùng (C2-14) | Không có tài liệu công khai | Nhân viên bán hàng và thủ kho đang dùng phần mềm bán hàng hằng ngày và sẽ cập nhật website; thợ chỉ được xem lịch; mô phỏng không hỏi mức thành thạo (`MP2 A3`, `F3`). Chủ tiệm lớn tuổi ngại công nghệ, cần người cập nhật hộ (`MP1 F2`, `F3`) |

### G2 · Sản phẩm, giá, khuyến mãi

| Nội dung | Ngành (tài liệu) | Cửa hàng giả định (mô phỏng) |
|---|---|---|
| Thông tin khách hỏi | 4/4 website lọc theo hãng và thông số (công suất, dung tích), có so sánh và đánh giá; ít nhất 2/4 có hỏi đáp (TL-19) | Công suất, kích thước khoét đá, cảm ứng, lưu lượng hút, số bộ chén, bảo hành; thông số chép từ catalogue về một file Excel riêng (`MP2 B2`). Kích thước khoét đá hỏi nhiều nhất (`MP3 B2`) |
| Đưa mẫu máy mới lên bán | Không có tài liệu công khai về quy trình nội bộ | Thủ kho tạo mã hàng; quản lý đề xuất giá, chủ duyệt; nhân viên chụp ảnh, viết bài đăng các kênh (`MP3 B1`; `MP2 B1`) |
| Giá | Giá niêm yết đã gồm VAT (TL-10, TL-11) | Chủ quyết theo giá hãng và giá đối thủ; sàn đổi giá hằng tuần; giá sửa tay ở từng kênh nên hay lệch, có lần sàn còn hiện hàng khi kho đã hết (`MP2 B3`, `B6`) |
| Khuyến mãi | 4/4 có mã giảm giá và khuyến mãi theo sản phẩm (TL-19) | Giảm giá, quà tặng, theo hãng, voucher khách quen, mã cho người viết nhận xét; chủ quyết, ghi Excel; mỗi đơn chỉ một giảm giá và một quà, từng có nhân viên cộng nhầm hai mã (`MP2 B4`) |
| Nhận xét của khách | Website lớn cho đánh giá sản phẩm (TL-19) | Khách gửi ảnh máy đã lắp, cửa hàng đăng lại; có nhận xét sai (khách tự lắp rồi chê máy) và tài khoản lạ vào chê (`MP2 B7`) |
| Nhãn năng lượng | Máy lạnh, tủ lạnh, nồi cơm, bình nóng lạnh bắt buộc dán nhãn (TL-31) | Khách hỏi "có tiết kiệm điện không" (`MP1 B2`); mô phỏng không nói về nhãn |

### G3 · Bán hàng, đặt hàng, thanh toán

| Nội dung | Ngành (tài liệu) | Cửa hàng giả định (mô phỏng) |
|---|---|---|
| Kênh khách đặt | Chuỗi lớn: website, ứng dụng, cửa hàng; giá trung bình mỗi sản phẩm điện gia dụng trên sàn khoảng 323 nghìn (TL-34), nhóm suy ra sàn chủ yếu bán đồ nhỏ (suy luận của nhóm) | Facebook, Zalo, TikTok, gian hàng sàn, tại cửa hàng, gọi điện; muốn kéo khách từ sàn về website riêng (`MP2 C1`). Vai khách nơi mua gần nhất: sàn 4, siêu thị điện máy 3, cửa hàng gần nhà 3 trên 10 (`MK K5`) |
| Đặt hàng | Đặt bằng họ tên, số điện thoại, địa chỉ; MediaMart ghi rõ không cần tài khoản (TL-18); xác nhận đơn qua điện thoại (TL-02, TL-11) | Chốt qua tin nhắn: tên, số điện thoại, địa chỉ, giờ nhận, có lắp không; gõ vào phần mềm và đăng tin mẫu vào nhóm Zalo (`MP2 C2`). 3/10 vai khách chỉ muốn nhập số điện thoại, 5/10 muốn tạo tài khoản (`MK K11`) |
| Thanh toán | Tiền mặt khi nhận, chuyển khoản hoặc QR, thẻ, trả góp (TL-05, TL-13); một nơi bắt đặt trước 20–30% với đơn trả tiền khi nhận (TL-17) | Tiền mặt khi giao, chuyển khoản, thẻ tại cửa hàng; máy trên 10 triệu cọc 10–20%; trả góp qua công ty tài chính (`MP2 C3`). Vai khách: tiền mặt khi nhận 6, ví điện tử 6 trên 10 (`MK K10`) |
| Huỷ đơn, bỏ đơn | Nhà bán được huỷ đơn, hoàn đủ tiền (TL-13); nơi có cọc thì giữ cọc khi khách bỏ đơn (TL-17) | Huỷ trước khi giao thì hoàn cọc, trừ phí nếu đã xuất kho; một tháng 6 đơn tỉnh khách không nhận, mất 300–500 nghìn phí giao hai chiều mỗi đơn; máy trả về còn seal thì nhập lại kho (`MP2 C4`, `C6`) |
| Khách hỏi đơn tới đâu | Chuỗi lớn cho tra đơn bằng số điện thoại + OTP (TL-04, TL-10) | Ngày nào cũng có; nhân viên tra tên hoặc số điện thoại trong phần mềm, tra mã vận đơn, hỏi thợ rồi nhắn lại; lúc đông đơn khách chờ lâu (`MP2 C7`) |

### G4 · Xử lý đơn, giao hàng, lắp đặt, đổi trả

| Nội dung | Ngành (tài liệu) | Cửa hàng giả định (mô phỏng) |
|---|---|---|
| Xác nhận đơn, hẹn giờ | Cả 4 nơi gọi điện cho khách trước khi giao: xác nhận đơn (TL-02, TL-10) hoặc hẹn giờ giao (TL-11, TL-14); trên các trang đọc được không thấy nơi nào cho khách tự chọn khung giờ giao lắp tại nhà (TL-18) | Gọi xác nhận trong 30 phút sau khi chốt (`MP2 D1`); hẹn giờ lắp qua điện thoại (`MP3 D2`). Vai khách: tự chọn giờ trên website 3, giao và lắp cùng lần 3 trên 10 (`MK K13`) |
| Xếp lịch giao lắp | Không có tài liệu công khai | Quản lý xếp lịch và phân công kỹ thuật viên trên Google Sheets, nhân viên bán hàng hẹn giờ với khách (nhóm ghép: shop nhỏ chủ tự xếp, `MP2 D6`; đại lý tư vấn cùng kỹ thuật xếp, quản lý duyệt, `MP3 D6`). Không có cảnh báo trùng; hai đơn trùng 2 giờ chiều ở hai quận, khách chờ 2 tiếng (`MP2 D2`, `D6`). Lịch hẹn kỹ thuật với khách là chỗ dễ sai (`MP3 F1`) |
| Phí giao | Miễn phí theo ngưỡng giá trị đơn và khoảng cách (TL-01, TL-07, TL-11, TL-14) | Nội thành thợ cửa hàng giao kèm lắp; tỉnh gửi đơn vị vận chuyển, khách tự thuê thợ (`MP2 D3`) |
| Lắp đặt | Tách công lắp và vật tư theo bảng giá; việc ngoài danh mục khảo sát rồi báo giá (TL-01, TL-08) | Máy âm (bếp từ, hút mùi, máy rửa chén, lò) phải lắp; miễn phí nội thành cho đơn trên 5 triệu, dưới thì 200–300 nghìn (`MP2 D2`); kỹ thuật khảo sát đo tủ, cắt đá, điện nước trước, lắp xong ký biên bản (`MP3 D2`) |
| Thu tiền khi giao | Hoá đơn lập lúc giao, kể cả khi chưa thu tiền (TL-26) | Thợ nộp kế toán tối cùng ngày, ghi sổ tay, thỉnh thoảng lệch vài chục nghìn; đơn tỉnh đơn vị vận chuyển đối soát sau 3–5 ngày (`MP2 D5`) |
| Đổi trả | Phân biệt lỗi nhà sản xuất và không lỗi, phí khấu trừ theo %; cần biên bản lỗi; luật bắt nhận lại hàng không đúng mô tả (TL-03, TL-12, TL-15, TL-20 Đ16) | Đổi trong 7 ngày nếu còn seal, lỗi do hãng trong 30 ngày; ghi Zalo và phiếu đổi trả giấy (`MP2 D4`). Quản lý duyệt, ngoại lệ hỏi chủ; máy đã lắp thì chỉ đổi khi lỗi hãng (`MP3 D4`) |

### G5 · Nhập hàng, kho, bảo hành

| Nội dung | Ngành (tài liệu) | Cửa hàng giả định (mô phỏng) |
|---|---|---|
| Nhập hàng | Mẫu phiếu nhập 01-VT: số lượng theo chứng từ và thực nhập (TL-28, TL-29) | Thủ kho báo sắp hết, quản lý cùng thủ kho lập đề xuất, chủ duyệt (`MP3 E1`); đặt qua Zalo, điện thoại, không có mức tồn tối thiểu, mỗi mẫu 1–2 nhà phân phối (`MP2 E1`). Kho đếm, kiểm thùng, ghi phiếu nhập (`MP2 E2`) |
| Ghi serial | Serial trên máy phải khớp phiếu bảo hành (TL-16, TL-18) | Chỉ ghi serial máy giá cao lúc nhập, đồ rẻ không ghi (`MP2 E2`, `E5`); tiệm gia đình chỉ chép serial lúc bán (`MP1 E2`, `E5`); nhập serial bằng tay dễ sai (`MP3 F1`) |
| Xuất kho | Mẫu phiếu xuất 02-VT: lý do xuất, số lượng yêu cầu và thực xuất (TL-28) | Kho soạn hàng theo đơn, in phiếu xuất có serial (`MP2 D1`, `E5`); hàng xuất mà chỉ báo trong Zalo thì không vào phần mềm, gây lệch (`MP2 E5`) |
| Kiểm kê | Sổ S2d-HKD (TL-30) | Mỗi tháng một lần; lệch thì tìm lại phiếu, không ra thì ghi hao hụt (`MP2 E3`) |
| Bảo hành | Bảo hành gốc của hãng; tra cứu bằng số điện thoại, IMEI, mã phiếu; luật: giao văn bản tiếp nhận, sửa 3 lần không được thì đổi mới hoặc hoàn tiền (TL-04, TL-16, TL-18, TL-22 Đ30) | Kho tra serial xem còn hạn; gửi hãng 7–15 ngày; khách mất phiếu thì lục hoá đơn; có sổ theo dõi máy gửi hãng (`MP2 E4`). Vai khách muốn tra bằng mã đơn 4, serial 2, số điện thoại 2 trên 10 (`MK K15`) |

## 3. Bảng yêu cầu chức năng (C2-4)

Cột **Ưu tiên** xét theo điều người bán mô phỏng muốn có (`MP2 F2`, `MP3 F2`) và điểm quan trọng của vai khách (`MK K8`).

### G1 · Quản trị hệ thống và báo cáo

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 1 | YC-HT-01 | Nhân viên đăng nhập; mỗi vai chỉ thấy chức năng của vai mình, một người được kiêm nhiều vai | MP3 A2, A3; MP2 A2, A3 | UC-HT-01, UC-HT-03 | Cao |
| 2 | YC-HT-02 | Quản lý tài khoản nhân viên: thêm, khoá, gán vai | MP3 A2 | UC-HT-02 | Cao |
| 3 | YC-HT-03 | Báo cáo doanh thu theo ngày, tháng, nhóm hàng, kênh bán; dùng được cho tự kê khai thuế của hộ kinh doanh | MP2 A4; TL-27 | UC-HT-04 | Cao |
| 4 | YC-HT-04 | Báo cáo nhập – xuất – tồn theo mẫu máy (dạng sổ S2d-HKD) và báo cáo máy đang bảo hành | MP2 A4, E3; MP3 A4; TL-30 | UC-HT-05 | Trung bình |
| 5 | YC-HT-05 | Lưu vết ai sửa giá, ai đổi trạng thái đơn, ai xuất kho, lúc nào | MP2 A5; MP3 A5 | UC-SP-03, UC-DH-03 | Cao |
| 6 | YC-HT-06 | Trang thông tin cửa hàng (tên, địa chỉ, mã số thuế, điện thoại) và bộ trang chính sách (giá, giao hàng, đổi trả, bảo hành, thanh toán, bảo mật, khiếu nại); liên kết đặt ở chân trang mọi màn hình | TL-20 Đ11; TL-21 | Chưa có (đề xuất) | Cao |
| 7 | YC-HT-07 | Khách gửi yêu cầu xem, sửa, xoá dữ liệu cá nhân hoặc rút đồng ý; hệ thống đếm hạn xử lý (phản hồi 2 ngày làm việc) | TL-24 Đ4, Đ10; TL-25 Đ5 | Chưa có (đề xuất) | Thấp |
| 8 | YC-HT-08 | Khách gửi khiếu nại trực tuyến, có mã và trạng thái; báo đã tiếp nhận trong 3 ngày làm việc | TL-22 Đ31; TL-23 Đ22 | Chưa có (đề xuất) | Trung bình |
| 9 | YC-HT-09 | Nhân viên và khách tự đổi mật khẩu; đổi xong thì các phiên đăng nhập cũ hết hiệu lực | Yêu cầu hệ thống (C2-3); đi cùng YC-PCN-02 | UC-HT-06 | Thấp |

### G2 · Sản phẩm và khuyến mãi

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 10 | YC-SP-01 | Quản lý danh mục theo nhóm hàng và thương hiệu | MP2 A1, B1; MP3 A1; TL-19 | UC-SP-01 | Cao |
| 11 | YC-SP-02 | Mỗi mẫu máy có thông số kỹ thuật lọc được (công suất, dung tích, kích thước, kích thước khoét đá với bếp âm), ảnh, thời hạn bảo hành, nhãn năng lượng; nhập một lần thay cho file Excel riêng | MP2 B2; MP3 B2, F2; TL-19, TL-31; MK K8 | UC-SP-02 | Cao |
| 12 | YC-SP-03 | Khách tìm theo tên hoặc model; lọc theo khoảng giá, hãng, thông số | TL-19; MK K9 | UC-SP-05 | Cao |
| 13 | YC-SP-04 | Khách so sánh 2–3 mẫu máy theo thông số | TL-19; MK K17 | UC-SP-06 | Trung bình |
| 14 | YC-SP-05 | Giá bán hiển thị đã gồm VAT; phí giao và lắp ghi riêng | TL-10, TL-11; TL-20 Đ12 | UC-SP-03 | Cao |
| 15 | YC-SP-06 | Khuyến mãi theo mẫu máy và mã giảm giá có hạn dùng; ghi người duyệt | MP2 B4, F2; MP3 B4; TL-19 | UC-SP-04 | Trung bình |
| 16 | YC-SP-07 | Chỉ khách đã nhận hàng mới được đánh giá; có hỏi đáp dưới trang sản phẩm | MP2 B7; TL-19; MK K8 | UC-SP-07 | Trung bình |
| 17 | YC-SP-08 | Thông tin mẫu máy đã đăng giữ ít nhất 1 năm; đổi giá không làm đổi giá trên đơn cũ | TL-20 Đ16 | UC-SP-02, UC-SP-03 | Trung bình |
| 18 | YC-SP-09 | Chỉ vai Quản lý cửa hàng được sửa giá; giá bán thấp hơn giá nhập gần nhất thì hệ thống cảnh báo, bắt ghi lý do và lưu vết để chủ xem lại | MP2 A3, A5; MP3 B1, B3 | UC-SP-03 | Cao |

### G3 · Mua hàng trực tuyến và thanh toán

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 19 | YC-MH-01 | Đặt hàng chỉ với họ tên, số điện thoại, địa chỉ, không bắt tạo tài khoản | TL-18; MK K11, K17 | UC-MH-04 | Cao |
| 20 | YC-MH-02 | Khách muốn thì tạo tài khoản để lưu địa chỉ và xem đơn cũ | MP2 C5; MK K11 | UC-MH-01, UC-MH-02 | Trung bình |
| 21 | YC-MH-03 | Màn hình xác nhận đơn hiện đủ sản phẩm, số lượng, thời hạn giao, khuyến mãi, tiền hàng, phí giao, phí lắp, cách thanh toán; khách sửa được rồi bấm đồng ý điều kiện giao dịch | TL-20 Đ12 | UC-MH-04 | Cao |
| 22 | YC-MH-04 | Ô đồng ý xử lý dữ liệu **không tích sẵn**, tách mục đích giao hàng và bảo hành với mục đích quảng cáo; lưu lại lần đồng ý | TL-24 Đ9; TL-25 Đ6 | UC-MH-04 | Cao |
| 23 | YC-MH-05 | Thanh toán: tiền mặt khi nhận, chuyển khoản hoặc QR, thẻ; trả góp qua công ty tài chính | MP2 C3; TL-05, TL-13; MK K10 | UC-MH-05 | Cao |
| 24 | YC-MH-06 | Đặt cọc trực tuyến cho đơn giá trị cao và đơn giao tỉnh; mức cọc do chủ đặt; ghi số tiền cọc trên đơn | MP2 C3, C6, F2; MP3 C3; TL-17 | UC-MH-05 | Cao |
| 25 | YC-MH-07 | Áp một mã giảm giá còn hạn vào đơn, không cộng hai mã | MP2 B4; TL-19 | UC-MH-08 | Trung bình |
| 26 | YC-MH-08 | Tra đơn bằng số điện thoại kèm mã đơn; thấy trạng thái, ngày giờ hẹn giao lắp | MP2 C7; MP3 C7; TL-04, TL-10 | UC-MH-06 | Cao |
| 27 | YC-MH-09 | Huỷ đơn khi chưa xuất kho; hoàn đủ tiền đã thu | MP2 C4; TL-13 | UC-MH-07 | Trung bình |
| 28 | YC-MH-10 | Khách xem, tải, in hoá đơn và chứng từ của đơn | TL-22 Đ29; TL-26 | UC-MH-06 | Trung bình |
| 29 | YC-MH-11 | Giỏ hàng: thêm nhiều mẫu máy, sửa số lượng, chọn có lắp đặt hay không; giữ giỏ khi chưa đăng nhập | TL-19; MK K17 | UC-MH-03 | Cao |

### G4 · Xử lý đơn, giao hàng, lắp đặt, đổi trả

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 30 | YC-DH-01 | Đơn mới hiện ở đầu danh sách "chờ xác nhận"; nhân viên gọi khách rồi xác nhận hoặc huỷ, ghi lý do | MP2 D1, C6; TL-02, TL-10, TL-11, TL-14 | UC-DH-01 | Cao |
| 31 | YC-DH-02 | Hẹn ngày và khung giờ khảo sát (máy âm tủ) và giao lắp với khách; xếp lịch thợ, cảnh báo khi trùng giờ | MP2 D2, D6, F1, F2; MP3 D2, D6, F2; TL-14; MK K13, K16, K17 | UC-DH-02 | Cao |
| 32 | YC-DH-03 | Phí giao tính theo ngưỡng giá trị đơn và khoảng cách (G4 đặt mức phí), hiện cho khách trước khi đặt (G3) | MP2 D2, D3; TL-01, TL-07, TL-11, TL-14; MK K17 | UC-MH-04 | Trung bình |
| 33 | YC-DH-04 | Cập nhật trạng thái giao: đang giao, đã giao, giao không thành công (kèm lý do) | MP2 C6, C7, D3 | UC-DH-03 | Cao |
| 34 | YC-DH-05 | Biên bản lắp đặt ghi máy và serial, kết quả chạy thử, công lắp và vật tư phát sinh theo bảng giá, khách ký xác nhận | MP3 D2; MP2 D2; TL-01, TL-08, TL-14 | UC-DH-05 | Trung bình |
| 35 | YC-DH-06 | Ghi tiền thu khi giao theo từng đơn, kế toán xác nhận trong ngày (trên website dùng tài khoản vai Quản lý cho tới khi chốt tác nhân Kế toán ở t112); đơn đã giao thì sinh yêu cầu lập hoá đơn dù chưa thu tiền | MP2 D5; MP3 D5; TL-26 | UC-DH-04 | Cao |
| 36 | YC-DH-07 | Yêu cầu đổi trả ghi loại lý do (lỗi nhà sản xuất, không đúng mô tả, đổi ý), phí khấu trừ, người duyệt; lý do "không đúng mô tả" thì bắt buộc nhận lại | MP2 D4; MP3 D4; TL-03, TL-12, TL-15, TL-20 Đ16 | UC-DH-06 | Cao |
| 37 | YC-DH-08 | Hoàn tiền theo cách khách đã trả (luật bắt buộc khi khách chấm dứt hợp đồng vì thông tin thiếu hoặc sai; trường hợp khác theo chính sách cửa hàng) | TL-03; TL-22 Đ38 k3, k4 | UC-DH-06 | Trung bình |

### G5 · Kho, nhà cung cấp, bảo hành

| STT | Mã | Yêu cầu | Nguồn | Use case | Ưu tiên |
|---|---|---|---|---|---|
| 38 | YC-KB-01 | Quản lý nhà cung cấp và đơn đặt hàng nhà cung cấp | MP2 E1; MP3 E1 | UC-KB-01, UC-KB-02 | Trung bình |
| 39 | YC-KB-02 | Phiếu nhập theo mẫu 01-VT (số lượng theo chứng từ và thực nhập) và ghi số serial **từng chiếc máy**, kể cả hàng giá thấp | MP2 E2; MP3 E2, F1; TL-28, TL-29 | UC-KB-03 | Cao |
| 40 | YC-KB-03 | Phiếu xuất theo mẫu 02-VT (lý do xuất, số lượng yêu cầu và thực xuất); chọn đúng serial đi theo đơn | MP2 D1, E5; MP3 D1, E5; TL-28 | UC-KB-04 | Cao |
| 41 | YC-KB-04 | Phiếu nhập, xuất đã duyệt thì khoá; sai thì lập phiếu điều chỉnh | MP3 A5; TL-28 (TT 99 Đ10) | UC-KB-03, UC-KB-04 | Trung bình |
| 42 | YC-KB-05 | Kiểm kê, so số trên hệ thống với số đếm; cảnh báo mẫu máy dưới mức tồn tối thiểu | MP2 E1, E3; MP3 E1, E3; TL-30 | UC-KB-05 | Trung bình |
| 43 | YC-KB-06 | Khách và nhân viên tra bảo hành theo **số serial**, số điện thoại hoặc mã đơn: ra ngày bán, hạn bảo hành, các lần bảo hành | MP2 E4, F2; MP3 F2; TL-04, TL-16, TL-18; MK K15 | UC-KB-07 | Cao |
| 44 | YC-KB-07 | Phiếu tiếp nhận bảo hành có lỗi, ngày hẹn trả; đếm số lần bảo hành của chiếc máy, cảnh báo ở lần thứ 3; thời gian sửa không tính vào hạn | MP3 E4; TL-22 Đ30; MK K16, K17 | UC-KB-06 | Cao |
| 45 | YC-KB-08 | Theo dõi máy đang gửi trung tâm bảo hành hãng (ngày gửi, ngày nhận lại) | MP2 E4; MP3 F1; TL-16 | UC-KB-06 | Trung bình |
| 46 | YC-KB-09 | Nhận lại máy về kho khi giao không thành hoặc khách đổi trả; ghi tình trạng máy | MP2 C6, D4; MP3 C6 | UC-KB-08 | Trung bình |

## 4. Yêu cầu phi chức năng (C2-4)

| STT | Mã | Yêu cầu | Nguồn | Ưu tiên |
|---|---|---|---|---|
| 1 | YC-PCN-01 | Không xoá cứng đơn hàng; giữ dữ liệu hợp đồng ít nhất 3 năm (hộ kinh doanh được giữ ít nhất 1 năm, chỉ trong 5 năm đầu kể từ khi thành lập); tài liệu kế toán của hộ ít nhất 5 năm | TL-20 Đ16; TL-30 | Cao |
| 2 | YC-PCN-02 | Mật khẩu lưu dạng băm; chỉ vai được giao mới xem số điện thoại, địa chỉ khách | TL-24; TL-25; MP3 F2 | Cao |
| 3 | YC-PCN-03 | Giao diện tiếng Việt, ít bước, chữ đủ to, dùng được trên điện thoại cho nhân viên giao hàng | MP2 A3, F3; MP1 F2 (C2-14: trình độ tin học); MK K17 | Cao |
| 4 | YC-PCN-04 | Hoá đơn lấy từ phần mềm hoá đơn điện tử hoặc máy tính tiền của cửa hàng; website chỉ lưu số, ký hiệu và đường dẫn tra cứu, không tự phát hành | TL-26 | Trung bình |
| 5 | YC-PCN-05 | Trang danh sách và chi tiết sản phẩm mở xong trong khoảng 3 giây trên điện thoại 4G | Mục tiêu nhóm tự đặt; MK K17 | Trung bình |
| 6 | YC-PCN-06 | Chi phí tên miền, chỗ đặt website nằm trong khoảng 2–3 triệu đồng một năm; phí cổng thanh toán tính riêng theo giao dịch | MP2 F3; MP1 F3 | Trung bình |

## 5. Yêu cầu chuyển đổi (C2-6)

| STT | Mã | Yêu cầu | Nguồn |
|---|---|---|---|
| 1 | YC-CD-01 | Nhập danh sách mẫu máy, thông số, tồn kho và số serial đang có từ phần mềm bán hàng và file Excel trước khi dùng | MP2 A3, B2 |
| 2 | YC-CD-02 | Hướng dẫn nhân viên dùng từng chức năng theo vai | MP2 F3; MP1 F3 |
| 3 | YC-CD-03 | Nộp hồ sơ thông báo website thương mại điện tử qua Cổng dịch vụ công quốc gia trước khi mở chức năng đặt hàng; gắn biểu tượng xác nhận | TL-20 Đ14; TL-21 |

## 6. Phân loại yêu cầu theo bài giảng (C2-6)

| Loại | Gồm | Ở đâu trong tệp |
|---|---|---|
| Yêu cầu nghiệp vụ | Mở thêm kênh bán trên website riêng; giảm đơn khách đặt rồi không nhận; xếp lịch giao lắp không trùng; tra bảo hành nhanh theo chiếc máy; biết doanh thu, tồn kho không phải ghép tay (`MP2 F1`, `F2`; `MP3 F1`, `F2`) | Mục 1, vấn đề 1–5 ở `hien-trang-to-chuc.md` |
| Yêu cầu của các bên liên quan | Chủ: báo cáo, duyệt giá; quản lý: duyệt đổi trả, xếp lịch; nhân viên: thao tác hằng ngày; khách: xem, đặt, tra đơn, tra bảo hành | Bảng đối tượng liên quan ở `hien-trang-to-chuc.md` mục 2 |
| Yêu cầu giải pháp: chức năng | 46 yêu cầu YC-HT, YC-SP, YC-MH, YC-DH, YC-KB | Mục 3 |
| Yêu cầu giải pháp: phi chức năng | YC-PCN-01 đến 06 | Mục 4 |
| Yêu cầu chuyển đổi | YC-CD-01 đến 03 | Mục 5 |

## 7. Việc còn lại

1. Ba yêu cầu chưa có use case (YC-HT-06, 07, 08) đưa ra họp chốt từ điển lớp (t112): thêm use case hay để ngoài phạm vi.
2. YC-SP-09 dùng giá nhập gần nhất: G2 và G5 thống nhất lấy từ `ChiTietPhieuNhap` hay thêm thuộc tính `giaNhap` cho `SanPham`.
3. **Ngoài phạm vi** (ghi vào mục giới hạn của báo cáo): đẩy giá và tồn kho tự động lên Facebook, sàn (`MP2 B6`); bán theo dự án qua kiến trúc sư (`MP3 C1`).
4. Nếu sau này vẫn có khảo sát thật: thay mã `MP`, `MK` bằng `PV`, `K` ở chỗ tương ứng, xét lại cột Ưu tiên.
