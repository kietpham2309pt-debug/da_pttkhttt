package vn.edu.huit.pttk.diengiadung.nghiepvu.g5;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.pttk.diengiadung.khodulieu.g5.NhaCungCapTruyVanG5;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.thucthe.NhaCungCap;

@Service
public class NhaCungCapBUS {

    private final NhaCungCapTruyVanG5 nhaCungCapDAO;

    public NhaCungCapBUS(NhaCungCapTruyVanG5 nhaCungCapDAO) {
        this.nhaCungCapDAO = nhaCungCapDAO;
    }

    @Transactional(readOnly = true)
    public List<NhaCungCap> tatCa() {
        return nhaCungCapDAO.findAllByOrderByDangHopTacDescTenNhaCungCapAsc();
    }

    @Transactional(readOnly = true)
    public List<NhaCungCap> dangHopTac() {
        return nhaCungCapDAO.findByDangHopTacTrueOrderByTenNhaCungCapAsc();
    }

    @Transactional(readOnly = true)
    public NhaCungCap tim(Long ma) {
        return nhaCungCapDAO.findById(ma).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy nhà cung cấp #" + ma));
    }

    @Transactional(readOnly = true)
    public long soChungTu(Long ma) {
        return nhaCungCapDAO.demPhieuNhap(ma) + nhaCungCapDAO.demDonDat(ma);
    }

    @Transactional
    public NhaCungCap luu(Long ma, String ten, String soDienThoai, String email, String diaChi) {
        String t = ten == null ? "" : ten.trim().replaceAll("\\s+", " ");
        String sdt = soDienThoai == null ? "" : soDienThoai.replaceAll("[\\s.]", "");
        String mail = email == null || email.isBlank() ? null : email.trim();
        String dc = diaChi == null || diaChi.isBlank() ? null : diaChi.trim();
        if (t.isEmpty()) {
            throw new LoiNghiepVu("Chưa nhập tên nhà cung cấp.");
        }
        if (t.length() > 150) {
            throw new LoiNghiepVu("Tên nhà cung cấp dài quá 150 ký tự.");
        }
        if (!sdt.matches("0\\d{8,13}")) {
            throw new LoiNghiepVu("Số điện thoại phải bắt đầu bằng 0 và có 9–14 chữ số.");
        }
        if (mail != null && (mail.length() > 100 || !mail.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+"))) {
            throw new LoiNghiepVu("Email không đúng định dạng.");
        }
        if (dc != null && dc.length() > 255) {
            throw new LoiNghiepVu("Địa chỉ dài quá 255 ký tự.");
        }
        boolean trung = ma == null ? nhaCungCapDAO.existsByTenNhaCungCapIgnoreCase(t)
                : nhaCungCapDAO.existsByTenNhaCungCapIgnoreCaseAndMaNhaCungCapNot(t, ma);
        if (trung) {
            throw new LoiNghiepVu("Đã có nhà cung cấp tên \"" + t + "\".");
        }
        NhaCungCap n = ma == null ? new NhaCungCap() : tim(ma);
        if (ma == null) {
            n.setDangHopTac(true);
        }
        n.setTenNhaCungCap(t);
        n.setSoDienThoai(sdt);
        n.setEmail(mail);
        n.setDiaChi(dc);
        return nhaCungCapDAO.save(n);
    }

    @Transactional
    public void doiHopTac(Long ma, boolean dangHopTac) {
        NhaCungCap n = tim(ma);
        n.setDangHopTac(dangHopTac);
        nhaCungCapDAO.save(n);
    }

    @Transactional
    public void xoa(Long ma) {
        NhaCungCap n = tim(ma);
        if (soChungTu(ma) > 0) {
            throw new LoiNghiepVu("Nhà cung cấp \"" + n.getTenNhaCungCap()
                    + "\" đã có đơn đặt hàng hoặc phiếu nhập nên không xoá được; hãy chọn \"Ngừng hợp tác\".");
        }
        nhaCungCapDAO.delete(n);
    }
}
