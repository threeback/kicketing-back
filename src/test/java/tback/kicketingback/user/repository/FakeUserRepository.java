package tback.kicketingback.user.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import tback.kicketingback.user.domain.User;

public class FakeUserRepository implements UserRepository {

	private final ConcurrentHashMap<String, User> map;

	public FakeUserRepository(ConcurrentHashMap map) {
		this.map = map;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(map.get(email));
	}

	@Override
	public void save(User user) {
		map.put(user.getEmail(), user);
	}

	@Override
	public boolean existsByEmail(String email) {
		return map.containsKey(email);
	}
}
