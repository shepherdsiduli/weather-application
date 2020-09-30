package com.shepherd.data.repository.datasource;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shepherd.data.db.RoomDb;
import com.shepherd.data.models.WeatherForecastResponse;

public class LocalDataSource implements DataSource<WeatherForecastResponse> {
    private final RoomDb mDatabase;
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LocalDataSource(Context mAppContext) {
        mDatabase = RoomDb.getDatabase(mAppContext);
    }

    @Override
    public LiveData<WeatherForecastResponse> getDataStream() {
        return mDatabase.weatherDao().getAllWeathersLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(WeatherForecastResponse weatherForecastResponse) {
        try {
            mDatabase.weatherDao().insertWeathers(weatherForecastResponse);
        } catch (Exception e) {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public WeatherForecastResponse getWeathers() {
        return mDatabase.weatherDao().getAllWeathers();
    }

    @VisibleForTesting
    public void deleteAllWeathers()
    {
        mDatabase.weatherDao().deleteAllWeathers();
    }
}
