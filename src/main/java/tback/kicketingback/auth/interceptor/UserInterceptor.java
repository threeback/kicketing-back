package tback.kicketingback.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.jwt.JwtTokenExtractor;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

	private final JwtTokenExtractor jwtTokenExtractor;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	public boolean preHandle(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler
	) {
		if (isOptionRequest(request)) {
			return true;
		}

		final String accessToken = jwtTokenExtractor.extractAccessToken(request);
		String email = jwtTokenProvider.extractEmailFromAccessToken(accessToken);
		validateMemberExist(email);
		return true;
	}

	private boolean isOptionRequest(HttpServletRequest request) {
		return request.getMethod().equals("OPTIONS");
	}

	private void validateMemberExist(final String email) {
		userRepository.findByEmail(email)
			.orElseThrow(NoSuchUserException::new);
	}
}
