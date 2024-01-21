package org.example.inflearn.Config;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Jwt.JwtFilter;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 기존에 사용하던 Spring Security의 버전이 업그레이드 됨에 따라 메소드의 사용방법도 변경되었음
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final MemberService memberService;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    // csrf 비활성화
                    .csrf(AbstractHttpConfigurer::disable)

                    // 커스텀한 필터 추가
                    .addFilterBefore(new JwtFilter(memberService,secretKey), UsernamePasswordAuthenticationFilter.class)

                    // 스프링 시큐리티가 세션을 생성하지도않고 기존것을 사용하지도 않음
                    .sessionManagement((sessionManagement) ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // 권한에 맞는 페이지 제공
                    .authorizeHttpRequests((authorizeRequests) ->
                            authorizeRequests.anyRequest().permitAll());


            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
