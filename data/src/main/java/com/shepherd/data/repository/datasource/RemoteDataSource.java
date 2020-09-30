package com.shepherd.data.repository.datasource;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shepherd.data.mappers.WeatherMapper;
import com.shepherd.data.models.CurrentLocation;
import com.shepherd.data.models.WeatherForecastResponse;

public class RemoteDataSource implements DataSource<WeatherForecastResponse>{

    private static final String TAG = RemoteDataSource.class.getSimpleName();

    public final String ENDPOINT_FETCH_FORECAST_DATA = "https://api.openweathermap.org/data/2.5/forecast?";
    public final String API_KEY = "1b7cb6b1854c1021d272901b760b03ee";

    private final RequestQueue mQueue;
    private final WeatherMapper mObjMapper;
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    private final MutableLiveData<WeatherForecastResponse> mDataApi = new MutableLiveData<>();

    public RemoteDataSource(Context appContext, WeatherMapper objMapper) {
        mQueue = Volley.newRequestQueue(appContext);
        mObjMapper = objMapper;
        mObjMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public LiveData<WeatherForecastResponse> getDataStream() {
        return mDataApi;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void fetch(@NonNull CurrentLocation location) {
        String url = getApiUrl(location);
        Log.d(TAG, url);
        final JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            Log.d(TAG, "Thread->" +
                                    Thread.currentThread().getName()+"\tGot some network response");
                            final WeatherForecastResponse data = mObjMapper.mapJSONToEntity(response.toString());
                            mDataApi.setValue(data);
                        },
                        error -> {
                            Log.d(TAG, "Thread->" +
                                    Thread.currentThread().getName()+"\tGot network error");
                            mError.setValue(error.toString());
                        });
        mQueue.add(jsonObjReq);
    }

    private String getApiUrl(CurrentLocation location){
        StringBuilder sb = new StringBuilder(ENDPOINT_FETCH_FORECAST_DATA);
        sb.append("appid="+API_KEY);
        sb.append("&lon="+location.getLongitude());
        sb.append("&lat="+location.getLatitude());
        sb.append("&units=metric");
        return sb.toString();
    }
}
