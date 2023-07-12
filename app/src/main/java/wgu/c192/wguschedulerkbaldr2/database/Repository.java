package wgu.c192.wguschedulerkbaldr2.database;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wgu.c192.wguschedulerkbaldr2.dao.AssessmentDAO;
import wgu.c192.wguschedulerkbaldr2.dao.CourseDAO;
import wgu.c192.wguschedulerkbaldr2.dao.TermDAO;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class Repository {

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private AssessmentDAO assessmentDAO;
    private List<Term> mAllTerms;
    //private List<Course> mAllCourses;
    //private List<Assessment> mAllAssessments;

    public Repository(Application application) {
        SchedulerDatabaseBuilder db = SchedulerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        //mCourseDAO = db.courseDAO();
    }

    public List<Term> getAllTerms() {
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }

    public void insert(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    //same thing for course and assessment


}
