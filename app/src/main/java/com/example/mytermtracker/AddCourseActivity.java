package com.example.mytermtracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
    private Switch mCourseAlert;

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
    PendingIntent oldStartSender;
    PendingIntent oldEndSender;

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
        mCourseAlert = findViewById(R.id.course_notification_switch);

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

            startYear = startDate.get(Calendar.YEAR);
            startMonth = startDate.get(Calendar.MONTH);
            startDay = startDate.get(Calendar.DAY_OF_MONTH);
            endYear = endDate.get(Calendar.YEAR);
            endMonth = endDate.get(Calendar.MONTH);
            endDay = endDate.get(Calendar.DAY_OF_MONTH);

            mCourseStatus.setSelection(((ArrayAdapter<String>)mCourseStatus.getAdapter()).getPosition(course.getStatus()));
            mMentorName.setText(course.getMentorName());
            mMentorEmail.setText(course.getMentorEmail());
            mMentorPhone.setText(course.getMentorPhone());
            mCourseNotes.setText(course.getNotes());

            Intent oldStartIntent = new Intent(AddCourseActivity.this, MyReceiver.class);
            oldStartIntent.putExtra("Channel", "Course Begins");
            oldStartIntent.putExtra("ID", course.getId().toString());
            oldStartIntent.putExtra("Name", course.getCourseTitle());
            oldStartIntent.putExtra("Message", " begins today!");
            oldStartSender = PendingIntent.getBroadcast(AddCourseActivity.this, 0, oldStartIntent, 0);

            Intent oldEndIntent = new Intent(AddCourseActivity.this, MyReceiver.class);
            oldEndIntent.putExtra("Channel", "Course Ends");
            oldEndIntent.putExtra("ID", Long.toString(course.getId() + 10000));
            oldEndIntent.putExtra("Name", course.getCourseTitle());
            oldEndIntent.putExtra("Message", " ends today!");
            oldEndSender = PendingIntent.getBroadcast(AddCourseActivity.this, 0, oldEndIntent, 0);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String queryResult = sharedPreferences.getString("Course " + course.getId().toString(), "");
            if(queryResult.length() > 0) {
                mCourseAlert.setChecked(true);
            } // end if
        } // end modifying if

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

                if(modifying){
                    year = endYear;
                    month = endMonth;
                    day = endDay;
                } // end if

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
                if(TextUtils.isEmpty(mEditCourseTitle.getText())) {
                    Toast.makeText(AddCourseActivity.this, "Yo Yo! This can't be empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper myHelper = new DBHelper(AddCourseActivity.this);
                    myHelper.getWritableDatabase();

                    String courseTitle = mEditCourseTitle.getText().toString();
                    Date startDate = new Date(startYear - 1900, startMonth, startDay);
                    Date endDate = new Date(endYear - 1900, endMonth, endDay);
                    String courseStatus = mCourseStatus.getSelectedItem().toString();
                    String mentorName = mMentorName.getText().toString();
                    String mentorEmail = mMentorEmail.getText().toString();
                    String mentorPhone = mMentorPhone.getText().toString();
                    String courseNotes = mCourseNotes.getText().toString();
                    boolean courseAlert = mCourseAlert.isChecked();

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

                    Intent startIntent = new Intent(AddCourseActivity.this, MyReceiver.class);
                    startIntent.putExtra("Channel", "Course Begins");
                    startIntent.putExtra("ID", course.getId().toString());
                    startIntent.putExtra("Name", course.getCourseTitle());
                    startIntent.putExtra("Message", " begins today!");
                    PendingIntent startSender = PendingIntent.getBroadcast(AddCourseActivity.this, 0, startIntent, 0);

                    Intent endIntent = new Intent(AddCourseActivity.this, MyReceiver.class);
                    endIntent.putExtra("Channel", "Course Ends");
                    endIntent.putExtra("ID", Long.toString(course.getId() + 10000));
                    endIntent.putExtra("Name", course.getCourseTitle());
                    endIntent.putExtra("Message", " ends today!");
                    PendingIntent endSender = PendingIntent.getBroadcast(AddCourseActivity.this, 0, endIntent, 0);

                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String queryResult = sharedPreferences.getString("Course " + course.getId().toString(), "");

                    if(queryResult.length() > 0) {
                        alarmManager.cancel(oldStartSender);
                        alarmManager.cancel(oldEndSender);
                    } // end if

                    if(courseAlert) {
                        Calendar alarmStartDate = Calendar.getInstance();
                        alarmStartDate.setTime(startDate);
                        long alarmStartMillis = alarmStartDate.getTimeInMillis();

                        Calendar alarmEndDate = Calendar.getInstance();
                        alarmEndDate.setTime(endDate);
                        long alarmEndMillis = alarmEndDate.getTimeInMillis();

                        sharedPreferences.edit().putString("Course " + course.getId().toString(), "true").apply();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartMillis, startSender);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmEndMillis, endSender);
                    } else {
                        alarmManager.cancel(startSender);
                        alarmManager.cancel(endSender);
                        sharedPreferences.edit().remove("Course " + course.getId().toString()).apply();
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
