package com.example.mondrian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class PaintingView extends View {

    ViewPainter viewPainter;
    Painting painting;

    public PaintingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        viewPainter = null;
        painting = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int height = getHeight();
        viewPainter = new ViewPainter(width, height);
    }

    void setPainting(Painting painting) {
        this.painting = painting;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(viewPainter != null && painting != null) {
            viewPainter.drawCanvas(canvas, this.painting);
        }
    }
}
