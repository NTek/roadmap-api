package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.GetSurveyDto;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.model.VoteEntity;

public interface APIService {

    GetSurveyDto getSurveyForDevice(String appSecret, String deviceToken, String langCode);

    VoteEntity createVote(String appSecret, NewVoteRequestDto dto);
}
