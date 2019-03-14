package com.sports.cricket.validations;

import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Register;
import com.sports.cricket.model.Schedule;
import com.sports.cricket.util.ValidateDeadline;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ResultValidator implements Serializable {

    static List<ErrorDetails> errorsList = null;

    public static List<ErrorDetails> isMatchResultValid(Schedule schedule){

        errorsList = new ArrayList<>();
        ErrorDetails errorDetails = null;
        if (schedule.getWinner().equalsIgnoreCase(schedule.getHomeTeam())){

        }else if(schedule.getWinner().equalsIgnoreCase(schedule.getAwayTeam())){

        }else if(schedule.getWinner().equalsIgnoreCase("draw")){

        }else{
            errorDetails = new ErrorDetails();
            errorDetails.setErrorField("winner");
            errorDetails.setErrorMessage("Please select a valid result ..!");
            errorsList.add(errorDetails);
        }

        return errorsList;
    }

    public static List<ErrorDetails> isValid(Prediction prediction){

        List<ErrorDetails> errorDetailsList = new ArrayList<>();

        if ( null == prediction){
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Prediction");
            errorDetails.setErrorMessage("Please select your prediction");
            errorDetailsList.add(errorDetails);
        }

        if (null != prediction
                && null != prediction.getFirstName()
                && prediction.getFirstName().equalsIgnoreCase("--- SELECT ---")){
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorField("Name");
            errorDetails.setErrorMessage("Please select a valid Name");
            errorDetailsList.add(errorDetails);
        }

        if ( null != prediction
                && null != prediction.getSelected()){
            if (prediction.getSelected().equalsIgnoreCase("--- SELECT ---")){
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setErrorField("Selected Field");
                errorDetails.setErrorMessage("Please select a valid prediction");
                errorDetailsList.add(errorDetails);
            }
        }

        return  errorDetailsList;
    }

}
