package com.iamsid.geek.vibes.springsecuritycustom.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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

		LOGGER.info("Inside Successful Authentication Handler");
		if (!this.isAlreadyAuthenticated()) {
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authResult);
			SecurityContextHolder.setContext(context);
			LOGGER.info("Add the Authentication Object to Security Context");
		}

		chain.doFilter(request, response);

	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		LOGGER.error("unsuccessful Authentication:-"+failed.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
	}


	@Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("requiresAuthentication");
		return super.requiresAuthentication(request, response);
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		/*
		 * Just for Demo - In real world scenario  get from token
		 */
		String user = request.getHeader("x-auth-user");
		
		/*
		 * Avoid Authentication Process if already authenticated
		 */
		if(this.isAlreadyAuthenticated()) {
			return SecurityContextHolder.getContext().getAuthentication();
		}
		if(StringUtils.isEmpty(user)) {
			throw new InsufficientAuthenticationException("Missing Credentials");
		}

		LOGGER.info("Attempt Authtentication (Extract Header Value):-"+user);
		
		return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user, null));
	}
	private boolean isAlreadyAuthenticated() {
		if(SecurityContextHolder.getContext().getAuthentication()!=null 
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			LOGGER.info("Already Authenticated");
			return true;
		}
		return false;
	}
}
