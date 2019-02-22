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

import static com.example.mytermtracker.MainActivity.allCourses;

public class TermDetailActivity extends AppCompatActivity implements CourseViewAdapter.CourseClickListener {

    DBHelper myHelper;
    static CourseViewAdapter courseAdapter;

    long termId;
    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();

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
        }); // end setOnClickListener

        Button btnLaunchAddCourse = findViewById(R.id.courseAddButton);
        btnLaunchAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddCourse();}
        });
        btnLaunchAddCourse.setVisibility(View.INVISIBLE);

        allCourses = myHelper.getAllCourses(termId);
        //myHelper.close();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTermCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseViewAdapter(this, allCourses);
        courseAdapter.setClickListener(this);
        recyclerView.setAdapter(courseAdapter);

        if(courseAdapter.getItemCount() > 0) {
            mainText = findViewById(R.id.course_text_information);
            mainText.setText(getString(R.string.click_course_text));
        } // end if
    } // end onCreate

    @Override
    public void onResume() {
        super.onResume();
        if(courseAdapter.getItemCount() > 0) {
            mainText = findViewById(R.id.course_text_information);
            mainText.setText(getString(R.string.click_course_text));
        } // end if
    } // end onResume

    @Override
    public void onCourseClick(View view, int position) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("FullCourse", courseAdapter.getItem(position));
        startActivity(intent);
    } // end onCourseClick

    private void launchAddCourse() {
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("termid", termId);
        startActivity(intent);
    } // end launchAddCourse

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    } // end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action:
            {launchAddCourse();}
        } // end switch
        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem title = menu.findItem(R.id.action);
        title.setTitle("ADD COURSE");
        return true;
    } // end onPrepareOptionsMenu
} // end TermDetailActivity
