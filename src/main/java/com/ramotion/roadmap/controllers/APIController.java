package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.dto.EmailPasswordForm;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.dto.SurveyDto;
import com.ramotion.roadmap.exceptions.AccessDeniedException;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.ApplicationEntity;
import com.ramotion.roadmap.model.Language;
import com.ramotion.roadmap.service.APIService;
import com.ramotion.roadmap.service.ApplicationService;
import com.ramotion.roadmap.service.SurveyService;
import com.ramotion.roadmap.service.UserService;
import com.ramotion.roadmap.utils.APIMappings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class APIController {

    private static final Logger LOG = Logger.getLogger(APIController.class.getName());

    @Autowired
    private APIService apiService;

    @Autowired
    private UserService userService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ApplicationService applicationService;

    //============================================ PUBLIC API FOR SDK ==================================================

    @RequestMapping(value = APIMappings.Web.SDK_API_ROOT + APIMappings.Web.SDK_GET_SURVEY, method = RequestMethod.GET)
    public Object getSurveys(@RequestHeader(value = "token", required = true) String token,
                             @RequestParam(value = "deviceToken", required = true) String deviceToken,
                             @RequestParam(value = "language", required = true) String lang) {
        return apiService.getSurveyForDevice(token, deviceToken, lang);
    }

    @RequestMapping(value = APIMappings.Web.SDK_API_ROOT + APIMappings.Web.SDK_CREATE_VOTE, method = RequestMethod.POST)
    public Object vote(@RequestHeader(value = "token", required = true) String token,
                       @RequestBody NewVoteRequestDto voteDto) {
        return apiService.createVote(token, voteDto);
    }

    @RequestMapping(value = APIMappings.Web.SDK_API_ROOT + APIMappings.Web.SDK_GET_LANGUAGES, method = RequestMethod.GET)
    public Object languages() {
        return Language.getAllEntities();
    }

    @RequestMapping(value = APIMappings.Web.SDK_API_ROOT + APIMappings.Web.FRONTEND_REGISTRATION, method = RequestMethod.POST)
    public void register(@Valid @RequestBody EmailPasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        userService.registerUser(form);
    }

    //========================================== CLOSED API FOR FRONTEND ===============================================

    @RequestMapping(value = APIMappings.Web.FRONTEND_APPS, method = RequestMethod.GET)
    public Object getApps(Principal principal) {
        return applicationService.getApplicationsByUser(principal.getName());
    }

    @RequestMapping(value = APIMappings.Web.FRONTEND_APPS, method = RequestMethod.POST)
    public Object createApp(Principal principal,
                            @Valid @RequestBody ApplicationEntity app,
                            BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return applicationService.createApplication(app, principal.getName());
    }

    @RequestMapping(value = APIMappings.Web.FRONTEND_APPS + "/{id}", method = RequestMethod.PUT)
    public Object editApp(Principal principal,
                          @PathVariable(value = "id") long id,
                          @Valid @RequestBody ApplicationEntity app,
                          BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return applicationService.editApplication(id, app, principal.getName());
    }

    @RequestMapping(value = APIMappings.Web.FRONTEND_PROFILE, method = RequestMethod.GET)
    public Object getUserProfile(Principal principal) {
        return userService.getUserProfile(principal.getName());
    }

    @RequestMapping(value = APIMappings.Web.FRONTEND_SURVEY, method = RequestMethod.POST)
    public Object createOrEditSurvey(Principal principal,
                                     @Valid @RequestBody SurveyDto dto,
                                     BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return surveyService.createSurvey(dto, principal.getName());
    }

    //============================================= EXCEPTION HANDLERS =================================================

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Object validationException(ValidationException e) {
        return e.getErrors();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object forbidden(AccessDeniedException e) {
        return e.getMessage();
    }

}


