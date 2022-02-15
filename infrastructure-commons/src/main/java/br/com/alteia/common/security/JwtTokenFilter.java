package br.com.alteia.common.security;

import br.com.alteia.common.exceptions.ErrorResponse;
import br.com.alteia.common.exceptions.SecurityException;
import br.com.alteia.common.security.type.UserJwt;
import br.com.alteia.common.security.type.UserRole;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static br.com.alteia.common.exceptions.enums.SecurityErrorMessages.JWT_EMPTY_CLAIMS;
import static br.com.alteia.common.exceptions.enums.SecurityErrorMessages.JWT_EXPIRED;
import static br.com.alteia.common.exceptions.enums.SecurityErrorMessages.JWT_INVALID_SIGNATURE;
import static br.com.alteia.common.exceptions.enums.SecurityErrorMessages.JWT_INVALID_TOKEN;
import static br.com.alteia.common.exceptions.enums.SecurityErrorMessages.JWT_UNSUPPORTED;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOG = Logger.getLogger(JwtTokenFilter.class.getName());

    @Value("${jwt.validation.enabled}")
    private boolean jwtValidationEnabled;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            final String jwtHeader = request.getHeader("JWT");

            if (isEmpty(jwtHeader)) {
                chain.doFilter(request, response);
                return;
            }

            validateAndReturnBody(jwtHeader);

            // Get user identity and set it on the spring security context
            List<UserRole> roles = List.copyOf((Collection<?>) Jwts.parser().setSigningKey(
                    Base64.getDecoder().decode(jwtSecret)
            ).parseClaimsJws(jwtHeader).getBody().get("Role")).stream().map(m -> new UserRole(m.toString())).toList();

            UserJwt userJwt = new UserJwt(roles);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userJwt, null,
                    Optional.of(userJwt).map(UserDetails::getAuthorities).orElse(Collections.emptyList())
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (SecurityException ex) {
            ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED, ex.getCode(), List.of(ex.getMessage()));
            response.getOutputStream().println(new Gson().toJson(errorResponse));
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
        }
    }

    private void validateAndReturnBody(String token) {
        try {
            Jwts.parser().setSigningKey(Base64.getDecoder().decode(jwtSecret)).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new SecurityException(JWT_INVALID_SIGNATURE.getCode(), JWT_INVALID_SIGNATURE.getMessage());
        } catch (MalformedJwtException ex) {
            throw new SecurityException(JWT_INVALID_TOKEN.getCode(), JWT_INVALID_TOKEN.getMessage());
        } catch (ExpiredJwtException ex) {
            throw new SecurityException(JWT_EXPIRED.getCode(), JWT_EXPIRED.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new SecurityException(JWT_UNSUPPORTED.getCode(), JWT_UNSUPPORTED.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new SecurityException(JWT_EMPTY_CLAIMS.getCode(), JWT_EMPTY_CLAIMS.getMessage());
        }
    }
}