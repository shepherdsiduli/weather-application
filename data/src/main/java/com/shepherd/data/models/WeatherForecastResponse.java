package com.shepherd.data.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@Entity(tableName = "weathers")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WeatherForecastResponse implements Serializable {

    @JsonProperty("cod")
    String code;

    @PrimaryKey
    @JsonProperty("cnt")
    int cnt;

    @JsonProperty("message")
    int message;

    @Ignore
    @JsonProperty("list")
    List<PlaceWeather> placeWeathers;

    @JsonProperty("city")
    @Embedded
    City city;

    public WeatherForecastResponse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PlaceWeather> getPlaceWeathers() {
        return placeWeathers;
    }

    public void setPlaceWeathers(List<PlaceWeather> placeWeathers) {
        this.placeWeathers = placeWeathers;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
