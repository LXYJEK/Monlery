package com.example.mondrian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

public class TopGallery extends AppCompatActivity {

    ViewPager2 topViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_gallery);
        topViewPager = findViewById(R.id.TopViewPager);
        JsonArrayRequest request = null;
        String url = ""; //配置为你自己的后端链接
        request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("1", "uploadImage: " + response.toString());
                    ArrayList<Painting> paintingArrayList = new ArrayList<>();
                    try {
                        for(int i=0; i<response.length(); i++) {
                            Painting painting = new Gson().fromJson(response.get(i).toString(), Painting.class);
                            paintingArrayList.add(0, painting);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PaintingPagerAdapter adapter = new PaintingPagerAdapter(TopGallery.this, paintingArrayList);
                    topViewPager.setAdapter(adapter);
                },
                error -> {
                    Log.d("1", "onErrorResponse: " + error.toString());
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void toGallery(View view) {
        startActivity(new Intent(this, Gallery.class));
    }
}