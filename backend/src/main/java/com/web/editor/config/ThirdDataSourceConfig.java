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

// email 데이터베이스 접근 Config
@Configuration
public class ThirdDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.third.datasource")
    public DataSource ThirdDataSource(){
        return DataSourceBuilder.create().build();
    }
    @Bean public SqlSessionFactory ThirdSqlSessionFactory(DataSource ThirdDataSource, ApplicationContext applicationContext) throws Exception 
    {
        // mybatis와 spring간의 표기법 변경 snake -> camel
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean(); 
        sqlSessionFactoryBean.setDataSource(ThirdDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.web.editor.model.dto.email");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class); 
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/email_query.xml")); 
        return sqlSessionFactoryBean.getObject(); 
    }

    @Bean public 
    SqlSessionTemplate ThirdSqlSessionTemplate(SqlSessionFactory ThirdSqlSessionFactory) throws Exception 
    {
        return new SqlSessionTemplate(ThirdSqlSessionFactory); 
    }
}