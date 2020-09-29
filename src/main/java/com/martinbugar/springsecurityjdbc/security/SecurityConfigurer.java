package com.martinbugar.springsecurityjdbc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Autowired
    DataSource dataSource; // toto defaultne ukazuje na H2 databazu


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication() // vytvori jdbc prepojenie
                .dataSource(dataSource) // pripoji ho na H2 databa
                .withDefaultSchema() // vztvori schemu v H2 databaze
                .withUser( // vytvori uzivatela v databaze
                        User.withUsername("user")
                        .password("pass")
                        .roles("USER")
                )
                .withUser(
                        User.withUsername("admin")
                                .password("pass")
                                .roles("ADMIN")
                );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder gePasswordEncoder (){
        return NoOpPasswordEncoder.getInstance();
    }
}
