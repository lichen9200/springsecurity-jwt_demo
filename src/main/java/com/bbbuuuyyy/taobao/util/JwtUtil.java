package com.bbbuuuyyy.taobao.util;

import com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//要不要交给springboot管理？？
public class JwtUtil {
    private static final String SECRET_KEY = "your-secret-key";  // 密钥，用于签署和验证令牌
    private static final long EXPIRATION_TIME = 86400000;  // 令牌的过期时间（以毫秒为单位），这里设置为24小时

    // 生成JWT令牌
    public static String generateToken(UserDetail userDetail) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetail.getUsername());
    }
//自定义的生成token的方法，先用着
    public static String generate(String userName) {
        // 过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userName) // 将userName放进JWT
                .setIssuedAt(new Date()) // 设置JWT签发时间
                .setExpiration(expiryDate)  // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 设置加密算法和秘钥
                .compact();
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        // 使用Jwts.builder()创建JWT令牌，并设置相关声明、主题、签发时间、过期时间和签名算法
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 验证JWT令牌是否有效
    public static Boolean validateToken(String token, UserDetail userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 从JWT令牌中提取用户名
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从JWT令牌中提取过期时间
    public static Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //实现通用的声明值提取逻辑

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        // 使用Jwts.parser()解析JWT令牌，并设置签名密钥，然后获取令牌中的所有声明
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token) {
        final Date expirationDate = extractExpirationDate(token);
        return expirationDate.before(new Date());
    }
}

