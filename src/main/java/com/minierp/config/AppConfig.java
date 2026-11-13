package com.minierp.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Root application context – data source, JdbcTemplate, transaction manager.
 * Does NOT include web-layer beans (those live in WebMvcConfig).
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.minierp.dao",
        "com.minierp.service",
        "com.minierp.util"
})
@PropertySource("classpath:jdbc.properties")
public class AppConfig {

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriver;

    // ------------------------------------------------------------------ //
    // DataSource (Commons DBCP2 connection pool)                          //
    // ------------------------------------------------------------------ //
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(jdbcDriver);
        ds.setUrl(jdbcUrl);
        ds.setUsername(jdbcUsername);
        ds.setPassword(jdbcPassword);
        ds.setInitialSize(3);
        ds.setMaxTotal(20);
        ds.setMaxIdle(5);
        ds.setMinIdle(2);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("SELECT 1");
        return ds;
    }

    // ------------------------------------------------------------------ //
    // JdbcTemplate                                                        //
    // ------------------------------------------------------------------ //
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // ------------------------------------------------------------------ //
    // Transaction Manager                                                 //
    // ------------------------------------------------------------------ //
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
