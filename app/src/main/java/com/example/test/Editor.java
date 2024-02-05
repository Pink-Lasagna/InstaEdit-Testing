package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Editor extends AppCompatActivity implements RotationGestureDetector.RotationListener {
    CanvasView canv;
    int height;
    int width;
    private RotationGestureDetector mRotationDetector;
    private ScaleGestureDetector mScaleDetector;
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
        canv = (CanvasView) findViewById(R.id.canvasView);
        Uri uri = i.getParcelableExtra("imageuri");
        canv.addBitmap(uriToBitmap(uri));
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        mRotationDetector = new RotationGestureDetector(this);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(event.getY());
        mScaleDetector.onTouchEvent(event);
        //mRotationDetector.onTouch(event);
        canv.invalidate();
        return true;
    }

    @Override
    public void onRotate(float deltaAngle) {
        canv.setRotation(canv.getRotation() + deltaAngle);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float totalScale = 1f;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            totalScale *= detector.getScaleFactor();
            canv.setScaleX(totalScale);
            canv.setScaleY(totalScale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            System.out.println(width + " " + canv.getWidth() + " " + detector.getFocusX());
            System.out.println(height + " " + canv.getHeight()+ " " + detector.getFocusY());
            canv.setPivotX(interDiffCoord(width,detector.getFocusX(),canv.getWidth()));
            canv.getMeasuredHeightAndState();
            canv.setPivotY(interDiffCoord(height,detector.getFocusY(),canv.getHeight()));
            return true;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }
    private float interDiffCoord(float coordview, float coord, float whole){
        return whole/(coordview/coord);
    }

    private int getWindowHeight() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        return realSize.y;
    }
    private int getWindowWidth() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        return realSize.x;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}