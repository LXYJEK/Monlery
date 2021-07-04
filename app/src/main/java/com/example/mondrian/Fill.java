package com.example.mondrian;

public class Fill {
    public float[] pts;
    int color;
    Fill(float start, float end, float top, float bottom, int color) {
        pts = new float[]{start, top, end, bottom};
        this.color = color;
    }
}
