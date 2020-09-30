package com.shepherd.data.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shepherd.data.models.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    public static String objectAsString(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String s = null;
        try {
            s = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getDayFromDate(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date netDate = new Date(date * 1000);
        String formattedDate = sdf.format(netDate);

        DateFormat format2 = new SimpleDateFormat("EEEE");
        String finalDay = format2.format(netDate);
        return finalDay;
    }

    public static String getDayTemperature(double temperature){
        //return main.getTemp() + "\u00b0C";
        String temp = temperature + "";
        String separator =".";
        int sepPos = temp.lastIndexOf(separator);
        return temp.substring(0,sepPos) + "°";
    }
}
