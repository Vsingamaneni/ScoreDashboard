package com.sports.cricket.dao;

import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Result;
import com.sports.cricket.model.Schedule;
import com.sports.cricket.model.Standings;

import java.util.List;

public interface ScheduleDao {

    List<Schedule> findAll();

    List<Schedule> scheduleList();

    Schedule findById(Integer matchNumber);

    List<Prediction> findPredictions(Integer memberId);

    boolean savePrediction(Prediction prediction);

    boolean updatePrediction(Prediction prediction);

    Prediction getPrediction(Integer predictionId, Integer matchId);

    boolean deletePrediction(Integer predictionId);

    boolean authorizeMember(Integer memberID);

    List<Prediction> getPredictionsByMatch(Integer matchId);

    boolean updateMatchResult(Schedule schedule);

    Integer totalMatches(Integer matchDay);

    boolean updateMatchDay(Integer matchDay);

    boolean addResult(Result result);

    boolean insertPredictions(List<Standings> standingsList);

    List<Standings> getLeaderBoard();

    List<Schedule> getScheduleByMatchDay(Integer matchDay);
}
