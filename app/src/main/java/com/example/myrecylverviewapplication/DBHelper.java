package com.example.myrecylverviewapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TERMTRACKER.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } // end constructor

    @Override
    public void onCreate(SQLiteDatabase db) {

    } // end onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // end onUpgrade

    // Adds information to the SQL database using Content Values! Return values!
    public long addTerm(String termName, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("termname", termName);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());

        return db.insert("Terms", null, values);
    } // end addTerm

    public void updateTerm(Long id, String termName, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("termname", termName);
        values.put("startdate", startDate.toString());
        values.put("enddate", endDate.toString());

        db.update("Terms", values, "id = ?", new String[] { String.valueOf(id) });
    } // end updateTerm

    // Deletes information from the database using a SQL statement! No Return Value!!
    public void deleteRecord(String sqlStatement) {
        this.getWritableDatabase().execSQL(sqlStatement);
    } // end deleteRecord

    public int removeRecord(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName, whereClause, whereArgs);

        // Example (MainActivity):
        // String[] whereArgs = {"12"};
        // int rows = myHelper.removeRecord("tableName", "id = ?", whereArgs);
    } // end removeRecord

    public long getIdByTermName(String termName) {
        long id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, startdate, enddate FROM Terms WHERE termname = '" + termName + "'";
        Cursor cursor = db.rawQuery(sql ,null);
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            Date startDate = Date.valueOf(cursor.getString(1));
            Date endDate = Date.valueOf(cursor.getString(2));
            System.out.println("RETRIEVED DATA: " + id + " STARTDATE: " + startDate + " ENDDATE: " + endDate);
        } // end if
        db.close();
        return id;
    } // end getTermByName

    public Term getTermById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Terms WHERE id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getLong(0);
            String termName = cursor.getString(1);
            Date startDate = Date.valueOf(cursor.getString(2));
            Date endDate = Date.valueOf(cursor.getString(3));
            Term term = new Term(id, termName, startDate, endDate);
            db.close();
            return term;
        } // end if
        db.close();
        return null;
    } // end getTermById


    public List<Term> getAllTerms() {
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
        cursor.close();
        db.close();
        return allTerms;
    } // end getAllTerms

    /**
     * createDataBase is run on launch and checks to see if the database already exists. If it
     * does not it will create the new tables and setup the database for working with the app.
     */
    public void createDataBase() {
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
                "startedate DATE, " +
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
                "courseid INTEGER, " +
                "FOREIGN KEY (courseid) REFERENCES Courses(id))");
    } // end createTable
} // end DBHelper

