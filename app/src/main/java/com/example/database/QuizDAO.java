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

    // Query to insert a new row to the 'Quiz_table':
    @Insert(onConflict = IGNORE)
    void insertQuiz(Quiz q);                                                                        // Set the 3 default entries (passed = false) when the database is created.

    // Query to get whether or not a certain quiz has been passed yet:
    @Query("SELECT Passed FROM Quiz_Table WHERE QuizID = :quizID")
    int getPassedStatusByID(int quizID);

    // Query to set whether or not a certain quiz has been passed:
    @Query ("UPDATE Quiz_Table SET Passed = 1 WHERE QuizID = :quizID")
    void setPassedStatusByID(int quizID);
}