package com.example.nemo_obs_race;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.nemo_obs_race.Logic.GameManager;

import java.util.UUID;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private AppCompatImageView nemo_IMG_background;
    private FrameLayout[] lanes;
    private GameManager gameManager;
    private Vibrator vibrator;
    private MediaPlayer crashSound;
    private TextView score_text;
    private final AppCompatImageView[] main_IMG_hearts = new AppCompatImageView[3];
    private AppCompatImageView player_left;
    private AppCompatImageView player_middle_left;
    private AppCompatImageView player_center;
    private AppCompatImageView player_middle_right;
    private AppCompatImageView player_right;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sensors);

        findViews();
        initViews();

        gameManager = new GameManager(3); // Initialize gameManager with 3 lives
        crashSound = MediaPlayer.create(this, R.raw.crash); // Initialize crash sound

        setupSensors();
        startGame();
    }

    private void findViews() {
        nemo_IMG_background = findViewById(R.id.nemo_IMG_background);
        main_IMG_hearts[0] = findViewById(R.id.main_IMG_heart1);
        main_IMG_hearts[1] = findViewById(R.id.main_IMG_heart2);
        main_IMG_hearts[2] = findViewById(R.id.main_IMG_heart3);
        lanes = new FrameLayout[]{
                findViewById(R.id.main_Left_line),
                findViewById(R.id.main_MiddleLeft_line),
                findViewById(R.id.main_Middle_line),
                findViewById(R.id.main_MiddleRight_line),
                findViewById(R.id.main_Right_line)
        };
        player_left = findViewById(R.id.player_left);
        player_middle_left = findViewById(R.id.player_middle_left);
        player_center = findViewById(R.id.player_center);
        player_middle_right = findViewById(R.id.player_middle_right);
        player_right = findViewById(R.id.player_right);
        score_text = findViewById(R.id.score_text);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Initialize the vibrator
    }

    private void initViews() {
        Glide.with(this)
                .load(R.drawable.sea)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(nemo_IMG_background);
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            if (x < -2) { // Tilt right
                moveRight();
            } else if (x > 2) { // Tilt left
                moveLeft();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void moveLeft() {
        if (gameManager.getNemoCol() > 0) {
            gameManager.moveNLeft();
            updatePlayerPosition();
        }
    }

    private void moveRight() {
        if (gameManager.getNemoCol() < 4) {
            gameManager.moveNRight();
            updatePlayerPosition();
        }
    }

    private void updatePlayerPosition() {
        player_left.setVisibility(View.INVISIBLE);
        player_middle_left.setVisibility(View.INVISIBLE);
        player_center.setVisibility(View.INVISIBLE);
        player_middle_right.setVisibility(View.INVISIBLE);
        player_right.setVisibility(View.INVISIBLE);

        switch (gameManager.getNemoCol()) {
            case 0:
                player_left.setVisibility(View.VISIBLE);
                break;
            case 1:
                player_middle_left.setVisibility(View.VISIBLE);
                break;
            case 2:
                player_center.setVisibility(View.VISIBLE);
                break;
            case 3:
                player_middle_right.setVisibility(View.VISIBLE);
                break;
            case 4:
                player_right.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startGame() {
        updatePlayerPosition();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!gameManager.isGameEnded()) {
                    gameManager.moveObstaclesDown();
                    checkCollision();
                    updateUI();
                    handler.postDelayed(this, 500); // Move obstacles every 500ms
                }
            }
        };
        handler.post(runnable);
    }

    private void checkCollision() {
        if (gameManager.checkCollision()) {
            vibrate();
            playCrashSound();
            showToastMessage();
            gameManager.decreaseLives();
            updateHearts();
            if (gameManager.isGameEnded()) {
                endGame();
            }
        } else if (gameManager.collectCoin()) {
            playCoinSound();
        }
    }

    private void playCoinSound() {
        MediaPlayer coinSound = MediaPlayer.create(getApplicationContext(), R.raw.coin);
        coinSound.start();
    }

    private void updateUI() {
        int[][] grid = gameManager.getGrid();
        for (FrameLayout lane : lanes) {
            lane.removeAllViews();
        }

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != GameManager.EMPTY) {
                    AppCompatImageView imageView = new AppCompatImageView(this);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.topMargin = row * (lanes[col].getHeight() / GameManager.GRID_ROWS);

                    // Set the size of the obstacles and coins
                    if (grid[row][col] == GameManager.OBSTACLES) {
                        params.width = (int) (70 * getResources().getDisplayMetrics().density); // 100dp
                        params.height = (int) (60 * getResources().getDisplayMetrics().density); // 90dp
                    } else if (grid[row][col] == GameManager.COIN) {
                        params.width = (int) (50 * getResources().getDisplayMetrics().density); // 50dp
                        params.height = (int) (50 * getResources().getDisplayMetrics().density); // 50dp
                    }

                    imageView.setLayoutParams(params);

                    if (grid[row][col] == GameManager.NEMO) {
                        imageView.setImageResource(R.drawable.nemo);
                    } else if (grid[row][col] == GameManager.OBSTACLES) {
                        imageView.setImageResource(R.drawable.shark);
                    } else if (grid[row][col] == GameManager.COIN) {
                        imageView.setImageResource(R.drawable.star);
                    }

                    lanes[col].addView(imageView);
                }
            }
        }

        // Update score text view
        int score = gameManager.getScore();
        score_text.setText(getString(R.string.score_format, score));
    }

    private void updateHearts() {
        int lives = gameManager.getLives();
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            main_IMG_hearts[i].setVisibility(i < lives ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void playCrashSound() {
        if (crashSound != null) {
            crashSound.start();
        }
    }

    private void showToastMessage() {
        Toast.makeText(this, "Collision detected!", Toast.LENGTH_SHORT).show();
    }

    private void endGame() {
        handler.removeCallbacks(runnable);
        showGameOverDialog();

        // Get the final score and return it to MainActivity
        int finalScore = gameManager.getScore();
        double distance = gameManager.getDistance(); // Get the distance from GameManager
        Intent resultIntent = new Intent();
        resultIntent.putExtra("score", finalScore);
        resultIntent.putExtra("distance", distance);

        // Add userId, latitude, and longitude
        String userId = UUID.randomUUID().toString(); // Generate a unique user ID
        double latitude = 32.0853; // Replace with actual latitude
        double longitude = 34.7818; // Replace with actual longitude
        resultIntent.putExtra("userId", userId);
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("The game has ended")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Close the activity
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (crashSound != null) {
            crashSound.release();
            crashSound = null;
        }
    }
}
