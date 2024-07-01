package com.example.nemo_obs_race.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.nemo_obs_race.Models.HighScore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoresManager {

    private static final String SP_FILE = "high_scores_sp";
    private static final String HIGH_SCORES_KEY = "high_scores";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public HighScoresManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveHighScore(HighScore highScore) {
        List<HighScore> highScores = getHighScores();
        boolean scoreUpdated = false;

        Log.d("HighScoresManager", "Saving high score for user " + highScore.getUserId() + ": " + highScore.getScore());

        for (HighScore score : highScores) {
            if (score.getUserId() != null && highScore.getUserId() != null && score.getUserId().equals(highScore.getUserId())) {
                if (highScore.getScore() > score.getScore()) {
                    score.setScore(highScore.getScore());
                    score.setLatitude(highScore.getLatitude());
                    score.setLongitude(highScore.getLongitude());
                    Log.d("HighScoresManager", "Updated high score for user " + highScore.getUserId() + ": " + highScore.getScore());
                }
                scoreUpdated = true;
                break;
            }
        }

        if (!scoreUpdated) {
            highScores.add(highScore);
            Log.d("HighScoresManager", "Added new high score for user " + highScore.getUserId() + ": " + highScore.getScore());
        }

        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore o1, HighScore o2) {
                return Integer.compare(o2.getScore(), o1.getScore());
            }
        });

        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }

        for (HighScore score : highScores) {
            Log.d("HighScoresManager", "Final high score for user " + score.getUserId() + ": " + score.getScore());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String highScoresJson = gson.toJson(highScores);
        editor.putString(HIGH_SCORES_KEY, highScoresJson);
        editor.apply();
    }



    public List<HighScore> getHighScores() {
        String highScoresJson = sharedPreferences.getString(HIGH_SCORES_KEY, null);
        if (highScoresJson == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<HighScore>>() {}.getType();
        return gson.fromJson(highScoresJson, type);
    }

    public void clearHighScores() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(HIGH_SCORES_KEY);
        editor.apply();
        Log.d("HighScoresManager", "All high scores cleared.");

        // Log to verify if the high scores are really cleared
        String highScoresJson = sharedPreferences.getString(HIGH_SCORES_KEY, null);
        Log.d("HighScoresManager", "High scores after clear: " + highScoresJson);
    }
}
