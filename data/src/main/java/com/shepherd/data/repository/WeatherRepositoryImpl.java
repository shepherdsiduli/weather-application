package com.shepherd.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.shepherd.data.models.CurrentLocation;
import com.shepherd.data.utilities.Utilities;
import com.shepherd.data.mappers.WeatherMapper;
import com.shepherd.data.models.WeatherForecastResponse;
import com.shepherd.data.repository.datasource.LocalDataSource;
import com.shepherd.data.repository.datasource.RemoteDataSource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherRepositoryImpl implements WeatherRepository {
    private static final String TAG = WeatherRepositoryImpl.class.getSimpleName();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private final WeatherMapper mWeatherMapper;
    MediatorLiveData<WeatherForecastResponse> mDataMerger = new MediatorLiveData<>();
    MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();

    private WeatherRepositoryImpl(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource, WeatherMapper mapper) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
        mWeatherMapper = mapper;
        mDataMerger.addSource(this.mRemoteDataSource.getDataStream(), entities ->
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataMerger\tmRemoteDataSource onChange invoked");

                        mLocalDataSource.writeData(entities);
                       // WeatherForecastResponse list = mWeatherMapper.mapEntityToModel(entities);
                        WeatherForecastResponse list = entities;
                        mDataMerger.postValue(list);
                    }
                })
        );
        mDataMerger.addSource(this.mLocalDataSource.getDataStream(), entities ->
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataMerger\tmLocalDataSource onChange invoked");
                       // WeatherForecastResponse models = mWeatherMapper.mapEntityToModel(entities);
                        WeatherForecastResponse models = entities;
                        mDataMerger.postValue(models);
                    }
                })
        );
        mErrorMerger.addSource(mRemoteDataSource.getErrorStream(), errorStr -> {
                    mErrorMerger.setValue(errorStr);
                    Log.d(TAG, "Network error -> fetching from LocalDataSource");
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            WeatherForecastResponse entities = (mLocalDataSource.getWeathers());
                            Log.d(TAG, Utilities.objectAsString(entities));
                           // mDataMerger.postValue(mWeatherMapper.mapEntityToModel(entities));
                            mDataMerger.postValue(entities);
                        }
                    });
                }
        );
        mErrorMerger.addSource(mLocalDataSource.getErrorStream(), errorStr -> mErrorMerger.setValue(errorStr));
    }

    public static WeatherRepository create(Context mAppContext) {
        final WeatherMapper mapper = new WeatherMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new WeatherRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @VisibleForTesting
    public static WeatherRepositoryImpl createImpl(Context mAppContext) {
        final WeatherMapper mapper = new WeatherMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new WeatherRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @Override
    public LiveData<WeatherForecastResponse> getPlaceWeather() {
        return mDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mErrorMerger;
    }

    @Override
    public void fetchData(CurrentLocation location) {
        mRemoteDataSource.fetch(location);
    }
}
