package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class DashboardActivity extends AppCompatActivity {

    /**
     * Shared Preferences used to automatically save the user's email
    */
    SharedPreferences mSharedPreferences;
    /**
     * String to save the save the user's email
     */
    private String username;
    /**
     * String to save the save the user's current weight and their goal weight
     */
    private String currentWeight, goalWeight;
    /**
     * String of the user's email that is passed from the login page.
     */
    public static final String EMAIL_KEY = "email";
    /**
     * String of email_goal to keep track of the user's goal weight in shared preferences
     */
    public String GOAL_KEY = "";
    /**
     * Textview of user's statistics to be displayed on the dashboard
     */
    TextView name, weight, avgCal, avgDur, goal;
    /**
     * User object returned from the dataProvider
     */
    private UserModel userModel;
    /**
     * Instance of dataProvider
     */
    DataProvider dp;
    /**
     * List of a user's previous workouts
     */
    ArrayList<WorkoutModel> workoutModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.EMAIL_KEY);
        name = (TextView) findViewById(R.id.first_name);
        weight = (TextView) findViewById(R.id.weight_txt);
        avgCal = (TextView) findViewById(R.id.avgCalories_txt);
        avgDur = (TextView) findViewById(R.id.avgDuration_txt);
        goal = (TextView) findViewById(R.id.goal_txt);

        dp = DataProvider.getInstance();
        setUpNavigationActivities();
        GOAL_KEY = username+"_goal";

        mSharedPreferences = getSharedPreferences(LoginActivity.PACKAGE_NAME, Context.MODE_PRIVATE);

        dp.getUser(username, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                name.setText(user.getFirstName());
                userModel = user;
            }
            @Override
            public void onError(String msg) { }
        });

        dp.getUsersWeight(username, new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                weight.setText(weightModels.get(0).getWeightInPounds()+" lbs");
            }

            @Override
            public void onError(String msg) {

            }
        });

        dp.getUsersWorkouts(username, new Interfaces.WorkoutlistCallback() {
            @Override
            public void onCompleted(ArrayList<WorkoutModel> workouts) {
                workoutModels = workouts;
                setStats();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(DashboardActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume(){
        dp.getUsersWeight(username, new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                    weight.setText(weightModels.get(0).getWeightInPounds()+" lbs");
            }

            @Override
            public void onError(String msg) {

            }
        });

        dp.getUsersWorkouts(username, new Interfaces.WorkoutlistCallback() {
            @Override
            public void onCompleted(ArrayList<WorkoutModel> workouts) {
                workoutModels = workouts;
                setStats();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(DashboardActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
            }
        });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra(EMAIL_KEY, username);
            startActivity(intent);
            return true;

        }else if(id==R.id.action_logout){
            mSharedPreferences.edit().clear().commit();
            openLogin();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * This method sets up all the buttons
     * and the activity navigation on button click.
     */
    private void setUpNavigationActivities() {

        Button logWorkoutButton = findViewById(R.id.log_workout);
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LogWorkoutActivity.class);
                intent.putExtra(LoginActivity.EMAIL_KEY, userModel.getEmail());
                intent.putExtra(LoginActivity.NAME_KEY, userModel.getFirstName());
                startActivity(intent);
            }
        });

        Button trackWorkoutButton = findViewById(R.id.track_workout);
        trackWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, TrackWorkoutActivity.class);
                intent.putExtra(LoginActivity.EMAIL_KEY, userModel.getEmail());
                intent.putExtra(LoginActivity.NAME_KEY, userModel.getFirstName());

                startActivity(intent);
            }
        });

        Button communityUsersButton = findViewById(R.id.community_users);
        communityUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, CommunityListActivity.class));
            }
        });

        Button workoutHistoryButton = findViewById(R.id.personal_history);
        workoutHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, WorkoutHistoryActivity.class));
            }
        });
    }

    /**
     * Method to open the Dashboard activity when login is successful. It will pass the user's email to the next activity.
     */
    public void openLogin(){
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * Method to set the user's statistics. It will calculate the average calories burned per workout and
     * average duration per workout using the data from workout history
     */
    public void setStats(){

        int numWorkouts = workoutModels.size();
        int calories = 0;
        int time = 0;
        String CALORIES, TIME;

        if (numWorkouts != 0) {
            for (WorkoutModel w : workoutModels) {
                calories += w.getCaloriesBurned();
                time += w.getLengthOfWorkout();
            }

            CALORIES = String.format("%d cal", calories / numWorkouts);
            TIME = String.format("%d min", TimeUnit.MILLISECONDS.toMinutes(time / numWorkouts));
        }
        else{
            CALORIES = String.format("%d cal", 0);
            TIME = String.format("%d min", 0);
        }

        avgCal.setText(CALORIES);
        avgDur.setText(TIME);
    }

    public void weightGraphClick(View view) {
        Intent intent = new Intent(this, WeightGraphActivity.class);
        startActivity(intent);
    }
}
