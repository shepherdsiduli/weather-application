package com.shepherd.data.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Rain implements Serializable {
    @JsonProperty("3h")
    double threeH;

    public Rain() {
    }

    public double getThreeH() {
        return threeH;
    }

    public void setThreeH(double threeH) {
        this.threeH = threeH;
    }
}
