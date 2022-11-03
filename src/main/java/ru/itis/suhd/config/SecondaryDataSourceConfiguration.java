package ru.itis.suhd.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackages = {"ru.itis.suhd.repository.stats"})
public class SecondaryDataSourceConfiguration {

    @Bean(name = "secondaryDataSourceProperties")
    @ConfigurationProperties("spring.datasource-secondary")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties("spring.datasource-secondary.configuration")
    public DataSource secondaryDataSource(@Qualifier("secondaryDataSourceProperties") DataSourceProperties secondaryDataSourceProperties) {
        return secondaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            @Qualifier("secondaryDataSource") DataSource secondaryDataSource) {

        Map<String, String> secondaryJpaProperties = new HashMap<>();
        secondaryJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        secondaryJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), secondaryJpaProperties, null);

        return entityManagerFactoryBuilder
                .dataSource(secondaryDataSource)
                .packages("ru.itis.suhd.model.stats")
                .persistenceUnit("secondaryDataSource")
                .properties(secondaryJpaProperties)
                .build();
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {

        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }
}
