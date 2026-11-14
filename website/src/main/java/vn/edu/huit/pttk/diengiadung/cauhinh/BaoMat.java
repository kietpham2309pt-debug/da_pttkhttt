package vn.edu.huit.pttk.diengiadung.cauhinh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.huit.pttk.diengiadung.nghiepvu.XuLyDangNhap;

@Configuration
public class BaoMat {

    public static final String[] NHAN_VIEN = {"QUAN_TRI", "QUAN_LY", "NV_BAN_HANG", "THU_KHO", "NV_GIAO_HANG", "KE_TOAN"};

    @Bean
    public PasswordEncoder maHoaMatKhau() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain chuoiLoc(HttpSecurity http, XuLyDangNhap xuLy) throws Exception {
        http.authorizeHttpRequests(q -> q
                        .requestMatchers("/quan-tri/tai-khoan/**").hasRole("QUAN_TRI")
                        .requestMatchers("/quan-tri/danh-muc/**", "/quan-tri/thuong-hieu/**", "/quan-tri/khuyen-mai/**").hasRole("QUAN_LY")
                        .requestMatchers("/quan-tri/san-pham/**").hasAnyRole("NV_BAN_HANG", "QUAN_LY")
                        .requestMatchers("/quan-tri/don-hang/**").hasAnyRole("NV_BAN_HANG", "QUAN_LY")
                        .requestMatchers("/quan-tri/giao-hang/**").hasAnyRole("QUAN_LY", "NV_GIAO_HANG", "NV_BAN_HANG")
                        .requestMatchers("/quan-tri/tien-thu/**").hasRole("KE_TOAN")
                        .requestMatchers("/quan-tri/doi-tra/**").hasAnyRole("NV_BAN_HANG", "QUAN_LY", "KE_TOAN")
                        .requestMatchers("/quan-tri/kho/**").hasAnyRole("THU_KHO", "QUAN_LY")
                        .requestMatchers("/quan-tri/bao-hanh/**").hasAnyRole("NV_BAN_HANG", "THU_KHO")
                        .requestMatchers("/quan-tri/bao-cao/**").hasAnyRole("QUAN_LY", "KE_TOAN")
                        .requestMatchers("/quan-tri", "/quan-tri/**").hasAnyRole(NHAN_VIEN)
                        .requestMatchers("/tai-khoan/**", "/danh-gia/**").hasRole("KHACH_HANG")
                        .requestMatchers("/doi-mat-khau").authenticated()
                        .anyRequest().permitAll())
                .formLogin(f -> f.loginPage("/dang-nhap").loginProcessingUrl("/dang-nhap")
                        .usernameParameter("tenDangNhap").passwordParameter("matKhau")
                        .successHandler(xuLy).failureHandler(xuLy).permitAll())
                .logout(l -> l.logoutUrl("/dang-xuat").logoutSuccessUrl("/?daDangXuat"))
                .exceptionHandling(e -> e.accessDeniedPage("/khong-co-quyen"));
        return http.build();
    }
}
