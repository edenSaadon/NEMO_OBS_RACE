
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
import com.bumptech.glide.Glide;
import com.example.nemo_obs_race.Logic.GameManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView nemo_IMG_background;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private AppCompatImageView player_left;
    private AppCompatImageView player_center;
    private AppCompatImageView player_right;
    private AppCompatImageView[] obstacles;
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
        gameManager = new GameManager(3); // Initialize gameManager with 3 lives
        startGame();
    }

    private void findViews() {
        nemo_IMG_background = findViewById(R.id.nemo_IMG_background);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_IMG_hearts[0] = findViewById(R.id.main_IMG_heart1);
        main_IMG_hearts[1] = findViewById(R.id.main_IMG_heart2);
        main_IMG_hearts[2] = findViewById(R.id.main_IMG_heart3);
        player_left = findViewById(R.id.player_left);
        player_center = findViewById(R.id.player_center);
        player_right = findViewById(R.id.player_right);
        obstacles = new AppCompatImageView[]{
                findViewById(R.id.obstacle_top),
                findViewById(R.id.obstacle_bottom)
        };
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
        player_left.setVisibility(View.INVISIBLE);
        player_center.setVisibility(View.INVISIBLE);
        player_right.setVisibility(View.INVISIBLE);

        switch (playerPosition) {
            case 0:
                player_left.setVisibility(View.VISIBLE);
                break;
            case 1:
                player_center.setVisibility(View.VISIBLE);
                break;
            case 2:
                player_right.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startGame() {
        gameManager = new GameManager(3); // Initialize gameManager with 3 lives

        runnable = new Runnable() {
            @Override
            public void run() {
                if (!gameManager.isGameEnded()) {
                    moveObstacles();
                    checkCollision();
                    handler.postDelayed(this, 500); // Move obstacles every 500ms
                }
            }
        };
        handler.post(runnable);
    }

    private void moveObstacles() {
        for (AppCompatImageView obstacle : obstacles) {
            obstacle.setY(obstacle.getY() + 100); // Move down
            if (obstacle.getY() > findViewById(R.id.main).getHeight()) {
                resetObstaclePosition(obstacle);
            }
        }
        refreshUI();
    }

    private void refreshUI() {
        updatePlayerPosition();
        updateObstaclesPosition();
        updateHearts();
    }

    private void updateObstaclesPosition() {
        int[][] grid = gameManager.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == GameManager.OBSTACLES) {
                    positionViewInLane(obstacles[row], col, row);
                }
            }
        }
    }

    private void positionViewInLane(View view, int lane, int row) {
        int laneWidth = findViewById(R.id.main).getWidth() / GameManager.GRID_COLS;
        view.setX(lane * laneWidth);
        int heartBottom = findViewById(R.id.hearts_life).getBottom();
        view.setY(heartBottom + (float) (row * (findViewById(R.id.main).getHeight() - heartBottom)) / GameManager.GRID_ROWS);
    }

    private void resetObstaclePosition(AppCompatImageView obstacle) {
        int heartBottom = findViewById(R.id.hearts_life).getBottom();
        obstacle.setY(heartBottom); // Set just below the hearts
        obstacle.setX((float) (Math.random() * (findViewById(R.id.main).getWidth() - obstacle.getWidth())));
    }

    private void checkCollision() {
        AppCompatImageView currentPlayerView = null;
        switch (playerPosition) {
            case 0:
                currentPlayerView = player_left;
                break;
            case 1:
                currentPlayerView = player_center;
                break;
            case 2:
                currentPlayerView = player_right;
                break;
        }

        if (currentPlayerView != null) {
            for (AppCompatImageView obstacle : obstacles) {
                if (checkOverlap(currentPlayerView, obstacle)) {
                    vibrate();
                    showToastMessage();
                    gameManager.decreaseLives();
                    updateHearts();
                    if (gameManager.isGameEnded()) {
                        endGame();
                    }
                    resetObstaclePosition(obstacle); // Reset position of the obstacle after collision
                }
            }
        }
    }

    private boolean checkOverlap(View view1, View view2) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        view1.getLocationOnScreen(location1);
        view2.getLocationOnScreen(location2);

        int view1Left = location1[0];
        int view1Right = location1[0] + view1.getWidth();
        int view1Top = location1[1];
        int view1Bottom = location1[1] + view1.getHeight();

        int view2Left = location2[0];
        int view2Right = location2[0] + view2.getWidth();
        int view2Top = location2[1];
        int view2Bottom = location2[1] + view2.getHeight();

        return view1Right >= view2Left && view1Left <= view2Right && view1Bottom >= view2Top && view1Top <= view2Bottom;
    }

    private void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void showToastMessage() {
        Toast.makeText(this, "Collision detected!", Toast.LENGTH_SHORT).show();
    }

    private void updateHearts() {
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            if (i < gameManager.getLives()) {
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