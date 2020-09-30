package com.shepherd.weather.tracking;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shepherd.data.models.Coordinates;
import com.shepherd.data.models.CurrentLocation;

import org.json.JSONObject;

import java.util.HashMap;

public class Tracker implements LifecycleObserver {
    private static final String TAG = Tracker.class.getSimpleName();
    private final String TRACKING_URL = "https://www.google.com";
  //  private final String TRACKING_URL = "https://developers.google.com/places/web-service/intro";
   // private final RequestQueue mQueue;
    private final String mOsVersion;
    private Context mContext;
    private CurrentLocation mCoordinates;

    public Tracker(Context context) {
        this.mContext = context;
        mOsVersion = Build.VERSION.RELEASE;
      //  mQueue = Volley.newRequestQueue(context.getApplicationContext());
        //context.getLifecycle().addObserver(this);
    }

    private StringRequest generateTrackingStringRequest(final String eventName) {
        return new StringRequest(Request.Method.POST, TRACKING_URL,
                response -> {
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                },
                error -> Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]")) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("eventName", eventName);
                params.put("osVersion", mOsVersion);
                return new JSONObject(params).toString().getBytes();
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void trackOnCreate() {
        Log.d(TAG, "trackOnCreate() called");
     //   mQueue.add(generateTrackingStringRequest("create"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void trackOnDestroy() {
        Log.d(TAG, "trackOnDestroy() called");
        //  ((AppCompatActivity)mContext).getLifecycle().removeObserver(this);
     //   mQueue.add(generateTrackingStringRequest("destroy"));
        // Lifecycle.State currentState =((AppCompatActivity)      mContext).getLifecycle().getCurrentState();
        mContext = null;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void trackOnStart() {
        Log.d(TAG, "trackOnStart() called");
      //  mQueue.add(generateTrackingStringRequest("start"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void trackOnResume() {
        Log.d(TAG, "trackOnResume() called");
      //  mQueue.add(generateTrackingStringRequest("resume"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void trackOnPause() {
        Log.d(TAG, "trackOnPause() called");
       // mQueue.add(generateTrackingStringRequest("pause"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void trackOnStop() {
        Log.d(TAG, "trackOnStop() called");
        //mQueue.add(generateTrackingStringRequest("stop"));
    }
}
