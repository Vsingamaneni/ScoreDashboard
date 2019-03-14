package com.sports.cricket.model;

import java.io.Serializable;

public class Prediction implements Serializable {

    private Integer predictionId;

    private Integer memberId;

    private Integer matchNumber;

    private String homeTeam;

    private String awayTeam;

    private String firstName;

    private String selected;

    private String predictedTime;

    private boolean canPredict;

    private Integer matchDay;

    public Integer getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(Integer predictionId) {
        this.predictionId = predictionId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getPredictedTime() {
        return predictedTime;
    }

    public void setPredictedTime(String predictedTime) {
        this.predictedTime = predictedTime;
    }

    public boolean isCanPredict() {
        return canPredict;
    }

    public void setCanPredict(boolean canPredict) {
        this.canPredict = canPredict;
    }

    public Integer getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(Integer matchDay) {
        this.matchDay = matchDay;
    }
}
