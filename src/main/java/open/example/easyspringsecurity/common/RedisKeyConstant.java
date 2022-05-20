package open.example.easyspringsecurity.common;

import lombok.Data;

/**
 * redis-->key-->缓存命名前缀
 */
@Data
public class RedisKeyConstant {
    /**
     * 过期时间：天
     */
    public static Integer TIME_OUT_DAY = 24*60*60;

    public static final String USER_KEY ="user-";
    public static final String ROLE_KEY ="role-";
    public static final String MENU_KEY ="menu-";

}
