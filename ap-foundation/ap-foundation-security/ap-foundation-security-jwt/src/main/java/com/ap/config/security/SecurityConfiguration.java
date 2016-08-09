package com.ap.config.security;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ap.config.base.Constants;
import com.ap.config.security.encryption.SHA256EncryptionService;
import com.ap.config.security.jwt.JWTConfigurer;
import com.ap.config.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256EncryptionService();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	log.info("spring security : configure jwt");
        http
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/logs/**").hasAuthority(Constants.ROLE_ADMINISTRATOR)
            .antMatchers("/api/audits/**").hasAuthority(Constants.ROLE_ADMINISTRATOR)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/websocket/tracker").hasAuthority(Constants.ROLE_ADMINISTRATOR)
            .antMatchers("/websocket/**").permitAll()
            .antMatchers("/metrics/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/health/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/trace/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/dump/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/shutdown/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/beans/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/configprops/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/info/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/autoconfig/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/env/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/mappings/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/liquibase/**").hasAuthority(Constants.ROLE_SYSTEM_ADMINISTRATOR)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/configuration/security").permitAll()
            .antMatchers("/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(Constants.ROLE_ADMINISTRATOR)
            .antMatchers("/protected/**").authenticated() 
        .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
