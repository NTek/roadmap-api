package com.ramotion.roadmap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Oleg Vasiliev on 10.11.2014.
 */
@EnableAsync
@EnableWebMvc
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = {
        "com.ramotion.roadmap.controllers",
        "com.ramotion.roadmap.service",
        "com.ramotion.roadmap.repository",
        "com.ramotion.roadmap.model"
})
public class AppConfig extends WebMvcConfigurerAdapter {

    public static final SimpleDateFormat DATETIME_FORMATER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    @Autowired
    private Environment env;

    /**
     * Configure default servlet for static app resources like images or *.css and *.js files
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * PostConstruct actions - executes after constructor when all fields injected
     */
    @PostConstruct
    public void postConstruct() {
        System.out.println("============== APPLICATION CONFIGURATOR CONSTRUCTED ============");
    }

    /**
     * Standard simple view-controller assignments here
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * Standard internal resource view resolver
     */
    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
