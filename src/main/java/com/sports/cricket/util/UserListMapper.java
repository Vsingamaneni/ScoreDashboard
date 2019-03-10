package com.sports.cricket.util;

import com.sports.cricket.model.MatchDetails;
import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Register;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    public static List<MatchDetails> getGeoDetails(List<Register> registerList){
        HashMap<String, Integer> hmap = new HashMap<>();
        StringBuffer stringBuffer;
        if (!CollectionUtils.isEmpty(registerList)){
            for (Register register : registerList){
                stringBuffer = new StringBuffer();
                stringBuffer.append(register.getCountry());
                if (hmap.containsKey(stringBuffer.toString())){
                    hmap.put(stringBuffer.toString(), hmap.get(stringBuffer.toString()) + 1);
                } else {
                    hmap.put(stringBuffer.toString(), 1);
                }
            }
        }

        return mapFromHashMap(hmap);
    }

    public static List<MatchDetails> mapFromHashMap(HashMap<String, Integer> hmap) {
        List<MatchDetails> matchDetailsList = new ArrayList<>();
        Set set = hmap.entrySet();
        Iterator users = set.iterator();
        while (users.hasNext()) {
            MatchDetails userDetails = new MatchDetails();
            Map.Entry entry = (Map.Entry) users.next();
            userDetails.setMatch(entry.getKey().toString());
            userDetails.setCount(Integer.parseInt(entry.getValue().toString()));
            matchDetailsList.add(userDetails);
        }
        return matchDetailsList;
    }
}
