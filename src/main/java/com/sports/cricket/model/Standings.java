package com.sports.cricket.model;

public class Standings {

    private Integer id;

    private Integer memberId;

    private Integer matchNumber;

    private String homeTeam;

    private String awayTeam;

    private String matchDate;

    private String predictedDate;

    private String selected;

    private String winner;

    private double wonAmount;

    private double lostAmount;

    private double netAmount;

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

    public double getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(double wonAmount) {
        this.wonAmount = wonAmount;
    }

    public double getLostAmount() {
        return lostAmount;
    }

    public void setLostAmount(double lostAmount) {
        this.lostAmount = lostAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }
}
