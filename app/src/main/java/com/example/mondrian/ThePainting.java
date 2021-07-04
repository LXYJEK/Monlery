package com.example.mondrian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class ThePainting extends AppCompatActivity {

    PaintingView pv0;
    PaintingView pv1;
    PaintingView pv2;
    TextView title;
    Painting painting;
    PopupMenu popup;
    MaterialButton moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_painting);
        pv0 = findViewById(R.id.PaintingView0);
        pv1 = findViewById(R.id.PaintingView1);
        pv2 = findViewById(R.id.PaintingView2);
        title = findViewById(R.id.TitleText);
        moreButton = findViewById(R.id.More);

        popup = new PopupMenu(this, moreButton);
        popup.inflate(R.menu.menu);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.save:
                    saveImage();
                    return true;
                case R.id.share:
                    shareImage();
                    return true;
                default:
                    return false;
            }
        });

        Intent intent = getIntent();
        String paintingStr = intent.getStringExtra("painting");
        painting = new Gson().fromJson(paintingStr, Painting.class);
        title.setText(painting.name);
        switch (painting.type) {
            case 0:
                pv0.setPainting(painting);
                pv0.setVisibility(View.VISIBLE);
                break;
            case 1:
                pv1.setPainting(painting);
                pv1.setVisibility(View.VISIBLE);
                break;
            case 2:
                pv2.setPainting(painting);
                pv2.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void saveImage() {
        Bitmap bitmap = generateImage(painting);
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, painting.name, "A Monlery Painting");
        Toast.makeText(ThePainting.this, "已保存至系统相册", Toast.LENGTH_SHORT).show();
    }

    public void shareImage() {
        Intent shareIntent = new Intent();
        Bitmap bitmap = generateImage(painting);
        Uri paintingUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, painting.name, "A Monlery Painting"));
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, paintingUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享"));
    }

    public Bitmap generateImage(Painting painting) {
        int height = 960, width = 960;
        if(painting.type == 0) {
            height = 720;
        } else if(painting.type == 2) {
            width = 720;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        for(Fill fill : painting.fills) {
            paint.setColor(fill.color);
            canvas.drawRect(fill.pts[0], fill.pts[1], fill.pts[2], fill.pts[3], paint);
        }
        paint.setColor(Color.BLACK);
        for(Line line : painting.lines) {
            canvas.drawLines(line.pts, paint);
        }
        return bitmap;
    }

    public void showList(View view) {
        popup.show();
    }
}