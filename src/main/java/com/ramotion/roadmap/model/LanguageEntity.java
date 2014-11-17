package com.ramotion.roadmap.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 12.11.2014.
 */
@Entity
@Table(name = "language")
public class LanguageEntity {

    private Integer id;

    private String name;

    private String code;

    @JsonIgnore
    private Collection<LocalizedFeatureEntity> localizedFeaturesById;

    @JsonIgnore
    private Collection<VoteEntity> votesById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code", nullable = true, insertable = true, updatable = true, length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguageEntity that = (LanguageEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "languageByLanguageId")
    public Collection<LocalizedFeatureEntity> getLocalizedFeaturesById() {
        return localizedFeaturesById;
    }

    public void setLocalizedFeaturesById(Collection<LocalizedFeatureEntity> localizedFeaturesById) {
        this.localizedFeaturesById = localizedFeaturesById;
    }

    @OneToMany(mappedBy = "languageByLanguageId")
    public Collection<VoteEntity> getVotesById() {
        return votesById;
    }

    public void setVotesById(Collection<VoteEntity> votesById) {
        this.votesById = votesById;
    }
}
