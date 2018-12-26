package com.sports.cricket.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sports.cricket.model.*;
import com.sports.cricket.service.RegistrationService;
import com.sports.cricket.service.ScheduleService;
import com.sports.cricket.util.LeaderBoardDetails;
import com.sports.cricket.util.MatchUpdates;
import com.sports.cricket.util.ValidateDeadline;
import com.sports.cricket.util.ValidatePredictions;
import com.sports.cricket.validations.ErrorDetails;
import com.sports.cricket.validations.FormValidator;
import com.sports.cricket.validations.ResultValidator;
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

    UserLogin userLogin = new UserLogin();

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, HttpSession httpSession) {
        logger.debug("index()");
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showHomePage(Model model, HttpSession httpSession) throws ParseException {

        if (null != httpSession.getAttribute("user")) {
            userLogin.setFirstName(httpSession.getAttribute("user").toString());
            userLogin.setLastName(httpSession.getAttribute("userLastName").toString());
            userLogin.setRole(httpSession.getAttribute("role").toString());
            model.addAttribute("user", userLogin);
        }

        logger.debug("showHomePage()");
        List<Schedule> scheduleList = scheduleService.scheduleList();
        scheduleList = MatchUpdates.setLineNumbersToSchedule(ValidateDeadLine.getScheduleList(scheduleList, scheduleService));

        model.addAttribute("schedules", scheduleList);
        return "users/index";

    }

    @RequestMapping(value = "/faq", method = RequestMethod.GET)
    public String showFaqs(Model model, HttpSession httpSession) {

        if (null != httpSession.getAttribute("user")) {
            userLogin.setFirstName(httpSession.getAttribute("user").toString());
            userLogin.setLastName(httpSession.getAttribute("userLastName").toString());
            userLogin.setRole(httpSession.getAttribute("role").toString());
            model.addAttribute("user", userLogin);
            model.addAttribute("session", userLogin);
        }

        logger.debug("showFaqs()");

        return "users/faq";

    }

    // Show login page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(ModelMap model, HttpSession httpSession) {

        List<ErrorDetails> loginErrorDetails = (List<ErrorDetails>) httpSession.getAttribute("loginErrorDetails");
        String loginError = (String) httpSession.getAttribute("msg");

        if (null != loginErrorDetails
                && loginErrorDetails.size() > 0) {
            model.addAttribute("loginErrorDetails", loginErrorDetails);

            httpSession.removeAttribute("loginErrorDetails");
        }

        if (null != loginError) {
            model.addAttribute("msg", loginError);
            httpSession.removeAttribute("msg");
        }

        UserLogin userLogin = new UserLogin();

        logger.debug("showLogin()");
        model.addAttribute("userLogin", userLogin);
        if (null == httpSession.getAttribute("login")) {
            return "users/login_form";
        } else {
            return "redirect:/";
        }
    }

    // Show login page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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

            List<Standings> standingsList = scheduleService.getLeaderBoard();
            List<Restrictions> restrictions = registrationService.getRestrictions();

            Register currentUser = registrationService.getUser(userLogin.getEmail());

            userLogin.setIsActive(currentUser.getIsActive());

            userLogin.setLimitReached(LeaderBoardDetails.isLimitReached(standingsList, userLogin.getMemberId(), restrictions.get(0).getMaxLimit()));

            model.addAttribute("session", userLogin);
            model.addAttribute("userLogin", userLogin);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/user_profile";
        }
    }

    // Validate the login details
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("userLogin") UserLogin userLogin, ModelMap model
            , HttpSession httpSession) {

        model.addAttribute("userLogin", userLogin);

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
                return "redirect:/profile";
            }

            return "redirect:/showPredictions";
        } else {
            httpSession.setAttribute("msg", "Invalid email or password..!!");
            return "redirect:/login";
        }
    }

    // Validate the login details
    @RequestMapping(value = "/member/{memberId}/optOut/{action}", method = RequestMethod.GET)
    public String optOutMember(@PathVariable("memberId") Integer memberId, @PathVariable("action") String action, ModelMap model
            , HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

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
            httpSession.setAttribute("login", userLogin);

            if (action.equalsIgnoreCase("disable")) {
                action = "N";
            } else if (action.equalsIgnoreCase("enable")) {
                action = "Y";
            }

            boolean isOptOutSuccess = registrationService.optOutUser(memberId, action);

            if (isOptOutSuccess && ( null != action && action.equalsIgnoreCase("N"))) {
                httpSession.setAttribute("msg", "You have successfully Opted out.. !!");
            }else if (isOptOutSuccess && ( null != action && action.equalsIgnoreCase("Y"))) {
                httpSession.setAttribute("msg", "You have successfully activated ..!!");
            }

            httpSession.setMaxInactiveInterval(5 * 60);
            return "redirect:/profile";
        }
    }

    // Update Result
    @RequestMapping(value = "/updateResult", method = RequestMethod.GET)
    public String updateResult(ModelMap model, HttpSession httpSession) throws ParseException {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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
            Schedule schedule = new Schedule();
            httpSession.removeAttribute("msg");
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);
            model.addAttribute("schedule", schedule);

            List<Schedule> schedules = scheduleService.findAll();

            model.addAttribute("schedules", schedules);
            if (null != httpSession.getAttribute("errorDetailsList")){
                List<ErrorDetails> errorDetailsList = (List<ErrorDetails>)httpSession.getAttribute("errorDetailsList");
                model.addAttribute("errorDetailsList", errorDetailsList);
                httpSession.removeAttribute("errorDetailsList");
            }

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/update_result";
        }
    }

    @RequestMapping(value = "/matchResult/update", method = RequestMethod.POST)
    public String updateMatchResult(@ModelAttribute("schedule") Schedule schedule, ModelMap model, HttpSession httpSession) throws ParseException {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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

            boolean isUpdateSuccess = false;
            boolean isUpdateResultSuccess = false;

            if (null != schedule
                    && null != schedule.getWinner()) {
                List<ErrorDetails> errorDetailsList = ResultValidator.isMatchResultValid(schedule);
                if (errorDetailsList.size() > 0 ){
                    httpSession.setAttribute("errorDetailsList" , errorDetailsList);
                    return "redirect:/updateResult";
                }
                Integer totalMatches = scheduleService.totalMatches(schedule.getMatchDay());
                isUpdateSuccess = scheduleService.updateMatchResult(schedule);
                SchedulePrediction schedulePrediction = matchUpdates.setUpdates(schedule, scheduleService, registrationService);
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
            return "redirect:/updateResult";
        }
    }

    // Display predictions
    @RequestMapping(value = "/showPredictions", method = RequestMethod.POST)
    public String showAllPredictions(ModelMap model, HttpSession httpSession) {
        return "redirect:/showPredictions";
    }

    // Display predictions
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String showHistory(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("session", userLogin);

            List<Standings> standingsList = LeaderBoardDetails.getStandings(scheduleService.getLeaderBoard(), userLogin.getMemberId());
            standingsList = MatchUpdates.mapStandings(standingsList);

            model.addAttribute("standingsList", standingsList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/standings";
        }
    }

    // Display predictions
    @RequestMapping(value = "/showPredictions", method = RequestMethod.GET)
    public String showPredictions(ModelMap model, HttpSession httpSession) throws ParseException {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("user", userLogin.getFirstName());
            httpSession.setAttribute("role", userLogin.getRole());
            httpSession.setAttribute("session", userLogin);

            Register register = registrationService.getUser(userLogin.getEmail());

            userLogin.setIsActive(register.getIsActive());

            if(userLogin.getIsActive().equalsIgnoreCase("N")){
                httpSession.setAttribute("msg", "You Need to be active to predict for matches");
                return "redirect:/profile";
            }

            List<Schedule> schedules = ValidatePredictions.validateSchedule(scheduleService.scheduleList());
            List<Prediction> predictions = scheduleService.findPredictions(userLogin.getMemberId());
            schedules = ValidatePredictions.isScheduleAfterRegistration(schedules, register.getRegisteredTime());
            List<Schedule> finalSchedule = ValidatePredictions.validatePrediction(schedules, predictions);
            predictions = ValidateDeadLine.mapScheduleToPredictions(schedules, predictions);

            model.addAttribute("predictions", predictions);
            model.addAttribute("schedules", finalSchedule);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/member_login";
        }
    }

    // Display predictions
    @RequestMapping(value = "/standings", method = RequestMethod.GET)
    public String standings(ModelMap model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
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
            httpSession.setAttribute("login", userLogin);
            httpSession.setAttribute("session", userLogin);

            List<Register> registerList = registrationService.getAllUsers();
            List<Standings> standingsList = scheduleService.getLeaderBoard();

            List<LeaderBoard> leaderBoardList = LeaderBoardDetails.mapLeaderBoard(standingsList, registerList);

            model.addAttribute("leaderBoardList", leaderBoardList);

            httpSession.setMaxInactiveInterval(5 * 60);
            return "users/leaderboard";
        }
    }

    // Display predictions
    @RequestMapping(value = "/prediction/{predictionId}/{matchNumber}/view", method = RequestMethod.GET)
    public String showMemberSelected(@PathVariable("predictionId") Integer predictionId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

        if (null == userLogin) {
            return "redirect:/";
        } else {

            Prediction prediction = scheduleService.getPrediction(predictionId, matchNumber);

            model.addAttribute("session", userLogin);
            model.addAttribute("prediction", prediction);
            httpSession.setAttribute("login", userLogin);

            return "users/show_prediction";
        }
    }

    // Register User
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterForm(Model model, HttpSession httpSession) {

        List<ErrorDetails> registerErrorDetails = (List<ErrorDetails>) httpSession.getAttribute("registerErrorDetails");

        if (null != registerErrorDetails
                && registerErrorDetails.size() > 0) {
            model.addAttribute("registerErrorDetails", registerErrorDetails);
            httpSession.removeAttribute("registerErrorDetails");
        }

        Register register = new Register();

        logger.debug("showRegister()");
        model.addAttribute("registerForm", register);

        if (null == httpSession.getAttribute("login")) {
            return "users/register_form";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("registerForm") Register register, ModelMap model
            , final RedirectAttributes redirectAttributes, HttpSession httpSession) {

        logger.debug("registerUser() : {}", "");

        if (null != httpSession.getAttribute("registerErrorDetails")) {
            httpSession.removeAttribute("registerErrorDetails");
        }

        if (null != model.get("registerErrorDetails")) {
            model.remove("registerErrorDetails");
        }

        List<Restrictions> restrictionsList = registrationService.getRestrictions();
        List<ErrorDetails> registerErrorDetails = formValidator.isRegisterValid(register, restrictionsList.get(0));

        if (null != registerErrorDetails
                && registerErrorDetails.size() > 0) {
            model.addAttribute("registerErrorDetails", registerErrorDetails);
            httpSession.setAttribute("registerErrorDetails", registerErrorDetails);
            return "redirect:/register";
        }

        boolean success = registrationService.registerUser(register);

        if (success) {
            redirectAttributes.addFlashAttribute("msg", "User added successfully!");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("msg", "Registration Failed!");
            return "redirect:/register";
        }

    }

    // Display predictions
    @RequestMapping(value = "/member/{memberId}/authorize", method = RequestMethod.GET)
    public String authorizeMember(@PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

        if (null == userLogin) {
            return "redirect:/";
        } else {
            boolean isAuthSuccess = scheduleService.authorizeMember(memberId);

            model.addAttribute("session", userLogin);
            model.addAttribute("isAuthSuccess", isAuthSuccess);
            if (isAuthSuccess){
                userLogin.setIsAdminActivated("Y");
            }
            httpSession.setAttribute("login", userLogin);

            return "redirect:/showAllUsers";
        }
    }

    // Show All Users
    @RequestMapping(value = "/showAllUsers", method = RequestMethod.GET)
    public String showAllUsers(Model model, HttpSession httpSession) {

        logger.debug("showAllUsers()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

        if (null != userLogin.getRole() && !userLogin.getRole().equalsIgnoreCase("admin")) {
            httpSession.setAttribute("msg", "You need to be an admin to view this page.!");
            return "redirect:/showPredictions";
        }
        model.addAttribute("session", userLogin);
        model.addAttribute("login", userLogin);
        model.addAttribute("userLogin", userLogin);
        List<Register> registerList = registrationService.getAllUsers();
        model.addAttribute("registerList", registerList);

        return "users/members_list";
    }

    // Show Current Predictions
    @RequestMapping(value = "/currentPredictions", method = RequestMethod.GET)
    public String showCurrentPredictions(Model model, HttpSession httpSession) throws ParseException {

        logger.debug("showCurrentPredictions()");

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

        model.addAttribute("session", userLogin);
        model.addAttribute("login", userLogin);
        model.addAttribute("userLogin", userLogin);
        List<Schedule> currentSchedule = ValidatePredictions.validateSchedule(scheduleService.findAll());

        List<SchedulePrediction> schedulePredictionsList = new ArrayList<>();
        for (Schedule schedule : currentSchedule) {

            SchedulePrediction matchDetails = matchUpdates.setUpdates(schedule, scheduleService, registrationService);
            ValidateDeadLine.isUpdatePossible(matchDetails.getSchedule(), matchDetails.getPrediction());
            schedulePredictionsList.add(matchDetails);
        }

        model.addAttribute("schedulePredictions", schedulePredictionsList);

        if (userLogin.getRole().equalsIgnoreCase("admin")) {
            return "users/prediction_list";
        }else {
            return "users/users_match_predictions";
        }
    }

    // Forget Password
    @RequestMapping(value = "/forget", method = RequestMethod.GET)
    public String forgetPassword(Model model, HttpSession httpSession) {

        if ( null != httpSession.getAttribute("errorDetailsList")){
            List<ErrorDetails> errorDetailsList = (List<ErrorDetails>)httpSession.getAttribute("errorDetailsList");
            model.addAttribute("errorDetailsList", errorDetailsList);
            httpSession.removeAttribute("errorDetailsList");
            return "users/forget_password";
        }

        Register register = new Register();

        logger.debug("forgetPassword()");
        model.addAttribute("registerForm", register);

        if (null == httpSession.getAttribute("login")) {
            return "users/forget_password";
        } else {
            return "redirect:/resetPassword";
        }
    }

    // Rest Password
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String resetPasswordAfterLogin(Model model, HttpSession httpSession) {

        logger.debug("resetPasswordAfterLogin()");
        Register userDetails;
        if (null != httpSession.getAttribute("login")) {
            UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");
            userDetails = registrationService.getUser(userLogin.getEmail());
            Register register = new Register();

            model.addAttribute("registerForm", register);
            model.addAttribute("userDetails", userDetails);

            return "users/reset_password";

        } else {
            return "redirect:/";
        }
    }

    // Register User
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("registerForm") Register register, Model model, HttpSession httpSession) {

        logger.debug("resetPassword()");
        Register userDetails = null;

        List<ErrorDetails> errorDetailsList = formValidator.isEmailValid(register);

        if (errorDetailsList.size() > 0){
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/forget";
        } else {
            userDetails = registrationService.getUser(register.getEmailId());
        }


        if (null == userDetails){
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorField("login");
            errorDetails.setErrorMessage("Invalid Email ID");
            errorDetailsList.add(errorDetails);
            httpSession.setAttribute("errorDetailsList", errorDetailsList);
            return "redirect:/forget";
        }

        logger.debug("forgetPassword()");
        model.addAttribute("registerForm", register);
        model.addAttribute("userDetails", userDetails);

        if (null == httpSession.getAttribute("login")) {
            return "users/reset_password";
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

        if (errorDetailsList.size() > 0){
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

        if (null == httpSession.getAttribute("login")) {
            return "redirect:/login";
        } else {
            return "redirect:/showPredictions";
        }
    }

    // show update form
    @RequestMapping(value = "/match/{memberId}/{matchNumber}/predict", method = RequestMethod.GET)
    public String predictMatch(@PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) {

        logger.debug("predictMatch() : {}", memberId, matchNumber);

        if (null != httpSession.getAttribute("errorDetailsList")) {
            List<ErrorDetails> errorDetailsList = (List<ErrorDetails>)httpSession.getAttribute("errorDetailsList");
            model.addAttribute("errorDetailsList", errorDetailsList);
            httpSession.removeAttribute("errorDetailsList");
        }

        Schedule schedule = scheduleService.findById(matchNumber);

        Prediction prediction = new Prediction();

        model.addAttribute("scheduleForm", schedule);
        model.addAttribute("predictionForm", prediction);
        model.addAttribute("session", httpSession.getAttribute("session"));

        return "users/prediction";
    }

    // show update form
    @RequestMapping(value = "/prediction/{predictionId}/delete", method = RequestMethod.GET)
    public String deletePrediction(@PathVariable("predictionId") Integer predictionId, Model model, HttpSession httpSession) {

        logger.debug("deletePrediction() : {}", predictionId);

        boolean isDeleteSuccess = scheduleService.deletePrediction(predictionId);

        model.addAttribute("isDeleteSuccess", isDeleteSuccess);
        model.addAttribute("session", httpSession.getAttribute("session"));
        httpSession.setAttribute("msg", "Prediction deleted successfully ..!! ");

        return "redirect:/showPredictions";

    }


    // show update prediction form
    @RequestMapping(value = "/prediction/{memberId}/{matchNumber}/update", method = RequestMethod.GET)
    public String showCurrentPrediction(@PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) {

        logger.debug("showCurrentPrediction() : {}", memberId, matchNumber);

        UserLogin userLogin = (UserLogin) httpSession.getAttribute("login");

        if (null == userLogin) {
            return "redirect:/";
        }

        Schedule schedule = scheduleService.findById(matchNumber);
        Prediction prediction = scheduleService.getPrediction(memberId, matchNumber);

        model.addAttribute("scheduleForm", schedule);
        model.addAttribute("predictionForm", prediction);
        model.addAttribute("session", httpSession.getAttribute("session"));
        httpSession.setAttribute("msg", "Update for match " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is saved successfully ..!! ");

        return "users/update_prediction";

    }

    // Update Prediction details
    @RequestMapping(value = "/match/{memberId}/{matchNumber}/save", method = RequestMethod.POST)
    public String saveUpdatedPrediction(@ModelAttribute("predictionForm") Prediction prediction, @PathVariable("memberId") Integer memberId, @PathVariable("matchNumber") Integer matchNumber, Model model, HttpSession httpSession) {

        logger.debug("saveUpdatedPrediction() : {}", memberId, matchNumber);

        boolean savePrediction = scheduleService.updatePrediction(prediction);

        model.addAttribute("isPredictionSuccess", savePrediction);
        model.addAttribute("session", httpSession.getAttribute("session"));

        return "redirect:/showPredictions";
    }

    // Update Prediction details
    @RequestMapping(value = "/match/{memberId}/add", method = RequestMethod.POST)
    public String savePrediction(@ModelAttribute("predictionForm") Prediction prediction, @PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        logger.debug("savePrediction() : {}", memberId, memberId);

        List<ErrorDetails> errorDetailsList = ResultValidator.isValid(prediction);
        if (errorDetailsList.size() > 0 ){
            httpSession.setAttribute("errorDetailsList", errorDetailsList);

            return "redirect:/match/"+prediction.getMemberId()+"/"+prediction.getMatchNumber()+"/predict";
        }
        boolean savePrediction = scheduleService.savePrediction(prediction);

        //Prediction prediction = new Prediction();

        //model.addAttribute("scheduleForm", schedule);
        model.addAttribute("isPredictionSuccess", savePrediction);
        model.addAttribute("session", httpSession.getAttribute("session"));
        //model.addAttribute("msg" , "Your Prediction for " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is Saved Successfully!!");
        httpSession.setAttribute("msg", "Your Prediction for " + prediction.getHomeTeam() + " vs " + prediction.getAwayTeam() + " is Saved Successfully!!");

        return "redirect:/showPredictions";
    }

    // Update Prediction details
    @RequestMapping(value = "/match/{memberId}/update", method = RequestMethod.POST)
    public String updatePrediction(@ModelAttribute("predictionForm") Prediction prediction, @PathVariable("memberId") Integer memberId, Model model, HttpSession httpSession) {

        logger.debug("updatePrediction() : {}", memberId, memberId);

        boolean savePrediction = scheduleService.savePrediction(prediction);

        model.addAttribute("isPredictionSuccess", savePrediction);
        model.addAttribute("session", httpSession.getAttribute("session"));

        return "redirect:/showPredictions";
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logoutUser(Model model, HttpSession httpSession) {

        logger.debug("logoutUser()");
        httpSession.invalidate();
        return "pages/logout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUserAgain(Model model, HttpSession httpSession) {

        logger.debug("logoutUser()");
        httpSession.invalidate();
        return "pages/logout";
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