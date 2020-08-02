package com.angeltashev.informatics.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO Finish configuration
        http
                .headers() // Add xss protection
                .and()
                .authorizeRequests()
                .antMatchers("/index", "/contact-us", "/about-us").permitAll()
                .antMatchers("/users/register", "/users/login").anonymous()
                .antMatchers("/home", "/users/**").authenticated()
                .antMatchers("root-admin-panel", "/root-admin-panel/**").hasRole("ROOT_ADMIN")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .defaultSuccessUrl("/home")
                .and()
                .rememberMe()
                .key("informaticsPrivateKey")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("rememberlogin")
                .tokenValiditySeconds(604800)
                .and()
                .logout()
                .deleteCookies("JSESSIONID", "rememberlogin")
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
                .and()
                .csrf().csrfTokenRepository(this.csrfTokenRepository());
    }
}
