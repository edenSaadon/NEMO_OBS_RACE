package com.example.nemo_obs_race;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nemo_obs_race.Logic.HighScoresManager;
import com.example.nemo_obs_race.Models.HighScore;

import java.util.List;

public class HighScoresFragment extends Fragment {

    private HighScoresManager highScoresManager;
    private TableLayout highScoresTable;
    private TextView bestScoreTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);

        highScoresTable = view.findViewById(R.id.highScoresTable);
        bestScoreTextView = view.findViewById(R.id.bestScore);

        highScoresManager = new HighScoresManager(getContext());
        displayHighScores();

        return view;
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
            Log.d("HighScoresFragment", "Displaying high score for user " + highScore.getUserId() + ": " + highScore.getScore());
            TableRow tableRow = new TableRow(getContext());
            if (rank == 1) {
                tableRow.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            } else if (rank % 2 == 0) {
                tableRow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }

            TextView rankView = new TextView(getContext());
            rankView.setText(String.valueOf(rank));
            rankView.setPadding(8, 8, 8, 8);
            tableRow.addView(rankView);

            TextView userIdView = new TextView(getContext());
            userIdView.setText(highScore.getUserId());
            userIdView.setPadding(8, 8, 8, 8);
            tableRow.addView(userIdView);

            TextView scoreView = new TextView(getContext());
            scoreView.setText(String.valueOf(highScore.getScore()));
            scoreView.setPadding(8, 8, 8, 8);
            tableRow.addView(scoreView);

            highScoresTable.addView(tableRow);

            tableRow.setOnClickListener(v -> {
                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentContainerMap);
                if (mapFragment != null) {
                    mapFragment.updateLocation(highScore.getLatitude(), highScore.getLongitude());
                }
            });

            rank++;
        }
    }

    private void addHeaderRow() {
        TableRow headerRow = new TableRow(getContext());
        headerRow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        TextView rankHeader = new TextView(getContext());
        rankHeader.setText("Rank");
        rankHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        rankHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(rankHeader);

        TextView userIdHeader = new TextView(getContext());
        userIdHeader.setText("User ID");
        userIdHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        userIdHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(userIdHeader);

        TextView scoreHeader = new TextView(getContext());
        scoreHeader.setText("Score");
        scoreHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        scoreHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(scoreHeader);

        highScoresTable.addView(headerRow);
    }
}
