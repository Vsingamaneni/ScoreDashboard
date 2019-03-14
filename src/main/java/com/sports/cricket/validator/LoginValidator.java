package com.sports.cricket.validator;

import com.sports.cricket.model.UserLogin;
import com.sports.cricket.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.Serializable;

@Component
public class LoginValidator  implements Validator, Serializable {

    @Autowired
    @Qualifier("emailValidator")
    EmailValidator emailValidator;

    @Autowired
    RegistrationService registrationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLogin.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserLogin user = (UserLogin) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.loginUser.password");


        /*if(!emailValidator.valid(user.getEmail())){
            errors.rejectValue("email", "Enter a valid email");
        }*/


    }
}
