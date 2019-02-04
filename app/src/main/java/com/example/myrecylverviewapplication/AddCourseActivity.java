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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

import static com.example.myrecylverviewapplication.TermDetailActivity.allCourses;
import static com.example.myrecylverviewapplication.TermDetailActivity.courseAdapter;

public class AddCourseActivity extends AppCompatActivity {

    private TextView mActivityTitle;
    private EditText mEditCourseTitle;
    private TextView mDisplayStartDate;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private TextView mDisplayEndDate;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;
    private Spinner mCourseStatus;
    private EditText mMenterName;
    private EditText mMentorEmail;
    private EditText mMentorPhone;
    private EditText mCourseNotes;

    int startYear;
    int startMonth;
    int startDay;
    int endYear;
    int endMonth;
    int endDay;

    Course course;
    Long courseid;
    Long termid;
    int listposition;

    boolean modifying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mActivityTitle = findViewById(R.id.activity_title);
        mEditCourseTitle = findViewById(R.id.course_title);
        mDisplayStartDate = findViewById(R.id.course_start_date);
        mDisplayEndDate = findViewById(R.id.course_end_date);
        mCourseStatus = findViewById(R.id.course_status);
        mMenterName = findViewById(R.id.course_mentor_name);
        mMentorEmail = findViewById(R.id.course_mentor_email);
        mMentorPhone = findViewById(R.id.course_mentor_phone);
        mCourseNotes = findViewById(R.id.course_notes);

        final Button insertButton = findViewById(R.id.course_add_button);
        final Button cancelButton = findViewById(R.id.course_cancel_button);

        Intent intent = getIntent();
        termid = intent.getLongExtra("termid", -1);
        //TODO Add intent gathering for Modify

        mDisplayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddCourseActivity.this,
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

                DatePickerDialog dialog = new DatePickerDialog(AddCourseActivity.this,
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

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mEditCourseTitle.getText())) {
                    Toast.makeText(AddCourseActivity.this, "Yo Yo! This can't be empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper myHelper = new DBHelper(AddCourseActivity.this);
                    myHelper.getWritableDatabase();

                    String course_title = mEditCourseTitle.getText().toString();
                    Date start_date = new Date(startYear - 1900, startMonth - 1, startDay);
                    Date end_date = new Date(endYear - 1900, endMonth - 1, endDay);
                    String course_status = mCourseStatus.getSelectedItem().toString();
                    String mentor_name = mMenterName.getText().toString();
                    String mentor_email = mMentorEmail.getText().toString();
                    String mentor_phone = mMentorPhone.getText().toString();
                    String course_notes = mCourseNotes.getText().toString();

                    if(modifying) {
                        course.setId(courseid);
                        course.setCourseTitle(course_title);
                        course.setStartDate(start_date);
                        course.setEndDate(end_date);
                        allCourses.set(listposition, course);
                        //myHelper.updateCourse(courseid, course_title, start_date, end_date, course_status, mentor_name, mentor_phone, mentor_email, course_notes);
                        courseAdapter.notifyDataSetChanged();
                        Toast.makeText(AddCourseActivity.this, "Modifying!", Toast.LENGTH_SHORT).show();
                    } else {
                        Course course = new Course(course_title, start_date, end_date, course_status, mentor_name, mentor_phone, mentor_email, course_notes, termid);
                        course.setId(myHelper.addCourse(course.getCourseTitle(), course.getStartDate(), course.getEndDate(), course.getStatus(), course.getMentorName(), course.getMentorPhone(), course.getMentorEmail(), course.getNotes(), course.getTermId()));
                        allCourses.add(course);
                        courseAdapter.notifyDataSetChanged();
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
