package com.ExpenseTracker.GradTeam777;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class showExpenses extends AppCompatActivity {

    PolynomialRegression regression;
    TextView predTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);
        predTxt = (TextView) findViewById(R.id.predictTxt);

        double[] x = { 1, 2, 3, 4, 5, 6};
        double[] y = { 100, 350, 1500, 6700, 20160, 40000 };
        regression = new PolynomialRegression(x, y, 3);
        //regression.get();
        double predX = 7;

        double predY = regression.predict(predX);

        predTxt.setText("predicted expenses for week "+predX+" is $"+predY);
    }
}
