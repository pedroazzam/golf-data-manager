package br.com.golfinvest.data.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

@Configuration
public class AppConfig {

//    public JdbcTemplate newJdbcTemplate(String dbAddress) throws IllegalAccessException, InstantiationException, InvocationTargetException {
//        System.out.println("OK2");
//        //String dbAddress = "c:/Users/pedro/Documents/azzam02.db";
//        final String driverClassName = "org.sqlite.JDBC";
//        final String jdbcUrl = "jdbc:sqlite:"+ dbAddress;
//        // using DataSourceBuilder:
//        final DataSource dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(jdbcUrl).build();
//        // and make the jdbcTemplate
//        return new JdbcTemplate(dataSource);
//    }

    @Bean (name = "jdbcTemplate")
    JdbcTemplate jdbcTemplate() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("OK1");
        String dbAddress = "";//""c:/Users/pedro/Documents/azzam01.db";
        final String driverClassName = "org.sqlite.JDBC";
        final String jdbcUrl = "jdbc:sqlite:"+ dbAddress;
        // using DataSourceBuilder:
        final DataSource dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(jdbcUrl).build();
        // and make the jdbcTemplate
        return new JdbcTemplate(dataSource);
    }

}
