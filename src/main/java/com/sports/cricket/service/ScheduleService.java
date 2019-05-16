package com.sports.cricket.service;

import com.sports.cricket.model.*;

import java.util.List;

public interface ScheduleService {

    List<Schedule> findAll();

    List<Schedule> scheduleList();

    Schedule findById(Integer matchNumber);

    List<Prediction> findPredictions(Integer memberId);

    boolean savePrediction(Prediction prediction);

    boolean updatePrediction(Prediction prediction);

    Prediction getPrediction(Integer predictionId, Integer matchId);

    Prediction getPrediction(Integer predictionId);

    boolean deletePrediction(Integer predictionId);

    boolean authorizeMember(Integer memberId);

    boolean deactivateMember(Integer memberId);

    List<Prediction> getPredictionsByMatch(Integer matchId);

    List<Prediction> getPredictionsByMatchDay(Integer matchDay);

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
