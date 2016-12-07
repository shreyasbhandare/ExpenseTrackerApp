package com.ExpenseTracker.GradTeam777;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.ExpenseTracker.GradTeam777.scanImage.Total;
import static com.scanlibrary.UriClass.filePath;

/**
 * Created by shreyas on 12/1/16.
 */
public class Pop extends Activity {
    private TextView popUpText;
    private EditText editTotal;
    private Button addButton;
    SQLiteDatabaseHelper myDB;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Intent returnIntent = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);


        myDB = new SQLiteDatabaseHelper(this);


        // configuring size of the pop up activity
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(height*.6));

        popUpText = (TextView) findViewById(R.id.popuptext);
        editTotal = (EditText) findViewById(R.id.enterManualTotal);
        addButton = (Button) findViewById(R.id.addManualTotal);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editTotal.getText().toString();
                double manTotal=0;
                //code to add total and date to database
                if(str.equals("")){
                    //add scanned total to DB
                    Calendar c = Calendar.getInstance();
                    String formattedDate = sdf.format(c.getTime());
                    String path = filePath.toString().substring(7);
                    myDB.insertEntry(formattedDate,Total,path);
                }
                else{
                    //add manually entered total to DB
                    Calendar c = Calendar.getInstance();
                    if(isDouble(str)) {
                        manTotal = Double.parseDouble(str);
                        String formattedDate = sdf.format(c.getTime());
                        String path = filePath.toString().substring(7);
                        myDB.insertEntry(formattedDate, manTotal, path);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"Plesae Enter Valid Amount Next Time!",Toast.LENGTH_SHORT).show();
                    }

                }
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        //String sTotal = getIntent().getStringExtra("Total");
        //Total = Float.parseFloat(sTotal);

        setPopUpText(Total);

    }

    public void setPopUpText(double total){
        if(total==-1){
            popUpText.setText("Sorry, Couldn't scan the Total!\nPlease add Manually!");
        }
        else{
            popUpText.setText("Scanned Total is "+total+"$\nEdit if not correct and press Add!");
        }
        return;
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
