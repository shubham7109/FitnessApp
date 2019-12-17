package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import edu.iastate.bitfitx.Models.WorkoutModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

/**
 * This activity displays a list of all workouts among all users
 */
public class CommunityListActivity extends AppCompatActivity {

    /**
     * List of workouts from all users
     */
    ArrayList<WorkoutModel> workoutModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);


        final RecyclerView recyclerView = findViewById(R.id.workout_history_rv);
        DataProvider dataProvider = DataProvider.getInstance();
        dataProvider.getAllWorkouts(new Interfaces.WorkoutlistCallback() {
            @Override
            public void onCompleted(ArrayList<WorkoutModel> workouts) {

                //Set up the RV
                workoutModels = workouts;
                WorkoutHistoryAdapter adapter = new WorkoutHistoryAdapter(CommunityListActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(CommunityListActivity.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String msg) {

            }
        });


    }


    /**
     * Adapter to handle the Recycler view population.
     */
    public class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder> {

        private LayoutInflater mInflater;

        WorkoutHistoryAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.workout_item, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WorkoutModel workoutModel = workoutModels.get(position);
            Date date = new Date(workoutModel.getWorkoutStartTime());
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getDefault());

            @SuppressLint("DefaultLocale") String lengthString = String.format("%d min", TimeUnit.MILLISECONDS.toMinutes(workoutModel.getLengthOfWorkout()));

            holder.date.setText(format.format(date));
            holder.workoutType.setText(String.valueOf(workoutModel.getWorkoutType()));
            holder.workoutLength.setText(lengthString);
            holder.caloriesBurnt.setText(String.valueOf(workoutModel.getCaloriesBurned()));
            holder.userName.setText(workoutModel.getUserName());
            holder.userName.setVisibility(View.VISIBLE);

        }

        // total number of rows
        @Override
        public int getItemCount() {
            return workoutModels.size();
        }


        // stores and recycles views as they are scrolled off screen
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView date;
            TextView workoutType;
            TextView workoutLength;
            TextView caloriesBurnt;
            TextView userName;


            ViewHolder(View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                workoutType = itemView.findViewById(R.id.workout_type);
                userName = itemView.findViewById(R.id.username);
                workoutLength = itemView.findViewById(R.id.workout_length);
                caloriesBurnt = itemView.findViewById(R.id.calories_burnt);
            }
        }
    }
}
