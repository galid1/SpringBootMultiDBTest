package com.example.multi.config;

import com.example.multi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "stanbyEntityManager",
        transactionManagerRef = "stanbyTransactionManager",
        basePackages = "com.example.multi.dao.stanby"
)
public class StanbyDBConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource stanbyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.stanby.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.stanby.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.stanby.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.stanby.datasource.password"));
        return dataSource;
    }

    @Bean(name = "stanbyEntityManager")
    public LocalContainerEntityManagerFactoryBean stanbyEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(stanbyDataSource())
                .properties(hibernateProperties())
                .packages(User.class)
                .persistenceUnit("userPU")
                .build();
    }

    @Bean(name = "stanbyTransactionManager")
    public PlatformTransactionManager stanbyTransactionManager(@Qualifier("stanbyEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map hibernateProperties() {
        Resource resource = new ClassPathResource("hibernate.properties");

        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            return properties.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue())
                    );
        } catch (IOException e) {
            return new HashMap();
        }
    }
}