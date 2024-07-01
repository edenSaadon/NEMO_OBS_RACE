package com.example.nemo_obs_race.Models;

public class HighScore {
    private String userId;
    private int score;

    public HighScore(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
