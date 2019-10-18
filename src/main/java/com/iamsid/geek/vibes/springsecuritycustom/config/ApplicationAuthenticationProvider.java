package com.iamsid.geek.vibes.springsecuritycustom.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;


@Service
public class ApplicationAuthenticationProvider implements AuthenticationProvider {

	private static final Log LOGGER=LogFactory.getLog(ApplicationAuthenticationProvider.class);
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LOGGER.info("Authentication Process Inside Authentication Provider Started");
		
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
		
		LOGGER.info("Authentication Process Inside Authentication Provider Completed");
		
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null,AuthorityUtils.createAuthorityList(roles));
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
