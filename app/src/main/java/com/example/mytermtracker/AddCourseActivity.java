package com.example.mytermtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

import static com.example.mytermtracker.MainActivity.allCourses;
import static com.example.mytermtracker.TermDetailActivity.courseAdapter;

public class AddCourseActivity extends AppCompatActivity {

    private TextView mActivityTitle;
    private EditText mEditCourseTitle;
    private TextView mDisplayStartDate;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private TextView mDisplayEndDate;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;
    private Spinner mCourseStatus;
    private EditText mMentorName;
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
        mMentorName = findViewById(R.id.course_mentor_name);
        mMentorEmail = findViewById(R.id.course_mentor_email);
        mMentorPhone = findViewById(R.id.course_mentor_phone);
        mCourseNotes = findViewById(R.id.course_notes);

        final Button insertButton = findViewById(R.id.course_add_button);
        final Button cancelButton = findViewById(R.id.course_cancel_button);

        Intent intent = getIntent();
        termid = intent.getLongExtra("termid", -1);

        if(termid == -1) {
            modifying = true;
            course = intent.getParcelableExtra("FullCourse");
            courseid = course.getId();
            termid = course.getTermId();
            listposition = intent.getIntExtra("listposition", -1);

            mActivityTitle.setText("Update Course Information");
            insertButton.setText("Update Course");
            cancelButton.setText("Cancel Update");
            mEditCourseTitle.setText(course.getCourseTitle());

            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(course.getStartDate());
            endDate.setTime(course.getEndDate());

            String startString = (startDate.get(Calendar.MONTH) + 1) + "/" + startDate.get(Calendar.DAY_OF_MONTH) + "/" + startDate.get(Calendar.YEAR);
            String endString = (endDate.get(Calendar.MONTH) + 1) + "/" + endDate.get(Calendar.DAY_OF_MONTH) + "/" + endDate.get(Calendar.YEAR);
            mDisplayStartDate.setText(startString);
            mDisplayEndDate.setText(endString);
            mCourseStatus.setSelection(((ArrayAdapter<String>)mCourseStatus.getAdapter()).getPosition(course.getStatus()));
            mMentorName.setText(course.getMentorName());
            mMentorEmail.setText(course.getMentorEmail());
            mMentorPhone.setText(course.getMentorPhone());
            mCourseNotes.setText(course.getNotes());
        } // end modifying if

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

                    String courseTitle = mEditCourseTitle.getText().toString();
                    Date startDate = new Date(startYear - 1900, startMonth - 1, startDay);
                    Date endDate = new Date(endYear - 1900, endMonth - 1, endDay);
                    String courseStatus = mCourseStatus.getSelectedItem().toString();
                    String mentorName = mMentorName.getText().toString();
                    String mentorEmail = mMentorEmail.getText().toString();
                    String mentorPhone = mMentorPhone.getText().toString();
                    String courseNotes = mCourseNotes.getText().toString();

                    if(modifying) {
                        course.setId(courseid);
                        course.setCourseTitle(courseTitle);
                        course.setStartDate(startDate);
                        course.setEndDate(endDate);
                        course.setStatus(courseStatus);
                        course.setMentorName(mentorName);
                        course.setMentorEmail(mentorEmail);
                        course.setMentorPhone(mentorPhone);
                        course.setNotes(courseNotes);
                        allCourses.set(listposition, course);
                        myHelper.updateCourse(courseid, courseTitle, startDate, endDate, courseStatus, mentorName, mentorPhone, mentorEmail, courseNotes, termid);
                        courseAdapter.notifyDataSetChanged();
                    } else {
                        Course course = new Course(courseTitle, startDate, endDate, courseStatus, mentorName, mentorPhone, mentorEmail, courseNotes, termid);
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
            } // end onClick
        }); // end setOnClickListener
    } // end onCreate
} // end AddCourseActivity
