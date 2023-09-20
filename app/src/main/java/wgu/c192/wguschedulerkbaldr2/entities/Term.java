package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "term")
public class Term {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int termID;
    private String termTitle;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;


    public Term(String termTitle, String startDate, String endDate) {
        this.termTitle = termTitle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term() {

    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return this.getTermTitle(); // assuming 'title' is the name of your term's title variable
    }
}
