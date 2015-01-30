package com.ramotion.roadmap.dto;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

public class SurveyDto {

    private Long id;

    @NotNull
    private Long applicationId;

    private String title;

    private Set<Long> features;

    private Long requiredVotes;

    //    @JsonSerialize(using = JsonTimestampSerializer.class)
    //    @JsonDeserialize(using = JsonTimestampDeserializer.class)
    private Timestamp requiredDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Set<Long> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Long> features) {
        this.features = features;
    }

    public Long getRequiredVotes() {
        return requiredVotes;
    }

    public void setRequiredVotes(Long requiredVotes) {
        this.requiredVotes = requiredVotes;
    }

    public Timestamp getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Timestamp requiredDate) {
        this.requiredDate = requiredDate;
    }


}
