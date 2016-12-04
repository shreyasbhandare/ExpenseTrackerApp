package com.ExpenseTracker.GradTeam777;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageZoom extends AppCompatActivity {

    private String imgPath;
    private ImageView zoomView;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        imgPath = getIntent().getExtras().getString("image_path");
        zoomView = (ImageView) findViewById(R.id.zoomImage);
        zoomView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageZoom.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
