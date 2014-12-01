package com.ramotion.roadmap.config;

import com.ramotion.roadmap.controllers.APIMappings;
import org.apache.log4j.Logger;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.annotation.PostConstruct;

/**
 * Created by Oleg Vasiliev on 01.12.2014.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("bob").password("123").roles("USER").and()
                .withUser("alice").password("123").roles("USER").and()
                .withUser("mike").password("123").roles("USER");
    }
}
