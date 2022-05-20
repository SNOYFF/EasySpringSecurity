package open.example.easyspringsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import open.example.easyspringsecurity.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {

    // 更新或者删除用户的时候，要直接把该用户的缓存删掉，或者更新缓存。
    // 新增用户 密码需要 {bcrypt}XXXX格式 使用BCryptPasswordEncoder加密。
    // 更新用户
    // 删除用户
    // 查询用户

}
