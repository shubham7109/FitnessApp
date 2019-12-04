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

import edu.iastate.bitfitx.Models.UserModel;
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
     * String of the user's email that is passed to the dashboard activity.
     */
    public static final String USER = "email";
    TextView weight;
    TextView name;
    DataProvider dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USER);
        name = (TextView) findViewById(R.id.first_name);
        weight = (TextView) findViewById(R.id.weight_txt);

        dp = DataProvider.getInstance();
        setUpNavigationActivities();

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        dp.getUser(username, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                name.setText(user.getFirstName());
                weight.setText(user.getWeight());
            }
            @Override
            public void onError(String msg) { }

        });
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
            //TODO: OPEN A SETTINGS ACTIVITY\
            //Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(USER, username);
            startActivity(intent);
            return true;

        }else if(id==R.id.action_logout){
            //TODO: Log out of this user and clear prefs
            //Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(DashboardActivity.this, LogWorkoutActivity.class));
            }
        });

        Button trackWorkoutButton = findViewById(R.id.track_workout);
        trackWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, TrackWorkoutActivity.class));
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
        finish();
    }
}
