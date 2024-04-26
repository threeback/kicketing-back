package tback.kicketingback.auth.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletResponse;

public record TokenResponse(String accessToken, String refreshToken) {

	public static TokenResponse of(String accessToken, String refreshToken) {
		return new TokenResponse(accessToken, refreshToken);
	}

	public void setAccessToken(HttpServletResponse response, int expirationTime) {
		ResponseCookie accessTokenCookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, accessToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(expirationTime)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
	}
}
