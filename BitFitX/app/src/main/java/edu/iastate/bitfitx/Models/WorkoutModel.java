package edu.iastate.bitfitx.Models;

public class WorkoutModel {

    /**
     * Eg Running, bilking...
     */
    private String workoutType;

    /**
     * In kcal units.
     */
    private long caloriesBurned;

    /**
     * Time in milliseconds
     */
    private long lengthOfWorkout;

    /**
     * Epoch start time: System.currentTimeMillis();
     */
    private long workoutStartTime;

    public WorkoutModel(String workoutType, long caloriesBurned, long lengthOfWorkout, long workoutStartTime) {
        this.workoutType = workoutType;
        this.caloriesBurned = caloriesBurned;
        this.lengthOfWorkout = lengthOfWorkout;
        this.workoutStartTime = workoutStartTime;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public long getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(long caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public long getLengthOfWorkout() {
        return lengthOfWorkout;
    }

    public void setLengthOfWorkout(long lengthOfWorkout) {
        this.lengthOfWorkout = lengthOfWorkout;
    }

    public long getWorkoutStartTime() {
        return workoutStartTime;
    }

    public void setWorkoutStartTime(long workoutStartTime) {
        this.workoutStartTime = workoutStartTime;
    }
}
