package vn.edu.huit.pttk.diengiadung.nghiepvu.khach;

import java.util.ArrayList;
import java.util.List;

public class LocSanPham {

    private String danhMuc;
    private List<Long> thuongHieu = new ArrayList<>();
    private Long giaTu;
    private Long giaDen;
    private String tuKhoa;
    private String sapXep = "moi";
    private int trang = 1;

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public List<Long> getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(List<Long> thuongHieu) {
        this.thuongHieu = thuongHieu == null ? new ArrayList<>() : thuongHieu;
    }

    public Long getGiaTu() {
        return giaTu;
    }

    public void setGiaTu(Long giaTu) {
        this.giaTu = giaTu;
    }

    public Long getGiaDen() {
        return giaDen;
    }

    public void setGiaDen(Long giaDen) {
        this.giaDen = giaDen;
    }

    public String getTuKhoa() {
        return tuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        this.tuKhoa = tuKhoa;
    }

    public String getSapXep() {
        return sapXep;
    }

    public void setSapXep(String sapXep) {
        this.sapXep = sapXep;
    }

    public int getTrang() {
        return trang;
    }

    public void setTrang(int trang) {
        this.trang = trang;
    }

    public boolean isCoLoc() {
        return !thuongHieu.isEmpty() || giaTu != null || giaDen != null;
    }

    public void chuanHoa() {
        if (danhMuc != null && danhMuc.isBlank()) {
            danhMuc = null;
        }
        if (tuKhoa != null) {
            tuKhoa = tuKhoa.trim();
            if (tuKhoa.isEmpty()) {
                tuKhoa = null;
            }
        }
        if (giaTu != null && giaTu < 0) {
            giaTu = 0L;
        }
        if (giaDen != null && giaDen < 0) {
            giaDen = 0L;
        }
        if (giaTu != null && giaDen != null && giaTu > giaDen) {
            Long tam = giaTu;
            giaTu = giaDen;
            giaDen = tam;
        }
        if (sapXep == null || !List.of("moi", "gia-tang", "gia-giam", "ten").contains(sapXep)) {
            sapXep = "moi";
        }
        if (trang < 1) {
            trang = 1;
        }
    }
}
