package com.ramotion.roadmap.config;

import com.ramotion.roadmap.model.Language;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

@EnableAsync
@EnableWebMvc
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = {"com.ramotion.roadmap.repository"})
@ComponentScan(basePackages = {
        "com.ramotion.roadmap.controllers",
        "com.ramotion.roadmap.service",
        "com.ramotion.roadmap.repository",
        "com.ramotion.roadmap.model",
        "com.ramotion.roadmap.exceptions",
        "com.ramotion.roadmap.utils"
})
public class AppConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(AppConfig.class.getName());
    public static final Language DEFAULT_LOCALIZATION_LANGUAGE = Language.en;
    public static final String LOCALIZATION_NOT_FOUND_MSG = "Default localization text not found";

    public static final byte USER_ACCESS_OWNER = 0;
    public static final byte USER_ACCESS_EDIT = 1;
    public static final byte USER_ACCESS_READ = 2;

    public static final String DB_URL_ENV_VAR_NAME = "CLEARDB_DATABASE_URL";
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    public static final String EMAIL_FROM = "Roadmap";

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
        LOG.info("Application config constructed");
    }

    /**
     * Standard simple view-controller assignments here
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/alogin").setViewName("alogin");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/chat").setViewName("chat");
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
     * Connection settings configured from application.properties file.
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
            String dbPort = dbUri.getPort() == -1 ? "" : (":" + dbUri.getPort());
            dbURL = env.getProperty("database.connection.url.driver") + dbUri.getHost() + dbPort +
                    dbUri.getPath() + env.getProperty("database.connection.url.properties");
        } catch (Exception e) {
            LOG.warn("Can't parse system variable '" + DB_URL_ENV_VAR_NAME + "' with database params, will be used defaults.");
            dbURL = env.getProperty("database.connection.url.driver") +
                    env.getProperty("database.connection.url.host") + ":" +
                    env.getProperty("database.connection.url.port") + "/" +
                    env.getProperty("database.connection.url.path") +
                    env.getProperty("database.connection.url.properties");
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
        jpaProperties.setProperty("hibernate.surveyId.new_generator_mappings", "true");
        jpaProperties.setProperty("hibernate.show_sql", "true");
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
        return springLiquibase;
    }


    /**
     * Standard Java mail sender with connection settings
     */
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl jms = new JavaMailSenderImpl();

        Properties javaMailProps = new Properties();
        javaMailProps.setProperty("mail.transport.protocol", "smtp");
        javaMailProps.setProperty("mail.smtp.auth", "true");
        javaMailProps.setProperty("mail.smtp.starttls.enable", env.getProperty("mailsender.smtp.starttls"));
        javaMailProps.setProperty("mail.debug", env.getProperty("mailsender.debug"));

        jms.setHost(env.getProperty("mailsender.smtp.host"));
        jms.setPort(Integer.parseInt(env.getProperty("mailsender.smtp.port")));
        jms.setUsername(env.getProperty("mailsender.smtp.username"));
        jms.setPassword(env.getProperty("mailsender.smtp.password"));

        jms.setJavaMailProperties(javaMailProps);
        return jms;
    }
}
