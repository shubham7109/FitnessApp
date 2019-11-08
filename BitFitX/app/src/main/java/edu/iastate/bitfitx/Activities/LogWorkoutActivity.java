package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.iastate.bitfitx.R;

public class LogWorkoutActivity extends AppCompatActivity {
    String workoutLength;
    String workoutComments;
    String workoutType;
    EditText length;
    EditText comments;
    Button addWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_workout_layout);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener(this);

        length = findViewById(R.id.workout_length);
        comments = findViewById(R.id.workout_comments);
        addWorkout = findViewById(R.id.add_workout_button);
    }

    public void onNewWorkoutClicked(){
        workoutComments = comments.getText().toString();
        workoutLength = length.getText().toString();
    }
}
