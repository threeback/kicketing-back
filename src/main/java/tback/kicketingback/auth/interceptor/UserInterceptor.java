package tback.kicketingback.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.jwt.JwtTokenExtractor;
import tback.kicketingback.auth.jwt.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

	private final JwtTokenExtractor jwtTokenExtractor;
	private final JwtTokenProvider jwtTokenProvider;

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
		jwtTokenProvider.extractEmailFromAccessToken(accessToken);
		return true;
	}

	private boolean isOptionRequest(HttpServletRequest request) {
		return request.getMethod().equals("OPTIONS");
	}
}
