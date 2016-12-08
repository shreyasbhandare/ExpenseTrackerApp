package com.ExpenseTracker.GradTeam777;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class predExpFrag extends Fragment {

    PolynomialRegression regression;
    TextView predTxt, sugTxt;
    SQLiteDatabaseHelper helper;
    BarChart barchart;

    //ArrayList<String> ydata=new ArrayList<>();
    ArrayList<Double> amtWeekly;

    String[]dates;
    double[]amounts;
    ArrayList<String>xDates;
    double predX,predY;

    public predExpFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pred_exp, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        helper = new SQLiteDatabaseHelper(getContext());
        predTxt = (TextView) getView().findViewById(R.id.predictTxt);
        sugTxt = (TextView) getView().findViewById(R.id.suggestTxt);

        xDates = new ArrayList<>();
        loadDBData();
        weeklyExp();
        showGraph();


        double[] xList = new double[xDates.size()];
        for(int j=0;j<xDates.size();j++){
            xList[j]=j+1;
        }

        Double[] yDoubList = new Double[amtWeekly.size()];
        amtWeekly.toArray(yDoubList);

        double[] yList = ArrayUtils.toPrimitive(yDoubList);



        regression = new PolynomialRegression(xList, yList, 3);
        int size = xList.length;
        predX = xList[size-1]+1;

        predY = regression.predict(predX);
        predY = Math.round(predY*100.0)/100.0;
        predTxt.setText(" $ "+predY);
        sugTxt.setText(suggest());
    }

    public void loadDBData(){

        Cursor result = helper.getEntriesAsc();
        int count=0;

        dates = new String[result.getCount()];
        amounts = new double[result.getCount()];

        while(result.moveToNext()){
            dates[count]=result.getString(1);
            amounts[count]=Double.parseDouble(result.getString(2));
            String d=result.getString(1);
            //ydata.add(d);
            count++;
        }
    }

    public void showGraph(){
        barchart=(BarChart)getView().findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries=new ArrayList<>();

        for(int i=0;i<amtWeekly.size();i++)
        {
            //String c=arr.get(i-1);
            Double a=amtWeekly.get(i);
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
        final int count =0;
        // what I want

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //String date[]=ydata.get((int)value).split("/");
                String date[]=xDates.get((int)value).split("/");
                String d=date[0]+"/"+date[1];
                return d;
            }
        });

    }
    /*
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
    */

    // getting weekly amount
    public void weeklyExp(){
        int count=0;
        amtWeekly = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String begin = parseLastDate(count);
        double amountCount=0;
        for(int i =0;i<dates.length;i++){
            Date date1;
            Date date2;
            try {
                date1 = sdf.parse(dates[i]);
                date2 = sdf.parse(begin);
                if(date1.compareTo(date2) > 0){
                    amtWeekly.add(amountCount);
                    xDates.add(begin);
                    begin = parseLastDate(i);
                    amountCount = amounts[i];
                }
                else {
                    amountCount += amounts[i];
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        begin = parseLastDate(dates.length-1);
        xDates.add(begin);
        amtWeekly.add(amountCount);
    }

    public String parseLastDate(int position){
        String[] str = dates[position].split("/");
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/YYYY");
        LocalDate now = new LocalDate(Integer.parseInt(str[2]),Integer.parseInt(str[0]),Integer.parseInt(str[1]));
        LocalDate monday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
        String lastDate = monday.toString(dtf);
        return lastDate;
    }

    public String suggest(){
        String suggest;
        if(predY>amtWeekly.get(amtWeekly.size()-1)){
            suggest = "Your Next Week Expenditure is going to be higher! Please spend carefully!";

        }
        else{
            suggest = "Your Next Week Expenditure is going to be lower! Keep it Up!";
        }
        return suggest;
    }
}
