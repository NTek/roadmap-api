package com.ramotion.roadmap.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by Oleg Vasiliev on 10.11.2014.
 * Class for app configuration
 */
@EnableAsync
@EnableWebMvc
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:app.properties")
@EnableJpaRepositories(basePackages = {"com.ramotion.roadmap.repository"})
@ComponentScan(basePackages = {
        "com.ramotion.roadmap.controllers",
        "com.ramotion.roadmap.service",
        "com.ramotion.roadmap.repository",
        "com.ramotion.roadmap.model"
})
public class AppConfig extends WebMvcConfigurerAdapter {

    public static final String DB_URL_ENV_VAR_NAME = "DATABASE_URL";
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    @Autowired
    private Environment env;

    /**
     * Configure default servlet for static app resources like images or *.css and *.js files
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    /**
     * PostConstruct actions - executes after constructor when all fields injected
     */
    @PostConstruct
    public void postConstruct() {
        System.out.println("============== APPLICATION CONFIG CONSTRUCTED ============");
    }

    /**
     * Standard simple view-controller assignments here
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
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

    /**
     * Datasource with connection pool by apache DBCP.
     * Connection settings configured from app.properties file.
     */
    @Bean
    public BasicDataSource getBasicDBCPDatasource() {

        String dbURL;
        String dbUsername;
        String dbPassword;

        //Get connection parameters from environment variable DB_URL_ENV_VAR_NAME
        try {
            URI dbUri = new URI(System.getenv(DB_URL_ENV_VAR_NAME));
            dbUsername = dbUri.getUserInfo().split(":")[0];
            dbPassword = dbUri.getUserInfo().split(":")[1];
            dbURL = env.getProperty("database.connection.url.driver") + dbUri.getHost() + ':' + dbUri.getPort() +
                    dbUri.getPath() + env.getProperty("database.connection.url.properties");
        } catch (Exception e) {
            System.err.println("Error while parsing database parameters from system variable " + DB_URL_ENV_VAR_NAME);
            dbURL = env.getProperty("database.connection.url.full");
            dbUsername = env.getProperty("database.connection.user");
            dbPassword = env.getProperty("database.connection.password");
        }

        BasicDataSource datasource = new BasicDataSource();
        datasource.setUrl(dbURL);
        datasource.setUsername(dbUsername);
        datasource.setPassword(dbPassword);
        datasource.setDriverClassName(env.getProperty("database.driver.classname"));
        datasource.setMaxActive(Integer.parseInt(env.getProperty("database.pool.maxActive")));
        datasource.setMaxIdle(Integer.parseInt(env.getProperty("database.pool.maxIdle")));
        datasource.setMaxWait(Integer.parseInt(env.getProperty("database.pool.maxWait")));
        datasource.setInitialSize(Integer.parseInt(env.getProperty("database.pool.initialSize")));
        datasource.setValidationQuery(env.getProperty("database.pool.validationQuery"));
        return datasource;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(getBasicDBCPDatasource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.ramotion.roadmap.model");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.setProperty("hibernate.id.new_generator_mappings", "true");
        jpaProperties.setProperty("show_sql", "true");
        jpaProperties.setProperty("format_sql", "true");

        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    public SpringLiquibase springLiquibaseBean() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(getBasicDBCPDatasource());
        springLiquibase.setChangeLog("classpath:db-changelog.sql");
        springLiquibase.setContexts("test, production");
        springLiquibase.setDropFirst(false);
        springLiquibase.setDefaultSchema("roadmap");
        return springLiquibase;
    }
}
