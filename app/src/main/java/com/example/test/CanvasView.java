package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

public class CanvasView extends View {
    private Bitmap bitmap;
    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    private Path path = new Path();
    public Matrix matrix = new Matrix();
    public boolean blockcanv;
    private Editor editor;
    private boolean pressDown;

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
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
        canvas.restore();
    }
    public void addBitmap(Bitmap raw){
        float startscale = 1f;
        if ((float) raw.getHeight() - editor.getHeight() > (float) raw.getWidth() - editor.getWidth()) {
            startscale = (float) editor.getHeight() / (raw.getHeight() * 1.5f);
        } else {
            startscale = (float) editor.getWidth() / (raw.getWidth() * 1.5f);
        }
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(raw.getWidth(), raw.getHeight());
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        this.setLayoutParams(layoutParams);
        bitmap = Bitmap.createBitmap(raw.getWidth(), raw.getHeight(),Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(raw,0,0,null);
        this.setScaleX(startscale);
        this.setScaleY(startscale);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(blockcanv) return false;
        System.out.println(event.getAction());
        float xPos = event.getX();
        float yPos = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                pressDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pressDown) path.lineTo(xPos,yPos);
                else return false;
                break;
            case MotionEvent.ACTION_UP:
                if(pressDown){
                    path.lineTo(xPos,yPos);
                    pressDown = false;
                } else return false;
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case 261:
                event.setAction(MotionEvent.ACTION_DOWN);
                blockcanv = true;
                break;
            case 6:
                if(blockcanv){
                    blockcanv = false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

}
