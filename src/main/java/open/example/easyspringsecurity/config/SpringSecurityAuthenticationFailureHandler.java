package open.example.easyspringsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录失败逻辑
 */
public class SpringSecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>(8);
        resp.put("status", 500);
        if (exception instanceof LockedException) {
            resp.put("msg", "账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            resp.put("msg", "密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            resp.put("msg", "账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            resp.put("msg", "账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException) {
            resp.put("msg", "用户名或者密码输入错误，请重新输入!");
        }else{
            resp.put("msg", "登录失败!" + exception.getMessage());
        }
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resp);
        response.getWriter().write(s);
    }
}
