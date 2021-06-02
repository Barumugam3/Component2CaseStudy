package com.iiht.estock.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtAuthenticationConfig jwtAuthenticationConfig;
    private final ObjectMapper mapper;

    public JwtUsernamePasswordAuthenticationFilter(JwtAuthenticationConfig jwtAuthenticationConfig, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(jwtAuthenticationConfig.getUrl(), "POST"));
        setAuthenticationManager(authenticationManager);
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        User user = mapper.readValue(request.getInputStream(), User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        Instant now = Instant.now();

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtAuthenticationConfig.getExpiration())))
                .signWith(SignatureAlgorithm.HS256, jwtAuthenticationConfig.getSecret().getBytes())
                .compact();

        response.addHeader(jwtAuthenticationConfig.getHeader(), jwtAuthenticationConfig.getPrefix() + " " + token);
    }

    @Data
    private static class User {
        private String token;
        private String userName;
        private String password;
    }
}
