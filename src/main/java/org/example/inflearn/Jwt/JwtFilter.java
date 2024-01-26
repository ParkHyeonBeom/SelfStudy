package org.example.inflearn.Jwt;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.Entity.Seller;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    // Spring Security를 통해 전달 받은 secretKey
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청에 대한 정보가 담긴 Request에서 Header를 추출
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더에 담긴 JWT 토큰을 추출하기 위한 작업, Jwt 토큰은 Bearer로 시작하는 특징을 활용
        String token;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.split(" ")[1];
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // 이후 사용자를 구별하기위해 Email을 통해 사용자 탐색
        String email = JwtUtils.getUserEmail(token, secretKey);
        Long idx = JwtUtils.getUserIdx(token, secretKey);


        // 소비자인지 먼저 확인
        Customer customer = memberService.getCustomerByCustomerId(email);

        if(customer != null)
        {
            String customerEmail = customer.getUsername();

        if (!JwtUtils.validate(token, customerEmail, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                Customer.builder().customerEmail(email).customerIdx(idx).build(), null,
                customer.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
        }

        // 소비자가 아니면 판매자일것이다
        else
        {
            Seller seller = memberService.getSellerBySellerId(email);

            String sellerEmail = seller.getUsername();

            if (!JwtUtils.validate(token, sellerEmail, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    seller.getSellerEmail(), null,
                    seller.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }
    }
}
