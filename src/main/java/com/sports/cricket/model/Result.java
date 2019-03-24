package com.sports.cricket.model;

import java.io.Serializable;

public class Result implements Serializable {

    private Integer matchNumber;

    private String homeTeam;

    private String awayTeam;

    private String startDate;

    private String winner;

    private float winningAmount;

    private float homeTeamCount;

    private float awayTeamCount;

    private float drawTeamCount;

    private float notPredictedCount;

    private Integer matchDay;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public float getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(float winningAmount) {
        this.winningAmount = winningAmount;
    }

    public float getHomeTeamCount() {
        return homeTeamCount;
    }

    public void setHomeTeamCount(float homeTeamCount) {
        this.homeTeamCount = homeTeamCount;
    }

    public float getAwayTeamCount() {
        return awayTeamCount;
    }

    public void setAwayTeamCount(float awayTeamCount) {
        this.awayTeamCount = awayTeamCount;
    }

    public float getDrawTeamCount() {
        return drawTeamCount;
    }

    public void setDrawTeamCount(float drawTeamCount) {
        this.drawTeamCount = drawTeamCount;
    }

    public float getNotPredictedCount() {
        return notPredictedCount;
    }

    public void setNotPredictedCount(float notPredictedCount) {
        this.notPredictedCount = notPredictedCount;
    }

    public Integer getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(Integer matchDay) {
        this.matchDay = matchDay;
    }
}
