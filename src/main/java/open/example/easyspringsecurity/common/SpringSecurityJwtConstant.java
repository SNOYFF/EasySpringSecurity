package open.example.easyspringsecurity.common;

/**
 * Jwt使用参数
 */
public class SpringSecurityJwtConstant {
    /**
     * token前缀
     */
    public static final String TOKEN_SPLIT = "Bearer ";

    /**
     * Jwt签名加密秘钥,可以自定义
     */
    public static final String JWT_SIGN_KEY = "JWTSIGNKEY";

    /**
     * Headers请求头key
     */
    public static final String HEADER = "accessToken";

    /**
     * JWT过期时间, 毫秒
     */
    public static final Long EXPIRATION_TIME = (long) (24 * 60 * 1000);
}
