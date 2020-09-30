package com.shepherd.weather.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shepherd.weather.viewmodel.WeatherViewModel;

public class UILessFragment extends Fragment {
    private static final String TAG = UILessFragment.class.getSimpleName();
    private WeatherViewModel mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);
    }
}
