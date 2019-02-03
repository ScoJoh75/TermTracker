package com.example.myrecylverviewapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class TermDetailActivity extends AppCompatActivity {

    DBHelper myHelper;
    static CourseViewAdapter courseAdapter;
    static List<Course> allCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        myHelper = new DBHelper(TermDetailActivity.this);
        myHelper.getWritableDatabase();
        myHelper.createDataBase();

        Button btnLaunchAddTerm = findViewById(R.id.courseAddButton);
        btnLaunchAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddCourse();}
        });

        allCourses = myHelper.getAllCourses();
        myHelper.close();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTermCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseViewAdapter(this, allCourses);
        courseAdapter.setClickListener(this);
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You Clicked " + courseAdapter.getItem(position).getCourseTitle() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void launchAddCourse() {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
}
