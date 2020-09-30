package com.shepherd.weather.screens;

import com.shepherd.data.models.WeatherForecastResponse;

public interface MainScreen {
    void updateData(WeatherForecastResponse data);
    void setError(String msg);
}
