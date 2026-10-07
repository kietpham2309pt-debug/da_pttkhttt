import math
import os

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib import font_manager
from matplotlib.patches import Circle, Ellipse, Rectangle

THU_MUC = os.path.dirname(os.path.abspath(__file__))
for tep in ("times.ttf", "timesi.ttf", "timesbd.ttf"):
    duong = os.path.join(os.environ.get("WINDIR", r"C:\Windows"), "Fonts", tep)
    if os.path.exists(duong):
        font_manager.fontManager.addfont(duong)
plt.rcParams["font.family"] = "Times New Roman"

CO_CHU = 18
DO_DAY_NET = 1.4
BAN_KINH = 28


class SoDo:
    def __init__(self, rong, cao):
        self.fig = plt.figure(figsize=(rong / 100, cao / 100), dpi=100)
        self.ax = self.fig.add_axes([0, 0, 1, 1])
        self.ax.set_xlim(0, rong)
        self.ax.set_ylim(cao, 0)
        self.ax.set_aspect("equal")
        self.ax.axis("off")
        self.hinh = {}

    def chu(self, x, y, noi_dung, co=CO_CHU, can="center", nghieng=False, doc="top"):
        self.ax.text(x, y, noi_dung, fontsize=co, ha=can, va=doc,
                     style="italic" if nghieng else "normal", linespacing=1.15)

    def net(self, xs, ys):
        self.ax.plot(xs, ys, color="black", lw=DO_DAY_NET)

    def tron(self, x, y, r):
        self.ax.add_patch(Circle((x, y), r, fill=False, lw=DO_DAY_NET))

    def ten_hinh(self, x, y, ten, vi_tri, lech_x=0):
        r = BAN_KINH
        if vi_tri == "tren":
            self.chu(x + lech_x, y - r - 8, ten, doc="bottom")
        elif vi_tri == "trai":
            self.chu(x - r - 10, y, ten, can="right", doc="center")
        elif vi_tri == "phai":
            self.chu(x + r + 10, y, ten, can="left", doc="center")
        else:
            self.chu(x + lech_x, y + r + 8, ten)

    def gach_cheo(self, x, y):
        g = math.radians(35)
        px, py = x + BAN_KINH * math.cos(g), y + BAN_KINH * math.sin(g)
        self.net([px - 13, px + 3], [py + 6, py - 13])

    def tac_nhan(self, ma, x, y, ten, khuon_mau="«business actor»"):
        self.tron(x, y - 30, 9)
        self.net([x - 6, x + 6], [y - 24, y - 36])
        self.net([x, x], [y - 21, y + 8])
        self.net([x - 17, x + 17], [y - 12, y - 12])
        self.net([x, x - 14], [y + 8, y + 30])
        self.net([x, x + 14], [y + 8, y + 30])
        self.chu(x, y + 34, khuon_mau, co=CO_CHU - 2, nghieng=True)
        self.chu(x, y + 36 + CO_CHU * 1.3, ten)
        self.hinh[ma] = (x, y, 0, (x - 17, y - 39, x + 17, y + 30))

    def thua_tac_vien(self, ma, x, y, ten, vi_tri="duoi"):
        r = BAN_KINH
        self.tron(x, y, r)
        self.net([x - 6, x + 3, x - 6], [y - r - 6, y - r, y - r + 6])
        self.tron(x, y - 11, 5)
        self.net([x, x], [y - 6, y + 7])
        self.net([x - 9, x + 9], [y - 1, y - 1])
        self.net([x, x - 7], [y + 7, y + 16])
        self.net([x, x + 7], [y + 7, y + 16])
        self.gach_cheo(x, y)
        self.ten_hinh(x, y, ten, vi_tri)
        self.hinh[ma] = (x, y, r, None)

    def thua_tac_vien_bien(self, ma, x, y, ten, vi_tri="duoi"):
        r = BAN_KINH
        self.tron(x, y, r)
        self.net([x - r - 16, x - r - 16], [y - 20, y + 20])
        self.net([x - r - 16, x - r], [y, y])
        self.ten_hinh(x, y, ten, vi_tri, lech_x=-8)
        self.hinh[ma] = (x, y, r, None)

    def thuc_the(self, ma, x, y, ten, vi_tri="duoi"):
        r = BAN_KINH
        self.tron(x, y, r)
        self.net([x - r, x + r], [y + r, y + r])
        self.gach_cheo(x, y)
        self.ten_hinh(x, y, ten, vi_tri)
        self.hinh[ma] = (x, y, r, None)

    def use_case(self, ma, x, y, ten, rong=150, cao=64):
        self.ax.add_patch(Ellipse((x, y), rong, cao, fill=False, lw=DO_DAY_NET))
        g = math.radians(40)
        px, py = x + rong / 2 * math.cos(g), y + cao / 2 * math.sin(g)
        self.net([px - 22, px], [py + 6, py - 16])
        self.chu(x, y, ten, doc="center")
        self.hinh[ma] = (x, y, 0, (x - rong / 2, y - cao / 2, x + rong / 2, y + cao / 2))

    def diem_tren_bien(self, ma, toi_x, toi_y):
        x, y, r, khung = self.hinh[ma]
        dx, dy = toi_x - x, toi_y - y
        if khung is None:
            d = math.hypot(dx, dy) or 1
            return x + dx * r / d, y + dy * r / d
        x0, y0, x1, y1 = khung
        ty = ((y1 - y) if dy > 0 else (y0 - y)) / dy if dy else float("inf")
        tx = ((x1 - x) if dx > 0 else (x0 - x)) / dx if dx else float("inf")
        t = min(abs(tx), abs(ty))
        return x + dx * t, y + dy * t

    def noi(self, a, b, ban_so_a, ban_so_b):
        xa, ya = self.hinh[a][:2]
        xb, yb = self.hinh[b][:2]
        pa = self.diem_tren_bien(a, xb, yb)
        pb = self.diem_tren_bien(b, xa, ya)
        self.net([pa[0], pb[0]], [pa[1], pb[1]])
        for p, q, so in ((pa, pb, ban_so_a), (pb, pa, ban_so_b)):
            d = math.hypot(q[0] - p[0], q[1] - p[1]) or 1
            ux, uy = (q[0] - p[0]) / d, (q[1] - p[1]) / d
            self.chu(p[0] + ux * 30 - uy * 11, p[1] + uy * 30 + ux * 11, so, co=CO_CHU - 2, doc="center")

    def hop(self, x, y, rong, cao, ten):
        self.ax.add_patch(Rectangle((x - rong / 2, y - cao / 2), rong, cao, fill=False, lw=DO_DAY_NET))
        self.chu(x, y, ten, doc="center")

    def luu(self, ten):
        duong = os.path.join(THU_MUC, ten + ".png")
        self.fig.savefig(duong, dpi=170, facecolor="white")
        plt.close(self.fig)
        print(duong)


def ky_hieu_nghiep_vu():
    s = SoDo(1080, 200)
    s.tac_nhan("a", 100, 60, "Tác nhân\nnghiệp vụ")
    s.use_case("u", 310, 60, "Use case\nnghiệp vụ", rong=180, cao=76)
    s.thua_tac_vien_bien("b", 540, 60, "Thừa tác viên\ngiao tiếp với\nmôi trường")
    s.thua_tac_vien("t", 760, 60, "Thừa tác viên\nlàm việc\nbên trong")
    s.thuc_the("e", 970, 60, "Thực thể\nnghiệp vụ")
    s.luu("ky-hieu-nghiep-vu")


def to_chuc_cua_hang():
    s = SoDo(1000, 330)
    s.hop(500, 45, 240, 56, "Chủ cửa hàng")
    s.hop(500, 145, 240, 56, "Quản lý cửa hàng")
    bo_phan = [(125, "Bộ phận bán hàng\n(nhân viên bán hàng)"),
               (375, "Kho\n(thủ kho)"),
               (625, "Bộ phận giao hàng,\nlắp đặt (nhân viên\ngiao hàng – kỹ thuật)"),
               (875, "Kế toán")]
    s.net([500, 500], [73, 117])
    s.net([500, 500], [173, 205])
    s.net([bo_phan[0][0], bo_phan[-1][0]], [205, 205])
    for x, ten in bo_phan:
        s.net([x, x], [205, 228])
        s.hop(x, 274, 236, 92, ten)
    s.luu("to-chuc-cua-hang")


def doi_tuong_buc07():
    s = SoDo(1040, 540)
    s.tac_nhan("cch", 95, 100, "Chủ cửa hàng")
    s.thua_tac_vien_bien("ql", 420, 85, "Quản lý cửa hàng", vi_tri="tren")
    s.thuc_the("bc", 840, 85, "Báo cáo kinh doanh", vi_tri="tren")
    s.thua_tac_vien("kt", 640, 275, "Kế toán", vi_tri="phai")
    s.thua_tac_vien_bien("tk", 170, 275, "Thủ kho", vi_tri="tren")
    s.thuc_the("cm", 80, 445, "Chiếc máy")
    s.thuc_the("px", 255, 445, "Phiếu\nxuất kho")
    s.thuc_the("pn", 430, 445, "Phiếu\nnhập kho")
    s.thuc_the("pbh", 610, 445, "Phiếu tiếp nhận\nbảo hành")
    s.thuc_the("hd", 785, 445, "Hoá đơn\nbán hàng")
    s.thuc_the("snt", 955, 445, "Sổ nộp tiền")

    s.noi("cch", "ql", "1", "1")
    s.noi("ql", "bc", "1", "0..n")
    s.noi("ql", "kt", "1", "1")
    s.noi("ql", "tk", "1", "1")
    s.noi("kt", "bc", "1", "0..n")
    s.noi("kt", "tk", "1", "1")
    s.noi("kt", "pn", "1", "0..n")
    s.noi("kt", "pbh", "1", "0..n")
    s.noi("kt", "hd", "1", "0..n")
    s.noi("kt", "snt", "1", "1")
    s.noi("tk", "cm", "1", "0..n")
    s.noi("tk", "px", "1", "0..n")
    s.noi("tk", "pn", "1", "0..n")
    s.luu("doi-tuong-buc07")


if __name__ == "__main__":
    ky_hieu_nghiep_vu()
    to_chuc_cua_hang()
    doi_tuong_buc07()
