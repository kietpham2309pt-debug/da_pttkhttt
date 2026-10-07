# Bảng hỏi khách hàng (Google Form)

Phiên bản 1.1 (07/10/2026) · dùng cho việc **t82** (tuần 8) · mục tiêu **ít nhất 30 phiếu** · làm trong khoảng 4–6 phút

Bản 1.1 đã sửa theo đợt điền thử với 10 vai khách hàng mô phỏng, xem [`ket-qua-thu-nghiem.md`](ket-qua-thu-nghiem.md).

**Tạo form tự động:** mở [script.google.com](https://script.google.com) bằng tài khoản Google của nhóm, bấm **Dự án mới**,
xoá hết chữ có sẵn, dán toàn bộ nội dung [`tao-google-form.gs`](tao-google-form.gs), bấm **Lưu**, chọn hàm `taoBangHoi`, bấm **Chạy**,
cấp quyền khi được hỏi. Xong thì mở **Nhật ký thực thi**: có link điền form, link sửa form và link bảng tính nhận kết quả.
Form có sẵn phần rẽ nhánh (câu K7 chỉ hiện với người ít hoặc chưa mua online).

## Tiêu đề và lời mở đầu

**Khảo sát thói quen mua đồ điện gia dụng**

> Chào bạn, tụi mình là sinh viên ngành Công nghệ thông tin, Trường Đại học Công Thương TP.HCM, đang làm bài tập môn Phân tích thiết kế hệ thống thông tin với đề tài website bán hàng điện gia dụng. Khảo sát mất khoảng 4 đến 6 phút, không hỏi tên, số điện thoại hay email. Câu trả lời chỉ dùng cho bài học. Cảm ơn bạn nhiều.

## Mục 1. Về bạn

**K1 ★ Bạn bao nhiêu tuổi?** · một lựa chọn · G1
- Dưới 22 · 22 đến 30 · 31 đến 45 · 46 đến 60 · Trên 60

**K2 ★ Bạn đang sống ở đâu?** · một lựa chọn · G4
- Gợi ý dưới câu: *Thủ Đức tính là nội thành cũ.*
- TP.HCM, khu vực nội thành cũ · TP.HCM, khu vực ngoại thành cũ (Củ Chi, Hóc Môn, Bình Chánh, Nhà Bè, Cần Giờ) · TP.HCM, khu vực Bình Dương hoặc Bà Rịa - Vũng Tàu cũ · Tỉnh, thành khác

**K3 ★ Bạn thường mua đồ điện gia dụng để dùng ở đâu?** · một lựa chọn · G3
- Trong gia đình · Cho quán, cửa hàng hoặc văn phòng · Cả hai

**K4 ★ Trong 12 tháng qua, bạn hoặc người nhà đã mua xong bao nhiêu món đồ điện gia dụng (bếp, nồi cơm, máy hút mùi, lò vi sóng, quạt, máy lọc nước…)?** · một lựa chọn · G1
- Chưa mua món nào · 1 món · 2 đến 3 món · Từ 4 món trở lên

## Mục 2. Thói quen mua

**K5 ★ Lần gần nhất mua đồ điện gia dụng, bạn mua ở đâu?** · một lựa chọn · G3
- Cửa hàng điện gia dụng gần nhà · Siêu thị điện máy (Điện Máy Xanh, Nguyễn Kim, Chợ Lớn…) · Sàn thương mại điện tử (Shopee, Lazada, Tiki, TikTok Shop) · Xem livestream rồi đặt (TikTok, Facebook) · Website của hãng hoặc của cửa hàng · Nhắn Facebook, Zalo cho cửa hàng · Người nhà mua giùm · Chưa mua bao giờ · Khác

**K6 ★ Bạn đã từng tự đặt mua đồ điện gia dụng trên mạng (sàn, website, livestream, Facebook, Zalo) chưa?** · một lựa chọn · G3 · **rẽ nhánh**
- Đã từng, nhiều lần → sang Mục 3
- Đã từng, một hai lần → sang Mục 2b
- Chưa từng → sang Mục 2b

## Mục 2b. Vì sao bạn ít mua online (chỉ hiện với người chọn "một hai lần" hoặc "chưa từng")

**K7 Điều gì làm bạn ngại mua đồ điện gia dụng trên mạng?** · nhiều lựa chọn · không bắt buộc · G3, G4
- Sợ hàng giả, hàng không đúng mô tả · Muốn xem và sờ tận tay · Lo giao hàng làm hỏng máy · Lo không ai lắp đặt · Lo bảo hành khó · Phí giao hàng cao · Không rành đặt hàng trên mạng · Khác

## Mục 3. Chọn sản phẩm

**K8 ★ Khi chọn mua một món đồ điện gia dụng, các điều sau quan trọng với bạn ở mức nào?** · lưới · G2, G4, G5
- Gợi ý dưới câu: *1 = không quan trọng, 5 = rất quan trọng.*
- Hàng: Giá tốt · Hàng chính hãng, thương hiệu quen · Bảo hành dài · Được lắp đặt tận nơi · Giao hàng nhanh, đúng hẹn · Có đánh giá của người đã mua · So sánh được thông số (công suất, kích thước) giữa các mẫu
- Cột: 1 · 2 · 3 · 4 · 5

**K9 Trên một trang web bán hàng, bạn hay tìm sản phẩm bằng cách nào?** · nhiều lựa chọn · G2
- Gõ tên vào ô tìm kiếm · Bấm vào danh mục (bếp, máy hút mùi…) · Lọc theo khoảng giá · Lọc theo thương hiệu · Xem mục khuyến mãi · Hỏi nhân viên qua chat · Tôi chưa dùng web bán hàng

## Mục 4. Đặt hàng và thanh toán

**K10 ★ Khi mua online, bạn muốn trả tiền bằng cách nào?** · nhiều lựa chọn · G3
- Trả tiền mặt khi nhận hàng · Chuyển khoản hoặc quét mã QR · Ví điện tử (MoMo, ZaloPay, VNPay…) · Thẻ ngân hàng, thẻ tín dụng · Trả góp

**K11 ★ Bạn có muốn tạo tài khoản trên website để đặt hàng không?** · một lựa chọn · G3
- Có, để theo dõi đơn và lưu địa chỉ · Đăng nhập nhanh bằng Google hoặc Zalo thì được · Không, chỉ muốn nhập số điện thoại rồi đặt · Không biết, chưa đặt online bao giờ

**K12 Khi mua, bạn có cần những điều sau không?** · nhiều lựa chọn · không bắt buộc · G2, G3, G4
- Xem trước phí giao hàng trước khi đặt · Mua nhiều món một lần với giá ưu đãi · Giao và lắp tất cả các món trong một lần · Được chỉ cách dùng sau khi lắp · Xuất hoá đơn VAT · Không cần điều nào ở trên

## Mục 5. Giao hàng, lắp đặt, bảo hành

**K13 ★ Với đồ lớn cần lắp (bếp âm, máy hút mùi, máy rửa chén), bạn muốn được hẹn lịch lắp đặt thế nào?** · một lựa chọn · G4
- Giao và lắp luôn trong cùng một lần · Tự chọn ngày giờ lắp trên website · Nhân viên gọi điện hoặc nhắn Zalo hẹn · Tự lắp hoặc nhờ thợ quen · Ở xa, cần hướng dẫn hoặc video để tự lắp

**K14 Bạn đã từng gặp rắc rối nào sau khi mua đồ điện gia dụng chưa?** · nhiều lựa chọn · không bắt buộc · G4, G5
- Chưa từng · Đổi trả bị làm khó · Bảo hành lâu, bị đẩy qua lại giữa cửa hàng và hãng · Giao trễ hẹn · Lắp đặt ẩu · Khác

**K15 ★ Khi máy cần bảo hành, bạn muốn tra cứu thời hạn bảo hành bằng gì?** · nhiều lựa chọn · G5
- Số điện thoại lúc mua · Số serial (dãy số in trên tem dán sau máy) · Mã đơn hàng hoặc hoá đơn · Giữ phiếu bảo hành giấy là đủ

## Mục 6. Góp ý

**K16 Điều làm bạn khó chịu nhất khi mua đồ điện gia dụng là gì?** · đoạn văn ngắn · không bắt buộc · cả nhóm

**K17 Một website bán đồ điện gia dụng nên có tính năng gì để bạn muốn mua ở đó?** · đoạn văn ngắn · không bắt buộc · cả nhóm

## Câu nào dùng cho gói nào

| Gói | Câu | Dùng để |
|---|---|---|
| G1 | K1, K4 | Mô tả người trả lời trong mục khảo sát |
| G2 | K8, K9, K12 | Bộ lọc, tìm kiếm, so sánh thông số, mua nhiều món giá ưu đãi |
| G3 | K3, K5, K6, K7, K10, K11, K12 | Kênh mua, cách thanh toán, có cần tài khoản không, hoá đơn VAT |
| G4 | K2, K7, K8, K12, K13, K14 | Giao hàng, lắp đặt, hướng dẫn sử dụng, đổi trả |
| G5 | K8, K14, K15 | Tra cứu bảo hành theo serial, theo số điện thoại |
| Cả nhóm | K16, K17 | Yêu cầu chức năng, câu trích dẫn trong báo cáo |

## Phát phiếu

- Mỗi người gửi link vào ít nhất 2 nhóm (lớp, gia đình, khu trọ, nhóm Zalo người quen). 5 người × 6 phiếu là đủ 30.
- Ưu tiên người **đã từng mua đồ điện gia dụng**. Nhóm toàn sinh viên thì kết quả lệch, nên nhờ cả cha mẹ, cô chú trả lời.
- Người lớn tuổi điền mất 6–10 phút: nên ngồi đọc giúp, nhưng để họ tự chọn.
- Đóng form sau 7 ngày, kết quả có sẵn trong bảng tính đi kèm, nhóm trưởng vẽ biểu đồ cho mục khảo sát.
