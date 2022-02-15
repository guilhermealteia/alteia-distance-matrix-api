package br.com.alteia.common.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

@FunctionalInterface
public interface UrlAuthorizationRegistry {
    void addToRegistry(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                    registry);

    default void configureRegistry(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                    registry) {
        addToRegistry(registry);
        addActuatorDefaults(registry);
        addSwaggerDefaults(registry);
        addHealthControllerDefault(registry);
    }

    default void addSwaggerDefaults(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                    registry) {
        // Swagger
        registry.antMatchers(HttpMethod.GET, "/webjars/springfox-swagger-ui/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/**/api-docs")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-resources")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-resources/configuration/ui")
                .permitAll();
    }

    default void addActuatorDefaults(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                    registry) {
        registry.antMatchers(HttpMethod.GET, "/actuator/**").permitAll();
    }

    default void addHealthControllerDefault(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                    registry) {
        registry.antMatchers(HttpMethod.GET, "/health/**").permitAll();
    }
}
