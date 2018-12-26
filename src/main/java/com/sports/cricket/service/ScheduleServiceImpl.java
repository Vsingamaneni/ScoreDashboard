package com.sports.cricket.service;

import com.sports.cricket.dao.ScheduleDao;
import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Result;
import com.sports.cricket.model.Schedule;
import com.sports.cricket.model.Standings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    ScheduleDao scheduleDao;

    @Autowired
    public void setScheduleDao(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDao.findAll();
    }

    @Override
    public List<Schedule> scheduleList() {
        return scheduleDao.scheduleList();
    }

    @Override
    public Schedule findById(Integer matchNumber) {
        return scheduleDao.findById(matchNumber);
    }

    @Override
    public List<Prediction> findPredictions(Integer memberId) {
        return scheduleDao.findPredictions(memberId);
    }

    @Override
    public boolean savePrediction(Prediction prediction) {
        return scheduleDao.savePrediction(prediction);
    }

    @Override
    public boolean updatePrediction(Prediction prediction) {
        return scheduleDao.updatePrediction(prediction);
    }

    @Override
    public Prediction getPrediction(Integer predictionId, Integer matchId) {
        return scheduleDao.getPrediction(predictionId, matchId);
    }

    @Override
    public boolean deletePrediction(Integer predictionId) {
        return scheduleDao.deletePrediction(predictionId);
    }

    @Override
    public boolean authorizeMember(Integer memberId) {
        return scheduleDao.authorizeMember(memberId);
    }

    @Override
    public List<Prediction> getPredictionsByMatch(Integer matchId) {
        return scheduleDao.getPredictionsByMatch(matchId);
    }

    @Override
    public boolean updateMatchResult(Schedule schedule) {
        return scheduleDao.updateMatchResult(schedule);
    }

    @Override
    public Integer totalMatches(Integer matchDay) {
        return scheduleDao.totalMatches(matchDay);
    }

    @Override
    public boolean updateMatchDay(Integer matchDay) {
        return scheduleDao.updateMatchDay(matchDay);
    }

    @Override
    public boolean addResult(Result result) {
        return scheduleDao.addResult(result);
    }

    @Override
    public boolean insertPredictions(List<Standings> standingsList) {
       return scheduleDao.insertPredictions(standingsList);
    }

    @Override
    public List<Standings> getLeaderBoard() {
        return scheduleDao.getLeaderBoard();
    }

    @Override
    public List<Schedule> getScheduleByMatchDay(Integer matchDay) {
        return scheduleDao.getScheduleByMatchDay(matchDay);
    }

}
