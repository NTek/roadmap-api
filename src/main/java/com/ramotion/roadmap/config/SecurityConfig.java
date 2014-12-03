package com.ramotion.roadmap.config;

import com.ramotion.roadmap.controllers.APIMappings;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.annotation.PostConstruct;

/**
 * Created by Oleg Vasiliev on 01.12.2014.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());


    public static final String AUTH_BY_USERNAME_SQL = "select email,role from user where email = ?";
    public static final String USERS_BY_USERNAME_SQL = "select email,password,enabled from user where email = ?";
    public static final String CHANGE_PASSWORD_SQL = "update user set password = ? where email = ?";

    @Autowired
    private BasicDataSource basicDataSource;

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
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()

                .formLogin()
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll()
                .and()

                .logout()
                .logoutSuccessUrl("/login?logout")
                .logoutUrl("/logout")
                .permitAll()
                .and()

                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(APIMappings.Web.API_ROOT + "/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .and();

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
//        auth.inMemoryAuthentication()
//                .withUser("bob").password("123").roles("USER").and()
//                .withUser("alice").password("123").roles("USER").and()
//                .withUser("mike").password("123").roles("USER");
        auth.userDetailsService(getJdbcUserDetailsManager()).passwordEncoder(new ShaPasswordEncoder());

    }
}
