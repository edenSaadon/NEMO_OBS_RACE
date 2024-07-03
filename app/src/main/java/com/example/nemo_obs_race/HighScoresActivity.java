package com.example.nemo_obs_race;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HighScoresActivity extends AppCompatActivity implements HighScoresFragment.OnScoreClickListener {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        // Load HighScoresFragment and MapFragment
        loadFragment(new HighScoresFragment(), R.id.fragmentContainerHighScores);
        mapFragment = new MapFragment();
        loadFragment(mapFragment, R.id.fragmentContainerMap);
    }

    private void loadFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    @Override
    public void onScoreClick(double latitude, double longitude) {
        if (mapFragment != null) {
            mapFragment.updateLocation(latitude, longitude);
        }
    }
}
