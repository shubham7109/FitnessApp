package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

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

        //IN CASE SPINNER DOESN'T WORK
        /*mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LogWorkoutActivity.this, mySpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-generated method stub
            }
        });*/

        addWorkout = findViewById(R.id.add_workout_button);
        startPicker = findViewById(R.id.start_picker);
        endPicker = findViewById(R.id.end_picker);
        datePicker = findViewById(R.id.datePicker);
    }

    public void onAddWorkoutButtonClicked(View view){
        workoutLength = getWorkoutLength(datePicker, endPicker) - getWorkoutLength(datePicker, startPicker);
        workoutType = mySpinner.getSelectedItem().toString();
        workoutStartTime = getWorkoutLength(datePicker, startPicker);
        caloriesBurned = 100;

        workout = new WorkoutModel(workoutType, caloriesBurned, workoutLength, workoutStartTime);

    }

    public long getWorkoutLength(DatePicker date, TimePicker time){
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        myCalendar.set(Calendar.MONTH, date.getMonth());
        myCalendar.set(Calendar.YEAR, date.getYear());
        myCalendar.set(Calendar.HOUR, time.getHour());
        myCalendar.set(Calendar.MINUTE, time.getMinute());

        return myCalendar.getTimeInMillis();
    }
}
