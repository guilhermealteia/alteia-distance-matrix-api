package br.com.alteia.common.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

@Configuration
public class SpringConfig {

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    @Bean
    public ServletRegistrationBean<Servlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        return new ServletRegistrationBean<>(dispatcherServlet);
    }
}