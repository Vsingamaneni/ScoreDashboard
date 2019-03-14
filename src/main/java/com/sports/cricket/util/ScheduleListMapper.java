package com.sports.cricket.util;

import com.sports.cricket.model.Schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListMapper implements Serializable {

    public static List<Schedule> mapScheduleStauts(List<Schedule> scheduleList){

        List<Schedule> updatedStatus = new ArrayList<>();

        for(Schedule schedule : scheduleList){
            if (null != schedule.getWinner()){
                schedule.setStatus("COMPLETED");
            } else if (null == schedule.getWinner()
                    && schedule.isCanPredict()){
                schedule.setStatus("PREDICTIONS OPEN");
            } else if (null == schedule.getWinner()
                    && !schedule.isCanPredict()){
                schedule.setStatus("IN PROGRESS");
            }
            updatedStatus.add(schedule);
        }

        return updatedStatus;
    }
}
