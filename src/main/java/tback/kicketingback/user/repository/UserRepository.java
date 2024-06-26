package tback.kicketingback.user.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import tback.kicketingback.user.domain.User;

public interface UserRepository extends Repository<User, Long> {

	Optional<User> findByEmail(String email);

	void save(User user);

	boolean existsByEmail(String email);
}
