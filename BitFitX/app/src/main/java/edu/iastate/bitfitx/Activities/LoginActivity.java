package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

/**
 * This activity manages login and registration of users into the application
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * String of the user's first name that is saved in  SharedPreferences and passed to the dashboard activity.
     */
    public static final String NAME_KEY = "firstName";
    /**
     * String of the user's email that is saved in SharedPreferences and passed to the dashboard activity.
     */
    public static String EMAIL_KEY = "email";
    /**
     * String of the package name
     */
    public static String PACKAGE_NAME = "edu.iastate.bitfitx";
    /**
     * String to store the user's email
     */
     String email;
    /**
     * String to store the user's password
     */
     String password;
    /**
     * Shared Preferences used to automatically save the user's email
     */
    SharedPreferences mSharedPreferences;
    /**
     * Instance of dataProvider
     */
    DataProvider dp;
    /**
     * Textview for error message if login is unsuccessful
     */
    TextView errorMsg;

    //True, when we need to run tests for the database
    private final static boolean IS_DATABASE_TEST_ENABLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //DO NOT REMOVE THIS IF STATEMENT
        if (IS_DATABASE_TEST_ENABLED) {
            startActivity(new Intent(this, DatabaseTestActivity.class));
            finish();
        }

        dp = DataProvider.getInstance();
        mSharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        //mSharedPreferences.edit().clear().commit();

        if (mSharedPreferences.getString(EMAIL_KEY, null)!=null){
            openDashboard(mSharedPreferences.getString(EMAIL_KEY, null));
        }

        //Sets up the text edit views and error message
        final EditText email_txt = (EditText) findViewById(R.id.email_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);
        errorMsg = (TextView) findViewById(R.id.error_txt) ;
        errorMsg.setVisibility(View.INVISIBLE);

        //when login button is clicked, checks the database to see if username and password are correct.
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_txt.getText().toString();
                password = password_txt.getText().toString();

                dp.getUser(email, new Interfaces.UserCallback() {
                    @Override
                    public void onCompleted(UserModel user) {
                        if (user.getPassword().equals(password)) {
                            mSharedPreferences.edit().putString(EMAIL_KEY, email).commit();
                            openDashboard(email);
                        }
                        else{
                            onError("Incorrect Password");
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        email_txt.getText().clear();
                        password_txt.getText().clear();
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    /**
     * Changes activity to the Register Activity page
     * @param view Register button that was clicked
     */
    public void onRegisterClicked (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_to_top, R.anim.slide_from_bottom);
    }

    /**
     * If the username and password are correct, changes activity to the Dashboard Activity page
     * @param email The user's email
     */
    public void openDashboard(String email){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
}
