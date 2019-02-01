package com.example.myrecylverviewapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TermViewAdapter extends RecyclerView.Adapter<TermViewAdapter.ViewHolder> {

    private List<Term> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private static final String TAG = "TermViewAdapter";

    // data is passed into the constructor
    TermViewAdapter(Context context, List<Term> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Term term = mData.get(position);
        holder.myTextView.setText(term.getTermName());
        holder.termStartDate.setText(term.getStartDate().toString());
        holder.termEndDate.setText(term.getEndDate().toString());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView termStartDate;
        TextView termEndDate;
        ImageButton editButton;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTermName);
            termStartDate = itemView.findViewById(R.id.tvTermStartDate);
            termEndDate = itemView.findViewById(R.id.tvTermEndDate);
            editButton = itemView.findViewById(R.id.termEditButton);

            itemView.setOnClickListener(this);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Edit Button Clicked for: " + getItem(getAdapterPosition()).getTermName() + " With ID of: " + getItem(getAdapterPosition()).getId());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Term getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
