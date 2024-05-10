package tback.kicketingback.user.signout.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.domain.User;

@Service
public class SignOutService {

	private final RedisRepository refreshRedisRepository;

	public SignOutService(@Qualifier("refreshRedisRepository") RedisRepository refreshRedisRepository) {
		this.refreshRedisRepository = refreshRedisRepository;
	}

	public void signOutUser(User user) {
		refreshRedisRepository.deleteValues(user.getEmail());
	}
}
