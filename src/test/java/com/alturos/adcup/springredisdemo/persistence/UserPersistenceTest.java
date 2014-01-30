package com.alturos.adcup.springredisdemo.persistence;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alturos.adcup.SpringRedisDemoApp;
import com.alturos.adcup.springredisdemo.core.domain.User;
import com.alturos.adcup.springredisdemo.events.AddUserEvent;
import com.alturos.adcup.springredisdemo.events.RequestUserByIdEvent;
import com.alturos.adcup.springredisdemo.persistence.services.UserPersistenceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenseServiceConfiguration.class, SpringRedisDemoApp.class})
public class UserPersistenceTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserPersistenceTest.class);
	
	@BeforeClass
	public static void setup() {
		LOG.debug("inititializing test case ..");
		
	}
	
	@Autowired
	private UserPersistenceService service;
	
	@Test
	public void isUserAddedSuccessfully() {
		LOG.debug("test isUserAddedSuccessfully ...");		
		String userId = service.addUser(new AddUserEvent(new User("userName", "password")));
		
		assertNotNull(userId);
		//assertTrue(userId >= 0);	
	}
	
	@Test
	public void isUserFoundById() {
		LOG.debug("test isUserFoundById ...");
		
		// TODO add user has already been tested, maybe we can remove this obsolete call ...		
		String userId = service.addUser(new AddUserEvent(new User("userName", "password")));
		
		User user = service.getUserById(new RequestUserByIdEvent(userId));
		assertNotNull(user);
		assertEquals("userName", user.getUserName());
		assertEquals("password", user.getPassword());		
	}
	

}
