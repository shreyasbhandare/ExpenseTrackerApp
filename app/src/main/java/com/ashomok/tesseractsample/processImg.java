package com.ashomok.tesseractsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by shreyas on 11/30/16.
 */

public class processImg {

    Context context;
    Bitmap src, rawImg;
    Uri opUri;

    public processImg(Context context){
        this.context=context;
    }

    //highlight, grayscale and then binary  ---> output
    public Uri convertImage(Uri imgUri){

        try {
            src = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;
        /*
        final double GS_RED = 0.21;
        final double GS_GREEN = 0.72;
        final double GS_BLUE = 0.07;
        */
        // create output bitmap
        rawImg = Bitmap.createScaledBitmap(src, 400, 500, false);

        Bitmap bmOut = Bitmap.createBitmap(rawImg.getWidth(), rawImg.getHeight(), rawImg.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;
        //threshold for binary image(<127 = black ; >127 = white)
        int threshold = 170;
        //Value to add to each pixel to generate highlight
        int value = 22;

        // get image size
        int width = rawImg.getWidth();
        int height = rawImg.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = rawImg.getPixel(x, y);
                // retrieve color
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                R += value;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                G += value;
                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                B += value;
                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }


                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                if(R < threshold){
                    R =  0xFF000000;
                } else{
                    R = 0xFFFFFFFF;
                }

                if(G < threshold){
                    G =  0xFF000000;
                } else{
                    G = 0xFFFFFFFF;
                }

                if(B < threshold){
                    B =  0xFF000000;
                } else{
                    B = 0xFFFFFFFF;
                }

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        opUri = SaveImage(bmOut);

        /*
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmOut.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmOut, "Title", null);
        return Uri.parse(path);*/
        // return final image
        return opUri;
    }

    public Uri SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString()+"/TesseractSample/Processed";
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
}
