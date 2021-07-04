package com.example.mondrian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recyclerView = findViewById(R.id.recyclerView);
        String url = ""; //配置为你自己的后端链接
        JsonArrayRequest request = null;
        request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("1", "uploadImage: " + response.toString());
                    ArrayList<Painting> paintingArrayList = new ArrayList<>();
                    try {
                        for(int i=0; i<response.length(); i++) {
                            Painting painting = new Gson().fromJson(response.get(i).toString(), Painting.class);
                            paintingArrayList.add(painting);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PaintingListAdapter adapter = new PaintingListAdapter(Gallery.this, paintingArrayList, new PaintingListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Painting item) {
                            Intent intent = new Intent(Gallery.this, ThePainting.class);
                            intent.putExtra("painting", new Gson().toJson(item));
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                },
                error -> {
                    Log.d("1", "onErrorResponse: " + error.toString());
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}