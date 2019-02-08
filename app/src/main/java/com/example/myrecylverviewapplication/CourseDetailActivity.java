package com.example.myrecylverviewapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CourseDetailActivity extends AppCompatActivity {

    DBHelper myHelper;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        course = intent.getParcelableExtra("FullCourse");

        myHelper = new DBHelper(CourseDetailActivity.this);
        myHelper.getWritableDatabase();

        TextView courseTitle = findViewById(R.id.course_detail_title);
        TextView courseStartDate = findViewById(R.id.course_start_date);
        TextView courseEndDate = findViewById(R.id.course_end_date);
        TextView courseStatus = findViewById(R.id.course_status);
        TextView courseMentorName = findViewById(R.id.course_mentor_name);
        TextView courseMentorEmail = findViewById(R.id.course_mentor_email);
        TextView courseMentorPhone = findViewById(R.id.course_mentor_phone);
        TextView courseNotes = findViewById(R.id.course_notes);

        courseTitle.setText(course.getCourseTitle());
        courseStartDate.setText(course.getStartDate().toString());
        courseEndDate.setText(course.getEndDate().toString());
        courseStatus.setText(course.getStatus());
        courseMentorName.setText(course.getMentorName());
        courseMentorEmail.setText(course.getMentorEmail());
        courseMentorPhone.setText(course.getMentorPhone());
        courseNotes.setText(course.getNotes());

        ImageButton deleteCourse = findViewById(R.id.delete_course_button);
        deleteCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    myHelper.deleteCourse(course.getId());
                    myHelper.close();
                    finish();
            } // end onClick
        });

        ImageButton shareNotes = findViewById(R.id.share_notes);
        shareNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO Share them notes!
            }
        });


    } // end onCreate
} // end CourseDetailActivity
