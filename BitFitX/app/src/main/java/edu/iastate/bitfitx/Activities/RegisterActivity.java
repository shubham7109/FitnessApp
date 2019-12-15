package edu.iastate.bitfitx.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class RegisterActivity extends AppCompatActivity {

    UserModel user;
    /**
     * Instance of dataProvider
     */
    DataProvider dp;
    /**
     * Strings to store the user's statistics to be displayed on the dashboard
     */
    String firstName, lastName, email, password, weight;
    /**
     * String of the package name
     */
    public static String PACKAGE_NAME = "edu.iastate.bitfitx";
    /**
     * Shared Preferences used to automatically save the user's email
     */
    SharedPreferences mSharedPreferences;
    /**
     * Textview for error message if login is unsuccessful
     */
    TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dp = DataProvider.getInstance();
        mSharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);


        final EditText first_txt = (EditText) findViewById(R.id.firstname_text);
        final EditText last_txt = (EditText) findViewById(R.id.lastname_text);
        final EditText email_txt = (EditText) findViewById(R.id.email_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);
        final EditText weight_txt = (EditText) findViewById(R.id.weight_text);

        errorMsg = (TextView) findViewById(R.id.error_text) ;
        errorMsg.setVisibility(View.INVISIBLE);

        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = first_txt.getText().toString();
                lastName = last_txt.getText().toString();
                email = email_txt.getText().toString();
                password = password_txt.getText().toString();
                weight = weight_txt.getText().toString();

                if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || weight.equals("")) {
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    user = new UserModel(firstName, lastName, email, password, weight);
                    dp = new DataProvider();
                    dp.addUser(user, new Interfaces.DataProviderCallback() {
                        @Override
                        public void onCompleted() {
                            if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || weight.equals("")) {
                                onError("Incorrect Password");
                            } else {
                                openLogin();
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            errorMsg.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }
    /**
     * Method to open the Dashboard activity when login is successful. It will pass the user's email to the next activity.
     */
    public void openLogin(){
        mSharedPreferences.edit().putString(EMAIL_KEY, email).commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_to_bottom, R.anim.slide_from_top);
        finish();
    }
}