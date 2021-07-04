package com.example.mondrian;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Switch;

public class
ViewPainter {
    Paint paint;
    int width;
    int height;
    ViewPainter(int width, int height) {
        paint = new Paint();
        paint.setStrokeWidth((float) 16.0);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        this.width = width;
        this.height = height;
    }

    public void drawCanvas(Canvas canvas, Painting painting) {
        float stdHeight = 960f, stdWidth = 960f;
        if(painting.type == 0) {
            stdHeight = 720f;
        } else if(painting.type == 2) {
            stdWidth = 720f;
        }
        float SCALE = Math.max(width / stdWidth, height / stdHeight);
        float TRANSLATEX = (width - stdWidth * SCALE) / 2;
        float TRANSLATEY = (height - stdHeight * SCALE) / 2;
        canvas.translate(TRANSLATEX, TRANSLATEY);
        canvas.scale(SCALE, SCALE);
        for(Fill fill : painting.fills) {
            paint.setColor(fill.color);
            canvas.drawRect(fill.pts[0], fill.pts[1], fill.pts[2], fill.pts[3], paint);
        }
        paint.setColor(Color.BLACK);
        for(Line line : painting.lines) {
            canvas.drawLines(line.pts, paint);
        }
    }
}
