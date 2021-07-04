package com.example.mondrian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Painter {
    final int HORIZONTAL = 0;
    final int VERTICAL = 1;
    final int LINE = 0;
    final int COLOR = 1;
    private final int WHITE = Color.parseColor("#eef0eb");
    private final int BLUE = Color.parseColor("#3c4681");
    private final int RED = Color.parseColor("#C53632");
    private final int YELLOW = Color.parseColor("#f3ca18");
    private final Paint paint;
    private final Random random;
    private final Gson gson;
    Painting painting;
    int startFlag;
    int type;
    float SCALE;
    private float WIDTH;
    private float HEIGHT;
    int state = LINE;
    int lineState = HORIZONTAL;
    private final Studio contextActivity;
    private final ArrayList<Line> tempLines;
    private final Stack<Painting> paintingStack;
    private float startX;
    private float startY;
    private boolean isScroll = false;

    Painter(Context context, float width, float height) {
        gson = new Gson();
        paint = new Paint();
        paint.setStrokeWidth((float) 16.0);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        random = new Random();
        startFlag = 0;
        contextActivity = (Studio) context;
        initScale(width, height);
        painting = new Painting();
        tempLines = new ArrayList<>();
        paintingStack = new Stack<>();
        initPainting();
        contextActivity.painter = this;
    }

    void initScale(float width, float height) {
        if(width < height) {
            WIDTH = 720;
            HEIGHT = 960;
            type = 2;
        } else if (width > height) {
            WIDTH = 960;
            HEIGHT = 720;
            type = 0;
        } else {
            WIDTH = 960;
            HEIGHT = 960;
            type = 1;
        }
        SCALE = width / WIDTH;
    }

    private void initPainting() {
        painting.type = type;
        painting.fills.add(new Fill(0, WIDTH, 0, HEIGHT, WHITE));
    }

    void addBorder() {
        painting.lines.clear();
        painting.lines.add(new Line(0, 8, WIDTH, 8));
        painting.lines.add(new Line(WIDTH-8, 8, WIDTH-8, HEIGHT));
        painting.lines.add(new Line(WIDTH-8, HEIGHT-8, 0, HEIGHT-8));
        painting.lines.add(new Line(8, HEIGHT-8, 8, 8));
    }

    void removeBorder() {
        painting.lines.clear();
    }

    int nextColor(int curColor) {
        if(curColor == WHITE) {
            return BLUE;
        } else if(curColor == BLUE) {
            return RED;
        } else if(curColor == RED) {
            return YELLOW;
        } else {
            return WHITE;
        }
    }

    float getStart(float x, float y) {
        float start = 0;
        for(Line line : painting.lines){
            if(y >= line.pts[1] && y <= line.pts[3]) {
                if(line.pts[0] <= x) {
                    start = Math.max(start, line.pts[0]);
                }
            }
        }
        return start;
    }

    float getEnd(float x, float y) {
        float end = WIDTH;
        for(Line line : painting.lines){
            if(y >= line.pts[1] && y <= line.pts[3]) {
                if(line.pts[0] >= x) {
                    end = Math.min(end, line.pts[0]);
                }
            }
        }
        return end;
    }

    float getTop(float x, float y) {
        float top = 0;
        for(Line line : painting.lines) {
            if(x >= line.pts[0] && x <= line.pts[2]) {
                if(line.pts[1] <= y) {
                    top = Math.max(top, line.pts[1]);
                }
            }
        }
        return top;
    }

    float getBottom(float x, float y) {
        float bottom = HEIGHT;
        for(Line line : painting.lines) {
            if(x >= line.pts[0] && x <= line.pts[2]) {
                if(line.pts[1] >= y) {
                    bottom = Math.min(bottom, line.pts[1]);
                }
            }
        }
        return bottom;
    }

    ArrayList<Float> getBetweenXs(float x1, float x2, float y) {
        float start = 0, end = WIDTH;
        ArrayList<Float> Xs = new ArrayList<>();
        for(Line line : painting.lines){
            if(y >= line.pts[1] && y <= line.pts[3]) {
                if(line.pts[0] <= x1) {
                    start = Math.max(start, line.pts[0]);
                }
                if(line.pts[0] >= x2) {
                    end = Math.min(end, line.pts[0]);
                }
                if(line.pts[0] > x1 && line.pts[0] < x2) {
                    Xs.add(line.pts[0]);
                }
            }
        }
        Xs.add(start);
        Xs.add(end);
        Collections.sort(Xs);
        return Xs;
    }

    ArrayList<Float> getBetweenYs(float y1, float y2, float x) {
        float top = 0, bottom = HEIGHT;
        ArrayList<Float> Ys = new ArrayList<>();
        for(Line line : painting.lines){
            if(x >= line.pts[0] && x <= line.pts[2]) {
                if(line.pts[1] <= y1) {
                    top = Math.max(top, line.pts[1]);
                }
                if(line.pts[1] >= y2) {
                    bottom = Math.min(bottom, line.pts[1]);
                }
                if(line.pts[1] > y1 && line.pts[1] < y2) {
                    Ys.add(line.pts[1]);
                }
            }
        }
        Ys.add(top);
        Ys.add(bottom);
        Collections.sort(Ys);
        return Ys;
    }


    boolean isFillContain(Fill fill, float x, float y) {
        return x > fill.pts[0] && x < fill.pts[2] && y > fill.pts[1] && y < fill.pts[3];
    }

    void fillColor(float start, float end, float top, float bottom) {
        int rNum = random.nextInt(10);
        int curColor = WHITE;
        if(rNum == 0) {
            curColor = BLUE;
        } else if(rNum == 1) {
            curColor = RED;
        } else if(rNum == 2) {
            curColor = YELLOW;
        }
        painting.fills.add(new Fill(start, end, top, bottom, curColor));
    }

    void fillColor(float x, float y) {
        Fill curFill = null;
        for(Fill fill : painting.fills) {
            if(isFillContain(fill, x, y)) {
                curFill = fill;
            }
        }
        if(curFill != null) {
            curFill.color = nextColor(curFill.color);
        }
    }

    void reduceFill(float x, float y) {
        for(Fill fill : painting.fills) {
            if(isFillContain(fill, x, y)) {
                painting.fills.remove(fill);
                break;
            }
        }
    }

    void addMultipleTempLine(float x1, float y1, float x2, float y2) {
        if(lineState == HORIZONTAL) {
            if(x1>x2) {
                x1 = x1 + x2;
                x2 = x1 - x2;
                x1 = x1 - x2;
            }
            ArrayList<Float> Xs = getBetweenXs(x1, x2, y1);
            for(int i = 0; i < Xs.size() - 1; i++) {
                float x = (Xs.get(i) + Xs.get(i+1)) / 2;
                addTempLine(x, y1);
            }
        } else if(lineState == VERTICAL) {
            if(y1>y2) {
                y1 = y1 + y2;
                y2 = y1 - y2;
                y1 = y1 - y2;
            }
            ArrayList<Float> Ys = getBetweenYs(y1, y2, x1);
            for(int i = 0; i < Ys.size() - 1; i++) {
                float y = (Ys.get(i) + Ys.get(i+1)) / 2;
                addTempLine(x1, y);
            }
        }
    }

    void addMultipleLine(float x1, float y1, float x2, float y2) {
        paintingStack.push(gson.fromJson(gson.toJson(painting), Painting.class));
        if(lineState == HORIZONTAL) {
            if(x1>x2) {
                x1 = x1 + x2;
                x2 = x1 - x2;
                x1 = x1 - x2;
            }
            ArrayList<Float> Xs = getBetweenXs(x1, x2, y1);
            for(int i = 0; i < Xs.size() - 1; i++) {
                float x = (Xs.get(i) + Xs.get(i+1)) / 2;
                addLine(x, y1);
            }
        } else if(lineState == VERTICAL) {
            if(y1>y2) {
                y1 = y1 + y2;
                y2 = y1 - y2;
                y1 = y1 - y2;
            }
            ArrayList<Float> Ys = getBetweenYs(y1, y2, x1);
            Log.d("1", "addMultipleLine: " + Ys.toString());
            for(int i = 0; i < Ys.size() - 1; i++) {
                float y = (Ys.get(i) + Ys.get(i+1)) / 2;
                addLine(x1, y);
            }
        }
        switchLineStateAndEmit();
    }

    void addSingleLine(float x, float y) {
        paintingStack.push(gson.fromJson(gson.toJson(painting), Painting.class));
        addLine(x, y);
        switchLineStateAndEmit();
    }

    void addLine(float x, float y) {
        this.reduceFill(x, y);
        float start = getStart(x, y);
        float end = getEnd(x, y);
        float top = getTop(x, y);
        float bottom = getBottom(x, y);
        if(lineState == HORIZONTAL) {
            fillColor(start, end, top, y);
            fillColor(start, end, y, bottom);
            painting.lines.add(new Line(start, y, end, y));
        } else if(lineState == VERTICAL) {
            fillColor(start, x, top, bottom);
            fillColor(x, end, top, bottom);
            painting.lines.add(new Line(x, top, x, bottom));
        }
    }

    void clearTempLine() {
        tempLines.clear();
    }

    void addTempLine(float x, float y) {
        float start = getStart(x, y);
        float end = getEnd(x, y);
        float top = getTop(x, y);
        float bottom = getBottom(x, y);
        if(lineState == HORIZONTAL) {
            tempLines.add(new Line(start, y, end, y));
        } else if(lineState == VERTICAL) {
            tempLines.add(new Line(x, top, x, bottom));
        }
    }

    JSONObject generateJSONObject() throws JSONException {
        return new JSONObject(gson.toJson(painting));
    }

    void drawCanvas(Canvas canvas) {
        canvas.scale(SCALE, SCALE);
        for(Fill fill : painting.fills) {
            paint.setColor(fill.color);
            canvas.drawRect(fill.pts[0], fill.pts[1], fill.pts[2], fill.pts[3], paint);
        }
        paint.setColor(Color.GRAY);
        for(Line line : tempLines) {
            canvas.drawLines(line.pts, paint);
        }
        paint.setColor(Color.BLACK);
        for(Line line : painting.lines) {
            canvas.drawLines(line.pts, paint);
        }
        clearTempLine();
    }

    public void switchLineState() {
        if (lineState == HORIZONTAL) {
            lineState = VERTICAL;
        } else if (lineState == VERTICAL) {
            lineState = HORIZONTAL;
        }
    }

    public void switchLineStateAndEmit() {
        switchLineState();
        contextActivity.onRotate();
    }

    public void revoke() {
        if(!paintingStack.empty()) {
            painting = paintingStack.pop();
        }
    }

    boolean handleScroll(float x, float y) {
        if(state == LINE) {
            isScroll = true;
            addMultipleTempLine(startX, startY, x, y);
            return true;
        }
        return false;
    }

    boolean handleScroll(MotionEvent e) {
        float x = e.getX()/SCALE, y = e.getY()/SCALE;
        if(state == LINE) {
            isScroll = true;
            addMultipleTempLine(startX, startY, x, y);
            return true;
        }
        return false;
    }

    private void handleSingleTap(float x, float y) {
        if(state == LINE) {
            addSingleLine(x, y);
        } else if(state == COLOR) {
            fillColor(x, y);
        }
    }

    private void handleScrollTap(float x, float y) {
        clearTempLine();
        addMultipleLine(startX, startY, x, y);
        isScroll = false;
    }

    public boolean handleTouch(MotionEvent e) {
        if(startFlag == 0) {
            return false;
        }
        float x = e.getX() / SCALE, y = e.getY() / SCALE;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                return false;
            case MotionEvent.ACTION_MOVE:
                return handleScroll(x, y);
            case MotionEvent.ACTION_UP:
                if (isScroll) {
                    handleScrollTap(x, y);
                } else {
                    handleSingleTap(x, y);
                }
                return true;
            default:
                return false;
        }
    }
}
