package com.ramotion.roadmap.controllers.web;

import com.ramotion.roadmap.controllers.APIMappings;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.service.APIService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Oleg Vasiliev on 19.11.2014.
 * Web API for client sdk
 */
@Controller
@ResponseBody
@RequestMapping(value = "/api/{secret}/", method = RequestMethod.GET)
public class APIController {

    private static final Logger LOG = Logger.getLogger(APIController.class.getName());

    @Autowired
    private APIService apiService;

    @ResponseBody
    @RequestMapping(value = APIMappings.Web.GET_SURVEYS, method = RequestMethod.GET)
    public Object getSurveys(@PathVariable(value = "secret") String secret,
                             @RequestParam(value = "device", required = true) String deviceToken,
                             @RequestParam(value = "lang", required = true) String lang) {
        return apiService.getSurveysForDevice(secret, deviceToken, lang);
    }

    @ResponseBody
    @RequestMapping(value = APIMappings.Web.VOTE, method = RequestMethod.POST)
    public Object vote(@PathVariable(value = "secret") String secret,
                       @RequestBody NewVoteRequestDto voteDto) {
        return apiService.createVote(secret, voteDto);
    }

}


