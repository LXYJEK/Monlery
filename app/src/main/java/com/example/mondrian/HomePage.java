 package com.example.mondrian;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

 public class HomePage extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ImageView iV = findViewById(R.id.StudioView);
        AnimatedVectorDrawable i1 = (AnimatedVectorDrawable) iV.getDrawable();
        i1.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                ((AnimatedVectorDrawable) drawable).start();
            }
        });
        i1.start();
        List<PaintingData> paintingDatas = PaintingDataDB.getDatabase(this).paintingDataDao().getAllPaintings();
        ArrayList<Painting> paintings = new ArrayList<>();
        for(PaintingData paintingData : paintingDatas) {
            paintings.add(paintingData.getPainting());
        }
        PaintingView iV2 = findViewById(R.id.MineView);
        Animator animator = AnimatorInflater.loadAnimator(HomePage.this, R.animator.ani_image);
        animator.setTarget(iV2);
        final int[] index = {0};
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(paintingDatas.size() == 0) {
                    return;
                } else if(index[0] == paintings.size()) {
                    index[0] = 0;
                }
                iV2.setPainting(paintings.get(index[0]));
                index[0] = index[0] + 1;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();


        PaintingView galleryView = findViewById(R.id.GalleryView);
        String url = ""; //配置为你自己的后端链接
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.length() != 0) {
                            Painting painting = new Gson().fromJson(response.get(0).toString(), Painting.class);
                            galleryView.setPainting(painting);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("1", "onErrorResponse: " + error.toString());
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void toPaintPage(View view) {
        startActivity(new Intent(this, Studio.class));
    }

    public void toMyPaintingsPage(View view) {
        startActivity(new Intent(this, MyPaintings.class));
    }

     public void toTopGalleryPage(View view) {
         startActivity(new Intent(this, TopGallery.class));
     }
}