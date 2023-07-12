package wgu.c192.wguschedulerkbaldr2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import wgu.c192.wguschedulerkbaldr2.dao.TermDAO;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

//@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)
@Database(entities = {Term.class}, version = 1, exportSchema = false)
public abstract class SchedulerDatabaseBuilder extends RoomDatabase {
    //public abstract CourseDAO courseDAO();
    //public abstract AssessmentDAO assessmentDAO();

    public abstract TermDAO termDAO();
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
