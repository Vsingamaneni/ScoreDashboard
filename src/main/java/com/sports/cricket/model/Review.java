package com.sports.cricket.model;

import java.io.Serializable;

public class Review implements Serializable {

    private int memberId;

    private String name;

    private String feedback;

    private String interested;

    private String improvements;

    private String ideas;

    private float fiveStar;

    private float fourStar;

    private float threeStar;

    private float twoStar;

    private float oneStar;

    private float overall;

    private float overallPercent;

    private float fivePercent;

    private float fourPercent;

    private float threePercent;

    private float twoPercent;

    private float onePercent;

    private float totalCount;

    private int yes;

    private int no;

    private boolean showResponse;

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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getIdeas() {
        return ideas;
    }

    public void setIdeas(String ideas) {
        this.ideas = ideas;
    }

    public float getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(float fiveStar) {
        this.fiveStar = fiveStar;
    }

    public float getFourStar() {
        return fourStar;
    }

    public void setFourStar(float fourStar) {
        this.fourStar = fourStar;
    }

    public float getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(float threeStar) {
        this.threeStar = threeStar;
    }

    public float getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(float twoStar) {
        this.twoStar = twoStar;
    }

    public float getOneStar() {
        return oneStar;
    }

    public void setOneStar(float oneStar) {
        this.oneStar = oneStar;
    }

    public float getOverall() {
        return overall;
    }

    public void setOverall(float overall) {
        this.overall = overall;
    }

    public float getFivePercent() {
        return fivePercent;
    }

    public void setFivePercent(float fivePercent) {
        this.fivePercent = fivePercent;
    }

    public float getFourPercent() {
        return fourPercent;
    }

    public void setFourPercent(float fourPercent) {
        this.fourPercent = fourPercent;
    }

    public float getThreePercent() {
        return threePercent;
    }

    public void setThreePercent(float threePercent) {
        this.threePercent = threePercent;
    }

    public float getTwoPercent() {
        return twoPercent;
    }

    public void setTwoPercent(float twoPercent) {
        this.twoPercent = twoPercent;
    }

    public float getOnePercent() {
        return onePercent;
    }

    public void setOnePercent(float onePercent) {
        this.onePercent = onePercent;
    }

    public void setOnePercent(int onePercent) {
        this.onePercent = onePercent;
    }

    public float getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(float totalCount) {
        this.totalCount = totalCount;
    }

    public float getOverallPercent() {
        return overallPercent;
    }

    public void setOverallPercent(float overallPercent) {
        this.overallPercent = overallPercent;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public boolean isShowResponse() {
        return showResponse;
    }

    public void setShowResponse(boolean showResponse) {
        this.showResponse = showResponse;
    }
}
