package com.sports.cricket.model;

import java.io.Serializable;

public class UserReview implements Serializable {

    private String reviewRating;

    private String interested;

    private String improve;

    private String ideas;

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getImprove() {
        return improve;
    }

    public void setImprove(String improve) {
        this.improve = improve;
    }

    public String getIdeas() {
        return ideas;
    }

    public void setIdeas(String ideas) {
        this.ideas = ideas;
    }
}
