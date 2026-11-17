# Đặc tả use case hệ thống · G4 Xử lý đơn, giao hàng, lắp đặt, đổi trả

Bản 0.1 (14/10/2026) · G4 · việc t102, t103 · làm theo khuôn mục 2 của [`../chung/mau-dac-ta.md`](../chung/mau-dac-ta.md)

Tên, mã use case theo [`../chung/danh-muc-use-case.md`](../chung/danh-muc-use-case.md); trạng thái đơn và trạng thái chiếc máy theo mục 4, 5 của [`../chung/lop-va-trang-thai-chung.md`](../chung/lop-va-trang-thai-chung.md).
Quy tắc `QĐx` dẫn lại từ [`dac-ta-nghiep-vu.md`](dac-ta-nghiep-vu.md); mã `YC-…` từ mục 3 của [`../chung/hien-trang-va-yeu-cau.md`](../chung/hien-trang-va-yeu-cau.md).

## Sơ đồ use case phân hệ G4

![Sơ đồ use case phân hệ G4](so-do/uc-phan-he-g4.png)

Sáu use case UC-DH-01 đến UC-DH-06. So với danh mục chung có hai chỗ đổi, theo quyết định ngày 14/10/2026:
**Kế toán** là tác nhân mới kế thừa Nhân viên, làm tác nhân của UC-DH-04 Xác nhận tiền thu khi giao (thay Quản lý cửa hàng),
và tham gia UC-DH-06 ở bước ghi đã hoàn tiền (vì trong use case nghiệp vụ Đổi trả hàng, kế toán là người hoàn tiền).
Danh mục chung không có quan hệ include, extend nào giữa các use case của G4 nên sơ đồ không vẽ.

| Mã | Use case | Tác nhân | Đặc tả |
|---|---|---|---|
| UC-DH-01 | Duyệt, xác nhận đơn hàng | Nhân viên bán hàng | ★ dưới đây |
| UC-DH-02 | Phân công giao hàng, lắp đặt | Quản lý cửa hàng | ✓ dưới đây |
| UC-DH-03 | Cập nhật trạng thái giao hàng | Nhân viên giao hàng – kỹ thuật; Nhân viên bán hàng (đơn gửi đơn vị vận chuyển) | ✓ dưới đây |
| UC-DH-04 | Xác nhận tiền thu khi giao | Kế toán | ✓ dưới đây |
| UC-DH-05 | Lập biên bản lắp đặt | Nhân viên giao hàng – kỹ thuật | ✓ dưới đây |
| UC-DH-06 | Xử lý đổi trả, hoàn tiền | Nhân viên bán hàng (tiếp nhận), Quản lý cửa hàng (duyệt), Kế toán (hoàn tiền) | ★ dưới đây |

## UC-DH-01 Duyệt, xác nhận đơn hàng

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-01 Duyệt, xác nhận đơn hàng |
| Gói | G4 |
| Tác nhân chính | Nhân viên bán hàng |
| Tác nhân phụ | Không có |
| Mô tả | Nhân viên bán hàng gọi khách xác nhận đơn vừa đặt rồi chuyển đơn sang Đã xác nhận để xếp lịch và xuất kho, hoặc huỷ đơn kèm lý do |
| Điều kiện trước | Đã đăng nhập với vai Nhân viên bán hàng; có đơn ở trạng thái Chờ xác nhận (tạo ở UC-MH-04) |
| Điều kiện sau | Đơn ở trạng thái Đã xác nhận, hoặc Đã huỷ kèm lý do; đơn ghi nhân viên duyệt; thêm một dòng `LichSuTrangThaiDon` (người đổi, thời điểm); đơn Đã xác nhận hiện trong danh sách chờ xếp lịch (UC-DH-02) và chờ xuất kho (UC-KB-04) |
| Màn hình | `giao-dien/quan-tri/don-hang.html` (danh sách đơn lọc theo trạng thái); màn hình chi tiết đơn chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang Đơn hàng, chọn lọc "Chờ xác nhận" | Hiện danh sách đơn Chờ xác nhận, đơn mới ở đầu; mỗi dòng có mã đơn, ngày giờ đặt, người nhận, số điện thoại, tổng tiền và số phút đã chờ; đơn chờ quá 30 phút được tô nổi |
| 2 | Chọn một đơn | Hiện chi tiết: người nhận, số điện thoại, địa chỉ giao (nội thành hay tỉnh), sản phẩm, số lượng, đơn giá lúc đặt, khuyến mãi, phí giao, phí lắp, cách thanh toán, tiền cọc đã nhận, có cần lắp đặt không; số máy còn trong kho của từng mẫu |
| 3 | Gọi điện cho khách (ngoài hệ thống) xác nhận sản phẩm, địa chỉ, cách thanh toán; ghi ngày giờ khách muốn nhận vào ô ghi chú, sửa địa chỉ nếu khách đổi | Lưu ghi chú và địa chỉ đã sửa |
| 4 | Bấm "Xác nhận đơn" | Kiểm tra đơn vẫn ở Chờ xác nhận và mỗi mẫu máy trong đơn còn đủ số lượng trong kho |
| 5 | | Chuyển đơn sang Đã xác nhận, ghi nhân viên duyệt, thêm dòng lịch sử trạng thái |
| 6 | | Đưa đơn vào danh sách chờ xếp lịch giao lắp (đơn nội thành) hoặc chờ gửi đơn vị vận chuyển (đơn tỉnh) và danh sách chờ xuất kho; báo "Đã xác nhận đơn #…" |

**Luồng thay thế**
- `1a.` Không có đơn chờ: hiện "Không có đơn chờ xác nhận".
- `1b.` Khách gọi huỷ đơn đã xác nhận nhưng chưa xuất kho: nhân viên lọc "Đã xác nhận", mở đơn, bấm "Huỷ đơn" và làm như `3b`; nút Huỷ chỉ hiện khi đơn chưa có phiếu xuất. Đơn đã sang Đang giao thì không huỷ ở đây; khách từ chối nhận xử lý ở UC-DH-03.
- `3a.` Khách chưa nghe máy: nhân viên bấm "Gọi lại sau"; hệ thống tăng số lần đã gọi, giữ đơn ở Chờ xác nhận và hiện số lần gọi trên danh sách.
- `3b.` Khách báo huỷ, hoặc đã gọi đủ số lần theo mức chủ cửa hàng quy định mà không liên lạc được: nhân viên bấm "Huỷ đơn", chọn lý do (Khách huỷ, Không liên lạc được, Hết hàng, Khác); hệ thống bắt buộc có lý do, chuyển đơn sang Đã huỷ, thêm dòng lịch sử; đơn đã nhận cọc thì hiện số tiền phải hoàn đủ cho khách (QĐ3 của BUC-03).
- `4a.` Không đủ hàng: hệ thống báo mẫu máy thiếu và số còn; khách đồng ý chờ thì nhân viên ghi ngày dự kiến có hàng vào ghi chú, đơn giữ Chờ xác nhận; khách không chờ thì huỷ như `3b` với lý do Hết hàng.
- `4b.` Đơn đã đổi trạng thái trong lúc nhân viên đang gọi (khách tự huỷ ở UC-MH-07, hoặc nhân viên khác đã xác nhận): hệ thống báo "Đơn #… đã ở trạng thái …", không ghi đè.
- `4c.` Đơn có tiền hàng trên 10 triệu đồng chưa có tiền cọc: hệ thống nhắc "Đơn trên 10 triệu: xin cọc 10–20%" kèm số tiền gợi ý; khách chuyển cọc (UC-MH-05) thì đơn hiện số tiền cọc rồi tiếp tục bước 5; khách không cọc thì xử lý theo mức chủ cửa hàng quy định.

**Ngoại lệ**
- Mất kết nối hoặc lỗi máy chủ khi bấm Xác nhận, Huỷ: không đổi trạng thái, không thêm dòng lịch sử; báo "Chưa lưu được, thử lại".
- Phiên đăng nhập hết hạn: chuyển về trang đăng nhập, đơn giữ nguyên trạng thái.

**Quy tắc nghiệp vụ:** QĐ1 (gọi xác nhận trong 30 phút), QĐ3 (huỷ trước khi xuất kho hoàn đủ cọc) của BUC-03; đơn trên 10 triệu xin cọc 10–20% (`MP2 C3`);
YC-DH-01; YC-HT-05 (lưu vết ai đổi trạng thái đơn); chuyển trạng thái theo mục 4 của `lop-va-trang-thai-chung.md` (Chờ xác nhận sang Đã xác nhận hoặc Đã huỷ; Đã xác nhận sang Đã huỷ chỉ khi chưa xuất kho).

![Sơ đồ hoạt động UC-DH-01](so-do/hd-uc-dh-01.png)

## UC-DH-02 Phân công giao hàng, lắp đặt

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-02 Phân công giao hàng, lắp đặt |
| Gói | G4 |
| Tác nhân chính | Quản lý cửa hàng |
| Tác nhân phụ | Nhân viên giao hàng – kỹ thuật (nhận việc được phân công trong lịch của mình) |
| Mô tả | Quản lý cửa hàng xếp ngày, khung giờ giao (kèm lắp nếu có) cho đơn đã xác nhận và phân công nhân viên giao hàng – kỹ thuật; hệ thống cảnh báo khi người được chọn trùng giờ |
| Điều kiện trước | Đã đăng nhập với vai Quản lý cửa hàng; đơn ở trạng thái Đã xác nhận |
| Điều kiện sau | Phiếu giao hàng của đơn có ngày, khung giờ hẹn và người phụ trách (hoặc ghi gửi đơn vị vận chuyển); trạng thái đơn không đổi; lưu người xếp, thời điểm và lý do nếu lưu khi trùng giờ; khách thấy ngày giờ hẹn khi tra đơn (UC-MH-06) |
| Màn hình | Chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang "Lịch giao lắp" | Hiện danh sách đơn Đã xác nhận chưa xếp lịch (mã đơn, địa chỉ, nội thành hay tỉnh, ngày giờ khách muốn nhận, có lắp đặt không) và lịch theo ngày của từng nhân viên giao hàng – kỹ thuật, chia theo khung giờ |
| 2 | Chọn một đơn | Hiện chi tiết đơn và các máy cần lắp; đánh dấu đơn giao kèm lắp |
| 3 | Chọn ngày, khung giờ, nhân viên giao hàng – kỹ thuật phụ trách (có thể thêm người đi cùng), loại việc (Giao, Giao kèm lắp), bấm "Lưu lịch" | Kiểm tra từng người được chọn đã có việc khác trong cùng ngày, cùng khung giờ chưa |
| 4 | | Không trùng: lưu ngày, khung giờ, người phụ trách lên phiếu giao hàng của đơn; ghi người xếp, thời điểm |
| 5 | | Thêm việc vào lịch của nhân viên giao hàng – kỹ thuật; đơn hiện ngày giờ hẹn cho nhân viên bán hàng gọi báo khách và cho khách khi tra đơn |

**Luồng thay thế**
- `2a.` Đơn giao tỉnh: quản lý chọn "Gửi đơn vị vận chuyển" thay cho người phụ trách và nhập ngày gửi dự kiến; hệ thống không kiểm tra trùng; việc theo dõi giao làm ở UC-DH-03 (Nhân viên bán hàng cập nhật).
- `2b.` Đơn có máy âm tủ mà khách chưa có số đo tủ, khoét đá: quản lý xếp thêm một buổi Khảo sát trước buổi giao lắp, làm như bước 3–5.
- `3a.` Trùng khung giờ: hệ thống cảnh báo "Nhân viên … đã có đơn #… lúc …, địa chỉ …", gợi ý người khác còn trống cùng khung giờ và khung giờ trống gần nhất của người đã chọn; quản lý chọn lại (quay lại bước 3), hoặc vẫn lưu khi nhập lý do (ví dụ hai đơn cùng một toà nhà), hệ thống lưu lý do để xem lại.
- `3b.` Ngày giờ chọn đã qua: báo "Chọn ngày giờ sau thời điểm hiện tại", không lưu.
- `5a.` Đổi lịch đã xếp (khách xin đổi giờ, hoặc nhân viên giao hàng – kỹ thuật báo chỗ lắp chưa sẵn sàng): quản lý mở đơn đã xếp, sửa ngày, khung giờ hoặc người; hệ thống kiểm tra trùng như bước 3, giữ lịch cũ trong lịch sử thay đổi.

**Ngoại lệ**
- Hai lần lưu gần như cùng lúc cho cùng một người, cùng khung giờ: hệ thống kiểm tra lại lúc ghi, lần lưu sau nhận cảnh báo trùng như `3a`.
- Mất kết nối khi lưu: không lưu lịch, báo "Chưa lưu được, thử lại".

**Quy tắc nghiệp vụ:** QĐ2 (nội thành tự giao kèm lắp, tỉnh gửi đơn vị vận chuyển), QĐ4 (mỗi người một đơn trong một khung giờ, độ dài khung giờ theo mức chủ cửa hàng quy định) của BUC-03;
QĐ6 của BUC-08 (khảo sát trước khi lắp máy âm); YC-DH-02 (giải quyết vấn đề 3 ở `hien-trang-to-chuc.md`).

![Sơ đồ hoạt động UC-DH-02](so-do/hd-uc-dh-02.png)

## UC-DH-03 Cập nhật trạng thái giao hàng

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-03 Cập nhật trạng thái giao hàng |
| Gói | G4 |
| Tác nhân chính | Nhân viên giao hàng – kỹ thuật (đơn cửa hàng tự giao); Nhân viên bán hàng (đơn gửi đơn vị vận chuyển) |
| Tác nhân phụ | Không có |
| Mô tả | Người phụ trách phiếu giao ghi kết quả giao: đã giao kèm số tiền thu khi giao, hoặc giao không thành công kèm lý do; hệ thống chuyển trạng thái đơn và trạng thái chiếc máy tương ứng |
| Điều kiện trước | Đã đăng nhập với vai tương ứng; đơn có phiếu giao Đã phân công (UC-DH-02); đơn ở trạng thái Đang giao (thủ kho đã lập phiếu xuất ở UC-KB-04) |
| Điều kiện sau | Đã giao: phiếu giao sang Đã giao, ghi tiền thu khi giao; có tiền thu thì thêm một `GiaoDichThanhToan`; các chiếc máy của đơn chuyển Đã xuất sang Đã bán, ghi `ngayBan`; đơn sang Hoàn thành, có số hoá đơn, thêm dòng `LichSuTrangThaiDon`. Không thành công: phiếu giao sang Không thành công kèm lý do; đơn sang Giao không thành công (máy chờ thủ kho nhận lại ở UC-KB-08), hoặc giữ Đang giao nếu khách chỉ hẹn giao lại |
| Màn hình | Chưa có trong `giao-dien/`; website: `quan-tri/g4/giao-hang.html` (việc của tôi), `quan-tri/g4/phieu-giao.html` (chi tiết phiếu) |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở danh sách việc giao của mình (nhân viên bán hàng: danh sách đơn gửi vận chuyển đang chờ) | Hiện các phiếu giao Đã phân công của người đăng nhập, xếp theo ngày, giờ hẹn |
| 2 | Chọn một phiếu | Hiện lịch hẹn, thông tin đơn, các máy và số serial theo phiếu xuất kho, số tiền còn phải thu |
| 3 | Đối chiếu serial trên máy với phiếu xuất kho, giao máy; khách kiểm hàng và ký phiếu giao | |
| 4 | Đơn có lắp: thực hiện UC-DH-05 Lập biên bản lắp đặt | |
| 5 | Nhập số tiền thu khi giao (đơn vận chuyển: tiền đơn vị vận chuyển thu hộ), bấm "Đã giao" | Kiểm tra phiếu còn Đã phân công, người bấm là người phụ trách, đơn đang Đang giao, đơn có lắp thì đã có biên bản khách ký, số tiền không âm |
| 6 | | Ghi tiền thu lên phiếu, phiếu sang Đã giao; tiền thu lớn hơn 0 thì lưu một giao dịch thanh toán; thu đủ thì `trangThaiThanhToan` sang Đã thanh toán |
| 7 | | Chuyển các chiếc máy Đã xuất của đơn sang Đã bán, ghi ngày bán; cấp số hoá đơn; chuyển đơn sang Hoàn thành, thêm dòng lịch sử; báo "Đã ghi giao xong đơn #…" |

**Luồng thay thế**
- `1a.` Không có việc nào: hiện danh sách rỗng.
- `3a.` Khách từ chối nhận: người phụ trách nhập lý do, bấm "Không thành công"; hệ thống bắt buộc có lý do, chuyển phiếu sang Không thành công, chuyển đơn sang Giao không thành công, thêm dòng lịch sử; máy đưa vào danh sách chờ thủ kho nhận lại (UC-KB-08); tiền cọc xử lý theo QĐ3 của BUC-03.
- `3b.` Khách chỉ hẹn giao lại: người phụ trách đánh dấu "chỉ hẹn giao lại" rồi bấm "Không thành công"; hệ thống ghi lý do vào phiếu và ghi chú đơn, đơn giữ Đang giao và quay về danh sách chờ phân công (UC-DH-02).
- `5a.` Thủ kho chưa xuất kho (đơn chưa Đang giao): báo "thủ kho chưa xuất kho nên chưa báo đã giao được", không đổi gì.
- `5b.` Đơn có lắp mà chưa có biên bản khách ký: báo "lập biên bản lắp đặt và có chữ ký khách trước khi báo hoàn thành".
- `5c.` Phiếu phân công cho người khác, hoặc đã được cập nhật: báo "chỉ người đó cập nhật được" hoặc "phiếu đã …, không cập nhật lại được".
- `6a.` Tiền thu ít hơn số còn phải thu: vẫn ghi đã giao, báo số còn thiếu, `trangThaiThanhToan` giữ nguyên (hoá đơn vẫn giao cho khách theo QĐ7).

**Ngoại lệ**
- Mất kết nối hoặc lỗi máy chủ khi lưu: phiếu, đơn, chiếc máy và giao dịch lưu cùng một lần; lỗi thì không lưu gì, báo "Chưa lưu được, thử lại".

**Quy tắc nghiệp vụ:** QĐ2 (đơn tỉnh gửi đơn vị vận chuyển), QĐ3 (huỷ hoặc không nhận sau khi xuất kho), QĐ5 (đối chiếu serial), QĐ6, QĐ7 (hoá đơn giao lúc giao máy kể cả khi khách chưa trả) của BUC-03;
YC-DH-04; YC-HT-05; trạng thái đơn theo mục 4 (Đang giao sang Hoàn thành hoặc Giao không thành công), trạng thái chiếc máy theo mục 5 (Đã xuất sang Đã bán, ghi `ngayBan`) của `lop-va-trang-thai-chung.md`.

**Ghi chú hiện thực:** website không có bước riêng cho khách ký phiếu giao trên hệ thống (chữ ký ở liên giấy); buổi Khảo sát lắp đặt cũng dùng nút "Đã giao" ("Đã khảo sát xong") nhưng không đổi trạng thái đơn.

![Sơ đồ hoạt động UC-DH-03](so-do/hd-uc-dh-03.png)

## UC-DH-04 Xác nhận tiền thu khi giao

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-04 Xác nhận tiền thu khi giao |
| Gói | G4 |
| Tác nhân chính | Kế toán |
| Tác nhân phụ | Không có (nhân viên giao hàng – kỹ thuật nộp tiền ngoài hệ thống) |
| Mô tả | Kế toán nhận tiền thu khi giao do nhân viên giao hàng – kỹ thuật nộp (hoặc tiền đơn vị vận chuyển đối soát), đối chiếu với các phiếu giao đã ghi và xác nhận đã nhận, ghi khoản lệch nếu có |
| Điều kiện trước | Đã đăng nhập với vai Kế toán; có phiếu giao Đã giao có tiền thu khi giao lớn hơn 0 chưa được xác nhận (UC-DH-03) |
| Điều kiện sau | Các phiếu được chọn ghi kế toán xác nhận và thời điểm xác nhận; số tiền lệch (nếu có) kèm ghi chú được ghi vào đơn của các phiếu đó; phiếu đã xác nhận không xác nhận lại được |
| Màn hình | Chưa có trong `giao-dien/`; website: `quan-tri/g4/tien-thu.html` |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang "Tiền thu khi giao" | Hiện các phiếu Đã giao có tiền thu chưa xác nhận, gom theo ngày giao và người nộp (nhân viên hoặc đơn vị vận chuyển), mỗi nhóm có tổng tiền; bên dưới là các phiếu đã xác nhận trong 7 ngày qua |
| 2 | Nhận tiền và liên phiếu giao có chữ ký khách từ người nộp (ngoài hệ thống), đếm tiền | |
| 3 | Chọn các phiếu của nhóm, nhập số tiền thực nhận, bấm "Xác nhận đã nhận" | Kiểm tra mỗi phiếu chưa được ai xác nhận, ở Đã giao và có tiền thu; cộng tổng tiền thu của các phiếu |
| 4 | | So số tiền thực nhận với tổng: khớp thì ghi kế toán xác nhận và thời điểm lên từng phiếu |
| 5 | | Báo "Đã xác nhận nhận đủ … của n phiếu giao" |

**Luồng thay thế**
- `1a.` Không có phiếu chờ xác nhận: hiện danh sách rỗng.
- `3a.` Chưa chọn phiếu nào: báo "Chọn ít nhất một phiếu giao để xác nhận".
- `3b.` Một phiếu đã được kế toán khác xác nhận: báo "Phiếu PG… đã được … xác nhận lúc …, không ghi đè", không lưu gì.
- `4a.` Số tiền nộp lệch tổng mà chưa có ghi chú: báo số lệch thừa hoặc thiếu, bắt buộc ghi chú khoản lệch; kế toán đối chiếu lại từng phiếu với người nộp, ghi chú rồi bấm lại (quay lại bước 3).
- `4b.` Lệch có ghi chú: hệ thống vẫn xác nhận các phiếu, ghi khoản lệch kèm ghi chú vào đơn của từng phiếu, nhắc "Báo quản lý cửa hàng".

**Ngoại lệ**
- Mất kết nối khi lưu: không phiếu nào được xác nhận, báo "Chưa lưu được, thử lại".

**Quy tắc nghiệp vụ:** QĐ8 (tiền thu khi giao nộp kế toán trong ngày), QĐ9 (đơn vị vận chuyển đối soát tiền thu hộ sau 3–5 ngày) của BUC-03, dòng thay thế "Xử lý tiền nộp lệch" tại bước 10 của BUC-03;
YC-DH-06; YC-HT-05 (lưu ai xác nhận, lúc nào).

**Ghi chú hiện thực:** không có thực thể Sổ nộp tiền riêng, phiếu giao đóng vai dòng sổ (kế toán xác nhận, thời điểm); khoản lệch ghi vào ghi chú đơn; hệ thống chỉ nhắc báo quản lý, không gửi thông báo cho quản lý cửa hàng.

![Sơ đồ hoạt động UC-DH-04](so-do/hd-uc-dh-04.png)

## UC-DH-05 Lập biên bản lắp đặt

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-05 Lập biên bản lắp đặt |
| Gói | G4 |
| Tác nhân chính | Nhân viên giao hàng – kỹ thuật |
| Tác nhân phụ | Không có (khách hàng ký biên bản giấy) |
| Mô tả | Nhân viên giao hàng – kỹ thuật lắp máy tại nhà khách, chạy thử, rồi lập biên bản lắp đặt ghi kết quả chạy thử, tình trạng công trình, phát sinh, phí lắp và vật tư, có xác nhận khách đã ký |
| Điều kiện trước | Đã đăng nhập với vai Nhân viên giao hàng – kỹ thuật; là người được phân công phiếu giao loại Giao kèm lắp đặt (đơn nội thành, khách chọn lắp) ở trạng thái Đã phân công |
| Điều kiện sau | Có một `PhieuLapDat` gắn với phiếu giao, ghi kết quả chạy thử, tình trạng công trình, phát sinh, phí lắp, phí vật tư, ngày lập, khách đã ký hay chưa; khi khách đã ký thì phiếu giao được phép báo "Đã giao" ở UC-DH-03 |
| Màn hình | Chưa có trong `giao-dien/`; website: khối "Biên bản lắp đặt" trên `quan-tri/g4/phieu-giao.html` |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở phiếu giao kèm lắp được phân công | Hiện đơn, các máy và số serial theo phiếu xuất kho, khung biên bản lắp đặt (trống nếu chưa lập) |
| 2 | Khảo sát chỗ lắp (tủ, khoét đá, điện, nước), lắp máy, đấu nối, chạy thử cùng khách (ngoài hệ thống) | |
| 3 | Nhập kết quả chạy thử, tình trạng công trình, phát sinh (việc, vật tư ngoài bảng giá đã báo giá khách), phí lắp, phí vật tư; bấm "Lập biên bản" | Kiểm tra phiếu có việc lắp, không phải đơn gửi vận chuyển, người lập là người được phân công, kết quả chạy thử không trống, phí không âm |
| 4 | | Lưu biên bản lắp đặt với ngày lập; báo "Đã lưu biên bản lắp đặt. Khách chưa ký: chưa báo hoàn thành được" |
| 5 | Đưa khách ký biên bản giấy, ký tên người lắp; đánh dấu "Khách đã ký", bấm "Lưu biên bản" | Lưu lại biên bản với khách đã ký; báo "Đã lưu biên bản lắp đặt (khách đã ký)" |
| 6 | | Phiếu giao cho phép báo "Đã giao"; số tiền gợi ý thu cộng thêm phí vật tư và phần phí lắp vượt mức đã tính trên đơn; tiếp tục bước 5 của UC-DH-03 |

**Luồng thay thế**
- `2a.` Chỗ lắp chưa sẵn sàng (tủ chưa xong, chưa khoét đá, thiếu điện nước): nhân viên ghi tình trạng công trình, hẹn với khách, bấm "Không thành công" kèm lý do và đánh dấu "chỉ hẹn giao lại" (UC-DH-03 `3b`); quản lý cửa hàng xếp lại buổi lắp (UC-DH-02).
- `2b.` Máy lỗi khi chạy thử, không do lắp đặt: nhân viên ghi lỗi vào kết quả chạy thử của biên bản, mang máy về; bấm "Không thành công" kèm lý do; cửa hàng đổi máy cùng mẫu và hẹn lắp lại.
- `3a.` Bỏ trống kết quả chạy thử hoặc phí âm: báo "Ghi kết quả chạy thử" hoặc "Phí lắp và phí vật tư không được âm", không lưu.
- `3b.` Phiếu phân công cho người khác hoặc đã cập nhật xong: báo không cập nhật được, không lưu.
- `5a.` Sửa biên bản trước khi báo đã giao: mở lại phiếu, sửa các ô rồi "Lưu biên bản"; hệ thống ghi đè biên bản của phiếu (mỗi phiếu giao một biên bản).

**Ngoại lệ**
- Mất kết nối khi lưu: biên bản không được lưu, báo "Chưa lưu được, thử lại".

**Quy tắc nghiệp vụ:** QĐ1–QĐ7 của BUC-08 (máy âm tủ và máy lọc nước lắp tận nơi, chỉ đơn nội thành, công lắp miễn phí trên 5 triệu, vật tư tính riêng theo bảng giá, ngoài danh mục phải báo giá trước, khảo sát trước khi lắp máy âm, biên bản ghi đủ và có chữ ký khách);
YC-DH-05.

**Ghi chú hiện thực:** biên bản không lưu riêng danh sách máy và serial (lấy theo phiếu xuất kho của đơn khi hiện); chưa có bảng giá công lắp và vật tư trong hệ thống nên phí lắp, phí vật tư nhập tay; nhánh `2b` chưa có nút riêng, nhân viên dùng "Không thành công" như `2a`.

![Sơ đồ hoạt động UC-DH-05](so-do/hd-uc-dh-05.png)

## UC-DH-06 Xử lý đổi trả, hoàn tiền

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-DH-06 Xử lý đổi trả, hoàn tiền |
| Gói | G4 |
| Tác nhân chính | Nhân viên bán hàng (tiếp nhận yêu cầu) |
| Tác nhân phụ | Quản lý cửa hàng (duyệt hoặc từ chối); Kế toán (ghi đã hoàn tiền) |
| Mô tả | Nhân viên bán hàng lập yêu cầu đổi trả cho chiếc máy khách mang lại, hệ thống đối chiếu hạn theo serial, quản lý cửa hàng duyệt; máy về kho, khách được đổi máy mới hoặc hoàn tiền theo cách đã trả |
| Điều kiện trước | Người làm từng bước đã đăng nhập với vai tương ứng; đơn ở trạng thái Hoàn thành; chiếc máy khách mang lại ở trạng thái Đã bán |
| Điều kiện sau | Có một `YeuCauDoiTra` ở trạng thái Đã duyệt hoặc Từ chối, ghi người duyệt. Khi duyệt: chiếc máy chuyển Đã bán sang Trả về kho. Trả hàng: số tiền hoàn ghi trên yêu cầu; khách trả toàn bộ đơn thì đơn sang Đã trả hàng, `trangThaiThanhToan` sang Đã hoàn tiền. Đổi máy: đơn có thêm phiếu xuất máy mới, đơn giữ Hoàn thành |
| Màn hình | Chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Nhân viên bán hàng: tìm đơn theo mã đơn, số điện thoại hoặc số serial, bấm "Tạo yêu cầu đổi trả" | Hiện các chiếc máy của đơn: tên, model, serial, ngày bán, số ngày đã qua từ ngày bán, đã lắp hay chưa |
| 2 | Chọn chiếc máy khách mang lại, chọn hình thức (Đổi máy, Trả hàng), loại lý do (Lỗi nhà sản xuất, Không đúng mô tả, Đổi ý), ghi tình trạng máy (còn seal, đã lắp, bên ngoài), bấm "Gửi duyệt" | Kiểm tra serial thuộc đơn này và đang ở trạng thái Đã bán |
| 3 | | Đối chiếu điều kiện: đổi ý thì trong 7 ngày, còn seal, chưa lắp; lỗi nhà sản xuất thì trong 30 ngày; không đúng mô tả thì luôn nhận; hiện "Đủ điều kiện" hoặc điều kiện nào không đạt |
| 4 | | Tạo `YeuCauDoiTra` trạng thái Chờ duyệt, lưu kết quả đối chiếu; báo Quản lý cửa hàng có yêu cầu chờ duyệt |
| 5 | Quản lý cửa hàng: mở yêu cầu chờ duyệt | Hiện yêu cầu, hoá đơn, các lần bảo hành của chiếc máy, phí khấu trừ tính theo mức chủ cửa hàng đặt |
| 6 | Sửa phí khấu trừ nếu cần, bấm "Duyệt" | Chuyển yêu cầu sang Đã duyệt, ghi người duyệt, thời điểm; báo nhân viên bán hàng |
| 7 | Nhân viên bán hàng: nhận lại máy, hoá đơn, phụ kiện từ khách, bấm "Đã nhận máy" | Chuyển chiếc máy sang Trả về kho; đưa vào danh sách chờ thủ kho nhận về kho (UC-KB-08) |
| 8 | Kế toán: mở yêu cầu đã duyệt hình thức Trả hàng | Hiện số tiền phải hoàn (số khách đã trả cho chiếc máy trừ phí khấu trừ) và cách khách đã trả |
| 9 | Hoàn tiền cho khách theo đúng cách đó (ngoài hệ thống), nhập mã giao dịch hoặc ghi chú, bấm "Đã hoàn tiền" | Lưu số tiền, ngày hoàn, người hoàn; khách trả toàn bộ đơn thì chuyển đơn sang Đã trả hàng, `trangThaiThanhToan` sang Đã hoàn tiền; thêm dòng lịch sử trạng thái; báo nhân viên bán hàng |

**Luồng thay thế**
- `2a.` Serial không thuộc đơn hoặc không phải máy cửa hàng bán: báo "Serial không khớp đơn", không tạo yêu cầu.
- `2b.` Chiếc máy đã có một yêu cầu đổi trả Chờ duyệt: báo trùng và mở yêu cầu đang có.
- `3a.` Không đủ điều kiện (quá 7 ngày, mất seal, đã lắp mà không phải lỗi nhà sản xuất, lỗi nhà sản xuất quá 30 ngày): hệ thống ghi rõ điều kiện không đạt; lỗi nhà sản xuất quá 30 ngày thì gợi ý chuyển sang Tiếp nhận bảo hành (UC-KB-06); khách xin xét ngoại lệ thì nhân viên vẫn gửi duyệt, yêu cầu được đánh dấu "Ngoại lệ".
- `6a.` Từ chối: quản lý bấm "Từ chối", bắt buộc nhập lý do; yêu cầu sang Từ chối, chiếc máy giữ Đã bán; nhân viên bán hàng báo khách; use case kết thúc.
- `6b.` Yêu cầu đánh dấu Ngoại lệ: quản lý xin ý kiến chủ cửa hàng (ngoài hệ thống), ghi ý kiến của chủ vào yêu cầu rồi Duyệt hoặc Từ chối theo ý chủ.
- `6c.` Lý do Không đúng mô tả: hệ thống không cho Từ chối; quản lý chỉ chọn đổi máy hoặc trả hàng theo ý khách.
- `8a.` Hình thức Đổi máy: không qua kế toán; hệ thống tạo yêu cầu xuất một máy cùng mẫu cho đơn, thủ kho lập phiếu xuất chọn serial mới (UC-KB-04); nhân viên bán hàng giao máy mới, bấm "Đã giao máy đổi"; hệ thống chuyển chiếc máy mới sang Đã bán, đơn giữ Hoàn thành.
- `9a.` Khách chỉ trả một phần đơn: đơn giữ Hoàn thành, `trangThaiThanhToan` giữ nguyên; số tiền hoàn chỉ ghi trên yêu cầu đổi trả (quy tắc chung 4).
- `9b.` Kế toán chọn cách hoàn khác cách khách đã trả: hệ thống không cho lưu, báo "Hoàn tiền theo cách khách đã trả".

**Ngoại lệ**
- Mất kết nối khi lưu: yêu cầu đổi trả và trạng thái chiếc máy lưu cùng một lần; lỗi thì không lưu gì, báo "Chưa lưu được, thử lại".
- Hai người cùng xử lý một yêu cầu: người lưu sau nhận báo "Yêu cầu đã được … xử lý lúc …", không ghi đè.

**Quy tắc nghiệp vụ:** QĐ1–QĐ9 của BUC-04 (hạn 7 ngày, 30 ngày; máy đã lắp chỉ đổi khi lỗi hãng; không đúng mô tả bắt buộc nhận lại; ngoại lệ hỏi chủ; phí khấu trừ theo mức chủ quy định; hoàn theo cách khách đã trả; máy lỗi để riêng);
YC-DH-07, YC-DH-08; YC-HT-05; trạng thái chiếc máy theo mục 5 (Đã bán sang Trả về kho), trạng thái đơn theo mục 4 (Hoàn thành sang Đã trả hàng) của `lop-va-trang-thai-chung.md`.

![Sơ đồ hoạt động UC-DH-06](so-do/hd-uc-dh-06.png)
