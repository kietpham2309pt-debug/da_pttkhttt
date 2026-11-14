package vn.edu.huit.pttk.diengiadung.nghiepvu.g1;

import java.util.Map;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;

public class LoiNhapLieu extends LoiNghiepVu {

    private final Map<String, String> loiO;

    public LoiNhapLieu(Map<String, String> loiO) {
        super("Kiểm tra lại các ô được báo lỗi bên dưới.");
        this.loiO = loiO;
    }

    public Map<String, String> getLoiO() {
        return loiO;
    }
}
