package com.sports.cricket.util;

import com.sports.cricket.model.MatchDetails;
import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

public class PredictionListMapper {

    public static List<Prediction> predictionList(JdbcTemplate jdbcTemplate, String sql, int matchDay) {
        List<Prediction> predictionList = jdbcTemplate.query(sql, new Object[]{matchDay}, rs -> {

            List<Prediction> predictionList1 = new ArrayList<>();
            while (rs.next()) {
                Prediction prediction = new Prediction();

                prediction.setPredictionId(rs.getInt("predictionId"));
                prediction.setMemberId(rs.getInt("memberId"));
                prediction.setMatchNumber(rs.getInt("matchNumber"));
                prediction.setHomeTeam(rs.getString("homeTeam"));
                prediction.setAwayTeam(rs.getString("awayTeam"));
                prediction.setFirstName(rs.getString("firstName"));
                prediction.setSelected(rs.getString("selected"));
                prediction.setPredictedTime(rs.getString("predictedTime"));

                predictionList1.add(prediction);
            }
            return predictionList1;
        });
        return predictionList;
    }

    public static List<MatchDetails> matchDetails(List<Prediction> predictionList){
        HashMap<String, Integer> hmap = new HashMap<>();
        StringBuffer stringBuffer;
        if (!CollectionUtils.isEmpty(predictionList)){
            for (Prediction prediction : predictionList){
                stringBuffer = new StringBuffer();
                stringBuffer.append(prediction.getHomeTeam())
                        .append(" vs ")
                        .append(prediction.getAwayTeam());
                if (hmap.containsKey(stringBuffer.toString())){
                    hmap.put(stringBuffer.toString(), hmap.get((stringBuffer.toString()) + 1));
                } else {
                    hmap.put(stringBuffer.toString(), 1);
                }
            }
        }
        return UserListMapper.mapFromHashMap(hmap);
    }

    public static List<MatchDetails> getNewsFeed(List<Schedule> scheduleList, int matchDay){
        List<Schedule> schedules = new ArrayList<>();

        for (Schedule schedule : scheduleList){
            if (schedule.getMatchDay() == matchDay){
                schedules.add(schedule);
            } else if (schedule.getMatchDay() == matchDay-1){
                schedules.add(schedule);
            }
        }

        List<MatchDetails> matchDetailsList = null;
        try {
            matchDetailsList = generateNewsFeed(schedules);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return matchDetailsList;
    }


    /*
     * get schedule deadlines
     * get match result updated
     * get standings updated
     */
    public static List<MatchDetails> generateNewsFeed(List<Schedule> scheduleList) throws ParseException {
        List<MatchDetails> matchDetailsList = new ArrayList<>();
        MatchDetails matchDetails;
        for (Schedule schedule : scheduleList){
            if (null == schedule.getWinner()){
                if (ValidateDeadline.isDeadLineReached(schedule.getStartDate())){
                    matchDetails = new MatchDetails();
                    matchDetails.setMatch("Predictions for "+ schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is now closed");
                    matchDetailsList.add(matchDetails);
                } else {
                    matchDetails = new MatchDetails();
                    matchDetails.setMatch("Predictions for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is open");
                    matchDetailsList.add(matchDetails);
                }
            } else {

                matchDetails = new MatchDetails();
                matchDetails.setMatch("Predictions for "+ schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is now closed");
                matchDetailsList.add(matchDetails);

                matchDetails = new MatchDetails();
                matchDetails.setMatch("Result for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is updated");
                matchDetailsList.add(matchDetails);

            }
        }
        return matchDetailsList;
    }
}
