package com.iamsid.geek.vibes.springsecuritycustom.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApplicationAuthFilter extends AbstractAuthenticationProcessingFilter {

	private final static Log LOGGER = LogFactory.getLog(ApplicationAuthFilter.class);

	public ApplicationAuthFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));

	}


	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
		return;
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		LOGGER.error("unsuccessfulAuthentication");
		super.unsuccessfulAuthentication(request, response, failed);
	}


	@Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("requiresAuthentication");
		return super.requiresAuthentication(request, response);
    }

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
	
}
