package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
	@JsonProperty("id") String responseId,
	@JsonProperty("email") String email,
	@JsonProperty("profile") KakaoProfile kakaoProfile) {

}
