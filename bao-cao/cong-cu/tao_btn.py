import os
import re
import sys

sys.stdout.reconfigure(encoding="utf-8")
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from md_docx import (RONG_TRANG_CM, WD_ALIGN_PARAGRAPH, DemHinh, Pt, bang, chia_muc, doan, doc_md, gach_dau, hinh, khoi,
                     muc_luc, ngat_trang, tao_tai_lieu, tieu_de, trang_ngang, ve_khoi)

REPO = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
PT = os.path.join(REPO, "phan-tich")
CH = os.path.join(PT, "chung")
SD = os.path.join(CH, "so-do")
TK = os.path.join(REPO, "thiet-ke")
RA = sys.argv[1]
GOI = ["g1", "g2", "g3", "g4", "g5"]
RONG_TOI_DA_DOC = 2150


def lay_bang(ten_tep, dau_muc, thu_tu=0):
    text = doc_md(ten_tep)
    for cap in ("## ", "### "):
        for ten, dong in chia_muc(text, cap):
            if ten.startswith(dau_muc):
                bs = [nd for loai, nd in khoi(dong) if loai == "table"]
                return bs[thu_tu]
    raise KeyError(dau_muc)


def sach(s):
    s = re.sub(r"`([^`]*)`", r"\1", s)
    return re.sub(r"\[([^\]]+)\]\([^)]+\)", r"\1", s)


def ten_puml(dong):
    m = re.match(r'^\s*(actor|boundary|participant|entity|control)\s+"([^"]+)"', dong)
    if not m:
        return None
    t = re.sub(r"<\$[^>]+>", "", m.group(2))
    t = t.replace("\\n", " ").replace("__", "").replace(":", "").strip()
    return re.sub(r"\s+", " ", t)


def luong_hd(puml):
    ds = []
    for l in doc_md(puml).split("\n"):
        m = re.match(r"^\s*\|([^|]+)\|\s*$", l)
        if m and m.group(1).strip() not in ds:
            ds.append(m.group(1).strip())
        m = re.match(r'^\s*partition\s+"?([^"{]+)"?', l)
        if m and m.group(1).strip() not in ds:
            ds.append(m.group(1).strip())
    return ds


def doi_tuong_tt(puml):
    ds = []
    for l in doc_md(puml).split("\n"):
        t = ten_puml(l)
        if t and t not in ds:
            ds.append(t)
    return ds


def loai_hinh(src):
    b = os.path.basename(src)
    if b.startswith("doi-tuong"):
        return "dt"
    if b.startswith("hd-"):
        return "hd"
    if b.startswith("tt-"):
        return "tt"
    return "khac"


def la_dong_so_do(k):
    if k[0] != "p":
        return False
    return k[1].lstrip("*").strip().startswith("Sơ đồ")


def doc_buc():
    kq = {}
    for g in GOI:
        p = os.path.join(PT, g, "dac-ta-nghiep-vu.md")
        goc = os.path.dirname(p)
        for ten, dong in chia_muc(doc_md(p)):
            m = re.match(r"(BUC-\d\d)\s*[:·.-]?\s*(.*)", ten)
            if not m:
                continue
            than, hinh_ds, giai_thich = [], [], None
            for k in khoi(dong):
                if k[0] == "img":
                    hinh_ds.append((loai_hinh(k[1][1]), os.path.normpath(os.path.join(goc, k[1][1])), giai_thich))
                    giai_thich = None
                elif la_dong_so_do(k):
                    giai_thich = k[1]
                else:
                    than.append(k)
            kq[m.group(1)] = {"ten": m.group(2).strip(), "goi": g, "than": than, "hinh": hinh_ds}
    return dict(sorted(kq.items()))


def doc_suc():
    kq = []
    for g in GOI:
        p = os.path.join(PT, g, "dac-ta-use-case.md")
        for ten, dong in chia_muc(doc_md(p)):
            m = re.match(r"(UC-[A-Z]{2}-\d\d)\s*[:·.-]?\s*(.*)", ten)
            if not m:
                continue
            ks = [k for k in khoi(dong) if k[0] != "img" and not la_dong_so_do(k)]
            kq.append((m.group(1), m.group(2).strip(), g, ks))
    return kq


def hinh_phan_he(g):
    thu_muc = os.path.join(PT, g, "so-do")
    for f in sorted(os.listdir(thu_muc)):
        if f.startswith("uc-phan-he") and f.endswith(".png"):
            return os.path.join(thu_muc, f)
    return None


def bang_buc(buc):
    for loai, nd in buc["than"]:
        if loai == "table" and nd and nd[0][0].lower() == "mục":
            return {r[0]: r[1] for r in nd[1:] if len(r) > 1}
    return {}


def hinh_tu_dong(d, duong, chu_thich, dem, cao_toi_da_cm=22.0):
    from PIL import Image
    w, h = Image.open(duong).size
    if w > RONG_TOI_DA_DOC and w > h:
        trang_ngang(d, lambda: hinh(d, duong, chu_thich, dem, rong_cm=24.5, cao_toi_da_cm=15.5))
    else:
        hinh(d, duong, chu_thich, dem, cao_toi_da_cm=cao_toi_da_cm)


DOI_CHUONG = {"5": "9", "6": "10", "7": "11"}
CHU_THICH = re.compile(r"^\*?(Hình|Bảng) \d+\.\d+[:.]\s*(.*?)\*?$")


def doi_so(text):
    return re.sub(r"\b(chương|Chương|mục|Mục|Bảng) ([567])\b", lambda m: f"{m.group(1)} {DOI_CHUONG[m.group(2)]}", text)


def ve_than_md(d, ds, goc, so, dem):
    i = 0
    while i < len(ds):
        loai, nd = ds[i]
        if loai == "img":
            ct = nd[0]
            if i + 1 < len(ds) and ds[i + 1][0] == "p":
                m = CHU_THICH.match(ds[i + 1][1].strip())
                if m and m.group(1) == "Hình":
                    ct = m.group(2).strip()
                    i += 1
            p = os.path.normpath(os.path.join(goc, nd[1]))
            if os.path.exists(p):
                hinh_tu_dong(d, p, doi_so(ct), dem)
            else:
                doan(d, f"(Thiếu ảnh {nd[1]})", nghieng=True, co=11)
        elif loai == "h3":
            if re.match(r"^\d+\.\d+", nd):
                tieu_de(d, re.sub(r"^\d+\.", f"{so}.", nd), 3)
            else:
                doan(d, nd, dam=True)
        elif loai == "p":
            if CHU_THICH.match(nd.strip()) and nd.strip().startswith("*Hình"):
                pass
            else:
                ve_khoi(d, [(loai, doi_so(nd))])
        elif loai == "table":
            ve_khoi(d, [(loai, [[doi_so(c) for c in r] for r in nd])])
        elif loai in ("ul",):
            ve_khoi(d, [(loai, doi_so(nd))])
        else:
            ve_khoi(d, [(loai, nd)])
        i += 1


def ve_chuong_md(d, duong, so, dem, bo_muc=(), bo_h3=()):
    goc = os.path.dirname(duong)
    text = doc_md(duong)
    dau = text.split("\n## ", 1)[0].split("\n")
    if dau and dau[0].startswith("# "):
        dau = dau[1:]
    ve_than_md(d, khoi(dau), goc, so, dem)
    stt = 0
    for ten, dong in chia_muc(text):
        if any(ten.startswith(b) for b in bo_muc):
            continue
        stt += 1
        if re.match(r"^\d+\.\d+", ten):
            ten_moi = re.sub(r"^\d+\.(\d+)", lambda m: f"{so}.{m.group(1)}", ten)
        else:
            ten_moi = f"{so}.{stt} {ten}"
        tieu_de(d, ten_moi, 2)
        giu = []
        bo = False
        for l in dong:
            if l.startswith("### "):
                bo = any(l[4:].strip().startswith(h) for h in bo_h3)
            if not bo:
                giu.append(l)
        ve_than_md(d, khoi(giu), goc, so, dem)


THANH_VIEN = [
    ("G1", "Tổng quan, tích hợp và quản trị", "Phạm Tuấn Kiệt (nhóm trưởng)", "2001240237", "BUC-07", "UC-HT-01 đến UC-HT-06"),
    ("G2", "Sản phẩm và khuyến mãi", "Lê Đạt Tuấn Kiệt", "2001240229", "BUC-01", "UC-SP-01 đến UC-SP-07"),
    ("G3", "Mua hàng trực tuyến và thanh toán", "Huỳnh Văn Lân", "2001240244", "BUC-02", "UC-MH-01 đến UC-MH-08"),
    ("G4", "Xử lý đơn, giao hàng – lắp đặt và đổi trả", "Võ Trí Thức", "2001240486", "BUC-03, BUC-04, BUC-08", "UC-DH-01 đến UC-DH-06"),
    ("G5", "Kho, nhà cung cấp và bảo hành", "Nguyễn Đăng Khoa", "2001240205", "BUC-05, BUC-06, BUC-09", "UC-KB-01 đến UC-KB-08"),
]
TEN_PHAN_HE = {"g1": "Quản trị hệ thống và báo cáo", "g2": "Sản phẩm và khuyến mãi", "g3": "Mua hàng trực tuyến và thanh toán",
               "g4": "Xử lý đơn, giao hàng, lắp đặt, đổi trả", "g5": "Kho, nhà cung cấp, bảo hành"}

THUAT_NGU_THEM = [
    ("Đơn hàng", "Bản ghi một lần mua của khách: người nhận, số điện thoại, địa chỉ giao, các mẫu máy và số lượng, có lắp đặt hay không, cách trả tiền, trạng thái. Mỗi đơn có mã dạng DH000012."),
    ("Đặt cọc", "Khoản khách trả trước cho đơn có tiền hàng trên 10 triệu đồng (vai mô phỏng xin 10–20%, website dùng 10%); hoàn hoặc trừ theo quy tắc khi khách huỷ."),
    ("Phiếu xuất kho", "Giấy thủ kho lập khi soạn hàng giao cho một đơn: số phiếu, ngày, người nhận, địa chỉ giao, mẫu máy và số serial từng chiếc."),
    ("Phiếu nhập kho", "Giấy thủ kho lập khi nhận hàng từ nhà cung cấp: số lượng theo chứng từ và thực nhập, số serial từng chiếc (mẫu 01-VT)."),
    ("Phiếu giao hàng", "Giấy đi kèm máy khi giao, khách ký nhận một liên."),
    ("Hoá đơn bán hàng", "Chứng từ bán hàng lập khi giao hàng, kể cả khi khách chưa trả tiền."),
    ("Phiếu đổi trả", "Giấy ghi yêu cầu đổi hoặc trả máy: số điện thoại, mã đơn, lý do, tình trạng máy, hướng xử lý, người duyệt."),
    ("Đơn vị vận chuyển", "Bên nhận chở hàng đi tỉnh và thu tiền hộ; đối soát tiền thu hộ với cửa hàng sau 3–5 ngày."),
    ("Cổng thanh toán", "Dịch vụ bên ngoài xử lý thanh toán trực tuyến và trả kết quả thành công hoặc từ chối. Bản chạy thử dùng trang cổng mô phỏng."),
    ("Khách hàng (trên website)", "Người đã đăng ký tài khoản; kế thừa mọi việc của khách vãng lai, thêm quản lý hồ sơ, sổ địa chỉ, xem lại đơn cũ, đánh giá sản phẩm."),
]

HE_THONG_HO_TRO = [
    ["S-01", "Website bán hàng điện gia dụng", "Hệ thống được xây dựng", "Giao diện khách và giao diện quản trị, xử lý nghiệp vụ, cơ sở dữ liệu. Bản chạy thử viết bằng Spring Boot + MySQL (mục 12).", "35 use case hệ thống ở mục 4.2"],
    ["S-02", "Cổng thanh toán", "Dịch vụ bên ngoài", "Nhận yêu cầu thanh toán trực tuyến, trả kết quả thành công hoặc từ chối. Bản chạy thử dùng cổng mô phỏng.", "UC-MH-05"],
    ["S-03", "Phần mềm hoá đơn điện tử của cửa hàng", "Dịch vụ bên ngoài, không tích hợp trực tiếp", "Cửa hàng phát hành hoá đơn ở phần mềm này; website chỉ lưu số hoá đơn (YC-PCN-04).", "UC-MH-06, UC-DH-03"],
]

DU_KIEN = [
    ["UC-HT-07", "Quản lý trang thông tin và chính sách cửa hàng", "Quản lý cửa hàng", "YC-HT-06; luật thương mại điện tử bắt buộc công bố thông tin người bán và chính sách (TL-20 Đ11, TL-21). Bản chạy thử đã có hai trang chính sách tĩnh."],
    ["UC-HT-08", "Tiếp nhận yêu cầu về dữ liệu cá nhân", "Khách vãng lai, Quản trị hệ thống", "YC-HT-07 (TL-24, TL-25)"],
    ["UC-HT-09", "Gửi và xử lý khiếu nại", "Khách vãng lai, Nhân viên bán hàng", "YC-HT-08 (TL-22 Đ31, TL-23 Đ22)"],
    ["UC-DH-07", "Đặt lịch khảo sát lắp đặt", "Khách hàng", "Vai đại lý mô phỏng muốn có (MP3 F2); bản chạy thử để quản lý xếp loại việc Khảo sát trong UC-DH-02"],
]

GIA_DINH = [
    "Đối tượng phân tích là **cửa hàng giả định**, ghép từ ba vai người bán do trí tuệ nhân tạo đóng (MP1, MP2, MP3) và 35 tài liệu công khai của ngành (TL). Nhóm không phỏng vấn cửa hàng thật và không phát bảng hỏi (quyết định ngày 14/10/2026). Mọi chi tiết lấy từ mô phỏng ghi mã MP, MK.",
    "Mức cụ thể không có trong nguồn được nhóm chọn cho bản chạy thử: cọc 10% với đơn có tiền hàng trên 10 triệu đồng (vai mô phỏng xin 10–20%); phí lắp 250.000 đ với đơn nội thành không quá 5 triệu (vai mô phỏng thu 200–300 nghìn); phí khấu trừ 10% khi khách đổi ý trả hàng; mức tồn tối thiểu 2 máy. Tất cả là mức chủ cửa hàng quy định, sửa được.",
    "Chủ cửa hàng duyệt giá, khuyến mãi, đơn đặt hàng nhà cung cấp và đổi trả ngoại lệ từ bên ngoài hệ thống nghiệp vụ: quản lý cửa hàng xin ý kiến, chủ trả lời. Trên website chủ dùng tài khoản vai Quản lý cửa hàng.",
    "Nhân viên đã có tài khoản và đã đăng nhập trước khi thực hiện các use case hệ thống phía cửa hàng; sơ đồ không vẽ include Đăng nhập.",
    "Ngoài phạm vi: bán và đồng bộ giá, tồn kho lên sàn thương mại điện tử, mạng xã hội; bán theo dự án qua kiến trúc sư; chấm công, tính lương; kế toán thuế; phát hành hoá đơn điện tử; phí giao tỉnh.",
    "Thanh toán trực tuyến trong bản chạy thử là cổng mô phỏng, không kết nối ngân hàng thật; email xác nhận chỉ hiện thông báo, không gửi thật.",
]

CAN_XAC_NHAN = [
    ["Q-01", "Giảng viên có chấp nhận cửa hàng giả định dựng từ tài liệu công khai và phỏng vấn mô phỏng không? Tiêu chí Tính thực tế và khả thi chấm phần này thế nào?", "Mục 1, 2, 5.1; tiêu chí 20% điểm bài nhóm"],
    ["Q-02", "Công cụ vẽ bắt buộc (Rational Rose như học phần thực hành, hay được dùng PlantUML, StarUML, draw.io)? Có phải nộp kèm tệp mô hình không?", "Mục 8–11"],
    ["Q-03", "Chủ cửa hàng có được làm tác nhân nghiệp vụ không (bài giảng C3-22: tác nhân không điều khiển hoạt động của hệ thống)?", "BUC-07; BUC-01, BUC-04, BUC-05 nếu nối thêm chủ"],
    ["Q-04", "Thừa tác viên giao tiếp với tác nhân vẽ hình biên như C3-47, C3-66 hay dùng chung một hình như C3-51, C3-72?", "Mục 8.2, 8.4"],
    ["Q-05", "Mỗi use case nghiệp vụ có cần đủ cả sơ đồ đối tượng nghiệp vụ, sơ đồ hoạt động và sơ đồ tuần tự nghiệp vụ không?", "Mục 8"],
    ["Q-06", "Có cần nộp bản chạy thử không, và nếu có thì chấm phần nào (đề cương học phần 3(3,0) không yêu cầu viết chương trình)?", "Mục 12"],
    ["Q-07", "Hạn nộp, hình thức nộp, có thuyết trình hay vấn đáp không?", "Kế hoạch nhóm"],
]

THONG_TIN_THUC_THE = {
    "Thông tin mẫu máy (thông số, ảnh, giá)": "Tên, model, hãng, nhóm hàng, thông số (công suất, kích thước khoét đá…), ảnh, giá bán, thời hạn bảo hành, có cần lắp đặt",
    "Chiếc máy (có số serial)": "Số serial, mẫu máy, ngày nhập, ngày bán, trạng thái",
    "Bảng giá và khuyến mãi": "Giá bán từng mẫu; chương trình khuyến mãi, mức giảm, quà tặng, thời gian áp dụng",
    "Đơn hàng": "Mã đơn, ngày đặt, người nhận, số điện thoại, địa chỉ giao, mẫu máy, số lượng, đơn giá, có lắp đặt, cách trả tiền, tiền cọc, trạng thái",
    "Hoá đơn bán hàng": "Số, ngày, tên khách, địa chỉ, tên hàng, số lượng, đơn giá, thành tiền",
    "Phiếu bảo hành": "Tên khách, mẫu máy, số serial, ngày mua, thời hạn bảo hành",
    "Phiếu xuất kho": "Số phiếu, ngày xuất, người nhận, địa chỉ giao, tên hàng, model, serial, số lượng, chữ ký thủ kho, người giao, người nhận",
    "Phiếu giao hàng": "Mã đơn, khách, địa chỉ, hàng giao, chữ ký người nhận (2 liên)",
    "Biên bản lắp đặt": "Mã đơn, khách, địa chỉ công trình, máy và serial, kết quả chạy thử, phát sinh, chữ ký khách và kỹ thuật viên",
    "Sổ nộp tiền": "Ngày, người nộp, đơn hàng, số tiền thu khi giao, người nhận",
    "Phiếu đổi trả": "Số điện thoại, mã đơn, lý do, tình trạng máy, hướng xử lý, người duyệt",
    "Đơn đặt hàng nhà cung cấp": "Nhà cung cấp, mẫu máy, số lượng, ngày đặt, người duyệt",
    "Phiếu nhập kho": "Số phiếu, ngày nhập, nhà cung cấp, số hoá đơn, tên hàng, model, serial, số lượng theo chứng từ và thực nhập, tình trạng, chữ ký",
    "Phiếu tiếp nhận bảo hành": "Số phiếu, ngày nhận, khách, máy, model, serial, ngày mua, mô tả lỗi, tình trạng bên ngoài, ngày hẹn trả, chữ ký hai bên",
    "Báo cáo kinh doanh": "Doanh thu theo nhóm hàng, hàng bán chạy, tồn kho theo mẫu, máy đang bảo hành, khoản lệch tiền, đề xuất nhập hàng và khuyến mãi",
}


def phan_dau(d):
    doan(d, "BÀI TẬP NHÓM · PHÂN TÍCH THIẾT KẾ HỆ THỐNG THÔNG TIN", WD_ALIGN_PARAGRAPH.CENTER, 12, dam=True, sau=2)
    doan(d, "Website bán hàng điện gia dụng", WD_ALIGN_PARAGRAPH.CENTER, 24, dam=True, sau=10)
    bang(d, ["Mục", "Thông tin"], [
        ["Học phần", "Phân tích thiết kế hệ thống thông tin (0101003615)"],
        ["Lớp", "15DHTH06 (mã lớp học phần 010100361506)"],
        ["Giảng viên hướng dẫn", "Nguyễn Thị Thúy A"],
        ["Nhóm", "Nhóm 4"],
        ["Mã nguồn", "github.com/kietpham2309pt-debug/da_pttkhttt"],
        ["Phiên bản", "1.0, ngày 14/11/2026"],
    ], rong=[1, 2.6], co=12)
    bang(d, ["Gói", "Phân hệ", "Thành viên", "MSSV"], [[t[0], t[1], t[2], t[3]] for t in THANH_VIEN], rong=[0.6, 2.6, 2.2, 1.2], co=12)
    doan(d, "Đối tượng phân tích là một **cửa hàng giả định** dựng từ tài liệu công khai của ngành và phỏng vấn mô phỏng; xem mục 1.1 và mục 13.1.",
         WD_ALIGN_PARAGRAPH.CENTER, 11.5, nghieng=True)
    ngat_trang(d)
    doan(d, "Mục lục", WD_ALIGN_PARAGRAPH.CENTER, 16, dam=True)
    muc_luc(d)
    ngat_trang(d)
    tieu_de(d, "Quy ước đánh mã", 1)
    bang(d, ["Tiền tố", "Ý nghĩa", "Ví dụ"], [
        ["A-", "Tác nhân nghiệp vụ (business actor), đứng ngoài cửa hàng", "A-01"],
        ["W-", "Thừa tác viên (business worker), người thực hiện nghiệp vụ trong cửa hàng", "W-01"],
        ["S-", "Hệ thống hoặc dịch vụ hỗ trợ", "S-01"],
        ["BUC-", "Use case nghiệp vụ", "BUC-01"],
        ["QT-", "Quy trình nghiệp vụ ứng với use case nghiệp vụ", "QT-03"],
        ["UC-HT, UC-SP, UC-MH, UC-DH, UC-KB", "Use case hệ thống theo 5 phân hệ (gói G1 đến G5)", "UC-MH-04"],
        ["BR-", "Quy tắc nghiệp vụ (business rule)", "BR-01"],
        ["YC-", "Yêu cầu chức năng, phi chức năng, chuyển đổi (xác định ở chương 2)", "YC-MH-01"],
        ["TL-", "Tài liệu công khai đã phân tích (dữ kiện thật, có đường dẫn)", "TL-20"],
        ["MP1, MP2, MP3", "Lời ba vai người bán trong phỏng vấn mô phỏng, kèm mã câu", "MP2 C4"],
        ["MK", "Phiếu bảng hỏi của 10 vai khách mô phỏng, kèm mã câu", "MK K11"],
        ["Q-", "Điểm cần xác nhận", "Q-01"],
    ], rong=[1.4, 3.4, 0.9])


def chuong_1(d):
    tieu_de(d, "1. Tổng quan nghiệp vụ", 1)
    tieu_de(d, "1.1 Đề tài và đối tượng phân tích", 2)
    doan(d, "Đề tài phân tích và thiết kế **website bán hàng cho một cửa hàng điện gia dụng**: khách xem sản phẩm, đặt hàng, thanh toán, theo dõi đơn, tra cứu bảo hành; nhân viên cửa hàng quản lý sản phẩm, giá, khuyến mãi, đơn hàng, giao hàng và lắp đặt tận nơi, đổi trả, kho theo từng chiếc máy (số serial), bảo hành và báo cáo. Nhóm đã viết thêm một **bản chạy thử** đúng theo thiết kế (mục 12).")
    doan(d, "Nhóm không thực hiện phỏng vấn tại một cửa hàng cụ thể mà khảo sát bằng hai cách. Thứ nhất, **phân tích tài liệu** công khai của ngành: chính sách bán hàng, giao lắp, đổi trả, bảo hành của ba chuỗi điện máy và một nhà bán lẻ thiết bị bếp, các văn bản pháp luật hiện hành và mẫu chứng từ kho chính thức (35 tài liệu). Thứ hai, **phỏng vấn mô phỏng**: dùng trí tuệ nhân tạo đóng vai ba kiểu người bán (tiệm gia đình, shop bán trực tuyến, đại lý có phần mềm quản lý) trả lời đúng kịch bản phỏng vấn của nhóm, và mười kiểu khách điền bảng câu hỏi. Từ đó nhóm dựng một **cửa hàng giả định** làm đối tượng phân tích; nội dung lấy từ mô phỏng ghi mã MP, MK để phân biệt với dữ kiện từ tài liệu (TL).")
    doan(d, "Cửa hàng giả định bán thiết bị bếp và đồ điện gia dụng (bếp từ, máy hút mùi, máy rửa chén, lò nướng, máy lọc nước, nồi cơm, quạt); có chủ cửa hàng và 12 người: 1 quản lý, 5 nhân viên bán hàng, 1 thủ kho, 1 kế toán, 4 nhân viên giao hàng – kỹ thuật. Khách đặt tại cửa hàng, qua điện thoại, tin nhắn và mạng xã hội; cửa hàng chưa có website riêng.")
    tieu_de(d, "1.2 Vòng đời một đơn hàng", 2)
    bang(d, ["Giai đoạn", "Use case nghiệp vụ", "Nơi diễn ra", "Kết quả chính"], [
        ["Giới thiệu", "BUC-01 Giới thiệu sản phẩm và khuyến mãi", "Cửa hàng, các kênh bán", "Thông tin mẫu máy, giá và khuyến mãi đã duyệt đến được khách"],
        ["Bán hàng", "BUC-02 Bán hàng", "Cửa hàng, điện thoại, tin nhắn", "Đơn hàng đã chốt; tiền cọc với đơn trên 10 triệu đồng"],
        ["Giao hàng", "BUC-03 Giao hàng, mở rộng bởi BUC-08 Lắp đặt tận nơi", "Nhà khách; đơn tỉnh qua đơn vị vận chuyển", "Đúng chiếc máy (serial) tới tay khách; hoá đơn, phiếu bảo hành, biên bản lắp đặt; tiền thu khi giao nộp kế toán trong ngày"],
        ["Sau bán", "BUC-04 Đổi trả hàng, BUC-06 Bảo hành sản phẩm (cùng gọi BUC-09 Kiểm tra máy theo số serial)", "Cửa hàng; trung tâm bảo hành hãng", "Máy được đổi, hoàn tiền hoặc sửa xong trả khách"],
        ["Hỗ trợ", "BUC-05 Nhập hàng", "Kho", "Máy nhập kho có serial, phiếu nhập kho"],
        ["Quản lý", "BUC-07 Lập báo cáo kinh doanh", "Nội bộ", "Báo cáo tháng kèm đề xuất nhập hàng, khuyến mãi đã duyệt"],
    ], rong=[1, 2.4, 1.7, 2.8])
    tieu_de(d, "1.3 Phân hệ và phân công", 2)
    bang(d, ["Gói", "Phân hệ", "Phụ trách", "Use case nghiệp vụ", "Use case hệ thống"],
         [[t[0], t[1], t[2], t[4], t[5]] for t in THANH_VIEN], rong=[0.5, 2, 1.7, 1.3, 1.6])
    tieu_de(d, "1.4 Vấn đề hiện tại và chiến lược", 2)
    vd = lay_bang(os.path.join(REPO, "khao-sat", "tinh-huong-gia-dinh.md"), "4.")
    bang(d, ["#", "Vấn đề của cửa hàng giả định", "Nguồn", "Gói"], [[sach(c) for c in r] for r in vd[1:]], rong=[0.3, 4, 1.6, 0.7])
    doan(d, "Chiến lược phân tích yêu cầu (C2-8): **cải tiến quy trình nghiệp vụ**. Cửa hàng giữ cách bán đang có, thêm kênh đặt hàng trên website riêng và thay các ghi chép rời rạc bằng dữ liệu chung; không tái thiết kế vì vai người bán mô phỏng ngại làm rối quy trình đang chạy (MP3 F2, MP2 F2).")


def chuong_2_3(d, dem):
    tieu_de(d, "2. Thuật ngữ nghiệp vụ", 1)
    tn = lay_bang(os.path.join(CH, "hien-trang-to-chuc.md"), "5.")
    bang(d, ["Thuật ngữ", "Diễn giải"], [[sach(r[0]), sach(r[1])] for r in tn[1:]] + [list(x) for x in THUAT_NGU_THEM], rong=[1.3, 4])
    tieu_de(d, "3. Tác nhân nghiệp vụ, thừa tác viên và hệ thống hỗ trợ", 1)
    tieu_de(d, "3.1 Tác nhân nghiệp vụ (Business Actor)", 2)
    an = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "1.")
    bang(d, ["Mã", "Tên", "Mô tả và trách nhiệm", "Use case nghiệp vụ"],
         [[f"A-{i + 1:02d}", sach(r[0]), sach(r[1]) + ". " + sach(r[2]) + ".", sach(r[3])] for i, r in enumerate(an[1:])], rong=[0.6, 1.4, 4, 1.3])
    tieu_de(d, "3.2 Thừa tác viên (Business Worker)", 2)
    loai = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "2.", 0)
    mota = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "2.", 1)
    lk = {r[0]: r for r in loai[1:]}
    bang(d, ["Mã", "Tên", "Loại", "Trách nhiệm", "Use case nghiệp vụ"],
         [[f"W-{i + 1:02d}", r[0], sach(lk[r[0]][1]) + (" (với " + sach(lk[r[0]][2]) + ")" if lk[r[0]][2] and "Không" not in lk[r[0]][2] else ""), sach(r[1]), sach(r[4])]
          for i, r in enumerate(mota[1:])], rong=[0.6, 1.4, 2, 3, 1.2])
    tieu_de(d, "3.3 Hệ thống và dịch vụ hỗ trợ", 2)
    bang(d, ["Mã", "Tên", "Loại", "Mô tả và trách nhiệm", "Use case"], HE_THONG_HO_TRO, rong=[0.6, 1.6, 1.4, 3.4, 1.2])
    tieu_de(d, "3.4 Tác nhân hệ thống", 2)
    doan(d, "Tác nhân hệ thống là người trực tiếp dùng website. Thừa tác viên ở mục 3.2 trở thành tác nhân hệ thống khi thao tác trên website; chủ cửa hàng dùng tài khoản vai Quản lý cửa hàng.")
    th = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "6.")
    bang(d, th[0], [[sach(c) for c in r] for r in th[1:]], rong=[1.4, 1.2, 4, 1])
    hinh(d, os.path.join(SD, "tac-nhan-he-thong.png"), "Sơ đồ tác nhân hệ thống và quan hệ tổng quát hoá", dem)


def chuong_4(d, dem, buc, suc):
    tieu_de(d, "4. Danh mục use case", 1)
    tieu_de(d, "4.1 Use case nghiệp vụ (Business Use Case)", 2)
    bu = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "4.")
    so_muc = {m: f"5.1.{i + 1}" for i, m in enumerate(buc)}
    bang(d, ["Mã", "Use case nghiệp vụ", "Quy trình", "Gói", "Tác nhân nghiệp vụ", "Quan hệ", "Đặc tả"],
         [[r[0], r[1], sach(r[2]), r[3], r[4], sach(r[5]), so_muc.get(r[0], "")] for r in bu[1:]], rong=[0.95, 1.5, 2.1, 0.6, 1.3, 1.5, 0.7], co=11)
    tieu_de(d, "4.2 Use case hệ thống (System Use Case)", 2)
    so_suc = {s[0]: f"5.2.{i + 1}" for i, s in enumerate(suc)}
    dong_uc = []
    for g in GOI:
        dm = lay_bang(os.path.join(CH, "danh-muc-use-case.md"), g.upper())
        for r in dm[1:]:
            dong_uc.append([r[0], sach(r[1]), sach(r[2]), TEN_PHAN_HE[g], so_suc.get(r[0], "")])
    bang(d, ["Mã", "Use case", "Tác nhân", "Phân hệ", "Đặc tả"], dong_uc, rong=[1.3, 2.6, 2.3, 1.7, 0.8], co=11)
    doan(d, f"Có {len(suc)} use case hệ thống được đặc tả chi tiết ở mục 5.2 (mỗi phân hệ 3 use case chính); tất cả 35 use case đều có trong bản chạy thử (mục 12).", nghieng=True, co=12)
    tieu_de(d, "4.3 Sơ đồ use case", 2)
    hinh(d, os.path.join(SD, "uc-nghiep-vu-tong.png"), "Sơ đồ use case nghiệp vụ tổng của cửa hàng", dem)
    hinh(d, os.path.join(SD, "uc-tong-quat-khach.png"), "Sơ đồ use case tổng quát phía khách", dem, cao_toi_da_cm=17)
    hinh(d, os.path.join(SD, "uc-tong-quat-cua-hang.png"), "Sơ đồ use case tổng quát phía cửa hàng", dem, cao_toi_da_cm=19)
    doan(d, "Sơ đồ use case chi tiết của từng phân hệ ở mục 8.1.", nghieng=True, co=12)
    tieu_de(d, "4.4 Chỗ các phân hệ nối vào nhau", 2)
    no = lay_bang(os.path.join(CH, "danh-muc-use-case.md"), "3.")
    bang(d, no[0], [[sach(c) for c in r] for r in no[1:]], rong=[1.6, 1.6, 1.8, 1.6], co=11)
    tieu_de(d, "4.5 Use case dự kiến, chưa đặc tả", 2)
    bang(d, ["Mã dự kiến", "Tên", "Tác nhân", "Ghi chú"], DU_KIEN, rong=[0.9, 2, 1.5, 3])


def chuong_5_6_7(d, dem, buc, suc):
    tieu_de(d, "5. Đặc tả use case chi tiết", 1)
    tieu_de(d, "5.1 Đặc tả use case nghiệp vụ", 2)
    doan(d, "Mỗi use case nghiệp vụ đặc tả theo Mẫu 1 của bài giảng (giới thiệu, các dòng cơ bản, các dòng thay thế), kèm bảng thông tin bổ sung. Sơ đồ đối tượng nghiệp vụ, sơ đồ hoạt động và sơ đồ tuần tự nghiệp vụ của từng use case ở mục 8.")
    for i, (ma, b) in enumerate(buc.items()):
        tieu_de(d, f"5.1.{i + 1} {ma}: {b['ten']}", 3)
        ve_khoi(d, b["than"])
    tieu_de(d, "5.2 Đặc tả use case hệ thống", 2)
    for i, (ma, ten, g, ks) in enumerate(suc):
        tieu_de(d, f"5.2.{i + 1} {ma}: {ten}", 3)
        ve_khoi(d, ks)
    tieu_de(d, "6. Quy tắc nghiệp vụ", 1)
    doan(d, "Gom từ dòng Quy tắc nghiệp vụ của các đặc tả ở mục 5.1. Mã nguồn trong ngoặc cho biết quy tắc lấy từ tài liệu thật (TL) hay từ cửa hàng giả định (MP). Bản chạy thử kiểm các quy tắc này ở các lớp nghiệp vụ (mục 11).")
    br, n = [], 0
    for ma, b in buc.items():
        qd = bang_buc(b).get("Quy tắc nghiệp vụ", "")
        for phan in re.split(r"(?:^|\s|·|<br>)QĐ\s?\d+[.:]\s*", qd):
            phan = phan.strip(" ·;").replace("<br>", " ")
            if len(phan) > 3:
                n += 1
                br.append([f"BR-{n:02d}", sach(phan), ma])
    bang(d, ["Mã", "Quy tắc", "Áp dụng"], br, rong=[0.7, 5, 0.9])
    tieu_de(d, "7. Thực thể nghiệp vụ và vòng đời trạng thái", 1)
    tieu_de(d, "7.1 Thực thể nghiệp vụ", 2)
    tt = lay_bang(os.path.join(CH, "tac-nhan-va-thuc-the.md"), "3.")
    bang(d, ["Thực thể", "Nhóm", "Thông tin chính", "Tạo ra ở · dùng ở", "Lớp tương ứng"],
         [[r[0], r[1], THONG_TIN_THUC_THE.get(r[0], ""), f"{r[2]} · {r[3]}", sach(r[4])] for r in tt[1:]], rong=[1.4, 0.8, 3, 1.3, 1.6], co=11)
    tieu_de(d, "7.2 Trạng thái đơn hàng", 2)
    dh = lay_bang(os.path.join(CH, "lop-va-trang-thai-chung.md"), "4.")
    bang(d, ["Từ", "Sang", "Ai", "Use case", "Khi nào"], [[sach(c) for c in r[:5]] for r in dh[1:]], rong=[1.1, 1.1, 2, 1.3, 2], co=11)
    for l in ["Không quay ngược trạng thái (Hoàn thành không về Đang giao); sai thì xử lý bằng đổi trả.",
              "Đơn Giao không thành công: các chiếc máy đã xuất được thủ kho nhận lại về kho (UC-KB-08). Khách chỉ hẹn giao lại thì đơn vẫn Đang giao, ghi chú vào đơn.",
              "Mỗi lần đổi trạng thái lưu một dòng lịch sử: ai đổi, lúc nào.",
              "Đổi máy hoặc trả một phần: đơn giữ Hoàn thành, việc đổi trả ghi trên yêu cầu đổi trả."]:
        gach_dau(d, l)
    hinh(d, os.path.join(TK, "so-do", "trang-thai-don-hang.png"), "Sơ đồ trạng thái đơn hàng", dem, cao_toi_da_cm=17)
    tieu_de(d, "7.3 Trạng thái chiếc máy theo số serial", 2)
    cm = lay_bang(os.path.join(CH, "lop-va-trang-thai-chung.md"), "5.")
    bang(d, ["Từ", "Sang", "Ai", "Use case", "Ghi chú"], [[sach(c) for c in r[:5]] for r in cm[1:]], rong=[1.1, 1.2, 2, 1.2, 2.4], co=11)
    hinh(d, os.path.join(TK, "so-do", "trang-thai-chiec-may.png"), "Sơ đồ trạng thái chiếc máy", dem, cao_toi_da_cm=17)


def chuong_8(d, dem, buc):
    tieu_de(d, "8. Sơ đồ nghiệp vụ và use case", 1)
    tieu_de(d, "8.1 Sơ đồ use case phân hệ", 2)
    for g in GOI:
        h = hinh_phan_he(g)
        if h:
            hinh(d, h, f"Sơ đồ use case phân hệ {TEN_PHAN_HE[g]} ({g.upper()})", dem, rong_cm=13, cao_toi_da_cm=19)
    tieu_de(d, "8.2 Sơ đồ đối tượng nghiệp vụ", 2)
    dong = []
    for ma, b in buc.items():
        bb = bang_buc(b)
        dong.append([ma, sach(bb.get("Tác nhân nghiệp vụ", "")), sach(bb.get("Thừa tác viên", "")), sach(bb.get("Thực thể nghiệp vụ", ""))])
    bang(d, ["Sơ đồ", "Tác nhân nghiệp vụ", "Thừa tác viên", "Thực thể nghiệp vụ"], dong, rong=[0.8, 1.4, 2, 3.2], co=11)
    for ma, b in buc.items():
        for lh, duong, gt in b["hinh"]:
            if lh == "dt":
                hinh(d, duong, f"Sơ đồ đối tượng nghiệp vụ {ma} {b['ten']}", dem)
    for so, lh, ten_muc, ten_hinh, lay in (("8.3", "hd", "Sơ đồ hoạt động", "Sơ đồ hoạt động", luong_hd),
                                           ("8.4", "tt", "Sơ đồ tuần tự nghiệp vụ", "Sơ đồ tuần tự nghiệp vụ", doi_tuong_tt)):
        tieu_de(d, f"{so} {ten_muc}", 2)
        dong = []
        for ma, b in buc.items():
            ds = []
            for l2, duong, gt in b["hinh"]:
                if l2 == lh:
                    p = duong[:-4] + ".puml"
                    if os.path.exists(p):
                        ds += [x for x in lay(p) if x not in ds]
            gts = [sach(gt.split(":", 1)[-1].strip()) for l2, _, gt in b["hinh"] if l2 == lh and gt]
            dong.append([ma, (" | " if lh == "hd" else ", ").join(ds), " ".join(gts)])
        bang(d, ["Sơ đồ", "Luồng (swimlane)" if lh == "hd" else "Đối tượng (lifeline)", "Điểm thể hiện"], dong, rong=[0.8, 2.4, 3.8], co=11)
        for ma, b in buc.items():
            for l2, duong, gt in b["hinh"]:
                if l2 == lh:
                    hinh(d, duong, f"{ten_hinh} {ma} {b['ten']}", dem, cao_toi_da_cm=22)


def chuong_9_10_11(d, dem):
    tieu_de(d, "9. Mô hình hoá cấu trúc", 1)
    ve_chuong_md(d, os.path.join(TK, "chuong-5-mo-hinh-cau-truc.md"), 9, dem)
    tieu_de(d, "9.5 Bảng mô tả lớp", 2)
    text = doc_md(os.path.join(TK, "mo-ta-lop.md"))
    for ten, dong in chia_muc(text, "### "):
        doan(d, ten, dam=True)
        ve_khoi(d, [k for k in khoi(dong) if k[0] != "img"], rong_bang=[1.3, 1.4, 3])
    tieu_de(d, "10. Thiết kế lớp và cơ sở dữ liệu", 1)
    ve_chuong_md(d, os.path.join(TK, "chuong-6-thiet-ke-lop-csdl.md"), 10, dem)
    tieu_de(d, "10.3 Lược đồ quan hệ", 2)
    text = doc_md(os.path.join(TK, "luoc-do-quan-he.md"))
    dau = text.split("\n## ", 1)[0].split("\n", 1)[1]
    ve_khoi(d, khoi(dau.split("\n")))
    for ten, dong in chia_muc(text):
        doan(d, ten, dam=True)
        ve_khoi(d, khoi(dong))
    tieu_de(d, "10.4 Từ điển dữ liệu", 2)
    text = doc_md(os.path.join(TK, "tu-dien-du-lieu.md"))
    for ten, dong in chia_muc(text, "### "):
        doan(d, "Bảng " + ten, dam=True)
        ve_khoi(d, khoi(dong), rong_bang=[1.5, 1.1, 1.2, 0.5, 2.2, 2.4])
    p7 = os.path.join(TK, "chuong-7-thiet-ke-chuc-nang.md")
    if os.path.exists(p7):
        tieu_de(d, "11. Thiết kế chức năng hệ thống", 1)
        ve_chuong_md(d, p7, 11, dem, bo_h3=("Danh sách ảnh màn hình",))


def chuong_12_13(d, dem):
    p = os.path.join(REPO, "website", "README.md")
    if os.path.exists(p):
        tieu_de(d, "12. Bản chạy thử", 1)
        ve_chuong_md(d, p, 12, dem)
    tieu_de(d, "13. Giả định và điểm cần xác nhận", 1)
    tieu_de(d, "13.1 Giả định", 2)
    for g in GIA_DINH:
        gach_dau(d, g)
    tieu_de(d, "13.2 Điểm cần xác nhận", 2)
    bang(d, ["Mã", "Nội dung cần xác nhận", "Ảnh hưởng"], CAN_XAC_NHAN, rong=[0.6, 4.4, 2])


def main():
    d = tao_tai_lieu()
    dem = DemHinh()
    buc = doc_buc()
    suc = doc_suc()
    print("BUC", list(buc), "SUC", len(suc))
    phan_dau(d)
    chuong_1(d)
    chuong_2_3(d, dem)
    chuong_4(d, dem, buc, suc)
    chuong_5_6_7(d, dem, buc, suc)
    chuong_8(d, dem, buc)
    chuong_9_10_11(d, dem)
    chuong_12_13(d, dem)
    d.save(RA)
    print("Da luu", RA, "so hinh", dem.so)


if __name__ == "__main__":
    main()
