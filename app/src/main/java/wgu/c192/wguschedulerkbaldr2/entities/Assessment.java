package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "assessment",
        foreignKeys = {
                @ForeignKey(
                        entity = Course.class,
                        parentColumns = "courseID",
                        childColumns = "assessmentID",
                        onDelete = ForeignKey.CASCADE,
                        deferred = true)})
public class Assessment {
    
    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private String assessmentTitle;
    private int courseID_F; //Foreign Key
    
    public Assessment(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }
    
    public Assessment() {
    
    }
    
    public int getCourseID_F() {
        return courseID_F;
    }
    
    public void setCourseID_F(int courseID_F) {
        this.courseID_F = courseID_F;
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
}
