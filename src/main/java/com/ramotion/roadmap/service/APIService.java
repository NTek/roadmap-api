package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.model.SurveyEntity;
import com.ramotion.roadmap.model.VoteEntity;

import java.util.List;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 */
public interface APIService {

    List<SurveyEntity> getSurveysForDevice(String appSecret, String deviceToken, String langCode);

    VoteEntity createVote(String appSecret, NewVoteRequestDto dto);
}
