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
                prediction.setAmount(rs.getInt("amount"));

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
                prediction.setAmount(rs.getInt("amount"));

                predictions.add(prediction);
            }
            return predictions;
        });
        return predictionList;
    }

    public static List<MatchDetails> matchDetails(List<Prediction> predictionList) {
        HashMap<String, Integer> hmap = new HashMap<>();
        StringBuffer stringBuffer;
        if (!CollectionUtils.isEmpty(predictionList)) {
            for (Prediction prediction : predictionList) {
                stringBuffer = new StringBuffer();
                stringBuffer.append(prediction.getHomeTeam())
                        .append(" vs ")
                        .append(prediction.getAwayTeam());
                if (hmap.containsKey(stringBuffer.toString())) {
                    hmap.put(stringBuffer.toString(), (hmap.get(stringBuffer.toString()) + 1));
                } else {
                    hmap.put(stringBuffer.toString(), 1);
                }
            }
        }
        return UserListMapper.mapFromHashMap(hmap);
    }

    public static List<MatchDetails> getNewsFeed(List<Schedule> scheduleList, int matchDay) {
        List<Schedule> schedules = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            if (schedule.getMatchDay() == matchDay) {
                schedules.add(schedule);
            } else if (schedule.getMatchDay() == matchDay - 1) {
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
        for (Schedule schedule : scheduleList) {
            if (null == schedule.getWinner()) {
                if (ValidateDeadline.isDeadLineReached(schedule.getStartDate())) {
                    matchDetails = new MatchDetails();
                    matchDetails.setMatch("Predictions for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is now closed");
                    matchDetailsList.add(matchDetails);
                } else {
                    matchDetails = new MatchDetails();
                    matchDetails.setMatch("Predictions for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is open");
                    matchDetailsList.add(matchDetails);
                }
            } else {

                matchDetails = new MatchDetails();
                matchDetails.setMatch("Predictions for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is now closed");
                matchDetailsList.add(matchDetails);

                matchDetails = new MatchDetails();
                matchDetails.setMatch("Result for " + schedule.getHomeTeam() + " vs " + schedule.getAwayTeam() + " is updated");
                matchDetailsList.add(matchDetails);

            }
        }
        return matchDetailsList;
    }

    public static List<Prediction> adminPredictions(ScheduleService scheduleService, int memberId, int matchday) {

        List<Prediction> adminPredictions = scheduleService.getAdminPrediction(memberId, matchday);

        return adminPredictions;
    }

    public static int getAdminId(RegistrationService registrationService) {
        int adminId = 0;

        List<Register> registerList = registrationService.getAllUsers();
        for (Register register : registerList) {
            if (!register.getRole().equalsIgnoreCase("admin")) {
                continue;
            }
            adminId = register.getMemberId();
        }

        return adminId;
    }

    public static int getActiveMatchDay(ScheduleService scheduleService) {
        int matchDay = 0;

        List<Schedule> scheduleList = scheduleService.findAll();
        for (Schedule schedule : scheduleList) {
            if (!schedule.isIsactive()) {
                continue;
            }
            matchDay = schedule.getMatchDay();
        }
        return matchDay;
    }

    public static List<Prediction> sortPredictions(List<Prediction> predictionList) {
        Collections.sort(predictionList, new MatchDayPredictions());
        return predictionList;
    }

    public static List<Prediction> mapPredictionPerSelection(List<Prediction> predictionList, Schedule schedule) {
        List<Prediction> predictions = new ArrayList<>();
        List<Prediction> homeTeamPredictions = new ArrayList<>();
        List<Prediction> awayTeamPredictions = new ArrayList<>();
        List<Prediction> defaultList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(predictionList) && predictionList.size() > 0) {
            predictionList.stream().forEach(p -> {
                if (p.getSelected().equals(schedule.getHomeTeam())) {
                    homeTeamPredictions.add(p);
                } else if (p.getSelected().equals(schedule.getAwayTeam())) {
                    awayTeamPredictions.add(p);
                } else {
                    defaultList.add(p);
                }
            });
        }

        Collections.sort(homeTeamPredictions, new VariablePredictions());
        predictions.addAll(homeTeamPredictions);

        Collections.sort(awayTeamPredictions, new VariablePredictions());
        predictions.addAll(awayTeamPredictions);

        predictions.addAll(defaultList);

        return predictions;
    }

    static class MatchDayPredictions implements Comparator<Prediction> {

        @Override
        public int compare(Prediction p1, Prediction p2) {
            return p2.getFirstName().compareTo(p1.getFirstName());
        }
    }

    static class VariablePredictions implements Comparator<Prediction> {

        @Override
        public int compare(Prediction p1, Prediction p2) {
            return p2.getAmount().compareTo(p1.getAmount());
        }
    }

    public static void setMatchFeeList(Schedule schedule) {
        List<Integer> matchList = new ArrayList<>();
        if (null != schedule.getMaxAmount()) {
            int matchFee = schedule.getMatchFee();
            for (int fee = matchFee; fee <= schedule.getMaxAmount(); fee += 50) {
                matchList.add(fee);
            }
        }
        schedule.setMatchFeeList(matchList);
    }

    public static Prediction getUserPredictions(List<SchedulePrediction> schedulePredictions, UserLogin userLogin) {
        if (null != schedulePredictions && schedulePredictions.size() > 0) {
            SchedulePrediction schedulePrediction = schedulePredictions.get(0);
            List<Prediction> predictionList = schedulePrediction.getPrediction();
            if (null != predictionList && predictionList.size() > 0) {
                for (Prediction prediction : predictionList) {
                    if (prediction.getMemberId() == userLogin.getMemberId()) {
                        return prediction;
                    }
                }
            }
        }
        return null;
    }

    public static SchedulePrediction mapCountTotals(SchedulePrediction schedulePrediction) {
        try {
            HashMap<String, List<CountTotal>> countMap = new HashMap<>();

            List<Prediction> predictionList = schedulePrediction.getPrediction();
            if (null != predictionList && predictionList.size() > 0) {
                for (Prediction prediction : predictionList) {
                    if (prediction.getSelected().equals(prediction.getHomeTeam()) || prediction.getSelected().equals(prediction.getAwayTeam())) {
                        if (countMap.containsKey(prediction.getSelected())) {
                            List<CountTotal> totals = countMap.get(prediction.getSelected());
                            if (null != totals) {
                                boolean isFound = false;
                                for (CountTotal total : totals) {
                                    if (total.getAmount() == prediction.getAmount()) {
                                        isFound = true;
                                        total.setCount(total.getCount() + 1);
                                        total.setExpected(prediction.getExpectedAmount());
                                    }
                                }
                                if (!isFound) {
                                    CountTotal newCount = new CountTotal();
                                    newCount.setAmount(prediction.getAmount());
                                    newCount.setCount(1);
                                    newCount.setExpected(prediction.getExpectedAmount());
                                    totals.add(newCount);
                                }
                            }
                        } else {
                            CountTotal newCount = new CountTotal();
                            newCount.setAmount(prediction.getAmount());
                            newCount.setCount(1);
                            newCount.setExpected(prediction.getExpectedAmount());
                            List<CountTotal> countTotals = new ArrayList<>();
                            countTotals.add(newCount);
                            countMap.putIfAbsent(prediction.getSelected(), countTotals);
                        }
                    }
                }
            }

            Schedule schedule = schedulePrediction.getSchedule();

            if (countMap.containsKey(schedule.getHomeTeam())) {
                schedulePrediction.setHomeTotal(countMap.get(schedule.getHomeTeam()));
                Collections.sort(schedulePrediction.getHomeTotal(), new PredictionCount());
            }

            if (countMap.containsKey(schedule.getAwayTeam())) {
                schedulePrediction.setAwayTotal(countMap.get(schedule.getAwayTeam()));
                Collections.sort(schedulePrediction.getAwayTotal(), new PredictionCount());
            }

        } catch (Exception exception) {
            System.out.println("Unable to process totals");
        }
        return schedulePrediction;
    }

    static class PredictionCount implements Comparator<CountTotal> {

        @Override
        public int compare(CountTotal l1, CountTotal l2) {
            if(l1.getAmount() < l2.getAmount()){
                return 1;
            } else {
                return -1;
            }
        }
    }
}
