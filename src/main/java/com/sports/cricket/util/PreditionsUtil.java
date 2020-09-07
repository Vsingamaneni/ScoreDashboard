package com.sports.cricket.util;

import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Schedule;
import com.sports.cricket.model.SchedulePrediction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PreditionsUtil {

    public static float getHomeTeamTotal(List<Prediction> predictionList, Schedule schedule){
        int homeTotal = 0;
        for (Prediction prediction : predictionList){
            if (prediction.getSelected().equals(schedule.getHomeTeam())){
                homeTotal = homeTotal+ prediction.getAmount();
            }
        }
        return homeTotal;
    }

    public static float getAwayTeamTotal(List<Prediction> predictionList, Schedule schedule){
        int awayTotal = 0;
        for (Prediction prediction : predictionList){
            if (prediction.getSelected().equals(schedule.getAwayTeam())){
                awayTotal = awayTotal + prediction.getAmount();
            }
        }
        return awayTotal;
    }

    public static void mapExpectedWinTotal(List<Prediction> predictions, SchedulePrediction schedulePrediction){
        float expected;
        for (Prediction prediction : predictions){
            if (!prediction.getSelected().equals("default")) {
                float predictedAmount = Float.valueOf(String.format("%.2f", (prediction.getAmount() - (prediction.getAmount() * .05))));
                if (prediction.getSelected().equals(prediction.getHomeTeam())) {
                    expected = (Float.valueOf(String.format("%.2f", ((predictedAmount) / (schedulePrediction.getHomeWinAmount() - schedulePrediction.getDefaultQuota())) * schedulePrediction.getAwayWinAmount())));
                } else if (prediction.getSelected().equals(prediction.getAwayTeam())) {
                    expected = (Float.valueOf(String.format("%.2f", ((predictedAmount) / (schedulePrediction.getAwayWinAmount() - schedulePrediction.getDefaultQuota())) * schedulePrediction.getHomeWinAmount())));
                } else {
                    expected = 0;
                }
                prediction.setExpectedAmount(expected);
            }
        }
        Collections.sort(predictions, new PredictionsComp());
    }

    static class PredictionsComp implements Comparator<Prediction> {

        @Override
        public int compare(Prediction l1, Prediction l2) {
            if(l1.getExpectedAmount() < l2.getExpectedAmount()){
                return 1;
            } else {
                return -1;
            }
        }
    }
}
