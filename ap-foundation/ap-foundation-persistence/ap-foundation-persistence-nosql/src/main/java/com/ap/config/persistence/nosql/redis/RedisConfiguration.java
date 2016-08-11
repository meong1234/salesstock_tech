package com.ap.config.persistence.nosql.redis;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ap.config.base.FoundationProperties;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisRepositories(value= {"com"}, includeFilters = @ComponentScan.Filter(pattern = "..*RedisRepository", type = FilterType.REGEX))
@Slf4j
public class RedisConfiguration {
	
	@Inject
	private FoundationProperties foundationProperties;

    /**
     * Use this to refine the Connection Pool.
     *
     * @return RedisConnectionFactory
     */
    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
    	log.debug("Setup jedisConnectionFactory started" );
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisConnectionFactory.setHostName(foundationProperties.getRedis().getHost());
        jedisConnectionFactory.setPassword(foundationProperties.getRedis().getPassword());
        jedisConnectionFactory.setPort(foundationProperties.getRedis().getPort());
        jedisConnectionFactory.setUsePool(foundationProperties.getRedis().isUsepool());

        if (foundationProperties.getRedis().isUsepool()) {
            jedisPoolConfig.setMaxTotal(foundationProperties.getRedis().getMaxActive());
            jedisPoolConfig.setMaxIdle(foundationProperties.getRedis().getMaxIdle());
            jedisPoolConfig.setMaxWaitMillis(foundationProperties.getRedis().getMaxWaitMillis());
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPoolConfig.setTestOnReturn(true);
        }

        return jedisConnectionFactory;
    }
	
	@Bean
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<Object>(Object.class);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {

		RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setDefaultSerializer(stringRedisSerializer());
		template.setHashValueSerializer(jackson2JsonRedisSerializer());
		return template;
	}
}
