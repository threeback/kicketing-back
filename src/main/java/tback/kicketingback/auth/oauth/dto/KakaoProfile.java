package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfile(@JsonProperty("nickname") String nickname) {
}
