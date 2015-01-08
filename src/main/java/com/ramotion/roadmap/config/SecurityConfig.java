package com.ramotion.roadmap.config;

import com.ramotion.roadmap.utils.APIMappings;
import com.ramotion.roadmap.utils.LoginFailureHandler;
import com.ramotion.roadmap.utils.LoginSuccessHandler;
import com.ramotion.roadmap.utils.RestAuthenticationEntryPoint;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.annotation.PostConstruct;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());

    public static final String AUTH_BY_USERNAME_SQL = "select email,role from user where email = ?";
    public static final String USERS_BY_USERNAME_SQL = "select email,password,enabled from user where email = ?";
    public static final String CHANGE_PASSWORD_SQL = "update user set password = ? where email = ?";

    @Autowired
    private BasicDataSource basicDataSource;

//    @Autowired
//    private UserService userService;

    /**
     * PostConstruct actions - executes after constructor when all fields injected
     */
    @PostConstruct
    public void postConstruct() {
        LOG.info("Security config constructed");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // See https://jira.springsource.org/browse/SPR-11496
                .headers().addHeaderWriter(
                new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(getRestAuthenticationEntryPoint())
                .and()

                .formLogin()
                .loginPage("/login")
                .failureHandler(getLoginFailureHandler())
                .successHandler(getLoginSuccessHandler())
                .permitAll()
                .and()

                .logout()
                .logoutSuccessUrl("/")
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
                .and()

                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/alogin").permitAll()
                .antMatchers(APIMappings.Web.SDK_API_ROOT + "/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and();
    }

    @Bean
    public RestAuthenticationEntryPoint getRestAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public LoginFailureHandler getLoginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public LoginSuccessHandler getLoginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public JdbcUserDetailsManager getJdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(basicDataSource);
        jdbcUserDetailsManager.setChangePasswordSql(CHANGE_PASSWORD_SQL);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(AUTH_BY_USERNAME_SQL);
        jdbcUserDetailsManager.setUsersByUsernameQuery(USERS_BY_USERNAME_SQL);
        return jdbcUserDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(getJdbcUserDetailsManager()).passwordEncoder(new ShaPasswordEncoder());
    }
}
