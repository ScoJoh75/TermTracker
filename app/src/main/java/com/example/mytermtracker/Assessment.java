package com.example.mytermtracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Assessment implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    private Long id;
    private String assessmentName;
    private String assessmentType;
    private Date goalDate;
    private Long courseId;

    Assessment(Long id, String assessmentName, String assessmentType, Date goalDate, Long courseId) {
        this.id = id;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.goalDate = goalDate;
        this.courseId = courseId;
    } // end Full Constructor

    Assessment(String assessmentName, String assessmentType, Date goalDate, Long courseId) {
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.goalDate = goalDate;
        this.courseId = courseId;
    } // end Full Constructor

    public Long getId() {
        return id;
    } // end getId

    public void setId(Long id) {
        this.id = id;
    } // end setId

    String getAssessmentName() {
        return assessmentName;
    } // end getAssessmentName

    void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    } // end setAssessmentName

    String getAssessmentType() {
        return assessmentType;
    } // end getAssessmentType

    void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    } // end setAssessmentType

    Date getGoalDate() {
        return goalDate;
    } // end getGoalDate

    void setGoalDate(Date goalDate) {
        this.goalDate = goalDate;
    } // end setGoalDate

    Long getCourseId() {
        return courseId;
    } // end getCourseId

    private Assessment(Parcel in) {
        this.id = in.readLong();
        this.assessmentName = in.readString();
        this.assessmentType = in.readString();
        this.goalDate = new Date(in.readLong());
        this.courseId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.assessmentName);
        dest.writeString(this.assessmentType);
        dest.writeLong(this.goalDate.getTime());
        dest.writeLong(this.courseId);
    }
} // end Assessment
