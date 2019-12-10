package edu.iastate.bitfitx.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
    private String username, newWeight;
    TextView weight, firstname, lastname, email, password;
    /**
     * Instance of dataProvider
     */
    DataProvider dp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.EMAIL_KEY);
        firstname = (TextView) findViewById(R.id.first_name);
        lastname = (TextView) findViewById(R.id.last_name);
        email = (TextView) findViewById(R.id.email_txt);
        password = (TextView) findViewById(R.id.password_txt);
        weight = (TextView) findViewById(R.id.weight_txt);

        dp = DataProvider.getInstance();

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        dp.getUser(username, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                firstname.setText(user.getFirstName());
                lastname.setText(user.getLastName());
                email.setText(username);
                password.setText(user.getPassword());
                weight.setText(user.getWeight());
            }
            @Override
            public void onError(String msg) { }

        });
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

                dp.updateWeight(username, newWeight, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                        weight.setText(newWeight);
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
        //mSharedPreferences.edit().putString(EMAIL_KEY, email).commit();
    }

    /**
     * Changes the user's current password.
     * @param view Change password button that was clicked
     */
    public void onChgPasswordClicked (View view) {
        //OPTIONAL METHOD
    }
}

