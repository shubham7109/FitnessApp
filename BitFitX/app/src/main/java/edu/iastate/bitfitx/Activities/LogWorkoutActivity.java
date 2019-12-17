package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

/**
 * This Activity will allow a User to log a workout based on the day they did the workout and what they did for the workout.
 */

public class LogWorkoutActivity extends AppCompatActivity {
    /**
     * Instance of the DataProvider
     */
    DataProvider dp;
    /**
     * Instance of the workout model
     */
    WorkoutModel workout;
    /**
     * Long variables for the length of the workout, the amount of calories burned, and the start time of the workout
     */
    long workoutLength, caloriesBurned, workoutStartTime;
    /**
     * String variables for the type of workout, the username of the user, and the weight of the user
     */
    String workoutType, username, weight;
    /**
     * Button for adding a workout
     */
    Button addWorkout;
    /**
     * The spinner in which you can choose a type of workout
     */
    Spinner mySpinner;
    /**
     * Time Pickers to choose the start and end times of the workout
     */
    TimePicker startPicker, endPicker;
    /**
     * Date Picker to choose the date in which the workout was done
     */
    DatePicker datePicker;
    /**
     * Private String variable for the firstname of the user
     */
    private String firstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_workout_layout);

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
        firstName = getIntent().getStringExtra("firstName");

        addWorkout = findViewById(R.id.add_workout_button);
        startPicker = findViewById(R.id.start_picker);
        endPicker = findViewById(R.id.end_picker);
        datePicker = findViewById(R.id.datePicker);

        dp.getUsersWeight(username, new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                weight = (weightModels.get(0).getWeightInPounds());
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    /**
     * Adds workout to the Database
     * @param view A instance of View
     */
    public void onAddWorkoutButtonClicked(View view){
        workoutLength = getWorkoutLength(datePicker, endPicker) - getWorkoutLength(datePicker, startPicker);
        workoutType = mySpinner.getSelectedItem().toString();
        workoutStartTime = getWorkoutLength(datePicker, startPicker) - 43200000;
        caloriesBurned = TrackWorkoutActivity.calculateCals(workoutType, weight, workoutLength);

        workout = new WorkoutModel(workoutType, firstName, caloriesBurned, workoutLength, workoutStartTime);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        if(workoutLength > 0 ){
            dp.addUserWorkout(email, workout, new Interfaces.DataProviderCallback() {
                @Override
                public void onCompleted() {
                    Toast.makeText(LogWorkoutActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(LogWorkoutActivity.this, "Error while adding: " +msg, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Workout length cannot be negative", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Calculates the time in miliseconds based on the time and date
     * @param date The date inputted for the workout
     * @param time The time input for the the workout
     * @return
     */
    public long getWorkoutLength(DatePicker date, TimePicker time){
        Calendar myCalendar = Calendar.getInstance();
        int test = date.getDayOfMonth();
        test = date.getMonth();
        myCalendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        myCalendar.set(Calendar.MONTH, date.getMonth());
        myCalendar.set(Calendar.YEAR, date.getYear());
        myCalendar.set(Calendar.HOUR, time.getHour());
        myCalendar.set(Calendar.MINUTE, time.getMinute());

        return myCalendar.getTimeInMillis();
    }
}
