package com.iiht.estock.edge.config;

import com.iiht.estock.edge.filters.JwtAuthenticationConfig;
import com.iiht.estock.edge.filters.JwtTokenAuthenticationFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationConfig jwtAuthenticationConfig;

    @Bean
    public JwtAuthenticationConfig jwtConfig() {
        return new JwtAuthenticationConfig();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .exceptionHandling().authenticationEntryPoint(
                (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtAuthenticationConfig),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(jwtAuthenticationConfig.getUrl()).permitAll()
                .antMatchers("/company-service/api/v1.0/market/company/register").hasRole("ADMIN")
                .antMatchers("/company-service/api/v1.0/market/company/delete/**").hasRole("ADMIN")
                .antMatchers("/stock-command-service/api/v1.0/market/stock/add/**").hasRole("ADMIN")
                .antMatchers("/stock-command-service/api/v1.0/market/stock/delete/**").hasRole("ADMIN")
                .antMatchers("/company-service/api/v1.0/market/company/getall",
                        "/company-service/api/v1.0/market/company/info/**",
                        "/stock-query-service/api/v1.0/market/stock/get/**").permitAll();
    }
}
