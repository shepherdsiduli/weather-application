package com.shepherd.weather.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.shepherd.data.models.CurrentLocation;
import com.shepherd.weather.tracking.Tracker;

public class LocationManager implements LifecycleObserver {
    private static final String TAG = LocationManager.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Tracker mTracker;
    private Context mContext;
    private static CurrentLocation mCurrentLocation;

    public LocationManager(Context context, Tracker mTracker) {
        Log.d(TAG, "LocationManager() called with: con = [" + context + "], mTracker = [" + mTracker + "]");
        this.mTracker = mTracker;
        this.mContext = context;
        this.mCurrentLocation = new CurrentLocation(0.0, 0.0);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void init() {
        Log.d(TAG, "init() called");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext.getApplicationContext());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void clean() {
        Log.d(TAG, "clean() called");
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        final LifecycleOwner lcOwner = (LifecycleOwner) mContext;
        mFusedLocationClient.removeLocationUpdates(mLocationCallbacks);
        lcOwner.getLifecycle().removeObserver(this);
        mContext = null;
    }


    private LocationCallback mLocationCallbacks = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "onLocationResult() called with: locationResult = [" + locationResult + "]");
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    //mTracker.trackLocation(location.getLatitude(), location.getLongitude());
                    mCurrentLocation.setLatitude(location.getLatitude());
                    mCurrentLocation.setLongitude(location.getLongitude());
                }
            }
        }
    };

    public synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient() called");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext.getApplicationContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(10000);
                        mLocationRequest.setFastestInterval(10000);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED)
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallbacks, Looper.myLooper());

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "onConnectionSuspended() called with: i = [" + i + "]");
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> Log.d(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]"))
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static CurrentLocation getLocation(){
        return mCurrentLocation;
    }
}
