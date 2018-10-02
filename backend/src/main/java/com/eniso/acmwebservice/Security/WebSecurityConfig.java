package com.eniso.acmwebservice.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationProvider authProvider;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.authorizeRequests().anyRequest().permitAll().anyRequest().anonymous();
        //http.antMatcher("api/acmers/").authorizeRequests().anyRequest().hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/api/login").permitAll()
                .antMatchers("/api/acmers/").hasAuthority("ADMIN");
        http.csrf().disable();
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}