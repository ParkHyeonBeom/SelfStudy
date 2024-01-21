package org.example.inflearn.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.Entity.Seller;

import java.security.Key;
import java.util.Date;

// JWT를 생성하고 검증하는 역할 수행
public class JwtUtils {

    // 오버로딩을 활용한 Customer, Seller의  AccessToken 메소드 분리 구현
    // Key는 Spring Security를 거쳐서 전달받는 SecretKey를 의미한다.

    public static String generateAccessToken(Customer customer, String secretkey, int expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("idx", customer.getCustomerIdx());
        claims.put("email", customer.getCustomerEmail());
        claims.put("grade",customer.getCustomerGrade());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSignKey(secretkey), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static String generateAccessToken(Seller seller, String key, int expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("idx", seller.getSellerIdx());
        claims.put("email", seller.getSellerEmail());
        claims.put("grade",seller.getSellerGrade());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSignKey(key), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static Key getSignKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public static Boolean validate(String token, String userEmail, String key) {

        String usernameByToken = getUserEmail(token, key);

        Date expireTime = extractAllClaims(token, key).getExpiration();
        Boolean result = expireTime.before(new Date(System.currentTimeMillis()));

        return usernameByToken.equals(userEmail) && !result;
    }

    public static String getUserEmail(String token, String key) {
        return extractAllClaims(token, key).get("email", String.class);
    }

    public static Long getUserIdx(String token, String key) {
        return extractAllClaims(token, key).get("idx", Long.class);
    }

    // extractAllclaims() : 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
    public static Claims extractAllClaims(String token, String key) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
