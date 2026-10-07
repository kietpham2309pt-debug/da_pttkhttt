# Phân tích thiết kế hệ thống thông tin: Website bán hàng điện gia dụng

Bài tập nhóm học phần **Phân tích thiết kế hệ thống thông tin** (mã 0101003615), Khoa Công nghệ thông tin,
Trường Đại học Công Thương TP.HCM, học kỳ 1 năm học 2026-2027.
Lớp 15DHTH06 (mã lớp học phần 010100361506) · Giảng viên hướng dẫn: Nguyễn Thị Thúy A.

## Thành viên nhóm 4

| MSSV | Họ tên | Vai trò |
|---|---|---|
| 2001240237 | Phạm Tuấn Kiệt | Nhóm trưởng · G1 Tổng quan, tích hợp, quản trị |
| 2001240244 | Huỳnh Văn Lân | G3 Mua hàng trực tuyến, thanh toán |
| 2001240229 | Lê Đạt Tuấn Kiệt | G2 Sản phẩm, khuyến mãi |
| 2001240205 | Nguyễn Đăng Khoa | G5 Kho, nhà cung cấp, bảo hành |
| 2001240486 | Võ Trí Thức | G4 Xử lý đơn, giao hàng lắp đặt, đổi trả |

Năm gói việc chia dọc theo phân hệ: **G1** Tổng quan, tích hợp, quản trị · **G2** Sản phẩm, khuyến mãi ·
**G3** Mua hàng trực tuyến, thanh toán · **G4** Xử lý đơn, giao hàng lắp đặt, đổi trả · **G5** Kho, nhà cung cấp, bảo hành.

## Nội dung repo

| Thư mục | Nội dung |
|---|---|
| `giao-dien/` | Bộ giao diện chung (HTML tĩnh) để thiết kế màn hình, chụp ảnh cho báo cáo và làm bản mẫu khi khảo sát |
| `phan-tich/` | Phần phân tích thiết kế. `phan-tich/chung/` là phần dùng chung cả 5 gói phải theo: đối chiếu giáo trình, hiện trạng tổ chức, tác nhân và thừa tác viên, use case, lớp và trạng thái chung, mẫu đặc tả |
| `bao-cao/` | Báo cáo tiến độ (Word và PDF), dàn ý báo cáo cuối kỳ |
| `khao-sat/` | Kế hoạch và kịch bản phỏng vấn cửa hàng, bảng hỏi khách hàng, script tạo Google Form, kết quả chạy thử (thư mục `mo-phong/` là dữ liệu AI đóng vai, **không** dùng làm số liệu thật) |

**Đọc trước khi vẽ:** [`phan-tich/chung/doi-chieu-giao-trinh.md`](phan-tich/chung/doi-chieu-giao-trinh.md): bài giảng 4 chương yêu cầu gì, làm theo thứ tự nào, ký hiệu ra sao.

## Mở bộ giao diện

Mở `giao-dien/index.html` bằng Chrome hoặc Edge. Không cần cài đặt, không cần mạng.
Đọc [`giao-dien/QUY_TAC_GIAO_DIEN.md`](giao-dien/QUY_TAC_GIAO_DIEN.md) trước khi làm màn hình mới.

## Làm việc với repo

```bash
git clone https://github.com/kietpham2309pt-debug/da_pttkhttt.git
git switch -c g5-phieu-nhap
# làm xong
git add .
git commit -m "G5: man hinh lap phieu nhap"
git push -u origin g5-phieu-nhap
```

Sau đó tạo Pull Request trên GitHub để nhóm trưởng xem rồi gộp vào `main`.
