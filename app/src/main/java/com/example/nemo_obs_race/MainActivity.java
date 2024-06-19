package com.example.nemo_obs_race;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.bumptech.glide.Glide;
import com.example.nemo_obs_race.Logic.GameManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView nemo_IMG_background;
    private LinearLayoutCompat hearts_life;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private LinearLayoutCompat button_layout;
    private AppCompatImageView player;
    private AppCompatImageView obstacle_top;
    private AppCompatImageView obstacle_bottom;
    private GameManager gameManager;
    private Vibrator vibrator;

    private final AppCompatImageView[] main_IMG_hearts = new AppCompatImageView[3];
    private int playerPosition = 1; // 0: left, 1: center, 2: right
    private final Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
        setupButtons();
        startGame();
    }

    private void findViews() {
        nemo_IMG_background = findViewById(R.id.nemo_IMG_background);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_IMG_hearts[0] = findViewById(R.id.main_IMG_heart1);
        main_IMG_hearts[1] = findViewById(R.id.main_IMG_heart2);
        main_IMG_hearts[2] = findViewById(R.id.main_IMG_heart3);
        player = findViewById(R.id.player);
        obstacle_bottom = findViewById(R.id.obstacle_bottom);
        obstacle_top = findViewById(R.id.obstacle_top);
        button_layout = findViewById(R.id.button_layout);
        hearts_life = findViewById(R.id.hearts_life);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Initialize the vibrator
    }

    private void initViews() {
        Glide.with(this)
                .load(R.drawable.sea)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(nemo_IMG_background);
    }

    private void setupButtons() {
        main_BTN_left.setOnClickListener(v -> moveLeft());
        main_BTN_right.setOnClickListener(v -> moveRight());
    }

    private void moveLeft() {
        if (playerPosition > 0) {
            playerPosition--;
            updatePlayerPosition();
        }
    }

    private void moveRight() {
        if (playerPosition < 2) {
            playerPosition++;
            updatePlayerPosition();
        }
    }

    private void updatePlayerPosition() {
        float translationX = 0;
        int screenWidth = findViewById(R.id.main).getWidth();
        switch (playerPosition) {
            case 0:
                translationX = -((float) (screenWidth / 3)); // Move to the left third
                break;
            case 1:
                translationX = 0; // Center position
                break;
            case 2:
                translationX = (float)(screenWidth / 3); // Move to the right third
                break;
        }
        player.setTranslationX(translationX);
    }

    private void startGame() {
        gameManager = new GameManager(3); // Initialize gameManager with 3 lives

        runnable = new Runnable() {
            @Override
            public void run() {
                moveObstacles();
                checkCollision();
                handler.postDelayed(this, 1000); // Move obstacles every second
            }
        };
        handler.post(runnable);
    }

    private void moveObstacles() {
        obstacle_top.setY(obstacle_top.getY() + 100); // Move down
        obstacle_bottom.setY(obstacle_bottom.getY() + 100); // Move down

        int buttonY = button_layout.getTop();
        int heartsY = hearts_life.getBottom();

        if (obstacle_top.getY() > buttonY - obstacle_top.getHeight()) {
            resetObstaclePosition(obstacle_top, heartsY);
        }
        if (obstacle_bottom.getY() > buttonY - obstacle_bottom.getHeight()) {
            resetObstaclePosition(obstacle_bottom, heartsY);
        }
    }

    private void resetObstaclePosition(AppCompatImageView obstacle, int heartsY) {
        obstacle.setY(heartsY + 20); // Set just below the hearts
        obstacle.setX((float) (Math.random() * (findViewById(R.id.main).getWidth() - obstacle.getWidth())));
    }

    private void checkCollision() {
        if (checkOverlap(player, obstacle_top) || checkOverlap(player, obstacle_bottom)) {
            vibrate();
            showToastMessage("Collision detected!");
            gameManager.decreaseLives();
            updateHearts();
            if (gameManager.isGameEnded()) {
                endGame();
            }
        }
    }

    private void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkOverlap(View view1, View view2) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        view1.getLocationOnScreen(location1);
        view2.getLocationOnScreen(location2);

        return location1[0] < location2[0] + view2.getWidth() &&
                location1[0] + view1.getWidth() > location2[0] &&
                location1[1] < location2[1] + view2.getHeight() &&
                location1[1] + view1.getHeight() > location2[1];
    }

    private void updateHearts() {
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            if (i < gameManager.getLifeCount()) {
                main_IMG_hearts[i].setVisibility(View.VISIBLE);
            } else {
                main_IMG_hearts[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void endGame() {
        handler.removeCallbacks(runnable);
        showGameOverDialog();
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
}
