package com.example.mondrian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyPaintings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_paintings);
        RecyclerView rv = findViewById(R.id.PanitingRecycle);
        List<PaintingData> paintingDatas = PaintingDataDB.getDatabase(this).paintingDataDao().getAllPaintings();
        ArrayList<Painting> paintings = new ArrayList<>();
        for(PaintingData paintingData : paintingDatas) {
            paintings.add(paintingData.getPainting());
        }
        PaintingListAdapter adapter = new PaintingListAdapter(this, paintings, new PaintingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Painting item) {
                Intent intent = new Intent(MyPaintings.this, ThePainting.class);
                intent.putExtra("painting", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(this, 4));
    }
}