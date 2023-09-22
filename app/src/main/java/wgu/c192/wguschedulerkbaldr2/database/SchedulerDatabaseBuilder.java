package wgu.c192.wguschedulerkbaldr2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import wgu.c192.wguschedulerkbaldr2.dao.AssessmentDAO;
import wgu.c192.wguschedulerkbaldr2.dao.CourseDAO;
import wgu.c192.wguschedulerkbaldr2.dao.TermDAO;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;
import wgu.c192.wguschedulerkbaldr2.entities.Course;
import wgu.c192.wguschedulerkbaldr2.entities.Term;
import wgu.c192.wguschedulerkbaldr2.util.Converters;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 2, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class SchedulerDatabaseBuilder extends RoomDatabase {
    //public abstract AssessmentDAO assessmentDAO();

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    private static volatile SchedulerDatabaseBuilder INSTANCE;

    static SchedulerDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SchedulerDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SchedulerDatabaseBuilder.class, "MySchedulerDatabase.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }


}
