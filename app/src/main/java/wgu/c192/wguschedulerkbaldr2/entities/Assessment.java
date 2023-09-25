package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ASSESSMENT")
public class Assessment {
    
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int assessmentID;
    private String assessmentTitle;
    private String startDate;
    private String endDate;
    private boolean assessmentType;
    private int courseID_f;
    
    public Assessment(String assessmentTitle, String startDate, String endDate, boolean assessmentType, int courseID_f) {
        this.assessmentTitle = assessmentTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assessmentType = assessmentType;
        this.courseID_f = courseID_f;
    }
    
    // Default Constructor
    public Assessment() {
    }
    
    public int getAssessmentID() {
        return assessmentID;
    }
    
    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }
    
    public String getAssessmentTitle() {
        return assessmentTitle;
    }
    
    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
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
    
    public boolean getAssessmentType() {
        return assessmentType;
    }
    
    public void setAssessmentType(boolean assessmentType) {
        this.assessmentType = assessmentType;
    }
    
    public int getCourseID_f() {
        return courseID_f;
    }
    
    public void setCourseID_f(int courseID_f) {
        this.courseID_f = courseID_f;
    }
    
    
}