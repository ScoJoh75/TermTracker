package com.example.myrecylverviewapplication;

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

import static com.example.myrecylverviewapplication.MainActivity.allCourses;

public class TermDetailActivity extends AppCompatActivity implements CourseViewAdapter.CourseClickListener {

    DBHelper myHelper;
    static CourseViewAdapter courseAdapter;

    long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        myHelper = new DBHelper(TermDetailActivity.this);
        myHelper.getWritableDatabase();

        Intent intent = getIntent();
        termId = intent.getLongExtra("TermId", -1);
        String termName = intent.getStringExtra("TermName");

        TextView tvTermName = findViewById(R.id.termName);
        tvTermName.setText(termName);

        ImageButton deleteTerm = findViewById(R.id.deleteTermButton);
        deleteTerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (allCourses.size() == 0) {
                    myHelper.deleteTerm(termId);
                    myHelper.close();
                    finish();
                } else {
                    Toast.makeText(TermDetailActivity.this, "This Term still has courses. Please delete courses before deleting a Term!", Toast.LENGTH_LONG).show();
                } // end if
            } // end onClick
        });

        Button btnLaunchAddTerm = findViewById(R.id.courseAddButton);
        btnLaunchAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddCourse();}
        });

        allCourses = myHelper.getAllCourses(termId);
        //myHelper.close();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTermCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseViewAdapter(this, allCourses);
        courseAdapter.setClickListener(this);
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    public void onCourseClick(View view, int position) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("FullCourse", courseAdapter.getItem(position));
        this.startActivity(intent);
    }

    private void launchAddCourse() {
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("termid", termId);
        startActivity(intent);
    }
}
