package br.com.alteia.microservicechangeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "br.com.alteia.common.configuration",
        "br.com.alteia.common.security",
        "br.com.alteia.microservicechangeit"
})
@EntityScan("br.com.alteia.microservicechangeit.entities")
@EnableAutoConfiguration
public class CleanArchitectureChangeItApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CleanArchitectureChangeItApplication.class, args);
    }

}