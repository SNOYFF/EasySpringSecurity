package open.example.easyspringsecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import open.example.easyspringsecurity.common.SpringSecurityJwtConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class EasySpringSecurityApplicationTests {

    @Test
    void contextLoads() {
        // {bcrypt}XXXXX  格式
        // {bcrypt}$2a$10$.XfA37JxDQwnZHy0.nTT/eDZD9zqxaTzPkz79fhz9YrBs5gweiESm  密码为123
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("123");
        System.out.println("加密后的密码是:" + password);
    }

    @Test
    void parse() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("created", new Date());
        map.put("sub", "john");
        String token =  Jwts.builder()
                // 自定义属性
                .setClaims(map)
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + SpringSecurityJwtConstant.EXPIRATION_TIME))
                // 签名
                .signWith(SignatureAlgorithm.HS256, SpringSecurityJwtConstant.JWT_SIGN_KEY)
                .compact();
        System.out.println(token);

        Claims claims = Jwts.parser()
                .setSigningKey(SpringSecurityJwtConstant.JWT_SIGN_KEY)
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims);
    }

}
