import io
import os
import re
import sys
from datetime import datetime, timedelta

NGUON = sys.argv[1]
RA = sys.argv[2]
MAT_KHAU = "$2a$10$B523y4XvFUkj/bvQD.Sn.uQSH0REW.ozGPOxsvA5GQRgjwdjguLdq"


def doc_bo(chuoi, i):
    gia_tri, cur, trong_chuoi, la_chuoi = [], "", False, False
    i += 1
    while True:
        c = chuoi[i]
        if trong_chuoi:
            if c == "'":
                if chuoi[i + 1] == "'":
                    cur += "'"
                    i += 2
                    continue
                trong_chuoi = False
            else:
                cur += c
        elif c == "'":
            trong_chuoi, la_chuoi, cur = True, True, ""
        elif c in ",)":
            s = cur.strip()
            if la_chuoi:
                gia_tri.append(cur)
            elif s.upper() == "NULL":
                gia_tri.append(None)
            else:
                gia_tri.append(int(s) if re.fullmatch(r"-?\d+", s) else s)
            cur, la_chuoi = "", False
            if c == ")":
                return gia_tri, i + 1
        else:
            cur += c
        i += 1


def bang(sql, ten):
    m = re.search(r"INSERT IGNORE INTO " + ten + r"\s*\(([^)]*)\)\s*VALUES", sql)
    cot = [x.strip() for x in m.group(1).split(",")]
    i = m.end()
    dong = []
    while True:
        while sql[i] in " \r\n\t,":
            i += 1
        if sql[i] != "(":
            break
        bo, i = doc_bo(sql, i)
        dong.append(dict(zip(cot, bo)))
    return dong


def q(v):
    if v is None:
        return "NULL"
    if isinstance(v, bool):
        return "b'1'" if v else "b'0'"
    if isinstance(v, int):
        return str(v)
    return "'" + str(v).replace("\\", "\\\\").replace("'", "''") + "'"


def chen(ten, cot, ds):
    if not ds:
        return ""
    hang = ",\n".join("(" + ", ".join(q(x) for x in r) + ")" for r in ds)
    return f"INSERT IGNORE INTO {ten} ({', '.join(cot)}) VALUES\n{hang};\n\n"


def main():
    sql = io.open(NGUON, encoding="utf-8").read()
    loai = {r["id"]: r for r in bang(sql, "loai_san_pham")}
    th = bang(sql, "thuong_hieu")
    ncc = bang(sql, "nha_cung_cap")
    sp = bang(sql, "san_pham")
    goc = datetime(2026, 9, 1, 8, 0, 0)
    out = ["SET NAMES utf8mb4;\n\n"]

    out.append(chen("VaiTro", ["maVaiTro", "tenVaiTro"], [
        ("KHACH_HANG", "Khách hàng"), ("NV_BAN_HANG", "Nhân viên bán hàng"), ("THU_KHO", "Thủ kho"),
        ("NV_GIAO_HANG", "Nhân viên giao hàng – kỹ thuật"), ("QUAN_LY", "Quản lý cửa hàng"),
        ("KE_TOAN", "Kế toán"), ("QUAN_TRI", "Quản trị hệ thống")]))

    tk = [
        (1, "quantri", "Lê Minh Quân", "0901000001", ["QUAN_TRI"]),
        (2, "quanly", "Trần Thu Trang", "0901000002", ["QUAN_LY"]),
        (3, "banhang", "Nguyễn Hữu Phúc", "0901000003", ["NV_BAN_HANG"]),
        (4, "thukho", "Phan Văn Khải", "0901000004", ["THU_KHO"]),
        (5, "giaohang1", "Đỗ Văn Lực", "0901000005", ["NV_GIAO_HANG"]),
        (6, "giaohang2", "Võ Thành Nam", "0901000006", ["NV_GIAO_HANG"]),
        (7, "ketoan", "Huỳnh Thị Mai", "0901000007", ["KE_TOAN"]),
        (8, "khach1", "Nguyễn Văn An", "0912000001", ["KHACH_HANG"]),
    ]
    out.append(chen("TaiKhoan", ["maTaiKhoan", "tenDangNhap", "matKhau", "trangThai", "ngayTao", "soLanSaiLienTiep", "phaiDoiMatKhau"],
                    [(t[0], t[1], MAT_KHAU, "DANG_DUNG", goc.strftime("%Y-%m-%d %H:%M:%S"), 0, False) for t in tk]))
    out.append(chen("TaiKhoanVaiTro", ["maTaiKhoan", "maVaiTro"], [(t[0], v) for t in tk for v in t[4]]))
    out.append(chen("NhanVien", ["maNhanVien", "hoTen", "soDienThoai", "maTaiKhoan"], [(t[0], t[2], t[3], t[0]) for t in tk if t[4] != ["KHACH_HANG"]]))
    out.append(chen("KhachHang", ["maKhachHang", "hoTen", "soDienThoai", "email", "ngayDangKy", "maTaiKhoan"],
                    [(1, "Nguyễn Văn An", "0912000001", "an.nguyen@example.com", goc.strftime("%Y-%m-%d %H:%M:%S"), 8)]))
    out.append(chen("DiaChiGiaoHang", ["maDiaChi", "maKhachHang", "hoTenNguoiNhan", "soDienThoai", "diaChi", "noiThanh", "macDinh"],
                    [(1, 1, "Nguyễn Văn An", "0912000001", "140 Lê Trọng Tấn, Tây Thạnh, Tân Phú, TP.HCM", True, True)]))

    out.append(chen("DanhMuc", ["maDanhMuc", "tenDanhMuc", "thuTu"], [(l["ma_loai"], l["ten_loai"], l["id"]) for l in loai.values()]))
    out.append(chen("ThuongHieu", ["maThuongHieu", "tenThuongHieu", "xuatXu"], [(t["id"], t["ten_thuong_hieu"], t["xuat_xu"]) for t in th]))
    out.append(chen("NhaCungCap", ["maNhaCungCap", "tenNhaCungCap", "soDienThoai", "email", "diaChi", "dangHopTac"],
                    [(n["id"], n["ten_nha_cung_cap"], n["so_dien_thoai"], n["email"], n["dia_chi"], True) for n in ncc]))

    sp_ds, ts_ds, ma_ts = [], [], 0
    for s in sp:
        thong_so = [p.split(":", 1) for p in (s["thong_so_ky_thuat"] or "").split("; ") if ":" in p]
        kieu_lap = dict((a.strip(), b.strip()) for a, b in thong_so).get("Kiểu lắp", "")
        loai_ma = loai[s["loai_san_pham_id"]]["ma_loai"]
        can_lap = loai_ma in ("bep-dien-tu", "bep-hong-ngoai", "hut-mui") or "Âm" in kieu_lap
        model = dict((a.strip(), b.strip()) for a, b in thong_so).get("Model", s["ma_san_pham"])
        sp_ds.append((s["id"], s["ten_san_pham"], model, loai_ma, s["thuong_hieu_id"], s["gia_khuyen_mai"] or s["gia_ban"],
                      s["gia_ban"], s["bao_hanh_thang"] or 12, can_lap, s["mo_ta"], s["hinh_anh"],
                      "DANG_BAN" if s["dang_ban"] else "NGUNG_BAN", goc.strftime("%Y-%m-%d %H:%M:%S")))
        for a, b in thong_so:
            if a.strip() in ("Thương hiệu", "Model"):
                continue
            ma_ts += 1
            ts_ds.append((ma_ts, s["id"], a.strip()[:80], b.strip()[:120]))
    out.append(chen("SanPham", ["maSanPham", "tenSanPham", "model", "maDanhMuc", "maThuongHieu", "giaBan", "giaNiemYet", "thoiHanBaoHanh",
                                "canLapDat", "moTa", "anh", "trangThai", "ngayTao"], sp_ds))
    out.append(chen("ThongSoKyThuat", ["maThongSo", "maSanPham", "tenThongSo", "giaTri"], ts_ds))

    pn, ctpn, serial = [], [], []
    ma_ct = 0
    for k, n in enumerate(ncc):
        pn.append((k + 1, n["id"], f"HD-NCC-{k + 1:04d}", (goc + timedelta(days=k)).strftime("%Y-%m-%d %H:%M:%S"), 4, "Nhập hàng đầu kỳ"))
    for s in sp:
        so = max(2, min(int(s["so_luong_ton"] or 0), 6))
        ma_ct += 1
        ma_pn = (s["id"] % len(ncc)) + 1
        gia_nhap = int((s["gia_khuyen_mai"] or s["gia_ban"]) * 0.78) // 1000 * 1000
        ctpn.append((ma_ct, ma_pn, s["id"], so, so, gia_nhap))
        for j in range(so):
            serial.append([f"{s['ma_san_pham']}-{j + 1:04d}", s["id"], ma_ct, pn[ma_pn - 1][3], None, None, "TRONG_KHO"])
    out.append(chen("PhieuNhap", ["maPhieuNhap", "maNhaCungCap", "soHoaDonNCC", "ngayNhap", "maThuKho", "ghiChu"], pn))
    out.append(chen("ChiTietPhieuNhap", ["maChiTietNhap", "maPhieuNhap", "maSanPham", "soLuongChungTu", "soLuongThucNhap", "donGiaNhap"], ctpn))

    gia = {s["id"]: (s["gia_khuyen_mai"] or s["gia_ban"]) for s in sp}
    ten = {s["id"]: s["ten_san_pham"] for s in sp}
    ma = {s["id"]: s["ma_san_pham"] for s in sp}
    canlap = {r[0]: r[8] for r in sp_ds}

    def lay_serial(ma_sp):
        for r in serial:
            if r[1] == ma_sp and r[6] == "TRONG_KHO":
                return r
        raise KeyError(ma_sp)

    don = [
        (1, 1, "Nguyễn Văn An", "0912000001", "140 Lê Trọng Tấn, Tây Thạnh, Tân Phú, TP.HCM", [(1, 1)], "HOAN_THANH", 20),
        (2, None, "Trần Thị Bình", "0912000002", "45 Nguyễn Trãi, Phường 2, Quận 5, TP.HCM", [(9, 1)], "HOAN_THANH", 12),
        (3, None, "Lê Hoàng Cường", "0912000003", "12 Võ Văn Ngân, Thủ Đức, TP.HCM", [(17, 1), (25, 1)], "HOAN_THANH", 6),
        (4, None, "Phạm Thị Dung", "0912000004", "88 Cộng Hòa, Tân Bình, TP.HCM", [(2, 1)], "DANG_GIAO", 2),
        (5, None, "Hoàng Anh Tuấn", "0912000005", "202 Phan Đăng Lưu, Phú Nhuận, TP.HCM", [(33, 1)], "DA_XAC_NHAN", 1),
        (6, 1, "Nguyễn Văn An", "0912000001", "140 Lê Trọng Tấn, Tây Thạnh, Tân Phú, TP.HCM", [(10, 1)], "CHO_XAC_NHAN", 0),
        (7, None, "Đặng Thu Hà", "0912000007", "5 Trần Hưng Đạo, Quận 1, TP.HCM", [(5, 1)], "CHO_XAC_NHAN", 0),
    ]
    dh, ct, ls, px, ctpx, pg, gd = [], [], [], [], [], [], []
    ma_ct = ma_ls = ma_px = ma_pg = ma_gd = 0
    bay_gio = datetime(2026, 11, 14, 9, 0, 0)
    for (m, kh, hoten, sdt, dc, hang, tt, truoc) in don:
        ngay = bay_gio - timedelta(days=truoc, hours=2)
        tien_hang = sum(gia[a] * b for a, b in hang)
        lap = any(canlap[a] for a, _ in hang)
        phi_lap = 0 if (tien_hang > 5000000 or not lap) else 250000
        tong = tien_hang + phi_lap
        coc = (tong // 10 // 1000 * 1000) if tien_hang > 10000000 else 0
        hoan_thanh = tt == "HOAN_THANH"
        tttt = "DA_THANH_TOAN" if hoan_thanh else ("DA_DAT_COC" if coc else "CHUA_THANH_TOAN")
        dh.append((m, kh, hoten, sdt, None, dc, True, lap, ngay.strftime("%Y-%m-%d %H:%M:%S"),
                   (ngay + timedelta(days=2)).strftime("%Y-%m-%d %H:%M:%S") if hoan_thanh else None,
                   tien_hang, 0, 0, phi_lap, tong, coc, "TIEN_MAT_KHI_NHAN", tttt, tt,
                   3 if tt != "CHO_XAC_NHAN" else None, f"HD{m:07d}" if hoan_thanh else None, True))
        for a, b in hang:
            ma_ct += 1
            ct.append((ma_ct, m, a, ten[a], b, gia[a]))
        buoc = ["CHO_XAC_NHAN", "DA_XAC_NHAN", "DANG_GIAO", "HOAN_THANH"]
        den = buoc.index(tt)
        cu = None
        for k in range(den + 1):
            ma_ls += 1
            ls.append((ma_ls, m, cu, buoc[k], (ngay + timedelta(hours=k * 8)).strftime("%Y-%m-%d %H:%M:%S"),
                       None if k == 0 else (3 if k == 1 else (4 if k == 2 else 5)), None))
            cu = buoc[k]
        if coc:
            ma_gd += 1
            gd.append((ma_gd, m, "COC", coc, "Chuyển khoản qua cổng", f"MOCK-{m:06d}-COC", "THANH_CONG", (ngay + timedelta(minutes=5)).strftime("%Y-%m-%d %H:%M:%S")))
        if den >= 2:
            ma_px += 1
            ngay_xuat = ngay + timedelta(hours=16)
            px.append((ma_px, m, ngay_xuat.strftime("%Y-%m-%d %H:%M:%S"), "Xuất bán theo đơn", 4))
            ma_pg += 1
            pg.append((ma_pg, m, "GIAO_LAP" if lap else "GIAO", 5, None, None, ngay_xuat.strftime("%Y-%m-%d"), "14:00:00", "16:00:00",
                       "DA_GIAO" if hoan_thanh else "DA_PHAN_CONG", tong - coc if hoan_thanh else 0,
                       7 if hoan_thanh else None, (ngay_xuat + timedelta(hours=8)).strftime("%Y-%m-%d %H:%M:%S") if hoan_thanh else None, 2))
            for a, b in hang:
                for _ in range(b):
                    r = lay_serial(a)
                    r[6] = "DA_BAN" if hoan_thanh else "DA_XUAT"
                    if hoan_thanh:
                        r[4] = (ngay + timedelta(days=2)).strftime("%Y-%m-%d %H:%M:%S")
                        r[5] = m
                    ctpx.append((ma_px, r[0]))
    out.append(chen("DonHang", ["maDonHang", "maKhachHang", "hoTenNguoiNhan", "soDienThoaiNhan", "email", "diaChiGiao", "noiThanh", "canLapDat",
                                "ngayDat", "ngayHoanThanh", "tienHang", "tienGiam", "phiGiao", "phiLapDat", "tongTien", "tienCoc",
                                "phuongThucThanhToan", "trangThaiThanhToan", "trangThai", "maNhanVienDuyet", "soHoaDon", "dongYXuLyDuLieu"], dh))
    out.append(chen("SanPhamSerial", ["soSerial", "maSanPham", "maChiTietNhap", "ngayNhap", "ngayBan", "maDonHangBan", "trangThai"], [tuple(r) for r in serial]))
    out.append(chen("ChiTietDonHang", ["maChiTiet", "maDonHang", "maSanPham", "tenSanPhamLucBan", "soLuong", "donGia"], ct))
    out.append(chen("LichSuTrangThaiDon", ["maLichSu", "maDonHang", "trangThaiCu", "trangThaiMoi", "thoiDiem", "maNhanVien", "ghiChu"], ls))
    out.append(chen("GiaoDichThanhToan", ["maGiaoDich", "maDonHang", "loai", "soTien", "phuongThuc", "maGiaoDichCong", "trangThai", "thoiDiem"], gd))
    out.append(chen("PhieuXuat", ["maPhieuXuat", "maDonHang", "ngayXuat", "lyDoXuat", "maThuKho"], px))
    out.append(chen("ChiTietPhieuXuat", ["maPhieuXuat", "soSerial"], ctpx))
    out.append(chen("PhieuGiaoHang", ["maPhieuGiao", "maDonHang", "loaiViec", "maNhanVienGiao", "donViVanChuyen", "maVanDon", "ngayHen", "gioBatDau",
                                      "gioKetThuc", "trangThai", "tienThuKhiGiao", "maKeToanXacNhan", "thoiDiemXacNhanTien", "maNguoiPhanCong"], pg))
    hom_nay = bay_gio.date()
    out.append(chen("KhuyenMai", ["maKhuyenMai", "tenKhuyenMai", "loai", "maCode", "kieuGiam", "mucGiam", "giamToiDa", "donToiThieu", "quaTang",
                                  "ngayBatDau", "ngayKetThuc", "soLuotToiDa", "soLuotDaDung", "trangThai", "maNguoiTao"], [
        (1, "Giảm 500.000 đ cho đơn từ 10 triệu", "MA_GIAM_GIA", "GIAM500K", "SO_TIEN", 500000, None, 10000000, None,
         str(hom_nay - timedelta(days=14)), str(hom_nay + timedelta(days=30)), 200, 0, "DANG_CHAY", 2),
        (2, "Tuần lễ bếp từ: giảm 5%", "GIAM_THEO_SAN_PHAM", None, "PHAN_TRAM", 5, 1000000, 0, "Bộ nồi inox 3 món",
         str(hom_nay - timedelta(days=3)), str(hom_nay + timedelta(days=11)), None, 0, "DANG_CHAY", 2)]))
    out.append(chen("KhuyenMaiSanPham", ["maKhuyenMai", "maSanPham"], [(2, s["id"]) for s in sp if s["loai_san_pham_id"] == 1]))
    io.open(RA, "w", encoding="utf-8", newline="\n").write("".join(out))
    print("san pham", len(sp_ds), "thong so", len(ts_ds), "serial", len(serial), "don", len(dh))


if __name__ == "__main__":
    main()
