package open.example.easyspringsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义鉴定异常，鉴定该用户没权限访问失败会在这里处理
 * （可能出现的情况:如果授权失败不走这个逻辑，那可能是因为GlobalExceptionHandler 全局异常处理器会比 AccessDeniedHandler 先捕获 AccessDeniedException 异常，则在全局异常处理即可）
 */
@Component
public class SpringSecurityAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>(8);
        resp.put("status", 500);
        resp.put("msg", "拒绝访问,当前无该权限");
        resp.put("exception",accessDeniedException.getMessage());
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resp);
        response.getWriter().write(s);
    }
}
