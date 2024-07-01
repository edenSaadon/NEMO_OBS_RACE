package com.example.nemo_obs_race;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {

    private CustomMapView customMapView;
    private TextView odometerTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        customMapView = view.findViewById(R.id.customMapView);
        odometerTextView = view.findViewById(R.id.odometer);

        // Initialize CustomMapView and odometer here

        return view;
    }

    public void updateLocation(double latitude, double longitude) {
        customMapView.updateLocation(latitude, longitude);
        // Update the odometer value here
    }
}
