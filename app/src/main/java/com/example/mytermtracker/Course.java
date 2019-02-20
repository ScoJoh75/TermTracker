package com.example.mytermtracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Course implements Parcelable {

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    private Long id;
    private String courseTitle;
    private Date startDate;
    private Date endDate;
    private String status;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private String notes;
    private Long termId;

    Course(Long id, String courseTitle, Date startDate, Date endDate, String status, String mentorName, String mentorPhone, String mentorEmail, String notes, Long termId) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.notes = notes;
        this.termId = termId;
    } // end Full Constructor

    public Course(String courseTitle, Date startDate, Date endDate, String status, String mentorName, String mentorPhone, String mentorEmail, String notes, Long termId) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.notes = notes;
        this.termId = termId;
    } // end Full Constructor

    public Long getId() {
        return id;
    } // end getId

    public void setId(Long id) {
        this.id = id;
    } // end setId

    String getCourseTitle() {
        return courseTitle;
    } // end getCourseTitle

    void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    } // end setCourseTitle

    Date getStartDate() {
        return startDate;
    } // end getStartDate

    void setStartDate(Date startDate) {
        this.startDate = startDate;
    } // end setStartDate

    Date getEndDate() {
        return endDate;
    } // end getEndDate

    void setEndDate(Date endDate) {
        this.endDate = endDate;
    } // end setEndDate

    String getStatus() {
        return status;
    } // end getStatus

    void setStatus(String status) {
        this.status = status;
    } // end setStatus

    String getMentorName() {
        return mentorName;
    } // end getMentorName

    void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    } // end setMentorName

    String getMentorPhone() {
        return mentorPhone;
    } // end getMentorPhone

    void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    } // end setMentorPhone

    String getMentorEmail() {
        return mentorEmail;
    } // end getMentorEmail

    void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    } // end setMentorEmail

    String getNotes() {
        return notes;
    } // end getNotes

    void setNotes(String notes) {
        this.notes = notes;
    } // end setNotes

    Long getTermId() {
        return termId;
    } // end getTermId

    private Course(Parcel in) {
        this.id = in.readLong();
        this.courseTitle = in.readString();
        this.startDate = new Date(in.readLong());
        this.endDate = new Date(in.readLong());
        this.status = in.readString();
        this.mentorName = in.readString();
        this.mentorPhone = in.readString();
        this.mentorEmail = in.readString();
        this.notes = in.readString();
        this.termId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.courseTitle);
        dest.writeLong(this.startDate.getTime());
        dest.writeLong(this.endDate.getTime());
        dest.writeString(this.status);
        dest.writeString(this.mentorName);
        dest.writeString(this.mentorPhone);
        dest.writeString(this.mentorEmail);
        dest.writeString(this.notes);
        dest.writeLong(this.termId);
    }
} // end Courses
