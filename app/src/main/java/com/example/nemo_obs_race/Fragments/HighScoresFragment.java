package com.example.nemo_obs_race.Fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.example.nemo_obs_race.R;

import java.util.List;

public class HighScoresFragment extends Fragment {

    private HighScoresManager highScoresManager;
    private TableLayout highScoresTable;
    private TextView bestScoreTextView;
    private OnScoreClickListener onScoreClickListener;

    public interface OnScoreClickListener {
        void onScoreClick(double latitude, double longitude);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScoreClickListener) {
            onScoreClickListener = (OnScoreClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScoreClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        highScoresTable = view.findViewById(R.id.highScoresTable);
        bestScoreTextView = view.findViewById(R.id.bestScore);
        highScoresManager = new HighScoresManager(getContext());
        loadHighScores();
        return view;
    }

    private void loadHighScores() {
        List<HighScore> highScores = highScoresManager.getHighScores();
        highScoresTable.removeAllViews();

        // Create table header
        TableRow header = new TableRow(getContext());
        header.addView(createTextView("Rank"));
        header.addView(createTextView("User ID", 12)); // הקטנת גודל הטקסט של עמודת ה-ID
        header.addView(createTextView("Score"));
        highScoresTable.addView(header);

        // Populate table rows
        for (int i = 0; i < highScores.size(); i++) {
            HighScore highScore = highScores.get(i);
            TableRow row = new TableRow(getContext());
            row.addView(createTextView(String.valueOf(i + 1)));
            row.addView(createTextView(highScore.getUserId(), 12)); // הקטנת גודל הטקסט של עמודת ה-ID
            row.addView(createTextView(String.valueOf(highScore.getScore())));

            row.setOnClickListener(v -> {
                if (onScoreClickListener != null) {
                    onScoreClickListener.onScoreClick(highScore.getLatitude(), highScore.getLongitude());
                }
            });

            highScoresTable.addView(row);
        }

        // Update best score
        if (!highScores.isEmpty()) {
            HighScore bestScore = highScores.get(0);
            bestScoreTextView.setText(String.format("Best Score: %d", bestScore.getScore()));
        } else {
            bestScoreTextView.setText("Best Score: 0");
        }
    }

    private TextView createTextView(String text) {
        return createTextView(text, 16); // הקטנת גודל הטקסט הכללי
    }

    private TextView createTextView(String text, int textSize) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(4, 4, 4, 4); // הקטנת padding
        textView.setTextSize(textSize);
        return textView;
    }
}
