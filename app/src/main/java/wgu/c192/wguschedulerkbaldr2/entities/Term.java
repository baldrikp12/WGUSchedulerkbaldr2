package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term")
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int termID;
    private String termName;

    public Term(int termID, String termName) {
        this.termID = termID;
        this.termName = termName;
    }

    public Term() {

    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }
}
