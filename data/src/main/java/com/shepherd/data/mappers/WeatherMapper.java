package com.shepherd.data.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shepherd.data.models.WeatherForecastResponse;

public class WeatherMapper extends ObjectMapper {

    public WeatherForecastResponse mapJSONToEntity(String jsonStr) {
        WeatherForecastResponse data = null;
        try {
            data = readValue(jsonStr, WeatherForecastResponse.class);
        } catch (Exception e) {
        }
        return data;
    }
}
