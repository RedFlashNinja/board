package my.painboard.web.controller;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.User;
import my.painboard.db.service.UserService;
import my.painboard.db.service.implementation.UserDetailsImpl;
import my.painboard.web.model.request.SignInRequest;
import my.painboard.web.model.request.SignUpRequest;
import my.painboard.web.model.response.AuthenticationResponse;
import my.painboard.web.model.response.ErrorMessage;
import my.painboard.web.model.response.ErrorMessages;
import my.painboard.web.model.response.MessageResponse;
import my.painboard.web.model.response.NotificationMessages;
import my.painboard.web.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest-auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/signin", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest loginCredentials) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.getUserName(), loginCredentials.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthenticationResponse(jwt,
                userDetails.getUuid(),
                userDetails.getUsername()
        ));
    }

    @PostMapping(path = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.isUserRegistered(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorMessage(new Date(), ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage()));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUserName(), bCryptPasswordEncoder.encode(signUpRequest.getPassword()));

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse(NotificationMessages.USER_SUCCESSFULLY_REGISTERED.getNotificationMessage()));
    }
}
