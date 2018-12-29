package com.sports.cricket.util;

import com.sports.cricket.model.MatchDetails;
import com.sports.cricket.model.Prediction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

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
        List<MatchDetails> matchDetailsList = new ArrayList<>();
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
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            MatchDetails matchDetails = new MatchDetails();
            Map.Entry mentry = (Map.Entry)iterator.next();
            matchDetails.setMatch(mentry.getKey().toString());
            matchDetails.setCount(Integer.parseInt(mentry.getValue().toString()));
            matchDetailsList.add(matchDetails);
        }
        return matchDetailsList;
    }
}
