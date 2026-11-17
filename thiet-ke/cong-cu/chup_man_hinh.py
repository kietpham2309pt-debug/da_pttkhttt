import os
import re
import sys

from playwright.sync_api import sync_playwright

sys.stdout.reconfigure(encoding="utf-8")
GOC = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8090"
RA = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "man-hinh"))
os.makedirs(RA, exist_ok=True)
CAO_TOI_DA = 1400
SERIAL = "BDT-BOS-001-0001"


def chup(trang, ten):
    trang.wait_for_load_state("networkidle")
    cao = trang.evaluate("document.documentElement.scrollHeight")
    trang.screenshot(path=os.path.join(RA, ten + ".png"), full_page=True,
                     clip={"x": 0, "y": 0, "width": 1366, "height": min(cao, CAO_TOI_DA)})
    tieu_de = trang.title()
    print(f"{ten:24} {trang.url} cao={cao} tieu_de={tieu_de}")
    if "lỗi" in tieu_de.lower() or "error" in trang.url:
        print("  !!! trang loi")


def dang_nhap(trang, ten):
    trang.goto(GOC + "/dang-nhap")
    trang.fill("#ten-dang-nhap", ten)
    trang.fill("#mat-khau", "123456")
    trang.click("form[action$='/dang-nhap'] button[type=submit]")
    trang.wait_for_load_state()


def phien(trinh, ham, ten=None):
    ctx = trinh.new_context(viewport={"width": 1366, "height": 900}, locale="vi-VN")
    trang = ctx.new_page()
    try:
        if ten:
            dang_nhap(trang, ten)
        return ham(trang)
    finally:
        ctx.close()


def them_gio(trang, ma):
    trang.goto(f"{GOC}/san-pham/{ma}")
    nut = trang.locator("form.ct__dat button.ct__them")
    if nut.count() == 0:
        return False
    nut.click()
    trang.wait_for_load_state()
    return True


def khach(trang):
    trang.goto(GOC + "/")
    chup(trang, "trang-chu")
    trang.goto(GOC + "/san-pham?danhMuc=bep-dien-tu&thuongHieu=1&thuongHieu=2&sapXep=gia-tang")
    chup(trang, "danh-sach-loc")
    trang.goto(GOC + "/san-pham/1")
    chup(trang, "chi-tiet-san-pham")
    da_them = 0
    for ma in range(1, 41):
        if them_gio(trang, ma):
            da_them += 1
        if da_them == 2:
            break
    trang.goto(GOC + "/gio-hang")
    chup(trang, "gio-hang")
    trang.goto(GOC + "/dat-hang")
    trang.fill("#hoTen", "Trần Minh Khang")
    trang.fill("#soDienThoai", "0987654321")
    trang.fill("#tinhThanh", "TP.HCM")
    trang.fill("#quanHuyen", "Tân Phú")
    trang.fill("#diaChi", "140 Lê Trọng Tấn, Tây Thạnh")
    trang.check("input[name=phuongThuc][value=CONG_THANH_TOAN]")
    trang.click("button[name=nut][value=xemLai]")
    trang.wait_for_load_state()
    chup(trang, "dat-hang-xac-nhan")
    trang.check("input[name=dongYDieuKien]")
    trang.check("input[name=dongYXuLyDuLieu]")
    trang.click("form[action$='/dat-hang/xac-nhan'] button[type=submit]")
    trang.wait_for_load_state()
    m = re.search(r"/hoan-tat/(\d+)", trang.url)
    if not m:
        print("  !!! dat hang khong thanh cong", trang.url)
        return
    trang.click("a[href$='/thanh-toan/%s']" % m.group(1))
    trang.wait_for_load_state()
    chup(trang, "cong-thanh-toan")


def tra_cuu(trang):
    trang.goto(GOC + "/tra-cuu-don")
    trang.fill("#soDienThoai", "0912000007")
    trang.fill("#maDon", "7")
    trang.click("form[action$='/tra-cuu-don'] button[type=submit]")
    trang.wait_for_load_state()
    chup(trang, "tra-cuu-don")
    trang.goto(GOC + "/bao-hanh?cach=serial&soSerial=" + SERIAL)
    chup(trang, "tra-cuu-bao-hanh")


def mo(duong, ten):
    def lam(trang):
        trang.goto(GOC + duong)
        chup(trang, ten)
    return lam


def lap_don_dat(trang):
    trang.goto(GOC + "/quan-tri/kho/don-dat/lap")
    trang.select_option("#maNhaCungCap", index=1)
    o = trang.locator("input[name^='sl_']")
    o.nth(0).fill("3")
    trang.click("form[action$='/don-dat/lap'] button[type=submit]")
    trang.wait_for_load_state()
    m = re.search(r"/don-dat/(\d+)", trang.url)
    return m.group(1) if m else None


def gui_ncc(ma):
    def lam(trang):
        trang.goto(f"{GOC}/quan-tri/kho/don-dat/{ma}")
        trang.fill("#yKienChu", "Chủ đồng ý ngày 14/11")
        trang.click("#formGui button[type=submit]")
        trang.wait_for_load_state()
        print("  gui ncc ->", trang.url)
    return lam


def lap_phieu_nhap(ma):
    def lam(trang):
        trang.goto(f"{GOC}/quan-tri/kho/phieu-nhap/lap?donDat={ma}")
        trang.fill("#soHoaDon", "HD-NCC-0104")
        trang.fill("#nguoiGiao", "Nguyễn Văn Tài")
        trang.locator("textarea[name=serial]").first.fill("SN-MOI-0001\nSN-MOI-0002\nSN-MOI-0003")
        chup(trang, "phieu-nhap")
    return lam


def main():
    with sync_playwright() as p:
        trinh = p.chromium.launch(channel="chrome")
        phien(trinh, khach)
        phien(trinh, tra_cuu)
        phien(trinh, mo("/quan-tri", "tong-quan-quan-tri"), "quanly")
        phien(trinh, mo("/quan-tri/don-hang/7", "don-hang-chi-tiet"), "banhang")
        phien(trinh, mo("/quan-tri/giao-hang/phan-cong?don=5", "phan-cong-giao-lap"), "quanly")
        ma = phien(trinh, lap_don_dat, "thukho")
        print("  don dat", ma)
        if ma:
            phien(trinh, gui_ncc(ma), "quanly")
            phien(trinh, lap_phieu_nhap(ma), "thukho")
        else:
            phien(trinh, mo("/quan-tri/kho/phieu-nhap/1", "phieu-nhap"), "thukho")
        phien(trinh, mo("/quan-tri/bao-hanh/tiep-nhan?serial=" + SERIAL, "bao-hanh"), "banhang")
        phien(trinh, mo("/quan-tri/bao-cao/doanh-thu?tuNgay=2026-10-01&denNgay=2026-11-30&nhom=ngay", "bao-cao-doanh-thu"), "ketoan")
        trinh.close()


if __name__ == "__main__":
    main()
