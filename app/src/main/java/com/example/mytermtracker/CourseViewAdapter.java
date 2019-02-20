package com.example.mytermtracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import static com.example.mytermtracker.MainActivity.allCourses;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {

    private List<Course> mData;
    private LayoutInflater mInflater;
    private CourseClickListener mClickListener;

    // data is passed into the constructor
    CourseViewAdapter(Context context, List<Course> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.course_view_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mData.get(position);
        holder.courseNameView.setText(course.getCourseTitle());
        holder.courseStartDate.setText(course.getStartDate().toString());
        holder.courseEndDate.setText(course.getEndDate().toString());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView courseNameView;
        TextView courseStartDate;
        TextView courseEndDate;
        ImageButton editButton;

        ViewHolder(View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.tvCourseName);
            courseStartDate = itemView.findViewById(R.id.tvCourseStartDate);
            courseEndDate = itemView.findViewById(R.id.tvCourseEndDate);
            editButton = itemView.findViewById(R.id.courseEditButton);

            final Context context = itemView.getContext();

            itemView.setOnClickListener(this);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int listposition = allCourses.indexOf(getItem(position));
                    Intent intent = new Intent(context, AddCourseActivity.class);
                    intent.putExtra("listposition", listposition);
                    intent.putExtra("FullCourse", getItem(position));

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onCourseClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Course getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(CourseClickListener courseClickListener) {
        this.mClickListener = courseClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface CourseClickListener {
        void onCourseClick(View view, int position);
    }
}
