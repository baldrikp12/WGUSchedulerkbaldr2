package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "course", foreignKeys = {@ForeignKey(entity = Term.class, parentColumns = "termID", childColumns = "courseID", onDelete = ForeignKey.CASCADE)})
public class Course {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int courseID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String instName;
    private String instNumber;
    private String instEmail;
    private int termID_f; //Foreign Key

    public Course(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Course() {

    }

    public String getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getInstNumber() {
        return instNumber;
    }

    public void setInstNumber(String instNumber) {
        this.instNumber = instNumber;
    }

    public String getInstEmail() {
        return instEmail;
    }

    public void setInstEmail(String instEmail) {
        this.instEmail = instEmail;
    }

    public int getTermID_f() {
        return termID_f;
    }

    public void setTermID_f(int termID_f) {
        this.termID_f = termID_f;
    }

    public int getTermID_F() {
        return termID_f;
    }

    public void setTermID_F(int termID) {
        this.termID_f = termID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
