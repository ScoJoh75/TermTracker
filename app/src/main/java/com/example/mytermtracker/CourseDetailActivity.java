package com.example.mytermtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();

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
                if (allAssessments.size() == 0) {
                    myHelper.deleteCourse(course.getId());
                    myHelper.close();
                    finish();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "This Course still has assessments. Please delete all assessments before deleting a Course!", Toast.LENGTH_LONG).show();
                } // end if
            } // end onClick
        });

        ImageButton shareNotes = findViewById(R.id.share_notes);
        shareNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, course.getNotes());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        Button addAssessment = findViewById(R.id.add_assessment_button);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddAssessment();
            }
        });
        addAssessment.setVisibility(View.INVISIBLE);

        allAssessments = myHelper.getAllAssessments(course.getId());

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.all_course_assessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter = new AssessmentViewAdapter(this, allAssessments);
        assessmentAdapter.setClickListener(this);
        recyclerView.setAdapter(assessmentAdapter);

        if(assessmentAdapter.getItemCount() > 0) {
            mainText = findViewById(R.id.assessment_text_information);
            mainText.setText(getString(R.string.click_assessment_text));
        } // end if
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    } // end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action:
            {launchAddAssessment();}
        } // end switch
        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem title = menu.findItem(R.id.action);
        title.setTitle("ADD ASSESSMENT");
        return true;
    } // end onPrepareOptionsMenu
} // end CourseDetailActivity
