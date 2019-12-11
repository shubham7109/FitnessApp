package edu.iastate.bitfitx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import edu.iastate.bitfitx.Models.WeightModel;
import edu.iastate.bitfitx.R;
import edu.iastate.bitfitx.Utils.DataProvider;
import edu.iastate.bitfitx.Utils.Interfaces;

public class WeightGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_graph);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


        DataProvider dataProvider = DataProvider.getInstance();

        dataProvider.getUsersWeight("lol", new Interfaces.WeightListCallback() {
            @Override
            public void onCompleted(ArrayList<WeightModel> weightModels) {
                Toast.makeText(WeightGraphActivity.this, "lol", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
