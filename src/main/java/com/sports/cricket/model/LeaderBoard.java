package com.sports.cricket.model;

import java.io.Serializable;

public class LeaderBoard implements Serializable {

    private Integer rank;

    private Integer memberId;

    private String firstName;

    private String lastName;

    private float wonAmount;

    private float lostAmount;

    private float total;

    private String isActive;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public float getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(float wonAmount) {
        this.wonAmount = wonAmount;
    }

    public float getLostAmount() {
        return lostAmount;
    }

    public void setLostAmount(float lostAmount) {
        this.lostAmount = lostAmount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
