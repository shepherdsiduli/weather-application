package com.shepherd.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shepherd.data.models.WeatherForecastResponse;

@Database(entities = {WeatherForecastResponse.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {

    static final String DATABASE_NAME = "weather_data";
    private static RoomDb INSTANCE;
    public abstract WeatherDao weatherDao();
    public static RoomDb getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDb.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }
}
