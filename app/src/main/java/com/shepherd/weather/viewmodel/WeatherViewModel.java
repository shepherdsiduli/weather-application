package com.shepherd.weather.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shepherd.data.models.CurrentLocation;
import com.shepherd.data.models.WeatherForecastResponse;
import com.shepherd.data.repository.WeatherRepository;
import com.shepherd.data.repository.WeatherRepositoryImpl;

public class WeatherViewModel extends AndroidViewModel {

    private static final String TAG = WeatherViewModel.class.getSimpleName();
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = WeatherRepositoryImpl.create(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public LiveData<String> getErrorUpdates() {
        return mWeatherRepository.getErrorStream();
    }

    public LiveData<WeatherForecastResponse> getPlaceWeather() {
        return mWeatherRepository.getPlaceWeather();
    }

    public void fetchData(CurrentLocation location) {
        mWeatherRepository.fetchData(location);
    }

    @VisibleForTesting
    public WeatherViewModel(@NonNull Application application,WeatherRepositoryImpl repo) {
        super(application);
        this.mWeatherRepository = repo;
    }
}
