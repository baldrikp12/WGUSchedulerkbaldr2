package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "COURSE",
        foreignKeys = {
                @ForeignKey(
                        entity = Term.class,
                        parentColumns = "TermID",
                        childColumns = "CourseID",
                        onDelete = ForeignKey.CASCADE,
                        deferred = true)})
public class Course {
    
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CourseID")
    @NonNull
    private int courseID = 0;
    
    @ColumnInfo(name = "CourseTitle")
    @NonNull
    private String courseTitle = "";
    
    @ColumnInfo(name = "CourseStart")
    @Nullable
    private String courseStart;
    
    @ColumnInfo(name = "CourseEnd")
    @Nullable
    private String courseEnd;
    
    @ColumnInfo(name = "MentName")
    @Nullable
    private String mentName;
    
    @ColumnInfo(name = "MentNumber")
    @Nullable
    private String mentNumber;
    
    @ColumnInfo(name = "MentEmail")
    @Nullable
    private String mentEmail;
    
    private int termID_f; // Foreign key
    
    public Course(String courseTitle, String startDate, String endDate, int termID, String mentName, String mentNumber, String mentEmail) {
        this.courseTitle = courseTitle;
        this.courseStart = startDate;
        this.courseEnd = endDate;
        this.termID_f = termID;
        this.mentName = mentName;
        this.mentNumber = mentNumber;
        this.mentEmail = mentEmail;
    }
    
    public Course(String courseTitle, String startDate, String endDate, String mentName, String mentNumber, String mentEmail) {
        this.courseTitle = courseTitle;
        this.courseStart = startDate;
        this.courseEnd = endDate;
        this.mentName = mentName;
        this.mentNumber = mentNumber;
        this.mentEmail = mentEmail;
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
    
    public String getMentName() {
        return mentName;
    }
    
    public void setMentName(String mentName) {
        this.mentName = mentName;
    }
    
    public String getMentNumber() {
        return mentNumber;
    }
    
    public void setMentNumber(String mentNumber) {
        this.mentNumber = mentNumber;
    }
    
    public String getMentEmail() {
        return mentEmail;
    }
    
    public void setMentEmail(String mentEmail) {
        this.mentEmail = mentEmail;
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
