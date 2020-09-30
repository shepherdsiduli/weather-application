package com.shepherd.data.repository;

import androidx.lifecycle.LiveData;

import com.shepherd.data.models.CurrentLocation;
import com.shepherd.data.models.WeatherForecastResponse;

public interface WeatherRepository {
    LiveData<WeatherForecastResponse> getPlaceWeather();
    LiveData<String> getErrorStream();
    void fetchData(CurrentLocation location);
}
