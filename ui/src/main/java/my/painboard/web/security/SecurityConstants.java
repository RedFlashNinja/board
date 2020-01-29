package my.painboard.web.security;

import java.util.Date;

public class SecurityConstants {
    public static final Date EXPIRATION_TIME = new Date((new Date()).getTime() + 864000000); // 10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWTSECRET = "JWTSECRETKEY";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_URL = "/rest-auth/**";
    public static final String SIGN_UP_URL = "/rest-auth/signup";
    public static final String SIGN_IN_URL = "/rest-auth/signin";
    public static final String VERIFICATION_EMAIL_URL = "/rest-auth/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/rest-auth/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/rest-auth/password-reset";
    public static final String H2_CONSOLE = "/h2-console/**";

}
