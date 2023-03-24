package io.ruv.proto;

import liquibase.integration.spring.SpringLiquibase;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
@EnableJpaAuditing
public class ProtoApplication {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseaServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {

        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:migrations/master.yaml");

        return liquibase;
    }


    public static void main(String[] args) {
        SpringApplication.run(ProtoApplication.class, args);
    }

}
