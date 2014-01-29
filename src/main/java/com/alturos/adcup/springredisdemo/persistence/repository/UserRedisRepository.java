package com.alturos.adcup.springredisdemo.persistence.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alturos.adcup.springredisdemo.persistence.domain.User;


public class UserRedisRepository {

	private static final Logger LOG = LoggerFactory.getLogger(UserRedisRepository.class);

	private static final String KEY_USER_ID_COUNTER = "global:uid";
	
	private static final String KEYS_USERS_BY_ID = "uid";
	
	
	private final RedisAtomicLong userIdCounter;
	
	StringRedisTemplate template;

	@Autowired
	public UserRedisRepository(StringRedisTemplate template) {
		this.template = template;
		this.userIdCounter = new RedisAtomicLong(KEY_USER_ID_COUNTER, template.getConnectionFactory());
	}
	
	

	public Long addUser(User user) {
		LOG.debug("Adding new user {} to REDIS repo", user);
				
		long id = userIdCounter.incrementAndGet();
		String key = KEYS_USERS_BY_ID + ":" + String.valueOf(id);
		BoundHashOperations<String, String, String> userOps = template.boundHashOps(key);
		userOps.put("name", user.getUserName());
		userOps.put("password", user.getPassword());
						
		return id;
	}
}
