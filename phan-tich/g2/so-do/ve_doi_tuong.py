import os
import sys

THU_MUC_GOI = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(THU_MUC_GOI, "..", "..", "chung", "so-do"))

from ve_nghiep_vu import SoDo
import matplotlib.pyplot as plt


def doi_tuong_buc01():
    s = SoDo(900, 590)
    s.thuc_the("bg", 130, 110, "Bảng giá và\nkhuyến mãi", vi_tri="tren")
    s.thua_tac_vien_bien("ql", 540, 110, "Quản lý cửa hàng", vi_tri="tren")
    s.thuc_the("tt", 540, 290, "Thông tin\nmẫu máy", vi_tri="tren")
    s.tac_nhan("kh", 80, 400, "Khách hàng")
    s.thua_tac_vien_bien("bh", 250, 400, "Nhân viên\nbán hàng")
    s.thua_tac_vien_bien("tk", 830, 400, "Thủ kho")
    s.thuc_the("cm", 540, 500, "Chiếc máy")

    s.noi("kh", "bh", "0..n", "1")
    s.noi("ql", "bh", "1", "1")
    s.noi("ql", "tk", "1", "1")
    s.noi("ql", "bg", "1", "0..n")
    s.noi("bh", "bg", "1", "0..n")
    s.noi("bh", "tt", "1", "0..n")
    s.noi("bh", "cm", "1", "0..n")
    s.noi("tk", "tt", "1", "0..n")
    s.noi("tk", "cm", "1", "0..n")

    s.fig.savefig(os.path.join(THU_MUC_GOI, "doi-tuong-buc01.png"), dpi=170, facecolor="white")
    plt.close(s.fig)


if __name__ == "__main__":
    doi_tuong_buc01()
