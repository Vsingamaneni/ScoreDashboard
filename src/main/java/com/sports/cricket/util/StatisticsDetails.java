package com.sports.cricket.util;

import com.sports.cricket.model.*;

import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsDetails {

    public static List<StatsDetails> getDefaults(List<Standings> standingsList, List<Register> registerList){
        List<StatsDetails> defaultLists =  new ArrayList<>();
        StatsDetails defaultList;
        int count = 0;
        for (Register register : registerList){
            count = 0;
            for (Standings standings : standingsList){
                if (register.getMemberId() != standings.getMemberId()){
                    continue;
                }
                if (standings.getSelected().equals("default")){
                    count++;
                }
            }
            defaultList = new StatsDetails();
            defaultList.setName(register.getfName() + " " +register.getlName());
            defaultList.setDefaultCount(count);
            defaultLists.add(defaultList);
        }

        Collections.sort(defaultLists, DEFAULT_COUNT);

        if (defaultLists.size() > 10) {
            return defaultLists.subList(0, 10);
        } else {
            return defaultLists;
        }
    }

    public static List<StatsDetails> getWinsAndLosses(List<Standings> standingsList, List<Register> registerList){
        List<StatsDetails> winAndLosses =  new ArrayList<>();
        StatsDetails winAndLoss;
        int winCount = 0;
        int lossCount = 0;
        for (Register register : registerList){
            winCount = 0;
            lossCount = 0;
            for (Standings standings : standingsList){
                if (register.getMemberId() != standings.getMemberId()){
                    continue;
                }
                if (standings.getSelected().equals(standings.getWinner())){
                    winCount++;
                } else {
                    lossCount++;
                }
            }
            winAndLoss = new StatsDetails();
            winAndLoss.setName(register.getfName() + " " +register.getlName());
            winAndLoss.setWinCount(winCount);
            winAndLoss.setLossCount(lossCount);
            winAndLosses.add(winAndLoss);
        }

        return winAndLosses;
    }

    public static List<StatsDetails> getHighestWinning(List<Standings> standingsList, List<Register> registerList){
        List<StatsDetails> winAndLossAmounts =  new ArrayList<>();
        StatsDetails totalAmounts;

        float wonAmount = 0;
        float lostAmount = 0;
        for (Register user: registerList){
            wonAmount = 0;
            lostAmount = 0;
            for (Standings standings : standingsList){
                if (user.getMemberId() != standings.getMemberId()){
                    continue;
                }
                if (standings.getWonAmount() > 0) {
                    wonAmount = wonAmount + standings.getWonAmount();
                } else if (standings.getLostAmount() > 0 ){
                    lostAmount = lostAmount + standings.getLostAmount();
                }
            }
            totalAmounts = new StatsDetails();
            totalAmounts.setName(user.getfName() + " " +user.getlName());
            totalAmounts.setWonAmount(Float.valueOf(String.format("%.2f",wonAmount)));
            totalAmounts.setLossAmount(lostAmount);
            winAndLossAmounts.add(totalAmounts);
        }
        return winAndLossAmounts;
    }

    public static void pickTopTenWinningCount(List<StatsDetails> statsDetails){
        Collections.sort(statsDetails, COUNT_TOP_TEN);
    }

    public static List<StatsDetails> pickTopTenLoosingCount(List<StatsDetails> statsDetails){
        Collections.sort(statsDetails, COUNT_LOST_TEN);
        if (statsDetails.size() > 10) {
            return statsDetails.subList(0, 10);
        } else {
            return statsDetails;
        }
    }

    public static void pickTopTenWonAmounts(List<StatsDetails> statsDetails){
        Collections.sort(statsDetails, AMOUNTS_WON_TOP_TEN);
    }

    public static StatsDetails getIndividualStats(List<Standings> standingsList, Register user){
        StatsDetails individualStats =  new StatsDetails();
        Map<String, StatsCount> userStats = new HashMap<>();
        StatsCount statsCount;

        for (Standings standings : standingsList) {
            if (user.getMemberId() != standings.getMemberId()) {
                continue;
            }
            if (userStats.containsKey(standings.getSelected())) {
                statsCount = userStats.get(standings.getSelected());
                statsCount.setSelectedCount(statsCount.getSelectedCount()+1);
                if (standings.getSelected().equals(standings.getWinner())) {
                    statsCount.setWonCount(statsCount.getWonCount() +1);
                } else if (!standings.getSelected().equals("default")){
                    statsCount.setLostCount(statsCount.getLostCount() +1);
                }

                userStats.put(standings.getSelected(), statsCount);
            } else {
                statsCount = new StatsCount();
                statsCount.setSelectedCount(1);
                if (standings.getSelected().equals(standings.getWinner())) {
                    statsCount.setWonCount(1);
                }else if (!standings.getSelected().equals("default")){
                    statsCount.setLostCount(1);
                }

                userStats.put(standings.getSelected(), statsCount);
            }
        }
        individualStats.setName(user.getfName() + " " +user.getlName());
        individualStats.setUserStats(userStats);

        validateStatsDetails(individualStats);

        return individualStats;
    }

    public static StatsDetails validateStatsDetails(StatsDetails statsDetails){

        Map<String, StatsCount> userStats = statsDetails.getUserStats();
        if (userStats.containsKey("default")){
            userStats.remove("default");
        }

        return statsDetails;
    }

    private static final Comparator<StatsDetails> COUNT_TOP_TEN = Comparator
            .comparing(StatsDetails::getWinCount, nullsLast(reverseOrder()))
            .thenComparingInt(StatsDetails::getLossCount);

    private static final Comparator<StatsDetails> COUNT_LOST_TEN = Comparator
            .comparing(StatsDetails::getLossCount, nullsLast(reverseOrder()))
            .thenComparingInt(StatsDetails::getWinCount);

    private static final Comparator<StatsDetails> AMOUNTS_WON_TOP_TEN = Comparator
            .comparing(StatsDetails::getWonAmount, nullsLast(reverseOrder()))
            .thenComparing(StatsDetails::getLossAmount);

    private static final Comparator<StatsDetails> DEFAULT_COUNT = Comparator
            .comparing(StatsDetails::getDefaultCount, nullsLast(reverseOrder()));


}
