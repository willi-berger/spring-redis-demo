package com.alturos.adcup.springredisdemo.persistence.redisrepo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alturos.adcup.SpringRedisDemoApp;
import com.alturos.adcup.springredisdemo.core.domain.Permission;
import com.alturos.adcup.springredisdemo.persistence.PersistenseServiceConfiguration;
import com.alturos.adcup.springredisdemo.persistence.repository.PermissionRedisRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenseServiceConfiguration.class,
		SpringRedisDemoApp.class } )
public class PermissionRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(PermissionRepositoryTest.class);

	@Autowired
	private PermissionRedisRepository repository;
	
	@BeforeClass
	public static void setup() {
		LOG.debug("inititializing test class ..");
		
	}

	@Before
	public void before() {
		LOG.debug("inititializing test case ..");
		
		repository.flush();
	}
	
	
	@Test
	public void isPermissionAddedSuccessfully() {
		LOG.debug("***********  test isPermissionAddedSuccessfully ...");
		
		Permission p1 = new Permission(1, 10, 20, "permission 1");
		
		long id = repository.addPermission(p1);

		assertTrue(id > 0);
		
		Permission p2 = repository.getPermissionById(String.valueOf(id));
		assertNotNull(p2);
		assertEquals(1, p2.getCreated());
		assertEquals(10, p2.getValidFrom());
		assertEquals(20, p2.getValidTo());
		assertEquals("permission 1", p2.getText());
	}


}
