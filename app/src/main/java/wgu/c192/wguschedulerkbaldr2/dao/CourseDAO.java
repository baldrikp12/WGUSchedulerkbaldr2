package wgu.c192.wguschedulerkbaldr2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.entities.Course;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM COURSE ORDER BY CourseID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM COURSE WHERE courseID = :courseID")
    Course getAssociatedCourse(int courseID);

    @Query("SELECT * FROM COURSE WHERE termID_f = :termID")
    List<Course> getCoursesByTermID(int termID);
}
