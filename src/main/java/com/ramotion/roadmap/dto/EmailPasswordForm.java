package com.ramotion.roadmap.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Simple email-password form with validation requirments
 */
public class EmailPasswordForm {

    @NotNull(message = "required")
    @NotEmpty(message = "email can't be empty")
    @Email(regexp = "(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3})", message = "invalid email")
    private String email;

    @NotNull(message = "required")
    @NotEmpty(message = "password can't be empty")
    @Pattern(regexp = "(^(?=.*\\d)(?=.*[a-zA-Z]).{6,50}$)", message = "at least one char and one number, length between 6 and 50 chars")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailPasswordForm that = (EmailPasswordForm) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
