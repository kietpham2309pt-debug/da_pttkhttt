package vn.edu.huit.pttk.diengiadung.nghiepvu;

import static vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial.*;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.SanPhamSerialDAO;
import vn.edu.huit.pttk.diengiadung.thucthe.SanPhamSerial;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Service
public class SerialBUS {

    private static final Map<TrangThaiSerial, Set<TrangThaiSerial>> DUOC_DOI = new EnumMap<>(TrangThaiSerial.class);

    static {
        DUOC_DOI.put(TRONG_KHO, EnumSet.of(DA_XUAT));
        DUOC_DOI.put(DA_XUAT, EnumSet.of(DA_BAN, TRONG_KHO));
        DUOC_DOI.put(DA_BAN, EnumSet.of(DANG_BAO_HANH, TRA_VE_KHO));
        DUOC_DOI.put(DANG_BAO_HANH, EnumSet.of(DA_BAN));
        DUOC_DOI.put(TRA_VE_KHO, EnumSet.of(TRONG_KHO, LOI_CHO_GUI_HANG));
        DUOC_DOI.put(LOI_CHO_GUI_HANG, EnumSet.noneOf(TrangThaiSerial.class));
    }

    private final SanPhamSerialDAO serialDAO;

    public SerialBUS(SanPhamSerialDAO serialDAO) {
        this.serialDAO = serialDAO;
    }

    @Transactional
    public void doiTrangThai(SanPhamSerial may, TrangThaiSerial moi) {
        if (!DUOC_DOI.getOrDefault(may.getTrangThai(), Set.of()).contains(moi)) {
            throw new LoiNghiepVu("Máy " + may.getSoSerial() + " đang \"" + may.getTrangThai().getNhan()
                    + "\", không chuyển sang \"" + moi.getNhan() + "\" được");
        }
        may.setTrangThai(moi);
        serialDAO.save(may);
    }
}
