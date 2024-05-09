package tback.kicketingback.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.mail.SmtpService;

import static tback.kicketingback.auth.oauth.util.PasswordUtil.isPasswordFormat;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SmtpService smtpService;

    public User findUser(final String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
    }

    @Transactional
    public void updateAddress(User user, String address) {
        user.updateAddress(address);
    }

    @Transactional
    public void changePassword(User user, String password) {
        if (!isPasswordFormat(password)) {
            throw new AuthInvalidPasswordException();
        }

        user.changePassword(password);
    }

    public void matchPassword(User user, String confirmPassword) {
        user.validatePassword(confirmPassword);
    }

    public void matchInform(User user, String email, String name) {

        user.validateEmail(email);
        user.validateName(name);
    }

    @Transactional
    public void setRandomPassword(User user, String email) {
        String newPassword = PasswordUtil.createRandomPassword();
        smtpService.sendRandomPassword(email, newPassword);
        user.changePassword(newPassword);
    }
}