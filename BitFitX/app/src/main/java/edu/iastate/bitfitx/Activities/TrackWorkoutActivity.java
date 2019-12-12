package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.iastate.bitfitx.Models.StopWatchModel;
import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class TrackWorkoutActivity extends AppCompatActivity {

    private String firstName;
    private StopWatchModel stopWatchModel;
    DataProvider dp;
    WorkoutModel workout;
    String username;
    TextView clockDisplay;
    ImageButton start;
    ImageButton stop;
    Button submitWorkout;
    Spinner mySpinner;
    long lengthOfWorkout;
    String workoutType;
    long startTime;
    long caloriesBurned;
    String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.EMAIL_KEY);
        firstName = intent.getStringExtra(LoginActivity.NAME_KEY);


        dp = DataProvider.getInstance();

        mySpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mySpinner.setAdapter(adapter);

        clockDisplay = findViewById(R.id.clock_textview);
        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);
        submitWorkout = findViewById(R.id.submit_workout_button);
        stopWatchModel = new StopWatchModel(this);

        dp.getUsersWeight(username, new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                weight = (weightModels.get(0).getWeightInPounds());
            }

            @Override
            public void onError(String msg) {

            }
        });

        setButtonVisibility(false);
    }

    public void onStartClicked(View view){
        stopWatchModel.startThread();
        startTime = System.currentTimeMillis();
        setButtonVisibility(true);
    }

    public void onStopClicked(View view){
        stopWatchModel.handleStop();
        setButtonVisibility(false);
    }

    public void setDisplay(String displayText){
        clockDisplay.setText(displayText);
    }

    public void setButtonVisibility(boolean isRunning){
        if(isRunning){
            stop.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);
            submitWorkout.setVisibility(View.GONE);
        }else{
            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            submitWorkout.setVisibility(View.VISIBLE);
        }
    }

    public void onSubmitClicked(View view){
        lengthOfWorkout = stopWatchModel.getElapsedTime();
        workoutType = mySpinner.getSelectedItem().toString();
        caloriesBurned = calculateCals(workoutType, weight, lengthOfWorkout);

        workout = new WorkoutModel(workoutType, firstName, caloriesBurned, lengthOfWorkout, startTime);

        if(lengthOfWorkout > 0 ){
            dp.addUserWorkout(username, workout, new Interfaces.DataProviderCallback() {
                @Override
                public void onCompleted() {
                    Toast.makeText(TrackWorkoutActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(TrackWorkoutActivity.this, "Error while adding: " +msg, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Workout length cannot be negative", Toast.LENGTH_SHORT).show();
        }

    }

    public static long calculateCals(String workoutType, String myWeight, long lengthOfWorkout){
        long weight = Long.valueOf(myWeight);
        lengthOfWorkout = lengthOfWorkout / 60000;
        if(workoutType.equals("Running")){
            return (long) (.0175 * 9.8 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Swimming")){
            return (long) (.0175 * 8.75 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Bicycling")){
            return (long) (.0175 * 8 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Cycling(Stationary)")){
            return (long) (.0175 * 7.7 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Walking")){
            return (long) (.0175 * 4 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Calisthenics")){
            return (long) (.0175 * 6.25 * (weight * .45)) * lengthOfWorkout;
        }
        else if (workoutType.equals("Dancing")){
            return (long) (.0175 * 6 * (weight * .45)) * lengthOfWorkout;
        }
        else
            return 0;
    }
}
