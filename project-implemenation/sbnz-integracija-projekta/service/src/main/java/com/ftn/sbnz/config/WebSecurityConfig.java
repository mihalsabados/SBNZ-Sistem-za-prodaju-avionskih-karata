package com.ftn.sbnz.config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().and()

            .authorizeRequests()
            .antMatchers("/users/**", "/ws/**", "/ws/info", "/device-script/**", "*").permitAll()
            .and().cors();
        http.csrf().disable();

        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST,
                "/auth/login-final-step",
                "/auth/login-first-step",
                "/users/register");

        web.ignoring().antMatchers(HttpMethod.GET,
                "/", "/webjars/**",
                "/*.html",
                "favicon.ico",
                "/**/*.html",
                "**/ws",
                "/**/*.css",
                "/**/*.js"
        );
    }
}
