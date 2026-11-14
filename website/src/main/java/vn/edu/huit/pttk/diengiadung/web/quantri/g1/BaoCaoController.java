package vn.edu.huit.pttk.diengiadung.web.quantri.g1;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.BaoCaoBUS;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g1.BaoCaoDoanhThu;

@Controller
@RequestMapping("/quan-tri/bao-cao")
public class BaoCaoController {

    private final BaoCaoBUS bus;

    public BaoCaoController(BaoCaoBUS bus) {
        this.bus = bus;
    }

    @GetMapping("/doanh-thu")
    public String doanhThu(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                           @RequestParam(defaultValue = "ngay") String nhom, Model model) {
        LocalDate tu = tuNgay == null ? LocalDate.now().withDayOfMonth(1) : tuNgay;
        LocalDate den = denNgay == null ? LocalDate.now() : denNgay;
        model.addAttribute("tuNgay", tu);
        model.addAttribute("denNgay", den);
        model.addAttribute("nhom", nhom);
        BaoCaoDoanhThu bc;
        try {
            bc = bus.doanhThu(tu, den, nhom);
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
            bc = bus.doanhThu(LocalDate.now().withDayOfMonth(1), LocalDate.now(), "ngay");
        }
        model.addAttribute("bc", bc);
        return "quan-tri/g1/doanh-thu";
    }

    @GetMapping("/doanh-thu/xuat")
    public ResponseEntity<byte[]> xuat(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                                       @RequestParam(defaultValue = "ngay") String nhom) {
        try {
            BaoCaoDoanhThu bc = bus.doanhThu(tuNgay, denNgay, nhom);
            DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE;
            String ten = "bao-cao-doanh-thu_" + tuNgay.format(f) + "_" + denNgay.format(f) + ".csv";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(ten).build().toString())
                    .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                    .body(bus.xuatCsv(bc));
        } catch (LoiNghiepVu e) {
            return ResponseEntity.status(302).header(HttpHeaders.LOCATION,
                    "/quan-tri/bao-cao/doanh-thu?tuNgay=" + tuNgay + "&denNgay=" + denNgay + "&nhom=" + nhom).build();
        }
    }

    @GetMapping("/ton-kho")
    public String tonKho(Model model) {
        model.addAttribute("dsTon", bus.tonKho());
        model.addAttribute("sapHet", bus.mauSapHet());
        model.addAttribute("baoHanh", bus.phieuBaoHanhDangMo());
        model.addAttribute("mucToiThieu", BaoCaoBUS.MUC_TON_TOI_THIEU);
        return "quan-tri/g1/ton-kho";
    }
}
