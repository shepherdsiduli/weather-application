package com.shepherd.weather.recview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shepherd.data.models.PlaceWeather;
import com.shepherd.data.utilities.Utilities;
import com.shepherd.weather.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder> {
    public WeatherForecastAdapter(Context mContext) {
        this.mContext = mContext;
    }

    List<PlaceWeather> mItems = new ArrayList<>();
    LinkedHashMap<String, List<PlaceWeather>> mDayForeCasts = new LinkedHashMap<>();
    Context mContext;

    @NonNull
    @Override
    public WeatherForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastViewHolder holder, int position) {
        final PlaceWeather placeWeather = mItems.get(position);
        holder.dayName.setText(Utilities.getDayFromDate(placeWeather.getDt()));
        holder.dayAverageTemp.setText(Utilities.getDayTemperature(placeWeather.getMain().getTemp()));
        String icon = placeWeather.getWeather().get(0).getIcon();
        String iconFull = "a"+ icon + "_svg";
        int resID = mContext.getResources().getIdentifier(iconFull, "drawable", mContext.getPackageName());
        if(resID != 0){
            holder.dayWeatherIcon.setBackgroundResource(resID);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<PlaceWeather> items) {
        this.mItems.clear();
        // this.mItems.addAll(items);
        getDayList(items);
        notifyDataSetChanged();
    }

    /*
     * if proper api ie (16 day / Daily Forecast) is used no need for this unfortunately its paid
     * https://openweathermap.org/api
     * api.openweathermap.org/data/2.5/forecast/daily?lat=35&lon=139&cnt=10
     * api.openweathermap.org/data/2.5/forecast/daily?q=London&mode=xml&units=metric&cnt=7
     */
    public void getDayList(List<PlaceWeather> items) {
        String today = Utilities.getDayFromDate(items.get(0).getDt());
        List<PlaceWeather> filteredList = new ArrayList<>();
        HashMap<String, PlaceWeather> hashMap = new HashMap<>();
        for (PlaceWeather placeWeather : items) {
            String day = Utilities.getDayFromDate(placeWeather.getDt());
            if (!hashMap.containsKey(day) && !day.equals(today)) {
                hashMap.put(day, placeWeather);
                filteredList.add(placeWeather);
            }

            if(!day.equals(today)){
                if(mDayForeCasts.containsKey(day)){
                    List<PlaceWeather> list = mDayForeCasts.get(day);
                    list.add(placeWeather);
                    mDayForeCasts.put(day, list);
                } else {
                    List<PlaceWeather> list = new ArrayList<>();
                    list.add(placeWeather);
                    mDayForeCasts.put(day, list);
                }
            }
        }

        filteredList = new ArrayList<>();

        for (HashMap.Entry<String, List<PlaceWeather>> entry : mDayForeCasts.entrySet()) {
            filteredList.add(getElementWithHighestTemperature(entry.getValue()));
        }

        this.mItems.addAll(filteredList);
    }

    PlaceWeather getElementWithHighestTemperature(List<PlaceWeather> list){
        PlaceWeather highest = list.get(0);
        for(PlaceWeather placeWeather : list){
            if(placeWeather.getMain().getTemp() > highest.getMain().getTemp()){
                highest = placeWeather;
            }
        }
        return highest;
    }

    class WeatherForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dayName;
        TextView dayAverageTemp;
        ImageView dayWeatherIcon;

        public WeatherForecastViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day_name);
            dayWeatherIcon = itemView.findViewById(R.id.day_weather_icon);
            dayAverageTemp = itemView.findViewById(R.id.day_average_temperature);
        }
    }
}
