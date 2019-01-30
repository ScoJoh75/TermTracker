package com.example.myrecylverviewapplication;

import java.sql.Date;

public class Term {
    private long id;
    private String termName;
    private Date startDate;
    private Date endDate;

    public Term(long id, String termName, Date startDate, Date endDate) {
        this.id = id;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    } // end Full Constructor

    public Term (String termName, Date startDate, Date endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    } // end Constructor

    public Term () {}

    public long getId() {
        return id;
    } // end getId

    public void setId(long id) {
        this.id = id;
    } // end setId

    public String getTermName() {
        return termName;
    } // end getTermName

    public void setTermName(String termName) {
        this.termName = termName;
    } // end setTermName

    public Date getStartDate() {
        return startDate;
    } // end getStartDate

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    } // end setStartDate

    public Date getEndDate() {
        return endDate;
    } // end getEndDate

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    } // end setEndDate
} // end Term

