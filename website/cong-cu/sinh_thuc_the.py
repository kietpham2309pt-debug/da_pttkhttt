import io
import os
import sys

GOC = sys.argv[1]
PKG = "vn.edu.huit.pttk.diengiadung"
TT = os.path.join(GOC, *PKG.split("."), "thucthe")
DAO = os.path.join(GOC, *PKG.split("."), "khodulieu")

ENUM = {
    "TrangThaiTaiKhoan": [("DANG_DUNG", "Đang dùng"), ("DA_KHOA", "Đã khoá")],
    "TrangThaiSanPham": [("CHUA_MO_BAN", "Chưa mở bán"), ("DANG_BAN", "Đang bán"), ("NGUNG_BAN", "Ngừng bán")],
    "LoaiKhuyenMai": [("GIAM_THEO_SAN_PHAM", "Giảm theo sản phẩm"), ("MA_GIAM_GIA", "Mã giảm giá")],
    "KieuGiam": [("PHAN_TRAM", "Phần trăm"), ("SO_TIEN", "Số tiền")],
    "TrangThaiKhuyenMai": [("DANG_CHAY", "Đang chạy"), ("TAM_DUNG", "Tạm dừng")],
    "TrangThaiDonHang": [("CHO_XAC_NHAN", "Chờ xác nhận"), ("DA_XAC_NHAN", "Đã xác nhận"), ("DANG_GIAO", "Đang giao"),
                         ("HOAN_THANH", "Hoàn thành"), ("GIAO_KHONG_THANH_CONG", "Giao không thành công"),
                         ("DA_HUY", "Đã huỷ"), ("DA_TRA_HANG", "Đã trả hàng")],
    "TrangThaiThanhToan": [("CHUA_THANH_TOAN", "Chưa thanh toán"), ("DA_DAT_COC", "Đã đặt cọc"),
                           ("DA_THANH_TOAN", "Đã thanh toán"), ("DA_HOAN_TIEN", "Đã hoàn tiền")],
    "PhuongThucThanhToan": [("TIEN_MAT_KHI_NHAN", "Tiền mặt khi nhận hàng"), ("CONG_THANH_TOAN", "Thanh toán trực tuyến")],
    "LoaiGiaoDich": [("COC", "Đặt cọc"), ("THANH_TOAN", "Thanh toán"), ("HOAN_TIEN", "Hoàn tiền")],
    "TrangThaiGiaoDich": [("THANH_CONG", "Thành công"), ("THAT_BAI", "Thất bại")],
    "LoaiViecGiao": [("GIAO", "Giao hàng"), ("GIAO_LAP", "Giao kèm lắp đặt"), ("KHAO_SAT", "Khảo sát lắp đặt")],
    "TrangThaiPhieuGiao": [("DA_PHAN_CONG", "Đã phân công"), ("DA_GIAO", "Đã giao"), ("KHONG_THANH_CONG", "Không thành công")],
    "TrangThaiDonDat": [("DE_XUAT", "Đề xuất"), ("DA_GUI", "Đã gửi"), ("DA_NHAN", "Đã nhận"), ("DA_HUY", "Đã huỷ")],
    "TrangThaiSerial": [("TRONG_KHO", "Trong kho"), ("DA_XUAT", "Đã xuất"), ("DA_BAN", "Đã bán"), ("DANG_BAO_HANH", "Đang bảo hành"),
                        ("TRA_VE_KHO", "Trả về kho"), ("LOI_CHO_GUI_HANG", "Lỗi chờ gửi hãng")],
    "LoaiLyDoDoiTra": [("LOI_NHA_SAN_XUAT", "Lỗi nhà sản xuất"), ("KHONG_DUNG_MO_TA", "Không đúng mô tả"), ("DOI_Y", "Khách đổi ý")],
    "HinhThucDoiTra": [("DOI_MAY", "Đổi máy"), ("TRA_HANG", "Trả hàng")],
    "TrangThaiDoiTra": [("CHO_DUYET", "Chờ duyệt"), ("DA_DUYET", "Đã duyệt"), ("TU_CHOI", "Từ chối"), ("HOAN_TAT", "Hoàn tất")],
    "TrangThaiBaoHanh": [("DA_TIEP_NHAN", "Đã tiếp nhận"), ("DA_GUI_HANG", "Đã gửi hãng"), ("DA_NHAN_VE", "Đã nhận về"), ("DA_TRA_KHACH", "Đã trả khách")],
}

E = [
    ("VaiTro", [("maVaiTro", "String", "id")], [("tenVaiTro", "String", {})]),
    ("TaiKhoan", [("maTaiKhoan", "Long", "auto")], [
        ("tenDangNhap", "String", {}), ("matKhau", "String", {}), ("trangThai", "TrangThaiTaiKhoan", {}),
        ("ngayTao", "LocalDateTime", {}), ("soLanSaiLienTiep", "Integer", {}), ("khoaDenLuc", "LocalDateTime", {}),
        ("phaiDoiMatKhau", "Boolean", {}),
        ("vaiTro", "M2M:VaiTro:TaiKhoanVaiTro:maTaiKhoan:maVaiTro", {"eager": True})]),
    ("NhanVien", [("maNhanVien", "Long", "auto")], [
        ("hoTen", "String", {}), ("soDienThoai", "String", {}), ("taiKhoan", "O2O:TaiKhoan:maTaiKhoan", {})]),
    ("KhachHang", [("maKhachHang", "Long", "auto")], [
        ("hoTen", "String", {}), ("soDienThoai", "String", {}), ("email", "String", {}), ("ngayDangKy", "LocalDateTime", {}),
        ("taiKhoan", "O2O:TaiKhoan:maTaiKhoan", {})]),
    ("DiaChiGiaoHang", [("maDiaChi", "Long", "auto")], [
        ("khachHang", "M2O:KhachHang:maKhachHang", {}), ("hoTenNguoiNhan", "String", {}), ("soDienThoai", "String", {}),
        ("diaChi", "String", {}), ("noiThanh", "Boolean", {}), ("macDinh", "Boolean", {})]),
    ("DanhMuc", [("maDanhMuc", "String", "id")], [("tenDanhMuc", "String", {}), ("thuTu", "Integer", {})]),
    ("ThuongHieu", [("maThuongHieu", "Long", "auto")], [("tenThuongHieu", "String", {}), ("xuatXu", "String", {})]),
    ("SanPham", [("maSanPham", "Long", "auto")], [
        ("tenSanPham", "String", {}), ("model", "String", {}), ("danhMuc", "M2O:DanhMuc:maDanhMuc", {}),
        ("thuongHieu", "M2O:ThuongHieu:maThuongHieu", {}), ("giaBan", "Long", {}), ("giaNiemYet", "Long", {}),
        ("thoiHanBaoHanh", "Integer", {}), ("canLapDat", "Boolean", {}), ("moTa", "TEXT", {}), ("anh", "String", {}),
        ("trangThai", "TrangThaiSanPham", {}), ("ngayTao", "LocalDateTime", {}),
        ("thongSo", "O2M:ThongSoKyThuat:sanPham", {})]),
    ("ThongSoKyThuat", [("maThongSo", "Long", "auto")], [
        ("sanPham", "M2O:SanPham:maSanPham", {}), ("tenThongSo", "String", {}), ("giaTri", "String", {})]),
    ("LichSuGia", [("maLichSu", "Long", "auto")], [
        ("sanPham", "M2O:SanPham:maSanPham", {}), ("giaCu", "Long", {}), ("giaMoi", "Long", {}), ("lyDo", "String", {}),
        ("duoiGiaNhap", "Boolean", {}), ("thoiDiem", "LocalDateTime", {}), ("nhanVien", "M2O:NhanVien:maNhanVien", {})]),
    ("KhuyenMai", [("maKhuyenMai", "Long", "auto")], [
        ("tenKhuyenMai", "String", {}), ("loai", "LoaiKhuyenMai", {}), ("maCode", "String", {}), ("kieuGiam", "KieuGiam", {}),
        ("mucGiam", "Long", {}), ("giamToiDa", "Long", {}), ("donToiThieu", "Long", {}), ("quaTang", "String", {}),
        ("ngayBatDau", "LocalDate", {}), ("ngayKetThuc", "LocalDate", {}), ("soLuotToiDa", "Integer", {}),
        ("soLuotDaDung", "Integer", {}), ("trangThai", "TrangThaiKhuyenMai", {}), ("nguoiTao", "M2O:NhanVien:maNguoiTao", {}),
        ("sanPham", "M2M:SanPham:KhuyenMaiSanPham:maKhuyenMai:maSanPham", {})]),
    ("DonHang", [("maDonHang", "Long", "auto")], [
        ("khachHang", "M2O:KhachHang:maKhachHang", {}), ("hoTenNguoiNhan", "String", {}), ("soDienThoaiNhan", "String", {}),
        ("email", "String", {}), ("diaChiGiao", "String", {}), ("noiThanh", "Boolean", {}), ("canLapDat", "Boolean", {}),
        ("ngayDat", "LocalDateTime", {}), ("ngayHoanThanh", "LocalDateTime", {}), ("tienHang", "Long", {}), ("tienGiam", "Long", {}),
        ("phiGiao", "Long", {}), ("phiLapDat", "Long", {}), ("tongTien", "Long", {}), ("tienCoc", "Long", {}),
        ("phuongThucThanhToan", "PhuongThucThanhToan", {}), ("trangThaiThanhToan", "TrangThaiThanhToan", {}),
        ("trangThai", "TrangThaiDonHang", {}), ("khuyenMai", "M2O:KhuyenMai:maKhuyenMai", {}),
        ("nhanVienDuyet", "M2O:NhanVien:maNhanVienDuyet", {}), ("ghiChu", "String", {}), ("lyDoHuy", "String", {}),
        ("soHoaDon", "String", {}), ("dongYXuLyDuLieu", "Boolean", {}), ("dongYQuangCao", "Boolean", {}),
        ("chiTiet", "O2M:ChiTietDonHang:donHang", {})]),
    ("ChiTietDonHang", [("maChiTiet", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("sanPham", "M2O:SanPham:maSanPham", {}), ("tenSanPhamLucBan", "String", {}),
        ("soLuong", "Integer", {}), ("donGia", "Long", {})]),
    ("GiaoDichThanhToan", [("maGiaoDich", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("loai", "LoaiGiaoDich", {}), ("soTien", "Long", {}), ("phuongThuc", "String", {}),
        ("maGiaoDichCong", "String", {}), ("trangThai", "TrangThaiGiaoDich", {}), ("thoiDiem", "LocalDateTime", {})]),
    ("LichSuTrangThaiDon", [("maLichSu", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("trangThaiCu", "TrangThaiDonHang", {}), ("trangThaiMoi", "TrangThaiDonHang", {}),
        ("thoiDiem", "LocalDateTime", {}), ("nhanVien", "M2O:NhanVien:maNhanVien", {}), ("ghiChu", "String", {})]),
    ("DanhGia", [("maDanhGia", "Long", "auto")], [
        ("sanPham", "M2O:SanPham:maSanPham", {}), ("khachHang", "M2O:KhachHang:maKhachHang", {}),
        ("donHang", "M2O:DonHang:maDonHang", {}), ("soSao", "Integer", {}), ("noiDung", "String", {}), ("ngayDanhGia", "LocalDateTime", {})]),
    ("PhieuGiaoHang", [("maPhieuGiao", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("loaiViec", "LoaiViecGiao", {}), ("nhanVienGiao", "M2O:NhanVien:maNhanVienGiao", {}),
        ("donViVanChuyen", "String", {}), ("maVanDon", "String", {}), ("ngayHen", "LocalDate", {}), ("gioBatDau", "LocalTime", {}),
        ("gioKetThuc", "LocalTime", {}), ("lyDoTrungLich", "String", {}), ("trangThai", "TrangThaiPhieuGiao", {}),
        ("lyDoKhongThanhCong", "String", {}), ("tienThuKhiGiao", "Long", {}), ("keToanXacNhan", "M2O:NhanVien:maKeToanXacNhan", {}),
        ("thoiDiemXacNhanTien", "LocalDateTime", {}), ("nguoiPhanCong", "M2O:NhanVien:maNguoiPhanCong", {})]),
    ("PhieuLapDat", [("maPhieuLapDat", "Long", "auto")], [
        ("phieuGiao", "O2O:PhieuGiaoHang:maPhieuGiao", {}), ("ketQuaChayThu", "String", {}), ("tinhTrangCongTrinh", "String", {}),
        ("phatSinh", "String", {}), ("phiLapDat", "Long", {}), ("phiVatTu", "Long", {}), ("khachDaKy", "Boolean", {}),
        ("ngayLap", "LocalDateTime", {})]),
    ("NhaCungCap", [("maNhaCungCap", "Long", "auto")], [
        ("tenNhaCungCap", "String", {}), ("soDienThoai", "String", {}), ("email", "String", {}), ("diaChi", "String", {}),
        ("dangHopTac", "Boolean", {})]),
    ("DonDatHangNCC", [("maDonDat", "Long", "auto")], [
        ("nhaCungCap", "M2O:NhaCungCap:maNhaCungCap", {}), ("ngayLap", "LocalDateTime", {}), ("trangThai", "TrangThaiDonDat", {}),
        ("nguoiLap", "M2O:NhanVien:maNguoiLap", {}), ("nguoiGui", "M2O:NhanVien:maNguoiGui", {}), ("ghiChu", "String", {}),
        ("chiTiet", "O2M:ChiTietDonDatHangNCC:donDat", {})]),
    ("ChiTietDonDatHangNCC", [("donDat", "M2O:DonDatHangNCC:maDonDat", "idrel"), ("sanPham", "M2O:SanPham:maSanPham", "idrel")], [
        ("soLuong", "Integer", {})]),
    ("PhieuNhap", [("maPhieuNhap", "Long", "auto")], [
        ("nhaCungCap", "M2O:NhaCungCap:maNhaCungCap", {}), ("donDat", "M2O:DonDatHangNCC:maDonDat", {}), ("soHoaDonNCC", "String", {}),
        ("ngayNhap", "LocalDateTime", {}), ("thuKho", "M2O:NhanVien:maThuKho", {}), ("ghiChu", "String", {}),
        ("chiTiet", "O2M:ChiTietPhieuNhap:phieuNhap", {})]),
    ("ChiTietPhieuNhap", [("maChiTietNhap", "Long", "auto")], [
        ("phieuNhap", "M2O:PhieuNhap:maPhieuNhap", {}), ("sanPham", "M2O:SanPham:maSanPham", {}), ("soLuongChungTu", "Integer", {}),
        ("soLuongThucNhap", "Integer", {}), ("donGiaNhap", "Long", {})]),
    ("SanPhamSerial", [("soSerial", "String", "id")], [
        ("sanPham", "M2O:SanPham:maSanPham", {}), ("chiTietNhap", "M2O:ChiTietPhieuNhap:maChiTietNhap", {}),
        ("ngayNhap", "LocalDateTime", {}), ("ngayBan", "LocalDateTime", {}), ("donHangBan", "M2O:DonHang:maDonHangBan", {}),
        ("trangThai", "TrangThaiSerial", {})]),
    ("PhieuXuat", [("maPhieuXuat", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("ngayXuat", "LocalDateTime", {}), ("lyDoXuat", "String", {}),
        ("thuKho", "M2O:NhanVien:maThuKho", {}), ("serial", "M2M:SanPhamSerial:ChiTietPhieuXuat:maPhieuXuat:soSerial", {})]),
    ("YeuCauDoiTra", [("maYeuCau", "Long", "auto")], [
        ("donHang", "M2O:DonHang:maDonHang", {}), ("ngayYeuCau", "LocalDateTime", {}), ("loaiLyDo", "LoaiLyDoDoiTra", {}),
        ("lyDo", "String", {}), ("hinhThuc", "HinhThucDoiTra", {}), ("tinhTrangMay", "String", {}), ("conSeal", "Boolean", {}),
        ("phiKhauTru", "Long", {}), ("soTienHoan", "Long", {}), ("laNgoaiLe", "Boolean", {}), ("ghiChuYKienChu", "String", {}),
        ("trangThai", "TrangThaiDoiTra", {}), ("nhanVienTiepNhan", "M2O:NhanVien:maNhanVienTiepNhan", {}),
        ("nguoiDuyet", "M2O:NhanVien:maNguoiDuyet", {}), ("ngayDuyet", "LocalDateTime", {}), ("lyDoTuChoi", "String", {}),
        ("serial", "M2M:SanPhamSerial:ChiTietDoiTra:maYeuCau:soSerial", {})]),
    ("PhieuBaoHanh", [("maPhieu", "Long", "auto")], [
        ("serial", "M2O:SanPhamSerial:soSerial", {}), ("hoTenKhach", "String", {}), ("soDienThoaiKhach", "String", {}),
        ("ngayNhan", "LocalDateTime", {}), ("moTaLoi", "String", {}), ("tinhTrangBenNgoai", "String", {}), ("ngayHenTra", "LocalDate", {}),
        ("lanThu", "Integer", {}), ("ngayGuiHang", "LocalDateTime", {}), ("ngayNhanTuHang", "LocalDateTime", {}),
        ("ngayTraKhach", "LocalDateTime", {}), ("ketQua", "String", {}), ("trangThai", "TrangThaiBaoHanh", {}),
        ("nhanVien", "M2O:NhanVien:maNhanVien", {})]),
]

ID_TYPES = {"Long": "Long", "String": "String"}


def hoa(s):
    return s[0].upper() + s[1:]


def sinh_enum(ten, gt):
    gia_tri = ",\n".join(f'    {k}("{v}")' for k, v in gt)
    return f"""package {PKG}.thucthe;

public enum {ten} {{
{gia_tri};

    private final String nhan;

    {ten}(String nhan) {{
        this.nhan = nhan;
    }}

    public String getNhan() {{
        return nhan;
    }}
}}
"""


def sinh_lop(ten, ids, truong):
    imp = {"jakarta.persistence.*"}
    than, gs = [], []
    co_id_lop = any(k == "idrel" for _, _, k in ids)
    for f, t, kieu in ids:
        if kieu == "auto":
            than.append(f"    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private {t} {f};")
            gs.append((f, t))
        elif kieu == "id":
            than.append(f"    @Id\n    private {t} {f};")
            gs.append((f, t))
        else:
            _, dich, cot = t.split(":")
            than.append(f"    @Id\n    @ManyToOne(fetch = FetchType.LAZY)\n    @JoinColumn(name = \"{cot}\")\n    private {dich} {f};")
            gs.append((f, dich))
    for f, t, o in truong:
        if t.startswith("M2O:"):
            _, dich, cot = t.split(":")
            than.append(f"    @ManyToOne(fetch = FetchType.LAZY)\n    @JoinColumn(name = \"{cot}\")\n    private {dich} {f};")
            gs.append((f, dich))
        elif t.startswith("O2O:"):
            _, dich, cot = t.split(":")
            than.append(f"    @OneToOne(fetch = FetchType.LAZY)\n    @JoinColumn(name = \"{cot}\")\n    private {dich} {f};")
            gs.append((f, dich))
        elif t.startswith("O2M:"):
            _, dich, mb = t.split(":")
            imp |= {"java.util.ArrayList", "java.util.List"}
            than.append(f"    @OneToMany(mappedBy = \"{mb}\", cascade = CascadeType.ALL, orphanRemoval = true)\n    private List<{dich}> {f} = new ArrayList<>();")
            gs.append((f, f"List<{dich}>"))
        elif t.startswith("M2M:"):
            _, dich, bang, c1, c2 = t.split(":")
            imp |= {"java.util.LinkedHashSet", "java.util.Set"}
            fetch = "FetchType.EAGER" if o.get("eager") else "FetchType.LAZY"
            than.append(f"    @ManyToMany(fetch = {fetch})\n    @JoinTable(name = \"{bang}\", joinColumns = @JoinColumn(name = \"{c1}\"), inverseJoinColumns = @JoinColumn(name = \"{c2}\"))\n    private Set<{dich}> {f} = new LinkedHashSet<>();")
            gs.append((f, f"Set<{dich}>"))
        elif t == "TEXT":
            than.append(f"    @Column(columnDefinition = \"TEXT\")\n    private String {f};")
            gs.append((f, "String"))
        elif t in ENUM:
            imp |= {"org.hibernate.annotations.JdbcTypeCode", "org.hibernate.type.SqlTypes"}
            than.append(f"    @Enumerated(EnumType.STRING)\n    @JdbcTypeCode(SqlTypes.VARCHAR)\n    private {t} {f};")
            gs.append((f, t))
        else:
            if t in ("LocalDateTime", "LocalDate", "LocalTime"):
                imp.add(f"java.time.{t}")
            than.append(f"    private {t} {f};")
            gs.append((f, t))
    phuong_thuc = []
    for f, t in gs:
        phuong_thuc.append(f"    public {t} get{hoa(f)}() {{\n        return {f};\n    }}\n\n    public void set{hoa(f)}({t} {f}) {{\n        this.{f} = {f};\n    }}")
    id_class = ""
    if co_id_lop:
        imp |= {"java.io.Serializable", "java.util.Objects"}
        id_class = f"@IdClass({ten}.Khoa.class)\n"
        ks = [(f, t.split(":")[1]) for f, t, _ in ids]
        kt = "\n".join(f"        private Long {f};" for f, _ in ks)
        eq = " && ".join(f"Objects.equals({f}, o.{f})" for f, _ in ks)
        hs = ", ".join(f for f, _ in ks)
        get = "\n\n".join(f"        public Long get{hoa(f)}() {{\n            return {f};\n        }}\n\n        public void set{hoa(f)}(Long {f}) {{\n            this.{f} = {f};\n        }}" for f, _ in ks)
        khoa = f"""

    public static class Khoa implements Serializable {{
{kt}

        public Khoa() {{
        }}

{get}

        @Override
        public boolean equals(Object x) {{
            if (this == x) return true;
            if (!(x instanceof Khoa o)) return false;
            return {eq};
        }}

        @Override
        public int hashCode() {{
            return Objects.hash({hs});
        }}
    }}"""
    else:
        khoa = ""
    imports = "\n".join(f"import {i};" for i in sorted(imp))
    return f"""package {PKG}.thucthe;

{imports}

@Entity
@Table(name = "{ten}")
{id_class}public class {ten} {{

{chr(10).join(x + chr(10) for x in than)}
{chr(10).join(x + chr(10) for x in phuong_thuc)}{khoa}
}}
"""


def main():
    os.makedirs(TT, exist_ok=True)
    os.makedirs(DAO, exist_ok=True)
    for ten, gt in ENUM.items():
        io.open(os.path.join(TT, ten + ".java"), "w", encoding="utf-8", newline="\n").write(sinh_enum(ten, gt))
    for ten, ids, truong in E:
        io.open(os.path.join(TT, ten + ".java"), "w", encoding="utf-8", newline="\n").write(sinh_lop(ten, ids, truong))
        id_t = "Long" if ids[0][2] == "auto" else ("String" if ids[0][2] == "id" else f"{ten}.Khoa")
        p = os.path.join(DAO, ten + "DAO.java")
        if not os.path.exists(p):
            io.open(p, "w", encoding="utf-8", newline="\n").write(f"""package {PKG}.khodulieu;

import {PKG}.thucthe.{ten};
import org.springframework.data.jpa.repository.JpaRepository;

public interface {ten}DAO extends JpaRepository<{ten}, {id_t}> {{
}}
""")
    print("xong", len(E), "lop", len(ENUM), "enum")


if __name__ == "__main__":
    main()
