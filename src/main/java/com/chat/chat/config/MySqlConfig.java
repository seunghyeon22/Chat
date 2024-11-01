package com.chat.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration // 스프링 Config 클래스임
@EnableTransactionManagement // 스프링 트랜젝션 관리 기능을 활성화 시킴
public class MySqlConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;


    // 데이터 소스에서 트랜젝션을 관리
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){ // Datasource : 데이터베이스에 대한 연결정보 객체
        return new DataSourceTransactionManager(dataSource);
    }

    // 트랜젝션을 프로그램적으로 관리
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){ // PlatformTransactionManager : 트랜젝션 관리를 위한 기본 인터페이스, 프로그래밍 방식으로 트랜잭션 관리 지원. 트랜잭션을 시작, 커밋하거나 롤백가능.
        return new TransactionTemplate(transactionManager);
    }

    //
    @Bean(name = "createUserTranscationManager")
    public PlatformTransactionManager createUserTransactionManager(DataSource dataSource){
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);

        return manager;
    }

}
