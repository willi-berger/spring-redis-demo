package at.wberger.springredisdemo.persistence.repository;

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

import at.wberger.springredisdemo.core.domain.Permission;

public class PermissionRedisRepository {

	private static final Logger LOG = LoggerFactory
			.getLogger(PermissionRedisRepository.class);

	private final RedisAtomicLong permIdCounter;

	private RedisTemplate<String, Object> template;

	private static final String KEYS_PERMISSIONS_BY_ID = "perm";

	private static String KEY_PERM_ID_COUNTER = "global:permid";

	// TODO improve value mapping !!
	/*
	 * willie@willie-p6651at:~/work/redis-2.8.4$ ./src/redis-cli 127.0.0.1:6379>
	 * hget perm:1 created
	 * "\xac\xed\x00\x05sr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x01"
	 * 127.0.0.1:6379> hget perm:1 text "\xac\xed\x00\x05t\x00\x0cpermission 1"
	 * 127.0.0.1:6379>
	 */

	@Autowired
	// TODO improve constructor - use a connection factory instead of string
	// template
	public PermissionRedisRepository(StringRedisTemplate template) {
		this.template = new RedisTemplate<String, Object>();

		this.template.setConnectionFactory(template.getConnectionFactory());
		this.template.setKeySerializer(new StringRedisSerializer());
		this.template.setHashKeySerializer(new StringRedisSerializer());
		this.template.setValueSerializer(new StringRedisSerializer());
		// this.template.setHashValueSerializer(new StringRedisSerializer());
		// this.template.setHashValueSerializer(new OxmSerializer(new
		// CastorMarshaller(), new CastorMarshaller()));
		this.template.afterPropertiesSet();

		// TODO this creates a "global:uid" when we use another redis DB (e.g.
		// 9) this key is created before we switch DB to 9
		this.permIdCounter = new RedisAtomicLong(KEY_PERM_ID_COUNTER,
				template.getConnectionFactory());
	}

	public long addPermission(Permission p) {
		LOG.debug("Adding new permission {} to REDIS", p);
		long id = permIdCounter.incrementAndGet();
		String key = KEYS_PERMISSIONS_BY_ID + ":" + id;

		// add the permission as hash to REDIS
		BoundHashOperations<String, String, Object> permOps = template
				.boundHashOps(key);
		permOps.put("text", p.getText());
		permOps.put("validFrom", p.getValidFrom());
		permOps.put("validTo", p.getValidTo());
		permOps.put("created", p.getCreated());

		return id;
	}

	public Permission getPermissionById(String id) {
		LOG.debug("Get permisson by ID {}", id);

		String key = KEYS_PERMISSIONS_BY_ID + ":" + (id);
		BoundHashOperations<String, String, Object> userOps = template
				.boundHashOps(key);
		List<Object> mgetResultList = userOps.multiGet(Arrays.asList("created",
				"validFrom", "validTo", "text"));

		return new Permission((Integer) mgetResultList.get(0),
				(Integer) mgetResultList.get(1),
				(Integer) mgetResultList.get(2), (String) mgetResultList.get(3));
	}

	/**
	 * select the redis DB index to use
	 * 
	 * @param index
	 */
	public void select(int index) {
		LOG.debug("select redis DB {}", index);
		((JedisConnectionFactory) template.getConnectionFactory())
				.setDatabase(index);
	}

	public void flush() {
		LOG.debug("flush redis");
		template.getConnectionFactory().getConnection().flushDb();
	}

}
