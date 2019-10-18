package com.iamsid.geek.vibes.springsecuritycustom.config;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ApplicationAuthenticationProvider implements AuthenticationProvider {

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
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		
		/*
		 * Roles should be fetched from database or some other service
		 */
		String[] roles = new String[] { "ROLE_AUTHENTICATED"};
		
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null,AuthorityUtils.createAuthorityList(roles));
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
