//package com.dc.rest.imdbservice.datasource;
//
//import java.util.Properties;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.sql.DataSource;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.orm.jpa.JpaTransactionManager;
//
///***
// ** Author: Dominic Coutinho
// ** Description: This class contains bean definitions to interact with DB
// ** UPDATE: 08/12/2020: Migrated form Spring Boot 1.5x to 2.3.2. Config moved to application.properties
// */
//
//@Configuration
//public class DataSourceConfig{
//
//  @Bean
//  public DataSource dataSource() {
//    org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
//    dataSource.setDriverClassName("org.postgresql.Driver");
//    dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
//    dataSource.setUsername("postgres");
//    dataSource.setPassword("postgres");
//
//    return dataSource;
//  }
//
//  @Bean
//  @DependsOn("flyway")
//  public EntityManagerFactory entityManagerFactory() {
//    Properties jpaProps = new Properties();
//    jpaProps.put("openjpa.ConnectionFactory", dataSource());
//    jpaProps.put("openjpa.Log", "log4j");
//    jpaProps.put("openjpa.ConnectionFactoryProperties", "true");
//    return Persistence.createEntityManagerFactory("moviesPersistenceUnit", jpaProps);
//  }
//
//  @Bean(initMethod = "migrate")
//  Flyway flyway() {
//    Flyway flyway = new Flyway();
//    flyway.setSchemas("imdb");
//    flyway.setBaselineOnMigrate(true);
//    flyway.setDataSource(dataSource());
//    return flyway;
//  }
//
//  @Bean
//  @Autowired
//  public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
//    return entityManagerFactory.createEntityManager();
//  }
//
//  @Bean
//  @Autowired
//  public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//    JpaTransactionManager txManager = new JpaTransactionManager();
//    txManager.setEntityManagerFactory(entityManagerFactory);
//    txManager.setDefaultTimeout(900);
//
//    return txManager;
//  }
//}
