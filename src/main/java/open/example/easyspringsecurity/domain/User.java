package open.example.easyspringsecurity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("user")
public class User {

    @TableId
    private String id;
    private String username;
    /**
     * 密码加密须 BCryptPasswordEncoder 加密 且 {bcrypt}XXXXX  格式 或者不加密{noop}123 格式
     * 比如你设置的密码是123，BCryptPasswordEncoder加密后假如是 ABCDE ,那么存储到数据库就必须是  {bcrypt}ABCDE,如果你想明文存储就{noop}123
     * 加密可以看 EasySpringSecurityApplicationTests
     */
    private String password;
    /**
     * 用户是否可用
     */
    private String enabled;
    /**
     * 用户是否过期
     */
    private String accountNonExpired;
    /**
     * 用户是否被锁定
     */
    private String accountNonLocked;
    /**
     * 用户凭证（密码）是否过期
     */
    private String credentialsNonExpired;

}
