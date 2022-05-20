package open.example.easyspringsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义注销逻辑
 */
public class SpringSecurityAuthenticationLogoutHandler implements LogoutSuccessHandler {

    /**
     *
     * @param request
     * @param response
     * @param authentication 登出前的用户实体类对象
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>(8);
        resp.put("status", 200);
        resp.put("msg", "登出成功!");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resp);
        response.getWriter().write(s);
    }
}