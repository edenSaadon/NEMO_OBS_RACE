package com.example.nemo_obs_race;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.nemo_obs_race.Models.HighScore;

import java.util.List;

public class CustomMapView extends View {
    private List<HighScore> highScores;
    private Paint paint;
    private double currentLatitude;
    private double currentLongitude;

    public CustomMapView(Context context) {
        super(context);
        init();
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setHighScores(List<HighScore> highScores) {
        this.highScores = highScores;
        invalidate();
    }

    public void updateLocation(double latitude, double longitude) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (highScores != null) {
            for (HighScore score : highScores) {
                float x = (float) (canvas.getWidth() * (score.getLongitude() + 180) / 360);
                float y = (float) (canvas.getHeight() * (90 - score.getLatitude()) / 180);
                canvas.drawCircle(x, y, 10, paint);
            }
        }

        float currentX = (float) (canvas.getWidth() * (currentLongitude + 180) / 360);
        float currentY = (float) (canvas.getHeight() * (90 - currentLatitude) / 180);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(currentX, currentY, 15, paint);
    }
}
