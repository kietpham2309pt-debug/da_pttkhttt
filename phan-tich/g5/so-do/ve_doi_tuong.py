import math
import os
import sys

THU_MUC = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(THU_MUC, "..", "..", "chung", "so-do"))

import matplotlib.pyplot as plt
from ve_nghiep_vu import CO_CHU, SoDo


def noi(s, a, b, ban_so_a, ban_so_b):
    xa, ya = s.hinh[a][:2]
    xb, yb = s.hinh[b][:2]
    pa = s.diem_tren_bien(a, xb, yb)
    pb = s.diem_tren_bien(b, xa, ya)
    s.net([pa[0], pb[0]], [pa[1], pb[1]])
    for p, q, so in ((pa, pb, ban_so_a), (pb, pa, ban_so_b)):
        d = math.hypot(q[0] - p[0], q[1] - p[1]) or 1
        ux, uy = (q[0] - p[0]) / d, (q[1] - p[1]) / d
        lech = 5 + 4.5 * len(so) * abs(uy) + 9 * abs(ux)
        s.chu(p[0] + ux * 30 - uy * lech, p[1] + uy * 30 + ux * lech, so, co=CO_CHU - 2, doc="center")


def luu(s, ten):
    duong = os.path.join(THU_MUC, ten + ".png")
    s.fig.savefig(duong, dpi=170, facecolor="white")
    plt.close(s.fig)
    print(duong)


def doi_tuong_buc05():
    s = SoDo(1000, 600)
    s.tac_nhan("ncc", 90, 300, "Nhà cung cấp")
    s.thua_tac_vien_bien("ql", 640, 110, "Quản lý cửa hàng", vi_tri="tren")
    s.thuc_the("ddh", 880, 150, "Đơn đặt hàng\nnhà cung cấp", vi_tri="tren")
    s.thua_tac_vien_bien("tk", 380, 330, "Thủ kho", vi_tri="tren")
    s.thuc_the("hd", 780, 330, "Hoá đơn\nnhà cung cấp", vi_tri="phai")
    s.thuc_the("cm", 200, 500, "Chiếc máy")
    s.thuc_the("pn", 560, 500, "Phiếu\nnhập kho")
    s.thua_tac_vien("kt", 780, 500, "Kế toán", vi_tri="phai")

    noi(s, "ncc", "ql", "1", "1")
    noi(s, "ncc", "tk", "1", "1")
    noi(s, "ql", "tk", "1", "1")
    noi(s, "ql", "ddh", "1", "0..n")
    noi(s, "tk", "ddh", "1", "0..n")
    noi(s, "tk", "hd", "1", "0..n")
    noi(s, "tk", "cm", "1", "0..n")
    noi(s, "tk", "pn", "1", "0..n")
    noi(s, "tk", "kt", "1", "1")
    noi(s, "kt", "pn", "1", "0..n")
    noi(s, "kt", "hd", "1", "0..n")
    luu(s, "doi-tuong-buc05")


def doi_tuong_buc06():
    s = SoDo(1050, 600)
    s.tac_nhan("kh", 80, 140, "Khách hàng")
    s.thua_tac_vien_bien("nv", 330, 140, "Nhân viên bán hàng", vi_tri="tren")
    s.thua_tac_vien_bien("tk", 700, 140, "Thủ kho", vi_tri="tren")
    s.tac_nhan("ttbh", 940, 330, "Trung tâm\nbảo hành hãng")
    s.thuc_the("pbh", 120, 480, "Phiếu\nbảo hành")
    s.thuc_the("ptn", 340, 480, "Phiếu tiếp nhận\nbảo hành")
    s.thuc_the("cm", 570, 480, "Chiếc máy")
    s.thuc_the("std", 790, 480, "Sổ theo dõi\nmáy gửi hãng")

    noi(s, "kh", "nv", "1", "1")
    noi(s, "nv", "tk", "1", "1")
    noi(s, "nv", "ttbh", "1", "1")
    noi(s, "tk", "ttbh", "1", "1")
    noi(s, "nv", "pbh", "1", "0..n")
    noi(s, "nv", "ptn", "1", "0..n")
    noi(s, "nv", "cm", "1", "0..n")
    noi(s, "tk", "ptn", "1", "0..n")
    noi(s, "tk", "cm", "1", "0..n")
    noi(s, "tk", "std", "1", "1")
    luu(s, "doi-tuong-buc06")


def doi_tuong_buc09():
    s = SoDo(1000, 560)
    s.thua_tac_vien_bien("nv", 500, 100, "Nhân viên bán hàng", vi_tri="tren")
    s.thuc_the("cm", 130, 370, "Chiếc máy")
    s.thuc_the("px", 310, 430, "Phiếu\nxuất kho")
    s.thuc_the("hd", 500, 450, "Hoá đơn\nbán hàng")
    s.thuc_the("pbh", 690, 430, "Phiếu\nbảo hành")
    s.thuc_the("ptn", 870, 370, "Phiếu tiếp nhận\nbảo hành")

    for ma in ("cm", "px", "hd", "pbh", "ptn"):
        noi(s, "nv", ma, "1", "0..n")
    luu(s, "doi-tuong-buc09")


if __name__ == "__main__":
    doi_tuong_buc05()
    doi_tuong_buc06()
    doi_tuong_buc09()
