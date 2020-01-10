package com.iamsid.geek.vibes.springsecuritycustom.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service(value="roleService")
@Slf4j
public class RoleService {

	public List<String> getRoles(){
		log.info("Get the Roles available in Application");
		/*
		 *Ideally this will be fetched from a Persistent source or cache 
		 */
		return Arrays.asList(new String[] {"ROLE_ADMIN","ROLE_WRITE"});
	}
}
