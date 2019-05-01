package com.sports.cricket.model;

import java.io.Serializable;

public class Standings implements Serializable {

    private Integer id;

    private Integer memberId;

    private Integer matchNumber;

    private String homeTeam;

    private String awayTeam;

    private String matchDate;

    private String predictedDate;

    private String selected;

    private String winner;

    private float wonAmount;

    private float lostAmount;

    private float netAmount;

    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getPredictedDate() {
        return predictedDate;
    }

    public void setPredictedDate(String predictedDate) {
        this.predictedDate = predictedDate;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public float getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(float netAmount) {
        this.netAmount = netAmount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
