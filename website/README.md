# Bản chạy thử website bán hàng điện gia dụng

## Công nghệ và cấu trúc

Bản chạy thử hiện thực đúng thiết kế ở chương 9–11: 31 bảng MySQL theo lược đồ quan hệ, 27 lớp thực thể, kiến trúc 3 tầng.

| Thành phần | Lựa chọn |
|---|---|
| Ngôn ngữ, nền tảng | Java 17, Spring Boot 3.5 |
| Giao diện | Thymeleaf, HTML, CSS (không dùng thư viện giao diện ngoài) |
| Bảo mật | Spring Security: đăng nhập bằng biểu mẫu, mật khẩu băm BCrypt, phân quyền theo 7 vai trò, khoá tài khoản 15 phút sau 5 lần sai |
| Truy cập dữ liệu | Spring Data JPA (Hibernate), kiểm lược đồ khi khởi động (`ddl-auto=validate`) |
| Cơ sở dữ liệu | MySQL 8.4, tệp `src/main/resources/db/schema.sql` (31 bảng) và `data.sql` (dữ liệu mẫu 40 mẫu máy) |
| Đóng gói | Maven Wrapper (`mvnw`), một tệp `.jar` chạy độc lập |

| Tầng | Gói mã nguồn | Nội dung |
|---|---|---|
| Giao diện | `web`, `web.khach`, `web.quantri.g1` … `g5`; `templates/` | 26 controller, các trang Thymeleaf phía khách và phía quản trị |
| Nghiệp vụ | `nghiepvu`, `nghiepvu.khach`, `nghiepvu.g1` … `g5` | Lớp …BUS kiểm quy tắc nghiệp vụ: chuyển trạng thái đơn, chiếc máy theo serial, tiền cọc, phí lắp, đổi trả, bảo hành |
| Truy cập dữ liệu | `khodulieu` | Lớp …DAO (Spring Data JPA) |
| Thực thể | `thucthe` | Lớp thực thể ánh xạ bảng |
| Cấu hình | `cauhinh` | Phân quyền theo đường dẫn |

## Cách chạy

1. Cài JDK 17 và MySQL 8. Khởi động MySQL ở cổng 3306, tài khoản `root` không mật khẩu (nếu khác, đặt biến môi trường `DB_USER`, `DB_PASS`).
2. Trong thư mục `website`, đóng gói: `mvnw.cmd -DskipTests package` (Windows) hoặc `./mvnw -DskipTests package`.
3. Chạy: `java -jar target/diengiadung-1.0.0.jar`. Lần đầu chạy, chương trình tự tạo cơ sở dữ liệu `pttk_dien_gia_dung`, tạo bảng và nạp dữ liệu mẫu.
4. Mở `http://localhost:8090`. Đổi cổng bằng biến môi trường `PORT`.

## Tài khoản mẫu

Mọi tài khoản mẫu dùng mật khẩu `123456`.

| Tài khoản | Vai trò | Dùng để thử |
|---|---|---|
| quantri | Quản trị hệ thống | Tài khoản nhân viên, phân quyền |
| quanly | Quản lý cửa hàng (chủ cửa hàng cũng dùng vai này) | Danh mục, giá, khuyến mãi, phân công giao lắp, duyệt đổi trả, báo cáo |
| banhang | Nhân viên bán hàng | Sản phẩm, duyệt đơn, tiếp nhận đổi trả và bảo hành |
| thukho | Thủ kho | Nhà cung cấp, đơn đặt hàng, phiếu nhập, phiếu xuất, tồn kho, nhận lại máy |
| giaohang1, giaohang2 | Nhân viên giao hàng – kỹ thuật | Cập nhật giao hàng, biên bản lắp đặt |
| ketoan | Kế toán | Xác nhận tiền thu khi giao, hoàn tiền, báo cáo doanh thu |
| khach1 | Khách hàng | Hồ sơ, sổ địa chỉ, xem đơn cũ, đánh giá |

Khách vãng lai không cần đăng nhập vẫn xem sản phẩm, đặt hàng, tra cứu đơn và tra cứu bảo hành.

## Use case và đường dẫn

| Use case | Đường dẫn | Vai |
|---|---|---|
| UC-HT-01 Đăng nhập, UC-HT-06 Đổi mật khẩu | `/dang-nhap`, `/doi-mat-khau` | Mọi vai |
| UC-HT-02 Quản lý tài khoản nhân viên, UC-HT-03 Phân quyền | `/quan-tri/tai-khoan` | Quản trị hệ thống |
| UC-HT-04 Báo cáo doanh thu, UC-HT-05 Báo cáo tồn kho và bảo hành | `/quan-tri/bao-cao/doanh-thu`, `/quan-tri/bao-cao/ton-kho` | Quản lý, Kế toán |
| UC-SP-01 Danh mục, thương hiệu | `/quan-tri/danh-muc` | Quản lý |
| UC-SP-02 Sản phẩm, UC-SP-03 Cập nhật giá | `/quan-tri/san-pham`, `/quan-tri/san-pham/{mã}/gia` | Nhân viên bán hàng; giá: Quản lý |
| UC-SP-04 Khuyến mãi, mã giảm giá | `/quan-tri/khuyen-mai` | Quản lý |
| UC-SP-05 Tìm kiếm, lọc, UC-SP-06 Chi tiết, so sánh | `/san-pham`, `/san-pham/{mã}`, `/so-sanh` | Khách |
| UC-SP-07 Đánh giá sản phẩm | `/danh-gia` | Khách hàng |
| UC-MH-01 Đăng ký, UC-MH-02 Hồ sơ, sổ địa chỉ | `/dang-ky`, `/tai-khoan` | Khách |
| UC-MH-03 Giỏ hàng | `/gio-hang` | Khách |
| UC-MH-04 Đặt hàng, UC-MH-08 Áp mã giảm giá | `/dat-hang` | Khách |
| UC-MH-05 Thanh toán trực tuyến | `/thanh-toan/{mã đơn}` (cổng mô phỏng) | Khách |
| UC-MH-06 Theo dõi đơn, UC-MH-07 Huỷ đơn | `/tra-cuu-don` | Khách |
| UC-DH-01 Duyệt, xác nhận đơn | `/quan-tri/don-hang` | Nhân viên bán hàng |
| UC-DH-02 Phân công giao lắp | `/quan-tri/giao-hang/phan-cong` | Quản lý |
| UC-DH-03 Cập nhật giao hàng, UC-DH-05 Biên bản lắp đặt | `/quan-tri/giao-hang` | Nhân viên giao hàng – kỹ thuật |
| UC-DH-04 Xác nhận tiền thu khi giao | `/quan-tri/tien-thu` | Kế toán |
| UC-DH-06 Đổi trả, hoàn tiền | `/quan-tri/doi-tra` | Nhân viên bán hàng, Quản lý, Kế toán |
| UC-KB-01 Nhà cung cấp, UC-KB-02 Đơn đặt hàng | `/quan-tri/kho/nha-cung-cap`, `/quan-tri/kho/don-dat` | Thủ kho |
| UC-KB-03 Phiếu nhập, UC-KB-04 Phiếu xuất | `/quan-tri/kho/phieu-nhap`, `/quan-tri/kho/phieu-xuat` | Thủ kho |
| UC-KB-05 Kiểm kê, cảnh báo tồn thấp | `/quan-tri/kho/ton-kho` | Thủ kho |
| UC-KB-06 Tiếp nhận bảo hành, UC-KB-07 Tra cứu bảo hành | `/quan-tri/bao-hanh`, `/bao-hanh` | Nhân viên bán hàng; khách |
| UC-KB-08 Nhận lại máy về kho | `/quan-tri/kho/nhan-lai` | Thủ kho |

## Quy tắc nghiệp vụ được kiểm trong mã

- Đơn có tiền hàng trên 10 triệu đồng: màn hình duyệt đơn nhắc nhân viên xin cọc và gợi ý số tiền 10% đến 20%. Đơn nội thành trên 5 triệu đồng miễn phí lắp, không quá 5 triệu thu 250.000 đ.
- Chỉ duyệt đơn khi số máy trong kho (trừ phần đang giữ cho đơn khác) đủ cho đơn; đơn đã có phiếu xuất kho thì không huỷ được.
- Trạng thái đơn chỉ đi theo các bước đã thiết kế (Chờ xác nhận → Đã xác nhận → Đang giao → Hoàn thành; Huỷ; Giao không thành công), mỗi lần đổi ghi một dòng lịch sử.
- Mỗi chiếc máy theo dõi bằng số serial, chuyển trạng thái Trong kho → Đã xuất → Đã bán → Đang bảo hành / Trả về kho; phiếu xuất chỉ nhận serial đang ở trong kho và đúng mẫu máy của đơn.
- Đổi trả: trong 7 ngày còn nguyên seal, hoặc trong 30 ngày nếu lỗi nhà sản xuất; ngoài điều kiện phải có Quản lý duyệt.
- Bảo hành tra theo số serial hoặc số điện thoại, kiểm còn hạn trước khi tiếp nhận.
- Chỉ Quản lý sửa giá; giá thấp hơn giá nhập gần nhất phải nhập lý do và được lưu vết.

## Giới hạn của bản chạy thử

- Cổng thanh toán là trang mô phỏng, không kết nối ngân hàng; email xác nhận chỉ hiện thông báo.
- Không phát hành hoá đơn điện tử (chỉ lưu số hoá đơn), không tính phí giao đi tỉnh.
- Một số thông tin chưa có cột riêng trong thiết kế (lý do sửa giá, kết quả kiểm máy khi nhận lại) được ghi vào cột ghi chú của bảng liên quan.
