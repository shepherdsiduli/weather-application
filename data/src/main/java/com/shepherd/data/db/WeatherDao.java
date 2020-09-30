package com.shepherd.data.db;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shepherd.data.models.WeatherForecastResponse;

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeathers(WeatherForecastResponse weatherForecastResponse);

    @Query("SELECT * FROM weathers")
    LiveData<WeatherForecastResponse> getAllWeathersLive();

    @Query("SELECT * FROM weathers")
    WeatherForecastResponse getAllWeathers();

    @VisibleForTesting
    @Query("DELETE FROM weathers")
    void deleteAllWeathers();
}
