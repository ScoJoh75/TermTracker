package com.example.myrecylverviewapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TermViewAdapter.ItemClickListener {

    DBHelper myHelper;
    TermViewAdapter adapter;
    List<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new DBHelper(MainActivity.this);
        myHelper.getWritableDatabase();
        myHelper.createDataBase();

        // data to populate the RecyclerView with
//        Term term = new Term("Term 1", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
//        myHelper.addTerm(term.getTermName(), term.getStartDate(), term.getEndDate());
//        term = new Term("Term 2", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
//        myHelper.addTerm(term.getTermName(), term.getStartDate(), term.getEndDate());
//        term = new Term("Term 3", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
//        myHelper.addTerm(term.getTermName(), term.getStartDate(), term.getEndDate());

        allTerms = myHelper.getAllTerms();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTerms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TermViewAdapter(this, allTerms);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You Clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
