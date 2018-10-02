package com.eniso.acmwebservice;

import com.eniso.acmwebservice.Dao.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

//@EnableWebMvc
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableJpaRepositories("com.eniso.acmwebservice.Dao")
public class AcmwebserviceApplication implements CommandLineRunner {
    @Resource
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(AcmwebserviceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // angular port here
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }


    @Override
    public void run(String... arg) throws Exception {
        storageService.deleteAll();
        storageService.init();
    }
}
