package wgu.c192.wguschedulerkbaldr2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.entities.Assessment;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM Assessment ORDER BY assessmentID ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM Assessment WHERE assessmentID = :assessmentID ORDER BY assessmentID ASC")
    List<Assessment> getAllAssociatedAssessments(int assessmentID);
}
