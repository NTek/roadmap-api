package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.model.SurveyEntity;
import com.ramotion.roadmap.model.VoteEntity;
import com.ramotion.roadmap.repository.ApplicationRepository;
import com.ramotion.roadmap.repository.SurveyRepository;
import com.ramotion.roadmap.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 * Implementation for Web API Service
 */
@Service
public class APIServiceImpl implements APIService {

    private SurveyRepository surveyRepository;
    private ApplicationRepository applicationRepository;
    private VoteRepository voteRepository;

    @Autowired
    public APIServiceImpl(SurveyRepository surveyRepository, ApplicationRepository applicationRepository, VoteRepository voteRepository) {
        this.surveyRepository = surveyRepository;
        this.applicationRepository = applicationRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public List<SurveyEntity> getSurveysForDevice(String apiKey, String deviceToken, String langCode) {
        //TODO: write business logic for get localized surveys here
        return null;
    }

    @Override
    public VoteEntity createVote(String appSecret, NewVoteRequestDto dto) {
        //TODO: write business logic for voting here
        return null;
    }

}
