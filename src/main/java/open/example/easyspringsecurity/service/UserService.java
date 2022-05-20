package open.example.easyspringsecurity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import open.example.easyspringsecurity.dao.RoleDao;
import open.example.easyspringsecurity.dao.UserDao;
import open.example.easyspringsecurity.domain.User;
import open.example.easyspringsecurity.domain.Authority;
import open.example.easyspringsecurity.enums.YesOrNoEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    public User loadUserByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        User user = userDao.selectOne(userQueryWrapper);
        return user;
    }

    public List<String> getRolesByUid(String userId ) {
        return roleDao.selectRoleList(userId);
    }

    /**
     * User 转 Authorities
     * @param user
     * @return
     */
    public static Authority user2Authorities(User user) {
        Authority authority = new Authority();
        authority.setId(user.getId());
        authority.setUsername(user.getUsername());
        authority.setPassword(user.getPassword());
        authority.setAccountNonExpired(YesOrNoEnum.YES.getCode().equals(user.getAccountNonExpired()));
        authority.setEnabled(YesOrNoEnum.YES.getCode().equals(user.getEnabled()));
        authority.setAccountNonLocked(YesOrNoEnum.YES.getCode().equals(user.getAccountNonLocked()));
        authority.setCredentialsNonExpired(YesOrNoEnum.YES.getCode().equals(user.getCredentialsNonExpired()));
        return authority;
    }

}
