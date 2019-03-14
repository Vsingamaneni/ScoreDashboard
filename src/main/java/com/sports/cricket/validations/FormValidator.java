package com.sports.cricket.validations;

import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Register;
import com.sports.cricket.model.Restrictions;
import com.sports.cricket.model.UserLogin;
import com.sports.cricket.service.RegistrationService;
import com.sports.cricket.validator.EmailValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FormValidator implements Serializable {

    private static final String EMPTY_STRING ="";

    EmailValidator emailValidator = new EmailValidator();

    List<ErrorDetails> errorsList = null;

    public List<ErrorDetails> isLoginValid(UserLogin userLogin){

        errorsList = new ArrayList<>();
        ErrorDetails errorDetails;

        if(null == userLogin){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("userLogin");
            errorDetails.setErrorMessage("User Login details are empty..!!");
            errorsList.add(errorDetails);
        }

        if(null != userLogin){
            if(null == userLogin.getEmail() || userLogin.getEmail().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("email");
                errorDetails.setErrorMessage("Dude, Email Id is required!");
                errorsList.add(errorDetails);
            }else if(null != userLogin.getEmail() && !userLogin.getEmail().isEmpty()){
                if(!emailValidator.valid(userLogin.getEmail())) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("email");
                    errorDetails.setErrorMessage("Invalid Email ID format bruh!");
                    errorsList.add(errorDetails);
                }
            }

            if(null == userLogin.getPassword() || userLogin.getPassword().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("password");
                errorDetails.setErrorMessage("Password dude?");
                errorsList.add(errorDetails);
            }
        }

        return errorsList;
    }

    public List<ErrorDetails> ValidateRegistrationDetails(Register register, Restrictions restrictions){
        errorsList = new ArrayList<>();
        ErrorDetails errorDetails = null;

        if(null == register){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("register");
            errorDetails.setErrorMessage("Register details are empty..!!");
            errorsList.add(errorDetails);
        }

        if(null != register){
            if(null == register.getfName() || register.getfName().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("firstName");
                errorDetails.setErrorMessage("First Name cannot be empty ..!!");
                errorsList.add(errorDetails);
            }

            if(null == register.getlName() || register.getlName().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("lastName");
                errorDetails.setErrorMessage("Last Name cannot be empty ..!!");
                errorsList.add(errorDetails);
            }

            if(null == register.getEmailId() || register.getEmailId().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("emailId");
                errorDetails.setErrorMessage("emailId cannot be empty ..!!");
                errorsList.add(errorDetails);
            }else if(null != register.getEmailId() && ! register.getEmailId().isEmpty()){
                if(!emailValidator.valid(register.getEmailId())) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("email");
                    errorDetails.setErrorMessage("Invalid Email ID format ..!!");
                    errorsList.add(errorDetails);
                }
            }

            if(null == register.getConfirmEmailId() || register.getConfirmEmailId().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("confirmEmailId");
                errorDetails.setErrorMessage("Confirm EmailId cannot be empty ..!!");
                errorsList.add(errorDetails);
            }else if(null != register.getConfirmEmailId() && ! register.getConfirmEmailId().isEmpty()){
                if(!emailValidator.valid(register.getConfirmEmailId())) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("confirmEmail");
                    errorDetails.setErrorMessage("Invalid Confirm Email ID format ..!!");
                    errorsList.add(errorDetails);
                }
            }

            if(null != register && !register.getEmailId().isEmpty() && !register.getConfirmEmailId().isEmpty()){
                if(!register.getEmailId().equalsIgnoreCase(register.getConfirmEmailId())){
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("emailId");
                    errorDetails.setErrorMessage("Both Email and Confirm Email didnt match!!");
                    errorsList.add(errorDetails);
                }
            }

            if(null == register.getPassword() || register.getPassword().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("password");
                errorDetails.setErrorMessage("Password cannot be empty ..!!");
                errorsList.add(errorDetails);
            }

            if(null == register.getConfirmPassword() || register.getConfirmPassword().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("confirmPassword");
                errorDetails.setErrorMessage("Confirm Password cannot be empty ..!!");
                errorsList.add(errorDetails);
            }

            if(null != register && !register.getPassword().isEmpty() && !register.getConfirmPassword().isEmpty()){
                if(!register.getPassword().equalsIgnoreCase(register.getConfirmPassword())){
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("password");
                    errorDetails.setErrorMessage("Both Password and Confirm Password didn't match!!");
                    errorsList.add(errorDetails);
                }
            }

            if(null == register.getSecurityAnswer() || register.getSecurityAnswer().isEmpty()){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("securityAnswer");
                errorDetails.setErrorMessage("Security Answer cannot be empty ..!!");
                errorsList.add(errorDetails);
            }

            if ( null != restrictions){
                if( null != register.getSecurity()) {
                    if (!restrictions.getSecurityCode().equalsIgnoreCase(register.getSecurity())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorField("securityCode");
                        errorDetails.setErrorMessage("Security Code didn't match. Contact admin to get the right code");
                        errorsList.add(errorDetails);
                    }
                } else{
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorField("securityKey");
                    errorDetails.setErrorMessage("Security Key cannot be empty..!");
                    errorsList.add(errorDetails);
                }
            }


        }

        return errorsList;
    }

    public List<ErrorDetails> ValidateUpdatePrediction(Prediction prediction){
        errorsList = new ArrayList<>();
        ErrorDetails errorDetails = null;

        if(null == prediction){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("prediction");
            errorDetails.setErrorMessage("prediction details are empty..!!");
            errorsList.add(errorDetails);
        }

        if(null != prediction){

            if(null != prediction.getSelected() && prediction.getSelected().equalsIgnoreCase("--- SELECT ---")){

                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("Selected");
                errorDetails.setErrorMessage("Select one of the teams !!");
                errorsList.add(errorDetails);
            }

        }

        return errorsList;
    }

    public List<ErrorDetails> isUpdateValid(Register register, RegistrationService registrationService){

        errorsList = new ArrayList<>();
        ErrorDetails errorDetails;

        if (null == register){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("All Fields");
            errorDetails.setErrorMessage("PLease fill the below form");
            errorsList.add(errorDetails);
        }

        if (null != register
                && register.getSecurityQuestion().equalsIgnoreCase("      -- SELECT --     ")){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Security Question");
            errorDetails.setErrorMessage("Please select your security question");
            errorsList.add(errorDetails);
        }


        if (null != register
                && null == register.getSecurityAnswer() || register.getSecurityAnswer().equals(EMPTY_STRING)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Security Answer");
            errorDetails.setErrorMessage("Security Answer cannot be empty..!");
            errorsList.add(errorDetails);
        } else if (null != register
                && null != register.getSecurityAnswer()) {
            Register userDetails = registrationService.getUser(register.getEmailId());
            if (!userDetails.getSecurityAnswer().trim().equalsIgnoreCase(register.getSecurityAnswer().trim())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("Security Answer");
                errorDetails.setErrorMessage("Security Answer didn't match");
                errorsList.add(errorDetails);
            }
        }

        if (null != register
                && null == register.getPassword() || register.getPassword().equals(EMPTY_STRING)){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Password");
            errorDetails.setErrorMessage("Password cannot be empty");
            errorsList.add(errorDetails);
        }

        if (null != register
                && null == register.getConfirmPassword() || register.getConfirmPassword().equals(EMPTY_STRING)){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Confirm Password");
            errorDetails.setErrorMessage("Confirm Password cannot be empty");
            errorsList.add(errorDetails);
        }


        if (null != register
                && null != register.getPassword()
                && null != register.getConfirmPassword()
                && !register.getConfirmPassword().equals(EMPTY_STRING)
                && !register.getPassword().equals(EMPTY_STRING)){
            if (!register.getPassword().trim().equals(register.getConfirmPassword().trim())){
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("Password mismatch");
                errorDetails.setErrorMessage("Password and Confirm Password mismatch");
                errorsList.add(errorDetails);
            }
        }

        return errorsList;
    }

    public List<ErrorDetails> isEmailValid(Register register){

        errorsList = new ArrayList<>();
        ErrorDetails errorDetails;

        if(null == register){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("EmailId");
            errorDetails.setErrorMessage("Please provide the required details in the form..!");
            errorsList.add(errorDetails);
        }

        if (null != register
                && null == register.getEmailId() || register.getEmailId() == ""){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("EmailId");
            errorDetails.setErrorMessage("Email ID cannot be empty..!");
            errorsList.add(errorDetails);
        }

        if (null != register
                && null == register.getConfirmEmailId() || register.getConfirmEmailId() == ""){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Confirm EmailId");
            errorDetails.setErrorMessage("Confirm Email ID cannot be empty..!");
            errorsList.add(errorDetails);
        }

        if (null != register
                && null != register.getConfirmEmailId() && null != register.getEmailId() && register.getEmailId() != "" && register.getConfirmEmailId() != ""){
            if (!register.getEmailId().trim().equalsIgnoreCase(register.getConfirmEmailId().trim())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorField("Email Mismatch");
                errorDetails.setErrorMessage("Email id's you provided didn't match..!");
                errorsList.add(errorDetails);
            }
        }

        return errorsList;
    }
}
