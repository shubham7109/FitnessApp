package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.iastate.bitfitx.R;

public class TrackWorkoutActivity extends AppCompatActivity {

    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout);

        //TODO Implement class here...
        firstName = getIntent().getStringExtra("firstName");//This will be needed by the workoutModel
    }
}
