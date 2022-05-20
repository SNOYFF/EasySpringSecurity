package open.example.easyspringsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import open.example.easyspringsecurity.common.SpringSecurityJwtConstant;
import open.example.easyspringsecurity.domain.Authority;
import open.example.easyspringsecurity.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录成功的逻辑
 */
public class SpringSecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
    /**
     *
     * @param request
     * @param response
     * @param authentication 登录成功后的对象(除此之外还可以通过--SecurityContextHolder.getContext().getAuthentication()--获取Authentication对象)
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>(8);
        resp.put("status", 200);
        resp.put("msg", "登录成功!");
        // 返回token
        Authority authority = (Authority)authentication.getPrincipal();
        String token = JwtUtil.createToken(authority.getUsername());
        resp.put("accessToken", SpringSecurityJwtConstant.TOKEN_SPLIT+token);
        authority.setPassword(null);
        resp.put("authority", authority);
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resp);
        response.getWriter().write(s);
    }
}
