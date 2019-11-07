package pl.piaseckif.ppmtool.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piaseckif.ppmtool.domain.User;
import pl.piaseckif.ppmtool.services.MapValidationErrorService;
import pl.piaseckif.ppmtool.services.UserService;
import pl.piaseckif.ppmtool.validator.UserValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private MapValidationErrorService mapValidationErrorService;
    private UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, MapValidationErrorService mapValidationErrorService, UserValidator userValidator) {
        this.userService = userService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.userValidator = userValidator;
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
}
