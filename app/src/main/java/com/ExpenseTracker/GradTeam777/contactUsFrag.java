package com.ExpenseTracker.GradTeam777;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class contactUsFrag extends Fragment {

    EditText subject,message,name,email;
    Button send;
    public contactUsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subject=(EditText) getView().findViewById(R.id.subject);
        message=(EditText)getView().findViewById(R.id.message);
        send=(Button)getView().findViewById(R.id.send);
        name=(EditText)getView().findViewById(R.id.name);
        email=(EditText)getView().findViewById(R.id.email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub=subject.getText().toString();
                String msg=message.getText().toString();
                String nm=name.getText().toString();
                String eml=email.getText().toString();

                if(sub.equals("")||msg.equals("")||nm.equals("")||eml.equals(""))
                {
                    Toast.makeText(getContext(), "Please Fill All The Details!!!", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getContext(),"Thank you for Contacting!",Toast.LENGTH_SHORT).show();
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
            /// ------------------check------------------
            getActivity().finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
