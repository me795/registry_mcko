package ru.mcko.registry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.enable.csrf}")
    private boolean csrfEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
//                .authorizeRequests()
//                .antMatchers("/registration", "/upload").permitAll()
//                .antMatchers("/","/welcome").hasAnyRole("USER","ADMIN","SUPERADMIN")
//                .antMatchers("/users","/user/**").hasRole("ADMIN")
//                .and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/welcome")
//                .and().logout().logoutUrl("/logout");
//        return http.build();

        http
                .authorizeRequests()
                .antMatchers("/registration").permitAll()
                .antMatchers("/file/upload").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/","/welcome").hasAnyRole("USER","ADMIN","SUPERADMIN")
                .antMatchers("/registry/organization").hasAnyRole("USER","ADMIN","SUPERADMIN")
                .antMatchers("/users","/user/**").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/delegates/**","/delegate/**").hasAnyRole("ADMIN","SUPERADMIN")
                .and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/welcome")
                .and().logout().logoutUrl("/logout");

        if(! csrfEnabled){
            http.csrf().disable();
        }

        return http.build();
    }

}





