package edu.iastate.bitfitx.Utils;

import java.util.ArrayList;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WorkoutModel;

public class Interfaces {

    public interface DataProviderCallback{
        public void onCompleted();
        public void onError(String msg);
    }

    public interface UserCallback{
        public void onCompleted(UserModel user);
        public void onError(String msg);
    }

    public interface UserListCallback{
        public void onCompleted(ArrayList<UserModel> user);
        public void onError(String msg);
    }

    public interface WorkoutlistCallback{
        public void onCompleted(ArrayList<WorkoutModel> workouts);
        public void onError(String msg);
    }

}
