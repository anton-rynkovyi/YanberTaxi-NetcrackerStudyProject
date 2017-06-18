package com.netcracker.project.study.vaadin.configurations;

import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DataSource dataSource;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       /* auth.inMemoryAuthentication()
                .withUser("admin").password("123").roles("ADMIN")
                .and()
                .withUser("client").password("123").roles("CLIENT")
                .and()
                .withUser("driver").password("123").roles("DRIVER");*/

        /*auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select login as username, password, 1 as enabled from Users where login=?")
                .authoritiesByUsernameQuery("select login as username, role from Users where login=?");*/
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/authorization"))
                .and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/admin*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/driver*").access("hasRole('ROLE_DRIVER')")
                .antMatchers("/client*").access("hasRole('ROLE_CLIENT')")
                .antMatchers("/authorization").permitAll()
                //.antMatchers("/admin, /driver, /client").permitAll()
                //.anyRequest().permitAll()
                .and()
                .formLogin()//.loginPage("/auth").failureUrl("/auth?error")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().permitAll();
       /* http.authorizeRequests()
                .antMatchers("/admin**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/driver**").access("hasRole('ROLE_DRIVER')")
                .antMatchers("/client**").access("hasRole('ROLE_CLIENT')")
                .and()
                .formLogin().loginPage("/auth").failureUrl("/login?error")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf();*/
    }

    @Bean
    public DaoAuthenticationProvider createDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        //provider.setPasswordEncoder(bcryptPasswordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





















