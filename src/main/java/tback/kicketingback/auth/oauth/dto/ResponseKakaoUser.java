package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseKakaoUser(
	@JsonProperty("id") Long id,
	@JsonProperty("kakao_account") KakaoUserInfo kakaoUserInfo) {
}
