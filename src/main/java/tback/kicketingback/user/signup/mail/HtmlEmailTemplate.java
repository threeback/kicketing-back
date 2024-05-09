package tback.kicketingback.user.signup.mail;

import lombok.RequiredArgsConstructor;

class HtmlEmailTemplate {
    private static final String DEFAULT_FORM = """
                <table border='0' cellpadding='0' cellspacing='0' width='100%%>
                    <tr>
                        <td align='center'>
                            <h1> Welcome to Kicketing! </h1>
                            %s
                            <h3> 감사합니다. </h3>
                        </td>
                    </tr>
                </table>
            """;

    @RequiredArgsConstructor
    enum MailType {
        EMAIL_VERIFICATION("회원가입을 위한 이메일 인증", """
                    <h3> 회원가입을 위한 요청하신 인증 번호입니다. </h3><br>
                    <h2> 아래 코드를 회원가입 창으로 돌아가 입력해주세요. </h2>
                    <div align='center' style='border:1px solid black; font-family:verdana;'>
                        <h2> 회원가입 인증 코드입니다. </h2>
                        <h1 style='color:blue'> %s </h1>
                        <br>
                    </div><br>
                """),
        EMAIL_RANDOM_PASSWORD("비밀번호 변경을 위한 임시 비밀번호 안내", """
                    <h3> 무작위 비밀번호를 안내드립니다. </h3><br>
                    <h2> 임시 비밀번호를 변경 후 사용하길 권장드립니다. </h2>
                    <div align='center' style='border:1px solid black; font-family:verdana;'>
                        <h2> 무작위 임시 비밀번호입니다. </h2>
                        <h1 style='color:blue'> %s </h1>
                        <br>
                    </div><br>
                """);

        private final String subject;
        private final String content;

        public String subject() {
            if (this.equals(EMAIL_VERIFICATION)) {
                return subject;
            }
            if (this.equals(EMAIL_RANDOM_PASSWORD)) {
                return subject;
            }
            throw new UnsupportedOperationException("지원하지 않는 메일 타입입니다.");
        }

        public String content(String certification) {
            if (this.equals(EMAIL_VERIFICATION)) {
                return wrapContent(content.formatted(certification));
            }
            if (this.equals(EMAIL_RANDOM_PASSWORD)) {
                return wrapContent(content.formatted(certification));
            }
            throw new UnsupportedOperationException("지원하지 않는 메일 타입입니다.");
        }

        private String wrapContent(String content) {
            return DEFAULT_FORM.formatted(content);
        }
    }
}
