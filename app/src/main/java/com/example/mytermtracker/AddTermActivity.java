package com.example.mytermtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

import static com.example.mytermtracker.MainActivity.allTerms;
import static com.example.mytermtracker.MainActivity.termAdapter;

public class AddTermActivity extends AppCompatActivity {

    private EditText mEditTermView;
    private TextView mDisplayStartDate;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private TextView mDisplayEndDate;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;

    int startYear;
    int startMonth;
    int startDay;
    int endYear;
    int endMonth;
    int endDay;

    Term term;
    Long id;
    int listposition;

    boolean modifying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        TextView mActivityTitle = findViewById(R.id.termInfoTitle);
        mEditTermView = findViewById(R.id.edit_term_name);
        mDisplayStartDate = findViewById(R.id.edit_start_date);
        mDisplayEndDate = findViewById(R.id.edit_end_date);
        Button cancelButton = findViewById(R.id.cancelInsertButton);

        // This section adds a button programmatically rather than via XML
        RelativeLayout relativeLayout = findViewById(R.id.new_term_layout);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Button insertButton = new Button(this);
        insertButton.setText("Add New Term");
        int buttonID = 1964;
        insertButton.setId(buttonID);
        relativeLayout.addView(insertButton, buttonParams);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) insertButton.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_TOP, cancelButton.getId());
        params.addRule(RelativeLayout.RIGHT_OF, cancelButton.getId());

        buttonParams.setMargins(256,0,16,0);
        insertButton.setLayoutParams(buttonParams);

        Intent intent = getIntent();

        if(intent.getExtras() != null) {
            modifying = true;
            term = intent.getParcelableExtra("FullTerm");
            id = term.getId();
            listposition = intent.getIntExtra("listposition", -1);

            mActivityTitle.setText(getString(R.string.add_modify_title));
            insertButton.setText(getString(R.string.update_term));
            cancelButton.setText(getString(R.string.cancel_update));
            mEditTermView.setText(term.getTermName());

            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(term.getStartDate());
            endDate.setTime(term.getEndDate());

            String startString = (startDate.get(Calendar.MONTH) + 1) + "/" + startDate.get(Calendar.DAY_OF_MONTH) + "/" + startDate.get(Calendar.YEAR);
            String endString = (endDate.get(Calendar.MONTH) + 1) + "/" + endDate.get(Calendar.DAY_OF_MONTH) + "/" + endDate.get(Calendar.YEAR);
            mDisplayStartDate.setText(startString);
            mDisplayEndDate.setText(endString);

            startYear = startDate.get(Calendar.YEAR);
            startMonth = startDate.get(Calendar.MONTH);
            startDay = startDate.get(Calendar.DAY_OF_MONTH);
            endYear = endDate.get(Calendar.YEAR);
            endMonth = endDate.get(Calendar.MONTH);
            endDay = endDate.get(Calendar.DAY_OF_MONTH);
        }

        mDisplayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                if(modifying){
                    year = startYear;
                    month = startMonth;
                    day = startDay;
                } // end if

                DatePickerDialog dialog = new DatePickerDialog(AddTermActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mStartDateSetListener,
                        year, month, day);
                dialog.show();
            } // end onClick
        }); // end setOnClickListener

        mDisplayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                if(modifying){
                    year = endYear;
                    month = endMonth;
                    day = endDay;
                } // end if

                DatePickerDialog dialog = new DatePickerDialog(AddTermActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mEndDateSetListener,
                        year, month, day);
                dialog.show();
            } // end onClick
        }); // end setOnClickListener

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String startDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                mDisplayStartDate.setText(startDate);
                startYear = year;
                startMonth = month;
                startDay = dayOfMonth;
            } // end onDateSet
        }; // end mStartDateSetListener

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                mDisplayEndDate.setText(endDate);
                endYear = year;
                endMonth = month;
                endDay = dayOfMonth;
            } // end onDateSet
        }; // end mEndDateSetListener

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mEditTermView.getText())) {
                    Toast.makeText(AddTermActivity.this, "Yo Yo! This can't be empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper myHelper = new DBHelper(AddTermActivity.this);
                    myHelper.getWritableDatabase();

                    String term_name = mEditTermView.getText().toString();
                    Date start_date = new Date(startYear - 1900, startMonth, startDay);
                    Date end_date = new Date(endYear - 1900, endMonth, endDay);

                    if(modifying) {
                        term.setId(id);
                        term.setTermName(term_name);
                        term.setStartDate(start_date);
                        term.setEndDate(end_date);
                        allTerms.set(listposition, term);
                        myHelper.updateTerm(id, term_name, start_date, end_date);
                        termAdapter.notifyDataSetChanged();
                    } else {
                        Term term = new Term(term_name, start_date, end_date);
                        term.setId(myHelper.addTerm(term.getTermName(), term.getStartDate(), term.getEndDate()));
                        allTerms.add(term);
                        termAdapter.notifyDataSetChanged();
                    } // end if

                    myHelper.close();
                    finish();
                } // end if
            } // end onClick
        }); // end setOnClickListener

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
