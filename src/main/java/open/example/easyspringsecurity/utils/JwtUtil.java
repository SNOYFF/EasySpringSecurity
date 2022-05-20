package open.example.easyspringsecurity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import open.example.easyspringsecurity.common.SpringSecurityJwtConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
public class JwtUtil {

    /**
     * 创建时间
     */
    private static final String CLAIM_KEY_CREATED = "created";
    /**
     * jwt属性:主题，这里就设置为用户名,Claims有7种属性
     */
    private static final String CLAIM_KEY_USERNAME = "sub";

    /**
     * 生成token
     */
    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>(8);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_USERNAME, username);
        return Jwts.builder()
                // 自定义属性
                .setClaims(claims)
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + SpringSecurityJwtConstant.EXPIRATION_TIME))
                // 签名
                .signWith(SignatureAlgorithm.HS256, SpringSecurityJwtConstant.JWT_SIGN_KEY)
                .compact();
    }

    /**
     * 解析
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SpringSecurityJwtConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 验证Token是否有效
     */
    public static Boolean parseTokenIsValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SpringSecurityJwtConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 获取用户名
     */
    public static String getUsernameFromToken(String token) {
        try {
            return getClaimsFromToken(token).getSubject();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 刷新token
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + SpringSecurityJwtConstant.EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS256, SpringSecurityJwtConstant.JWT_SIGN_KEY).compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

}