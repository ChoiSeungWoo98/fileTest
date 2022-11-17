package com.example.fileupload.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration //설정 관련 클래스에 작성한다.
@MapperScan("com.example.fileupload.file") //작성한 경로부터 하위 경로까지 모두 @Mapper를 스캔한다.
@RequiredArgsConstructor
public class MyBatisConfig {
    //    커넥션 풀 및 MyBatis에 필요한 요소를 메모리에 할당 및 관리, xml과 java연동에 필요한 경로 관리
    private final ApplicationContext applicationContext;

    //    @Bean : 메소드의 리턴 객체를 스프링 컨테이너에 등록, 객체명은 메소드의 이름으로 자동 설정되며,
//            직접 설정하고자 할 때에는 @Bean(name="객체명")으로 사용
    @Bean //@Configuration 또는 @Component가 작성된 클래스의 메소드에만 사용이 가능하다.
    @ConfigurationProperties(prefix = "spring.datasource.hikari") //properties 파일에서 prefix인 설정 모두 가져오기
    public HikariConfig hikariConfig() {
        return new HikariConfig(); //properties파일에서 가져온 설정들과 필드가 매핑되어 자동으로 주입된다.
    }

    @Bean
    public HikariDataSource hikariDataSource(){
//        DataSource 객체에 DBMS 정보 설정
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
//        세션 팩토리 설정 객체
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        DBMS 정보를 담고 있는 DataSource를 세션 팩토리 설정 객체에 전달
        sqlSessionFactoryBean.setDataSource(hikariDataSource());
//        SQL 쿼리를 작성할 mapper.xml 경로 설정
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:/mapper/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/config/config.xml"));
        try {
//            위에서 설정한 세션 팩토리 빈을 통해 세션 팩토리 생성
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
//            팟홀(언더바) 표기법을 카멜 표기법으로 자동 변경 설정
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
            return sqlSessionFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}