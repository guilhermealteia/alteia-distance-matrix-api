package br.com.alteia.microservicechangeit.configuration;

import br.com.alteia.common.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.alteia.common.security.type.UserRole.INTERNAL_USER_ROLE;
import static br.com.alteia.microservicechangeit.distance_between_adresses.DistanceBetweenAdressesEndpointsConstants.GET_DISTANCE_BETWEEN_TWO_CEPS_PATH;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(SpringSecurityConfig.class.getName());
    public static final String ALL_FOR_PATH_FORMAT = "%s/**";

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    private final JwtTokenFilter jwtTokenFilter;

    public SpringSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        super();
        this.jwtTokenFilter = jwtTokenFilter;

        // Inherit security context in async function calls
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }


    // Set password encoding schema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            LOG.log(Level.SEVERE, "Unauthorized request - {}", ex.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                //Swagger
                .antMatchers("/").permitAll()
                .antMatchers(format(ALL_FOR_PATH_FORMAT, "/v2/api-docs")).permitAll()
                //Observabilidade
                .antMatchers(format(ALL_FOR_PATH_FORMAT, "/prometheus")).permitAll()
                .antMatchers(format(ALL_FOR_PATH_FORMAT, "/info")).permitAll()

                //Funcionalidades

                //Google Distance API
                .antMatchers(GET, GET_DISTANCE_BETWEEN_TWO_CEPS_PATH).hasAnyAuthority(INTERNAL_USER_ROLE)

                .antMatchers("/**").denyAll();

        // Add JWT token filter
        http.addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}