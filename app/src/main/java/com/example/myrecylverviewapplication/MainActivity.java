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

public class MainActivity extends AppCompatActivity implements TermViewAdapter.ItemClickListener {

    DBHelper myHelper;
    static TermViewAdapter termAdapter;
    static List<Term> allTerms;

    private Button btnLaunchAddTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new DBHelper(MainActivity.this);
        myHelper.getWritableDatabase();
        myHelper.createDataBase();

        btnLaunchAddTerm = findViewById(R.id.termAddButton);

        btnLaunchAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {launchAddTerm();}
        });

        allTerms = myHelper.getAllTerms();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.allTerms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter = new TermViewAdapter(this, allTerms);
        termAdapter.setClickListener(this);
        recyclerView.setAdapter(termAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You Clicked " + termAdapter.getItem(position).getTermName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void launchAddTerm() {
        Intent intent = new Intent(this, AddTermActivity.class);
        startActivity(intent);
    }
}
