package my.painboard.web.model.response;

import lombok.Data;

import static my.painboard.web.security.SecurityConstants.TOKEN_PREFIX;

@Data
public class AuthenticationResponse {
    private String token;
    private String type = TOKEN_PREFIX;
    private String userId;
    private String userName;

    public AuthenticationResponse(String token, String userId, String userName) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
    }
}
