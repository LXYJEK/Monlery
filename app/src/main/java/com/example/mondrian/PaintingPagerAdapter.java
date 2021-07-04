package com.example.mondrian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaintingPagerAdapter extends RecyclerView.Adapter<PaintingPagerAdapter.PaintingViewHolder> {
    ArrayList<Painting> paintingArrayList;
    private LayoutInflater mInflater;

    PaintingPagerAdapter(Context context, ArrayList<Painting> paintingArrayList) {
        mInflater = LayoutInflater.from(context);
        this.paintingArrayList = paintingArrayList;
    }

    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.painting_card_big,
                parent, false);
        return new PaintingViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder holder, int position) {
        Painting painting = paintingArrayList.get(position);
        int type = painting.type;
        holder.title.setText(painting.name);
        switch (type) {
            case 0:
                holder.paintingView0.setPainting(painting);
                holder.paintingView0.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.paintingView1.setPainting(painting);
                holder.paintingView1.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.paintingView2.setPainting(painting);
                holder.paintingView2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return paintingArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class PaintingViewHolder extends RecyclerView.ViewHolder {
        final PaintingPagerAdapter paintingListAdapter;
        public final PaintingView paintingView0;
        public final PaintingView paintingView1;
        public final PaintingView paintingView2;
        public final TextView title;
        PaintingViewHolder(View view, PaintingPagerAdapter adapter) {
            super(view);
            paintingView0 = view.findViewById(R.id.PaintingView0);
            paintingView1 = view.findViewById(R.id.PaintingView1);
            paintingView2 = view.findViewById(R.id.PaintingView2);
            title = view.findViewById(R.id.PaintingTitle);
            paintingListAdapter = adapter;
        }
    }
}

