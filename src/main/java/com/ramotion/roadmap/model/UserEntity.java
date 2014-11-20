package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
@Entity
@Table(name = "user")
@EntityListeners(value = AuditableEntityListener.class)
public class UserEntity implements EntityWithAuditTimestamps {

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private boolean disabled;
    private String role;

    private Timestamp recoveryTokenExpiration;
    private String recoveryToken;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private Collection<UserHasApplicationEntity> userToApplicationsById;

    @JsonIgnore
    private Collection<AuthTokenEntity> userAuthTokensById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "disabled", nullable = false, insertable = true, updatable = true)
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Basic
    @Column(name = "role", nullable = false, insertable = true, updatable = true, length = 255)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "recovery_token_expiration", nullable = true, insertable = true, updatable = true)
    public Timestamp getRecoveryTokenExpiration() {
        return recoveryTokenExpiration;
    }

    public void setRecoveryTokenExpiration(Timestamp recoveryTokenExpiration) {
        this.recoveryTokenExpiration = recoveryTokenExpiration;
    }

    @Basic
    @Column(name = "recovery_token", nullable = true, insertable = true, updatable = true, length = 255)
    public String getRecoveryToken() {
        return recoveryToken;
    }

    public void setRecoveryToken(String recoveryToken) {
        this.recoveryToken = recoveryToken;
    }

    @Override
    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    @Override
    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (disabled != that.disabled) return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (recoveryToken != null ? !recoveryToken.equals(that.recoveryToken) : that.recoveryToken != null)
            return false;
        if (recoveryTokenExpiration != null ? !recoveryTokenExpiration.equals(that.recoveryTokenExpiration) : that.recoveryTokenExpiration != null)
            return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (userAuthTokensById != null ? !userAuthTokensById.equals(that.userAuthTokensById) : that.userAuthTokensById != null)
            return false;
        if (userToApplicationsById != null ? !userToApplicationsById.equals(that.userToApplicationsById) : that.userToApplicationsById != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (disabled ? 1 : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (recoveryTokenExpiration != null ? recoveryTokenExpiration.hashCode() : 0);
        result = 31 * result + (recoveryToken != null ? recoveryToken.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (userToApplicationsById != null ? userToApplicationsById.hashCode() : 0);
        result = 31 * result + (userAuthTokensById != null ? userAuthTokensById.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "user")
    public Collection<AuthTokenEntity> getUserAuthTokensById() {
        return userAuthTokensById;
    }

    public void setUserAuthTokensById(Collection<AuthTokenEntity> userAuthTokensById) {
        this.userAuthTokensById = userAuthTokensById;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<UserHasApplicationEntity> getUserToApplicationsById() {
        return userToApplicationsById;
    }

    public void setUserToApplicationsById(Collection<UserHasApplicationEntity> userToApplicationsById) {
        this.userToApplicationsById = userToApplicationsById;
    }
}
