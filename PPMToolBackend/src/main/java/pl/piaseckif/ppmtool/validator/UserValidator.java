package pl.piaseckif.ppmtool.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.piaseckif.ppmtool.domain.User;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User)o;
        if (user.getPassword().length()<6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters long");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("password", "Match", "Passwords must match");
        }

    }
}
