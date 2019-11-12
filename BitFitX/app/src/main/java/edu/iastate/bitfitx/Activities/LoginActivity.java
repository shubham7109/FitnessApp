package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import edu.iastate.bitfitx.Models.UserModel;

import edu.iastate.bitfitx.R;

public class LoginActivity extends AppCompatActivity {

    /**
     * Element for entering the email
     */
    private TextView email;
    /**
     * Element for entering the password
     */
    private TextView password;
    /**
     * String to store the user's email
     */
    private String username;
    /**
     * Shared Preferences used to automatically save the user's email
     */
    private SharedPreferences mSharedPreferences;
    /**
     * Title of the list that is going to be edited. The string is passed to the second activity for editing.
     */
    public static final String USER = "";

    //True, when we need to run tests for the database
    private final static boolean IS_DATABASE_TEST_ENABLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (mSharedPreferences.getString("email", null)!=null){
            username = mSharedPreferences.getString("email", null);
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(USER, username);
            startActivity(intent);
        }
        //DO NOT REMOVE THIS IF STATEMENT
        if (IS_DATABASE_TEST_ENABLED) {
            startActivity(new Intent(this, DatabaseTestActivity.class));
        }

        //TODO Add stuff here.. Maybe login/registration page.
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    username = (s.toString());
                } catch (Exception e) {
                    username = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    /**
     * Saves the username and starts dashboard activity
     * @param view Login button that was clicked
     */
    public void onLoginClicked (View view) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("email", username);
        editor.commit();
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(USER, username);
        startActivity(intent);
    }

    /**
     * Adds a new list to the LiveData container from the user input text.
     * @param view Register button that was clicked
     */
    public void onRegisterClicked (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
