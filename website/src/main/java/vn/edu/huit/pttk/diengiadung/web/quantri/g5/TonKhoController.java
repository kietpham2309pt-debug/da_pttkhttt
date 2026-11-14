package vn.edu.huit.pttk.diengiadung.web.quantri.g5;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.pttk.diengiadung.nghiepvu.LoiNghiepVu;
import vn.edu.huit.pttk.diengiadung.nghiepvu.g5.TonKhoBUS;
import vn.edu.huit.pttk.diengiadung.thucthe.TrangThaiSerial;

@Controller
@RequestMapping("/quan-tri/kho/ton-kho")
public class TonKhoController {

    private final TonKhoBUS tonKhoBUS;

    public TonKhoController(TonKhoBUS tonKhoBUS) {
        this.tonKhoBUS = tonKhoBUS;
    }

    private void moTrang(Model model, Map<Long, String> soDem) {
        List<TonKhoBUS.DongTon> ton = tonKhoBUS.tonTheoMau();
        model.addAttribute("ton", ton);
        model.addAttribute("cacTrangThai", TrangThaiSerial.values());
        model.addAttribute("tonToiThieu", TonKhoBUS.TON_TOI_THIEU);
        model.addAttribute("soDuoi", ton.stream().filter(TonKhoBUS.DongTon::duoiToiThieu).count());
        model.addAttribute("tongTrongKho", ton.stream().mapToLong(TonKhoBUS.DongTon::trongKho).sum());
        model.addAttribute("soDem", soDem);
    }

    @GetMapping
    public String xem(Model model) {
        moTrang(model, Map.of());
        return "quan-tri/g5/ton-kho";
    }

    private static Map<Long, String> docSoDem(MultiValueMap<String, String> p) {
        Map<Long, String> kq = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : p.entrySet()) {
            if (e.getKey().startsWith("dem_") && !e.getValue().isEmpty()) {
                try {
                    kq.put(Long.parseLong(e.getKey().substring(4)), e.getValue().get(0));
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
        }
        return kq;
    }

    @PostMapping("/kiem-ke")
    public String kiemKe(@RequestParam MultiValueMap<String, String> p, Model model) {
        Map<Long, String> soDem = docSoDem(p);
        try {
            List<TonKhoBUS.DongKiemKe> kq = tonKhoBUS.kiemKe(soDem);
            model.addAttribute("kiemKe", kq);
            long lech = kq.stream().filter(d -> d.chenhLech() != null && d.chenhLech() != 0).count();
            model.addAttribute("thongBao", lech == 0 ? "Kiểm kê khớp: số đếm thực tế bằng số Trong kho trên hệ thống ở mọi mẫu đã đếm."
                    : "Có " + lech + " mẫu máy lệch giữa số đếm thực tế và hệ thống; xem bảng kết quả kiểm kê.");
        } catch (LoiNghiepVu e) {
            model.addAttribute("loi", e.getMessage());
        }
        moTrang(model, soDem);
        return "quan-tri/g5/ton-kho";
    }

    @PostMapping("/kiem-ke.csv")
    public ResponseEntity<byte[]> taiCsv(@RequestParam MultiValueMap<String, String> p) {
        List<TonKhoBUS.DongKiemKe> kq;
        try {
            kq = tonKhoBUS.kiemKe(docSoDem(p));
        } catch (LoiNghiepVu e) {
            return ResponseEntity.badRequest().contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
                    .body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder sb = new StringBuilder("﻿Mã,Tên sản phẩm,Model,Trong kho (hệ thống),Đếm thực tế,Chênh lệch\r\n");
        for (TonKhoBUS.DongKiemKe d : kq) {
            sb.append(d.maSanPham()).append(',')
                    .append(csv(d.tenSanPham())).append(',')
                    .append(csv(d.model())).append(',')
                    .append(d.heThong()).append(',')
                    .append(d.thucTe() == null ? "" : d.thucTe()).append(',')
                    .append(d.chenhLech() == null ? "" : d.chenhLech()).append("\r\n");
        }
        String ten = "kiem-ke-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")) + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ten + "\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String csv(String s) {
        if (s == null) {
            return "";
        }
        return s.contains(",") || s.contains("\"") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }
}
