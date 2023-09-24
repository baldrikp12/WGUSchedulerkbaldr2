package wgu.c192.wguschedulerkbaldr2.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment")
public class Assessment {
    
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int assessmentID;
    private String assessmentTitle;
    private int courseID_f;
    
    // Additional Fields
    private String dueDate;
    private String type;
    private String description;
    
    // Fields for assessment type
    private boolean isObjective; // True for objective, False for performance
    
    public Assessment(String assessmentTitle, int courseID_f) {
        this.assessmentTitle = assessmentTitle;
        this.courseID_f = courseID_f;
    }
    
    // Default Constructor
    public Assessment() {
        // Set default values if needed
        this.courseID_f = 0; // Default value
    }
    
    public int getCourseID_F() {
        return courseID_f;
    }
    
    public void setCourseID_F(int courseID_F) {
        this.courseID_f = courseID_F;
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
    
    public String getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isObjective() {
        return isObjective;
    }
    
    public void setObjective(boolean objective) {
        isObjective = objective;
    }
}