package com.set.filters;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.set.dao.DAOFactory;


/*
 * @author Björn Dalberg
 * @created 24 maj 2016
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("customUserDetailService")
	UserDetailsService userDetailsService;
	
	@Autowired
	PersistentTokenRepository tokenRepository;
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/")
			.access("hasRole('user') or harRole('admin')")
			.antMatchers("/useradmin").access("hasRole('admin')").and().formLogin().loginPage("/login")
			.loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").and()
			.rememberMe().rememberMeParameter("remember_me").tokenRepository(tokenRepository)
			.tokenValiditySeconds(86400);
	}
	
	@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(DAOFactory.getInstance("setbd.jndi"));
        return tokenRepositoryImpl;
    }
}
