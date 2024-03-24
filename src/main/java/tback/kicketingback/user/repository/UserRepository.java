package tback.kicketingback.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
