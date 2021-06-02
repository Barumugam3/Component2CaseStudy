package com.iiht.estock.edge.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(jwtAuthenticationConfig.getHeader());

        if (token != null && token.startsWith(jwtAuthenticationConfig.getPrefix() + " ")) {
            token = token.replace(jwtAuthenticationConfig.getPrefix() + " ", "");

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtAuthenticationConfig.getSecret().getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                String userName = claims.getSubject();

                List<String> authorities = claims.get("authorities", List.class);

                if (userName != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception exception) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
