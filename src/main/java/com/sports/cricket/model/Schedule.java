package com.sports.cricket.model;

public class Schedule {

    private Integer lineNumber;

    private Integer matchNumber;

    private String homeTeam;

    private String awayTeam;

    private String startDate;

    private String winner;

    private Integer possibleResult;

    private boolean isactive;

    private Integer matchFee;

    private boolean canPredict;

    private Integer matchDay;

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
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

    public Integer getPossibleResult() {
        return possibleResult;
    }

    public void setPossibleResult(Integer possibleResult) {
        this.possibleResult = possibleResult;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }


    public Integer getMatchFee() {
        return matchFee;
    }

    public void setMatchFee(Integer matchFee) {
        this.matchFee = matchFee;
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
