import importlib.util
import io
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from y_nghia_cot import y_nghia

REPO = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
SCHEMA = os.path.join(REPO, "website", "src", "main", "resources", "db", "schema.sql")
RA = os.path.join(REPO, "thiet-ke")
SD = os.path.join(RA, "so-do")

luu_argv = sys.argv
sys.argv = ["x", "khong-dung"]
spec = importlib.util.spec_from_file_location("stt", os.path.join(REPO, "website", "cong-cu", "sinh_thuc_the.py"))
stt = importlib.util.module_from_spec(spec)
spec.loader.exec_module(stt)
sys.argv = luu_argv

GOI = {
    "G1": ("Quản trị hệ thống", ["VaiTro", "TaiKhoan", "NhanVien"]),
    "G2": ("Sản phẩm và khuyến mãi", ["DanhMuc", "ThuongHieu", "SanPham", "ThongSoKyThuat", "LichSuGia", "KhuyenMai", "DanhGia"]),
    "G3": ("Mua hàng và thanh toán", ["KhachHang", "DiaChiGiaoHang", "DonHang", "ChiTietDonHang", "GiaoDichThanhToan"]),
    "G4": ("Đơn hàng, giao lắp, đổi trả", ["LichSuTrangThaiDon", "PhieuGiaoHang", "PhieuLapDat", "YeuCauDoiTra"]),
    "G5": ("Kho, nhà cung cấp, bảo hành", ["NhaCungCap", "DonDatHangNCC", "ChiTietDonDatHangNCC", "PhieuNhap", "ChiTietPhieuNhap",
                                           "SanPhamSerial", "PhieuXuat", "PhieuBaoHanh"]),
}
GOI_CUA = {l: g for g, (_, ds) in GOI.items() for l in ds}

PHUONG_THUC = {
    "TaiKhoan": [("kiemTraMatKhau", "matKhau : String", "boolean"), ("khoa", "", "void"), ("moKhoa", "", "void"),
                 ("ghiSaiMatKhau", "", "void"), ("doiMatKhau", "matKhauMoi : String", "void"), ("coVai", "maVaiTro : String", "boolean")],
    "NhanVien": [("capNhatThongTin", "hoTen : String, soDienThoai : String", "void")],
    "KhachHang": [("capNhatHoSo", "hoTen : String, email : String", "void"), ("diaChiMacDinh", "", "DiaChiGiaoHang")],
    "DiaChiGiaoHang": [("datMacDinh", "", "void")],
    "DanhMuc": [("coSanPham", "", "boolean")],
    "SanPham": [("capNhatGia", "giaMoi : long, lyDo : String, nhanVien : NhanVien", "LichSuGia"), ("moBan", "", "void"),
                ("ngungBan", "", "void"), ("soMayTrongKho", "", "int"), ("giaSauKhuyenMai", "", "long")],
    "LichSuGia": [],
    "KhuyenMai": [("conHieuLuc", "ngay : LocalDate", "boolean"), ("tinhTienGiam", "tienHang : long", "long"), ("tamDung", "", "void"),
                  ("tangLuotDung", "", "void")],
    "DanhGia": [],
    "DonHang": [("tinhTongTien", "", "long"), ("tinhTienCoc", "", "long"), ("doiTrangThai", "moi : TrangThaiDonHang, nhanVien : NhanVien", "void"),
                ("huy", "lyDo : String", "void"), ("apKhuyenMai", "km : KhuyenMai", "void")],
    "ChiTietDonHang": [("thanhTien", "", "long")],
    "GiaoDichThanhToan": [],
    "LichSuTrangThaiDon": [],
    "PhieuGiaoHang": [("trungLichVoi", "khac : PhieuGiaoHang", "boolean"), ("baoDaGiao", "tienThu : long", "void"),
                      ("baoKhongThanhCong", "lyDo : String", "void"), ("xacNhanTienThu", "keToan : NhanVien", "void")],
    "PhieuLapDat": [("tongPhi", "", "long")],
    "YeuCauDoiTra": [("trongHanDoiTra", "ngay : LocalDate", "boolean"), ("duyet", "quanLy : NhanVien", "void"),
                     ("tuChoi", "quanLy : NhanVien, lyDo : String", "void"), ("hoanTien", "soTien : long", "void")],
    "NhaCungCap": [("ngungHopTac", "", "void")],
    "DonDatHangNCC": [("guiNhaCungCap", "quanLy : NhanVien", "void"), ("huy", "", "void")],
    "PhieuNhap": [("themDong", "sp : SanPham, soChungTu : int, soThucNhap : int, donGia : long", "ChiTietPhieuNhap")],
    "ChiTietPhieuNhap": [("ghiSerial", "dsSerial : List<String>", "void")],
    "SanPhamSerial": [("hanBaoHanh", "", "LocalDate"), ("conBaoHanh", "ngay : LocalDate", "boolean"),
                      ("doiTrangThai", "moi : TrangThaiSerial", "void")],
    "PhieuXuat": [("chonSerial", "may : SanPhamSerial", "void")],
    "PhieuBaoHanh": [("guiHang", "ngay : LocalDateTime", "void"), ("nhanVe", "ketQua : String", "void"), ("traKhach", "", "void")],
}

KIEU_JAVA = {"Long": "long", "Integer": "int", "Boolean": "boolean", "TEXT": "String"}


def doc_schema():
    s = io.open(SCHEMA, encoding="utf-8").read()
    bang = {}
    for m in re.finditer(r"CREATE TABLE IF NOT EXISTS (\w+) \((.*?)\n\) ENGINE", s, re.S):
        ten, than = m.group(1), m.group(2)
        cot, pk, uq, fk, ck = [], [], [], [], []
        for d in [x.strip().rstrip(",") for x in than.split("\n") if x.strip()]:
            if d.startswith("PRIMARY KEY"):
                pk = re.findall(r"\w+", d.split("(", 1)[1])
            elif d.startswith("UNIQUE KEY"):
                uq.append(re.findall(r"\w+", d.split("(", 1)[1]))
            elif d.startswith("KEY"):
                continue
            elif "FOREIGN KEY" in d:
                mm = re.search(r"FOREIGN KEY \((\w+)\) REFERENCES (\w+) \((\w+)\)", d)
                fk.append((mm.group(1), mm.group(2), mm.group(3), "ON DELETE CASCADE" in d))
            elif d.startswith("CONSTRAINT") and "CHECK" in d:
                ck.append(d.split("CHECK", 1)[1].strip())
            else:
                mm = re.match(r"(\w+) (\w+(?:\(\d+\))?)(.*)", d)
                ten_cot, kieu, con = mm.group(1), mm.group(2), mm.group(3)
                cot.append({"ten": ten_cot, "kieu": kieu, "null": "NOT NULL" not in con, "tang": "AUTO_INCREMENT" in con,
                            "md": (re.search(r"DEFAULT ('[^']*'|b'\d'|\w+)", con) or [None, None])[1]})
        bang[ten] = {"cot": cot, "pk": pk, "uq": uq, "fk": fk, "ck": ck}
    return bang


def rang_buoc_cot(b, c):
    kq = []
    if c["tang"]:
        kq.append("Tự tăng")
    for u in b["uq"]:
        if c["ten"] in u:
            kq.append("UNIQUE" + (" (cùng " + ", ".join(x for x in u if x != c["ten"]) + ")" if len(u) > 1 else ""))
    for ck in b["ck"]:
        if re.search(r"\b" + c["ten"] + r"\b", ck):
            kq.append("CHECK " + (ck[1:-1] if ck.startswith("(") and ck.endswith(")") else ck).replace("b'1'", "1"))
    if c["md"] is not None:
        kq.append("Mặc định " + c["md"].replace("b'0'", "0").replace("b'1'", "1"))
    for f in b["fk"]:
        if f[0] == c["ten"] and f[3]:
            kq.append("Xoá dây chuyền")
    return "; ".join(kq)


def tu_dien(bang):
    out = ["# Từ điển dữ liệu\n",
           "Sinh tự động từ `website/src/main/resources/db/schema.sql` (MySQL 8, `utf8mb4_unicode_ci`). Kiểu `BIT(1)` là đúng/sai; tiền lưu `BIGINT` theo đồng.\n",
           "Khoá: **PK** khoá chính, **FK → Bảng** khoá ngoại.\n"]
    for g, (ten_goi, ds) in GOI.items():
        out.append(f"\n## {g} · {ten_goi}\n")
        for t in ds:
            b = bang[t]
            out.append(f"\n### {t}\n\n| Cột | Kiểu | Khoá | Null | Ràng buộc | Ý nghĩa |\n|---|---|---|---|---|---|")
            fkm = {f[0]: f[1] for f in b["fk"]}
            for c in b["cot"]:
                khoa = []
                if c["ten"] in b["pk"]:
                    khoa.append("PK")
                if c["ten"] in fkm:
                    khoa.append("FK → " + fkm[c["ten"]])
                out.append(f"| {c['ten']} | {c['kieu']} | {', '.join(khoa)} | {'Có' if c['null'] else 'Không'} | {rang_buoc_cot(b, c)} | {y_nghia(c['ten'], t)} |")
            ck_bang = [x for x in b["ck"] if not any(re.search(r"\b" + c["ten"] + r"\b", x) for c in b["cot"])]
            if ck_bang:
                out.append("\nRàng buộc bảng: " + "; ".join(ck_bang))
    io.open(os.path.join(RA, "tu-dien-du-lieu.md"), "w", encoding="utf-8", newline="\n").write("\n".join(out) + "\n")


def luoc_do(bang):
    out = ["# Lược đồ quan hệ\n",
           "Chuyển từ sơ đồ lớp thiết kế theo quy tắc mục 6.3: mỗi lớp thành một bảng; quan hệ 1–n đặt khoá ngoại ở phía nhiều; "
           "quan hệ n–n tách bảng trung gian (`TaiKhoanVaiTro`, `KhuyenMaiSanPham`, `ChiTietPhieuXuat`, `ChiTietDoiTra`); "
           "lớp kết hợp `ChiTietDonHang`, `ChiTietPhieuNhap`, `ChiTietDonDatHangNCC` thành bảng có khoá riêng hoặc khoá ghép. "
           "Gạch dưới là khoá chính, `#` là khoá ngoại.\n"]
    for g, (ten_goi, ds) in GOI.items():
        out.append(f"\n## {g} · {ten_goi}\n")
        for t in ds + [x for x in bang if x not in GOI_CUA and g == "G1" and x in ("TaiKhoanVaiTro",)] + \
                [x for x in ("KhuyenMaiSanPham",) if g == "G2"] + [x for x in ("ChiTietPhieuXuat",) if g == "G5"] + \
                [x for x in ("ChiTietDoiTra",) if g == "G4"]:
            b = bang[t]
            fkm = {f[0] for f in b["fk"]}
            cs = []
            for c in b["cot"]:
                ten = c["ten"]
                if ten in b["pk"]:
                    ten = f"<u>{ten}</u>"
                if c["ten"] in fkm:
                    ten = "#" + ten
                cs.append(ten)
            out.append(f"- **{t}**({', '.join(cs)})")
    io.open(os.path.join(RA, "luoc-do-quan-he.md"), "w", encoding="utf-8", newline="\n").write("\n".join(out) + "\n")


DAU_PUML = """skinparam defaultFontName "Times New Roman"
skinparam defaultFontSize 15
skinparam monochrome true
skinparam shadowing false
skinparam dpi 170
skinparam classAttributeIconSize 0
hide circle
"""


def csdl_puml(bang):
    for g, (ten_goi, ds) in GOI.items():
        ngoai = set()
        dong = []
        bo_sung = {"G1": ["TaiKhoanVaiTro"], "G2": ["KhuyenMaiSanPham"], "G4": ["ChiTietDoiTra"], "G5": ["ChiTietPhieuXuat"]}.get(g, [])
        tat_ca = ds + bo_sung
        for t in tat_ca:
            b = bang[t]
            fkm = {f[0]: f[1] for f in b["fk"]}
            dong.append(f'entity {t} {{')
            for c in b["cot"]:
                if c["ten"] in b["pk"]:
                    dong.append(f"  * {c['ten']} : {c['kieu']} <<PK>>" + (" <<FK>>" if c["ten"] in fkm else ""))
            dong.append("  --")
            for c in b["cot"]:
                if c["ten"] in b["pk"]:
                    continue
                dong.append(f"  {'*' if not c['null'] else ''}{c['ten']} : {c['kieu']}" + (" <<FK>>" if c["ten"] in fkm else ""))
            dong.append("}")
            for f in b["fk"]:
                if f[1] not in tat_ca:
                    ngoai.add(f[1])
        for t in sorted(ngoai):
            dong.append(f"entity {t} <<{GOI_CUA.get(t, '')}>> {{\n  * {bang[t]['pk'][0]} <<PK>>\n}}")
        for t in tat_ca:
            b = bang[t]
            cot_null = {c["ten"]: c["null"] for c in b["cot"]}
            for f in b["fk"]:
                trai = "|o" if cot_null[f[0]] else "||"
                dong.append(f"{f[1]} {trai}--o{{ {t}")
        text = "@startuml csdl-" + g.lower() + "\n!pragma layout smetana\n" + DAU_PUML + "skinparam defaultFontSize 14\n" + \
               "\n".join(dong) + "\n@enduml\n"
        io.open(os.path.join(SD, f"csdl-{g.lower()}.puml"), "w", encoding="utf-8", newline="\n").write(text)


def kieu_hien(t):
    if t.startswith("M2O:") or t.startswith("O2O:"):
        return t.split(":")[1]
    if t.startswith("O2M:"):
        return "List<" + t.split(":")[1] + ">"
    if t.startswith("M2M:"):
        return "Set<" + t.split(":")[1] + ">"
    return KIEU_JAVA.get(t, t)


def lop_puml(bang, thiet_ke):
    E = {ten: (ids, truong) for ten, ids, truong in stt.E}
    for g, (ten_goi, ds) in GOI.items():
        dong, ngoai, lien_ket = [], set(), []
        for ten in ds:
            ids, truong = E[ten]
            dong.append(f"class {ten} {{")
            for f, t, k in ids:
                if k in ("auto", "id"):
                    dong.append(f"  {'- ' if thiet_ke else ''}{f}{' : ' + kieu_hien(t) if thiet_ke else ''}")
            for f, t, o in truong:
                if t.startswith(("M2O:", "O2O:", "O2M:", "M2M:")):
                    continue
                dong.append(f"  {'- ' if thiet_ke else ''}{f}{' : ' + kieu_hien(t) if thiet_ke else ''}")
            if ten == "DonHang":
                dong.append(f"  {'- ' if thiet_ke else ''}/soTienConPhaiThu{' : long' if thiet_ke else ''}")
            if ten == "SanPhamSerial":
                dong.append(f"  {'- ' if thiet_ke else ''}/hanBaoHanh{' : LocalDate' if thiet_ke else ''}")
            dong.append("  --")
            for p, ts, kq in PHUONG_THUC.get(ten, []):
                dong.append(f"  {'+ ' if thiet_ke else ''}{p}({ts if thiet_ke else ''}){' : ' + kq if thiet_ke else ''}")
            dong.append("}")
            for f, t, k in ids:
                if k == "idrel":
                    lien_ket.append((ten, t.split(":")[1], "*", "1", f))
            for f, t, o in truong:
                if t.startswith(("M2O:", "O2O:")):
                    dich, cot = t.split(":")[1], t.split(":")[2]
                    cn = next((c["null"] for c in bang[ten]["cot"] if c["ten"] == cot), True)
                    mot = "0..1" if cn else "1"
                    lien_ket.append((ten, dich, "0..1" if t.startswith("O2O:") else "*", mot, f))
                elif t.startswith("M2M:"):
                    lien_ket.append((ten, t.split(":")[1], "*", "*", f))
        for a, b2, ba, bb, f in lien_ket:
            if b2 not in ds:
                ngoai.add(b2)
        for t in sorted(ngoai):
            dong.append(f"class {t} <<{GOI_CUA.get(t)}>>")
        da = set()
        for a, b2, ba, bb, f in lien_ket:
            if (a, b2, f) in da:
                continue
            da.add((a, b2, f))
            thanh_phan = (a, b2) in {("ChiTietDonHang", "DonHang"), ("ChiTietPhieuNhap", "PhieuNhap"), ("ThongSoKyThuat", "SanPham"),
                                     ("ChiTietDonDatHangNCC", "DonDatHangNCC"), ("LichSuTrangThaiDon", "DonHang")}
            mui = "*--" if thanh_phan else "--"
            nhan = f" : {f}" if (thiet_ke or f not in (b2[0].lower() + b2[1:],)) else ""
            dong.append(f'{b2} "{bb}" {mui} "{ba}" {a}{nhan}')
        ten_file = f"lop-{'tk' if thiet_ke else 'pt'}-{g.lower()}"
        text = f"@startuml {ten_file}\n!pragma layout smetana\n" + DAU_PUML + \
               "\n".join(dong) + "\n@enduml\n"
        io.open(os.path.join(SD, ten_file + ".puml"), "w", encoding="utf-8", newline="\n").write(text)


def lop_tong(bang):
    E = {ten: (ids, truong) for ten, ids, truong in stt.E}
    dong = []
    for g, (ten_goi, ds) in GOI.items():
        dong.append(f'package "{g} · {ten_goi}" {{')
        for ten in ds:
            dong.append(f"  class {ten}")
        dong.append("}")
    da = set()
    for ten, (ids, truong) in E.items():
        for f, t, o in truong + [(f, t, {}) for f, t, k in ids if k == "idrel"]:
            if t.startswith(("M2O:", "O2O:", "M2M:")):
                dich = t.split(":")[1]
                if dich == "NhanVien" and ten != "TaiKhoan":
                    continue
                k = (ten, dich, f)
                if k in da:
                    continue
                da.add(k)
                if t.startswith("M2M:"):
                    dong.append(f'{dich} "*" -- "*" {ten}')
                elif t.startswith("O2O:"):
                    dong.append(f'{dich} "1" -- "0..1" {ten}')
                else:
                    dong.append(f'{dich} "1" -- "*" {ten}')
    text = "@startuml lop-tong-hop\n!pragma layout smetana\n" + DAU_PUML + "skinparam defaultFontSize 14\nhide members\n" + \
           "\n".join(dong) + "\n@enduml\n"
    io.open(os.path.join(SD, "lop-tong-hop.puml"), "w", encoding="utf-8", newline="\n").write(text)


Y_NGHIA_LOP = {
    "VaiTro": "Vai trò trên website (khách hàng, nhân viên bán hàng, thủ kho, nhân viên giao hàng – kỹ thuật, quản lý cửa hàng, kế toán, quản trị hệ thống).",
    "TaiKhoan": "Tài khoản đăng nhập của nhân viên hoặc khách hàng; một tài khoản kiêm được nhiều vai.",
    "NhanVien": "Người làm việc trong cửa hàng giả định; ghi người thực hiện từng nghiệp vụ.",
    "KhachHang": "Khách đã đăng ký tài khoản. Khách vãng lai không có bản ghi này.",
    "DiaChiGiaoHang": "Sổ địa chỉ của khách hàng đã đăng ký.",
    "DanhMuc": "Nhóm hàng: bếp điện từ, bếp hồng ngoại, máy hút mùi, máy rửa chén, lò nướng, lò vi sóng.",
    "ThuongHieu": "Hãng sản xuất.",
    "SanPham": "Một mẫu máy (thông tin, giá, thời hạn bảo hành). Khác với chiếc máy cụ thể `SanPhamSerial`.",
    "ThongSoKyThuat": "Một dòng thông số của mẫu máy (công suất, kích thước khoét đá…), dùng để lọc và so sánh.",
    "LichSuGia": "Mỗi lần đổi giá: ai đổi, giá cũ, giá mới, lý do, có dưới giá nhập không (YC-SP-09, YC-HT-05).",
    "KhuyenMai": "Chương trình giảm theo sản phẩm hoặc mã giảm giá; mỗi đơn áp tối đa một mã.",
    "DanhGia": "Đánh giá của khách đã nhận hàng cho một mẫu máy trong một đơn.",
    "DonHang": "Một lần mua của khách (kể cả khách vãng lai), mang trạng thái đơn và trạng thái thanh toán.",
    "ChiTietDonHang": "Một dòng mẫu máy trong đơn, giữ đơn giá và tên lúc bán.",
    "GiaoDichThanhToan": "Một lần thu cọc, thu tiền hoặc hoàn tiền qua cổng thanh toán hoặc tại cửa hàng.",
    "LichSuTrangThaiDon": "Mỗi lần đơn đổi trạng thái: từ, sang, ai, lúc nào.",
    "PhieuGiaoHang": "Lịch giao, lắp hoặc khảo sát cho một đơn: người phụ trách hoặc đơn vị vận chuyển, ngày giờ hẹn, kết quả, tiền thu khi giao.",
    "PhieuLapDat": "Biên bản lắp đặt: kết quả chạy thử, phát sinh, phí, chữ ký khách.",
    "YeuCauDoiTra": "Yêu cầu đổi máy hoặc trả hàng của khách, người duyệt, số tiền hoàn.",
    "NhaCungCap": "Hãng hoặc nhà phân phối bán hàng cho cửa hàng.",
    "DonDatHangNCC": "Đề xuất và đơn đặt hàng gửi nhà cung cấp.",
    "ChiTietDonDatHangNCC": "Một dòng mẫu máy và số lượng trong đơn đặt hàng.",
    "PhieuNhap": "Phiếu nhập kho theo mẫu 01-VT.",
    "ChiTietPhieuNhap": "Một dòng mẫu máy trong phiếu nhập: số theo chứng từ, số thực nhập, đơn giá nhập.",
    "SanPhamSerial": "Một chiếc máy cụ thể theo số serial; vòng đời từ nhập kho tới bán, bảo hành, trả về.",
    "PhieuXuat": "Phiếu xuất kho cho một đơn, ghi đúng những chiếc máy đã xuất.",
    "PhieuBaoHanh": "Phiếu tiếp nhận bảo hành một chiếc máy, theo dõi gửi hãng và trả khách.",
}


def mo_ta_lop():
    E = {ten: (ids, truong) for ten, ids, truong in stt.E}
    out = ["# Bảng mô tả lớp\n", "Sinh từ cùng nguồn với lớp thực thể của website, nên luôn khớp sơ đồ lớp và CSDL. "
           "Thuộc tính tham chiếu (mối kết hợp) ghi ở cột Kiểu bằng tên lớp.\n"]
    for g, (ten_goi, ds) in GOI.items():
        out.append(f"\n## {g} · {ten_goi}\n")
        for ten in ds:
            ids, truong = E[ten]
            out.append(f"\n### {ten}\n\n{Y_NGHIA_LOP[ten]}\n\n| Thuộc tính | Kiểu | Ý nghĩa |\n|---|---|---|")
            for f, t, k in ids:
                out.append(f"| {f} | {kieu_hien(t)} | {y_nghia(f if k != 'idrel' else t.split(':')[2], ten) if k != 'idrel' else 'Thuộc khoá: ' + kieu_hien(t)} |")
            for f, t, o in truong:
                if t.startswith(("M2O:", "O2O:")):
                    yn = y_nghia(t.split(":")[2], ten)
                elif t.startswith(("O2M:", "M2M:")):
                    yn = "Danh sách " + kieu_hien(t).split("<")[1].rstrip(">") + " liên quan"
                else:
                    yn = y_nghia(f, ten)
                out.append(f"| {f} | {kieu_hien(t)} | {yn} |")
            pts = PHUONG_THUC.get(ten, [])
            if pts:
                out.append("\nPhương thức: " + "; ".join(f"`{p}({a})` → {k}" for p, a, k in pts) + ".")
    io.open(os.path.join(RA, "mo-ta-lop.md"), "w", encoding="utf-8", newline="\n").write("\n".join(out) + "\n")


def main():
    os.makedirs(SD, exist_ok=True)
    bang = doc_schema()
    print("so bang", len(bang))
    mo_ta_lop()
    tu_dien(bang)
    luoc_do(bang)
    csdl_puml(bang)
    lop_puml(bang, False)
    lop_puml(bang, True)
    lop_tong(bang)


if __name__ == "__main__":
    main()
