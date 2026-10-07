var TIEU_DE = 'Khảo sát thói quen mua đồ điện gia dụng';

function motLuaChon(form, tieuDe, luaChon, batBuoc, coKhac) {
  var cau = form.addMultipleChoiceItem().setTitle(tieuDe).setChoiceValues(luaChon).setRequired(batBuoc);
  if (coKhac) cau.showOtherOption(true);
  return cau;
}

function nhieuLuaChon(form, tieuDe, luaChon, batBuoc, coKhac) {
  var cau = form.addCheckboxItem().setTitle(tieuDe).setChoiceValues(luaChon).setRequired(batBuoc);
  if (coKhac) cau.showOtherOption(true);
  return cau;
}

function taoBangHoi() {
  var form = FormApp.create(TIEU_DE);
  form.setDescription(
    'Chào bạn, tụi mình là sinh viên ngành Công nghệ thông tin, Trường Đại học Công Thương TP.HCM, ' +
    'đang làm bài tập môn Phân tích thiết kế hệ thống thông tin với đề tài website bán hàng điện gia dụng. ' +
    'Khảo sát mất khoảng 4 đến 6 phút, không hỏi tên, số điện thoại hay email. ' +
    'Câu trả lời chỉ dùng cho bài học. Cảm ơn bạn nhiều.');
  form.setProgressBar(true);
  form.setConfirmationMessage('Cảm ơn bạn đã dành thời gian trả lời.');

  form.addSectionHeaderItem().setTitle('Mục 1. Về bạn');
  motLuaChon(form, 'K1. Bạn bao nhiêu tuổi?',
    ['Dưới 22', '22 đến 30', '31 đến 45', '46 đến 60', 'Trên 60'], true);
  motLuaChon(form, 'K2. Bạn đang sống ở đâu?', [
    'TP.HCM, khu vực nội thành cũ',
    'TP.HCM, khu vực ngoại thành cũ (Củ Chi, Hóc Môn, Bình Chánh, Nhà Bè, Cần Giờ)',
    'TP.HCM, khu vực Bình Dương hoặc Bà Rịa - Vũng Tàu cũ',
    'Tỉnh, thành khác'
  ], true).setHelpText('Thủ Đức tính là nội thành cũ.');
  motLuaChon(form, 'K3. Bạn thường mua đồ điện gia dụng để dùng ở đâu?',
    ['Trong gia đình', 'Cho quán, cửa hàng hoặc văn phòng', 'Cả hai'], true);
  motLuaChon(form,
    'K4. Trong 12 tháng qua, bạn hoặc người nhà đã mua xong bao nhiêu món đồ điện gia dụng ' +
    '(bếp, nồi cơm, máy hút mùi, lò vi sóng, quạt, máy lọc nước…)?',
    ['Chưa mua món nào', '1 món', '2 đến 3 món', 'Từ 4 món trở lên'], true);

  form.addPageBreakItem().setTitle('Mục 2. Thói quen mua');
  motLuaChon(form, 'K5. Lần gần nhất mua đồ điện gia dụng, bạn mua ở đâu?', [
    'Cửa hàng điện gia dụng gần nhà',
    'Siêu thị điện máy (Điện Máy Xanh, Nguyễn Kim, Chợ Lớn…)',
    'Sàn thương mại điện tử (Shopee, Lazada, Tiki, TikTok Shop)',
    'Xem livestream rồi đặt (TikTok, Facebook)',
    'Website của hãng hoặc của cửa hàng',
    'Nhắn Facebook, Zalo cho cửa hàng',
    'Người nhà mua giùm',
    'Chưa mua bao giờ'
  ], true, true);
  var k6 = form.addMultipleChoiceItem()
    .setTitle('K6. Bạn đã từng tự đặt mua đồ điện gia dụng trên mạng (sàn, website, livestream, Facebook, Zalo) chưa?')
    .setRequired(true);

  var muc2b = form.addPageBreakItem().setTitle('Mục 2b. Vì sao bạn ít mua online');
  nhieuLuaChon(form, 'K7. Điều gì làm bạn ngại mua đồ điện gia dụng trên mạng?', [
    'Sợ hàng giả, hàng không đúng mô tả',
    'Muốn xem và sờ tận tay',
    'Lo giao hàng làm hỏng máy',
    'Lo không ai lắp đặt',
    'Lo bảo hành khó',
    'Phí giao hàng cao',
    'Không rành đặt hàng trên mạng'
  ], false, true);

  var muc3 = form.addPageBreakItem().setTitle('Mục 3. Chọn sản phẩm');
  k6.setChoices([
    k6.createChoice('Đã từng, nhiều lần', muc3),
    k6.createChoice('Đã từng, một hai lần', muc2b),
    k6.createChoice('Chưa từng', muc2b)
  ]);
  form.addGridItem()
    .setTitle('K8. Khi chọn mua một món đồ điện gia dụng, các điều sau quan trọng với bạn ở mức nào?')
    .setHelpText('1 = không quan trọng, 5 = rất quan trọng.')
    .setRows([
      'Giá tốt',
      'Hàng chính hãng, thương hiệu quen',
      'Bảo hành dài',
      'Được lắp đặt tận nơi',
      'Giao hàng nhanh, đúng hẹn',
      'Có đánh giá của người đã mua',
      'So sánh được thông số (công suất, kích thước) giữa các mẫu'
    ])
    .setColumns(['1', '2', '3', '4', '5'])
    .setRequired(true);
  nhieuLuaChon(form, 'K9. Trên một trang web bán hàng, bạn hay tìm sản phẩm bằng cách nào?', [
    'Gõ tên vào ô tìm kiếm',
    'Bấm vào danh mục (bếp, máy hút mùi…)',
    'Lọc theo khoảng giá',
    'Lọc theo thương hiệu',
    'Xem mục khuyến mãi',
    'Hỏi nhân viên qua chat',
    'Tôi chưa dùng web bán hàng'
  ], false);

  form.addPageBreakItem().setTitle('Mục 4. Đặt hàng và thanh toán');
  nhieuLuaChon(form, 'K10. Khi mua online, bạn muốn trả tiền bằng cách nào?', [
    'Trả tiền mặt khi nhận hàng',
    'Chuyển khoản hoặc quét mã QR',
    'Ví điện tử (MoMo, ZaloPay, VNPay…)',
    'Thẻ ngân hàng, thẻ tín dụng',
    'Trả góp'
  ], true);
  motLuaChon(form, 'K11. Bạn có muốn tạo tài khoản trên website để đặt hàng không?', [
    'Có, để theo dõi đơn và lưu địa chỉ',
    'Đăng nhập nhanh bằng Google hoặc Zalo thì được',
    'Không, chỉ muốn nhập số điện thoại rồi đặt',
    'Không biết, chưa đặt online bao giờ'
  ], true);
  nhieuLuaChon(form, 'K12. Khi mua, bạn có cần những điều sau không?', [
    'Xem trước phí giao hàng trước khi đặt',
    'Mua nhiều món một lần với giá ưu đãi',
    'Giao và lắp tất cả các món trong một lần',
    'Được chỉ cách dùng sau khi lắp',
    'Xuất hoá đơn VAT',
    'Không cần điều nào ở trên'
  ], false);

  form.addPageBreakItem().setTitle('Mục 5. Giao hàng, lắp đặt, bảo hành');
  motLuaChon(form, 'K13. Với đồ lớn cần lắp (bếp âm, máy hút mùi, máy rửa chén), bạn muốn được hẹn lịch lắp đặt thế nào?', [
    'Giao và lắp luôn trong cùng một lần',
    'Tự chọn ngày giờ lắp trên website',
    'Nhân viên gọi điện hoặc nhắn Zalo hẹn',
    'Tự lắp hoặc nhờ thợ quen',
    'Ở xa, cần hướng dẫn hoặc video để tự lắp'
  ], true);
  nhieuLuaChon(form, 'K14. Bạn đã từng gặp rắc rối nào sau khi mua đồ điện gia dụng chưa?', [
    'Chưa từng',
    'Đổi trả bị làm khó',
    'Bảo hành lâu, bị đẩy qua lại giữa cửa hàng và hãng',
    'Giao trễ hẹn',
    'Lắp đặt ẩu'
  ], false, true);
  nhieuLuaChon(form, 'K15. Khi máy cần bảo hành, bạn muốn tra cứu thời hạn bảo hành bằng gì?', [
    'Số điện thoại lúc mua',
    'Số serial (dãy số in trên tem dán sau máy)',
    'Mã đơn hàng hoặc hoá đơn',
    'Giữ phiếu bảo hành giấy là đủ'
  ], true);

  form.addPageBreakItem().setTitle('Mục 6. Góp ý');
  form.addParagraphTextItem().setTitle('K16. Điều làm bạn khó chịu nhất khi mua đồ điện gia dụng là gì?');
  form.addParagraphTextItem().setTitle('K17. Một website bán đồ điện gia dụng nên có tính năng gì để bạn muốn mua ở đó?');

  var bang = SpreadsheetApp.create('Kết quả - ' + TIEU_DE);
  form.setDestination(FormApp.DestinationType.SPREADSHEET, bang.getId());
  if (typeof form.setPublished === 'function') form.setPublished(true);

  Logger.log('Link gửi người điền: ' + form.getPublishedUrl());
  Logger.log('Link sửa form: ' + form.getEditUrl());
  Logger.log('Bảng tính kết quả: ' + bang.getUrl());
}
