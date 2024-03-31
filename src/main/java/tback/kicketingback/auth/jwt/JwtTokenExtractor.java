package tback.kicketingback.auth.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import tback.kicketingback.auth.exception.exceptions.AuthenticationFailException;

@Component
public class JwtTokenExtractor {

	private static final String PREFIX_BEARER = "Bearer ";
	private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
	private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

	public String extractAccessToken(final HttpServletRequest request) {
		final String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
		if (StringUtils.hasText(accessToken) && accessToken.startsWith(PREFIX_BEARER)) {
			return accessToken.substring(PREFIX_BEARER.length());
		}
		throw new AuthenticationFailException(JwtTokenType.ACCESS_TOKEN);
	}

	public String extractRefreshToken(final HttpServletRequest request) {
		final String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
		if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(PREFIX_BEARER)) {
			return refreshToken.substring(PREFIX_BEARER.length());
		}
		throw new AuthenticationFailException(JwtTokenType.REFRESH_TOKEN);
	}
}
