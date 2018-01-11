package com.uuzu.mktgo.config.mysql;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 *
 * @author lixing
 * @date 2017/6/27
 */
@Configuration
@MapperScan(basePackages = "com.uuzu.mktgo.mapper",sqlSessionFactoryRef = "mobMktGoSqlSessionFactory")
public class MysqlConfig {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;
    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Primary
    @ConfigurationProperties("spring.datasource.druid.mysql")
    @Bean
    public DataSource mysqlDataSource(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "mobMktGoTransactionManager")
    @Primary
    public DataSourceTransactionManager rdsTransactionManager() {
        return new DataSourceTransactionManager(mysqlDataSource());
    }

    @Bean(name = "mobMktGoSqlSessionFactory")
    @Primary
    public SqlSessionFactory rdsSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mysqlDataSource());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(this.mapperLocations));
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        return sessionFactory.getObject();
    }

}
