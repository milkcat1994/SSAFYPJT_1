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

// user 데이터베이스 접근 Config
@Configuration
public class FirstDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.first.datasource")
    public DataSource firstDataSource(){
        return DataSourceBuilder.create().build();
    }
    @Bean public SqlSessionFactory firstSqlSessionFactory(DataSource firstDataSource, ApplicationContext applicationContext) throws Exception 
    {
        // mybatis와 spring간의 표기법 변경 snake -> camel
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
    
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(firstDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.web.editor.model.dto.user");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class); 
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/user/*.xml"));  
        return sqlSessionFactoryBean.getObject(); 
    }

    @Bean public 
    SqlSessionTemplate firstSqlSessionTemplate(SqlSessionFactory firstSqlSessionFactory) throws Exception 
    {
        return new SqlSessionTemplate(firstSqlSessionFactory); 
    }        
}