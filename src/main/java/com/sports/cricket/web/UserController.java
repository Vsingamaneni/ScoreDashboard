package com.sports.cricket.web;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sports.cricket.model.*;
import com.sports.cricket.service.RegistrationService;
import com.sports.cricket.service.ScheduleService;
import com.sports.cricket.util.*;
import com.sports.cricket.validations.ErrorDetails;
import com.sports.cricket.validations.FormValidator;
import com.sports.cricket.validations.ValidateDeadLine;
import com.sports.cricket.validator.LoginValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.cricket.service.UserService;
import com.sports.cricket.validator.UserFormValidator;

@Controller
@ControllerAdvice
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserFormValidator userFormValidator;

    @Autowired
    LoginValidator loginValidator;

    FormValidator formValidator = new FormValidator();

    MatchUpdates matchUpdates = new MatchUpdates();

    private UserService userService;
    private ScheduleService scheduleService;
    private RegistrationService registrationService;

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Show index page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, HttpSession httpSession) {
        return "redirect:/index";
    }

    // Show index page
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showHomePage(Model model, HttpSession httpSession) {
        if (null != httpSession
                && null == httpSession.getAttribute("session")) {
            UserLogin userLogin = new UserLogin();
            logger.debug("Login Page");
            model.addAttribute("userLogin", userLogin);
            return "users/index";
        } else if (null != httpSession
                && null != httpSession.getAttribute("session")) {
            UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");
            if (userLogin.getIsAdminActivated().equalsIgnoreCase("N")) {
                return "users/contact_admin";
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
            return "redirect:/login";
        }

        logger.debug("saveOrUpdateLogin() : {}", "");

        UserLogin loginDetails = registrationService.loginUser(userLogin);

        if ( null != loginDetails
                && loginDetails.isLoginSuccess()) {

            model.addAttribute("session", loginDetails);
            //model.addAttribute("msg", "User logged in");
            httpSession.setAttribute("login", loginDetails);
            httpSession.setAttribute("user", loginDetails.getFirstName());
            httpSession.setAttribute("userLastName", loginDetails.getLastName());
            httpSession.setAttribute("role", loginDetails.getRole());
            httpSession.setAttribute("session", userLogin);

            if (loginDetails.getIsAdminActivated().equalsIgnoreCase("N")){
                httpSession.setAttribute("msg", "Please contact the admin to activate your account..!");
                return "users/contact_admin";
            }
            return "redirect:/profile";
        } else {
            httpSession.setAttribute("msg", "Invalid email or password..!!");
            return "redirect:/";
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

            String value = (String) httpSession.getAttribute("msg");
            if (null != value) {
                model.addAttribute("msg", value);
            }

            httpSession.removeAttribute("msg");
            httpSession.setAttribute("login", userLogin);
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

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/welcome";
        }
    }

    // Show Register page
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String schedule(ModelMap model, HttpSession httpSession) throws ParseException {
        logger.debug("schedule()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("session");

        model.addAttribute("session", userLogin);
        model.addAttribute("login", userLogin);
        model.addAttribute("userLogin", userLogin);

        if(userLogin.getIsActive().equalsIgnoreCase("N")){
            httpSession.setAttribute("msg", "You Need to be active to predict for matches");
            return "users/contact_admin";
        }

        List<Schedule> schedules = ValidatePredictions.validateSchedule(scheduleService.scheduleList());
        List<Prediction> predictions = scheduleService.findPredictions(userLogin.getMemberId());
        //schedules = ValidatePredictions.isScheduleAfterRegistration(schedules, userLogin.getRegisteredTime());
        List<Schedule> finalSchedule = ValidatePredictions.validatePrediction(schedules, predictions);
        predictions = ValidateDeadLine.mapScheduleToPredictions(schedules, predictions);

        model.addAttribute("predictions", predictions);
        model.addAttribute("schedules", schedules);

        httpSession.setMaxInactiveInterval(5 * 60);

        return "users/schedule";
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
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("msg", "OOOPSS!! Registration Failed!");
            return "redirect:/register";
        }
    }

    // Show Register page
    @RequestMapping(value = "/register", method = RequestMethod.GET)
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

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logoutUser(Model model, HttpSession httpSession) {

        logger.debug("logoutUser()");
        httpSession.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUserAgain(Model model, HttpSession httpSession) {

        logger.debug("logoutUser()");
        httpSession.invalidate();
        return "redirect:/";
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ModelAndView handleEmptyData(HttpServletRequest req, Exception ex) {

        logger.debug("handleEmptyData()");
        logger.error("Request: {}, error ", req.getRequestURL(), ex);

        ModelAndView model = new ModelAndView();
        model.setViewName("user/show");
        model.addObject("msg", "user not found");

        return model;

    }

}