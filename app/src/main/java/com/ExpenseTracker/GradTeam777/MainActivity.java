package com.ExpenseTracker.GradTeam777;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static final int SCAN_IMAGE = 3; // code for scan image activity
    static final int SHOW_EXP = 4; // code for predict expenses activity
    static final int SHOW_BILL = 5; // code for show bill list activity

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+ "/OCR/";
    public static final String lang = "eng";
    static final int MY_REQUEST_CODE = 1; // code for camera permissions

    Button scanBill, predExp, showBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBill = (Button) findViewById(R.id.scan_btn);
        predExp = (Button) findViewById(R.id.show_exp);
        showBills = (Button) findViewById(R.id.show_bills);

        //isStoragePermissionGranted();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            }
        }

        scanBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,scanImage.class);
                startActivityForResult(intent,SCAN_IMAGE);
            }
        });

        predExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, showExpenses.class);
                startActivityForResult(intent,SHOW_EXP);
            }
        });

        showBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, showBillList.class);
                startActivityForResult(intent,SHOW_BILL);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /*
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");

        }
        return true;
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);

            prepareDirectory(DATA_PATH + "tessdata/");

            //----------------------train------------------------
            copyData();
            Log.v(TAG,"DATA TRAINED");
        }
    }

    public void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + path);
        }
    }

    public void copyData(){
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists() || !(new File(DATA_PATH + "Receipts/NoBill.jpg").exists())) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = getAssets().open("tessdata/"+lang + ".traineddata");

                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

                InputStream istr;
                Bitmap noImage = null;
                try {
                    istr = assetManager.open("no-image.jpg");
                    noImage = BitmapFactory.decodeStream(istr);
                } catch (IOException e) {
                    // handle exception
                }

                //saving no bill image
                SaveImage(noImage);

                //saving default week data
                copyWeeklyData();



                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }

    }

    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString()+"/OCR/Receipts/";
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = "NoBill.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyWeeklyData(){
        SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(getApplicationContext());
        double[] amt = {50.23,20.76,30.34,50.11,40.465,50.13,19.34,60,59.24,22.45,65.24,15.67,34.44,38.99,25.73,50.22,79.11,3.4,30.22};
        String[] dates = {"10/03/2016","10/07/2016","10/10/2016","10/12/2016","10/18/2016","10/22/2016","10/25/2016","10/27/2016","10/31/2016","11/04/2016","11/09/2016","11/10/2016","11/14/2016","11/18/2016","11/21/2016","11/22/2016","11/28/2016","12/02/2016","12/07/2016"};
        String url = "/storage/emulated/0/OCR/Receipts/NoBill.jpg";

        for(int i=0;i<amt.length;i++){
            helper.insertEntry(dates[i],amt[i],url);
        }
    }

    public void query_click(View view)
    {
        Intent intent = new Intent(this, QueryActivity.class);
        startActivity(intent);
    }
}



