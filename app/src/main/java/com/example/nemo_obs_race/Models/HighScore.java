package com.example.nemo_obs_race.Models;

public class HighScore {
    private String userId;
    private int score;
    private double latitude;
    private double longitude;

    public HighScore(String userId, int score, double latitude, double longitude) {
        this.userId = userId;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HighScore(String userId, int score) {
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
