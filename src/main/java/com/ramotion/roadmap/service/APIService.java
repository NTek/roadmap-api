package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.dto.web.GetSurveyDto;
import com.ramotion.roadmap.model.VoteEntity;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 */
public interface APIService {

    GetSurveyDto getSurveyForDevice(String appSecret, String deviceToken, String langCode);

    VoteEntity createVote(String appSecret, NewVoteRequestDto dto);
}