package com.ExpenseTracker.GradTeam777;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction();
            HomeFrag ldb = new HomeFrag();
            Transaction.replace(R.id.frameLeft, ldb);
            Transaction.addToBackStack(null);
            Transaction.commit();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            addFrag rdb = new addFrag();
            fragmentTransaction.replace(R.id.frameRight, rdb);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            HomeFrag ldb = new HomeFrag();
            fragmentTransaction.replace(R.id.frame, ldb);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}



