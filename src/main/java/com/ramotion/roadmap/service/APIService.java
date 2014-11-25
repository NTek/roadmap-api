package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.dto.web.GetSurveyResponceDto;
import com.ramotion.roadmap.model.VoteEntity;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 */
public interface APIService {

    GetSurveyResponceDto getSurveyForDevice(String appSecret, String deviceToken, String langCode);

    VoteEntity createVote(String appSecret, NewVoteRequestDto dto);
}
