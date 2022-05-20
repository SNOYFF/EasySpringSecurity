package open.example.easyspringsecurity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 自定义的认证的实体类
 */
@Data
public class Authority implements UserDetails {

    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户是否可用
     */
    private Boolean enabled;
    /**
     * 用户是否过期
     */
    private Boolean accountNonExpired;
    /**
     * 用户是否被锁定
     */
    private Boolean accountNonLocked;
    /**
     * 用户凭证（密码）是否过期
     */
    private Boolean credentialsNonExpired;
    /**
     * 角色（一般是从数据库查询出来的值）
     */
    @TableField(exist = false)
    private List<String> roles ;
    /**
     * 授权的值，一般对应的就是角色的值，UserDetails里的属性之一,供SpringSecurity框架授权使用
     */
    @TableField(exist = false)
    private  Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
