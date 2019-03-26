package com.sports.cricket.util;

import com.sports.cricket.model.*;
import com.sports.cricket.service.RegistrationService;
import com.sports.cricket.service.ScheduleService;
import com.sports.cricket.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

public class PredictionListMapper implements Serializable {

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

    public static List<Prediction> adminPredictionList(JdbcTemplate jdbcTemplate, String sql) {
        List<Prediction> predictionList = jdbcTemplate.query(sql, new Object[]{}, rs -> {

            List<Prediction> predictions = new ArrayList<>();
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
                prediction.setMatchDay(rs.getInt("matchDay"));

                predictions.add(prediction);
            }
            return predictions;
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
                    hmap.put(stringBuffer.toString(), (hmap.get(stringBuffer.toString()) + 1));
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

    public static List<Prediction> adminPredictions(RegistrationService registrationService, ScheduleService scheduleService){

        int memberId = getAdminId(registrationService);

        int matchday = getActiveMatchDay(scheduleService);

        List<Prediction> adminPredictions = scheduleService.getAdminPrediction(memberId, matchday);

        return adminPredictions;
    }

    public static int getAdminId(RegistrationService registrationService){
        int adminId = 0;

        List<Register> registerList = registrationService.getAllUsers();
        for (Register register : registerList){
            if (!register.getRole().equalsIgnoreCase("admin")){
                continue;
            }
            adminId = register.getMemberId();
        }

        return adminId;
    }

    public static int getActiveMatchDay(ScheduleService scheduleService){
        int matchDay = 0;

        List<Schedule> scheduleList = scheduleService.findAll();
        for (Schedule schedule : scheduleList){
            if (!schedule.isIsactive()){
                continue;
            }
            matchDay = schedule.getMatchDay();
        }
        return matchDay;
    }

    public static List<Prediction> sortPredictions(List<Prediction> predictionList){
        Collections.sort(predictionList, new MatchDayPredictions());
        return predictionList;
    }

    static class MatchDayPredictions implements Comparator<Prediction> {

        @Override
        public int compare(Prediction p1, Prediction p2) {
            return p1.getFirstName().compareTo(p2.getFirstName());
        }
    }
}
