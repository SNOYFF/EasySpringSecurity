package open.example.easyspringsecurity.config;

import open.example.easyspringsecurity.common.RedisKeyConstant;
import open.example.easyspringsecurity.domain.Authority;
import open.example.easyspringsecurity.domain.User;
import open.example.easyspringsecurity.service.UserService;
import open.example.easyspringsecurity.utils.GsonUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 实现自定义登录用户的认证
 */
@Service
public class SpringSecurityUserDetailsService implements UserDetailsService {

    /**
     * redis工具
     */
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * user类服务
     */
    @Resource
    private UserService userService;

    /**
     * 自定义根据用户名加载用户,这里使用了redis缓存
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        User user =new User();
        String userKey = RedisKeyConstant.USER_KEY+username;
        if (redisTemplate.hasKey(userKey)) {
            if (!Objects.isNull(redisTemplate.opsForValue().get(userKey))) {
                user = GsonUtil.fromJson(redisTemplate.opsForValue().get(userKey).toString(), User.class);
            }
        }else {
            user = userService.loadUserByUsername(username);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("用户不存在!");
            }
            redisTemplate.opsForValue().set(userKey, GsonUtil.toJson(user),RedisKeyConstant.TIME_OUT_DAY,TimeUnit.SECONDS);
        }
        Authority authority = UserService.user2Authorities(user);
        // 设置该用户的角色
        String roleKey = RedisKeyConstant.ROLE_KEY+authority.getUsername();
        List<String> roleList = new ArrayList<>();
        if(redisTemplate.hasKey(roleKey)) {
            String[] strings = GsonUtil.fromJson(redisTemplate.opsForValue().get(roleKey).toString(), String[].class);
            roleList = Arrays.asList(strings);
        }else {
            roleList = userService.getRolesByUid(authority.getId());
            redisTemplate.opsForValue().set(roleKey, GsonUtil.toJson(roleList),RedisKeyConstant.TIME_OUT_DAY, TimeUnit.SECONDS);
        }
        authority.setRoles(roleList);
        return authority;
    }

}
