package tback.kicketingback.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import tback.kicketingback.global.repository.RedisRepository;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host.refresh}")
	private String refreshHostName;

	@Value("${spring.data.redis.port.refresh}")
	private int refreshPort;

	@Value("${spring.data.redis.host.signup}")
	private String signupHostName;

	@Value("${spring.data.redis.port.signup}")
	private int signupPort;

	@Bean
	public LettuceConnectionFactory refreshRedisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(refreshHostName, refreshPort));
	}

	@Bean
	StringRedisTemplate refreshRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(refreshRedisConnectionFactory());
		template.setEnableTransactionSupport(true);
		return template;
	}

	@Bean
	@Qualifier("refreshRedisRepository")
	RedisRepository refreshRedisRepository() {
		return new RedisRepository(refreshRedisTemplate());
	}

	@Bean
	public LettuceConnectionFactory signupRedisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(signupHostName, signupPort));
	}

	@Bean
	StringRedisTemplate signupRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(signupRedisConnectionFactory());
		template.setEnableTransactionSupport(true);
		return template;
	}

	@Bean
	@Qualifier("signupRedisRepository")
	RedisRepository signupRedisRepository() {
		return new RedisRepository(signupRedisTemplate());
	}
}
