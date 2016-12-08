package com.ExpenseTracker.GradTeam777;


import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {

    private static final String TAG = "MainActivity";

    static final int SCAN_IMAGE = 3; // code for scan image activity
    static final int SHOW_EXP = 4; // code for predict expenses activity
    static final int SHOW_BILL = 5; // code for show bill list activity

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+ "/OCR/";
    public static final String lang = "eng";
    static final int MY_REQUEST_CODE = 1; // code for camera permissions

    Button scanBill, predExp, showBills, contactUs;

    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scanBill = (Button) getView().findViewById(R.id.scan_btn);
        predExp = (Button)  getView().findViewById(R.id.show_exp);
        showBills = (Button)  getView().findViewById(R.id.show_bills);
        contactUs = (Button)  getView().findViewById(R.id.query_button);

        //isStoragePermissionGranted();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            }
        }
        else {
            prepareDirectory(DATA_PATH + "tessdata/");

            //----------------------train------------------------
            copyData();
            Log.v(TAG,"DATA TRAINED");
        }

        scanBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    addFrag rdb = new addFrag();
                    fragmentTransaction.replace(R.id.frameRight, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                else {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    addFrag rdb = new addFrag();
                    fragmentTransaction.replace(R.id.frame, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        predExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    predExpFrag rdb = new predExpFrag();
                    fragmentTransaction.replace(R.id.frameRight, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                else {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    predExpFrag rdb = new predExpFrag();
                    fragmentTransaction.replace(R.id.frame, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        showBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    myBillFrag rdb = new myBillFrag();
                    fragmentTransaction.replace(R.id.frameRight, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                else {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    myBillFrag rdb = new myBillFrag();
                    fragmentTransaction.replace(R.id.frame, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    contactUsFrag rdb = new contactUsFrag();
                    fragmentTransaction.replace(R.id.frameRight, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                else {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    contactUsFrag rdb = new contactUsFrag();
                    fragmentTransaction.replace(R.id.frame, rdb);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

    }

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

                AssetManager assetManager = getActivity().getAssets();
                InputStream in = getActivity().getAssets().open("tessdata/"+lang + ".traineddata");

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
        SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(getContext());
        double[] amt = {50.23,20.76,30.34,50.11,40.465,50.13,19.34,60,59.24,22.45,65.24,15.67,34.44,38.99,25.73,50.22,79.11,3.4,30.22};
        String[] dates = {"10/03/2016","10/07/2016","10/10/2016","10/12/2016","10/18/2016","10/22/2016","10/25/2016","10/27/2016","10/31/2016","11/04/2016","11/09/2016","11/10/2016","11/14/2016","11/18/2016","11/21/2016","11/22/2016","11/28/2016","12/02/2016","12/07/2016"};
        String url = "/storage/emulated/0/OCR/Receipts/NoBill.jpg";

        for(int i=0;i<amt.length;i++){
            helper.insertEntry(dates[i],amt[i],url);
        }
    }
}
