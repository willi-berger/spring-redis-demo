package com.alturos.adcup.springredisdemo.persistence.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alturos.adcup.springredisdemo.persistence.domain.User;




public class UserRedisRepository {

	private static final Logger LOG = LoggerFactory.getLogger(UserRedisRepository.class);

	private static final String KEY_USER_ID_COUNTER = "global:uid";
	
	private static final String KEYS_USERS_BY_ID = "uid";

	private static final String KEY_USERS_LIST = "users";	
	
	private final RedisAtomicLong userIdCounter;
	
	private StringRedisTemplate template;

	@Autowired
	public UserRedisRepository(StringRedisTemplate template) {
		this.template = template;
		
		//TODO this creates a "global:uid" when we use another redis DB (e.g. 9) this key is created before we switch DB to 9
		this.userIdCounter = new RedisAtomicLong(KEY_USER_ID_COUNTER, template.getConnectionFactory());
	}

	/**
	 * Add a {@link User} to REDIS
	 * 
	 * - add user properties as hash with key <i>uid:[id]</i>
	 * - add reverse lookup id by userName with key <i>user:[username]:id</i>
	 * - add id to list <i>users</i>
	 * 
	 * @param user
	 * @return
	 */
	public long addUser(User user) {
		LOG.debug("Adding new user {} to REDIS repo", user);
				
		long id = userIdCounter.incrementAndGet();
		String key = keyForUserById(id);
		BoundHashOperations<String, String, String> userOps = template.boundHashOps(key);
		userOps.put("name", user.getUserName());
		userOps.put("password", user.getPassword());
		
		// reverse key: userName -> uid
		String uidByUnameKey = keyForUserByName(user.getUserName());
		template.boundValueOps(uidByUnameKey).set(String.valueOf(id));
		
		// add to global user list
		template.boundListOps(KEY_USERS_LIST).leftPush(String.valueOf(id));
						
		return id;
	}
	
	/**
	 * Get a user by <i>id</i>
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId) {
		LOG.debug("Get user by ID {}", userId);
		
		String key = keyForUserById(userId);
		BoundHashOperations<String, String, String> userOps = template.boundHashOps(key);
		List<String> mgetResultList = userOps.multiGet(Arrays.asList("name", "password"));
		
		return new User(userId, mgetResultList.get(0), mgetResultList.get(1));
	}
	
	/**
	 * Get a user by <i>userName</i>
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByName(String userName) {
				
		LOG.debug("Get user by user name {}", userName);
		String uid = template.boundValueOps(keyForUserByName(userName)).get();
		
		return getUserById(uid);
	}
	
	/**
	 * Get a list of {@link User}s sorted by user name 
	 * starting at index <code>start</code> and containing up to <code>len</code> items
	 * 
	 * @param start
	 * @param len
	 * @return
	 */
	public List<User> getUsers(int start, int len) {
		LOG.debug("get users start {} len {}", start, len);
		SortQuery<String> query = 
				SortQueryBuilder.sort(KEY_USERS_LIST)					
					.by("uid:*->name").alphabetical(true)
					.get("#")
					.get("uid:*->name")
					.get("uid:*->password")
					.limit(start, len).order(Order.ASC).build();
		
		BulkMapper<User, String> bm = new BulkMapper<User, String>() {
			@Override
			public User mapBulk(List<String> tuple) {
				User u = new User(tuple.get(0), tuple.get(1), tuple.get(2));
				return u;
			}			
		};
		
		List<User> users = template.sort(query, bm);
		return users;
	}
	
	
	
	private String keyForUserByName(String userName) {		
		return "user:" + userName + ":uid";
	}


	private String keyForUserById(String userId) {
		return KEYS_USERS_BY_ID + ":" + userId;
	}

	private String keyForUserById(long id) {
		return KEYS_USERS_BY_ID + ":" + Long.valueOf(id);
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
