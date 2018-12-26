package com.sports.cricket.model;

public class UserLogin {

   private String email;

   private String password;

   private Integer memberId;

   private String firstName;

   private String lastName;

   private String registeredTime;

   private boolean isLoginSuccess;

   private String role;

   private boolean isLimitReached;

   private String isActive;

   private String isAdminActivated;

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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLimitReached() {
        return isLimitReached;
    }

    public void setLimitReached(boolean limitReached) {
        isLimitReached = limitReached;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    public String getIsAdminActivated() {
        return isAdminActivated;
    }

    public void setIsAdminActivated(String isAdminActivated) {
        this.isAdminActivated = isAdminActivated;
    }
}
