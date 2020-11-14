package com.sports.cricket.config;

import javax.sql.DataSource;

import com.sports.cricket.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.Serializable;

@Configuration
public class SpringJDBCConfiguration implements Serializable {
/*    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //MySQL database we are using
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        //Google cloud URL - 34.67.153.144
        //dataSource.setUrl("jdbc:mysql://34.67.153.144:3306/ipl?serverTimezone=UTC");
        //dataSource.setUrl("jdbc:mysql://google/ipl?useSSL=false&cloudSqlInstance=34.67.153.144&socketFactory=com.google.cloud.sql.mysql.SocketFactory&amp");

        // Local
        dataSource.setUrl("jdbc:mysql://localhost:3306/ipl?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("");

        return dataSource;
    }*/
    // Google Cloud connection
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(System.getProperty("auto"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public UserDao userDao(){
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setJdbcTemplate(jdbcTemplate());
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public ScheduleDao scheduleDao(){
        ScheduleDaoImpl scheduleDao = new ScheduleDaoImpl();
        scheduleDao.setDataSource(dataSource());
        scheduleDao.setJdbcTemplate(jdbcTemplate());
        return scheduleDao;
    }

    @Bean
    public RegistrationDao registrationDao(){
        RegistrationDaoImpl registrationDao = new RegistrationDaoImpl();
        registrationDao.setDataSource(dataSource());
        registrationDao.setJdbcTemplate(jdbcTemplate());
        return registrationDao;
    }


}