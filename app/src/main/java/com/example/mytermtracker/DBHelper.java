package com.example.mytermtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.example.mytermtracker.CourseDetailActivity.assessmentAdapter;
import static com.example.mytermtracker.MainActivity.allAssessments;
import static com.example.mytermtracker.MainActivity.allTerms;
import static com.example.mytermtracker.MainActivity.allCourses;
import static com.example.mytermtracker.MainActivity.termAdapter;
import static com.example.mytermtracker.TermDetailActivity.courseAdapter;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TERMTRACKER.db";
    private static final int DATABASE_VERSION = 1;

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } // end constructor

    @Override
    public void onCreate(SQLiteDatabase db) {

    } // end onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // end onUpgrade

    // Adds information to the SQL database using Content Values! Return values!
    long addTerm(String termName, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("termname", termName);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());

        return db.insert("Terms", null, values);
    } // end addTerm

    void updateTerm(Long id, String termName, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("termname", termName);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());

        db.update("Terms", values, "id = ?", new String[] { String.valueOf(id) });
    } // end updateTerm

    void deleteTerm(long termId) {
        int id = -1;
        for(Term term : allTerms) {
            if(term.getId() == termId) {
                id = allTerms.indexOf(term);
            } // end if
        } // end for
        allTerms.remove(id);
        termAdapter.notifyDataSetChanged();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {Long.toString(termId)};
        db.delete("Terms", "id = ?", args);

        // Example (MainActivity):
        // String[] whereArgs = {"12"};
        // int rows = myHelper.removeRecord("tableName", "id = ?", whereArgs);
    } // end removeRecord

    // Adds information to the SQL database using Content Values! Return values!
    long addCourse(String courseTitle, Date startDate, Date endDate, String status, String mentorName, String mentorPhone, String mentorEmail, String courseNotes, Long termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("coursetitle", courseTitle);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());
        values.put("status", status);
        values.put("mentorname", mentorName);
        values.put("mentorphone", mentorPhone);
        values.put("mentoremail", mentorEmail);
        values.put("notes", courseNotes);
        values.put("termid", termId);

        return db.insert("Courses", null, values);
    } // end addCourse

    void updateCourse(Long courseid, String courseTitle, Date startDate, Date endDate, String courseStatus, String mentorName, String mentorPhone, String mentorEmail, String courseNotes, Long termid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("coursetitle", courseTitle);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());
        values.put("status", courseStatus);
        values.put("mentorname", mentorName);
        values.put("mentorphone", mentorPhone);
        values.put("mentoremail", mentorEmail);
        values.put("notes", courseNotes);
        values.put("termid", String.valueOf(termid));

        db.update("Courses", values, "id = ?", new String[] { String.valueOf(courseid) });
    } // end updateCourse

    void deleteCourse(long courseId) {
        int id = -1;
        for(Course course : allCourses) {
            if(course.getId() == courseId) {
                id = allCourses.indexOf(course);
            } // end if
        } // end for
        allCourses.remove(id);
        courseAdapter.notifyDataSetChanged();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {Long.toString(courseId)};
        db.delete("Courses", "id = ?", args);
    } // end removeRecord

    // Adds information to the SQL database using Content Values! Return values!
    long addAssessment(String assessmentName, String assessmentType, Date goalDate, Long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("assessmentname", assessmentName);
        values.put("assessmenttype", assessmentType);
        values.put("goaldate", goalDate.toString());
        values.put("courseid", courseId);

        return db.insert("Assessments", null, values);
    } // end addTerm


    void updateAssessment(Long id, String assessmentName, String assessmentType, Date goalDate, Long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("assessmentname", assessmentName);
        values.put("assessmenttype", assessmentType);
        values.put("goaldate", goalDate.toString());
        values.put("courseid", courseId);

        db.update("Assessments", values, "id = ?", new String[] { String.valueOf(id) });
    } // end updateTerm

    void deleteAssessment(long assessmentId) {
        int id = -1;
        for(Assessment assessment : allAssessments) {
            if(assessment.getId() == assessmentId) {
                id = allAssessments.indexOf(assessment);
            } // end if
        } // end for
        allAssessments.remove(id);
        assessmentAdapter.notifyDataSetChanged();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {Long.toString(assessmentId)};
        db.delete("Assessments", "id = ?", args);
    } // end removeRecord

    List<Term> getAllTerms() {
        List<Term> allTerms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Terms;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while(cursor.moveToNext()){
                Long id = cursor.getLong(0);
                String termName = cursor.getString(1);
                Date startDate = Date.valueOf(cursor.getString(2));
                Date endDate = Date.valueOf(cursor.getString(3));
                Term term = new Term(id, termName, startDate, endDate);
                allTerms.add(term);
            } // end while
        } // end if
        assert cursor != null;
        cursor.close();
        db.close();
        return allTerms;
    } // end getAllTerms


    List<Course> getAllCourses(Long termid) {
        List<Course> allCourses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String term_id = Long.toString(termid);
        String sql = "SELECT * FROM Courses WHERE termid = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {term_id});
        if (cursor != null) {
            while(cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String courseTitle = cursor.getString(1);
                Date startDate = Date.valueOf(cursor.getString(2));
                Date endDate = Date.valueOf(cursor.getString(3));
                String status = cursor.getString(4);
                String mentorName = cursor.getString(5);
                String mentorPhone = cursor.getString(6);
                String mentorEmail = cursor.getString(7);
                String notes = cursor.getString(8);
                Long termId = cursor.getLong(9);
                Course course = new Course(id, courseTitle, startDate, endDate, status, mentorName, mentorPhone, mentorEmail, notes, termId);
                allCourses.add(course);
            } // end while
        } // end if
        assert cursor != null;
        cursor.close();
        db.close();
        return allCourses;
    } // end getAllCourses

    List<Assessment> getAllAssessments(Long courseid) {
        List<Assessment> allAssessments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String course_id = Long.toString(courseid);
        String sql = "SELECT * FROM Assessments WHERE courseid = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {course_id});
        if (cursor != null) {
            while(cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String assessmentName = cursor.getString(1);
                String assessmentType = cursor.getString(2);
                Date goalDate = Date.valueOf(cursor.getString(3));
                long courseId = cursor.getLong(4);
                Assessment assessment = new Assessment(id, assessmentName, assessmentType, goalDate, courseId);
                allAssessments.add(assessment);
            } // end while
        } // end if
        assert cursor != null;
        cursor.close();
        db.close();
        return allAssessments;
    } // end getAllAssessments

    /**
     * createDataBase is run on launch and checks to see if the database already exists. If it
     * does not it will create the new tables and setup the database for working with the app.
     */
    void createDataBase() {
        String termTable = "Terms";
        String courseTable = "Courses";
        String assessmentTable = "Assessments";
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + termTable + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "termname TEXT, " +
                "startdate DATE, " +
                "enddate DATE)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + courseTable + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "coursetitle TEXT, " +
                "startdate DATE, " +
                "enddate DATE, " +
                "status TEXT, " +
                "mentorname TEXT, " +
                "mentorphone TEXT, " +
                "mentoremail TEXT, " +
                "notes TEXT, " +
                "termid INTEGER, " +
                "FOREIGN KEY (termid) REFERENCES Terms(id))");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + assessmentTable + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assessmentname TEXT, " +
                "assessmenttype TEXT, " +
                "goaldate DATE, " +
                "courseid INTEGER, " +
                "FOREIGN KEY (courseid) REFERENCES Courses(id))");
    } // end createTable
} // end DBHelper

