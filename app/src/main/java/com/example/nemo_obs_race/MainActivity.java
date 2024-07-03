package com.example.nemo_obs_race;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.nemo_obs_race.Logic.HighScoresManager;
import com.example.nemo_obs_race.Models.HighScore;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RadioGroup movementOptions;
    private Button confirmButton;
    private ImageView backgroundImage;
    private HighScoresManager highScoresManager;
    private static final int GAME_REQUEST_CODE = 1; // Request code for game activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        findViews();
        initViews();
       loadImageBackground();

        highScoresManager = new HighScoresManager(this);
    }

    private void findViews() {
        movementOptions = findViewById(R.id.movement_options);
        confirmButton = findViewById(R.id.confirm_button);
        backgroundImage = findViewById(R.id.background_image);
    }

    private void initViews() {
        confirmButton.setOnClickListener(v -> {
            int selectedId = movementOptions.getCheckedRadioButtonId();
            Intent intent;

            if (selectedId == R.id.radio_buttons) {
                intent = new Intent(MainActivity.this, ButtonsActivity.class);
            } else if (selectedId == R.id.radio_sensors) {
                intent = new Intent(MainActivity.this, SensorActivity.class);
            } else if (selectedId == R.id.radio_high_scores) {
                intent = new Intent(MainActivity.this, HighScoresActivity.class);
                startActivity(intent);
                return;
            } else {
                // Default action if no option is selected
                return;
            }

            startActivityForResult(intent, GAME_REQUEST_CODE);
        });
    }

    private void loadImageBackground() {
        Glide.with(this)
                .load(R.drawable.nemo_back) // Replace with your actual image resource
                .centerCrop() // or .centerCrop() based on your requirement
                .into(backgroundImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GAME_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the score from the game activity
            int score = data.getIntExtra("score", 0);
            String userId = data.getStringExtra("userId");
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            double distance = data.getDoubleExtra("distance", 0); // Get the distance

            if (userId != null && !userId.isEmpty()) {
                HighScore highScore = new HighScore(userId, score, latitude, longitude, distance); // Add distance to HighScore
                highScoresManager.saveHighScore(highScore);

                // Update the odometer in the map fragment
                MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerMap);
                if (mapFragment != null) {
                    mapFragment.updateOdometer(latitude, longitude, distance);
                }
            } else {
                Log.e("MainActivity", "User ID is null or empty");
            }
        }
    }
}
