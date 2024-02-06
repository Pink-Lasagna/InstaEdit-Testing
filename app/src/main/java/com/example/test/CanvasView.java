package com.example.test;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

public class CanvasView extends View implements RotationGestureDetector.RotationListener {
    private Bitmap bitmap;
    private final Canvas canvas = new Canvas();
    private final Paint paint = new Paint();
    private final Path path = new Path();
    public Matrix matrix = new Matrix();
    public boolean blockcanv;
    private final Editor editor;
    float totalScale = 1f;
    float pivotX;
    float pivotY;
    private final RotationGestureDetector mRotationDetector;
    private final ScaleGestureDetector mScaleDetector;
    float rotation = 0f;

    public CanvasView(Context context, AttributeSet atts) {
        super(context,atts);
        editor = (Editor) context;
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        blockcanv = false;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mRotationDetector = new RotationGestureDetector(this);
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.save();
        matrix.setScale(totalScale, totalScale, pivotX, pivotY);
        matrix.preRotate(rotation, pivotX, pivotY);
        canvas.drawBitmap(bitmap,matrix,null);
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
        canvas.restore();
    }
    public void addBitmap(Bitmap raw){
        float startscale = 0;
        System.out.println(editor.getHeight());
        System.out.println(editor.getWidth());
        if((float)raw.getHeight()-editor.getHeight()>(float)raw.getWidth()-editor.getWidth()){
            startscale = (float) editor.getHeight()/(raw.getHeight() * 2);
        } else{
            startscale = (float) editor.getWidth()/ (raw.getWidth()*2) ;
        }
        System.out.println(startscale);
        bitmap = Bitmap.createBitmap(raw.getWidth(),raw.getHeight(),Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(raw,matrix,null);
        canvas.scale(startscale,startscale);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotationDetector.onTouch(event);
        float xPos = event.getX();
        float yPos = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                break;
        }
        invalidate();
        return true;
    }
    @Override
    public void onRotate(float deltaAngle) {
        rotation += deltaAngle;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            totalScale *= detector.getScaleFactor();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            pivotX = detector.getFocusX();
            pivotY = detector.getFocusY();
            return true;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
