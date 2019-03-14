package com.sports.cricket.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ValidateDeadline implements Serializable {

    // Validates the prediction is valid or not
    public static boolean isPredictionValid(String date) throws ParseException {

        boolean isValid;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Date matchDate = sdf.parse(date);

        Date today = Calendar.getInstance().getTime();
        String currentDate = sdf.format(today);
        Date current = sdf.parse(currentDate);

        // Match Date is after current Date
        if (matchDate.compareTo(current) > 0) {
            isValid = true;
        }
        // Match Date is before Current Date
        else if (matchDate.compareTo(current) < 0) {
            isValid = false;
        } else if (matchDate.compareTo(current) == 0) {
           isValid = false;
        } else {
            isValid = false;
        }

        return isValid;
    }


    // Validates the prediction is valid or not
    public static boolean isDeadLineReached(String deadline) throws ParseException {

        boolean isDeadLineReached;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Date matchDate = sdf.parse(deadline);

        Date today = Calendar.getInstance().getTime();
        String currentDate = sdf.format(today);
        Date current = sdf.parse(currentDate);

        if (matchDate.compareTo(current) > 0) {
            isDeadLineReached = false;
        }
        else if (matchDate.compareTo(current) < 0) {
            isDeadLineReached = true;
        } else if (matchDate.compareTo(current) == 0) {
            isDeadLineReached = true;
        } else {
            isDeadLineReached = true;
        }

        return isDeadLineReached;
    }

    // Validates if the schedule is after registration
    public static boolean isPredictionAfterRegistration(String registeredTime, String scheduleTIme) throws ParseException {

        boolean isValid;

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
        Date registeredDate = currentDateFormat.parse(registeredTime);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Date scheduleDate = sdf.parse(scheduleTIme);

        // registeredDate is after scheduleDate Date
        if (registeredDate.compareTo(scheduleDate) > 0) {
            isValid = true;
        }// registeredDate is before scheduleDate Date
        else if (registeredDate.compareTo(scheduleDate) < 0) {
            isValid = false;
        } else if (registeredDate.compareTo(scheduleDate) == 0) {
            isValid = false;
        } else {
            isValid = false;
        }

        return isValid;
    }

    /*public static void main(String[] args) throws ParseException {
       boolean isTrue =  isPredictionAfterRegistration("2018-06-01T00:55:27.259", "2018 June 01 19:00:00");
        System.out.println(isTrue);
    }*/

}
