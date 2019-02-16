package com.example.mytermtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TermViewAdapter.TermClickListener {

    DBHelper myHelper;
    static TermViewAdapter termAdapter;
    static List<Term> allTerms;
    static List<Course> allCourses;
    static List<Assessment> allAssessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new DBHelper(MainActivity.this);
        myHelper.getWritableDatabase();
        myHelper.createDataBase();

        Button btnLaunchAddTerm = findViewById(R.id.termAddButton);
        btnLaunchAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddTerm();}
        });

        allTerms = myHelper.getAllTerms();
        myHelper.close();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTerms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter = new TermViewAdapter(this, allTerms);
        termAdapter.setClickListener(this);
        recyclerView.setAdapter(termAdapter);
    }
    

    @Override
    public void onTermClick(View view, int position) {
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra("TermId", termAdapter.getItem(position).getId());
        intent.putExtra("TermName", termAdapter.getItem(position).getTermName());
        this.startActivity(intent);
    }

    private void launchAddTerm() {
        Intent intent = new Intent(this, AddTermActivity.class);
        startActivity(intent);
    }
}
