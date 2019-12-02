package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.iastate.bitfitx.R;

public class DashboardActivity extends AppCompatActivity {

    /**
     * Shared Preferences used to automatically save the user's email
     */
    private SharedPreferences mSharedPreferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().commit();

        if (mSharedPreferences.getString("email", null)==null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            username = mSharedPreferences.getString("email", null);
        }

        Button logWorkoutButton = findViewById(R.id.log_workout);
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, LogWorkoutActivity.class));
            }
        });
    }
}
