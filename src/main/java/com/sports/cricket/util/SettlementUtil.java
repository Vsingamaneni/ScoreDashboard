package com.sports.cricket.util;

import com.sports.cricket.model.Register;
import com.sports.cricket.model.Settlement;
import com.sports.cricket.model.TrackSettlement;
import com.sports.cricket.model.UserLogin;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SettlementUtil {

    public static TrackSettlement parseSettlementDetails(TrackSettlement trackSettlement, List<Register> allUsers){

        int length = trackSettlement.getToDetails().indexOf("-");
        String memberId = trackSettlement.getToDetails().substring(0,length).trim();
        trackSettlement.setMemberId(Integer.parseInt(memberId));
        trackSettlement.setName(getName(trackSettlement.getMemberId(), allUsers));

        int settledLength = trackSettlement.getFromDetails().indexOf("-");
        String settledMemberId = trackSettlement.getFromDetails().substring(0,settledLength).trim();
        trackSettlement.setSettledMemberId(Integer.parseInt(settledMemberId));
        trackSettlement.setSettledName(getName(trackSettlement.getSettledMemberId(), allUsers));

        trackSettlement.setSettledTime(OffsetDateTime.now().toString());

        return trackSettlement;
    }

    public static List<Settlement> mapSettlement(TrackSettlement trackSettlement, Settlement toSettlement, Settlement fromSettlement){
        List<Settlement> settlementList = new ArrayList<>();
        toSettlement.setNet(toSettlement.getNet() + trackSettlement.getSettledAmount());
        fromSettlement.setNet(fromSettlement.getNet() + (trackSettlement.getSettledAmount() * -1));

        settlementList.add(toSettlement);
        settlementList.add(fromSettlement);

        return settlementList;
    }

    public static void setStatus(List<Settlement> settlementList){
        if (!CollectionUtils.isEmpty(settlementList)){
            for (Settlement settlement : settlementList){
                if (settlement.getNet() == 0.0){
                    settlement.setStatus("SETTLED");
                } else {
                    settlement.setStatus("IN_PROGRESS");
                }
            }
        }
    }

    public static List<TrackSettlement> mySettlementHistory(List<TrackSettlement> trackSettlementList, UserLogin userLogin){
        List<TrackSettlement> memberSettlementHistory = new ArrayList<>();
        if (!CollectionUtils.isEmpty(trackSettlementList)){
            for (TrackSettlement trackSettlement : trackSettlementList){
                if (userLogin.getMemberId() == trackSettlement.getMemberId() || userLogin.getMemberId() == trackSettlement.getSettledMemberId()){
                    memberSettlementHistory.add(trackSettlement);
                }
            }
        }
        return memberSettlementHistory;
    }

    public static String getName(int memberId, List<Register> allUsers){
        for (Register register : allUsers){
            if (register.getMemberId() != memberId){
                continue;
            }
            return register.getfName() + " " + register.getlName();
        }
        return "";
    }
}
