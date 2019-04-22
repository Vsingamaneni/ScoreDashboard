package com.sports.cricket.model;

import java.util.HashMap;
import java.util.Map;

public class StatsDetails {

    public String name;

    public int defaultCount;

    public int winCount;

    public int lossCount;

    public float wonAmount;

    public float lossAmount;

    public Map<String, StatsCount> userStats;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(int defaultCount) {
        this.defaultCount = defaultCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public float getWonAmount() {
        return wonAmount;
    }

    public void setWonAmount(float wonAmount) {
        this.wonAmount = wonAmount;
    }

    public float getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(float lossAmount) {
        this.lossAmount = lossAmount;
    }

    public Map<String, StatsCount> getUserStats() {
        return userStats;
    }

    public void setUserStats(Map<String, StatsCount> userStats) {
        this.userStats = userStats;
    }
}
