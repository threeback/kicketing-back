package tback.kicketingback.user.exception.exceptions;

public class EmailDuplicatedException extends RuntimeException {
    public EmailDuplicatedException() {
        super("이메일이 중복되었습니다.");
    }
}
