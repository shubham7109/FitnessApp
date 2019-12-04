package edu.iastate.bitfitx.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.iastate.bitfitx.Models.UserModel;

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
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put("first", modelUser.getFirstName());
        user.put("last", modelUser.getLastName());
        user.put("email", modelUser.getEmail());
        user.put("password", modelUser.getPassword());

        String documentPath = modelUser.getEmail();
        //DocumentReference documentReference = db.document(documentPath);

        // Add a new document with a generated ID
        db.collection("users").document(documentPath)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataProviderCallback.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dataProviderCallback.onError("Error adding document" + e.getMessage());
                    }
                });

        // Get a new write batch
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

    /**
     * Get a user from the DB
     * @param emailID email-ID of the user
     */
    public void getUser(String emailID, final UserCallback callback){
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
                                UserModel userModel = new UserModel(
                                        data.get("firstName").toString(),
                                        data.get("lastName").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString(),
                                        data.get("weight").toString()
                                );

                                callback.onCompleted(userModel);
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

}
