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

import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import edu.iastate.bitfitx.Models.UserModel;

import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class LoginActivity extends AppCompatActivity {

    /**
     * String of the user's email that is saved in SharedPreferences and passed to the dashboard activity.
     */
    public static String EMAIL_KEY = "email";
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


        final EditText email_txt = (EditText) findViewById(R.id.email_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);
        errorMsg = (TextView) findViewById(R.id.error_txt) ;
        errorMsg.setVisibility(View.INVISIBLE);

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
     * Adds a new list to the LiveData container from the user input text.
     * @param view Register button that was clicked
     */
    public void onRegisterClicked (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Method to open the Dashboard activity when login is successful. It will pass the user's email to the next activity.
     * @param String The user's email
     */
    public void openDashboard(String email){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
        finish();
    }

}
