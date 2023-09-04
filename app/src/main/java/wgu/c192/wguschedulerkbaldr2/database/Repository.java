package wgu.c192.wguschedulerkbaldr2.database;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wgu.c192.wguschedulerkbaldr2.dao.AssessmentDAO;
import wgu.c192.wguschedulerkbaldr2.dao.CourseDAO;
import wgu.c192.wguschedulerkbaldr2.dao.TermDAO;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;
import wgu.c192.wguschedulerkbaldr2.entities.Course;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class Repository {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private List<Term> mAllTerms;
    private Term mTerm;
    private List<Course> mAllCourses;

    private AssessmentDAO mAssessmentDAO;

    private List<Assessment> mAllAssessments;

    public Repository(Application application) {
        SchedulerDatabaseBuilder db = SchedulerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
    }

    public List<Term> getAllTerms() {
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(500);
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Term getAssociatedTerm(int termId) {
        databaseExecutor.execute(() -> {
            mTerm = mTermDAO.getAssociatedTerm(termId);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mTerm;
    }


    public List<Course> getAllCourses() {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public void insert(Course course) {
        databaseExecutor.execute(() -> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Course course) {
        databaseExecutor.execute(() -> {
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getCoursesByTermID(int termID) {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getCoursesByTermID(termID);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public List<Assessment> getAllAssessments() {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public void insert(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
