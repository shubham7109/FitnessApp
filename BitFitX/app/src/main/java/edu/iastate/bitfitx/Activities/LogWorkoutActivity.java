package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.iastate.bitfitx.R;

public class LogWorkoutActivity extends AppCompatActivity {
    String workoutLength;
    String workoutComments;
    String workoutType;
    EditText length;
    EditText comments;
    Button addWorkout;
    Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_workout_layout);

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

        length = findViewById(R.id.workout_length);
        comments = findViewById(R.id.workout_comments);
        addWorkout = findViewById(R.id.add_workout_button);
    }

    public void onAddWorkoutButtonClicked(View view){
        workoutComments = comments.getText().toString();
        if(workoutComments == null){

        }
        workoutLength = length.getText().toString();
        workoutType = mySpinner.getSelectedItem().toString();

    }
}
