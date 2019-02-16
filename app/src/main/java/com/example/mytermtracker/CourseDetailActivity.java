package com.example.mytermtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.mytermtracker.MainActivity.allAssessments;

public class CourseDetailActivity extends AppCompatActivity implements AssessmentViewAdapter.AssessmentClickListener {

    DBHelper myHelper;
    static AssessmentViewAdapter assessmentAdapter;

    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        myHelper = new DBHelper(CourseDetailActivity.this);
        myHelper.getWritableDatabase();

        Intent intent = getIntent();
        course = intent.getParcelableExtra("FullCourse");

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
                Toast.makeText(CourseDetailActivity.this, "You're trying to share!!!", Toast.LENGTH_SHORT).show();
            }
        });

        Button addAssessment = findViewById(R.id.add_assessment_button);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddAssessment();
            }
        });

        allAssessments = myHelper.getAllAssessments(course.getId());

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.all_course_assessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter = new AssessmentViewAdapter(this, allAssessments);
        assessmentAdapter.setClickListener(this);
        recyclerView.setAdapter(assessmentAdapter);
    } // end onCreate

    @Override
    public void onAssessmentClick(View view, int position) {
        Intent intent = new Intent(this, AddAssessmentActivity.class);
        int listposition = allAssessments.indexOf(assessmentAdapter.getItem(position));
        intent.putExtra("FullAssessment", assessmentAdapter.getItem(position));
        intent.putExtra("listposition", listposition);
        this.startActivity(intent);
    }

    private void launchAddAssessment() {
        Intent intent = new Intent(this, AddAssessmentActivity.class);
        intent.putExtra("courseid", course.getId());
        startActivity(intent);
    } // end launchAddAssessment
} // end CourseDetailActivity
