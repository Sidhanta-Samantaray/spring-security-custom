package com.iamsid.geek.vibes.springsecuritycustom.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ApplicationController {

	@GetMapping(value="/ping")
	public @ResponseBody Map<String,String> ping(){
		return ImmutableMap.of("message","Ping Success");
	}
	
	
	@GetMapping(value="v1/secure/ping")
	@PreAuthorize(value = "hasAnyRole(@roleService.getRoles())")//Get the Roles from Service class Method
	public @ResponseBody Map<String,String> securePing(Principal principal){
		log.info("Success");
		return ImmutableMap.of("message","Secure Ping Success","user",principal.getName());
	}
}
