package com.example.nemo_obs_race;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nemo_obs_race.Logic.HighScoresManager;
import com.example.nemo_obs_race.Models.HighScore;

import java.util.List;

public class HighScoresActivity extends AppCompatActivity {

    private HighScoresManager highScoresManager;
    private TableLayout highScoresTable;
    private TextView bestScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        highScoresTable = findViewById(R.id.highScoresTable);
        bestScoreTextView = findViewById(R.id.bestScore);

        // Use Glide to load the background image
        Glide.with(this)
                .load(R.drawable.high_back)
                .into(backgroundImage);

        highScoresManager = new HighScoresManager(this);
        displayHighScores();
    }

    private void displayHighScores() {
        List<HighScore> highScores = highScoresManager.getHighScores();
        highScoresTable.removeAllViews(); // Clear the table before adding rows
        addHeaderRow();

        if (!highScores.isEmpty()) {
            HighScore bestScore = highScores.get(0);
            bestScoreTextView.setText("BEST SCORE: " + bestScore.getScore());
        }

        int rank = 1;
        for (HighScore highScore : highScores) {
            Log.d("HighScoresActivity", "Displaying high score for user " + highScore.getUserId() + ": " + highScore.getScore());
            TableRow tableRow = new TableRow(this);
            if (rank == 1) {
                tableRow.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            } else if (rank % 2 == 0) {
                tableRow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }

            TextView rankView = new TextView(this);
            rankView.setText(String.valueOf(rank));
            rankView.setPadding(8, 8, 8, 8);
            tableRow.addView(rankView);

            TextView userIdView = new TextView(this);
            userIdView.setText(highScore.getUserId());
            userIdView.setPadding(8, 8, 8, 8);
            tableRow.addView(userIdView);

            TextView scoreView = new TextView(this);
            scoreView.setText(String.valueOf(highScore.getScore()));
            scoreView.setPadding(8, 8, 8, 8);
            tableRow.addView(scoreView);

            highScoresTable.addView(tableRow);
            rank++;
        }
    }

    private void addHeaderRow() {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        TextView rankHeader = new TextView(this);
        rankHeader.setText("Rank");
        rankHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        rankHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(rankHeader);

        TextView userIdHeader = new TextView(this);
        userIdHeader.setText("User ID");
        userIdHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        userIdHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(userIdHeader);

        TextView scoreHeader = new TextView(this);
        scoreHeader.setText("Score");
        scoreHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        scoreHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(scoreHeader);

        highScoresTable.addView(headerRow);
    }
}
