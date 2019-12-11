package edu.iastate.bitfitx.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class DatabaseTestActivity extends AppCompatActivity{

    DataProvider dataProvider;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_test_layout);

        //This is how to get the instance of the DataProvider
        dataProvider = DataProvider.getInstance();

        createUserTest();
        getUserTest();
        getAllUsersTest();
        updateUserWeightTest();
        addWorkoutTest();
        getWorkoutsTest();
    }

    private void getWorkoutsTest() {
        dataProvider.getUsersWorkouts("lhealy@iastate.edu", new Interfaces.WorkoutlistCallback() {
            @Override
            public void onCompleted(ArrayList<WorkoutModel> workouts) {
                Log.e("test","test");
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void addWorkoutTest() {
        dataProvider.addUserWorkout(user.getEmail(), new WorkoutModel("Running", "Shubham", getRandInt(), 4743, 1575504743), new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void updateUserWeightTest() {
        dataProvider.addUserWeight(user.getEmail(), "69", new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void getAllUsersTest() {
        dataProvider.getAllUsers(new Interfaces.UserListCallback() {
            @Override
            public void onCompleted(ArrayList<UserModel> user) {
                //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void getUserTest() {
        dataProvider.getUser(user.getEmail(), new Interfaces.UserCallback() {
            @Override
            public void onCompleted(UserModel user) {
                //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void createUserTest() {
        UserModel userModel = new UserModel(getRandString(4),
                getRandString(4),
                getRandString(3) + "@iastate.edu",
                getRandString(3),
                String.valueOf(getRandDouble(3)));
        user = userModel;
        dataProvider.addUser(userModel, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                //Toast.makeText(DatabaseTestActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public static String getRandString(int length) {
        String SALTCHARS = "qwertyuioplkjhgfdsazxcvbnm";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static double getRandDouble(int length) {
        String SALTCHARS = "0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return Double.parseDouble(saltStr);
    }

    public static int getRandInt(){
        int min = 100;
        int max = 1000;
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }


}
