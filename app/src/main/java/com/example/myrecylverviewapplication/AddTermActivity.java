package com.example.myrecylverviewapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

import static com.example.myrecylverviewapplication.MainActivity.allTerms;
import static com.example.myrecylverviewapplication.MainActivity.termAdapter;

public class AddTermActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.termlistsql.REPLY";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        mEditTermView = findViewById(R.id.edit_term_name);
        mDisplayStartDate = findViewById(R.id.edit_start_date);
        mDisplayEndDate = findViewById(R.id.edit_end_date);

        final Button button = findViewById(R.id.termInsertButton);

        mDisplayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

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
                String startDate = (month += 1) + "/" + dayOfMonth + "/" + year;
                mDisplayStartDate.setText(startDate);
                startYear = year;
                startMonth = month;
                startDay = dayOfMonth;
            } // end onDateSet
        }; // end mStartDateSetListener

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = (month += 1) + "/" + dayOfMonth + "/" + year;
                mDisplayEndDate.setText(endDate);
                endYear = year;
                endMonth = month;
                endDay = dayOfMonth;
            } // end onDateSet
        }; // end mEndDateSetListener

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(mEditTermView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    DBHelper myHelper = new DBHelper(AddTermActivity.this);
                    myHelper.getWritableDatabase();

                    String term_name = mEditTermView.getText().toString();

                    Date start_date = new Date(startYear - 1900, startMonth - 1, startDay);
                    Date end_date = new Date(endYear - 1900, endMonth - 1, endDay);

                    Term term = new Term(term_name, start_date, end_date);
                    myHelper.addTerm(term.getTermName(), term.getStartDate(), term.getEndDate());
                    allTerms.add(term);
                    termAdapter.notifyDataSetChanged();
//                    replyIntent.putExtra(EXTRA_REPLY, term_name);
//                    replyIntent.putExtra("start_date", start_date.getTime());
//                    replyIntent.putExtra("end_date", end_date.getTime());
//                    setResult(RESULT_OK, replyIntent);
                } // end if
                finish();
            } // end onClick
        }); // end setOnClickListener
    } // end onCreate
}
