package edu.iastate.bitfitx.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import edu.iastate.bitfitx.Models.UserModel;
import edu.iastate.bitfitx.Models.WorkoutModel;

/**
 * This class will provide data from Firestore on
 * all aspects of the App
 * Example:
 * DataProvider dataProvider = DataProvider.getInstance();
 */
public class DataProvider extends Interfaces {

    private static FirebaseFirestore db;
    private static DataProvider instance = null;

    public DataProvider(){
        db = FirebaseFirestore.getInstance();
    }

    static public DataProvider getInstance() {
        return instance == null ? instance = new DataProvider() : instance;
    }


    /**
     * Adds the user to the DB
     * @param modelUser The model user to add
     */
    public void addUser(UserModel modelUser, final DataProviderCallback dataProviderCallback){
        WriteBatch batch = db.batch();

        DocumentReference donationRef = db.collection("users").document(modelUser.getEmail());
        batch.set(donationRef, modelUser);

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dataProviderCallback.onCompleted();
                }
            });
        }catch (Exception e){
            dataProviderCallback.onError("Error adding document" + e.getMessage());
        }
    }

    public void addUserWorkout(String emailID, final WorkoutModel workoutModel, final DataProviderCallback callback) {
        WriteBatch batch = db.batch();
        DocumentReference donationRef = db.collection("users").document(emailID).collection("workouts").document();
        batch.set(donationRef, workoutModel);

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }


    public void updateWeight(String emailID, final String weightInLbs, final DataProviderCallback callback){
    getUser(emailID, new UserCallback() {
        @Override
        public void onCompleted(UserModel user) {
            user.setWeight(weightInLbs); //Weight has now been updated
            addUser(user, new DataProviderCallback() {
                @Override
                public void onCompleted() {
                    callback.onCompleted();
                }

                @Override
                public void onError(String msg) {
                    callback.onError(msg);
                }
            });

        }

        @Override
        public void onError(String msg) {

        }
    });

    }

    /**
     * Get a user from the DB
     * @param emailID email-ID of the user
     */
    public void getUser(final String emailID, final UserCallback callback){
        try{
            db.collection("users")
                    .document(emailID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> data = documentSnapshot.getData();
                                if(data == null){
                                    callback.onError("No User found");
                                    return;
                                }
                                final UserModel userModel = new UserModel(
                                        data.get("firstName").toString(),
                                        data.get("lastName").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString(),
                                        data.get("weight").toString()
                                );

                                getUsersWorkouts(emailID, new WorkoutlistCallback() {
                                    @Override
                                    public void onCompleted(ArrayList<WorkoutModel> workouts) {
                                        userModel.setWorkoutList(workouts);
                                        callback.onCompleted(userModel);
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        callback.onCompleted(userModel);
                                    }
                                });


                            } else {
                                callback.onError("Error getting user: " + task.getException().getMessage());
                            }
                        }
                    });
        }catch (Exception e){
            callback.onError("Error getting user: " + e.getMessage());
        }
    }

    /**
     * Get all the users from the database
     * @param callback gets a list of all users
     */
    public void getAllUsers(final UserListCallback callback){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<UserModel> modelUsersList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                UserModel modelUser = new UserModel(
                                        data.get("firstName").toString(),
                                        data.get("lastName").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString(),
                                        data.get("weight").toString()
                                );
                                modelUsersList.add(modelUser);
                            }
                            callback.onCompleted(modelUsersList);
                        } else {
                            callback.onError("Error getting user: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getUsersWorkouts(String emailID, final WorkoutlistCallback callback){
        db.collection("users")
                .document(emailID)
                .collection("workouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WorkoutModel> workoutModels = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                WorkoutModel modelUser = new WorkoutModel(
                                        data.get("workoutType").toString(),
                                        data.get("userName").toString(),
                                        Long.valueOf(data.get("caloriesBurned").toString()),
                                        Long.valueOf(data.get("lengthOfWorkout").toString()),
                                        Long.valueOf(data.get("workoutStartTime").toString())
                                );
                                workoutModels.add(modelUser);
                            }


                            Collections.sort(workoutModels, new Comparator<WorkoutModel>() {
                                @Override
                                public int compare(WorkoutModel workoutModel, WorkoutModel t1) {
                                    return Long.compare(workoutModel.getWorkoutStartTime(), t1.getWorkoutStartTime());
                                }
                            });
                            Collections.reverse(workoutModels);


                            callback.onCompleted(workoutModels);
                        } else {
                            callback.onError("Error getting user: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private int count = 0;
    public void getAllWorkouts(final WorkoutlistCallback callback){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<WorkoutModel> workoutModels = new ArrayList<>();
                            final int size = task.getResult().size();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                getUsersWorkouts(document.getId(), new WorkoutlistCallback() {
                                    @Override
                                    public void onCompleted(ArrayList<WorkoutModel> workouts) {
                                        workoutModels.addAll(workouts);

                                        if(++count >= size){
                                            count = 0;

                                            Collections.sort(workoutModels, new Comparator<WorkoutModel>() {
                                                @Override
                                                public int compare(WorkoutModel workoutModel, WorkoutModel t1) {
                                                    return Long.compare(workoutModel.getWorkoutStartTime(), t1.getWorkoutStartTime());
                                                }
                                            });
                                            Collections.reverse(workoutModels);


                                            callback.onCompleted(workoutModels);
                                        }

                                    }

                                    @Override
                                    public void onError(String msg) {
                                        callback.onError(msg);
                                    }
                                });

                            }



                        } else {
                            callback.onError("Error getting user: " + task.getException().getMessage());
                        }
                    }
                });

    }

}
