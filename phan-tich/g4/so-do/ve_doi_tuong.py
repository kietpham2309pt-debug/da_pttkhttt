import os
import sys

sys.dont_write_bytecode = True
THU_MUC = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(THU_MUC, "..", "..", "chung", "so-do"))

import matplotlib.pyplot as plt
from ve_nghiep_vu import BAN_KINH, SoDo


def luu(s, ten):
    duong = os.path.join(THU_MUC, ten + ".png")
    s.fig.savefig(duong, dpi=170, facecolor="white")
    plt.close(s.fig)
    print(duong)


def ten_trai_bien(s, x, y, ten):
    s.chu(x - BAN_KINH - 26, y, ten, can="right", doc="center")


def doi_tuong_buc03():
    s = SoDo(1100, 770)
    s.tac_nhan("kh", 75, 330, "Khách hàng")
    s.tac_nhan("vc", 960, 600, "Đơn vị\nvận chuyển")
    s.thua_tac_vien_bien("nvbh", 260, 200, "Nhân viên bán hàng", vi_tri="tren")
    s.thua_tac_vien_bien("ql", 560, 200, "Quản lý cửa hàng", vi_tri="phai")
    s.thua_tac_vien_bien("tk", 260, 500, "")
    ten_trai_bien(s, 260, 500, "Thủ kho")
    s.thua_tac_vien_bien("gh", 560, 500, "")
    s.chu(528, 552, "Nhân viên giao hàng\n– kỹ thuật", can="right")
    s.thua_tac_vien("kt", 930, 420, "Kế toán", vi_tri="duoi")
    s.thuc_the("dh", 880, 80, "Đơn hàng", vi_tri="phai")
    s.thuc_the("snt", 1050, 420, "Sổ nộp\ntiền", vi_tri="duoi")
    s.thuc_the("pgh", 720, 330, "Phiếu\ngiao hàng", vi_tri="phai")
    s.thuc_the("pxk", 110, 680, "Phiếu\nxuất kho")
    s.thuc_the("cm", 330, 680, "Chiếc máy")
    s.thuc_the("hd", 560, 680, "Hoá đơn\nbán hàng")
    s.thuc_the("pbh", 760, 680, "Phiếu\nbảo hành")

    s.noi("kh", "nvbh", "1", "1")
    s.noi("kh", "gh", "1", "1")
    s.noi("vc", "gh", "1", "1")
    s.noi("nvbh", "ql", "1", "1")
    s.noi("nvbh", "tk", "1", "1")
    s.noi("ql", "gh", "1", "1")
    s.noi("tk", "gh", "1", "1")
    s.noi("gh", "kt", "1", "1")
    s.noi("nvbh", "dh", "1", "0..n")
    s.noi("kt", "dh", "1", "0..n")
    s.noi("kt", "snt", "1", "1")
    s.noi("ql", "pgh", "1", "0..n")
    s.noi("gh", "pgh", "1", "0..n")
    s.noi("tk", "pxk", "1", "0..n")
    s.noi("tk", "cm", "1", "0..n")
    s.noi("gh", "hd", "1", "0..n")
    s.noi("gh", "pbh", "1", "0..n")
    luu(s, "doi-tuong-buc03")


def doi_tuong_buc08():
    s = SoDo(1000, 400)
    s.tac_nhan("kh", 90, 150, "Khách hàng")
    s.thua_tac_vien_bien("gh", 420, 150, "Nhân viên giao hàng\n– kỹ thuật", vi_tri="tren")
    s.thuc_the("cm", 250, 320, "Chiếc máy")
    s.thuc_the("bb", 590, 320, "Biên bản lắp đặt")
    s.thuc_the("bg", 820, 150, "Bảng giá công lắp\nvà vật tư", vi_tri="tren")

    s.noi("kh", "gh", "1", "1")
    s.noi("gh", "cm", "1", "0..n")
    s.noi("gh", "bb", "1", "0..n")
    s.noi("gh", "bg", "1", "1")
    luu(s, "doi-tuong-buc08")


def doi_tuong_buc04():
    s = SoDo(1000, 690)
    s.tac_nhan("kh", 80, 130, "Khách hàng")
    s.thua_tac_vien_bien("nvbh", 330, 120, "Nhân viên bán hàng", vi_tri="tren")
    s.thua_tac_vien_bien("ql", 800, 120, "Quản lý cửa hàng", vi_tri="tren")
    s.thua_tac_vien_bien("tk", 170, 400, "")
    ten_trai_bien(s, 170, 400, "Thủ kho")
    s.thua_tac_vien("kt", 800, 300, "Kế toán", vi_tri="phai")
    s.thuc_the("pdt", 580, 300, "Phiếu đổi trả", vi_tri="duoi")
    s.thuc_the("cm", 360, 420, "Chiếc máy")
    s.thuc_the("pxk", 170, 590, "Phiếu\nxuất kho")
    s.thuc_the("hd", 600, 560, "Hoá đơn\nbán hàng")

    s.noi("kh", "nvbh", "1", "1")
    s.noi("nvbh", "ql", "1", "1")
    s.noi("nvbh", "tk", "1", "1")
    s.noi("ql", "kt", "1", "1")
    s.noi("nvbh", "pdt", "1", "0..n")
    s.noi("ql", "pdt", "1", "0..n")
    s.noi("kt", "pdt", "1", "0..n")
    s.noi("nvbh", "hd", "1", "0..n")
    s.noi("kt", "hd", "1", "0..n")
    s.noi("nvbh", "cm", "1", "0..n")
    s.noi("tk", "cm", "1", "0..n")
    s.noi("tk", "pxk", "1", "0..n")
    luu(s, "doi-tuong-buc04")


if __name__ == "__main__":
    doi_tuong_buc03()
    doi_tuong_buc08()
    doi_tuong_buc04()
