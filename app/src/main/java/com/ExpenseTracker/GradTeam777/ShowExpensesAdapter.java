package com.ExpenseTracker.GradTeam777;;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
        //ASync task to load image
        new LoadImageTask(filePath,itemImage).execute();
        //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //bitmap = Bitmap.createScaledBitmap(bitmap,60,60,true);
        //itemImage.setImageBitmap(bitmap);

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

    //Async task to load the image
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private String imgFilePath;
        private ImageView thumbnail;

        public LoadImageTask(String path, ImageView itemImage){
            this.imgFilePath = path;
            this.thumbnail = itemImage;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
            //Check if necessary
            bitmap = Bitmap.createScaledBitmap(bitmap,85,85,true);
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            thumbnail.setImageBitmap(result);
        }
    }
}
