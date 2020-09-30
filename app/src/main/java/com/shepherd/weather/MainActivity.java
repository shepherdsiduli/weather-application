package com.shepherd.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shepherd.data.models.CurrentLocation;
import com.shepherd.data.models.PlaceWeather;
import com.shepherd.data.models.WeatherForecastResponse;
import com.shepherd.data.utilities.Utilities;
import com.shepherd.weather.fragments.UILessFragment;
import com.shepherd.weather.location.LocationManager;
import com.shepherd.weather.recview.WeatherForecastAdapter;
import com.shepherd.weather.screens.MainScreen;
import com.shepherd.weather.viewmodel.WeatherViewModel;

public class MainActivity extends LocationActivity implements MainScreen {

    private static final String WEATHER_CLEAR = "Clear";
    private static final String WEATHER_CLOUDY = "Clouds";
    private static final String WEATHER_RAINY = "Rain";

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int DATA_FETCHING_INTERVAL = 10 * 5000; //5 seconds
    private long mLastFetchedDataTimeStamp;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeatherViewModel mViewModel;
    private LocationManager mLocationManager;
    private ConstraintLayout mWeatherDescriptionIcon;
    private ConstraintLayout mWeatherMainBackground;

    private final static CurrentLocation DEFAULT_LOCATION = new CurrentLocation(-26.2023, 28.0436);

    private final Observer<WeatherForecastResponse> mDataObserver = placeWeather -> updateData(placeWeather);

    private final Observer<String> mErrorObserver = errorMsg -> setError(errorMsg);

    private RecyclerView mRecyclerView;
    private WeatherForecastAdapter mWeatherForecastAdapter;
    private TextView mCurrentWeatherTemperature;
    private TextView mCurrentWeatherDescription;
    private TextView mCurrentWeatherMin;
    private TextView mCurrentWeatherCurrent;
    private TextView mCurrentWeatherMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = new LocationManager(this, null);
        bindViews();
        mViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mViewModel.getPlaceWeather().observe(this, mDataObserver);
        mViewModel.getErrorUpdates().observe(this, mErrorObserver);
        mViewModel.fetchData(getCurrentLocation());
        getSupportFragmentManager().beginTransaction()
                .add(new UILessFragment(), "UILessFragment").commit();
    }

    private void bindViews() {
        mRecyclerView = findViewById(R.id.recView);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                showErrorToast("Not fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchData(getCurrentLocation());
        });
        mWeatherForecastAdapter = new WeatherForecastAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mWeatherForecastAdapter);
        mCurrentWeatherDescription = findViewById(R.id.current_weather_description);
        mCurrentWeatherTemperature = findViewById(R.id.current_temperature);
        mCurrentWeatherMin = findViewById(R.id.current_weather_min);
        mCurrentWeatherCurrent = findViewById(R.id.current_weather_current);
        mCurrentWeatherMax = findViewById(R.id.current_weather_max);

        mWeatherDescriptionIcon = findViewById(R.id.rootView);
        mWeatherMainBackground = findViewById(R.id.main_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, "Error:" + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateData(WeatherForecastResponse weatherResponse) {
        mLastFetchedDataTimeStamp = System.currentTimeMillis();
        if (weatherResponse != null && weatherResponse.getPlaceWeathers() != null) {
            PlaceWeather currentWeather = weatherResponse.getPlaceWeathers().get(0);
            updateUI(currentWeather);
            weatherResponse.getPlaceWeathers().remove(0);
            mWeatherForecastAdapter.setItems(weatherResponse.getPlaceWeathers());
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateUI(PlaceWeather currentWeather){
        mCurrentWeatherTemperature.setText(Utilities.getDayTemperature(currentWeather.getMain().getTemp()));
        mCurrentWeatherDescription.setText(currentWeather.getWeather().get(0).getMain().toUpperCase());
        mCurrentWeatherMin.setText(Utilities.getDayTemperature(currentWeather.getMain().getTemp_min()));
        mCurrentWeatherCurrent.setText(Utilities.getDayTemperature(currentWeather.getMain().getTemp()));
        mCurrentWeatherMax.setText(Utilities.getDayTemperature(currentWeather.getMain().getTemp_max()));

        String main = currentWeather.getWeather().get(0).getMain().toLowerCase();
        if(main.contains(WEATHER_CLEAR.toLowerCase())){
            mWeatherDescriptionIcon.setBackgroundResource(R.drawable.forest_sunny);
            mWeatherMainBackground.setBackgroundResource(R.color.sunny);
        } else if(main.contains(WEATHER_CLOUDY.toLowerCase())){
            mWeatherDescriptionIcon.setBackgroundResource(R.drawable.forest_cloudy);
            mWeatherMainBackground.setBackgroundResource(R.color.cloudy);
        } else if(main.contains(WEATHER_RAINY.toLowerCase())){
            mWeatherDescriptionIcon.setBackgroundResource(R.drawable.forest_rainy);
            mWeatherMainBackground.setBackgroundResource(R.color.rainy);
        }
    }

    private CurrentLocation getCurrentLocation(){
        CurrentLocation location = LocationManager.getLocation();
        Log.d(TAG, Utilities.objectAsString(location));
        if(location == null || location.getLatitude() == 0.0 || location.getLongitude() == 0.0){
            return DEFAULT_LOCATION;
        }
        return location;
    }
}
