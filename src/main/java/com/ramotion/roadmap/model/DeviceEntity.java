package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 12.11.2014.
 */
@Entity
@Table(name = "device")
public class DeviceEntity {
    private String token;

    private Integer platform;

    private String deviceLocale;

    @JsonIgnore
    private Collection<VoteEntity> votesByToken;

    @Id
    @Column(name = "token", nullable = false, insertable = true, updatable = true, length = 255)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "platform", nullable = false, insertable = true, updatable = true)
    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    @Column(name = "device_locale", nullable = false, insertable = true, updatable = true, length = 10)
    public String getDeviceLocale() {
        return deviceLocale;
    }

    public void setDeviceLocale(String deviceLocale) {
        this.deviceLocale = deviceLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceEntity that = (DeviceEntity) o;

        if (deviceLocale != null ? !deviceLocale.equals(that.deviceLocale) : that.deviceLocale != null) return false;
        if (platform != null ? !platform.equals(that.platform) : that.platform != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        result = 31 * result + (deviceLocale != null ? deviceLocale.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "deviceByDeviceToken")
    public Collection<VoteEntity> getVotesByToken() {
        return votesByToken;
    }

    public void setVotesByToken(Collection<VoteEntity> votesByToken) {
        this.votesByToken = votesByToken;
    }
}
