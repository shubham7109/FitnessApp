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
     * String to store the user's email
     */
     String email;
    /**
     * String to store the user's password
     */
     String password;

     SharedPreferences mSharedPreferences;
    /**
     * Title of the list that is going to be edited. The string is passed to the second activity for editing.
     */
    public static final String USER = "email";

    DataProvider dp;
    TextView errorMsg;

    //True, when we need to run tests for the database
    private final static boolean IS_DATABASE_TEST_ENABLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        //mSharedPreferences.edit().clear().commit();

        if (mSharedPreferences.getString("email", null)!=null){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        dp = DataProvider.getInstance();

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        //DO NOT REMOVE THIS IF STATEMENT
        if (IS_DATABASE_TEST_ENABLED) {
            startActivity(new Intent(this, DatabaseTestActivity.class));
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
                        mSharedPreferences.edit().putString("email", email).commit();
                        openDashboard(email);
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
        intent.putExtra(USER, email);
        startActivity(intent);
        finish();
    }

}
