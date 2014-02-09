package com.alturos.adcup.springredisdemo.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alturos.adcup.springredisdemo.persistence.repository.PermissionRedisRepository;
import com.alturos.adcup.springredisdemo.persistence.repository.UserRedisRepository;
import com.alturos.adcup.springredisdemo.persistence.services.UserPersistenceEventHandler;
import com.alturos.adcup.springredisdemo.persistence.services.UserPersistenceService;

/**
 * Spring redis demo test case configuration
 * 
 */
@Configuration
public class PersistenseServiceConfiguration {

	
	@Bean
	UserPersistenceService userPersistenceService() {
		return new UserPersistenceEventHandler();
	}

	@Autowired
	StringRedisTemplate template;
	
	@Bean
	UserRedisRepository userRedisRepository() {
		return new UserRedisRepository(template);
	}
	
	@Bean
	PermissionRedisRepository permissionRedisRepository() {
		return new PermissionRedisRepository(template);
	}
}
