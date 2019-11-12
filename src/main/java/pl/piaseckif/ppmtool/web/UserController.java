package pl.piaseckif.ppmtool.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piaseckif.ppmtool.domain.User;
import pl.piaseckif.ppmtool.payload.JWTLoginSuccessResponse;
import pl.piaseckif.ppmtool.payload.LoginRequest;
import pl.piaseckif.ppmtool.security.JwtTokenProvider;
import pl.piaseckif.ppmtool.services.MapValidationErrorService;
import pl.piaseckif.ppmtool.services.UserService;
import pl.piaseckif.ppmtool.validator.UserValidator;

import javax.validation.Valid;

import static pl.piaseckif.ppmtool.services.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private MapValidationErrorService mapValidationErrorService;
    private UserValidator userValidator;
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, MapValidationErrorService mapValidationErrorService, UserValidator userValidator, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.userValidator = userValidator;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        user.setConfirmPassword("");


        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
        if (errorMap!=null) {
            return errorMap;
        } else {
            User user1 = userService.saveUser(user);
            return new ResponseEntity<User>(user1, HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) {
            return errorMap;
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = TOKEN_PREFIX+ tokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
        }

    }

}
