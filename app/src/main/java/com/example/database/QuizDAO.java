package com.example.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.mainClasses.Quiz;
import static androidx.room.OnConflictStrategy.IGNORE;

/*
 *  QuizDAO.java
 *  Room (DAO - Data Access Object)
 *  Here you can define all the Database queries in form of Java methods.
 */

@Dao
public interface QuizDAO {

    // Set the 3 default entries (passed = false) when the database is created.
    @Insert(onConflict = IGNORE)                                                                    // When there is already an entry (the PrimaryKey ID = 0), ignore it:
    void insertQuiz(Quiz q);                                                                        // Insert inserts a whole table entry. (one row)

    // Query to get if a certain quiz has been passed yet:
    @Query("SELECT Passed FROM Quiz_Table WHERE QuizID = :quizID")
    boolean getPassedStatusByID(int quizID);

    // Query to set if a certain quiz has been passed:
    @Query ("UPDATE Quiz_Table SET Passed = 1 WHERE QuizID = :quizID")
    void setPassedStatusByID(int quizID);
}