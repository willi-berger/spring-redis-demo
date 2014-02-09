package com.alturos.adcup.springredisdemo.persistence.repository;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alturos.adcup.springredisdemo.core.domain.Permission;




public class PermissionRedisRepository {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionRedisRepository.class);
	
	private final RedisAtomicLong permIdCounter;
	
	private RedisTemplate<String, String> template;

	private static final String KEYS_PERMISSIONS_BY_ID = "perm";

	private static String KEY_PERM_ID_COUNTER = "global:permid";

	@Autowired
	//TODO improve constructor - use a connection factory instead of string template
	public PermissionRedisRepository(StringRedisTemplate template) {
		this.template = new RedisTemplate<String, String>();
		
		this.template.setConnectionFactory(template.getConnectionFactory());
		this.template.setKeySerializer(new StringRedisSerializer());
		this.template.setHashKeySerializer(new StringRedisSerializer());
		this.template.afterPropertiesSet();
		
		//TODO this creates a "global:uid" when we use another redis DB (e.g. 9) this key is created before we switch DB to 9
		this.permIdCounter = new RedisAtomicLong(KEY_PERM_ID_COUNTER , template.getConnectionFactory());
	}
	
	public long addPermission(Permission p) {
		LOG.debug("Adding new permission {} to REDIS", p);
		long id = permIdCounter.incrementAndGet();
		String key = KEYS_PERMISSIONS_BY_ID +":"+id;
		
		// add the permission as hash to REDIS
		BoundHashOperations<String, String, Object> permOps = template.boundHashOps(key);
		permOps.put("text", p.getText());
		permOps.put("validFrom", p.getValidFrom());
		permOps.put("validTo", p.getValidTo());
		permOps.put("created", p.getCreated());
		
		return id;
	}

	public Permission getPermissionById(String id) {
		LOG.debug("Get permisson by ID {}", id);
		
		String key = KEYS_PERMISSIONS_BY_ID+":"+(id);
		BoundHashOperations<String, String, Object> userOps = template.boundHashOps(key);
		List<Object> mgetResultList = userOps.multiGet(Arrays.asList("created", "validFrom", "validTo", "text"));
		
		return new Permission((Integer)mgetResultList.get(0), (Integer)mgetResultList.get(1), (Integer)mgetResultList.get(2),
				(String)mgetResultList.get(3));
	}
	
	/**
	 * select the redis DB index to use
	 * @param index
	 */
	public void select(int index) {
		LOG.debug("select redis DB {}", index);
		((JedisConnectionFactory)template.getConnectionFactory()).setDatabase(index);
	}

	public void flush() {
		LOG.debug("flush redis");
		template.getConnectionFactory().getConnection().flushDb();		
	}

}
