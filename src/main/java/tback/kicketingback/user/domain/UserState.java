package tback.kicketingback.user.domain;

import lombok.Getter;

@Getter
public enum UserState {
    REGULAR_USER((short) 0),
    OAUTH_USER((short) 1);

    private final Short state;

    UserState(Short state) {
        this.state = state;
    }
}
