# Đặc tả use case hệ thống · G3 Mua hàng trực tuyến và thanh toán

Bản 0.1 (14/10/2026) · G3 · việc t102, t103 · theo khuôn mục 2 của [`../chung/mau-dac-ta.md`](../chung/mau-dac-ta.md); mã, tên use case theo [`../chung/danh-muc-use-case.md`](../chung/danh-muc-use-case.md)

Quy tắc `QĐx` dẫn lại từ use case nghiệp vụ BUC-02 ở [`dac-ta-nghiep-vu.md`](dac-ta-nghiep-vu.md); mã `YC-…` ở [`../chung/hien-trang-va-yeu-cau.md`](../chung/hien-trang-va-yeu-cau.md) mục 3, 4.
Trạng thái đơn hàng dùng đúng tên ở [`../chung/lop-va-trang-thai-chung.md`](../chung/lop-va-trang-thai-chung.md) mục 4.

## Sơ đồ use case phân hệ G3

![Sơ đồ use case phân hệ G3](so-do/uc-phan-he-g3.png)

G3 chốt (14/10/2026): **khách vãng lai được đặt hàng** chỉ với họ tên, số điện thoại, địa chỉ (`YC-MH-01`; `TL-18`; 3/10 vai khách mô phỏng chỉ muốn nhập số điện thoại, `MK K11`).
Vì vậy Đặt hàng, Thanh toán trực tuyến, Theo dõi đơn hàng, Huỷ đơn hàng nối với **Khách vãng lai**; Khách hàng kế thừa Khách vãng lai nên dùng được tất cả, riêng Quản lý hồ sơ, sổ địa chỉ chỉ Khách hàng có.
Áp mã giảm giá chỉ chạy bên trong Đặt hàng khi khách nhập mã (`<<extend>>`), nên không nối riêng với tác nhân. Cổng thanh toán là tác nhân phụ của Thanh toán trực tuyến.

| Mã | Use case | Tác nhân | Quan hệ | Đặc tả |
|---|---|---|---|---|
| UC-MH-01 | Đăng ký tài khoản khách hàng | Khách vãng lai | | |
| UC-MH-02 | Quản lý hồ sơ, sổ địa chỉ | Khách hàng | | |
| UC-MH-03 | Quản lý giỏ hàng | Khách vãng lai | | |
| UC-MH-04 | Đặt hàng | Khách vãng lai | | ★ |
| UC-MH-05 | Thanh toán trực tuyến | Khách vãng lai; Cổng thanh toán (phụ) | | ★ |
| UC-MH-06 | Theo dõi đơn hàng | Khách vãng lai | | ✓ |
| UC-MH-07 | Huỷ đơn hàng | Khách vãng lai | | |
| UC-MH-08 | Áp mã giảm giá | (qua UC-MH-04) | `<<extend>>` UC-MH-04 | |

## UC-MH-04 Đặt hàng

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-MH-04 Đặt hàng |
| Gói | G3 |
| Tác nhân chính | Khách vãng lai; Khách hàng (kế thừa Khách vãng lai) dùng được và có sẵn địa chỉ trong sổ địa chỉ |
| Tác nhân phụ | Không có |
| Mô tả | Khách xác nhận các mẫu máy trong giỏ, nhập thông tin nhận hàng, chọn lắp đặt và cách thanh toán, xem lại đủ chi tiết đơn rồi đồng ý đặt, để cửa hàng có đơn hàng chờ gọi xác nhận |
| Điều kiện trước | Giỏ hàng có ít nhất một mẫu máy đang bán (UC-MH-03). Không bắt buộc đăng nhập |
| Điều kiện sau | Có một `DonHang` mới (mã đơn, ngày đặt, họ tên và số điện thoại người đặt, người nhận, địa chỉ giao, có lắp đặt hay không, cách thanh toán, số tiền cọc phải trả nếu có) với các `ChiTietDonHang` lưu giá lúc đặt; đơn ở trạng thái **Chờ xác nhận**, `trangThaiThanhToan` là Chưa thanh toán; thêm một dòng `LichSuTrangThaiDon`; lưu lần đồng ý xử lý dữ liệu (mục đích, thời điểm); giỏ hàng trống. Đơn hiện đầu danh sách chờ xác nhận của UC-DH-01 |
| Màn hình | `giao-dien/khach/gio-hang.html` → `giao-dien/khach/dat-hang.html` → `giao-dien/khach/hoan-tat.html`. Trang `dat-hang.html` chưa có ô mã giảm giá, ô chọn lắp đặt, ô đồng ý xử lý dữ liệu và dòng tiền cọc: bổ sung khi thiết kế ở tuần 14 |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Bấm "Tiến hành đặt hàng" ở trang giỏ hàng | Kiểm tra từng mẫu máy trong giỏ còn bán, còn hàng; hiện trang đặt hàng kèm tóm tắt giỏ. Khách đã đăng nhập thì điền sẵn họ tên, số điện thoại và địa chỉ mặc định trong sổ địa chỉ |
| 2 | Nhập họ tên, số điện thoại, tỉnh hoặc thành phố, quận hoặc huyện, địa chỉ cụ thể; email và ghi chú nếu muốn | Kiểm tra các ô bắt buộc không để trống, số điện thoại đủ 10 chữ số |
| 3 | Chọn có lắp đặt hay không với từng mẫu máy cần lắp | Tính phí giao và phí lắp theo địa chỉ và giá trị đơn, hiện ngay trên tóm tắt đơn |
| 4 | Chọn cách thanh toán: tiền mặt khi nhận hàng, chuyển khoản hoặc QR, thẻ, trả góp qua công ty tài chính | Nếu đơn thuộc diện phải cọc thì hiện số tiền cọc phải trả trực tuyến |
| 5 | Bấm "Xem lại đơn" | Hiện màn hình xác nhận: mẫu máy, số lượng, đơn giá, thời hạn giao dự kiến, khuyến mãi, tiền hàng, phí giao, phí lắp, tiền cọc, tổng tiền, cách thanh toán; ô đồng ý điều kiện giao dịch, ô đồng ý dùng dữ liệu để giao hàng và bảo hành, ô đồng ý nhận tin khuyến mãi, cả ba ô **không tích sẵn** |
| 6 | Tích ô đồng ý điều kiện giao dịch và ô đồng ý dùng dữ liệu để giao hàng, bảo hành; bấm "Đặt hàng" | Kiểm tra hai ô bắt buộc đã tích; kiểm tra lại giá và tồn kho |
| 7 | | Tạo đơn hàng với mã đơn mới, lưu giá lúc đặt, trạng thái Chờ xác nhận; lưu lần đồng ý; ghi lịch sử trạng thái; làm trống giỏ |
| 8 | | Hiện trang "Đặt hàng thành công": mã đơn, tóm tắt đơn, lời nhắn nhân viên sẽ gọi xác nhận trong giờ làm việc, hướng dẫn tra đơn bằng số điện thoại và mã đơn. Đơn có tiền cọc hoặc chọn trả trực tuyến thì hiện thêm nút "Thanh toán ngay" dẫn sang UC-MH-05 |

**Luồng thay thế:**
- `1a.` Có mẫu máy đã ngừng bán hoặc hết hàng: báo tên mẫu máy đó, cho khách bỏ khỏi đơn hoặc quay lại giỏ hàng.
- `2a.` Để trống ô bắt buộc hoặc số điện thoại sai dạng: báo ngay dưới ô đó, không cho đi tiếp.
- `2b.` Khách hàng đã đăng nhập chọn địa chỉ khác trong sổ địa chỉ hoặc nhập địa chỉ mới; địa chỉ mới chỉ lưu vào sổ khi khách tích "Lưu địa chỉ này".
- `3a.` Địa chỉ ngoài nội thành: báo đơn gửi đơn vị vận chuyển và cửa hàng không lắp đặt, ẩn ô chọn lắp đặt (QĐ4).
- `4a.` Khách nhập mã giảm giá: thực hiện UC-MH-08 Áp mã giảm giá, tính lại tổng tiền rồi quay lại bước 4. Đơn đã có một mã thì mã mới thay mã cũ, không cộng hai mã (QĐ2).
- `5a.` Khách bấm "Sửa": quay lại bước 2 (thông tin nhận hàng) hoặc trang giỏ hàng (số lượng), giữ nguyên dữ liệu đã nhập.
- `6a.` Chưa tích ô đồng ý điều kiện giao dịch hoặc ô đồng ý dùng dữ liệu để giao hàng: báo cần đồng ý mới đặt được. Ô nhận tin khuyến mãi không bắt buộc.
- `6b.` Giá hoặc tồn kho đã đổi từ lúc khách mở trang: báo mẫu máy và giá mới, quay lại bước 5 để khách xem lại.
- `8a.` Khách không trả cọc trực tuyến: đơn vẫn ở trạng thái Chờ xác nhận, ghi "chưa cọc"; nhân viên bán hàng xử lý khi gọi xác nhận (UC-DH-01).

**Ngoại lệ:** mất kết nối hoặc lỗi lưu khi bấm "Đặt hàng" thì không tạo đơn dở dang, giỏ hàng giữ nguyên, báo "Chưa đặt được đơn, vui lòng thử lại"; khách bấm "Đặt hàng" hai lần liên tiếp thì chỉ tạo một đơn.

**Quy tắc nghiệp vụ:** QĐ2 (một mã giảm giá và một quà tặng), QĐ3 (giá đã gồm VAT, phí ghi riêng), QĐ4, QĐ5 (giao và lắp nội thành, đơn tỉnh), QĐ7 (cọc 10–20% đơn có tiền hàng trên 10 triệu đồng; trên website mức đơn và tỷ lệ cọc do chủ cửa hàng đặt) của BUC-02;
`YC-MH-01` (đặt không cần tài khoản), `YC-MH-03` (màn hình xác nhận đủ thông tin, khách sửa được rồi đồng ý điều kiện giao dịch), `YC-MH-04` (ô đồng ý không tích sẵn, tách mục đích), `YC-MH-06` (cọc cho đơn giá trị cao và đơn giao tỉnh), `YC-DH-03` (hiện phí giao trước khi đặt), `YC-SP-08` (đổi giá không làm đổi giá trên đơn đã đặt).

## UC-MH-05 Thanh toán trực tuyến

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-MH-05 Thanh toán trực tuyến |
| Gói | G3 |
| Tác nhân chính | Khách vãng lai; Khách hàng (kế thừa Khách vãng lai) |
| Tác nhân phụ | Cổng thanh toán (hệ thống bên ngoài nhận tiền và báo kết quả) |
| Mô tả | Khách trả tiền cọc hoặc toàn bộ tiền đơn qua cổng thanh toán (chuyển khoản hoặc QR, thẻ) để giữ đơn, hệ thống ghi số tiền đã trả vào đơn theo kết quả cổng gửi về |
| Điều kiện trước | Đơn đã tạo ở UC-MH-04, đang ở trạng thái Chờ xác nhận hoặc Đã xác nhận và còn số tiền phải trả trực tuyến (tiền cọc chưa trả hoặc khách chọn trả trước); khách mở từ trang Đặt hàng thành công hoặc từ kết quả tra đơn (UC-MH-06) |
| Điều kiện sau | Thành công: có một giao dịch thanh toán gắn với đơn (mã giao dịch của cổng, số tiền, loại tiền cọc hay trả đủ, thời điểm); số tiền đã trả của đơn tăng; trả đủ thì `trangThaiThanhToan` là Đã thanh toán, mới trả cọc thì đơn ghi tiền cọc đã nhận. Không thành công: đơn và trạng thái thanh toán giữ nguyên |
| Màn hình | Chưa có, thiết kế ở tuần 14 (trang chọn cách trả và trang kết quả; trang quét QR, nhập thẻ do cổng thanh toán cung cấp) |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Bấm "Thanh toán ngay" | Hiện mã đơn, số tiền phải trả (tiền cọc hoặc phần còn lại), hai cách trả: chuyển khoản hoặc QR, thẻ |
| 2 | Chọn cách trả, bấm "Thanh toán" | Tạo yêu cầu thanh toán (mã đơn, số tiền, hạn hiệu lực), gửi sang Cổng thanh toán và chuyển khách sang trang của cổng |
| 3 | Quét QR hoặc nhập thẻ trên trang của Cổng thanh toán, xác nhận trả tiền | Chờ kết quả từ Cổng thanh toán |
| 4 | Cổng thanh toán gửi kết quả giao dịch về hệ thống | Kiểm tra kết quả đúng do cổng gửi, đúng mã đơn và số tiền; ghi giao dịch, cộng số tiền đã trả vào đơn, cập nhật trạng thái thanh toán |
| 5 | | Hiện trang kết quả: mã giao dịch, số tiền đã trả, số tiền còn phải trả khi nhận hàng; gửi email xác nhận nếu đơn có email |

**Luồng thay thế:**
- `1a.` Đơn đã ở trạng thái Đã huỷ hoặc đã trả đủ: báo "Đơn không cần thanh toán", không cho đi tiếp.
- `1b.` Đơn phải cọc nhưng khách muốn trả hết: khách chọn "Trả toàn bộ", số tiền phải trả đổi thành phần còn lại của đơn.
- `3a.` Khách bấm huỷ trên trang cổng hoặc giao dịch bị từ chối: cổng báo không thành công, hệ thống giữ nguyên đơn, báo khách và cho thử lại từ bước 1; đơn không thuộc diện phải cọc thì khách được đổi sang trả tiền khi nhận hàng.
- `3b.` Hết hạn hiệu lực của yêu cầu mà chưa có kết quả: hệ thống huỷ yêu cầu, đơn giữ nguyên; khách thanh toán lại từ trang tra đơn (UC-MH-06).
- `4a.` Kết quả có số tiền khác số tiền yêu cầu hoặc không xác minh được do cổng gửi: không cộng vào đơn, lưu lại để kế toán đối chiếu.

**Ngoại lệ:** khách mất kết nối sau khi đã trả trên trang cổng thì kết quả cổng gửi về vẫn được ghi, khách xem lại ở UC-MH-06; cổng gửi lại cùng một kết quả nhiều lần thì chỉ ghi một lần theo mã giao dịch.

**Quy tắc nghiệp vụ:** QĐ6 (các cách trả tiền), QĐ7 (cọc), QĐ8, QĐ9 (hoàn cọc khi huỷ, hoàn theo cách khách đã trả) của BUC-02; `YC-MH-05` (tiền mặt khi nhận, chuyển khoản hoặc QR, thẻ, trả góp qua công ty tài chính),
`YC-MH-06` (cọc trực tuyến, mức cọc do chủ đặt, ghi số tiền cọc trên đơn), `YC-PCN-06` (phí cổng thanh toán tính riêng theo giao dịch). Website không lưu số thẻ của khách, chỉ lưu mã giao dịch do cổng trả về (nhóm đặt, cùng hướng `YC-PCN-02`).

## UC-MH-06 Theo dõi đơn hàng

| Mục | Nội dung |
|---|---|
| Mã, tên | UC-MH-06 Theo dõi đơn hàng |
| Gói | G3 |
| Tác nhân chính | Khách vãng lai (tra bằng số điện thoại kèm mã đơn); Khách hàng (kế thừa Khách vãng lai, xem thêm danh sách đơn của tài khoản) |
| Tác nhân phụ | Không có |
| Mô tả | Khách nhập số điện thoại đã dùng khi đặt và mã đơn để biết đơn đang ở trạng thái nào, ngày giờ hẹn giao lắp, tiền đã trả và còn phải trả, chứng từ của đơn, không phải nhắn hỏi nhân viên |
| Điều kiện trước | Đơn đã được tạo (UC-MH-04). Không bắt buộc đăng nhập |
| Điều kiện sau | Không dữ liệu đơn nào bị sửa; hệ thống chỉ đếm số lần tra sai để chống dò mã đơn |
| Màn hình | Chưa có, thiết kế ở tuần 14 (trang tra cứu đơn đặt link ở chân trang và trang chi tiết đơn) |

**Luồng sự kiện chính**

| Bước | Tác nhân | Hệ thống |
|---|---|---|
| 1 | Mở "Tra cứu đơn hàng" ở chân trang hoặc từ trang Đặt hàng thành công | Hiện ô số điện thoại và ô mã đơn |
| 2 | Nhập số điện thoại, mã đơn, bấm "Tra cứu" | Kiểm tra hai ô không để trống, đúng dạng |
| 3 | | Tìm đơn có mã đơn trùng và số điện thoại người đặt trùng |
| 4 | | Hiện chi tiết đơn: mã đơn, ngày đặt, mẫu máy, số lượng, tổng tiền, tiền đã trả, tiền còn phải trả, cách thanh toán; trạng thái đơn (Chờ xác nhận, Đã xác nhận, Đang giao, Hoàn thành, Giao không thành công, Đã huỷ, Đã trả hàng, nhãn màu theo phần chung) kèm các mốc đổi trạng thái; ngày và khung giờ hẹn giao lắp nếu đã hẹn; đơn vị vận chuyển và mã vận đơn với đơn đi tỉnh; số điện thoại cửa hàng. Địa chỉ và số điện thoại người nhận hiện che bớt một phần |
| 5 | Bấm "Xem hoá đơn" | Hiện số, ký hiệu hoá đơn và đường dẫn tra cứu hoá đơn điện tử; cho tải, in |

**Luồng thay thế:**
- `1a.` Khách hàng đã đăng nhập mở "Đơn hàng của tôi": hệ thống liệt kê các đơn gắn tài khoản, mới nhất trước; khách chọn một đơn rồi đi tiếp bước 4, không cần nhập số điện thoại và mã đơn.
- `2a.` Để trống hoặc sai dạng: báo ngay dưới ô đó.
- `3a.` Không có đơn khớp: báo chung "Không tìm thấy đơn với số điện thoại và mã đơn này", không nói rõ sai ô nào.
- `3b.` Tra sai 5 lần liên tiếp: tạm khoá tra cứu 15 phút (cùng mức của UC-HT-01).
- `4a.` Đơn còn tiền cọc hoặc tiền trả trước chưa trả: hiện nút "Thanh toán ngay" (UC-MH-05).
- `4b.` Đơn đang Chờ xác nhận: hiện nút "Huỷ đơn" (UC-MH-07). Đơn Đã xác nhận: hiện số điện thoại cửa hàng để khách gọi huỷ, nhân viên bán hàng huỷ ở UC-DH-01 nếu máy chưa xuất kho.
- `4c.` Đơn Đã huỷ hoặc Giao không thành công: hiện lý do và số tiền đã hoàn (trạng thái thanh toán Đã hoàn tiền).
- `5a.` Đơn chưa có hoá đơn: không hiện nút, ghi "Hoá đơn lập khi giao hàng" (QĐ11).

**Ngoại lệ:** lỗi truy vấn hoặc mất kết nối thì báo "Chưa tra được, vui lòng thử lại", không hiện dữ liệu dở dang.

**Quy tắc nghiệp vụ:** `YC-MH-08` (tra bằng số điện thoại kèm mã đơn, thấy trạng thái và ngày giờ hẹn giao lắp), `YC-MH-09` (huỷ khi chưa xuất kho), `YC-MH-10` (xem, tải, in hoá đơn và chứng từ),
`YC-PCN-02` (chỉ vai được giao mới xem đủ số điện thoại, địa chỉ khách, nên trang tra cứu che bớt), `YC-PCN-04` (hoá đơn lấy từ phần mềm hoá đơn điện tử, website chỉ lưu số, ký hiệu, đường dẫn tra cứu); QĐ11 của BUC-02.

## Điểm cần cập nhật ở phần chung

| Tệp chung | Đề xuất của G3 | Lý do |
|---|---|---|
| `danh-muc-use-case.md` mục 2 | UC-MH-04, 05, 06, 07 đổi tác nhân thành **Khách vãng lai** (Khách hàng kế thừa); UC-MH-08 không nối tác nhân riêng | Quyết định 14/10 cho khách vãng lai đặt hàng; đặt được thì phải trả cọc, tra đơn, huỷ đơn được |
| `lop-va-trang-thai-chung.md` mục 2 | `DonHang` thêm `hoTenNguoiDat`, `soDienThoaiNguoiDat` (bắt buộc khi đơn không gắn `KhachHang`), `tienCoc`, `/soTienDaTra`; `trangThaiThanhToan` thêm giá trị **Đã đặt cọc** | UC-MH-06 tra theo số điện thoại người đặt; UC-MH-05 ghi tiền cọc (`YC-MH-06`) |
| `lop-va-trang-thai-chung.md` mục 2 | Lớp mới `GiaoDichThanhToan` (maGiaoDich, maGiaoDichCong, soTien, loai: Tiền cọc, Trả đủ, Hoàn tiền, phuongThuc, thoiDiem, ketQua), `DonHang` 1 – * `GiaoDichThanhToan` | Một đơn có thể trả cọc rồi trả nốt, hoặc hoàn tiền; chống ghi trùng theo mã giao dịch của cổng |
| `lop-va-trang-thai-chung.md` mục 2 | Lớp `LanDongY` (mucDich, dongY, thoiDiem) gắn với `DonHang` hoặc `KhachHang` | `YC-MH-04`: lưu lần đồng ý theo từng mục đích |
| `lop-va-trang-thai-chung.md` (`HoaDon`) | Không tách lớp `HoaDon`; `DonHang` thêm `soHoaDon`, `kyHieuHoaDon`, `duongDanTraCuuHoaDon` | `YC-PCN-04`: website không tự phát hành hoá đơn, chỉ lưu số, ký hiệu và đường dẫn |
