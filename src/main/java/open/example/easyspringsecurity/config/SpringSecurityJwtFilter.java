package open.example.easyspringsecurity.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import open.example.easyspringsecurity.common.SpringSecurityJwtConstant;
import open.example.easyspringsecurity.domain.Authority;
import open.example.easyspringsecurity.utils.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token验证。使用手册：登录接口返回的 accessToken 还有值 Bearer eyJhbciOiJIUzI1N..... 放在header里面就可以
 */
@Configuration
public class SpringSecurityJwtFilter extends OncePerRequestFilter {

    /**
     * 自定义登录用户的认证
     */
    @Resource
    private SpringSecurityUserDetailsService springSecurityUserDetailsService;

    /**
     * 除开登录不拦截之外，其他请求统一拦截 -->想要登录不走该过滤链需要配置SpringSecuritySecurityConfig 里面的 addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 前端必须遵守的Token请求逻辑 Headers accessToken : Bearer iwtTokenString
        String tokenHead = request.getHeader(SpringSecurityJwtConstant.HEADER);
        if (StringUtils.isEmpty(tokenHead)) {
            throw new PreAuthenticatedCredentialsNotFoundException("Invalid Token");
        }
        String token = tokenHead.substring(SpringSecurityJwtConstant.TOKEN_SPLIT.length());
        if (!JwtUtil.parseTokenIsValid(token)) {
            throw new PreAuthenticatedCredentialsNotFoundException("Invalid Token");
        }
        String username = JwtUtil.getUsernameFromToken(token);
        // 获取用户信息
        Authority authority = (Authority)springSecurityUserDetailsService.loadUserByUsername(username);
        // 信息存储到SpringSecurity
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authority, null, authority.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 继续走过滤器链
        filterChain.doFilter(request, response);
    }
}
