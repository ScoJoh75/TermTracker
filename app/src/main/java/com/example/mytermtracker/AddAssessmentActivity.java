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

import static com.example.mytermtracker.CourseDetailActivity.assessmentAdapter;
import static com.example.mytermtracker.MainActivity.allAssessments;

public class AddAssessmentActivity extends AppCompatActivity {

    private EditText mAssessmentName;
    private Spinner mAssessmentType;
    private TextView mDisplayGoalDate;
    private DatePickerDialog.OnDateSetListener mGoalDateSetListener;
    private Switch mAssessmentAlert;

    int goalYear;
    int goalMonth;
    int goalDay;

    Assessment assessment;
    Long assessmentId;
    Long courseId;
    int listposition;

    boolean modifying = false;
    PendingIntent oldSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        TextView mActivityTitle = findViewById(R.id.assessment_activity_title);
        mAssessmentName = findViewById(R.id.assessment_name);
        mAssessmentType = findViewById(R.id.assessment_type);
        mDisplayGoalDate = findViewById(R.id.assessment_goal_date);
        mAssessmentAlert = findViewById(R.id.assessment_notification_switch);

        final Button insertButton = findViewById(R.id.assessment_add_button);
        final Button cancelButton = findViewById(R.id.assessment_cancel_button);

        Intent intent = getIntent();
        courseId = intent.getLongExtra("courseid", -1);

        if(courseId == -1) {
            modifying = true;
            assessment = intent.getParcelableExtra("FullAssessment");
            assessmentId = assessment.getId();
            courseId = assessment.getCourseId();
            listposition = intent.getIntExtra("listposition", -1);

            mActivityTitle.setText("Update Assessment Information");
            insertButton.setText("Update Assessment");
            cancelButton.setText("Cancel Update");
            mAssessmentName.setText(assessment.getAssessmentName());

            Calendar goalDate = Calendar.getInstance();
            goalDate.setTime(assessment.getGoalDate());

            String goalString = (goalDate.get(Calendar.MONTH) + 1) + "/" + goalDate.get(Calendar.DAY_OF_MONTH) + "/" + goalDate.get(Calendar.YEAR);
            mDisplayGoalDate.setText(goalString);
            goalYear = goalDate.get(Calendar.YEAR);
            goalMonth = goalDate.get(Calendar.MONTH);
            goalDay = goalDate.get(Calendar.DAY_OF_MONTH);

            mAssessmentType.setSelection(((ArrayAdapter<String>)mAssessmentType.getAdapter()).getPosition(assessment.getAssessmentType()));

            Intent oldIntent = new Intent(AddAssessmentActivity.this, MyReceiver.class);
            oldIntent.putExtra("Channel", "Assessment");
            oldIntent.putExtra("ID", assessment.getId().toString());
            oldIntent.putExtra("Name", assessment.getAssessmentName());
            oldSender = PendingIntent.getBroadcast(AddAssessmentActivity.this, 0, oldIntent, 0);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String queryResult = sharedPreferences.getString(assessmentId.toString(), "");
            if(queryResult.length() > 0) {
                mAssessmentAlert.setChecked(true);
            } // end if
        } // end modifying if

        mDisplayGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddAssessmentActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mGoalDateSetListener,
                        year, month, day);
                dialog.show();
            } // end onClick
        }); // end setOnClickListener

        mGoalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String goalDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                mDisplayGoalDate.setText(goalDate);
                goalYear = year;
                goalMonth = month;
                goalDay = dayOfMonth;
            } // end onDateSet
        }; // end mGoalDateSetListener

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mAssessmentName.getText())) {
                    Toast.makeText(AddAssessmentActivity.this, "Yo Yo! This can't be empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper myHelper = new DBHelper(AddAssessmentActivity.this);
                    myHelper.getWritableDatabase();

                    String assessmentName = mAssessmentName.getText().toString();
                    Date goalDate = new Date(goalYear - 1900, goalMonth, goalDay);
                    String assessmentType = mAssessmentType.getSelectedItem().toString();
                    boolean assessmentAlert = mAssessmentAlert.isChecked();

                    if(modifying) {
                        assessment.setId(assessmentId);
                        assessment.setAssessmentName(assessmentName);
                        assessment.setAssessmentType(assessmentType);
                        assessment.setGoalDate(goalDate);
                        allAssessments.set(listposition, assessment);
                        myHelper.updateAssessment(assessmentId, assessmentName, assessmentType, goalDate, courseId);
                        assessmentAdapter.notifyDataSetChanged();
                    } else {
                        assessment = new Assessment(assessmentName, assessmentType, goalDate, courseId);
                        assessment.setId(myHelper.addAssessment(assessment.getAssessmentName(), assessment.getAssessmentType(), assessment.getGoalDate(), assessment.getCourseId()));
                        allAssessments.add(assessment);
                        assessmentAdapter.notifyDataSetChanged();
                    } // end if

                    Intent intent = new Intent(AddAssessmentActivity.this, MyReceiver.class);
                    intent.putExtra("Channel", "Assessment");
                    intent.putExtra("ID", assessment.getId().toString());
                    intent.putExtra("Name", assessment.getAssessmentName());
                    PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentActivity.this, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    alarmManager.cancel(oldSender);

                    if(assessmentAlert) {
                        Toast.makeText(AddAssessmentActivity.this, "You're Saving an Alert!", Toast.LENGTH_LONG).show();
                        // TODO set Trigger time based on end dates and goal dates.
                        sharedPreferences.edit().putString(assessment.getId().toString(), "true").apply();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, sender);
                    } else {
                        Toast.makeText(AddAssessmentActivity.this, "You're Disabling an Alert", Toast.LENGTH_SHORT).show();
                        alarmManager.cancel(sender);
                        sharedPreferences.edit().remove(assessment.getId().toString()).apply();
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
} // end AddAssessmentActivity
