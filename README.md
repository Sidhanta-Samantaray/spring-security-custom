#### spring-security-custom

##### Demo application to show how to use custom filter and set the Securitycontext with authentication principal.(Not to be used for production or enterprise application , only for learning purpose)

###### x-auth-user is used as request header for all the secured end-point.

###### End-point Details
* /vibes/demo/api/ping
     * Accessible without x-auth-header
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
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
		.authorizeRequests()
		.antMatchers("/*/ping").permitAll()/* Accessible without authentication */
		.antMatchers("/v1/**").authenticated()/*Secured End-Points, needs Authentication*/
		.and()
		/*Making Sure Custom Authentication Filter is fired before the Spring Provided Filter*/
		.addFilterBefore(applicationAuthFilter() , UsernamePasswordAuthenticationFilter.class)
		.anonymous().disable();
	} 
```

Refer Spring Security Filter Order: <https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html>

##### What happens when Secured end-point is called ?
- Below code is being called to get the header from request and authentication object is returned.However Authentication is not set to TRUE here. 

``` 
@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LOGGER.info("Attempt Auth");
		/*
		 * Avoid Authentication if already authenticated
		 */
		if(SecurityContextHolder.getContext().getAuthentication()!=null 
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			LOGGER.info("Already Authenticated");
			return SecurityContextHolder.getContext().getAuthentication();
		}
		/*
		 * Just for Demo - In real world scenario  get from token
		 */
		String user = request.getHeader("x-auth-user");
		
		if(StringUtils.isEmpty(user)) {
			throw new AccessDeniedException("Invalid User");
		}
		return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user, null));
	}
```
- Application Authentication provider is used to validate the user and set Authentication to TRUE.

```
@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("ApplicationAuthenticationProvider authenticate()");
		
		/*
		 * For Demo Only
		 * Ideally these details will be fetched from Database or any OAuth Provider
		 */
		if(!authentication.getName().equalsIgnoreCase("admin")) {
			throw new AccessDeniedException("Invalid User");
		}
		
		/*
		 * Roles should be fetched from database or some other service
		 */
		String[] roles = new String[] { "ROLE_AUTHENTICATED"};
		
		//Below Constructor call will set the authentication to TRUE
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null,AuthorityUtils.createAuthorityList(roles));
	}
```
- Add the Authentication Object to Security Context inside successfulAuthentication() in custom filter

```
@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		LOGGER.info("Inside Successful Authentication Handler");
		
		//Create an empty context and add the Authentication object to current Context
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		
		LOGGER.info("Add the Authentication Object to Security Context");
	
		chain.doFilter(request, response);
		
	}
```
