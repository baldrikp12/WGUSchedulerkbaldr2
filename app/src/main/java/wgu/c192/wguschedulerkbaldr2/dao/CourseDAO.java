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

    @Query("SELECT * FROM course ORDER BY courseID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course WHERE courseID = :courseID ORDER BY courseID ASC")
    List<Course> getAllAssociatedCourses(int courseID);
}
