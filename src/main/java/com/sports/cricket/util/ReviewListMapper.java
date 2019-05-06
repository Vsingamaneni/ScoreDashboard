package com.sports.cricket.util;

import com.sports.cricket.model.Review;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ReviewListMapper {

    private static final String FIVE_STAR = "Very Satisfied (5)";
    private static final String FOUR_STAR = "Satisfied (4)";
    private static final String THREE_STAR = "Average (3)";
    private static final String TWO_STAR = "Unsatisfied (2)";
    private static final String ONE_STAR = "Very Unsatisfied (1)";

    public static Review mapReviews(List<Review> reviewList){

        Review reviewCount = new Review();

        if (!CollectionUtils.isEmpty(reviewList)){
            for (Review review : reviewList){
                switch (review.getFeedback()){
                    case FIVE_STAR:
                        reviewCount.setFiveStar(reviewCount.getFiveStar()+1);
                        break;
                    case FOUR_STAR:
                        reviewCount.setFourStar(reviewCount.getFourStar()+1);
                        break;
                    case THREE_STAR:
                        reviewCount.setThreeStar(reviewCount.getThreeStar()+1);
                        break;
                    case TWO_STAR:
                        reviewCount.setTwoStar(reviewCount.getTwoStar()+1);
                        break;
                    case ONE_STAR:
                        reviewCount.setOneStar(reviewCount.getOneStar()+1);
                        break;
                     default:
                         break;
                }
            }

            reviewCount.setTotalCount(reviewList.size());

            float overAllRating = ((reviewCount.getFiveStar() * 5) + (reviewCount.getFourStar() * 4 ) + (reviewCount.getThreeStar() * 3) +
                    (reviewCount.getTwoStar() * 2) + (reviewCount.getOneStar() * 1))/ reviewCount.getTotalCount();


            reviewCount.setOverall(BigDecimal.valueOf(overAllRating).setScale(1, RoundingMode.UP).floatValue());

            reviewCount.setFivePercent(BigDecimal.valueOf((reviewCount.getFiveStar()/reviewList.size()) * 100).setScale(1, RoundingMode.UP).floatValue());
            reviewCount.setFourPercent(BigDecimal.valueOf((reviewCount.getFourStar()/reviewList.size()) * 100).setScale(1, RoundingMode.UP).floatValue());
            reviewCount.setThreePercent(BigDecimal.valueOf((reviewCount.getThreeStar()/reviewList.size()) * 100).setScale(1, RoundingMode.UP).floatValue());
            reviewCount.setTwoPercent(BigDecimal.valueOf((reviewCount.getTwoStar()/reviewList.size()) * 100).setScale(1, RoundingMode.UP).floatValue());
            reviewCount.setOnePercent(BigDecimal.valueOf((reviewCount.getOneStar()/reviewList.size()) * 100).setScale(1, RoundingMode.UP).floatValue());

            reviewCount.setOverallPercent((reviewCount.getOverall()/5) * 100);
        }
        return reviewCount;
    }

    public static Review countInterested(List<Review> reviewsList){
        Review review = new Review();

        if (!CollectionUtils.isEmpty(reviewsList)){
            for (Review userReview : reviewsList){
                if (userReview.getInterested().equalsIgnoreCase("yes")){
                    review.setYes(review.getYes()+1);
                } else {
                    review.setNo(review.getNo()+1);
                }
            }
        }

        return review;
    }

    public static void setPreview(List<Review> reviewList){
        if (!CollectionUtils.isEmpty(reviewList)){
            for (Review review : reviewList){
                if (!StringUtils.isEmpty(review.getImprovements()) || !StringUtils.isEmpty(review.getIdeas())){
                    review.setShowResponse(true);
                }
            }
        }
    }
}
