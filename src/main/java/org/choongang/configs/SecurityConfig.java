package org.choongang.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.member.service.LoginFailureHandler;
import org.choongang.member.service.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* 인증 설정 S - 로그인, 로그아웃 */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });

        http.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login");
        });

        /* 인증 설정 E - 로그인, 로그아웃 */

        /* 인가 설정 S - 접근 통제 */
        // hasAuthority(...) hasAnyAuthority(...), hasRole, hasAnyRole
        // hasRole, hasAnyRole : db에 ROLE_롤명칭으로 저장
        http.authorizeHttpRequests(c -> {
           c.requestMatchers("/mypage/**").authenticated()  // 회원 전용
                   .requestMatchers("/admin/**")
                   .hasAnyAuthority("ADMIN", "MANAGER")
                   .anyRequest().permitAll(); // 그 외 모든 페이지는 모두 접근 가능
        });

        http.exceptionHandling(c -> {
            // 인증 실패시 기본 동작 정의
            c.authenticationEntryPoint((req, res, e) -> {
                String url = req.getRequestURI();
                if (url.indexOf("/admin") != -1) { // 관리자 페이지
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"");
                } else { // 회원전용 페이지
                    res.sendRedirect(req.getContextPath()+"/member/login");
                }
            });
        });

        /* 인가 설정 E - 접근 통제 */
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
