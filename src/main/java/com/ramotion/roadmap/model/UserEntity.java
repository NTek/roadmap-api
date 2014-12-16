package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "user")
@EntityListeners(value = {AuditableEntityListener.class, EntityWithUUIDListener.class})
public class UserEntity implements EntityWithAuditTimestamps, EntityWithUUID {

    @JsonIgnore
    private Long id;

    private UUID uuid;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    private String role = "ROLE_USER";

    @JsonIgnore
    private Timestamp recoveryTokenExpiration;

    @JsonIgnore
    private String recoveryToken;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private Collection<UserHasApplicationEntity> applications;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "uuid", nullable = false, insertable = true, updatable = false)
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enabled", nullable = false, insertable = true, updatable = true)
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "role", nullable = false, insertable = true, updatable = true, length = 255)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "recovery_token_expiration", nullable = true, insertable = true, updatable = true)
    public Timestamp getRecoveryTokenExpiration() {
        return recoveryTokenExpiration;
    }

    public void setRecoveryTokenExpiration(Timestamp recoveryTokenExpiration) {
        this.recoveryTokenExpiration = recoveryTokenExpiration;
    }

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

        if (enabled != that.enabled) return false;
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
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (recoveryTokenExpiration != null ? recoveryTokenExpiration.hashCode() : 0);
        result = 31 * result + (recoveryToken != null ? recoveryToken.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<UserHasApplicationEntity> getApplications() {
        return applications;
    }

    public void setApplications(Collection<UserHasApplicationEntity> userToApplicationsById) {
        this.applications = userToApplicationsById;
    }
}
