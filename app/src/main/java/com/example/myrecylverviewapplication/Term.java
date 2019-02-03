package com.example.myrecylverviewapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Term implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Term createFromParcel(Parcel in) {
            return new Term(in);
        }

        public Term[] newArray(int size) {
            return new Term[size];
        }
    };

    private long id;
    private String termName;
    private Date startDate;
    private Date endDate;

    Term(long id, String termName, Date startDate, Date endDate) {
        this.id = id;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    } // end Full Constructor

    Term(String termName, Date startDate, Date endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    } // end Constructor

    long getId() {
        return id;
    } // end getId

    void setId(long id) {
        this.id = id;
    } // end setId

    String getTermName() {
        return termName;
    } // end getTermName

    void setTermName(String termName) {
        this.termName = termName;
    } // end setTermName

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

    private Term(Parcel in) {
        this.id = in.readLong();
        this.termName = in.readString();
        this.startDate = new Date(in.readLong());
        this.endDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.termName);
        dest.writeLong(this.startDate.getTime());
        dest.writeLong(this.endDate.getTime());
    }
} // end Term
