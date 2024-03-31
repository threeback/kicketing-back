package tback.kicketingback.global.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	private static final int TOKEN_INDEX = 1;

	private final JwtTokenProvider jwtTokenProvider;

	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JwtLogin.class);
	}

	@Override
	public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwtToken = authorization.split(" ")[TOKEN_INDEX];
		String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
		return userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
	}
}
