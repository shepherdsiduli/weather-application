package com.shepherd.data.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Clouds implements Serializable {
    @JsonProperty("all")
    int all;

    public Clouds() {
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
