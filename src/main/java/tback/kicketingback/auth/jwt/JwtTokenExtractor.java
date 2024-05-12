package tback.kicketingback.auth.jwt;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import tback.kicketingback.auth.exception.exceptions.TokenExtractionException;

@Component
public class JwtTokenExtractor {

	private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;

	public String extractAccessToken(final HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_HEADER))
				.findFirst()
				.map(Cookie::getValue)
				.filter(StringUtils::hasText)
			)
			.orElseThrow(() -> new TokenExtractionException(JwtTokenType.ACCESS_TOKEN));
	}
}
