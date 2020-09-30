package com.shepherd.data.models;

import androidx.room.Embedded;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Sys implements Serializable {

    @JsonProperty("pod")
    String pod;

   /* @JsonProperty("type")
    int type;

    @JsonProperty("id")
    int id;

    @JsonProperty("country")
    String country;

    @Embedded
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("sunrise")
    Date sunrise;

    @Embedded
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("sunset")
    Date sunset;

    @Embedded
    @JsonProperty("rain")
    Rain rain;*/

    public Sys() {
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }
}
