package open.example.easyspringsecurity.config;

import open.example.easyspringsecurity.common.RedisKeyConstant;
import open.example.easyspringsecurity.domain.vo.MenuListVo;
import open.example.easyspringsecurity.service.MenuService;
import open.example.easyspringsecurity.utils.GsonUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 实现根据请求路径判断用户是否有权限访问逻辑 ==>根据路径找出所需要的角色。
 */
@Component
public class SpringSecurityUrlByRoleHandler implements FilterInvocationSecurityMetadataSource {

    /**
     * redis工具
     */
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 设置一个不存在的角色，来处理如果路径匹配不上统一执行拒绝处理
     */
    private static final String NONE_ROLE ="ROLE_NONE";

    /**
     * 获取url需要的权限
     */
    @Resource
    private MenuService menuService;

    /**
     * 路径必须符合AntPathMatcher规则，最好路径是 /hello/** 格式,/**就是当前路径下子路径（包含）所有的请求，也可以/hello
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        // 查询全部菜单
        List<MenuListVo> allMenu;
        if (redisTemplate.hasKey(RedisKeyConstant.MENU_KEY)) {
            MenuListVo[] menuListVos = GsonUtil.fromJson(redisTemplate.opsForValue().get(RedisKeyConstant.MENU_KEY).toString(), MenuListVo[].class);
            allMenu = Arrays.asList(menuListVos);
        }else {
            allMenu = menuService.getMenuAllList();
            redisTemplate.opsForValue().set(RedisKeyConstant.MENU_KEY, GsonUtil.toJson(allMenu),RedisKeyConstant.TIME_OUT_DAY, TimeUnit.SECONDS);
        }
        List<String> rolesList = new ArrayList<>();
        for (MenuListVo menu : allMenu) {
            if (antPathMatcher.match(menu.getUrl(), requestURI)) {
                rolesList.add(menu.getName());
            }
        }
        if (rolesList.size() > 0) {
            // 返回当前路径需要的角色，以便让后续SpringSecurity权鉴用户是否有权限执行这次请求。
            return SecurityConfig.createList(StringUtils.toStringArray(rolesList));
        }
        // 如果匹配不上，则可以设置一个不存在的角色，这样就可以拒绝全部的请求。
        // 又或者可以设置返回空，如果SpringSecuritySecurityConfig 里面 setRejectPublicInvocations 为 false则允许该用户访问，反之则不行
        return SecurityConfig.createList(NONE_ROLE);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
