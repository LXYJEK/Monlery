package com.example.mondrian;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

@Entity(tableName = "painting_table")
public class  PaintingData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String paintingStr;

    public int getId() {
        return id;
    }

    public String getPaintingStr() {
        return paintingStr;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPaintingStr(String paintingStr) {
        this.paintingStr = paintingStr;
    }

    Painting getPainting() {
        return new Gson().fromJson(paintingStr, Painting.class);
    }

    PaintingData() {}

    PaintingData(Painting painting) {
        paintingStr = new Gson().toJson(painting);
    }

}
