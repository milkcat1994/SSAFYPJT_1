package com.web.editor.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// request 데이터 베이스 접근 Config파일
@Configuration
public class SecondDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.second.datasource")
    public DataSource SecondDataSource(){
        return DataSourceBuilder.create().build();
    }
    @Bean public SqlSessionFactory SecondSqlSessionFactory(DataSource SecondDataSource, ApplicationContext applicationContext) throws Exception 
    {
        // mybatis와 spring간의 표기법 변경 snake -> camel
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
    
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean(); 
        sqlSessionFactoryBean.setDataSource(SecondDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.web.editor.model.dto.request");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class); 
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/request/*.xml")); 
        return sqlSessionFactoryBean.getObject(); 
    }

    @Bean public 
    SqlSessionTemplate SecondSqlSessionTemplate(SqlSessionFactory SecondSqlSessionFactory) throws Exception 
    {
        return new SqlSessionTemplate(SecondSqlSessionFactory); 
    }
}
