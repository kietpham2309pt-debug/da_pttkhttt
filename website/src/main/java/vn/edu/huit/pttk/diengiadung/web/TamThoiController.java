package vn.edu.huit.pttk.diengiadung.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TamThoiController {

    @GetMapping("/")
    public String trangChu() {
        return "mau-khach";
    }

    @GetMapping("/quan-tri")
    public String tongQuan() {
        return "quan-tri/mau";
    }
}
