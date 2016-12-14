package com.ExpenseTracker.GradTeam777;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class addFrag extends Fragment {

    private static final int REQUEST_CODE = 99;
    private Button scanButton;
    private Button cameraButton;
    private Button mediaButton;
    private static final int POP_REQ = 55;
    public static double Total;
    private parseText parsetext;
    Handler handler;

    TessBaseAPI tessBaseAPI;


    EditText amount;
    EditText edtDate;
    Button add_button;
    //Button scan_button;
    String dt;
    String amn;
    SQLiteDatabaseHelper myDB;

    final Calendar c=Calendar.getInstance();

    Uri outputFileUri, rawFileUri;
    private static final String TAG = "MainActivity";

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+ "/OCR/";
    public static final String lang = "eng";

    public addFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDB = new SQLiteDatabaseHelper(getContext());
        init();


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amn=amount.getText().toString();
                dt=edtDate.getText().toString();

                if(!isDouble(amn) || amn.equals("") || dt.equals("")){
                    Toast.makeText(getContext(),"Enter All Details Properly!",Toast.LENGTH_SHORT).show();
                }
                else{
                    double amt = Double.parseDouble(amn);
                    String url = "/storage/emulated/0/OCR/Receipts/NoBill.jpg";
                    myDB.insertEntry(dt,amt,url);
                    Toast.makeText(getContext(),"Record Added!",Toast.LENGTH_SHORT).show();
                }
                amount.setText("");
                edtDate.setText("");
            }
        });
    }

    private void init() {
        //manual add options
        amount=(EditText)getView().findViewById(R.id.amount);
        add_button=(Button)getView().findViewById(R.id.add_button);
        edtDate=(EditText)getView().findViewById(R.id.edtDte);

        // scan options
        scanButton = (Button) getView().findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new ScanButtonClickListener());
        cameraButton = (Button) getView().findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        mediaButton = (Button) getView().findViewById(R.id.mediaButton);
        mediaButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_MEDIA));

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date,
                        c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) ).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set( Calendar.MONTH, monthOfYear );
            c.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            setCurrentDateOnView();
        }
    };

    public void setCurrentDateOnView() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        edtDate.setText(sdf.format(c.getTime()));
    }
    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(getContext(), ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                getActivity().getContentResolver().delete(uri, null, null);
                outputFileUri = SaveImage(bitmap);

                Total = startOCR(bitmap);
                // start pop up activity for reviewing scanned Total
                startActivityForResult(new Intent(getContext(),Pop.class),POP_REQ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public String detectText(Bitmap bitmap) {

        //TessDataManager.initTessTrainedData(context);
        tessBaseAPI = new TessBaseAPI();

        tessBaseAPI.setDebug(true);

        tessBaseAPI.init(DATA_PATH, lang); //Init the Tess with the trained data file, with english language

        tessBaseAPI.setImage(bitmap);

        String text = tessBaseAPI.getUTF8Text();

        //Log.d(TAG, "Got data: " + text);
        tessBaseAPI.end();

        return text;
    }

    public void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                //Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            //Log.i(TAG, "Created directory " + path);
        }
    }

    public double startOCR(Bitmap bitmap) {

        double tot=0;

        try {
            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);*/

            // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imgUri);
            String result = detectText(bitmap);
            parsetext = new parseText(result);
            tot = parsetext.parseRawText();
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return tot;
    }

    public Uri SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString()+"/OCR/Processed/";
        File myDir = new File(root);
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "prReceipt-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String img_path = root + "/" + fname;

        return Uri.fromFile(new File(img_path));
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
