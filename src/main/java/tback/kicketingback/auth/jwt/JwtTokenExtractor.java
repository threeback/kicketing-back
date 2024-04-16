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
	private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

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

	public String extractRefreshToken(final String refreshToken) {
		if (StringUtils.hasText(refreshToken)) {
			return refreshToken;
		}
		throw new TokenExtractionException(JwtTokenType.REFRESH_TOKEN);
	}
}
