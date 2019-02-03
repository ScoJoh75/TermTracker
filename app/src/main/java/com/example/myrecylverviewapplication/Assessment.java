package com.example.myrecylverviewapplication;

public class Assessment {
    private int id;
    private String assessmentName;
    private String assessmentType;
    private int courseId;

    public Assessment(int id, String assessmentName, String assessmentType, int courseId) {
        this.id = id;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    } // end Full Constructor

    public Assessment(String assessmentName, String assessmentType, int courseId) {
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    } // end Full Constructor

    public int getId() {
        return id;
    } // end getId

    public void setId(int id) {
        this.id = id;
    } // end setId

    public String getAssessmentName() {
        return assessmentName;
    } // end getAssessmentName

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    } // end setAssessmentName

    public String getAssessmentType() {
        return assessmentType;
    } // end getAssessmentType

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    } // setAssessmentType

    public int getCourseId() {
        return courseId;
    } // end getCourseId

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    } // end setCourseId
} // end Assessment
