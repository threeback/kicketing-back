package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUserInfo(
	@JsonProperty("id") String responseId,
	@JsonProperty("email") String email,
	@JsonProperty("name") String name) {

}
