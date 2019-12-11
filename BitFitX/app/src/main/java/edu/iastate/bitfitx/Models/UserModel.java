package edu.iastate.bitfitx.Models;

import java.util.ArrayList;

public class UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<WeightModel> weightModelArrayList;
    private ArrayList<WorkoutModel> workoutList;
    private String weight;

    public UserModel(String firstName, String lastName, String email, String password, String weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.weight = weight;
        weightModelArrayList = new ArrayList<>();
        weightModelArrayList.add(new WeightModel(weight,System.currentTimeMillis()));
        workoutList = new ArrayList<>();
    }

    public void setWeightModelArrayList(ArrayList<WeightModel> weightModelArrayList) {
        this.weightModelArrayList = weightModelArrayList;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public ArrayList<WeightModel> getWeightModelArrayList() {
        return weightModelArrayList;
    }

    public void setWorkoutList(ArrayList<WorkoutModel> workoutList) {
        this.workoutList = workoutList;
    }

    public void addWorkout(WorkoutModel workoutModel){
        workoutList.add(workoutModel);
    }

    public ArrayList<WorkoutModel> getWorkoutList() {
        return workoutList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
