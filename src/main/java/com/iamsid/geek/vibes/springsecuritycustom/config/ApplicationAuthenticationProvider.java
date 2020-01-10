package com.iamsid.geek.vibes.springsecuritycustom.config;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ApplicationAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authentication Process Inside Authentication Provider Started");
		
		/*
		 * For Demo Only
		 * Ideally these details will be fetched from Database or any OAuth Provider
		 */
		if(!authentication.getName().equalsIgnoreCase("admin")) {
			throw new BadCredentialsException("Invalid User");
		}

		/*
		 * Roles should be fetched from database or some other service
		 */
		String[] roles = new String[] { "ROLE_VIEWER","ROLE_WRITE"};
		
		log.info("Authentication Process Inside Authentication Provider Completed");
		
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null,AuthorityUtils.createAuthorityList(roles));
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
