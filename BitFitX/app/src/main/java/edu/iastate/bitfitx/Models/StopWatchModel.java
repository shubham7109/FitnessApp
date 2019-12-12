package edu.iastate.bitfitx.Models;

import android.annotation.SuppressLint;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.iastate.bitfitx.Activities.TrackWorkoutActivity;

public class StopWatchModel {

    private Handler handler;
    private long TIME_START;
    private long CUR_TIME;
    private long ELAPSED_TIME;
    public boolean shouldThreadRun;
    private TrackWorkoutActivity trackWorkoutActivity;

    public StopWatchModel(TrackWorkoutActivity trackWorkoutActivity){this.trackWorkoutActivity = trackWorkoutActivity;}

    public void startThread(){
        handler = new Handler();
        shouldThreadRun = true;
        TIME_START = System.currentTimeMillis();

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if(shouldThreadRun){
                    ELAPSED_TIME = (System.currentTimeMillis() - TIME_START) + CUR_TIME;
                    trackWorkoutActivity.setDisplay(convertToString(ELAPSED_TIME));

                    handler.postDelayed(this, 10);
                }
            }
        };
        handler.postDelayed(r, 10);
    }

    private String convertToString(long millis){
        Date date = new Date(millis);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }

    public void handleStop(){
        shouldThreadRun = false;
        CUR_TIME = ELAPSED_TIME;
    }

    public long getElapsedTime(){
        return ELAPSED_TIME;
    }
}
