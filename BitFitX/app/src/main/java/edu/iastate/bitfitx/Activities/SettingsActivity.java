package edu.iastate.bitfitx.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class SettingsActivity extends AppCompatActivity {

    /**
     * Shared Preferences used to automatically save the user's email
     */
    SharedPreferences mSharedPreferences;
    /**
     * String to save the save the user's email
     */
    private String username;
    TextView weight, firstname, lastname;
    DataProvider dp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USER);
        firstname = (TextView) findViewById(R.id.first_name);
        lastname = (TextView) findViewById(R.id.last_name);
        weight = (TextView) findViewById(R.id.weight_txt);

        dp = DataProvider.getInstance();

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        dp.getUser(username, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                firstname.setText(user.getFirstName());
                weight.setText(user.getWeight());
            }
            @Override
            public void onError(String msg) { }

        });
    }

    /**
     * Adds a new goal weight.
     * @param view New Goal button that was clicked
     */
    public void onChgWeightClicked (View view) {
    }

    /**
     * Adds a new goal weight.
     * @param view New Goal button that was clicked
     */
    public void onChgPasswordClicked (View view) {
    }

    /**
     * Adds a new goal weight.
     * @param view New Goal button that was clicked
     */
    public void onNewGoalClicked (View view) {
    }

}

