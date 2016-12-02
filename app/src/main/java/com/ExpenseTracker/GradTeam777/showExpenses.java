package com.ExpenseTracker.GradTeam777;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class showExpenses extends AppCompatActivity {

    BarChart barchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);

        barchart=(BarChart)findViewById(R.id.bargraph);

        SQLExampleHelper helper = new SQLExampleHelper(this);
        //helper.delete();

        helper.insertEntry("Jan","102");
        helper.insertEntry("Feb","103");
        helper.insertEntry("Mar","104");

        ArrayList<String>array=helper.getEntireColumn(SQLExampleHelper.COLUMN_VALUE);
        ArrayList<String>arr=helper.getEntireColumn(SQLExampleHelper.COLUMN_NAME);

        ArrayList<BarEntry> barEntries=new ArrayList<>();

        for(int i=0;i<array.size();i++)
        {
            String c=arr.get(i);
            String a=array.get(i);
            Float b= Float.valueOf(a);
            barEntries.add(new BarEntry(i,b));
        }

        BarDataSet barDataSet=new BarDataSet(barEntries,"Values");

        BarData theData=new BarData(barDataSet);
        barchart.setData(theData);

        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);

        XAxis xAxis = barchart.getXAxis();
    }
}
