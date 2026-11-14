# Đặc tả use case hệ thống · G5 Kho, nhà cung cấp, bảo hành

Bản 0.1 (14/10/2026) · G5 · làm theo khuôn mục 2 của [`../chung/mau-dac-ta.md`](../chung/mau-dac-ta.md)

Tên, mã use case theo [`../chung/danh-muc-use-case.md`](../chung/danh-muc-use-case.md); trạng thái đơn và trạng thái chiếc máy theo mục 4, 5 của [`../chung/lop-va-trang-thai-chung.md`](../chung/lop-va-trang-thai-chung.md).
Quy tắc `QĐx` dẫn lại từ [`dac-ta-nghiep-vu.md`](dac-ta-nghiep-vu.md); mã `YC-…` từ mục 3 của [`../chung/hien-trang-va-yeu-cau.md`](../chung/hien-trang-va-yeu-cau.md).

| Mã | Use case | Tác nhân | Đặc tả |
|---|---|---|---|
| UC-KB-01 | Quản lý nhà cung cấp | Thủ kho | |
| UC-KB-02 | Lập đơn đặt hàng nhà cung cấp | Thủ kho | |
| UC-KB-03 | Lập phiếu nhập kho, ghi số serial | Thủ kho | ★ dưới đây |
| UC-KB-04 | Lập phiếu xuất kho theo đơn | Thủ kho | ✓ dưới đây |
| UC-KB-05 | Kiểm kê, cảnh báo tồn thấp | Thủ kho | |
| UC-KB-06 | Tiếp nhận bảo hành («include» UC-KB-07) | Nhân viên bán hàng | ★ dưới đây |
| UC-KB-07 | Tra cứu bảo hành (theo số serial hoặc số điện thoại) | Khách vãng lai | |
| UC-KB-08 | Nhận lại máy về kho (giao không thành công, đổi trả) | Thủ kho | |

## UC-KB-03 Lập phiếu nhập kho, ghi số serial

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-KB-03 Lập phiếu nhập kho, ghi số serial |
| Gói | G5 |
| Tác nhân chính | Thủ kho |
| Tác nhân phụ | Không có |
| Mô tả | Thủ kho nhận hàng nhà cung cấp giao, lập phiếu nhập kho theo mẫu 01-VT ghi số lượng theo chứng từ và số lượng thực nhập, ghi số serial của từng chiếc máy đạt yêu cầu; các chiếc máy vào kho ở trạng thái Trong kho |
| Điều kiện trước | Đã đăng nhập với vai Thủ kho; nhà cung cấp đã có trong danh mục (UC-KB-01); có đơn đặt hàng nhà cung cấp đã duyệt (UC-KB-02); mẫu máy đã có trong danh mục sản phẩm |
| Điều kiện sau | Có một `PhieuNhap` ghi nhà cung cấp, số hoá đơn, ngày nhập, người lập; mỗi dòng có mẫu máy, số lượng theo chứng từ, số lượng thực nhập, đơn giá, tình trạng hàng; mỗi chiếc máy thực nhập có một `SanPhamSerial` mới ở trạng thái Trong kho, ghi `ngayNhap`; số tồn của mẫu máy tăng theo số thực nhập; phiếu đã lưu thì khoá |
| Màn hình | Chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang "Nhập kho", bấm "Lập phiếu nhập" | Hiện danh sách đơn đặt hàng nhà cung cấp đã duyệt chưa nhập đủ (mã đơn, nhà cung cấp, ngày giao dự kiến) |
| 2 | Chọn đơn đặt hàng của lô hàng vừa về, nhập số hoá đơn nhà cung cấp, ngày hoá đơn, tên người giao | Tạo phiếu nhập nháp; chép sẵn từng mẫu máy, số lượng đặt, đơn giá từ đơn đặt hàng |
| 3 | Với từng mẫu máy: nhập số lượng theo chứng từ (theo hoá đơn), số lượng thực nhập (sau khi đếm, kiểm thùng, bật thử máy giá trị cao), tình trạng hàng | Kiểm tra số thực nhập không lớn hơn số theo chứng từ; mẫu máy thuộc nhóm giá trị cao thì nhắc "Bật thử trước khi nhận" |
| 4 | Với từng chiếc máy thực nhập: quét hoặc gõ số serial | Kiểm tra serial không để trống, chưa có trong hệ thống và chưa nhập trùng trên phiếu này; đếm số serial đã ghi của từng mẫu máy so với số thực nhập |
| 5 | Bấm "Lưu phiếu nhập" | Kiểm tra mỗi mẫu máy có đủ số serial bằng số thực nhập |
| 6 | | Lưu `PhieuNhap` cùng các dòng chi tiết; tạo `SanPhamSerial` cho từng chiếc ở trạng thái Trong kho, ghi `ngayNhap`; tăng số tồn của mẫu máy; ghi người lập, thời điểm; khoá phiếu |
| 7 | | Cập nhật số đã nhập của đơn đặt hàng nhà cung cấp; hiện phiếu nhập để in 2 liên cho thủ kho và người giao ký; đưa phiếu vào danh sách chờ kế toán đối chiếu với hoá đơn nhà cung cấp |

**Luồng thay thế**
- `1a.` Không có đơn đặt hàng nào đã duyệt chưa nhập: hiện "Không có đơn đặt hàng chờ nhập"; hàng không có đơn đặt hàng thì không nhận vào kho (QĐ1 của BUC-05).
- `3a.` Hàng thiếu, thùng móp hoặc máy bật thử không chạy: thủ kho trả chiếc máy hư cho người giao, nhập số thực nhập nhỏ hơn số theo chứng từ và ghi lý do (thiếu, hư) vào tình trạng hàng; hệ thống ghi phần chênh lệch để báo nhà cung cấp giao bù hoặc đổi máy; hàng giao bù về sau lập phiếu nhập mới cho cùng đơn đặt hàng.
- `3b.` Số thực nhập lớn hơn số theo chứng từ: báo "Số thực nhập không được lớn hơn số theo chứng từ", không cho đi tiếp.
- `4a.` Serial đã có trong hệ thống hoặc đã gõ trên phiếu này: báo "Serial … đã tồn tại (phiếu nhập #…)", không nhận serial đó; thủ kho đọc lại tem trên máy rồi nhập lại.
- `4b.` Lô hàng đặt riêng cho đơn của khách: thủ kho chọn mã đơn của khách cho những chiếc máy đó; hệ thống gắn mã đơn vào serial để UC-KB-04 chỉ xuất những chiếc này cho đúng đơn đó.
- `5a.` Số serial đã ghi của một mẫu máy khác số thực nhập: báo "Mẫu … còn thiếu … serial" (hoặc thừa), không lưu.
- `7a.` Phát hiện sai sau khi phiếu đã khoá: không sửa phiếu; thủ kho lập phiếu điều chỉnh dẫn số phiếu gốc, ghi lý do; hệ thống cập nhật serial và số tồn theo phiếu điều chỉnh.

**Ngoại lệ**
- Mất kết nối khi lưu: phiếu nhập, các serial và số tồn lưu cùng một lần; lỗi thì không lưu gì, giữ nguyên phiếu nháp trên màn hình, báo "Chưa lưu được, thử lại".
- Hai thủ kho cùng nhập một serial gần như cùng lúc: hệ thống kiểm tra lại lúc ghi, người lưu sau nhận báo trùng như `4a`.

**Quy tắc nghiệp vụ:** QĐ1 (đơn đặt hàng đã duyệt mới nhận), QĐ4 (bật thử máy giá trị cao), QĐ5 (mọi chiếc máy đều ghi serial, kể cả hàng giá thấp), QĐ6 (mẫu 01-VT có số theo chứng từ và thực nhập, 2 liên), QĐ7 (phiếu đã ký không sửa, lập phiếu điều chỉnh), QĐ8 (công nợ theo số thực nhập) của BUC-05;
YC-KB-02, YC-KB-04; YC-HT-05; trạng thái chiếc máy theo mục 5 của `lop-va-trang-thai-chung.md` ((mới) sang Trong kho). Serial là duy nhất trong toàn hệ thống (nhóm đặt).

## UC-KB-06 Tiếp nhận bảo hành

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-KB-06 Tiếp nhận bảo hành |
| Gói | G5 |
| Tác nhân chính | Nhân viên bán hàng |
| Tác nhân phụ | Thủ kho (ghi ngày gửi trung tâm bảo hành hãng và ngày nhận máy về) |
| Mô tả | Nhân viên bán hàng tiếp nhận máy lỗi của khách, tra chiếc máy theo serial (UC-KB-07), lập phiếu tiếp nhận bảo hành ghi lỗi và ngày hẹn trả; hệ thống đếm số lần bảo hành của chiếc máy, theo dõi máy gửi hãng tới khi trả lại khách |
| Điều kiện trước | Người làm từng bước đã đăng nhập với vai tương ứng; chiếc máy khách mang tới ở trạng thái Đã bán |
| Điều kiện sau | Có một `PhieuBaoHanh` gắn với chiếc máy, ghi ngày nhận, mô tả lỗi, tình trạng bên ngoài, ngày hẹn trả, người tiếp nhận; chiếc máy chuyển Đã bán sang Đang bảo hành, `ngayBan` giữ nguyên; số lần bảo hành của chiếc máy tăng một. Khi trả máy: phiếu ghi ngày gửi hãng, ngày nhận về, ngày trả; chiếc máy chuyển Đang bảo hành sang Đã bán; hạn bảo hành cộng thêm số ngày máy nằm ở hãng |
| Màn hình | Chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Nhân viên bán hàng: mở trang "Bảo hành", bấm "Tiếp nhận bảo hành" | Hiện ô tìm theo số serial, số điện thoại người mua hoặc mã đơn |
| 2 | Nhập số serial đọc trên tem máy (hoặc số điện thoại, mã đơn), bấm Tra | Thực hiện UC-KB-07 Tra cứu bảo hành: hiện chiếc máy (tên, model, serial), đơn, người mua, ngày bán, hạn bảo hành, các lần bảo hành trước và số ngày đã nằm ở hãng |
| 3 | Chọn chiếc máy, bấm "Lập phiếu tiếp nhận" | Kiểm tra chiếc máy ở trạng thái Đã bán và còn hạn bảo hành; đếm số lần bảo hành đã có |
| 4 | Ghi lời khách tả lỗi, tình trạng bên ngoài (trầy, móp, thiếu phụ kiện), chọn loại lỗi (Lỗi của máy, Lỗi do lắp đặt); chọn ngày hẹn trả; bấm Lưu | Gợi ý ngày hẹn trả trong khoảng 7–15 ngày; kiểm tra các ô bắt buộc |
| 5 | | Tạo `PhieuBaoHanh` ghi người tiếp nhận, thời điểm; chuyển chiếc máy Đã bán sang Đang bảo hành; hiện phiếu để in 2 liên cho hai bên ký, một liên giao khách; đưa máy vào danh sách chờ thủ kho gửi hãng |
| 6 | Thủ kho: chọn phiếu chờ gửi, gửi máy tới trung tâm bảo hành hãng (ngoài hệ thống), bấm "Đã gửi hãng" | Ghi ngày gửi, người gửi; phiếu hiện trong danh sách "Máy đang ở hãng" kèm số ngày đã gửi |
| 7 | Thủ kho: nhận máy đã sửa, kiểm tra máy chạy được, bấm "Đã nhận về" | Ghi ngày nhận về, tính số ngày máy nằm ở hãng; báo nhân viên bán hàng gọi khách tới nhận |
| 8 | Nhân viên bán hàng: báo khách (ngoài hệ thống); khách tới, thu lại liên phiếu của khách, trả máy, khách ký nhận; bấm "Đã trả khách" | Ghi ngày trả; chuyển chiếc máy Đang bảo hành sang Đã bán, giữ nguyên `ngayBan`; cộng số ngày máy nằm ở hãng vào hạn bảo hành |

**Luồng thay thế**
- `2a.` Không tìm thấy chiếc máy hoặc serial trên máy khác chứng từ (theo UC-KB-07): hệ thống báo "Không tìm thấy máy do cửa hàng bán"; nhân viên bán hàng hướng dẫn khách tới nơi bán hoặc trung tâm bảo hành hãng; use case kết thúc.
- `3a.` Máy hết hạn bảo hành: hệ thống báo "Hết hạn bảo hành từ ngày …"; khách đồng ý trả phí theo báo giá của trung tâm bảo hành hãng thì nhân viên đánh dấu "Sửa có phí" và tiếp tục bước 4; khách từ chối thì không lập phiếu, use case kết thúc.
- `3b.` Lần tiếp nhận này là lần bảo hành thứ 3 của chiếc máy: hệ thống cảnh báo "Máy đã bảo hành 2 lần, đây là lần thứ 3: lần này sửa không được thì đổi máy mới hoặc hoàn tiền"; nhân viên báo khách rồi tiếp tục bước 4.
- `3c.` Chiếc máy đã bảo hành từ 3 lần trở lên mà vẫn lỗi: hệ thống cảnh báo đỏ, không cho gửi hãng nữa, gợi ý "Chuyển sang đổi trả" và mở UC-DH-06 Xử lý đổi trả, hoàn tiền với chiếc máy này; use case kết thúc.
- `3d.` Chiếc máy đang có một phiếu bảo hành chưa trả (trạng thái Đang bảo hành): báo "Máy đang bảo hành theo phiếu #…" và mở phiếu đó, không tạo phiếu mới.
- `4a.` Lỗi do lắp đặt: hệ thống lưu phiếu với loại lỗi Lỗi do lắp đặt, không đưa vào danh sách chờ gửi hãng, báo quản lý cửa hàng phân công nhân viên giao hàng – kỹ thuật tới sửa (như UC-DH-02); sửa xong nhân viên bán hàng ghi kết quả, ngày xong, hệ thống chuyển máy về Đã bán; use case kết thúc.
- `5a.` Máy âm tủ hoặc cồng kềnh, khách không mang tới được: nhân viên chọn "Bảo hành tại nhà"; hệ thống không đưa máy vào danh sách chờ gửi; nhân viên báo trung tâm bảo hành hãng cử kỹ thuật tới nhà khách (ngoài hệ thống); hãng sửa xong, nhân viên gọi khách xác nhận rồi ghi kết quả, ngày xong; hệ thống chuyển máy về Đã bán; use case kết thúc.
- `6a.` Máy ở hãng quá ngày hẹn trả: danh sách "Máy đang ở hãng" tô nổi phiếu quá hạn; nhân viên bán hàng gọi báo khách ngày hẹn mới, hệ thống lưu ngày hẹn mới và giữ ngày hẹn cũ trong lịch sử phiếu.
- `7a.` Hãng báo không sửa được: thủ kho chọn "Hãng không sửa được"; hệ thống báo nhân viên bán hàng chuyển sang đổi máy hoặc hoàn tiền (UC-DH-06).

**Ngoại lệ**
- Mất kết nối khi lưu phiếu: phiếu bảo hành và trạng thái chiếc máy lưu cùng một lần; lỗi thì không lưu gì, báo "Chưa lưu được, thử lại".
- Hai người cùng cập nhật một phiếu: người lưu sau nhận báo "Phiếu đã được … cập nhật lúc …", không ghi đè.

**Quy tắc nghiệp vụ:** QĐ1 (giao khách văn bản tiếp nhận ghi lỗi, tình trạng, ngày hẹn trả), QĐ2 (cửa hàng không tự sửa, gửi hãng 7–15 ngày), QĐ3 (kỹ thuật cửa hàng chỉ xử lý lỗi lắp đặt), QĐ4 (bảo hành tại nhà cho máy âm tủ, cồng kềnh), QĐ5 (3 lần sửa không được thì đổi mới hoặc hoàn tiền), QĐ6 (thời gian ở hãng không tính vào hạn), QĐ7 (hết hạn chỉ sửa có phí khi khách đồng ý), QĐ8 của BUC-06; QĐ1–QĐ6 của BUC-09 (qua UC-KB-07);
YC-KB-06, YC-KB-07, YC-KB-08; trạng thái chiếc máy theo mục 5 của `lop-va-trang-thai-chung.md` (Đã bán sang Đang bảo hành, Đang bảo hành sang Đã bán, giữ nguyên `ngayBan`).

## UC-KB-04 Lập phiếu xuất kho theo đơn

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-KB-04 Lập phiếu xuất kho theo đơn |
| Gói | G5 |
| Tác nhân chính | Thủ kho |
| Tác nhân phụ | Không có |
| Mô tả | Thủ kho lập phiếu xuất kho theo mẫu 02-VT cho đơn đã xác nhận, chọn đúng những chiếc máy (theo serial) đang trong kho của từng mẫu máy trong đơn; máy sang Đã xuất, đơn sang Đang giao |
| Điều kiện trước | Đã đăng nhập với vai Thủ kho; đơn ở trạng thái Đã xác nhận (UC-DH-01); đã có lịch giao hoặc ghi gửi đơn vị vận chuyển (UC-DH-02) |
| Điều kiện sau | Có một `PhieuXuat` của đơn, ghi lý do xuất, ngày xuất, người lập, người nhận hàng (nhân viên giao hàng – kỹ thuật hoặc đơn vị vận chuyển), số lượng yêu cầu và thực xuất của từng mẫu máy, đúng những serial đã xuất; các chiếc máy chuyển Trong kho sang Đã xuất; số tồn của mẫu máy giảm; đơn chuyển Đã xác nhận sang Đang giao, thêm một dòng `LichSuTrangThaiDon`; phiếu đã lưu thì khoá |
| Màn hình | Chưa có, thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở trang "Xuất kho" | Hiện danh sách đơn Đã xác nhận chờ xuất kho (mã đơn, người nhận, ngày giờ hẹn giao, người giao hoặc đơn vị vận chuyển), đơn hẹn giao sớm ở đầu |
| 2 | Chọn một đơn, bấm "Lập phiếu xuất" | Tạo phiếu xuất nháp với lý do "Xuất bán theo đơn #…"; chép từng mẫu máy và số lượng yêu cầu từ đơn; với mỗi mẫu máy hiện danh sách serial đang Trong kho, serial nhập trước ở đầu |
| 3 | Với từng chiếc máy lấy trên kệ: quét hoặc chọn số serial | Kiểm tra serial thuộc đúng mẫu máy của dòng, đang ở trạng thái Trong kho, chưa chọn trên phiếu này và không gắn riêng cho đơn khác |
| 4 | Bấm "Lưu phiếu xuất" | Kiểm tra mỗi mẫu máy có số serial đã chọn bằng số lượng yêu cầu và đơn vẫn ở Đã xác nhận |
| 5 | | Lưu `PhieuXuat` với số lượng yêu cầu, thực xuất và các serial; chuyển từng chiếc máy Trong kho sang Đã xuất; giảm số tồn; ghi người lập, thời điểm; khoá phiếu |
| 6 | | Chuyển đơn Đã xác nhận sang Đang giao, thêm dòng lịch sử trạng thái; hiện phiếu xuất để in, người nhận hàng ký; đơn hiện trong danh sách theo dõi giao hàng (UC-DH-03) |

**Luồng thay thế**
- `1a.` Không có đơn chờ xuất: hiện "Không có đơn chờ xuất kho".
- `2a.` Đơn có máy đặt riêng (serial đã gắn mã đơn ở UC-KB-03): hệ thống chỉ hiện những serial gắn với đơn này cho mẫu máy đó.
- `2b.` Mẫu máy trong đơn không đủ số chiếc Trong kho: báo "Mẫu … còn …, đơn cần …"; không cho lưu phiếu, báo nhân viên bán hàng gọi khách hẹn lại; thủ kho có thể lập đơn đặt hàng nhà cung cấp (UC-KB-02).
- `3a.` Serial không thuộc mẫu máy của dòng, không ở trạng thái Trong kho, hoặc đã gắn cho đơn khác: báo "Serial … không xuất được: …" kèm lý do, không nhận serial đó.
- `3b.` Serial đã quét trên phiếu này: báo trùng, không nhận lần hai.
- `4a.` Số serial đã chọn khác số lượng yêu cầu: báo "Mẫu … cần … chiếc, đã chọn …", không lưu.
- `4b.` Đơn đã đổi trạng thái trong lúc lập phiếu (khách huỷ ở UC-DH-01): báo "Đơn #… đã ở trạng thái …", không lưu phiếu, các serial giữ Trong kho.
- `5a.` Xuất máy đổi cho yêu cầu đổi trả đã duyệt (từ UC-DH-06): lý do xuất là "Xuất đổi máy theo yêu cầu #…"; hệ thống làm như bước 3–5 nhưng không đổi trạng thái đơn (đơn giữ Hoàn thành).
- `6a.` Phát hiện xuất nhầm serial sau khi phiếu đã khoá: không sửa phiếu; thủ kho lập phiếu điều chỉnh dẫn số phiếu gốc, ghi lý do; hệ thống đổi trạng thái hai chiếc máy theo phiếu điều chỉnh.

**Ngoại lệ**
- Mất kết nối khi lưu: phiếu xuất, trạng thái các chiếc máy, số tồn và trạng thái đơn lưu cùng một lần; lỗi thì không lưu gì, báo "Chưa lưu được, thử lại".
- Hai thủ kho cùng chọn một serial cho hai phiếu: hệ thống kiểm tra lại lúc ghi, người lưu sau nhận báo như `3a`.

**Quy tắc nghiệp vụ:** Phiếu xuất là chứng từ duy nhất ghi serial theo đơn, dùng để tra bảo hành (BUC-09 bước 2, QĐ4); xuất đúng serial gắn riêng cho đơn đặt riêng (dòng thay thế tại bước 6 của BUC-05);
YC-KB-03, YC-KB-04; YC-HT-05 (lưu vết ai xuất kho, lúc nào); trạng thái chiếc máy theo mục 5 (Trong kho sang Đã xuất), trạng thái đơn theo mục 4 (Đã xác nhận sang Đang giao) của `lop-va-trang-thai-chung.md`.

## Sơ đồ use case phân hệ G5

![Sơ đồ use case phân hệ Kho, nhà cung cấp, bảo hành](so-do/uc-phan-he-g5.png)
