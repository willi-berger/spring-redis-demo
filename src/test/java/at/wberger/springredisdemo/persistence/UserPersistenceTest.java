package at.wberger.springredisdemo.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.wberger.SpringRedisDemoApp;
import at.wberger.springredisdemo.core.domain.User;
import at.wberger.springredisdemo.events.AddUserEvent;
import at.wberger.springredisdemo.events.RequestGetUsersEvent;
import at.wberger.springredisdemo.events.RequestUserByIdEvent;
import at.wberger.springredisdemo.events.RequestUserByNameEvent;
import at.wberger.springredisdemo.persistence.repository.UserRedisRepository;
import at.wberger.springredisdemo.persistence.services.UserPersistenceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenseServiceConfiguration.class,
		SpringRedisDemoApp.class } )
public class UserPersistenceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserPersistenceTest.class);

	@Autowired
	private UserRedisRepository repository;
	
	@BeforeClass
	public static void setup() {
		LOG.debug("inititializing test class ..");
		
	}

	@Before
	public void before() {
		LOG.debug("inititializing test case ..");
		repository.flush();
	}
	
	
	@Autowired
	private UserPersistenceService service;

	@Test
	public void isUserAddedSuccessfully() {
		LOG.debug("***********  test isUserAddedSuccessfully ...");
		String userId = service.addUser(new AddUserEvent(new User(null,
				"UserAddedSuccessfully", "password")));

		assertNotNull(userId);
		// assertTrue(userId >= 0);
	}

	@Test
	public void isUserFoundById() {
		LOG.debug("***********  test isUserFoundById ...");

		// TODO add user has already been tested, maybe we can remove this
		// obsolete call ...
		String userId = service.addUser(new AddUserEvent(new User(null,
				"isUserFoundById", "password")));

		User user = service.getUserById(new RequestUserByIdEvent(userId));
		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals("isUserFoundById", user.getUserName());
		assertEquals("password", user.getPassword());
	}

	@Test
	public void isUserFoundByName() {
		LOG.debug("***********  test isUserFoundById ...");

		String id = service.addUser(new AddUserEvent(new User(null, "isUserFoundByName",
				"passwd")));

		User user = service.getUserByUserName(new RequestUserByNameEvent(
				"isUserFoundByName"));
		assertNotNull(user);
		assertEquals(id, user.getId());
		assertEquals("isUserFoundByName", user.getUserName());
		assertEquals("passwd", user.getPassword());
	}

	@Test
	public void canGetUsersSortedByUserNames() {
		LOG.debug("***********  test can we get users sorted by user name ...");

		// add test users for sort
		addtestUsers();

		// get the users
		List<User> users = service.getUsers(new RequestGetUsersEvent(4, 5));
		assertNotNull(users);
		assertEquals(5, users.size());
		for (User u : users) {
			LOG.debug("--> u: {} ", u);
			assertNotNull(u.getId());
		}
	}

	private void addtestUsers() {
		addUserForName("Otto", "Paula", "Quelle", "Richard", "Siegfried",
				"Theodor", "Ulrich", "Caesar", "Viktor", "Wilhelm", "Zeppelin",
				"Anton", "Xanthippe", "Ypsilon", "Berta", "Ludwig", "Martha",
				"Nordpol", "Friedrich", "Dora", "Emil", "Gustav", "Heinrich",
				"Ida", "Julius", "Karl");
	}

	private void addUserForName(String... userNames) {
		for (String userName : userNames) {
			service.addUser(new AddUserEvent(new User(null, userName, "passwd")));
		}
	}

}
