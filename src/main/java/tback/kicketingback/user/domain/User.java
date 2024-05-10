package tback.kicketingback.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import tback.kicketingback.global.dto.BaseTimeEntity;
import tback.kicketingback.user.exception.exceptions.*;

import java.util.regex.Pattern;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.encode;

@Entity
@Getter
@DynamicInsert
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 320)
    private String email;

    @Column(nullable = false, columnDefinition = "char(64)")
    private String password;

    @Column
    private String address;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Short state;

    protected User() {
    }

    private User(final Long id, final String email, final String password, final String name, final Short state) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.state = state;
    }

    private static final String DEFAULT_EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final String DEFAULT_NAME_REGEX = "^[가-힣a-zA-Z]{2,20}$";

    /**
     * @param email
     * @param password
     * @param name
     * @return
     * @state Default 는 0으로 설정됨, OAuth 는 1로 설정.
     */
    public static User of(final String email, final String password, final String name, UserState userState) {
        validateCreateMember(email, password, name);
        return new User(null, email, encode(password), name, userState.getState());
    }

    private static void validateCreateMember(final String email, final String password, final String name) {
        if (!isEmailFormat(email)) {
            throw new EmailFormatException();
        }

        if (isEmpty(password)) {
            throw new UserPasswordEmptyException();
        }

        if (!isNameFormat(name)) {
            throw new AuthInvalidNameException();
        }
    }

    private static boolean isEmailFormat(final String email) {
        return Pattern.matches(DEFAULT_EMAIL_REGEX, email);
    }

    private static boolean isNameFormat(final String name) {
        return Pattern.matches(DEFAULT_NAME_REGEX, name);
    }

    private static boolean isEmpty(final String password) {
        return password.isBlank();
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(encode(password))) {
            throw new AuthInvalidPasswordException();
        }
    }

    public void validateEmail(final String email) {
        if (!this.email.equals(email)) {
            throw new AuthInvalidEmailException();
        }
    }

    public void validateName(final String name) {
        if (!this.name.equals(name)) {
            throw new AuthInvalidNameException();
        }
    }

    public void changePassword(final String newPassword) {
        isSamePassword(newPassword);
        this.password = encode(newPassword);
    }

    private void isSamePassword(final String password) {
        if (this.password.equals(encode(password))) {
            throw new AlreadySamePasswordException();
        }
    }

    public void updateAddress(final String newAddress) {
        this.address = newAddress;
    }
}
