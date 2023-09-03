package wgu.c192.wguschedulerkbaldr2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.entities.Term;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM TERM ORDER BY startDate DESC")
    List<Term> getAllTerms();

    @Query("SELECT * FROM TERM WHERE termID = :termID")
    Term getAssociatedTerm(int termID);

}
