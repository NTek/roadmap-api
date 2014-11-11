package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
@Entity
@Table(name = "user", schema = "", catalog = "roadmap")
public class UserEntity {
    private Long id;
    private String email;

    @JsonIgnore
    private String password;

    private Boolean disabled;
    private String role;
    private Timestamp recoveryTokenExpiration;
    private String recoveryToken;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

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

    @Basic
    @Column(name = "created_at", nullable = false, insertable = true, updatable = true)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "modified_at", nullable = false, insertable = true, updatable = true)
    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (disabled != null ? !disabled.equals(that.disabled) : that.disabled != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (modifiedAt != null ? !modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (recoveryToken != null ? !recoveryToken.equals(that.recoveryToken) : that.recoveryToken != null)
            return false;
        if (recoveryTokenExpiration != null ? !recoveryTokenExpiration.equals(that.recoveryTokenExpiration) : that.recoveryTokenExpiration != null)
            return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (recoveryTokenExpiration != null ? recoveryTokenExpiration.hashCode() : 0);
        result = 31 * result + (recoveryToken != null ? recoveryToken.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (modifiedAt != null ? modifiedAt.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
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
