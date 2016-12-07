package com.ExpenseTracker.GradTeam777;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class showBillList extends AppCompatActivity {

    SQLiteDatabaseHelper help;
    private ListView lView;
    private Button clickImage;
    private Button delete;
    private String img_path;
    ArrayList<ExpenseListItem> myList = new ArrayList<>();
    ShowExpensesAdapter adapter;
    private static final int CAMERA_REQ_CODE = 1;

    //hardcoded entries
    String imageUrl1 = "/storage/emulated/0/Pictures/1480752678636.jpg";
    String imageUrl2 = "/storage/emulated/0/Pictures/1480752728176.jpg";
    String imageUrl3 = "/storage/emulated/0/Pictures/1480752755062.jpg";
    double amt1 = 30.222;
    double amt2 = 12.123;
    double amt3 = 19.123;
    String date1 = "12/1/2016";
    String date2 = "12/2/2016";
    String date3 = "12/3/2016";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill_list);

        //Put entries into the table
        /*
        help = new SQLiteDatabaseHelper(this);
        help.insertEntry(date1,amt1,imageUrl1);
        help.insertEntry(date2,amt2,imageUrl2);
        help.insertEntry(date3,amt3,imageUrl3);*/

        lView = (ListView) findViewById(R.id.ListView1);
        adapter = new ShowExpensesAdapter(this, myList);

        //take from db and
        loadListData();
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //go to image zoom activity with the path of the image
                Intent intent = new Intent(getBaseContext(), ImageZoom.class);
                String path = myList.get(position).getImagePath().toString();
                intent.putExtra("image_path", path);
                startActivity(intent);
            }
        });

        //button to delete listview items
        delete = (Button)findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedEntries();
            }
        });
    }

    //Load from db and create the ArrayList
    public void loadListData(){
        help = new SQLiteDatabaseHelper(this);
        myList.clear();
        Cursor result = help.getEntries();
        while(result.moveToNext()){
            ExpenseListItem listItem = new ExpenseListItem();
            listItem.setId(Integer.parseInt(result.getString(0)));
            listItem.setDate(result.getString(1));
            listItem.setAmount(result.getDouble(2));
            listItem.setImagePath(result.getString(3));
            myList.add(listItem);
            adapter.notifyDataSetChanged();
        }
    }

    //Delete function
    public void deleteSelectedEntries(){
        help = new SQLiteDatabaseHelper(this);

        for(int i=0;i<myList.size();i++) {
            if (myList.get(i).getForDeletion() == true) {
                System.out.println("The item amount is" + myList.get(i).getAmount());
                Integer deletedRows = help.deleteData(Integer.toString(myList.get(i).getId()));
                if(deletedRows > 0)
                    Toast.makeText(this,"Records Deleted", Toast.LENGTH_LONG).show();
                myList.remove(i);
            }
        }
        //After deleting, look at the db and reload the listview
        loadListData();

    }

}

