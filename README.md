#### spring-security-custom

##### Demo application to show how to use custom filter and set the Securitycontext with authentication principal.(Not to be used for production or enterprise application , only for learning purpose)

######x-auth-user is used as request header for all the secured end-point.

###### End-point Details
* /vibes/demo/api/ping
     * Accessible without any x-aut-header
* /vibes/demo/api/secure/ping
      * Secured End-point, need to set the x-auth-header as *admin*
      
      
##### Configuration to Add Custom Filter to Filter chain     
``` 
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
```

Refer Spring Security Filter Order: <https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html>