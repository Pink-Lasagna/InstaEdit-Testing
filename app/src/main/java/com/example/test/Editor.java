package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Editor extends AppCompatActivity {
    CanvasView canv;
    int height;
    int width;
    int realheight;
    int realwidth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            height = getWindowHeight();
            width = getWindowWidth();
            realheight = getRealWindowHeight();
            realwidth = getRealWindowWidth();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent i = getIntent();
        canv = findViewById(R.id.canvasView);
        ConstraintLayout constraintLayout = findViewById(R.id.canvasvonstraint);
        Uri uri = i.getParcelableExtra("imageuri");
        Bitmap raw = uriToBitmap(uri);
        constraintLayout.setOnTouchListener(new MultiTouchListener());
        ConstraintLayout.LayoutParams params = new Constraints.LayoutParams(getWidth()*2, getHeight()*2);
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        constraintLayout.setLayoutParams(params);
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
        return displayMetrics.heightPixels;
    }

    private int getWindowWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public int getRealHeight() {
        return realheight;
    }

    public int getRealWidth() {
        return realwidth;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}