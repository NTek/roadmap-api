package com.ramotion.roadmap.controllers.web;

import com.ramotion.roadmap.controllers.APIMappings;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.Language;
import com.ramotion.roadmap.service.APIService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Oleg Vasiliev on 19.11.2014.
 * Web API for voting
 */
@Controller
@ResponseBody
@RequestMapping(value = "/api/")
public class APIController {

    private static final Logger LOG = Logger.getLogger(APIController.class.getName());

    @Autowired
    private APIService apiService;

    @ResponseBody
    @RequestMapping(value = APIMappings.Web.GET_SURVEY, method = RequestMethod.GET)
    public Object getSurveys(@RequestHeader(value = "token", required = true) String token,
                             @RequestParam(value = "deviceToken", required = true) String deviceToken,
                             @RequestParam(value = "language", required = true) String lang) {
        return apiService.getSurveyForDevice(token, deviceToken, lang);
    }

    @ResponseBody
    @RequestMapping(value = APIMappings.Web.CREATE_VOTE, method = RequestMethod.POST)
    public Object vote(@RequestHeader(value = "token", required = true) String token,
                       @RequestBody NewVoteRequestDto voteDto) {
        return apiService.createVote(token, voteDto);
    }

    @RequestMapping(value = APIMappings.Web.GET_LANGUAGES, method = RequestMethod.GET)
    public Object languages() {
        return Language.getAllEntities();
    }

    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public Object validationException(HttpServletRequest req, ValidationException e) {
        return e.getMessage();
    }

}


