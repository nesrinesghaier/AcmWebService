package com.eniso.acmwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories("com.eniso.acmwebservice.Dao")
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class AcmwebserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmwebserviceApplication.class, args);
    }

}
