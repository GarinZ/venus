package art.meiye.venus.dal.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "art.meiye.venus.dal.mapper",
        sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class DefaultDataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(value = "spring.datasource.youyou")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource cipDataSource) {
        return new DataSourceTransactionManager(cipDataSource);
    }


    @Bean
    @Primary
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        String patternPath = ResourcePatternResolver.CLASSPATH_URL_PREFIX + "youyou/*Mapper.xml";
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(patternPath));
        sessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactoryBean.getObject();
    }
}
