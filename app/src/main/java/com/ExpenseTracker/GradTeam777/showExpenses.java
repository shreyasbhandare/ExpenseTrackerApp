package com.ExpenseTracker.GradTeam777;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class showExpenses extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    PolynomialRegression regression;
    TextView predTxt;
    SQLiteDatabaseHelper helper;
    BarChart barchart;
    double[] xList;
    double[] yList;
    ArrayList<String> ydata=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);
        helper = new SQLiteDatabaseHelper(getApplicationContext());
        predTxt = (TextView) findViewById(R.id.predictTxt);

        loadDBData();
        showGraph();

        int size = xList.length;
        //double[] x = {1,2,3,4,5,6,7,8,9,10};
        //double[] y = {100.45, 125.25, 90.73, 80.15,150.93, 147.34, 120.53, 133.67, 70.23, 100.22};
        //int sizee = x.length;

        regression = new PolynomialRegression(xList, yList, 3);
        //regression.get();
        double predX = xList[size-1]+1;
        //double predXX = 7;

        double predY = regression.predict(predX);
        predTxt.setText("predicted expenses for week "+predX+" is $"+predY);
    }

    public void loadDBData(){

        Cursor result = helper.getEntries();
        int count=0;
        xList = new double[result.getCount()];
        yList = new double[result.getCount()];

        while(result.moveToNext()){
            //String date = result.getString(1);//.substring(4,2);
            //String day = date.substring(4,5);
            xList[count]=Double.parseDouble(result.getString(0));
            yList[count]=Double.parseDouble(result.getString(2));
            String d=result.getString(1);
            ydata.add(d);
            count++;
        }

    }

    public void showGraph(){
        barchart=(BarChart)findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries=new ArrayList<>();

        for(int i=0;i<yList.length;i++)
        {
            //String c=arr.get(i-1);
            Double a=yList[i];
            float b=a.floatValue();
            //float d=Float.parseFloat(c);
            barEntries.add(new BarEntry(i,b));
        }


        BarDataSet barDataSet=new BarDataSet(barEntries,"AMOUNT ($)");
        BarData theData=new BarData(barDataSet);
        barchart.setData(theData);

        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);
        barchart.getAxisRight().setDrawLabels(false);
        Description description = new Description();
        description.setText("DATE");
        barchart.setDescription(description);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
// what I want

        xAxis.setValueFormatter(new IAxisValueFormatter() {


            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String date[]=ydata.get((int)value).split("/");
                String d=date[0]+"/"+date[1];
                return d;
            }
        });

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
}
