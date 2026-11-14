package vn.edu.huit.pttk.diengiadung.nghiepvu;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class GioHang implements Serializable {

    private final Map<Long, Integer> dong = new LinkedHashMap<>();
    private final Map<Long, Boolean> lapDat = new LinkedHashMap<>();

    public synchronized void them(Long maSanPham, int soLuong, boolean canLap) {
        dong.merge(maSanPham, Math.max(1, soLuong), Integer::sum);
        lapDat.putIfAbsent(maSanPham, canLap);
    }

    public synchronized void datSoLuong(Long maSanPham, int soLuong) {
        if (soLuong <= 0) {
            xoa(maSanPham);
        } else if (dong.containsKey(maSanPham)) {
            dong.put(maSanPham, soLuong);
        }
    }

    public synchronized void datLapDat(Long maSanPham, boolean co) {
        if (dong.containsKey(maSanPham)) {
            lapDat.put(maSanPham, co);
        }
    }

    public synchronized void xoa(Long maSanPham) {
        dong.remove(maSanPham);
        lapDat.remove(maSanPham);
    }

    public synchronized void xoaHet() {
        dong.clear();
        lapDat.clear();
    }

    public synchronized Map<Long, Integer> getDong() {
        return new LinkedHashMap<>(dong);
    }

    public synchronized boolean coLapDat(Long maSanPham) {
        return Boolean.TRUE.equals(lapDat.get(maSanPham));
    }

    public synchronized int getTongSoLuong() {
        return dong.values().stream().mapToInt(Integer::intValue).sum();
    }

    public synchronized boolean isTrong() {
        return dong.isEmpty();
    }
}
