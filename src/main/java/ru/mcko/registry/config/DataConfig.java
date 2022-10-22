package ru.mcko.registry.config;


import ru.mcko.registry.entity.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Getter
@Configuration
@MapperScan(basePackages = {"ru.mcko.registry.repository.write", "ru.mcko.registry.repository.read"})
public class DataConfig {

    @Value("${spring.datasource.url}")
    private String rdsUrl;


    @Value("${spring.datasource.minimum-idle}")
    private String rdsMinimumIdle;

    @Value("${spring.datasource.username}")
    private String rdsUsername;

    @Value("${spring.datasource.password}")
    private String rdsPassword;

    @Value("${spring.datasource.maximum-pool-size}")
    private String rdsMaxPoolSize;

    @Value("${spring.datasource.maximum-timeout}")
    private String rdsMaxTimeout;

    @Value("${spring.datasource.connection-lifetime}")
    private String rdsConnectionLifetime;


    @Bean("dataSourceWrite")
    @Primary
    public DataSource dataSourceWrite() {
        Properties properties = new Properties();
        properties.setProperty("tcpKeepAlive", "true");
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(rdsUrl);
        cfg.setUsername(rdsUsername);
        cfg.setPassword(rdsPassword);
        cfg.setMinimumIdle(1);
        cfg.setMaximumPoolSize(Integer.parseInt(rdsMaxPoolSize));
        cfg.setConnectionTimeout(Long.parseLong(rdsMaxTimeout));
        cfg.setMaxLifetime(Long.parseLong(rdsConnectionLifetime));
        cfg.setIdleTimeout(30L * 1000);
        cfg.setLeakDetectionThreshold(60L * 1000);
        cfg.setDataSourceProperties(properties);
        return new HikariDataSource(cfg);
    }


    @Bean
    public SqlSessionFactory sqlSessionFactoryWrite() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSourceWrite());
        sqlSessionFactory.setTypeAliasesPackage("com.constructor.common.entity");

        List<String> mappers = new ArrayList<>();
        mappers.add("classpath*:ru/mcko/registry/repository/write/*.xml");
        mappers.add("classpath*:ru/mcko/registry/repository/read/*.xml");

        sqlSessionFactory.setMapperLocations(resolveMapperLocations(mappers));

        SqlSessionFactory object = sqlSessionFactory.getObject();
        assert object != null;
        object.getConfiguration().getTypeHandlerRegistry().register(LocalDateTime.class, LocalDateTimeTypeHandler.class);



        object.getConfiguration().setMapUnderscoreToCamelCase(true);
        return object;
    }

    public Resource[] resolveMapperLocations(List<String> mapperLocations) {
        List<Resource> resources = new ArrayList<>();
        if (mapperLocations != null) {
            for (String mapperLocation : mapperLocations) {
                Resource[] mappers;
                try {
                    mappers = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException ignored) {

                }
            }
        }

        Resource[] mapperResources = new Resource[resources.size()];
        mapperResources = resources.toArray(mapperResources);
        return mapperResources;
    }


}
