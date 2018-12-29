package com.sports.cricket.util;

import com.sports.cricket.model.MatchDetails;
import com.sports.cricket.model.Register;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class UserListMapper {

    public static List<MatchDetails> getUsersList(List<Register> registerList){
        List<MatchDetails> matchUpdates = new ArrayList<>();
        int active = 0;
        int inactive = 0;
        if (!CollectionUtils.isEmpty(registerList)){
            for (Register register : registerList){
                if (register.getIsActive().equalsIgnoreCase("Y")){
                    active++;
                }else{
                    inactive++;
                }
            }
        }
        MatchDetails activeUsers = new MatchDetails();
        activeUsers.setMatch("active");
        activeUsers.setCount(active);
        matchUpdates.add(activeUsers);

        MatchDetails inactiveUsers = new MatchDetails();
        inactiveUsers.setMatch("inactive");
        inactiveUsers.setCount(inactive);
        matchUpdates.add(inactiveUsers);

        return matchUpdates;
    }
}
