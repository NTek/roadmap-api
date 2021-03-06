package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.dto.EmailPasswordForm;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.dto.SurveyDto;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.ApplicationEntity;
import com.ramotion.roadmap.model.Language;
import com.ramotion.roadmap.service.APIService;
import com.ramotion.roadmap.service.ApplicationService;
import com.ramotion.roadmap.service.SurveyService;
import com.ramotion.roadmap.service.UserService;
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

    @RequestMapping(value = "/api/survey", method = RequestMethod.GET)
    public Object getSurveys(@RequestHeader(value = "token", required = true) String token,
                             @RequestParam(value = "deviceToken", required = true) String deviceToken,
                             @RequestParam(value = "language", required = true) String lang) {
        return apiService.getSurveyForDevice(token, deviceToken, lang);
    }

    @RequestMapping(value = "/api/vote", method = RequestMethod.POST)
    public Object vote(@RequestHeader(value = "token", required = true) String token,
                       @RequestBody NewVoteRequestDto voteDto) {
        return apiService.createVote(token, voteDto);
    }

    @RequestMapping(value = "/api/languages", method = RequestMethod.GET)
    public Object languages() {
        return Language.getAllEntities();
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody EmailPasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        userService.registerUser(form);
    }

    @RequestMapping(value = "/api/email-change-confirmation/{token}", method = RequestMethod.GET)
    public Object emailChangeConfirmation(@PathVariable(value = "token") String token) {
        return userService.confirmNewEmail(token);
    }

    //========================================== CLOSED API FOR FRONTEND ===============================================

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public Object getApps(Principal principal) {
        return applicationService.getApplicationsByUser(principal.getName());
    }

    @RequestMapping(value = "/apps", method = RequestMethod.POST)
    public Object createApp(Principal principal,
                            @Valid @RequestBody ApplicationEntity app,
                            BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return applicationService.createApplication(app, principal.getName());
    }

    @RequestMapping(value = "/apps/{id}", method = RequestMethod.PUT)
    public Object editApp(Principal principal,
                          @PathVariable(value = "id") long id,
                          @Valid @RequestBody ApplicationEntity app,
                          BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return applicationService.editApplication(id, app, principal.getName());
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Object getUserProfile(Principal principal) {
        return userService.getUserProfile(principal.getName());
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/profile/email", method = RequestMethod.POST)
    public void changeEmail(Principal principal,
                            @Valid @RequestBody EmailPasswordForm emailPasswordForm,
                            BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        userService.changeEmail(emailPasswordForm, principal.getName());
    }

    @RequestMapping(value = "/survey", method = RequestMethod.POST)
    public Object createSurvey(Principal principal,
                               @Valid @RequestBody SurveyDto dto,
                               BindingResult errors) {
        if (errors.hasErrors()) throw new ValidationException().withBindingResult(errors);
        return surveyService.createSurvey(dto, principal.getName());
    }

    @RequestMapping(value = "/survey/{id}/close", method = RequestMethod.GET)
    public Object closeSurvey(Principal principal,
                              @PathVariable(value = "id") long id) {
        return surveyService.closeSurvey(id, principal.getName());
    }

    @RequestMapping(value = "/survey/{id}/rename", method = RequestMethod.GET)
    public Object closeSurvey(Principal principal,
                              @RequestParam(required = true) String name,
                              @PathVariable(value = "id") long id) {
        return surveyService.renameSurvey(id, name, principal.getName());
    }

    @RequestMapping(value = "/survey/{id}/disable", method = RequestMethod.GET)
    public Object disableSurvey(Principal principal,
                                @PathVariable(value = "id") long id) {
        return surveyService.disableSurvey(id, principal.getName());
    }

    @RequestMapping(value = "/survey/{id}/enable", method = RequestMethod.GET)
    public Object enableSurvey(Principal principal,
                               @PathVariable(value = "id") long id) {
        return surveyService.enableSurvey(id, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/survey/{id}", method = RequestMethod.DELETE)
    public void deleteSurvey(Principal principal,
                             @PathVariable(value = "id") long id) {
        surveyService.deleteSurvey(id, principal.getName());
    }

}


