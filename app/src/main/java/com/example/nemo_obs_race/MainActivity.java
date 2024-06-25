package com.example.nemo_obs_race;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;
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
    private FrameLayout[] lanes;
    private GameManager gameManager;
    private Vibrator vibrator;

    private final AppCompatImageView[] main_IMG_hearts = new AppCompatImageView[3];
    private AppCompatImageView player_left;
    private AppCompatImageView player_center;
    private AppCompatImageView player_right;
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
        lanes = new FrameLayout[]{
                findViewById(R.id.main_Left_line),
                findViewById(R.id.main_Middle_line),
                findViewById(R.id.main_Right_line)
        };
        player_left = findViewById(R.id.player_left);
        player_center = findViewById(R.id.player_center);
        player_right = findViewById(R.id.player_right);
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
        if (gameManager.getNemoCol() > 0) {
            gameManager.moveNLeft();
            updatePlayerPosition();
        }
    }

    private void moveRight() {
        if (gameManager.getNemoCol() < 2) {
            gameManager.moveNRight();
            updatePlayerPosition();
        }
    }

    private void updatePlayerPosition() {
        player_left.setVisibility(View.INVISIBLE);
        player_center.setVisibility(View.INVISIBLE);
        player_right.setVisibility(View.INVISIBLE);

        switch (gameManager.getNemoCol()) {
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
            showToastMessage();
            gameManager.decreaseLives();
            updateHearts();
            if (gameManager.isGameEnded()) {
                endGame();
            }
        }
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

                    // Set the size of the obstacles
                    if (grid[row][col] == GameManager.OBSTACLES) {
                        params.width = (int) (70 * getResources().getDisplayMetrics().density); // 100dp
                        params.height = (int) (60 * getResources().getDisplayMetrics().density); // 90dp
                    }

                    imageView.setLayoutParams(params);

                    if (grid[row][col] == GameManager.NEMO) {
                        imageView.setImageResource(R.drawable.nemo);
                    } else if (grid[row][col] == GameManager.OBSTACLES) {
                        imageView.setImageResource(R.drawable.shark);
                    }

                    lanes[col].addView(imageView);
                }
            }
        }
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

    private void showToastMessage() {
        Toast.makeText(this, "Collision detected!", Toast.LENGTH_SHORT).show();
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
