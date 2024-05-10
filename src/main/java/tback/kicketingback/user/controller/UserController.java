package tback.kicketingback.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.dto.request.AddressRequest;
import tback.kicketingback.user.dto.request.InformRequest;
import tback.kicketingback.user.dto.request.NameRequest;
import tback.kicketingback.user.dto.request.PasswordRequest;
import tback.kicketingback.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/new-address")
    public ResponseEntity<Void> updateAddress(@JwtLogin User user, @RequestBody AddressRequest addressRequest) {

        userService.updateAddress(userService.findUser(user.getEmail()), addressRequest.address());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/new-password")
    public ResponseEntity<Void> changePassword(@JwtLogin User user, @RequestBody PasswordRequest passwordRequest) {

        userService.matchPassword(user, passwordRequest.confirmPassword());
        userService.changePassword(userService.findUser(user.getEmail()), passwordRequest.newPassword());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/confirm")
    public ResponseEntity<Void> matchInform(@JwtLogin User user, @RequestBody InformRequest informRequest) {

        userService.matchInform(user, informRequest.email(), informRequest.name());
        userService.setRandomPassword(user, user.getEmail());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(@JwtLogin User user, @RequestBody NameRequest nameRequest) {

        userService.updateName(userService.findUser(user.getEmail()), nameRequest.name());

        return ResponseEntity.ok().build();
    }

}
