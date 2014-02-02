package com.alturos.adcup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import com.alturos.adcup.springredisdemo.Person;

/**
 * Spring redis demo
 * 
 */
@Configuration
public class SpringRedisDemoApp {

	static final Logger LOG = LoggerFactory.getLogger(SpringRedisDemoApp.class);
	
	
	
	@Bean
	JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setDatabase(redisDbIndex());
		return factory;
	}

	/**
	 * the redis DB index to use
	 * @return
	 */
	protected int redisDbIndex() {
		return 0;
	}

	@Bean
	StringRedisTemplate template(JedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	// TODO can't we initialize 2 different templates as beans
	private static RedisTemplate<String, Person> personTemplate(
			RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Person> redisTemplate = new RedisTemplate<String, Person>();
		redisTemplate.setConnectionFactory(connectionFactory);
		
		
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		//redisTemplate.setValueSerializer(new JacksonJsonRedisSerializer<Person>(Person.class));
		
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

	public static void main(String[] args) {

		LOG.debug("Create AnnotationConfigApplicationContext .. ctx");
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringRedisDemoApp.class);
		

		LOG.debug("Get a StringRedisTemplateBean ");
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);

		String key = "hello.redis.key";
		String value = "hello world";
		LOG.debug("REDIS set key = {} value = {}", key, value);
		template.opsForValue().set(key, "hello redis");

		LOG.debug("REDIS get {}", key);

		String valueretrieved = template.opsForValue().get(key);
		LOG.debug("value = {}", valueretrieved);

		Person person = new Person("Willie", "Berger");
		RedisTemplate<String, Person> redisTemplate = personTemplate(template.getConnectionFactory());

		LOG.debug("REDIS set {}", person);
		redisTemplate.opsForValue().set("persons:willie", person);

		Person p = redisTemplate.opsForValue().get("persons:willie");
		LOG.debug("retrieved Person <{}>", p);

		LOG.debug("Closing ctx");
		ctx.close();

	}

}
