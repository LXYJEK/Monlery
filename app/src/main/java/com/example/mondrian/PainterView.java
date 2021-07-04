package com.example.mondrian;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

public class PainterView extends View {

    Painter painter;

    Activity contextActivity;

    private final GestureDetectorCompat mDetector;

    public PainterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        contextActivity = (Activity) context;
        class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public void onLongPress(MotionEvent e) {
                painter.handleScroll(e);
                invalidate();
            }
        }
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initPainter();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(painter != null) {
            painter.drawCanvas(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mDetector.onTouchEvent(e);
        if(painter.handleTouch(e)) {
            invalidate();
        }
        return true;
    }

    void initPainter() {
        int width = getWidth();
        int height = getHeight();
        if(painter == null || painter.startFlag == 0) {
            painter = new Painter(contextActivity, width, height);
        }
        invalidate();
    }
}
