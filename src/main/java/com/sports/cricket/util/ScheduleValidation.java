package com.sports.cricket.util;

import com.sports.cricket.model.Schedule;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

public class ScheduleValidation implements Serializable {

    public static int getMatchDay(List<Schedule> scheduleList){
        if (!CollectionUtils.isEmpty(scheduleList)){
            for(Schedule schedule : scheduleList){
                if (!schedule.isIsactive()){
                    continue;
                }
                return schedule.getMatchDay();
            }
        }
        return 0;
    }
}
