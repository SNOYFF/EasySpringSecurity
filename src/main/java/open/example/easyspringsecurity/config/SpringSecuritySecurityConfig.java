package open.example.easyspringsecurity.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * SpringSecurity配置类 --> 通过重写WebSecurityConfigurerAdapter实现自定义SpringSecurity逻辑
 */
@Configuration
public class SpringSecuritySecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Token认证
     */
    @Resource
    private SpringSecurityJwtFilter springSecurityJwtFilter;
    /**
     * 自定义认证失败逻辑处理
     */
    @Resource
    private SpringSecurityAuthenticationEntryPoint springSecurityAuthenticationEntryPoint;
    /**
     * 自定义鉴定失败逻辑处理
     */
    @Resource
    private SpringSecurityAccessDeniedHandler springSecurityAccessDeniedHandler;
    /**
     * 自定义根据用户判断是否有权限访问逻辑
     */
    @Resource
    private SpringSecurityUrlByRoleHandler springSecurityUrlByRoleHandler;
    /**
     * 集群环境 session管理（单机版可不用），session共享到redis
     */
    @Resource
    private FindByIndexNameSessionRepository sessionRepository;
    /**
     * 需要数据库操作SQL控制remember-me
     */
    @Resource
    private DataSource dataSource;
    /**
     * 自定义实现UserDetailsService
     */
    @Resource
    private SpringSecurityUserDetailsService springSecurityUserDetailsService;
    /**
     * 设置前端form表单里的登录用户名参数,提交默认是form表单提交而不是json格式，请求的时候注意！
     */
     private static final String USER_NAME ="username";
    /**
     * 设置前端form表单里的登录用户密码参数,提交默认是form表单提交而不是json格式，请求的时候注意！
     */
    private static final String PASS_WORD ="password";
    /**
     * Security默认登录项目url路径后缀名--仅使用Post请求，form格式提交。如果需要JSON格式提交需要实现自定义
     */
    private static final String LOGIN_URL ="login";
    /**
     * Security默认登出项目url路径后缀名--Post请求和Get请求都可以
     */
    private static final String LOGIN_OUT_URL ="logout";
    /**
     * 设置的remember-me时长（秒）
     */
    private static  int TIME_LONG = 60*60;
    /**
     * 设置集群同一用户最多同时登录数量（单机版不需要）
     */
    private static int MAXI_MUM_SESSIONS = 1;

    /**
     * 重写SpringSecurity基础配置方法 --> 主要实现了自定义的登录成功、失败和登出逻辑，定义了登录参数:用户名和密码
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        // 设置自定义根据用户判断是否有权限访问逻辑
        // 提示:如果开发频繁需要关闭权限验证，可以 1.将下面apply部分注释避免调试的时候访问被拒绝
        // 或者将setRejectPublicInvocations设置为false且 SpringSecurityUrlByRoleHandler 返回对象设置返回null(推荐)
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext))
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(springSecurityUrlByRoleHandler);
                        object.setRejectPublicInvocations(false); // false 表示springSecurityUrlByRoleConfig getAttributes 返回空允许访问该url,反之则不行
                        return object;
                    }
                })
            .and()
                .exceptionHandling()
                .accessDeniedHandler(springSecurityAccessDeniedHandler) // 自定义权鉴逻辑
                .authenticationEntryPoint(springSecurityAuthenticationEntryPoint); // 自定义认证逻辑

        http.authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .successHandler(new SpringSecurityAuthenticationSuccessHandler()) // 自定义成功登录事件
                .failureHandler(new SpringSecurityAuthenticationFailureHandler()) // 自定义失败登录事件
                .usernameParameter(USER_NAME)
                .passwordParameter(PASS_WORD)
                .permitAll()
            //.and()
            //    .rememberMe()// 开启remember-me功能 同时form表单使用key:remember-me  value:on 即可 （需要开启session才可以生效）
            //    .tokenRepository(jdbcTokenRepository()) // remember-me 实现类（持久化方案）
            //    .tokenValiditySeconds(TIME_LONG) // 控制remember-me时长(秒)
            .and()
                .logout()
                .logoutSuccessHandler(new SpringSecurityAuthenticationLogoutHandler()) // 自定义登出事件处理
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 表示不采用session,例如下面就使用了jwt
            .and()
                .addFilterAfter(springSecurityJwtFilter, UsernamePasswordAuthenticationFilter.class); // 如果采用JWT认证 则session就关闭。不采用JWT则采用下面session配置，同时关掉上面的sessionCreationPolicy部分
//                .sessionManagement()
//                .maximumSessions(MAXI_MUM_SESSIONS) // 控制最大同时登录用户数
//                .sessionRegistry(sessionRegistry()); // 设置集群session共享，单机版不需要设置集群
    }

    /**
     * 重写认证方法 --> 通过查数据库的数据认证并赋予用户角色
     ***************************************************************************************
     *   @Override
     *   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *       auth.userDetailsService(myUserDetailsService);
     *   }
     * 单数据源可以单使用 上面注释的 configure(AuthenticationManagerBuilder auth) 来实现即可（场景适用大部分足够了）
     ***************************************************************************************
     * 这个可以是配置多个据源认证（不同库，不同表的都行）,系统会依次遍历AuthenticationManager 下的 DaoAuthenticationProvider，只要你自定义实现UserDetailsService即可
     * ProviderManager 可以管理多个 DaoAuthenticationProvider, 只要有一个认证成功就终止,这个很方便你对接其他系统的用户认证。
     *
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        // 因为支持多数据源,这一步可以重复实现，即可多次==》》》重新new DaoAuthenticationProvider providerB再setUserDetailsService你的自定义DetailsService
        // ProviderManager 可以 ==》》》ProviderManager manager = new ProviderManager(providerA，providerB,providerC........)
        DaoAuthenticationProvider providerA = new DaoAuthenticationProvider();
        providerA.setUserDetailsService(springSecurityUserDetailsService);

        ProviderManager manager = new ProviderManager(providerA);
        return manager;
    }

    /**
     * 设置 remember-me 实现类（使用了持久化 相关表 就是JdbcTokenRepositoryImpl 里面的 persistent_logins,类面封装了CRUD的SQL语句）
     * form表单设置为==> key:remember-me value:on
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * 设置session集群共享(单机版可忽略)
     */
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}