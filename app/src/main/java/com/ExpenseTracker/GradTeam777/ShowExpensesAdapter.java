package com.ExpenseTracker.GradTeam777;;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vinya on 12/2/2016.
 */
public class ShowExpensesAdapter extends ArrayAdapter<ExpenseListItem> {
        public ShowExpensesAdapter(Context context, ArrayList<ExpenseListItem> resource) {
            super(context, R.layout.custom_expenses_list_adapter, resource);
        }

        //int index;
    CheckBox checkBox;
    ImageView itemImage;
    TextView itemDate;
    TextView itemAmount;


    @Override
    public View getView(final int position, View row, final ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        row = minflater.inflate(R.layout.custom_expenses_list_adapter, parent, false);
        checkBox=(CheckBox)row.findViewById(R.id.checkBox);

        String date=getItem(position).getDate();
        itemDate=(TextView)row.findViewById(R.id.showDateView);
        itemDate.setText(date);

        double amount=getItem(position).getAmount();
        String showAmount = Double.toString(amount);
        itemAmount = (TextView)row.findViewById(R.id.showAmountView);
        itemAmount.setText(showAmount);

        String filePath = getItem(position).getImagePath();
        itemImage = (ImageView) row.findViewById(R.id.billImageView);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        itemImage.setImageBitmap(bitmap);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb=(CheckBox)v.findViewById(R.id.checkBox);
                if(cb.isChecked())
                {
                    getItem(position).setForDeletion();
                }
            }
        });
        return row;
    }
}
