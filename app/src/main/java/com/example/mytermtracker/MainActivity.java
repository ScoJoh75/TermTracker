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
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TermViewAdapter.TermClickListener {

    DBHelper myHelper;
    static TermViewAdapter termAdapter;
    static List<Term> allTerms;
    static List<Course> allCourses;
    static List<Assessment> allAssessments;
    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        myHelper = new DBHelper(MainActivity.this);
        myHelper.getWritableDatabase();
        myHelper.createDataBase();

        Button btnLaunchAddTerm = findViewById(R.id.termAddButton);
        btnLaunchAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddTerm();}
        });
        btnLaunchAddTerm.setVisibility(View.INVISIBLE);

        allTerms = myHelper.getAllTerms();
        myHelper.close();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTerms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter = new TermViewAdapter(this, allTerms);
        termAdapter.setClickListener(this);
        recyclerView.setAdapter(termAdapter);

        if(termAdapter.getItemCount() > 0) {
            mainText = findViewById(R.id.main_text_information);
            mainText.setText(getString(R.string.click_term_text));
        } // end if
    } // end onCreate

    @Override
    public void onResume() {
        super.onResume();
        if(termAdapter.getItemCount() > 0) {
            mainText = findViewById(R.id.main_text_information);
            mainText.setText(getString(R.string.click_term_text));
        } // end if
    } // end onResume

    @Override
    public void onTermClick(View view, int position) {
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra("TermId", termAdapter.getItem(position).getId());
        intent.putExtra("TermName", termAdapter.getItem(position).getTermName());
        this.startActivity(intent);
    } // end onTermClick

    private void launchAddTerm() {
        Intent intent = new Intent(this, AddTermActivity.class);
        startActivity(intent);
    } // end launchAddTerm

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    } // end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action:
            {launchAddTerm();}
        }
        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected
} // end mainActivity
