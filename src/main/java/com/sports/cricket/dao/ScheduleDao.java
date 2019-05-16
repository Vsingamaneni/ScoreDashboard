package com.sports.cricket.dao;

import com.sports.cricket.model.*;

import java.util.List;

public interface ScheduleDao {

    List<Schedule> findAll();

    List<Schedule> scheduleList();

    Schedule findById(Integer matchNumber);

    List<Prediction> findPredictions(Integer memberId);

    boolean savePrediction(Prediction prediction);

    boolean updatePrediction(Prediction prediction);

    Prediction getPrediction(Integer predictionId, Integer matchId);

    Prediction getPrediction(Integer predictionId);

    List<Prediction> getPredictionByMatchDay(Integer matchDay);

    boolean deletePrediction(Integer predictionId);

    boolean authorizeMember(Integer memberID);

    boolean deactivateMember(Integer memberID);

    List<Prediction> getPredictionsByMatch(Integer matchId);

    boolean updateMatchResult(Schedule schedule);

    Integer totalMatches(Integer matchDay);

    boolean updateMatchDay(Integer matchDay);

    boolean addResult(Result result);

    boolean insertPredictions(List<Standings> standingsList);

    List<Standings> getLeaderBoard();

    List<Settlement> getSettlement();

    List<TrackSettlement> getSettlementsTrack();

    Settlement getSettlement(Integer memberId);

    boolean updateSettlement(List<Settlement> settlementList);

    boolean addSettlement(TrackSettlement trackSettlement);

    List<Schedule> getScheduleByMatchDay(Integer matchDay);

    List<Prediction> getAdminPrediction(Integer memberId, Integer matchDay);
}
