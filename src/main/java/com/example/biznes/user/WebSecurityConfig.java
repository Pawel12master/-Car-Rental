package com.example.biznes.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/cars/**").hasAnyAuthority("ADMIN", "USER")
                .mvcMatchers(HttpMethod.PUT, "/cars/{id}/edit").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "/cars/{id}/delete").hasAnyAuthority("ADMIN")

                .mvcMatchers("/reservation/**").hasAnyAuthority("USER")
                .mvcMatchers(HttpMethod.POST, "/reservations/{id}/add").hasAnyAuthority("USER")

                .mvcMatchers("/rents/**").hasAnyAuthority("USER")
                .mvcMatchers(HttpMethod.GET, "/rents/{id}/addRent").hasAnyAuthority("USER")
                .mvcMatchers(HttpMethod.POST, "/rents/{id}/create").hasAnyAuthority("USER")



                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .failureUrl("/error-login")
                .and().logout()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().exceptionHandling().accessDeniedPage("/admin");

    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("javabiznesmail@gmail.com");
        mailSender.setPassword("dptmazgzkwhayhei");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }



}