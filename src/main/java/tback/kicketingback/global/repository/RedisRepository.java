package tback.kicketingback.global.repository;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisRepository {

	private final StringRedisTemplate redisTemplate;

	public void setValues(String key, String data) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data);
	}

	public void setValues(String key, String data, Duration duration) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data, duration);
	}

	@Transactional(readOnly = true)
	public Optional<String> getValues(String key) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return Optional.ofNullable(values.get(key));
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}

	public void expireValues(String key, int timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
	}

	public void setHashOps(String key, Map<String, String> data) {
		HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
		values.putAll(key, data);
	}

	@Transactional(readOnly = true)
	public String getHashOps(String key, String hashKey) {
		HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
		return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String)redisTemplate.opsForHash().get(key, hashKey) :
			"";
	}

	public void deleteHashOps(String key, String hashKey) {
		HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
		values.delete(key, hashKey);
	}

	public boolean checkExistsValue(String value) {
		return !value.equals("false");
	}
}
