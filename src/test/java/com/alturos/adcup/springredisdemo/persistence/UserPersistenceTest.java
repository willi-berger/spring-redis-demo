package com.alturos.adcup.springredisdemo.persistence;

import static org.junit.Assert.*;

import java.util.List;

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
		LOG.debug("***********  test isUserAddedSuccessfully ...");		
		String userId = service.addUser(new AddUserEvent(new User("UserAddedSuccessfully", "password")));
		
		assertNotNull(userId);
		//assertTrue(userId >= 0);	
	}
	
	@Test
	public void isUserFoundById() {
		LOG.debug("***********  test isUserFoundById ...");
		
		// TODO add user has already been tested, maybe we can remove this obsolete call ...		
		String userId = service.addUser(new AddUserEvent(new User("isUserFoundById", "password")));
		
		User user = service.getUserById(new RequestUserByIdEvent(userId));
		assertNotNull(user);
		assertEquals("isUserFoundById", user.getUserName());
		assertEquals("password", user.getPassword());		
	}
	
	@Test
	public void isUserFoundByName() {
		LOG.debug("***********  test isUserFoundById ...");
		
		// TODO add user has already been tested, maybe we can remove this obsolete call ...		
		service.addUser(new AddUserEvent(new User("isUserFoundByName", "password")));
		
		User user = service.getUserByUserName(new RequestUserByNameEvent("isUserFoundByName"));
		assertNotNull(user);
		assertEquals("isUserFoundByName", user.getUserName());
		assertEquals("password", user.getPassword());		
	}

	@Test 
	public void canGetUsersSortedByUserNames() {
		LOG.debug("***********  test can we get users sorted by user name ...");
		
		// add some users
		for (int i=0; i<10; i++) {
			String name = "user-" + (10-i);
			LOG.debug("add user with name {}", name);
			service.addUser(new AddUserEvent(new User(name, "password")));
		}
		
		// get the users
		List<User> users = service.getUsers(new RequestGetUsersEvent(0, 5));
		assertNotNull(users);
		assertEquals(5, users.size());
		for (User u: users) {
			LOG.debug("--> u: {} ", u);
			assertNotNull(u.getId());
		}

	}
	
}
