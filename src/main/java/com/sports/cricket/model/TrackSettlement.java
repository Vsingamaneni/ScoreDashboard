package com.sports.cricket.model;

public class TrackSettlement {

    private int memberId;

    private String name;

    private int settledMemberId;

    private String settledName;

    private float settledAmount;

    private String toDetails;

    private String fromDetails;

    private String settledTime;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSettledMemberId() {
        return settledMemberId;
    }

    public void setSettledMemberId(int settledMemberId) {
        this.settledMemberId = settledMemberId;
    }

    public String getSettledName() {
        return settledName;
    }

    public void setSettledName(String settledName) {
        this.settledName = settledName;
    }

    public float getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(float settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getToDetails() {
        return toDetails;
    }

    public void setToDetails(String toDetails) {
        this.toDetails = toDetails;
    }

    public String getFromDetails() {
        return fromDetails;
    }

    public void setFromDetails(String fromDetails) {
        this.fromDetails = fromDetails;
    }

    public String getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(String settledTime) {
        this.settledTime = settledTime;
    }
}
