import os
import sys

sys.dont_write_bytecode = True
THU_MUC_GOI = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(THU_MUC_GOI, "..", "..", "chung", "so-do"))

from ve_nghiep_vu import SoDo
import matplotlib.pyplot as plt


def luu_anh(s, ten):
    duong = os.path.join(THU_MUC_GOI, ten + ".png")
    s.fig.savefig(duong, dpi=170, facecolor="white")
    plt.close(s.fig)
    print(duong)


def doi_tuong_buc02():
    s = SoDo(1080, 600)
    s.tac_nhan("kh", 95, 110, "Khách hàng")
    s.thua_tac_vien_bien("nv", 560, 110, "Nhân viên bán hàng", vi_tri="tren")
    s.thua_tac_vien("kt", 440, 360, "Kế toán")
    s.thuc_the("bg", 940, 110, "Bảng giá và\nkhuyến mãi", vi_tri="tren")
    s.thuc_the("tt", 970, 330, "Thông tin\nmẫu máy")
    s.thuc_the("cm", 940, 520, "Chiếc máy")
    s.thuc_the("pbh", 110, 330, "Phiếu\nbảo hành")
    s.thuc_the("hd", 250, 430, "Hoá đơn\nbán hàng")
    s.thuc_the("dh", 760, 500, "Đơn hàng")

    s.noi("kh", "nv", "0..n", "1")
    s.noi("nv", "kt", "1", "1")
    s.noi("nv", "bg", "1", "1")
    s.noi("nv", "tt", "1", "0..n")
    s.noi("nv", "cm", "1", "0..n")
    s.noi("nv", "pbh", "1", "0..n")
    s.noi("nv", "hd", "1", "0..n")
    s.noi("nv", "dh", "1", "0..n")
    s.noi("kt", "hd", "1", "0..n")
    s.noi("kt", "dh", "1", "0..n")
    luu_anh(s, "doi-tuong-buc02")


if __name__ == "__main__":
    doi_tuong_buc02()
