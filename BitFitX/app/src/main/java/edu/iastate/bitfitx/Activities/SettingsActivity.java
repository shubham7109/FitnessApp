package edu.iastate.bitfitx.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

/**
 * This activity handles the setting interface for the application like, updating user weight
 * weight goal and more.
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Shared Preferences used to automatically save the user's email
     */
    SharedPreferences mSharedPreferences;
    /**
     * String to save the save the user's email
     */
    private String username, newWeight, goalWeight;
    /**
     * Textview to display the user's stats
     */
    TextView weight, firstname, lastname, email;
    /**
     * Instance of dataProvider
     */
    DataProvider dp;
    /**
     * String of email_goal to keep track of the user's goal weight in shared preferences
     */
    public String GOAL_KEY = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.EMAIL_KEY);
        firstname = (TextView) findViewById(R.id.first_name);
        lastname = (TextView) findViewById(R.id.last_name);
        email = (TextView) findViewById(R.id.email_txt);
        weight = (TextView) findViewById(R.id.weight_txt);

        dp = DataProvider.getInstance();
        GOAL_KEY = username+"_goal";
        mSharedPreferences = getSharedPreferences(LoginActivity.PACKAGE_NAME, Context.MODE_PRIVATE);

        dp.getUser(username, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                firstname.setText(user.getFirstName());
                lastname.setText(user.getLastName());
                email.setText(username);
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
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_to_bottom, R.anim.slide_from_top);
        super.onBackPressed();
    }

    /**
     * Changes a user's current weight.
     * @param view Change weight button that was clicked
     */
    public void onChgWeightClicked (View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        final EditText editText = new EditText(this);
        alert.setTitle("Change weight");
        alert.setView(editText);

        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                newWeight = editText.getText().toString();

                dp.addUserWeight(username, newWeight, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                        weight.setText(newWeight+ " lbs");
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                newWeight = "";
            }
        });

        alert.show();
    }

    /**
     * Adds a new goal weight.
     * @param view New Goal button that was clicked
     */
    public void onNewGoal (View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        final EditText editText = new EditText(this);
        alert.setTitle("Set new weight goal");
        alert.setView(editText);

        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                goalWeight = editText.getText().toString();
                mSharedPreferences.edit().putString(GOAL_KEY, goalWeight).commit();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                goalWeight = "";
            }
        });

        alert.show();
    }

    /**
     * Updates the user's password.
     * @param view The button to click to update password
     */
    public void passwordClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        final EditText editText = new EditText(this);
        alert.setTitle("Input password");
        alert.setView(editText);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String newPassword = editText.getText().toString();

                dp.getUser(username, new Interfaces.UserCallback() {
                    @Override
                    public void onCompleted(UserModel user) {
                        user.setPassword(newPassword);
                        dp.addUser(user, new Interfaces.DataProviderCallback() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(SettingsActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String msg) {

                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
}

