package com.sports.cricket.model;

import java.io.Serializable;
import java.util.List;

public class SchedulePrediction implements Serializable {

    private Schedule schedule;

    private List<Prediction> prediction;

    private float homeTeamCount;

    private float awayTeamCount;

    private float drawTeamCount;

    private float notPredicted;

    private float homeWinAmount;

    private float awayWinAmount;

    private float drawWinAmount;

    private boolean isDeadlinReached;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Prediction> getPrediction() {
        return prediction;
    }

    public void setPrediction(List<Prediction> prediction) {
        this.prediction = prediction;
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

    public float getNotPredicted() {
        return notPredicted;
    }

    public void setNotPredicted(float notPredicted) {
        this.notPredicted = notPredicted;
    }

    public float getHomeWinAmount() {
        return homeWinAmount;
    }

    public void setHomeWinAmount(float homeWinAmount) {
        this.homeWinAmount = homeWinAmount;
    }

    public float getAwayWinAmount() {
        return awayWinAmount;
    }

    public void setAwayWinAmount(float awayWinAmount) {
        this.awayWinAmount = awayWinAmount;
    }

    public float getDrawWinAmount() {
        return drawWinAmount;
    }

    public void setDrawWinAmount(float drawWinAmount) {
        this.drawWinAmount = drawWinAmount;
    }

    public boolean isDeadlinReached() {
        return isDeadlinReached;
    }

    public void setDeadlinReached(boolean deadlinReached) {
        isDeadlinReached = deadlinReached;
    }
}
