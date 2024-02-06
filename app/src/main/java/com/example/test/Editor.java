package com.example.test;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class Editor extends AppCompatActivity{
    CanvasView canv;
    int height;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            height = getWindowHeight();
            width = getWindowWidth();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent i = getIntent();
        canv = findViewById(R.id.canvasView);
        Uri uri = i.getParcelableExtra("imageuri");
        canv.addBitmap(uriToBitmap(uri));
    }
    private Bitmap uriToBitmap(Uri uri){
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            Bitmap raw = BitmapFactory.decodeStream(input);
            input.close();
            return raw;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getRealWindowHeight() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        return realSize.y;
    }
    private int getRealWindowWidth() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        return realSize.x;
    }

    private int getWindowHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    private int getWindowWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}