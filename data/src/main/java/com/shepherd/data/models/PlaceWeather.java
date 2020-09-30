package com.shepherd.data.models;

import androidx.room.Embedded;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlaceWeather implements Serializable {
    /*@Embedded
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dt") */
    @JsonProperty("dt")
    long dt;

    @Embedded
    @JsonProperty("main")
    Main main;

    @Embedded
    @JsonProperty("weather")
    List<Weather> weather;

    @JsonProperty("clouds")
    Clouds clouds;

    @Embedded
    @JsonProperty("wind")
    Wind wind;

    @Embedded
    @JsonProperty("sys")
    Sys sys;

    @JsonProperty("dt_txt")
    String dt_txt;

   /* @Embedded
    @JsonProperty("coord")
    Coordinates coord;

    @JsonProperty("base")
    String base;

    @JsonProperty("visibility")
    int visibility;

    @JsonProperty("timezone")
    int timezone;

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("cod")
    String cod; */

    public PlaceWeather() {
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
