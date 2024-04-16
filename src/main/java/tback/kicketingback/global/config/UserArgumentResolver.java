package tback.kicketingback.global.config;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.exception.exceptions.TokenExtractionException;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.auth.jwt.JwtTokenType;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtTokenProvider jwtTokenProvider;

	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JwtLogin.class);
	}

	@Override
	public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		final String jwtToken = Arrays.stream(
				Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)).getCookies())
			.filter(cookie -> cookie.getName().equals(HttpHeaders.AUTHORIZATION))
			.findFirst()
			.map(Cookie::getValue)
			.orElseThrow(() -> new TokenExtractionException(JwtTokenType.ACCESS_TOKEN));

		String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
		return userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
	}
}
