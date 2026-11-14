import io
import json
import os
import re
import sys

K = json.load(io.open(sys.argv[1], encoding="utf-8"))
RA = sys.argv[2]

DOI = [
    ("{G}khach/trang-chu.html", "/"),
    ("{G}khach/danh-sach.html?danhMuc=", "/san-pham?danhMuc="),
    ("{G}khach/danh-sach.html", "/san-pham"),
    ("{G}khach/san-pham.html", "/san-pham"),
    ("{G}khach/gio-hang.html", "/gio-hang"),
    ("{G}khach/bao-mat.html", "/chinh-sach/bao-mat"),
    ("{G}khach/dieu-khoan.html", "/chinh-sach/dieu-khoan"),
    ("{G}assets/", "/assets/"),
    ("{G}", "/"),
]


def doi(s):
    for a, b in DOI:
        s = s.replace(a, b)
    return s


dau = doi(K["dau"])
dau = dau.replace('<a class="dh-nut" href="{G}khach/dang-nhap.html">Đăng nhập</a>'.replace("{G}", "/"),
                  '<a class="dh-nut" href="/tra-cuu-don">Tra cứu đơn</a>\n'
                  '    <a class="dh-nut" href="/bao-hanh">Bảo hành</a>\n'
                  '    <a class="dh-nut" sec:authorize="!isAuthenticated()" href="/dang-nhap">Đăng nhập</a>\n'
                  '    <a class="dh-nut" sec:authorize="hasRole(\'KHACH_HANG\')" href="/tai-khoan">Tài khoản</a>\n'
                  '    <a class="dh-nut" sec:authorize="isAuthenticated() and !hasRole(\'KHACH_HANG\')" href="/quan-tri">Quản trị</a>')
assert "/tra-cuu-don" in dau, "khong thay nut dang nhap"
dau = re.sub(r'aria-label="Giỏ hàng, 3 sản phẩm">', 'th:aria-label="|Giỏ hàng, ${soLuongGio} sản phẩm|">', dau)
dau = dau.replace('<span class="nut-tron__so">3</span>', '<span class="nut-tron__so" th:if="${soLuongGio > 0}" th:text="${soLuongGio}">0</span>')
assert "soLuongGio" in dau
chan = doi(K["chan"])
menu_nho = doi(K["menuNho"]).replace('href="/khach/dang-nhap.html"', 'href="/dang-nhap"')
menu_nho = menu_nho.replace("{G}khach/dang-nhap.html", "/dang-nhap")
for x in (dau, chan, menu_nho):
    assert "{G}" not in x and ".html" not in x, re.findall(r'href="[^"]*\.html[^"]*"', x)[:3]

html = f"""<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head th:fragment="dau(tieuDe)">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title th:text="${{tieuDe}} + ' - Bep Nè'">Bep Nè</title>
    <script src="/assets/js/khoi-dau.js"></script>
    <link rel="icon" href="/assets/img/favicon.ico" sizes="any">
    <link rel="apple-touch-icon" href="/assets/img/icon-180.png">
    <link rel="stylesheet" href="/assets/css/site.css">
    <link rel="stylesheet" href="/assets/css/ung-dung.css">
    <link rel="stylesheet" href="/css/web.css">
</head>
<body>
<th:block th:fragment="dauTrang">
{K["boIcon"]}
{K["luoi"]}
{dau}
</th:block>

<th:block th:fragment="thongBao">
    <div class="thong-bao thong-bao--ok" th:if="${{thongBao != null}}" role="status" th:text="${{thongBao}}"></div>
    <div class="thong-bao thong-bao--loi" th:if="${{loi != null}}" role="alert" th:text="${{loi}}"></div>
</th:block>

<th:block th:fragment="chanTrang">
{chan}
{menu_nho}
<script src="/assets/js/site.js"></script>
</th:block>
</body>
</html>
"""
os.makedirs(os.path.dirname(RA), exist_ok=True)
io.open(RA, "w", encoding="utf-8", newline="\n").write(html)
print("xong", len(html))
