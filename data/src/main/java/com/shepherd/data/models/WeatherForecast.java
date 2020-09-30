package com.shepherd.data.models;


import androidx.room.Embedded;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WeatherForecast implements Serializable {

    @Embedded
    @JsonProperty("list")
    List<PlaceWeather> placeWeathers;

    public WeatherForecast() {
    }

    public List<PlaceWeather> getPlaceWeathers() {
        return placeWeathers;
    }

    public void setPlaceWeathers(List<PlaceWeather> placeWeathers) {
        this.placeWeathers = placeWeathers;
    }
}
