package com.example.mondrian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;

public class Studio extends AppCompatActivity {

    private PainterView painterView;
    private MaterialButtonToggleGroup toolGroup;
    private MaterialButtonToggleGroup typeGroup;
    private MaterialButtonToggleGroup borderGroup;
    private MaterialButtonToggleGroup nextGroup;
    private MaterialButton lineButton;
    private MaterialButton colorButton;
    private MaterialButton undoButton;
    private MaterialButton nextButton;
    private MaterialButton continueButton;
    private TextInputEditText nameInput;
    RequestQueue queue;
    Painter painter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);
        painterView = findViewById(R.id.PainterView);
        toolGroup = findViewById(R.id.PainterToolGroup);
        typeGroup = findViewById(R.id.TypeGroup);
        borderGroup = findViewById(R.id.BorderGroup);
        nextGroup = findViewById(R.id.NextGroup);
        lineButton = findViewById(R.id.Line);
        colorButton = findViewById(R.id.Color);
        undoButton = findViewById(R.id.Undo);
        nextButton = findViewById(R.id.Next);
        continueButton = findViewById(R.id.Continue);
        nameInput = findViewById(R.id.NameInput);
        toolGroup.check(R.id.Line);
        typeGroup.check(R.id.Type1);
        borderGroup.check(R.id.Border0);
        MaterialButtonToggleGroup.OnButtonCheckedListener toolGroupCheckedListener;
        toolGroupCheckedListener = (group, checkedId, isChecked) -> group.check(checkedId);
        toolGroup.addOnButtonCheckedListener(toolGroupCheckedListener);
        typeGroup.addOnButtonCheckedListener(toolGroupCheckedListener);
        borderGroup.addOnButtonCheckedListener(toolGroupCheckedListener);
        queue = Volley.newRequestQueue(this);
    }

    public void onRotate() {
        if (painter.lineState == painter.HORIZONTAL) {
            lineButton.setIconResource(R.drawable.line_to_hor);
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) lineButton.getIcon();
            avd.start();
        } else {
            lineButton.setIconResource(R.drawable.line_to_ver);
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) lineButton.getIcon();
            avd.start();
        }
    }

    public void rotate() {
        if (painter.lineState == painter.HORIZONTAL) {
            lineButton.setIconResource(R.drawable.line_to_ver);
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) lineButton.getIcon();
            avd.start();
        } else {
            lineButton.setIconResource(R.drawable.line_to_hor);
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) lineButton.getIcon();
            avd.start();
        }
        painter.switchLineState();
    }

    public void revoke(View view) {
        painter.revoke();
        painterView.invalidate();
    }

    public void line(View view) {
        if(painter.state == painter.LINE) {
            rotate();
        } else if(painter.state == painter.COLOR) {
            painter.state = painter.LINE;
            if(painter.lineState == painter.HORIZONTAL) {
                lineButton.setIconResource(R.drawable.line_to_hor);
            } else {
                lineButton.setIconResource(R.drawable.line_to_ver);
            }
        }
    }

    public void color(View view) {
        if(painter.state == painter.LINE) {
            painter.state = painter.COLOR;
            if(painter.lineState == painter.HORIZONTAL) {
                lineButton.setIconResource(R.drawable.line_hor);
            } else {
                lineButton.setIconResource(R.drawable.line_ver);
            }
        }
    }

    public void uploadImage() throws JSONException {
        String url = ""; //配置为你自己的后端链接
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, painter.generateJSONObject(),
                response -> {
                    Painting painting = new Gson().fromJson(response.toString(), Painting.class);
                    Log.d("1", "uploadImage: " + painting.toString());
                },
                error -> {
                    Log.d("1", "onErrorResponse: " + error.toString());
                });
        queue.add(request);
    }

    public void toGallery(View view) {
        startActivity(new Intent(this, Gallery.class));
    }

    public void changeType(View view) {
        ConstraintSet set = new ConstraintSet();
        ConstraintLayout root = findViewById(R.id.root);
        set.clone(root);
        switch (view.getId()) {
            case R.id.Type0:
                set.setDimensionRatio(R.id.PainterView, "4:3");
                break;
            case R.id.Type1:
                set.setDimensionRatio(R.id.PainterView, "1:1");
                break;
            case R.id.Type2:
                set.setDimensionRatio(R.id.PainterView, "3:4");
                break;
        }
        set.applyTo(root);
    }

    public void next(View view) throws JSONException {
        if(typeGroup.getVisibility() == View.VISIBLE) {
            typeGroup.setVisibility(View.GONE);
            borderGroup.setVisibility(View.VISIBLE);
            nextButton.setIconResource(R.drawable.done);
        } else if(borderGroup.getVisibility() == View.VISIBLE) {
            borderGroup.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            toolGroup.setVisibility(View.VISIBLE);
            undoButton.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            painter.startFlag = 1;
        } else if(toolGroup.getVisibility() == View.VISIBLE) {
            toolGroup.setVisibility(View.INVISIBLE);
            undoButton.setVisibility(View.INVISIBLE);
            nameInput.setVisibility(View.VISIBLE);
            continueButton.setIconResource(R.drawable.done);
        } else {
            String name = String.valueOf(nameInput.getText());
            Log.d("1", "next: " + name + name.length());
            if(name.length() != 0) {
                painter.painting.name = name;
            } else {
                painter.painting.name = "未命名";
            }
            PaintingDataDB.getDatabase(this).paintingDataDao().insert(new PaintingData(painter.painting));
            uploadImage();
            Intent intent = new Intent(Studio.this, ThePainting.class);
            intent.putExtra("painting", new Gson().toJson(painter.painting));
            startActivity(intent);
            finish();
        }
    }

    public void setBorder(View view) {
        switch (view.getId()) {
            case R.id.Border0:
                painter.removeBorder();
                painterView.invalidate();
                break;
            case R.id.Border1:
                painter.addBorder();
                painterView.invalidate();
                break;
        }
    }
}