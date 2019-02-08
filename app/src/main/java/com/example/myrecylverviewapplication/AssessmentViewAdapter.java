package com.example.myrecylverviewapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import static com.example.myrecylverviewapplication.MainActivity.allAssessments;

public class AssessmentViewAdapter extends RecyclerView.Adapter<AssessmentViewAdapter.ViewHolder> {

    private List<Assessment> mData;
    private LayoutInflater mInflater;
    private AssessmentClickListener mClickListener;

    // data is passed into the constructor
    AssessmentViewAdapter(Context context, List<Assessment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.assessment_view_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Assessment assessment = mData.get(position);
        holder.assessmentNameView.setText(assessment.getAssessmentName());
        holder.assessmentGoalDate.setText(assessment.getGoalDate().toString());
        holder.assessmentTypeView.setText(assessment.getAssessmentType());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView assessmentNameView;
        TextView assessmentGoalDate;
        TextView assessmentTypeView;
        ImageButton editButton;

        ViewHolder(View itemView) {
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessment_name);
            assessmentGoalDate = itemView.findViewById(R.id.assessment_goal_date);
            assessmentTypeView = itemView.findViewById(R.id.assessment_type);
            editButton = itemView.findViewById(R.id.edit_button);

            final Context context = itemView.getContext();

            itemView.setOnClickListener(this);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int listposition = allAssessments.indexOf(getItem(position));
                    Intent intent = new Intent(context, AddAssessmentActivity.class);
                    intent.putExtra("listposition", listposition);
                    intent.putExtra("FullAssessment", getItem(position));

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onAssessmentClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Assessment getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(AssessmentClickListener assessmentClickListener) {
        this.mClickListener = assessmentClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface AssessmentClickListener {
        void onAssessmentClick(View view, int position);
    }
}
