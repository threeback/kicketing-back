package tback.kicketingback.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.dto.AddressRequest;
import tback.kicketingback.user.dto.InformRequest;
import tback.kicketingback.user.dto.PasswordRequest;
import tback.kicketingback.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/new-address")
    public ResponseEntity<Void> updateAddress(@JwtLogin User user, @RequestBody AddressRequest addressRequest) {

        userService.updateAddress(userService.findUser(user.getEmail()), addressRequest.address());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/new-password")
    public ResponseEntity<Void> changePassword(@JwtLogin User user, @RequestBody PasswordRequest passwordRequest) {

        userService.matchPassword(user, passwordRequest.confirmPassword());
        userService.changePassword(userService.findUser(user.getEmail()), passwordRequest.newPassword());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/confirm")
    public ResponseEntity<Void> matchInform(@JwtLogin User user, @RequestBody InformRequest informRequest) {

        userService.matchInform(user, informRequest.email(), informRequest.name());
        userService.setRandomPassword(user, user.getEmail());

        return ResponseEntity.ok().build();
    }

}
