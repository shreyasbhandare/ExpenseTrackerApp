package com.ExpenseTracker.GradTeam777;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class contactUs extends AppCompatActivity {

    EditText subject,message,name,email;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        subject=(EditText)findViewById(R.id.subject);
        message=(EditText)findViewById(R.id.message);
        send=(Button)findViewById(R.id.send);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub=subject.getText().toString();
                String msg=message.getText().toString();
                String nm=name.getText().toString();
                String eml=email.getText().toString();

                if(sub.equals("")||msg.equals("")||nm.equals("")||eml.equals(""))
                {
                    Toast.makeText(contactUs.this, "Please Fill All The Details!!!", Toast.LENGTH_SHORT).show();
                }
                else {

                    subject.setText(" ");
                    subject.setHint("SUBJECT");
                    message.setText(" ");
                    message.setHint("MESSAGE");
                    name.setText(" ");
                    name.setHint("NAME");
                    email.setText(" ");
                    email.setHint("EMAIL");

                    sendemail(sub, msg);
                    addtofirebase(nm, eml, sub, msg);
                }
                Toast.makeText(getApplicationContext(),"Thank you for Contacting!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addtofirebase(String nm,String eml, String sub, String msg)
    {
        Long tsLong = System.currentTimeMillis();
        Date df = new java.util.Date(tsLong);
        String timestamp = new SimpleDateFormat("MM/dd hh:mm:ss a").format(df);
        String[] t=timestamp.split("/");
        String time=t[0]+"-"+t[1];
        System.out.println(timestamp);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Query");
        DatabaseReference bart = ref.child(time);
        bart.child("NAME").setValue(nm);
        bart.child("EMAIL").setValue(eml);
        bart.child("SUBJECT").setValue(sub);
        bart.child("MESSAGE").setValue(msg, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                System.out.println("Added to database");
            }
        });
    }



    public void sendemail(String sub,String msg)
    {
        Log.i("Send email", "");

        String[] TO = {"varunvnv.7@gmail.com","bhandare.shreyas@gmail.com","vinayak.ghosh@gmail.com"};
        String[] CC = {};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(contactUs.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}