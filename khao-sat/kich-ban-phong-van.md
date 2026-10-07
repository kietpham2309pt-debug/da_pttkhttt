# Kịch bản phỏng vấn cửa hàng điện gia dụng

Phiên bản 1.1 (07/10/2026) · dùng cho việc **t81–t83** (tuần 8) · nhóm 4 · Phân tích thiết kế hệ thống thông tin

Bản 1.1 đã sửa theo đợt phỏng vấn thử với 3 vai chủ cửa hàng mô phỏng, xem [`ket-qua-thu-nghiem.md`](ket-qua-thu-nghiem.md).

## Trước buổi phỏng vấn

| Mục | Nội dung |
|---|---|
| Thời lượng | Hỏi hết mất 55–65 phút. Chỉ hỏi 15 câu ★ thì khoảng 40 phút. Hết giờ thì dừng, phần còn lại hỏi qua Zalo |
| Người đi | Ít nhất 3 người: 1 người dẫn, 1 người ghi chép, 1 người chụp biểu mẫu và **bấm giờ** (nhắc chuyển phần khi người được hỏi kể lan man) |
| Ai hỏi phần nào | Phần A, F: G1 · Phần B: G2 · Phần C: G3 · Phần D: G4 · Phần E: G5. Gói vắng mặt thì người dẫn hỏi thay |
| Mang theo | Bản in kịch bản này, điện thoại sạc đầy để ghi âm, bộ giao diện chung mở sẵn (`giao-dien/index.html`) để hỏi "nếu website trông như vầy thì…" |
| Xin phép | Ghi âm; chụp biểu mẫu. Hứa **che tên, số điện thoại khách và giá nhập**. Nếu ngại, xin chụp **tờ trống** |

**Sáu điều khi hỏi**
1. Hỏi về **lần gần nhất** ("Lần gần nhất có khách đặt qua Zalo, anh chị làm những bước nào?") thay vì "thường thì…".
2. Không gợi ý câu trả lời. Hỏi "Khi máy về kho thì ai kiểm?", đừng hỏi "Thủ kho kiểm đúng không ạ?".
3. Nghe thấy tên giấy tờ (phiếu, sổ, hoá đơn, file) thì **xin xem và chụp ngay**.
4. Câu chạm tới tiền (doanh thu, giá nhập, nợ, thất thoát): hỏi **ai làm, theo bước nào**, đừng hỏi **bao nhiêu**. Họ từ chối thì bỏ qua, không hỏi lại.
5. Không dùng từ chuyên môn trần trụi. Nói "dãy số in trên tem dán sau máy (số serial)", "thu tiền khi giao hàng" thay vì "COD".
6. Không hứa làm website thật cho cửa hàng. Đây là bài tập môn học.

**Người trả lời là quản lý, không phải chủ:** hỏi thêm "việc nào phải chờ chủ duyệt" ở mỗi phần (đó chính là ranh giới phân quyền).
Câu về chi phí (F4) thì nhờ chuyển cho chủ.

## Lời mở đầu (đọc gần nguyên văn)

> Dạ, tụi em là sinh viên năm 3 ngành Công nghệ thông tin, Trường Đại học Công Thương TP.HCM. Tụi em đang làm bài tập môn Phân tích thiết kế hệ thống thông tin, đề tài là website bán hàng cho cửa hàng điện gia dụng. Thầy cô yêu cầu tìm hiểu cách một cửa hàng thật đang làm việc, nên tụi em xin anh chị khoảng 40 phút để hỏi về cách cửa hàng bán hàng, nhập hàng, giao hàng và bảo hành. Thông tin chỉ dùng cho bài học, không đưa tên cửa hàng ra ngoài nếu anh chị không muốn. Giấy tờ nào anh chị cho chụp thì tụi em che tên, số điện thoại của khách và giá nhập; nếu được, anh chị cho tụi em chụp tờ còn trống cũng được ạ. Tụi em xin phép ghi âm để khỏi ghi sót, được không ạ?

---

## Phần A. Tổng quan cửa hàng · G1 · khoảng 7 phút

**A1 ★** Cửa hàng mở được bao lâu rồi, bán những nhóm hàng nào, nhóm nào bán chạy nhất?
- Dùng cho: mục 1.1 giới thiệu cửa hàng; danh mục sản phẩm của G2.

**A2 ★** Cửa hàng có bao nhiêu người, mỗi người lo việc gì? Có ai kiêm nhiều việc không?
- Hỏi thêm: ai bán hàng, ai giữ kho, ai đi giao và lắp, ai giữ sổ sách tiền bạc.
- Dùng cho: sơ đồ cơ cấu tổ chức; danh sách nhân viên nghiệp vụ (business worker) và tác nhân hệ thống.

**A3 ★** Hiện cửa hàng ghi chép bán hàng, nhập hàng bằng gì: sổ tay, Excel, phần mềm bán hàng, hay nhắn Zalo với nhau?
- Hỏi thêm (câu riêng, đừng gộp): Trong đó ai được xem, ai được sửa? Có ai sửa được giá bán không?
- Dùng cho: hiện trạng tin học hoá; chiến lược phân tích (mục 2.2); phân quyền UC-HT-03.

**A4** Cuối ngày hoặc cuối tháng, anh chị xem những gì để biết cửa hàng làm ăn ra sao? Ai làm ra các con số đó, mất bao lâu?
- Dùng cho: quy trình QT-01 Lập báo cáo kinh doanh; UC-HT-04, UC-HT-05.

**A5** Khi có sai sót như ghi nhầm, xuất nhầm máy, làm sao anh chị biết ai làm và sửa lại thế nào?
- Đừng dùng chữ "thất thoát". Họ ngập ngừng thì chuyển câu.
- Dùng cho: yêu cầu lưu vết thao tác, phân quyền.

## Phần B. Sản phẩm, giá, khuyến mãi · G2 · khoảng 7 phút

**B1 ★** Khi có một mẫu máy mới về, cửa hàng làm những bước nào để bắt đầu bán được mẫu đó?
- Hỏi thêm: ai đặt giá bán, có phải chờ chủ duyệt không, giá ghi ở đâu (tem giá, báo giá, bài đăng).
- Dùng cho: quy trình QT-02; sơ đồ hoạt động của G2.

**B2 ★** Với mỗi sản phẩm, khách hay hỏi những thông tin gì? Anh chị lấy thông số kỹ thuật ở đâu?
- Hỏi thêm: với bếp âm, máy hút mùi có cần kích thước khoét đá, kích thước tủ không.
- Dùng cho: thuộc tính lớp `SanPham`, `ThongSoKyThuat`; màn hình chi tiết sản phẩm.

**B3** Giá bán thay đổi khi nào? Bán theo giá hãng niêm yết hay tự quyết? (Bỏ qua phần đã nói ở B1.)
- Dùng cho: UC-SP-03 Cập nhật giá bán; quy tắc nghiệp vụ.

**B4** Cửa hàng hay chạy khuyến mãi kiểu nào: giảm giá, tặng quà, giảm theo hãng, mã giảm giá, giảm khi mua nguyên bộ? Ai quyết định và ghi lại ở đâu?
- Hỏi thêm: một đơn có được hưởng hai khuyến mãi cùng lúc không; đã từng áp nhầm khuyến mãi chưa.
- Dùng cho: lớp `KhuyenMai`, `MaGiamGia`; UC-SP-04.

**B5** Hàng trưng bày, hàng móp hộp có bán riêng giá khác không? Bảo hành có khác không?
- Dùng cho: trạng thái sản phẩm; thuộc tính bổ sung.

**B6** Nếu cửa hàng bán ở nhiều nơi (tại tiệm, Facebook, sàn thương mại điện tử), giá và số hàng còn lại ở các nơi có khớp nhau không? Ai cập nhật?
- Dùng cho: yêu cầu đồng bộ tồn kho và giá; rủi ro khi thêm website.

## Phần C. Bán hàng, đặt hàng, thanh toán · G3 · khoảng 8 phút

**C1 ★** Khách mua hàng qua những đường nào: tới cửa hàng, gọi điện, nhắn Zalo hay Facebook, livestream, sàn thương mại điện tử, qua kiến trúc sư hay công ty nội thất giới thiệu? Đường nào nhiều nhất?
- Dùng cho: tác nhân và kênh bán; phạm vi website.

**C2 ★** Kể giúp em lần gần nhất có khách đặt hàng qua điện thoại hoặc tin nhắn: từ lúc khách nhắn tới lúc chốt đơn, anh chị làm những bước nào, ghi lại những thông tin gì của khách? Có tin nhắn mẫu hay phiếu nào không?
- Dùng cho: quy trình QT-03; thuộc tính `KhachHang`, `DonHang`, `DiaChiGiaoHang`.

**C3 ★** Khách trả tiền bằng cách nào? Có cho đặt cọc, trả góp, xuất hoá đơn VAT không? Đơn lớn có trả nhiều đợt không?
- Dùng cho: lớp `ThanhToan`; UC-MH-05; luồng thay thế.

**C4** Khách huỷ đơn khi đã chốt thì xử lý ra sao? Tiền cọc tính thế nào?
- Dùng cho: UC-MH-07 Huỷ đơn; quy tắc nghiệp vụ.

**C5** Có lưu thông tin khách quen để lần sau chăm sóc không? Lưu ở đâu?
- Dùng cho: UC-MH-01, UC-MH-02; có cần tài khoản khách hàng không.

**C6** Có khi nào khách đặt rồi tới lúc giao lại không nhận không? Lúc đó máy trả về xử lý ra sao, cửa hàng có cách gì để phòng (gọi xác nhận, xin cọc)?
- Dùng cho: luồng thay thế của QT-03, QT-04; quy tắc đặt cọc.

## Phần D. Xử lý đơn, giao hàng, lắp đặt, đổi trả · G4 · khoảng 9 phút

**D1 ★** Sau khi chốt đơn, ai xác nhận lại với khách, ai lấy hàng trong kho, ai đi giao? Có giấy tờ gì đi kèm chiếc máy khi giao?
- Hỏi thêm: xin chụp phiếu giao hàng hoặc phiếu xuất kho.
- Dùng cho: quy trình QT-04; lớp `PhieuGiaoHang`.

**D2 ★** Những máy nào phải lắp đặt tận nơi? Trước khi lắp có đi đo đạc không (kích thước tủ, chỗ khoét đá, ổ điện, đường nước)? Lắp có tính phí không, xong có ký biên bản không?
- Hỏi thêm: xin chụp phiếu khảo sát hoặc biên bản lắp đặt (tờ trống cũng được).
- Dùng cho: UC-DH-02, UC-DH-05; lớp `PhieuLapDat`.

**D3** Cửa hàng tự giao hay thuê đơn vị vận chuyển? Giao tỉnh xa thì ai lắp?
- Dùng cho: tác nhân Đơn vị vận chuyển; phạm vi.

**D4 ★** Lần gần nhất khách muốn đổi hoặc trả máy, chuyện diễn ra thế nào? Trong bao nhiêu ngày thì được đổi, ai quyết định cho đổi, có ghi lại ở đâu không?
- Dùng cho: UC-DH-06; lớp `YeuCauDoiTra`, `HoanTien`; quy tắc đổi trả.

**D5** Khi nhân viên thu tiền lúc giao hàng, tiền đó nộp lại cho cửa hàng thế nào, ai kiểm lại?
- Dùng cho: UC-DH-04 Xác nhận thu tiền khi giao.

**D6** Lịch đi giao và lắp của thợ do ai xếp, ghi ở đâu? Có khi nào trùng lịch hoặc trễ hẹn với khách chưa, lúc đó xử lý ra sao?
- Dùng cho: UC-DH-02 Phân công giao hàng, lắp đặt; yêu cầu chọn khung giờ trống.

## Phần E. Nhập hàng, kho, bảo hành · G5 · khoảng 8 phút

**E1 ★** Khi nào thì cửa hàng quyết định nhập thêm hàng? Đặt hàng với nhà cung cấp qua đâu, ai duyệt?
- Hỏi thêm: có mức tồn tối thiểu không; một mẫu máy lấy từ một hay nhiều nhà cung cấp. Không hỏi giá nhập, không hỏi số nợ.
- Dùng cho: quy trình QT-05; lớp `NhaCungCap`, `DonDatHangNCC`.

**E2 ★** Lúc hàng về, ai kiểm, kiểm những gì, ghi vào đâu? Có ghi lại dãy số in trên tem dán sau máy (số serial) không, ghi lúc nhập hay lúc bán?
- Hỏi thêm: xin chụp phiếu nhập kho hoặc phiếu giao của nhà cung cấp (che giá).
- Dùng cho: lớp `PhieuNhap`, `ChiTietPhieuNhap`, `SanPhamSerial`.

**E3** Bao lâu kiểm lại số hàng trong kho một lần? Số trên sổ và số thật lệch nhau thì xử lý ra sao?
- Dùng cho: UC-KB-05.

**E4 ★** Khách mang máy hư tới bảo hành thì cửa hàng làm những bước nào? Cửa hàng tự sửa hay gửi về hãng? Làm sao biết máy còn hạn bảo hành?
- Hỏi thêm: bảo hành bằng phiếu giấy hay điện tử; máy đang gửi hãng thì theo dõi thế nào, khách hỏi tiến độ thì trả lời ra sao; xin chụp phiếu bảo hành.
- Dùng cho: quy trình QT-06; lớp `PhieuBaoHanh`; UC-KB-06, UC-KB-07.

## Phần F. Khép lại · G1 · khoảng 6 phút

**F1** Trong tất cả các việc vừa kể, việc nào mất thời gian hoặc hay sai nhất? (Bỏ qua nếu đã nói rõ ở A4.)
- Dùng cho: vấn đề tồn tại; ưu tiên chức năng.

**F2 ★** Nếu cửa hàng có website bán hàng, anh chị muốn nó làm được gì nhất? Điều gì làm anh chị ngại?
- Dùng cho: yêu cầu chức năng, phi chức năng.

**F3** Nếu có website thì ai trong cửa hàng sẽ cập nhật hàng, xử lý đơn? Những người đó dùng điện thoại, máy tính có quen không?
- Dùng cho: mục 1.5 khả thi vận hành; tác nhân sử dụng chính.

**F4** Ai là người quyết định có làm website hay không? Một năm chi khoảng bao nhiêu cho tên miền và chỗ đặt website thì anh chị thấy chấp nhận được?
- Hỏi cuối cùng. Người trả lời không phải chủ thì xin gửi câu này cho chủ.
- Dùng cho: mục 1.5 khả thi kinh tế.

**F5** Tụi em có thể nhắn Zalo hỏi thêm vài câu nếu thiếu không ạ? Giờ nào tiện cho anh chị?

## Danh sách biểu mẫu cần xin chụp

| Biểu mẫu | Gói dùng | Đã chụp |
|---|---|---|
| Hoá đơn bán lẻ, phiếu bán hàng hoặc báo giá | G3 | ☐ |
| Tin nhắn mẫu chốt đơn (ảnh chụp màn hình, che tên khách) | G3 | ☐ |
| Phiếu xuất kho, phiếu giao hàng | G4 | ☐ |
| Phiếu khảo sát trước lắp đặt, biên bản lắp đặt | G4 | ☐ |
| Lịch lắp đặt của thợ (sổ, bảng tính) | G4 | ☐ |
| Phiếu đổi trả | G4 | ☐ |
| Phiếu nhập kho, phiếu giao của nhà cung cấp (che giá) | G5 | ☐ |
| Phiếu bảo hành, phiếu tiếp nhận bảo hành | G5 | ☐ |
| Bảng giá, tờ khuyến mãi | G2 | ☐ |
| Mẫu báo cáo cuối ngày, cuối tháng | G1 | ☐ |

## Mẫu ghi chép

| Mã câu | Câu trả lời tóm tắt | Biểu mẫu nhắc tới | Câu cần hỏi lại |
|---|---|---|---|
| A1 | | | |
| A2 | | | |

Sau buổi phỏng vấn trong 24 giờ: người ghi chép chép lại bảng này vào thư mục `01_KhaoSat`, mỗi gói đọc phần của mình và bổ sung từ ghi âm.
