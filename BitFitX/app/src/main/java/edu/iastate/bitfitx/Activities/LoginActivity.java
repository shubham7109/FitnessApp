package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.iastate.bitfitx.R;

public class LoginActivity extends AppCompatActivity {

    //True, when we need to run tests for the database
    private final static boolean IS_DATABASE_TEST_ENABLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //DO NOT REMOVE THIS IF STATEMENT
        if(IS_DATABASE_TEST_ENABLED){
            startActivity(new Intent(this, DatabaseTestActivity.class));
        }

        //TODO Add stuff here.. Maybe login/registration page.

        //TODO: Run this when user is logged in or registers
        startActivity(new Intent(this, DashboardActivity.class));


    }
}
