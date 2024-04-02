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
}
