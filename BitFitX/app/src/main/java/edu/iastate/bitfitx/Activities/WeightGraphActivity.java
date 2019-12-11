package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class WeightGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_graph);


        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");

        if(email.equals("")) {
            Toast.makeText(this, "Error getting email", Toast.LENGTH_SHORT).show();
            return;
        }


        DataProvider dataProvider = DataProvider.getInstance();
        dataProvider.getUsersWeight(email, new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                Collections.reverse(weightModels);
                setUpGraph(weightModels);
                setUpRV();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(WeightGraphActivity.this, "Error retrieving weight", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRV() {
    }

    private void setUpGraph(ArrayList<WeightModel> weightModels) {
        GraphView graph = findViewById(R.id.graph);
        DataPoint[] dataPoints = new DataPoint[weightModels.size()];

        for(int i=0; i<weightModels.size(); i++){
            dataPoints[i] = new DataPoint(new Date(weightModels.get(i).getTimeInMillis()), Double.parseDouble(weightModels.get(i).getWeightInPounds()));
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setDrawBackground(true);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setTitle("lbs");

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long) dataPoint.getX());
                SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");

                Toast.makeText(WeightGraphActivity.this, dataPoint.getY()+"lbs on " + sd.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        graph.addSeries(series);


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(new Date(weightModels.get(0).getTimeInMillis()).getTime());
        graph.getViewport().setMaxX(new Date(weightModels.get(weightModels.size()-1).getTimeInMillis()).getTime());
        graph.getViewport().setXAxisBoundsManual(true);
    }
}
