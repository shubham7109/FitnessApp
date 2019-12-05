package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import java.util.Calendar;

import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class LogWorkoutActivity extends AppCompatActivity {
    DataProvider dp;
    WorkoutModel workout;
    long workoutLength;
    long caloriesBurned;
    long workoutStartTime;
    String workoutType;
    Button addWorkout;
    Spinner mySpinner;
    TimePicker startPicker;
    TimePicker endPicker;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_workout_layout);

        dp = DataProvider.getInstance();

        mySpinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mySpinner.setAdapter(adapter);

        addWorkout = findViewById(R.id.add_workout_button);
        startPicker = findViewById(R.id.start_picker);
        endPicker = findViewById(R.id.end_picker);
        datePicker = findViewById(R.id.datePicker);
    }

    public void onAddWorkoutButtonClicked(View view){
        workoutLength = getWorkoutLength(datePicker, endPicker) - getWorkoutLength(datePicker, startPicker);
        workoutType = mySpinner.getSelectedItem().toString();
        workoutStartTime = getWorkoutLength(datePicker, startPicker) - 43200000;
        caloriesBurned = 100;

        workout = new WorkoutModel(workoutType, caloriesBurned, workoutLength, workoutStartTime);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        dp.addUserWorkout(email, workout, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                Toast.makeText(LogWorkoutActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(LogWorkoutActivity.this, "Error while adding: " +msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

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
