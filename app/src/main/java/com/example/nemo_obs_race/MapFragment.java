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
    private double currentLatitude;
    private double currentLongitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        customMapView = view.findViewById(R.id.customMapView);
        odometerTextView = view.findViewById(R.id.odometer);

        return view;
    }

    public void updateLocation(double latitude, double longitude) {
        customMapView.updateLocation(latitude, longitude);
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
    }

    public void updateOdometer(double latitude, double longitude, double distance) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
        odometerTextView.setText(String.format("Distance: %.2f km", distance));
    }
}
