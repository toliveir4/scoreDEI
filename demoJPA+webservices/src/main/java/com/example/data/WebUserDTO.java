package com.example.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

public class WebUserDTO {
    @NotNull
    @NotEmpty(message = "Name cannot be empty")
    private String Name;

    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotNull
    @NotEmpty(message = "Matching Password cannot be empty")
    private String matchingPassword;

    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull
    private boolean admin;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
