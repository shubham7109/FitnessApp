package edu.iastate.bitfitx.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class RegisterActivity extends AppCompatActivity {

    UserModel user;
    DataProvider dp;
    String firstName;
    String lastName;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dp = DataProvider.getInstance();

        final EditText first_txt = (EditText) findViewById(R.id.firstname_text);
        final EditText last_txt = (EditText) findViewById(R.id.lastname_text);
        final EditText email_txt = (EditText) findViewById(R.id.email_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);

        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = first_txt.getText().toString();
                lastName = last_txt.getText().toString();
                email = email_txt.getText().toString();
                password = password_txt.getText().toString();

                user = new UserModel(firstName, lastName, email, password);
                dp = new DataProvider();
                dp.addUser(user, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                        openLogin();
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
            }
        });

    }
    /**
     * Method to open the Dashboard activity when login is successful. It will pass the user's email to the next activity.
     */
    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}