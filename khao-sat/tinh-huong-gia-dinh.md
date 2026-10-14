# Tình huống cửa hàng giả định

Bản 1.1 (14/10/2026, sửa sau soát chéo) · G1 giữ · việc t88 · thay cho các việc t76, t77, t83 (phỏng vấn) và t86, t87 (bảng hỏi) đã bỏ khỏi trang tiến độ

## 1. Quyết định của nhóm (14/10/2026)

Nhóm **không phỏng vấn cửa hàng thật và không phát bảng hỏi**. Phần khảo sát của chương 2 dựa trên hai nguồn:

| Nguồn | Là gì | Mã dẫn | Mức tin cậy |
|---|---|---|---|
| Phân tích tài liệu | 35 tài liệu công khai thật: chính sách của 3 chuỗi điện máy và 1 nhà bán lẻ thiết bị bếp, văn bản pháp luật hiện hành, mẫu phiếu nhập, phiếu xuất, tính năng 4 website ([`phan-tich-tai-lieu.md`](phan-tich-tai-lieu.md)) | `TL-01`…`TL-35` | Dữ kiện thật, có đường dẫn |
| Phỏng vấn mô phỏng | AI đóng vai 3 người bán hàng trả lời kịch bản phỏng vấn ([`mo-phong/phong-van-thu.md`](mo-phong/phong-van-thu.md)) | `MP1`, `MP2`, `MP3` kèm mã câu, ví dụ `MP2 C4` | **Giả định**, dùng để dựng tình huống |
| Bảng hỏi mô phỏng | AI đóng vai 10 kiểu khách điền bảng hỏi bản 1.0 ([`mo-phong/phieu-dien-thu-v1-0.json`](mo-phong/phieu-dien-thu-v1-0.json)) | `MK` kèm mã câu bản 1.1, ví dụ `MK K11` | **Giả định**, 10 phiếu không có giá trị thống kê |

**Quy tắc trình bày (giữ nguyên trong báo cáo):**
1. Cửa hàng trong đề tài luôn gọi là **"cửa hàng giả định"**. Không đặt tên hay địa chỉ như cửa hàng có thật.
2. Câu nào lấy từ mô phỏng thì ghi mã `MP…` hoặc `MK…`. Không viết "cửa hàng cho biết", "khách hàng trả lời", "kết quả khảo sát cho thấy".
3. Không vẽ biểu đồ phần trăm từ 10 phiếu `MK`. Chỉ ghi số phiếu, ví dụ "6/10 vai khách mô phỏng".
4. Mục 2.1 của báo cáo có một đoạn nói rõ phương pháp, lấy nguyên văn ở mục 6 dưới đây.

## 2. Ba vai mô phỏng

| Mã | Vai | Kiểu cửa hàng | Dùng cho phần nào của tình huống |
|---|---|---|---|
| `MP1` | Cô Hạnh, 54 tuổi | Tiệm gia đình 22 năm gần chợ, 4 người, ghi sổ tay | Đối chiếu cửa hàng nhỏ: trình độ tin học thấp, chưa có phiếu xuất, phiếu đổi trả |
| `MP2` | Anh Duy, 31 tuổi | Shop thiết bị bếp 7 người, 70% đơn từ Facebook, Zalo, TikTok, sàn | **Cách ghi chép và các vấn đề**: phần mềm bán hàng chưa phân quyền, Google Sheets, nhóm Zalo, khách đặt rồi không nhận, trùng lịch lắp |
| `MP3` | Chị Trang, 40 tuổi | Quản lý đại lý thiết bị bếp nhập khẩu, 12 người, quản lý theo serial | **Cơ cấu tổ chức và giấy tờ**: đủ 5 vai thừa tác viên, phiếu xuất kho, biên bản lắp đặt, phiếu tiếp nhận bảo hành |

Bản 1.0 của kịch bản thiếu 6 câu B6, B7, C6, C7, D6, E5. Ngày 14/10 đã cho 3 vai trả lời thêm, xem mục 4 của `phong-van-thu.md`.
Câu F của bản 1.0 đánh số khác bản 1.2: `MP… F3` (cũ) gồm cả F3 và F4 của bản 1.2. Trong các tệp phân tích, mã câu ghi theo **bản 1.2**, riêng F ghi theo bản đã hỏi.

## 3. Cửa hàng giả định

Ghép từ ba vai: **cơ cấu và giấy tờ lấy theo `MP3`**, **cách ghi chép và vấn đề lấy theo `MP2`**.
Lý do không lấy nguyên một vai: `MP3` đã có phần mềm quản lý theo serial và phân quyền chặt, nếu lấy nguyên thì hệ thống mới gần như không còn gì để cải tiến;
`MP2` có nhiều vấn đề rõ nhưng không có quản lý, kế toán riêng, không khớp mô hình 5 thừa tác viên nhóm đã chốt ở `tac-nhan-va-thuc-the.md`.

| Mục | Cửa hàng giả định | Lấy từ |
|---|---|---|
| Mặt hàng | Thiết bị bếp và đồ điện gia dụng: bếp từ, máy hút mùi, máy rửa chén, lò nướng, máy lọc nước, nồi cơm, quạt. Bếp từ và máy hút mùi bán nhiều nhất | `MP1 A1`, `MP2 A1`, `MP3 A1` |
| Người | Chủ cửa hàng; 1 quản lý; 5 nhân viên bán hàng (4 tư vấn, 1 chăm sóc khách kiêm tiếp nhận bảo hành); 1 thủ kho kiêm soạn hàng giao; 1 kế toán; 4 nhân viên giao hàng – kỹ thuật (khảo sát, giao, lắp) | `MP3 A2` |
| Ai quyết gì | Chủ duyệt giá bán, khuyến mãi, đơn đặt hàng nhà cung cấp; quản lý đề xuất giá, duyệt đổi trả và huỷ đơn thường, trường hợp lớn hỏi chủ | `MP3 B1`, `B4`, `C4`, `D4`, `E1` |
| Lịch giao lắp | Quản lý xếp lịch và phân công nhân viên giao hàng – kỹ thuật trên Google Sheets; nhân viên bán hàng hẹn giờ với khách. **Nhóm ghép**: ở shop nhỏ chủ tự xếp (`MP2 D6`), ở đại lý tư vấn cùng kỹ thuật xếp, quản lý duyệt (`MP3 D6`) | `MP2 D2`, `D6`; `MP3 D6` |
| Kênh bán | Tại cửa hàng; Facebook, Zalo, TikTok, gian hàng sàn; khách gọi điện. Chưa có website riêng, muốn kéo khách từ sàn về website vì phí sàn cao | `MP2 C1` |
| Ghi chép | Phần mềm bán hàng trên máy tính và điện thoại (chưa phân quyền kỹ, ai cũng sửa được giá); lịch lắp ghi Google Sheets; báo đơn qua nhóm Zalo nội bộ; tiền thu khi giao ghi sổ tay; thông số máy lưu file Excel riêng | `MP2 A3`, `B2`, `D2`, `D5` |
| Số serial | Chỉ ghi serial máy giá cao lúc nhập; khi bảo hành khách mất phiếu thì phải lục hoá đơn | `MP2 E2`, `E4` |
| Báo cáo | Cuối ngày xem doanh thu theo kênh, số đơn chốt, số đơn hoàn; cuối tháng thêm tồn kho. Kế toán xuất ra Excel ghép với số của sàn, mất 3–4 tiếng vì hay lệch | `MP2 A4` |
| Giấy tờ | Hoá đơn bán lẻ, phiếu xuất kho, phiếu giao hàng 2 liên, biên bản lắp đặt, phiếu đổi trả, phiếu nhập kho, phiếu tiếp nhận bảo hành, phiếu bảo hành của hãng | `MP1 D1`; `MP2 D1`, `D2`, `D4`, `E2`; `MP3 D1`, `D2`, `E2`, `E4` |
| Trình độ tin học | Nhân viên bán hàng và thủ kho đang dùng phần mềm bán hàng hằng ngày và sẽ là người cập nhật website; thợ chỉ được xem lịch; mô phỏng không hỏi mức thành thạo. Chủ tiệm lớn tuổi ngại công nghệ (đối chiếu `MP1`) | `MP2 A3`, `F3`; `MP1 F2`, `F3` |
| Chi phí chấp nhận | Khoảng 2–3 triệu đồng một năm cho tên miền và chỗ đặt website; chủ là người quyết | `MP2 F3`; `MP1 F3`; `MP3 F3` |

**Ô thông tin trên giấy tờ** (mô tả mẫu trắng trong mô phỏng, chưa có ảnh thật; dùng làm gợi ý thuộc tính lớp ở chương 5):

| Giấy tờ | Các ô | Lấy từ |
|---|---|---|
| Phiếu xuất kho | Số phiếu, ngày xuất, tên người nhận, địa chỉ giao, tên hàng, model, số serial, số lượng, ghi chú, chữ ký thủ kho, người giao, người nhận | `MP3 D1`; `MP2 D1` |
| Biên bản lắp đặt | Mã đơn, thông tin khách, địa chỉ công trình, danh sách máy và serial, kết quả chạy thử, tình trạng công trình, ghi chú phát sinh, chữ ký khách và kỹ thuật viên | `MP3 D2`; `MP2 D2` |
| Phiếu nhập kho | Số phiếu, ngày nhập, nhà cung cấp, số hoá đơn, tên hàng, model, serial, số lượng, đơn giá, tình trạng hàng, chữ ký thủ kho và người giao | `MP3 E2`; `MP2 E2` |
| Phiếu tiếp nhận bảo hành | Số phiếu, ngày nhận, thông tin khách, tên máy, model, serial, ngày mua, mô tả lỗi, tình trạng bên ngoài, ngày hẹn trả, chữ ký hai bên | `MP3 E4` |
| Phiếu đổi trả | Số điện thoại, mã đơn, lý do, tình trạng máy, hướng xử lý | `MP2 D4` |
| Tin báo đơn trong nhóm Zalo | Tên khách, số điện thoại, địa chỉ, sản phẩm, giá, thu tiền khi giao hay chuyển khoản, có lắp đặt không, giờ hẹn, ghi chú | `MP2 C2` |
| Hoá đơn bán lẻ | Số, ngày, tên khách, địa chỉ, tên hàng, số lượng, đơn giá, thành tiền | `MP1 D1` |

## 4. Năm vấn đề của cửa hàng giả định

Đủ chi tiết ở mục 4 của [`../phan-tich/chung/hien-trang-to-chuc.md`](../phan-tich/chung/hien-trang-to-chuc.md).

| # | Vấn đề | Lấy từ | Gói |
|---|---|---|---|
| 1 | Bán nhiều kênh, giá và khuyến mãi mỗi nơi một kiểu; ai cũng sửa được giá, từng bán dưới giá vốn; nhân viên cộng nhầm hai mã giảm giá | `MP2 A3`, `A5`, `B4`, `B6` | G1, G2 |
| 2 | Khách đặt rồi không nhận hàng, mất phí giao hai chiều | `MP2 C4`, `C6`, `F1` | G3, G4 |
| 3 | Lịch giao lắp ghi Google Sheets, trùng giờ, khách chờ | `MP2 D2`, `D6`, `F1`; `MP3 F1` | G4 |
| 4 | Serial chỉ ghi máy giá cao; tra bảo hành phải lục hoá đơn; nhập serial bằng tay dễ sai | `MP2 E2`, `E4`; `MP3 A5`, `F1`; `MP1 E2`, `E4` | G5 |
| 5 | Tiền thu khi giao ghi sổ tay; cuối tháng ghép số mất 3–4 tiếng, hay lệch | `MP2 A4`, `D5`; `MP1 F1` | G1, G4 |

## 5. Mười vai khách mô phỏng (`MK`)

Mười kiểu khách: sinh viên ở trọ, nhân viên văn phòng mê so sánh, lập trình viên ghét tạo tài khoản, mẹ có con nhỏ, bà nội trợ 58 tuổi, kỹ sư khó tính,
chủ quán ăn ngoại thành, công nhân ở Long An, vợ chồng mới cưới mua trọn bộ bếp, người về hưu 65 tuổi.
Mã câu đã đổi sang **bản 1.1** của bảng hỏi. **Mười phiếu do AI điền, chỉ dùng để thấy các kiểu nhu cầu, không suy ra tỷ lệ của người mua thật.**

| Câu (bản 1.1) | Số vai chọn (trên 10) | Dùng cho |
|---|---|---|
| K7 Ngại gì khi mua trên mạng (bản 1.1 chỉ hỏi người chọn "một hai lần" hoặc "chưa từng" ở K6, tức 5 vai; phiếu bản 1.0 cho cả 10 vai trả lời) | Sợ hàng không đúng mô tả 4 · lo không ai lắp 4 · lo bảo hành khó 4 · muốn xem tận tay 3 | Vấn đề G3, G4, G5 |
| K8 Điều quan trọng (điểm 1–5, trung bình) | Bảo hành dài 4,5 · thương hiệu quen 3,9 · lắp miễn phí 3,9 · đánh giá người đã mua 3,9 · giá rẻ 3,8 | Ưu tiên G2, G5 |
| K9 Cách tìm sản phẩm | Gõ tên 6 · bấm danh mục 6 · lọc giá 5 · lọc hãng 4 | YC-SP-03 |
| K10 Cách trả tiền | Tiền mặt khi nhận 6 · ví điện tử 6 · thẻ 3 · chuyển khoản 3 · trả góp 1 | YC-MH-05 |
| K11 Tạo tài khoản | Có 5 · chỉ nhập số điện thoại 3 · sao cũng được 2 | YC-MH-01, YC-MH-02 |
| K13 Hẹn lắp đặt | Tự chọn ngày giờ trên website 3 · giao và lắp cùng lần 3 · nhân viên gọi hẹn 2 · tự lắp 2 | YC-DH-02 |
| K15 Tra bảo hành bằng gì | Mã đơn hoặc hoá đơn 4 · số serial 2 · số điện thoại 2 · giữ phiếu giấy 2 | YC-KB-06 |
| K16, K17 Ý kiến tự do | Bảng so sánh thông số (3 vai); giao lắp một lần cho cả bộ (2 vai); biết rõ ai chịu trách nhiệm bảo hành (2 vai); hiện phí giao trước khi đặt; đặt không cần tài khoản; có số điện thoại để gọi | YC-SP-04, YC-DH-02, YC-KB-07, YC-DH-03, YC-MH-01 |

Câu K3, K12 chỉ có ở bản 1.1 nên không có phiếu mô phỏng.

## 6. Đoạn phương pháp đưa vào mục 2.1 của báo cáo

> Nhóm không thực hiện phỏng vấn tại một cửa hàng cụ thể mà khảo sát bằng hai cách. Thứ nhất, **phân tích tài liệu** công khai của ngành:
> chính sách bán hàng, giao lắp, đổi trả, bảo hành của ba chuỗi điện máy và một nhà bán lẻ thiết bị bếp, các văn bản pháp luật hiện hành và mẫu chứng từ kho chính thức (35 tài liệu, Phụ lục A).
> Thứ hai, **phỏng vấn mô phỏng**: dùng trí tuệ nhân tạo đóng vai ba kiểu người bán (tiệm gia đình, shop bán trực tuyến, đại lý có phần mềm quản lý)
> trả lời đúng kịch bản phỏng vấn của nhóm (Phụ lục B), và mười kiểu khách điền bảng câu hỏi (Phụ lục C).
> Từ đó nhóm dựng một **cửa hàng giả định** làm đối tượng phân tích. Các nội dung lấy từ mô phỏng được ghi mã nguồn MP, MK để phân biệt với dữ kiện từ tài liệu (TL).

## 7. Nếu sau này vẫn khảo sát được

Kịch bản 1.2, kế hoạch phỏng vấn, bảng hỏi `bang-hoi-khach-hang.md` và `tao-google-form.gs` vẫn giữ nguyên làm dự phòng. Có lời cửa hàng thật hoặc phiếu thật thì thay mã `MP`, `MK` bằng `PV`, `K` và bỏ chữ "giả định" ở chỗ tương ứng.
