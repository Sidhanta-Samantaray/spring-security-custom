package com.iamsid.geek.vibes.springsecuritycustom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringSecurityCustomApplicationTests {

	@Autowired
    private WebApplicationContext context;
	@Autowired
    private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();

	}   
	
	@Test
	public void testPing() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				   .get("/ping")
				   .accept(MediaType.APPLICATION_JSON))
		           .andDo(MockMvcResultHandlers.print())
		           .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//Send without any User
	@Test
	public void testSecurePingMissingHeader()  {
		
		try {
			 this.mockMvc.perform(MockMvcRequestBuilders
						   .get("/v1/secure/ping")
						   .accept(MediaType.APPLICATION_JSON))
				           .andDo(MockMvcResultHandlers.print())
				           .andExpect(MockMvcResultMatchers.status().isUnauthorized())
				           .andExpect(MockMvcResultMatchers.status().reason("Missing Credentials"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Send without any User
	@Test
	public void testSecurePingMissingHeaderValue()  {
		
		try {
			 this.mockMvc.perform(MockMvcRequestBuilders
						   .get("/v1/secure/ping")
						   .header("x-auth-user", "")
						   .accept(MediaType.APPLICATION_JSON))
				           .andDo(MockMvcResultHandlers.print())
				           .andExpect(MockMvcResultMatchers.status().isUnauthorized())
				           .andExpect(MockMvcResultMatchers.status().reason("Missing Credentials"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Send with Invalid User
	@Test
	public void testSecurePingBad()  {
		
		try {
			 this.mockMvc.perform(MockMvcRequestBuilders
						   .get("/v1/secure/ping")
						   .header("x-auth-user", "invalid")
						   .accept(MediaType.APPLICATION_JSON))
				           .andDo(MockMvcResultHandlers.print())
				           .andExpect(MockMvcResultMatchers.status().isUnauthorized())
				           .andExpect(MockMvcResultMatchers.status().reason("Invalid User"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Send with valid User
	@Test
	public void testSecurePingValid()  {
		
		try {
			 this.mockMvc.perform(MockMvcRequestBuilders
						   .get("/v1/secure/ping")
						   .header("x-auth-user", "admin")
						   .accept(MediaType.APPLICATION_JSON))
				           .andDo(MockMvcResultHandlers.print())
				           .andExpect(MockMvcResultMatchers.status().isOk());
				    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
