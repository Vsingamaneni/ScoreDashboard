package com.sports.cricket.web;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.sports.cricket.model.*;
import com.sports.cricket.service.RegistrationService;
import com.sports.cricket.service.ScheduleService;
import com.sports.cricket.util.*;
import com.sports.cricket.validations.ErrorDetails;
import com.sports.cricket.validations.FormValidator;
import com.sports.cricket.validations.ResultValidator;
import com.sports.cricket.validations.ValidateDeadLine;
import com.sports.cricket.validator.LoginValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.cricket.service.UserService;
import com.sports.cricket.validator.UserFormValidator;

import static com.sports.cricket.util.PredictionListMapper.getActiveMatchDay;
import static com.sports.cricket.util.PredictionListMapper.getAdminId;

@Controller
@ControllerAdvice
public class UserController implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserFormValidator userFormValidator;

    @Autowired
    LoginValidator loginValidator;

    FormValidator formValidator = new FormValidator();

    private ScheduleService scheduleService;
    private RegistrationService registrationService;

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Show index page
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, HttpSession httpSession) {
        return "redirect:/index";
    }

    // Show index page
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showHomePage(Model model, HttpSession httpSession) {
        if (null != httpSession.getAttribute("msg")) {
            model.addAttribute("msg", httpSession.getAttribute("msg"));
            httpSession.removeAttribute("msg");
        }
        if (null != httpSession
                && null == httpSession.getAttribute("session")) {
            UserLogin userLogin = new UserLogin();
            logger.debug("Login Page");
            List<ErrorDetails> loginErrorDetails = (List<ErrorDetails>) httpSession.getAttribute("loginErrorDetails");
            if (null != loginErrorDetails
                    && loginErrorDetails.size() > 0) {
                model.addAttribute("loginErrorDetails", loginErrorDetails);
                httpSession.removeAttribute("loginErrorDetails");
            }
            model.addAttribute("userLogin", userLogin);
            return "users/index";
        } else if (null != httpSession.getAttribute("session")) {
            UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
            Register register;
            if (null != userLogin.getIsAdminActivated()
                    && userLogin.getIsAdminActivated().equalsIgnoreCase("N")) {
                register = registrationService.getUser(userLogin.getEmail());

                userLogin.setIsActive(register.getIsActive());
                userLogin.setIsAdminActivated(register.getIsAdminActivated());
            }

            if (userLogin.getIsAdminActivated().equalsIgnoreCase("N")) {
                return "users/contact_admin";
            } else {
                return "redirect:/profile";
            }
        }
        return "redirect:/profile";
    }

    // Validate the login details
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("userLogin") UserLogin userLogin, ModelMap model
            , HttpSession httpSession) {

        if (null != httpSession.getAttribute("loginErrorDetails")) {
            httpSession.removeAttribute("loginErrorDetails");
        }

        if (null != model.get("loginErrorDetails")) {
            model.remove("loginErrorDetails");
        }

        List<ErrorDetails> loginErrorDetails = formValidator.isLoginValid(userLogin);

        if (null != loginErrorDetails
                && loginErrorDetails.size() > 0) {
            model.addAttribute("loginErrorDetails", loginErrorDetails);
            httpSession.setAttribute("loginErrorDetails", loginErrorDetails);
            return "redirect:/";
        }

        UserLogin loginDetails = registrationService.loginUser(userLogin);

        if (null != loginDetails
                && loginDetails.isLoginSuccess()) {

            model.addAttribute("session", loginDetails);
            httpSession.setAttribute("user", loginDetails.getFirstName());
            httpSession.setAttribute("userLastName", loginDetails.getLastName());
            httpSession.setAttribute("role", loginDetails.getRole());
            httpSession.setAttribute("session", userLogin);

            if (loginDetails.getIsAdminActivated().equalsIgnoreCase("N")) {
                httpSession.setAttribute("msg", "Please contact the admin to activate your account..!");
                return "users/contact_admin";
            }
            return "redirect:/profile";
        } else {
            List<ErrorDetails> errorDetailsList = new ArrayList<>();
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Password");
            errorDetails.setErrorMessage("Invalid email or password");
            errorDetailsList.add(errorDetails);
            httpSession.setAttribute("loginErrorDetails", errorDetailsList);
            return "redirect:/";
        }
    }

    // Show user profile page
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            httpSession.setMaxInactiveInterval(10 * 60);
            List<Standings> standingsList = scheduleService.getLeaderBoard();
            List<Restrictions> restrictions = registrationService.getRestrictions();

            userLogin.setLimitReached(LeaderBoardDetails.isLimitReached(standingsList, userLogin.getMemberId(), restrictions.get(0).getMaxLimit()));

            userLogin = UserListMapper.setLoginStatus(userLogin);

            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);
            model.addAttribute("session", userLogin);
            return "users/account";
        }
    }

    // Show user profile page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            Register register;
            if (null != userLogin.getIsAdminActivated()
                    && userLogin.getIsAdminActivated().equalsIgnoreCase("N")) {
                register = registrationService.getUser(userLogin.getEmail());

                userLogin.setIsActive(register.getIsActive());
                userLogin.setIsAdminActivated(register.getIsAdminActivated());
            }

            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);

            List<Schedule> scheduleList = scheduleService.scheduleList();
            int matchDay = ScheduleValidation.getMatchDay(scheduleList);
            model.addAttribute("matchDay", matchDay);

            List<Prediction> predictionList = scheduleService.getPredictionsByMatchDay(matchDay);
            List<MatchDetails> matchDetailsList = PredictionListMapper.matchDetails(predictionList);
            model.addAttribute("matchDetailsList", matchDetailsList);

            List<MatchDetails> matchDetails = PredictionListMapper.getNewsFeed(scheduleList, matchDay);
            model.addAttribute("newsFeed", matchDetails);

            List<Register> registerList = registrationService.getAllUsers();
            List<MatchDetails> userDetails = UserListMapper.getUsersList(registerList);
            model.addAttribute("userActiveDetails", userDetails);

            List<MatchDetails> usersList = UserListMapper.getGeoDetails(registerList);
            model.addAttribute("geoDetails", usersList);

            model.addAttribute("session", userLogin);
            model.addAttribute("userLogin", userLogin);

            httpSession.setMaxInactiveInterval(10 * 60);
            return "users/welcome";
        }
    }

    // Show All Users
    @RequestMapping(value = "/showAllUsers", method = RequestMethod.GET)
    public String showAllUsers(Model model, HttpSession httpSession) {

        logger.debug("showAllUsers()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        }

        model.addAttribute("session", userLogin);
        model.addAttribute("login", userLogin);
        List<Register> registerList = registrationService.getAllUsers();
        model.addAttribute("registerList", registerList);

        return "users/members";
    }

    // Validate the registration details
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("registerForm") Register register, ModelMap model, HttpSession httpSession, final RedirectAttributes redirectAttributes) {

        logger.debug("Register User()");

        List<Restrictions> restrictionsList = registrationService.getRestrictions();
        List<ErrorDetails> registerErrorDetails = formValidator.ValidateRegistrationDetails(register, restrictionsList.get(0));

        if (null != registerErrorDetails
                && registerErrorDetails.size() > 0) {
            model.addAttribute("registerErrorDetails", registerErrorDetails);
            httpSession.setAttribute("registerErrorDetails", registerErrorDetails);
            return "redirect:/register";
        }

        boolean success = registrationService.registerUser(register);

        if (success) {
            redirectAttributes.addFlashAttribute("msg", "Hurray, You are In!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("msg", "OOOPSS!! Registration Failed!");
            return "redirect:/register";
        }
    }

    // Show Register page
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String register(ModelMap model, HttpSession httpSession) {

        List<ErrorDetails> registerErrorDetails = (List<ErrorDetails>) httpSession.getAttribute("registerErrorDetails");

        if (null != registerErrorDetails
                && registerErrorDetails.size() > 0) {
            model.addAttribute("registerErrorDetails", registerErrorDetails);
            httpSession.removeAttribute("registerErrorDetails");
        }

        Register register = new Register();

        logger.debug("Register User()");
        model.addAttribute("registerForm", register);
        return "users/register";
    }

    // Display predictions
    @RequestMapping(value = "/member/{memberId}/authorize", method = RequestMethod.GET)
    public String authorizeMember(@PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            boolean isAuthSuccess = scheduleService.authorizeMember(memberId);

            model.addAttribute("session", userLogin);
            model.addAttribute("isAuthSuccess", isAuthSuccess);
            if (isAuthSuccess) {
                userLogin.setIsAdminActivated("Y");
                userLogin.setIsActive("Y");
            }
            httpSession.setAttribute("session", userLogin);

            return "redirect:/showAllUsers";
        }
    }

    // Display predictions
    @RequestMapping(value = "/member/{memberId}/deactivate", method = RequestMethod.GET)
    public String deactivateMember(@PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        UserLogin user = (UserLogin) httpSession.getAttribute("session");

        if (null == user) {
            return "redirect:/";
        } else {
            boolean isAuthSuccess = scheduleService.deactivateMember(memberId);

            model.addAttribute("session", user);
            if (isAuthSuccess) {
                user.setIsActive("N");
            }
            httpSession.setAttribute("session", user);

            return "redirect:/showAllUsers";
        }
    }

    // Forget Password
    @RequestMapping(value = "/forget", method = {RequestMethod.GET, RequestMethod.POST})
    public String forgetPassword(Model model, HttpSession httpSession) {

        if (null != httpSession.getAttribute("errorDetailsList")) {
            List<ErrorDetails> errorDetailsList = (List<ErrorDetails>) httpSession.getAttribute("errorDetailsList");
            model.addAttribute("errorDetailsList", errorDetailsList);
            httpSession.removeAttribute("errorDetailsList");
            return "users/forget";
        }

        Register register = new Register();

        logger.debug("forgetPassword()");
        model.addAttribute("registerForm", register);

        if (null == httpSession.getAttribute("session")) {
            return "users/forget";
        } else {
            return "redirect:/resetPassword";
        }
    }

    // Rest Password
    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST})
    public String resetPassword(@ModelAttribute("registerForm") Register register, Model model, HttpSession httpSession) {

        logger.debug("resetPassword()");
        Register userDetails;

        List<ErrorDetails> errorDetailsList = formValidator.isEmailValid(register);

        if (errorDetailsList.size() > 0) {
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/forget";
        } else {
            userDetails = registrationService.getUser(register.getEmailId());
        }

        if (null == userDetails) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage("Invalid Email ID!");
            errorDetails.setErrorField("emailID");
            errorDetailsList.add(errorDetails);
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/forget";
        }

        logger.debug("forgetPassword()");
        model.addAttribute("registerForm", register);
        model.addAttribute("userDetails", userDetails);

        if (null == httpSession.getAttribute("session")) {
            return "users/reset";
        } else {
            return "redirect:/";
        }
    }

    // Update Password
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(@ModelAttribute("registerForm") Register register, Model model, HttpSession httpSession) {

        logger.debug("inside updatePassword()");
        boolean isUpdateSuccess = false;

        List<ErrorDetails> errorDetailsList = formValidator.isUpdateValid(register, registrationService);

        if (errorDetailsList.size() > 0) {
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/forget";
        }

        if (null != register.getEmailId()) {
            isUpdateSuccess = registrationService.updateUser(register);
        }

        logger.debug("forgetPassword()");
        model.addAttribute("registerForm", register);
        model.addAttribute("isUpdateSuccess", isUpdateSuccess);
        model.addAttribute("msg", "You have successfully Updated your password ..!!");
        httpSession.setAttribute("msg", "You have successfully Updated your password ..!!");

        if (null == httpSession.getAttribute("session")) {
            return "redirect:/";
        } else {
            return "redirect:/predictions";
        }
    }

    // Show Register page
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String schedule(ModelMap model, HttpSession httpSession) throws ParseException {
        logger.debug("schedule()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            model.addAttribute("userLogin", userLogin);

            /*if (userLogin.getIsActive().equalsIgnoreCase("N")) {
                httpSession.setAttribute("msg", "You Need to be active to predict for matches");
                return "users/contact_admin";
            }*/

            List<Schedule> schedules = ValidatePredictions.validateSchedule(scheduleService.scheduleList());
            schedules = ScheduleListMapper.mapScheduleStauts(schedules);

            model.addAttribute("schedules", schedules);

            httpSession.setMaxInactiveInterval(5 * 60);

            return "users/schedule";
        }
    }

    // Display predictions
    @RequestMapping(value = "/predictions", method = RequestMethod.GET)
    public String showPredictions(ModelMap model, HttpSession httpSession) throws ParseException {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");


        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());

            userLogin.setIsActive(register.getIsActive());

            if (null != httpSession.getAttribute("msg")) {
                model.addAttribute("msg", httpSession.getAttribute("msg"));
                httpSession.removeAttribute("msg");
            }

            List<Schedule> schedules = ValidatePredictions.validateSchedule(scheduleService.scheduleList());
            List<Prediction> predictions = scheduleService.findPredictions(userLogin.getMemberId());
            schedules = ValidatePredictions.isScheduleAfterRegistration(schedules, register.getRegisteredTime());
            List<Schedule> finalSchedule = ValidatePredictions.validatePrediction(schedules, predictions);
            predictions = ValidateDeadLine.mapScheduleToPredictions(schedules, predictions);
            int matchDay = ScheduleValidation.getMatchDay(schedules);
            predictions = ScheduleValidation.setCurrentMatchDayPredictions(predictions, matchDay);

            model.addAttribute("predictions", predictions);
            model.addAttribute("schedules", finalSchedule);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/predictions";
        }
    }

    // Take review
    @RequestMapping(value = "/userReview", method = RequestMethod.GET)
    public String userReview(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());
            userLogin.setIsActive(register.getIsActive());

            Review review = registrationService.getReview(userLogin.getMemberId());

            if (null != review) {
                httpSession.setAttribute("review", review);
                return "redirect:/review";
            }

            UserReview userReview = new UserReview();

            model.addAttribute("userReview", userReview);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/userreview";
        }
    }

    // Display predictions
    @RequestMapping(value = "/saveReview", method = RequestMethod.POST)
    public String saveReview(@ModelAttribute("userReview") UserReview userRating, ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            Review review = new Review();

            if (null != userRating) {
                review.setMemberId(userLogin.getMemberId());
                review.setFeedback(userRating.getReviewRating());
                review.setInterested(userRating.getInterested());
                review.setImprovements(userRating.getImprove());
                review.setIdeas(userRating.getIdeas());
                review.setName(userLogin.getFirstName() + " " + userLogin.getLastName());
            }

            boolean isReviewSaved = registrationService.saveReview(review);

            if (isReviewSaved) {
                return "redirect:/review";
            }

            //model.addAttribute("rating", rating);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/review";
        }
    }

    // Display predictions
    @RequestMapping(value = "/showReviews", method = RequestMethod.GET)
    public String showReview(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            if (!userLogin.getRole().equalsIgnoreCase("admin")) {
                return "redirect:/";
            }

            List<Review> reviewList = registrationService.getAllReviews();
            Review interestedCount = ReviewListMapper.countInterested(reviewList);
            ReviewListMapper.setPreview(reviewList);

            model.addAttribute("interestedCount", interestedCount);
            model.addAttribute("reviewList", reviewList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/showreviews";
        }
    }

    // Display predictions
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public String doReview(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            Review review = (Review) httpSession.getAttribute("review");

            List<Review> reviewList = registrationService.getAllReviews();
            Review overAllReview = ReviewListMapper.mapReviews(reviewList);

            model.addAttribute("rating", review);
            model.addAttribute("overAllReview", overAllReview);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/review";
        }
    }

    // show update form
    @RequestMapping(value = "/match/{memberId}/{matchNumber}/predict", method = RequestMethod.GET)
    public String predictMatch(@PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) throws ParseException {

        logger.debug("predictMatch() : {}", memberId, matchNumber);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {

            if (null != httpSession.getAttribute("errorDetailsList")) {
                List<ErrorDetails> errorDetailsList = (List<ErrorDetails>) httpSession.getAttribute("errorDetailsList");
                model.addAttribute("errorDetailsList", errorDetailsList);
                httpSession.removeAttribute("errorDetailsList");
            }

            Schedule schedule = scheduleService.findById(matchNumber);
            if (ValidateDeadline.isDeadLineReached(schedule.getStartDate())) {
                httpSession.setAttribute("msg", "Prediction deadline is reached, cannot predict/update");
                return "redirect:/predictions";
            }

            PredictionListMapper.setMatchFeeList(schedule);

            Prediction prediction = new Prediction();

            model.addAttribute("scheduleForm", schedule);
            model.addAttribute("predictionForm", prediction);
            model.addAttribute("session", userLogin);

            return "users/user_prediction";
        }
    }

    // Save Prediction details
    @RequestMapping(value = "/match/{memberId}/save", method = RequestMethod.POST)
    public String savePrediction(@ModelAttribute("predictionForm") Prediction prediction, @PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        logger.debug("savePrediction() : {}", memberId, memberId);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {

            List<ErrorDetails> errorDetailsList = ResultValidator.isValid(prediction);
            if (errorDetailsList.size() > 0) {
                httpSession.setAttribute("errorDetailsList", errorDetailsList);
                return "redirect:/match/" + prediction.getMemberId() + "/" + prediction.getMatchNumber() + "/predict";
            }

            boolean savePrediction = scheduleService.savePrediction(prediction);

            //Prediction prediction = new Prediction();

            //model.addAttribute("scheduleForm", schedule);
            model.addAttribute("isPredictionSuccess", savePrediction);
            model.addAttribute("session", httpSession.getAttribute("session"));
            model.addAttribute("msg", "Your Prediction for " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is Saved Successfully!!");
            httpSession.setAttribute("msg", "Your Prediction for " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is Saved Successfully!!");

            return "redirect:/predictions";
        }
    }

    // Update prediction form
    @RequestMapping(value = "/prediction/{memberId}/{matchNumber}/update", method = RequestMethod.GET)
    public String updatePrediction(@PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) throws ParseException {

        logger.debug("updatePrediction() : {}", memberId, matchNumber);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null != httpSession.getAttribute("errorDetailsList")) {
            List<ErrorDetails> errorDetailsList = (List<ErrorDetails>) httpSession.getAttribute("errorDetailsList");
            httpSession.removeAttribute("errorDetailsList");
            model.addAttribute("errorDetailsList", errorDetailsList);
        }

        if (null == userLogin) {
            return "redirect:/";
        }

        Schedule schedule = scheduleService.findById(matchNumber);
        if (ValidateDeadline.isDeadLineReached(schedule.getStartDate())) {
            httpSession.setAttribute("msg", "Prediction deadline is reached, cannot predict/update");
            return "redirect:/predictions";
        }
        Prediction prediction = scheduleService.getPrediction(memberId, matchNumber);

        PredictionListMapper.setMatchFeeList(schedule);

        model.addAttribute("scheduleForm", schedule);
        model.addAttribute("predictionForm", prediction);
        model.addAttribute("session", userLogin);

        return "users/update";

    }

    // Update prediction Details
    @RequestMapping(value = "/match/{memberId}/{matchNumber}/save", method = RequestMethod.POST)
    public String saveUpdatedPrediction(@ModelAttribute("predictionForm") Prediction prediction, @PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) {

        logger.debug("saveUpdatedPrediction() : {}", memberId, matchNumber);

        List<ErrorDetails> errorDetailsList = formValidator.ValidateUpdatePrediction(prediction);

        if (null != errorDetailsList
                && errorDetailsList.size() > 0) {
            model.addAttribute("errorDetailsList", errorDetailsList);
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/prediction/{memberId}/{matchNumber}/update";
        }

        boolean savePrediction = scheduleService.updatePrediction(prediction);

        model.addAttribute("isPredictionSuccess", savePrediction);
        model.addAttribute("session", httpSession.getAttribute("session"));

        if (savePrediction) {
            httpSession.setAttribute("msg", "Prediction for " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is updated successfully");
        }

        return "redirect:/predictions";
    }

    // show delete form
    @RequestMapping(value = "/prediction/{predictionID}/delete", method = RequestMethod.GET)
    public String showDeletePrediction(@PathVariable("predictionID") Integer predictionId, Model model, HttpSession httpSession) {

        logger.debug("show Delete Form() : {}", predictionId);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {

            Prediction prediction = scheduleService.getPrediction(predictionId);

            model.addAttribute("session", userLogin);
            model.addAttribute("predictionForm", prediction);

            return "users/confirm_delete";
        }
    }

    // confirm delete form
    @RequestMapping(value = "/prediction/{predictionId}/delete", method = RequestMethod.POST)
    public String deletePrediction(@PathVariable("predictionId") Integer predictionId, Model model, HttpSession httpSession) {

        logger.debug("delete Prediction() : {}", predictionId);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            boolean isDeleteSuccess = scheduleService.deletePrediction(predictionId);

            model.addAttribute("isDeleteSuccess", isDeleteSuccess);
            model.addAttribute("session", httpSession.getAttribute("session"));
            httpSession.setAttribute("msg", "Prediction deleted successfully ..!! ");

            return "redirect:/predictions";
        }
    }

    // Save Result
    @RequestMapping(value = "/saveResult", method = RequestMethod.GET)
    public String saveResult(ModelMap model, HttpSession httpSession) {

        logger.debug("saveResult() : {}");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        if (null == userLogin) {
            return "redirect:/";
        } else {

            if (null != model.get("msg")) {
                model.remove("msg");
            }

            String value = (String) httpSession.getAttribute("msg");
            if (null != value) {
                model.addAttribute("msg", value);
                httpSession.removeAttribute("msg");
            }
            Schedule schedule = new Schedule();
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);
            model.addAttribute("schedule", schedule);
            model.addAttribute("session", userLogin);

            if (userLogin.getRole().equalsIgnoreCase("admin")) {

                List<Schedule> schedules = scheduleService.findAll();

                model.addAttribute("schedules", schedules);
                if (null != httpSession.getAttribute("errorDetailsList")) {
                    List<ErrorDetails> errorDetailsList = (List<ErrorDetails>) httpSession.getAttribute("errorDetailsList");
                    model.addAttribute("errorDetailsList", errorDetailsList);
                    httpSession.removeAttribute("errorDetailsList");
                }

                httpSession.setMaxInactiveInterval(5 * 60);
                return "users/updateResult";
            } else {
                return "redirect:/predictions";
            }
        }
    }

    @RequestMapping(value = "/matchResult/update", method = RequestMethod.POST)
    public String updateMatchResult(@ModelAttribute("schedule") Schedule schedule, ModelMap model, HttpSession httpSession) throws ParseException {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else if (!userLogin.getRole().equalsIgnoreCase("admin")) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            String value = (String) httpSession.getAttribute("msg");

            if (null != value) {
                model.addAttribute("msg", value);
            }

            httpSession.removeAttribute("msg");
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("session", userLogin);
            httpSession.setAttribute("role", userLogin.getRole());
            model.addAttribute("session", userLogin);

            boolean isUpdateSuccess = false;
            boolean isUpdateResultSuccess = false;

            if (null != schedule
                    && null != schedule.getWinner()) {
                List<ErrorDetails> errorDetailsList = ResultValidator.isMatchResultValid(schedule);
                if (errorDetailsList.size() > 0) {
                    httpSession.setAttribute("errorDetailsList", errorDetailsList);
                    return "redirect:/saveResult";
                }
                Integer totalMatches = scheduleService.totalMatches(schedule.getMatchDay());
                isUpdateSuccess = scheduleService.updateMatchResult(schedule);
                SchedulePrediction schedulePrediction = MatchUpdates.setUpdates(schedule, scheduleService, registrationService);
                Result result = MatchUpdates.mapResult(schedule, schedulePrediction);

                isUpdateResultSuccess = scheduleService.addResult(result);

                List<Standings> standingsList = MatchUpdates.updateStandings(schedulePrediction);

                scheduleService.insertPredictions(standingsList);

                if (totalMatches == 1) {
                    scheduleService.updateMatchDay(schedule.getMatchDay() + 1);
                }
            }

            if (isUpdateSuccess && isUpdateResultSuccess) {
                httpSession.setAttribute("msg", "Match result and standings are updated successfully ..!!");
            }

            httpSession.setMaxInactiveInterval(5 * 60);
            return "redirect:/saveResult";
        }
    }

    // Show Current Predictions
    @RequestMapping(value = "/currentPredictions", method = RequestMethod.GET)
    public String showCurrentPredictions(Model model, HttpSession httpSession) throws ParseException {

        logger.debug("showCurrentPredictions()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null == userLogin) {
            return "redirect:/";
        }

        model.addAttribute("session", userLogin);
        model.addAttribute("login", userLogin);
        model.addAttribute("userLogin", userLogin);

        List<Schedule> currentSchedule = ValidatePredictions.validateSchedule(scheduleService.findAll());
        boolean isScheduleDone = false;

        List<SchedulePrediction> schedulePredictionsList = new ArrayList<>();
        for (Schedule schedule : currentSchedule) {
            if (ValidateDeadline.isDeadLineReached(schedule.getStartDate()) || userLogin.getRole().equalsIgnoreCase("admin")) {
                SchedulePrediction matchDetails = MatchUpdates.setUpdates(schedule, scheduleService, registrationService);
                ValidateDeadLine.isUpdatePossible(matchDetails.getSchedule(), matchDetails.getPrediction());
                //List<Prediction> predictionList = PredictionListMapper.sortPredictions(matchDetails.getPrediction());

                List<Prediction> predictionList = PredictionListMapper.mapPredictionPerSelection(matchDetails.getPrediction(), schedule);

                matchDetails.setPrediction(predictionList);
                PredictionListMapper.mapCountTotals(matchDetails);
                schedulePredictionsList.add(matchDetails);
                isScheduleDone = true;
            }
        }

        if (!isScheduleDone) {
            int memberId = getAdminId(registrationService);

            int matchday = getActiveMatchDay(scheduleService);

            List<Prediction> adminPredictions = PredictionListMapper.adminPredictions(scheduleService, memberId, matchday);

            model.addAttribute("adminPredictions", adminPredictions);
            model.addAttribute("deadLineSchedule", currentSchedule);
        }

        model.addAttribute("schedulePredictions", schedulePredictionsList);

        Prediction prediction = PredictionListMapper.getUserPredictions(schedulePredictionsList, userLogin);
        if (null != prediction){
            model.addAttribute("userPrediction", prediction);
        }

        return "users/currentPredictions";
    }

    // Display Settlement
    @RequestMapping(value = "/standings", method = RequestMethod.GET)
    public String standings(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);

            String value = (String) httpSession.getAttribute("msg");

            if (null != value) {
                model.addAttribute("msg", value);
            }
            httpSession.removeAttribute("msg");
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());

            List<Register> registerList = registrationService.getAllUsers();
            List<Standings> standingsList = scheduleService.getLeaderBoard();

            List<LeaderBoard> leaderBoardList = LeaderBoardDetails.mapLeaderBoard(standingsList, registerList);

            model.addAttribute("leaderBoardList", leaderBoardList);
            LeaderBoard leaderBoard = LeaderBoardDetails.getCurrentUserStandings(leaderBoardList, register.getMemberId());
            model.addAttribute("leader", leaderBoard);

            if (null != userLogin.getRole()
                    && userLogin.getRole().equalsIgnoreCase("admin")) {
                List<Result> resultList = scheduleService.getResults();
                model.addAttribute("resultsList", resultList);
            }

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/leaderboard";
        }
    }

    // Add Settlement
    @RequestMapping(value = "/addSettlement", method = RequestMethod.GET)
    public String addSettlement(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            TrackSettlement trackSettlement = new TrackSettlement();

            List<Settlement> settlementDetails = scheduleService.getSettlement();
            LeaderBoardDetails.sortSettlement(settlementDetails);

            model.addAttribute("settlementDetails", settlementDetails);
            model.addAttribute("trackSettlement", trackSettlement);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/addSettlement";
        }
    }

    // Save Settlement
    @RequestMapping(value = "/saveSettlement", method = RequestMethod.POST)
    public String saveSettlement(@ModelAttribute("trackSettlement") TrackSettlement trackSettlement, ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            List<Register> allUsers = registrationService.getAllUsers();

            trackSettlement = SettlementUtil.parseSettlementDetails(trackSettlement, allUsers);

            Settlement toSettlement = scheduleService.getSettlement(trackSettlement.getMemberId());
            Settlement fromSettlement = scheduleService.getSettlement(trackSettlement.getSettledMemberId());
            List<Settlement> settlementList = SettlementUtil.mapSettlement(trackSettlement, toSettlement, fromSettlement);

            scheduleService.updateSettlement(settlementList);
            scheduleService.addSettlement(trackSettlement);

            httpSession.setAttribute("msg", "Settlement Added Successfully !!");
            httpSession.setMaxInactiveInterval(5 * 60);

            return "redirect:/settlement";
        }
    }

    // Display Settlement
    @RequestMapping(value = "/settlement", method = RequestMethod.GET)
    public String settlement(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            String value = (String) httpSession.getAttribute("msg");
            if (null != value) {
                model.addAttribute("msg", value);
                httpSession.removeAttribute("msg");
            }

            List<Settlement> settlementDetails = scheduleService.getSettlement();
            LeaderBoardDetails.sortSettlement(settlementDetails);
            SettlementUtil.setStatus(settlementDetails);

            Settlement memberSettlement = LeaderBoardDetails.getMemberSettlement(settlementDetails, userLogin);
            settlementDetails = SettlementUtil.pickNonSettled(settlementDetails);
            SettlementUtil.setNumbers(settlementDetails);

            List<Settlement> settlementList = new ArrayList<>();
            if (!userLogin.getRole().equals("admin")){
                for (Settlement settlement : settlementDetails){
                    if (settlement.getMemberId() != 1){
                        settlementList.add(settlement);
                    }
                }
            } else {
                settlementList = settlementDetails;
            }

            model.addAttribute("memberSettlement", memberSettlement);
            model.addAttribute("settlementDetails", settlementList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/settlement";
        }
    }

    // Settlement Details
    @RequestMapping(value = "/displaySettlement", method = RequestMethod.GET)
    public String displaySettlement(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            httpSession.setAttribute("session", userLogin);

            List<TrackSettlement> displaySettlements = scheduleService.getSettlementsTrack();

            List<TrackSettlement> mySettlementHistory = SettlementUtil.mySettlementHistory(displaySettlements, userLogin);

            model.addAttribute("displaySettlements", displaySettlements);
            model.addAttribute("mySettlementHistory", mySettlementHistory);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/display";
        }
    }

    // Stats
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String statistics(ModelMap model, HttpSession httpSession) {

        UserLogin login = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == login) {
            return "redirect:/";
        } else {
            model.addAttribute("session", login);
            httpSession.setAttribute("session", login);

            Register register = registrationService.getUser(login.getEmail());

            List<Register> registerList = registrationService.getAllUsers();
            List<Standings> standingsList = scheduleService.getLeaderBoard();

            // get the Users based on the default count
            List<StatsDetails> defaultLists = StatisticsDetails.getDefaults(standingsList, registerList);
            model.addAttribute("defaultLists", defaultLists);

            List<StatsDetails> winAndLossCount = StatisticsDetails.getWinsAndLosses(standingsList, registerList);
            StatisticsDetails.pickTopTenWinningCount(winAndLossCount);
            if (winAndLossCount.size() > 10) {
                model.addAttribute("winAndLossCount", winAndLossCount.subList(0, 10));
            } else {
                model.addAttribute("winAndLossCount", winAndLossCount);
            }

            List<StatsDetails> lossDetails = new ArrayList<>(winAndLossCount);

            StatisticsDetails.pickTopTenLoosingCount(lossDetails);
            if (lossDetails.size() > 10) {
                model.addAttribute("lossDetails", lossDetails.subList(0, 10));
            } else {
                model.addAttribute("lossDetails", lossDetails);
            }

            List<StatsDetails> winAndLossAmounts = StatisticsDetails.getHighestWinning(standingsList, registerList);
            StatisticsDetails.pickTopTenWonAmounts(winAndLossAmounts);
            if (winAndLossAmounts.size() > 10) {
                model.addAttribute("winAndLossAmounts", winAndLossAmounts.subList(0, 10));
            } else {
                model.addAttribute("winAndLossAmounts", winAndLossAmounts);
            }

            StatsDetails userStats = StatisticsDetails.getIndividualStats(standingsList, register);
            model.addAttribute("userStats", userStats);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/stats";
        }
    }

    // Display predictions
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String showResults(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            //model.addAttribute("msg", "User logged in");
            String value = (String) httpSession.getAttribute("msg");
            if (null != value) {
                model.addAttribute("msg", value);
            }
            httpSession.removeAttribute("msg");
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());

            List<Standings> standingsList = LeaderBoardDetails.getStandings(scheduleService.getLeaderBoard(), register.getMemberId());
            standingsList = MatchUpdates.mapStandings(standingsList);

            standingsList = LeaderBoardDetails.setResults(standingsList);
            model.addAttribute("standingsList", standingsList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/history";
        }
    }

    // Display predictions
    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String showHistory(ModelMap model, HttpSession httpSession) {
        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        if (null != model.get("msg")) {
            model.remove("msg");
        }

        if (null == userLogin) {
            return "redirect:/";
        } else {
            model.addAttribute("session", userLogin);
            //model.addAttribute("msg", "User logged in");
            String value = (String) httpSession.getAttribute("msg");
            if (null != value) {
                model.addAttribute("msg", value);
            }
            httpSession.removeAttribute("msg");
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());

            if (!register.getRole().equals("admin")) {
                return "redirect:/";
            }

            List<Result> resultList = scheduleService.getResults();
            SettlementUtil.mapResults(resultList);

            model.addAttribute("resultList", resultList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/results";
        }
    }

    // Show Rules
    @RequestMapping(value = "/rules", method = RequestMethod.GET)
    public String showRules(ModelMap model, HttpSession httpSession) {
        logger.debug("show Rules");
        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
        model.addAttribute("session", userLogin);
        httpSession.setAttribute("session", userLogin);
        httpSession.setMaxInactiveInterval(5 * 60);
        return "users/rules";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logoutUser(Model model, HttpSession httpSession) {
        logger.debug("logoutUser()");
        httpSession.invalidate();
        return "redirect:/";
    }

}