package com.example.deliveryoptimizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.deliveryoptimizer.repository")
public class PersistenceConfig {

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager txManager) {
        return new TransactionTemplate(txManager);
    }

    @Bean
    public SharedEntityManagerBean entityManager(EntityManagerFactory emf) {
        SharedEntityManagerBean seb = new SharedEntityManagerBean();
        seb.setEntityManagerFactory(emf);
        return seb;
    }

}
