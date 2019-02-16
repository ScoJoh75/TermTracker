package com.example.mytermtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

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
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessment_name);
            assessmentGoalDate = itemView.findViewById(R.id.assessment_goal_date);
            assessmentTypeView = itemView.findViewById(R.id.assessment_type);
            deleteButton = itemView.findViewById(R.id.delete_button);

            final Context context = itemView.getContext();

            itemView.setOnClickListener(this);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBHelper myhelper = new DBHelper(context);
                    myhelper.getWritableDatabase();

                    int position = getAdapterPosition();
                    myhelper.deleteAssessment(getItem(position).getId());
                } // end onClick
            }); // end setOnClickListener
        } // end ViewHolder

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
