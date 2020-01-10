package com.iamsid.geek.vibes.springsecuritycustom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iamsid.geek.vibes.springsecuritycustom.filter.ApplicationAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true,jsr250Enabled=true,securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
		 .csrf().disable()
		.httpBasic().disable()
		.formLogin().disable()
		.logout().disable()
		.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
		.authorizeRequests()
		.antMatchers("/*/ping").permitAll()
		.antMatchers("/v1/**").authenticated()
		.and()
		.addFilterBefore(applicationAuthFilter() , UsernamePasswordAuthenticationFilter.class)
		.anonymous().disable();

	}
	public String defaultFilterProcessesUrl() {
		return new String("/v1/**");
	}
   @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	 
	@Bean
	ApplicationAuthFilter applicationAuthFilter() throws Exception {
		ApplicationAuthFilter applicationAuthFilter = new ApplicationAuthFilter(defaultFilterProcessesUrl());
		applicationAuthFilter.setAuthenticationManager(authenticationManagerBean());
		return applicationAuthFilter;
	}
}
