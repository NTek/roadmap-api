package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.dto.NewVoteRequestDto;
import com.ramotion.roadmap.dto.web.GetSurveyDto;
import com.ramotion.roadmap.exceptions.NotFoundException;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.*;
import com.ramotion.roadmap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 * Implementation for Web API Service
 * TODO: Move validations to separate validator class
 * TODO: Add exception handlers
 */
@Service
@Transactional
public class APIServiceImpl implements APIService {

    private SurveyRepository surveyRepository;
    private FeatureTextRepository featureTextRepository;
    private FeatureRepository featureRepository;
    private ApplicationRepository applicationRepository;
    private VoteRepository voteRepository;

    @Autowired
    public APIServiceImpl(SurveyRepository surveyRepository, ApplicationRepository applicationRepository,
                          VoteRepository voteRepository, FeatureTextRepository featureTextRepository,
                          FeatureRepository featureRepository) {
        this.surveyRepository = surveyRepository;
        this.applicationRepository = applicationRepository;
        this.voteRepository = voteRepository;
        this.featureTextRepository = featureTextRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    public GetSurveyDto getSurveyForDevice(String apiToken, String deviceToken, String langCode) {
        ApplicationEntity app = applicationRepository.findByApiToken(apiToken);
        if (app == null) throw new NotFoundException();  //app api token invalid

        if (app.getActiveSurveyId() == null) throw new ValidationException(); //app does not have active survey

        SurveyEntity activeSurvey = surveyRepository.findOne(app.getActiveSurveyId());
        if (activeSurvey == null) throw new ValidationException(); //app does not have active survey

        VoteEntity existedVote = voteRepository.findOne(new VoteEntityPK(deviceToken, activeSurvey.getId()));
        if (existedVote != null) throw new ValidationException(); // this device already voted in this survey

        //detecting language
        Language lang = Language.valueOfOrNullIgnoreCase(langCode);
        if (lang == null) throw new ValidationException(); //invalid language

        GetSurveyDto getSurveyDto = new GetSurveyDto();
        getSurveyDto.setSurveyId(activeSurvey.getId());
        Collection<FeatureEntity> surveyFeatures = activeSurvey.getFeature();

        for (FeatureEntity feature : surveyFeatures) {
            FeatureTextEntity localizedFeature = featureTextRepository.findByFeatureIdAndLanguage(feature.getId(), lang);
            if (localizedFeature == null) {
                localizedFeature = featureTextRepository.findByFeatureIdAndLanguage(feature.getId(), AppConfig.DEFAULT_LOCALIZATION_LANGUAGE);
            }
            getSurveyDto.getFeatures().add(new GetSurveyDto.Feature(feature.getId(), localizedFeature != null ? localizedFeature.getText() : AppConfig.LOCALIZATION_NOT_FOUND_MSG));
        }

        return getSurveyDto;
    }

    @Override
    public VoteEntity createVote(String apiToken, NewVoteRequestDto dto) {
        ApplicationEntity app = applicationRepository.findByApiToken(apiToken);
        if (app == null) throw new NotFoundException();  //app api token invalid

        //detecting language
        Language lang = Language.valueOfOrNullIgnoreCase(dto.getLanguage());
        if (lang == null) throw new ValidationException(); //invalid language

        if (!app.getActiveSurveyId().equals(dto.getSurveyId()))
            throw new ValidationException(); // survey not available for voting

        SurveyEntity surveyEntity = surveyRepository.findOne(dto.getSurveyId());

        if (surveyEntity == null || !surveyEntity.getApplicationId().equals(app.getId()))
            throw new NotFoundException(); // survey not found

        if (surveyEntity.isDisabled())
            throw new ValidationException(); // survey temporary disabled

        FeatureEntity selectedFeature = featureRepository.findOne(dto.getFeatureId());

        if (selectedFeature == null || !surveyEntity.getFeature().contains(selectedFeature))
            throw new ValidationException(); // selected feature not found in survey

        VoteEntityPK pk = new VoteEntityPK(dto.getDevice(), dto.getSurveyId());

        if (voteRepository.exists(pk))
            throw new ValidationException(); // device already voted in this survey
        else {
            return voteRepository.save(new VoteEntity(pk, dto.getFeatureId(), lang));
        }

    }


}
