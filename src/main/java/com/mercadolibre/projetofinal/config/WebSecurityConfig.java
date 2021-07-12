package com.mercadolibre.projetofinal.config;

import com.mercadolibre.projetofinal.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.mercadolibre.projetofinal.enums.AccountRolesEnum.BUYER;
import static com.mercadolibre.projetofinal.enums.AccountRolesEnum.REPRESENTATIVE;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt-secret}")
    private String secret;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/sign-in").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/fresh-products/orders").hasRole(BUYER.getRole())
                .antMatchers(HttpMethod.PUT,"/api/v1/fresh-products/orders").hasRole(BUYER.getRole())
                .antMatchers(HttpMethod.GET,"/api/v1/fresh-products/orders").hasRole(BUYER.getRole())
                .antMatchers(HttpMethod.GET, "/ping").permitAll()
                .antMatchers(HttpMethod.GET, "/teste").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                .antMatchers(HttpMethod.GET, "/fake").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/fresh-products/representative/list").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.GET, "/api/v1/fresh-products").hasRole(BUYER.getRole())
                .antMatchers(HttpMethod.GET, "/api/v1/fresh-products/list").hasRole(BUYER.getRole())
                .antMatchers(HttpMethod.POST, "/api/v1/fresh-products/inboundorder").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.PUT, "/api/v1/fresh-products/inboundorder").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.GET,"/api/v1/fresh-products/warehouse").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.GET,"/api/v1/fresh-products/due-date").hasRole(REPRESENTATIVE.getRole())
                .antMatchers(HttpMethod.GET,"/api/v1/fresh-products/due-date/list").hasRole(REPRESENTATIVE.getRole())
                .anyRequest().denyAll();
    }
}
