package com.sports.cricket.model;

import java.io.Serializable;

public class MatchDetails implements Serializable {

    private String match;

    private int count;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
