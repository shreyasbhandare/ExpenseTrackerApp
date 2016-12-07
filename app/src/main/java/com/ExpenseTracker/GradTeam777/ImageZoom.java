package com.ExpenseTracker.GradTeam777;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoom extends AppCompatActivity {

    private String imgPath;
    private ImageView zoomView;
    PhotoViewAttacher attacher;
    //private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        imgPath = getIntent().getExtras().getString("image_path");
        zoomView = (ImageView) findViewById(R.id.zoomImage);
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        bitmap = Bitmap.createScaledBitmap(bitmap,400,600,true);
        zoomView.setImageBitmap(bitmap);

        attacher = new PhotoViewAttacher(zoomView);
    }
}
