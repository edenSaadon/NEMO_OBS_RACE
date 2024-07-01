package com.example.nemo_obs_race;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        // Load HighScoresFragment and MapFragment
        loadFragment(new HighScoresFragment(), R.id.fragmentContainerHighScores);
        loadFragment(new MapFragment(), R.id.fragmentContainerMap);
    }

    private void loadFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
}
