package com.example.mondrian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaintingListAdapter extends RecyclerView.Adapter<PaintingListAdapter.PaintingViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Painting item);
    }

    ArrayList<Painting> paintingArrayList;
    private LayoutInflater mInflater;
    OnItemClickListener listener;

    PaintingListAdapter(Context context, ArrayList<Painting> paintingArrayList, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.paintingArrayList = paintingArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.painting_card_small,
                parent, false);
        return new PaintingViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder holder, int position) {
        Painting painting = paintingArrayList.get(position);
        int type = painting.type;
        holder.view.setOnClickListener(v -> listener.onItemClick(painting));
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
        final PaintingListAdapter paintingListAdapter;
        public final View view;
        public final PaintingView paintingView0;
        public final PaintingView paintingView1;
        public final PaintingView paintingView2;
        public final TextView title;
        PaintingViewHolder(View view, PaintingListAdapter adapter) {
            super(view);
            this.view = view;
            paintingView0 = view.findViewById(R.id.PaintingView0);
            paintingView1 = view.findViewById(R.id.PaintingView1);
            paintingView2 = view.findViewById(R.id.PaintingView2);
            title = view.findViewById(R.id.PaintingTitle);
            paintingListAdapter = adapter;
        }
    }
}

