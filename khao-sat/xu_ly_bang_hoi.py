import csv
import os
import re
import sys
import unicodedata
from datetime import datetime

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import PathPatch
from matplotlib.path import Path

MAU_COT = "#2a78d6"
CHU_CHINH = "#0b0b0b"
CHU_PHU = "#52514e"
CHU_MO = "#898781"
DUONG_NEN = "#c3c2b7"

CAU = [
    ("K1", "mot", "Tuổi người trả lời", True,
     ["Dưới 22", "22 đến 30", "31 đến 45", "46 đến 60", "Trên 60"]),
    ("K2", "mot", "Nơi sống", False,
     ["TP.HCM, khu vực nội thành cũ",
      "TP.HCM, khu vực ngoại thành cũ (Củ Chi, Hóc Môn, Bình Chánh, Nhà Bè, Cần Giờ)",
      "TP.HCM, khu vực Bình Dương hoặc Bà Rịa - Vũng Tàu cũ",
      "Tỉnh, thành khác"]),
    ("K3", "mot", "Mua đồ điện gia dụng để dùng ở đâu", False,
     ["Trong gia đình", "Cho quán, cửa hàng hoặc văn phòng", "Cả hai"]),
    ("K4", "mot", "Số món đồ điện gia dụng đã mua trong 12 tháng", True,
     ["Chưa mua món nào", "1 món", "2 đến 3 món", "Từ 4 món trở lên"]),
    ("K5", "mot", "Nơi mua lần gần nhất", False,
     ["Cửa hàng điện gia dụng gần nhà",
      "Siêu thị điện máy (Điện Máy Xanh, Nguyễn Kim, Chợ Lớn…)",
      "Sàn thương mại điện tử (Shopee, Lazada, Tiki, TikTok Shop)",
      "Xem livestream rồi đặt (TikTok, Facebook)",
      "Website của hãng hoặc của cửa hàng",
      "Nhắn Facebook, Zalo cho cửa hàng",
      "Người nhà mua giùm",
      "Chưa mua bao giờ"]),
    ("K6", "mot", "Đã từng tự đặt mua đồ điện gia dụng trên mạng", True,
     ["Đã từng, nhiều lần", "Đã từng, một hai lần", "Chưa từng"]),
    ("K7", "nhieu", "Điều làm ngại mua đồ điện gia dụng trên mạng", False,
     ["Sợ hàng giả, hàng không đúng mô tả", "Muốn xem và sờ tận tay", "Lo giao hàng làm hỏng máy",
      "Lo không ai lắp đặt", "Lo bảo hành khó", "Phí giao hàng cao", "Không rành đặt hàng trên mạng"]),
    ("K8", "luoi", "Tỷ lệ người chấm 4 hoặc 5 điểm (quan trọng) cho từng tiêu chí", False,
     ["Giá tốt", "Hàng chính hãng, thương hiệu quen", "Bảo hành dài", "Được lắp đặt tận nơi",
      "Giao hàng nhanh, đúng hẹn", "Có đánh giá của người đã mua",
      "So sánh được thông số (công suất, kích thước) giữa các mẫu"]),
    ("K9", "nhieu", "Cách tìm sản phẩm trên trang web bán hàng", False,
     ["Gõ tên vào ô tìm kiếm", "Bấm vào danh mục (bếp, máy hút mùi…)", "Lọc theo khoảng giá",
      "Lọc theo thương hiệu", "Xem mục khuyến mãi", "Hỏi nhân viên qua chat", "Tôi chưa dùng web bán hàng"]),
    ("K10", "nhieu", "Cách muốn trả tiền khi mua online", False,
     ["Trả tiền mặt khi nhận hàng", "Chuyển khoản hoặc quét mã QR", "Ví điện tử (MoMo, ZaloPay, VNPay…)",
      "Thẻ ngân hàng, thẻ tín dụng", "Trả góp"]),
    ("K11", "mot", "Có muốn tạo tài khoản để đặt hàng", False,
     ["Có, để theo dõi đơn và lưu địa chỉ", "Đăng nhập nhanh bằng Google hoặc Zalo thì được",
      "Không, chỉ muốn nhập số điện thoại rồi đặt", "Không biết, chưa đặt online bao giờ"]),
    ("K12", "nhieu", "Những điều cần khi mua", False,
     ["Xem trước phí giao hàng trước khi đặt", "Mua nhiều món một lần với giá ưu đãi",
      "Giao và lắp tất cả các món trong một lần", "Được chỉ cách dùng sau khi lắp", "Xuất hoá đơn VAT",
      "Không cần điều nào ở trên"]),
    ("K13", "mot", "Cách muốn hẹn lịch lắp đặt đồ lớn", False,
     ["Giao và lắp luôn trong cùng một lần", "Tự chọn ngày giờ lắp trên website",
      "Nhân viên gọi điện hoặc nhắn Zalo hẹn", "Tự lắp hoặc nhờ thợ quen",
      "Ở xa, cần hướng dẫn hoặc video để tự lắp"]),
    ("K14", "nhieu", "Rắc rối đã gặp sau khi mua", False,
     ["Chưa từng", "Đổi trả bị làm khó", "Bảo hành lâu, bị đẩy qua lại giữa cửa hàng và hãng",
      "Giao trễ hẹn", "Lắp đặt ẩu"]),
    ("K15", "nhieu", "Cách muốn tra cứu thời hạn bảo hành", False,
     ["Số điện thoại lúc mua", "Số serial (dãy số in trên tem dán sau máy)", "Mã đơn hàng hoặc hoá đơn",
      "Giữ phiếu bảo hành giấy là đủ"]),
    ("K16", "chu", "Điều khó chịu nhất khi mua đồ điện gia dụng", False, []),
    ("K17", "chu", "Tính năng website nên có", False, []),
]

KHAC = "Khác"

HIEN_KHI = {"K7": ("K6", ["Đã từng, một hai lần", "Chưa từng"])}


def chuan(s):
    return unicodedata.normalize("NFC", (s or "").strip())


def doc_csv(duong_dan):
    with open(duong_dan, encoding="utf-8-sig", newline="") as f:
        dong = list(csv.reader(f))
    if not dong:
        sys.exit("Tệp CSV rỗng")
    tieu_de = [chuan(x) for x in dong[0]]
    phieu = [[chuan(x) for x in d] + [""] * (len(tieu_de) - len(d)) for d in dong[1:] if any(x.strip() for x in d)]
    return tieu_de, phieu


def tim_cot(tieu_de):
    cot = {}
    for i, t in enumerate(tieu_de):
        m = re.match(r"^(K\d+)\.", t)
        if not m:
            continue
        ma = m.group(1)
        hang = re.search(r"\[(.+)\]\s*$", t)
        if hang:
            cot.setdefault(ma, {})[chuan(hang.group(1))] = i
        else:
            cot[ma] = i
    return cot


def tach_nhieu(gia_tri, lua_chon):
    manh = gia_tri.split(", ")
    co = set(lua_chon)
    dai_nhat = max(x.count(", ") for x in lua_chon) + 1
    ket_qua, du = [], []
    i = 0
    while i < len(manh):
        for k in range(min(dai_nhat, len(manh) - i), 0, -1):
            ghep = ", ".join(manh[i:i + k])
            if ghep in co:
                ket_qua.append(ghep)
                i += k
                break
        else:
            du.append(manh[i])
            i += 1
    return ket_qua, ", ".join(du)


def dem(phieu, cot, ma, loai, lua_chon):
    so = {x: 0 for x in lua_chon}
    khac = []
    co_tra_loi = 0
    for p in phieu:
        v = p[cot]
        if not v:
            continue
        co_tra_loi += 1
        if loai == "mot":
            chon, du = ([v], "") if v in so else ([], v)
        else:
            chon, du = tach_nhieu(v, lua_chon)
        for c in chon:
            so[c] += 1
        if du:
            so[KHAC] = so.get(KHAC, 0) + 1
            khac.append(du)
    return so, co_tra_loi, khac


def doc_ngay(chuoi):
    hop = []
    for dang in ("%d/%m/%Y %H:%M:%S", "%m/%d/%Y %H:%M:%S", "%Y-%m-%d %H:%M:%S"):
        try:
            ds = [datetime.strptime(x, dang) for x in chuoi if x]
        except ValueError:
            continue
        if ds:
            hop.append(ds)
    return min(hop, key=lambda ds: max(ds) - min(ds)) if hop else []


def phan_tram(a, b):
    return 100.0 * a / b if b else 0.0


def so_vn(x, le=0):
    s = f"{x:.{le}f}"
    return s.replace(".", ",")


def ngat_dong(chu, rong=44):
    tu = chu.split()
    dong, hien = [], ""
    for t in tu:
        if hien and len(hien) + 1 + len(t) > rong:
            dong.append(hien)
            hien = t
        else:
            hien = (hien + " " + t).strip()
    if hien:
        dong.append(hien)
    return "\n".join(dong)


def hinh_cot(rong, y_giua, day, rx, ry):
    y0, y1 = y_giua - day / 2, y_giua + day / 2
    rx = min(rx, rong)
    dinh = [(0, y0), (rong - rx, y0), (rong, y0), (rong, y0 + ry), (rong, y1 - ry), (rong, y1), (rong - rx, y1),
            (0, y1), (0, y0)]
    ma = [Path.MOVETO, Path.LINETO, Path.CURVE3, Path.CURVE3, Path.LINETO, Path.CURVE3, Path.CURVE3,
          Path.LINETO, Path.CLOSEPOLY]
    return PathPatch(Path(dinh, ma), linewidth=0, facecolor=MAU_COT)


def ve_cot(nhan, gia_tri, chu_dau_cot, tieu_de, phu_de, duong_dan, gioi_han=100):
    plt.rcParams["font.family"] = ["Arial", "DejaVu Sans"]
    rong_hinh, dau, chan, day_cot, bo_goc = 6.3, 0.62, 0.12, 0.16, 0.042
    nhan_ngat = [ngat_dong(x, 34) for x in nhan]
    so_dong = max(x.count("\n") + 1 for x in nhan_ngat)
    buoc = 0.2 + 0.16 * so_dong
    n = len(nhan)
    cao = dau + n * buoc + chan
    fig, ax = plt.subplots(figsize=(rong_hinh, cao), dpi=200)
    fig.patch.set_facecolor("white")
    ax.set_facecolor("white")
    y = [n - 1 - i for i in range(n)]
    ax.set_ylim(-0.5, n - 0.5)
    ax.set_yticks(y)
    ax.set_yticklabels(nhan_ngat, fontsize=9, color=CHU_PHU)
    ax.tick_params(axis="y", length=0, pad=6)
    ax.set_xticks([])
    fig.subplots_adjust(left=0.4, right=0.97, top=1 - dau / cao, bottom=chan / cao)
    fig.canvas.draw()
    rong_nhan = max(t.get_window_extent().width for t in ax.get_yticklabels()) / fig.dpi
    trai = min(0.55, (rong_nhan + 0.2) / rong_hinh)
    fig.subplots_adjust(left=trai)
    ax.set_xlim(0, gioi_han * 1.24)
    rong_truc = (0.97 - trai) * rong_hinh
    rx = bo_goc * (ax.get_xlim()[1] / rong_truc)
    ry = bo_goc / buoc
    day = day_cot / buoc
    for yi, v, c in zip(y, gia_tri, chu_dau_cot):
        if v > 0:
            ax.add_patch(hinh_cot(v, yi, day, rx, ry))
        ax.text(v + gioi_han * 0.015, yi, c, va="center", ha="left", fontsize=9, color=CHU_CHINH)
    for k in ("top", "right", "bottom"):
        ax.spines[k].set_visible(False)
    ax.spines["left"].set_color(DUONG_NEN)
    ax.spines["left"].set_linewidth(1)
    fig.text(0.02, 1 - 0.18 / cao, tieu_de, fontsize=10.5, fontweight="bold", color=CHU_CHINH, va="top")
    fig.text(0.02, 1 - 0.40 / cao, phu_de, fontsize=8.5, color=CHU_MO, va="top")
    fig.savefig(duong_dan, dpi=200, facecolor="white")
    plt.close(fig)


def bang_md(cot_tieu_de, hang):
    dong = ["| " + " | ".join(cot_tieu_de) + " |", "|" + "|".join("---" for _ in cot_tieu_de) + "|"]
    dong += ["| " + " | ".join(str(x) for x in h) + " |" for h in hang]
    return "\n".join(dong)


def xu_ly(duong_csv, thu_muc_ra):
    tieu_de, phieu = doc_csv(duong_csv)
    cot = tim_cot(tieu_de)
    thieu = [ma for ma, *_ in CAU if ma not in cot]
    if thieu:
        sys.exit("Không tìm thấy cột của câu: " + ", ".join(thieu) + ". Kiểm tra lại tệp CSV tải từ bảng tính kết quả.")
    os.makedirs(thu_muc_ra, exist_ok=True)
    n = len(phieu)
    ngay = doc_ngay([p[0] for p in phieu])
    khoang = f"{min(ngay):%d/%m/%Y} đến {max(ngay):%d/%m/%Y}" if ngay else "không đọc được ngày"
    md = [f"# Kết quả bảng hỏi khách hàng",
          "",
          f"Tạo tự động bằng `xu_ly_bang_hoi.py` từ tệp `{os.path.basename(duong_csv)}` lúc {datetime.now():%d/%m/%Y %H:%M}.",
          f"Số phiếu: **{n}** · thời gian nhận phiếu: {khoang}.",
          "",
          "Câu nhiều lựa chọn: phần trăm tính trên số người trả lời câu đó, nên cộng lại có thể quá 100%.",
          "Câu K7 chỉ hiện với người chọn \"Đã từng, một hai lần\" hoặc \"Chưa từng\" ở câu K6.",
          ""]
    so_hinh = 0
    for ma, loai, ten, giu_thu_tu, lua_chon in CAU:
        if loai == "chu":
            tra_loi = [p[cot[ma]] for p in phieu if p[cot[ma]]]
            md += [f"## {ma}. {ten}", "", f"{len(tra_loi)} người trả lời.", ""]
            md += [f"- {t}" for t in tra_loi] or ["(chưa có)"]
            md.append("")
            continue
        if loai == "luoi":
            hang = []
            for h in lua_chon:
                i = cot[ma].get(chuan(h))
                diem = [int(p[i]) for p in phieu if i is not None and p[i].strip().isdigit()]
                m = len(diem)
                phan_bo = [sum(1 for d in diem if d == k) for k in range(1, 6)]
                cao = sum(1 for d in diem if d >= 4)
                tb = sum(diem) / m if m else 0
                hang.append((h, m, phan_bo, cao, phan_tram(cao, m), tb))
            hang.sort(key=lambda x: (-x[4], -x[5]))
            so_hinh += 1
            tep = f"{ma.lower()}.png"
            ve_cot([h[0] for h in hang], [h[4] for h in hang],
                   [f"{so_vn(h[4])}%  ·  TB {so_vn(h[5], 1)}" for h in hang],
                   f"{ma}. {ten}", f"{hang[0][1] if hang else 0} người trả lời · thang 1 đến 5 · TB là điểm trung bình",
                   os.path.join(thu_muc_ra, tep))
            md += [f"## {ma}. {ten}", "", f"![{ma}]({tep})", "",
                   bang_md(["Tiêu chí", "Số người", "1", "2", "3", "4", "5", "Chấm 4–5", "Điểm TB"],
                           [(h[0], h[1], *h[2], f"{so_vn(h[4], 1)}%", so_vn(h[5], 2)) for h in hang]), ""]
            continue
        so, co_tra_loi, khac = dem(phieu, cot[ma], ma, loai, lua_chon)
        duoc_hoi = ""
        if ma in HIEN_KHI:
            cau_truoc, chon_truoc = HIEN_KHI[ma]
            so_hoi = sum(1 for p in phieu if p[cot[cau_truoc]] in chon_truoc)
            duoc_hoi = f" trên {so_hoi} người được hỏi"
        muc = list(so.items())
        if not giu_thu_tu:
            muc.sort(key=lambda x: (x[0] == KHAC, -x[1]))
        muc = [x for x in muc if x[1] > 0 or x[0] != KHAC]
        tep = f"{ma.lower()}.png"
        so_hinh += 1
        loai_chu = "một lựa chọn" if loai == "mot" else "nhiều lựa chọn"
        ve_cot([x[0] for x in muc], [phan_tram(x[1], co_tra_loi) for x in muc],
               [f"{so_vn(phan_tram(x[1], co_tra_loi))}%  ({x[1]})" for x in muc],
               f"{ma}. {ten}", f"{co_tra_loi} người trả lời{duoc_hoi} · {loai_chu} · số trong ngoặc là số người",
               os.path.join(thu_muc_ra, tep))
        md += [f"## {ma}. {ten}", "", f"{co_tra_loi} người trả lời{duoc_hoi}.", "", f"![{ma}]({tep})", "",
               bang_md(["Lựa chọn", "Số người", "Tỷ lệ"],
                       [(x[0], x[1], f"{so_vn(phan_tram(x[1], co_tra_loi), 1)}%") for x in muc]), ""]
        if khac:
            md += ["Câu trả lời \"Khác\":", ""] + [f"- {k}" for k in khac] + [""]
    with open(os.path.join(thu_muc_ra, "ket-qua-bang-hoi.md"), "w", encoding="utf-8", newline="\n") as f:
        f.write("\n".join(md))
    print(f"{n} phiếu, {so_hinh} biểu đồ -> {thu_muc_ra}")


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    if len(sys.argv) < 2:
        sys.exit("Cách dùng: python xu_ly_bang_hoi.py <tep-tai-tu-bang-tinh.csv> [thu-muc-ket-qua]")
    xu_ly(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else os.path.join(os.path.dirname(os.path.abspath(__file__)), "ket-qua-bang-hoi"))
