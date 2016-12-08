package com.ExpenseTracker.GradTeam777;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class myBillFrag extends Fragment {

    SQLiteDatabaseHelper help;
    private ListView lView;
    private Button sort;
    private Button delete;
    private String img_path;

    ArrayList<ExpenseListItem> myList = new ArrayList<>();
    ShowExpensesAdapter adapter;
    private static final int CAMERA_REQ_CODE = 1;

    public myBillFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lView = (ListView) getView().findViewById(R.id.ListView1);
        adapter = new ShowExpensesAdapter(getContext(), myList);
        sort = (Button)getView().findViewById(R.id.sortButton);

        //take from db and
        loadListData();
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //go to image zoom activity with the path of the image
                Intent intent = new Intent(getContext(), ImageZoom.class);
                String path = myList.get(position).getImagePath().toString();
                intent.putExtra("image_path", path);
                startActivity(intent);
            }
        });

        //button to delete listview items
        delete = (Button)getView().findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedEntries();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatewiseListData();
            }
        });

    }

    //Load from db and create the ArrayList
    public void loadListData(){
        help = new SQLiteDatabaseHelper(getContext());
        myList.clear();
        Cursor result = help.getLatestFirstEntries();
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

    public void loadDatewiseListData(){
        help = new SQLiteDatabaseHelper(getContext());
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
        help = new SQLiteDatabaseHelper(getContext());

        for(int i=0;i<myList.size();i++) {
            if (myList.get(i).getForDeletion() == true) {
                System.out.println("The item amount is" + myList.get(i).getAmount());
                Integer deletedRows = help.deleteData(Integer.toString(myList.get(i).getId()));
                if(deletedRows > 0)
                    Toast.makeText(getContext(),"Records Deleted", Toast.LENGTH_LONG).show();
                myList.remove(i);
            }
        }
        //After deleting, look at the db and reload the listview
        loadListData();

    }

}
