package com.example.mainClasses;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 *  Quiz.java
 *  This class defines how one 'Quiz Object' looks like.
 *  It is used to save, if a certain quiz has already been passed or not.
 */

@Entity (tableName = "Quiz_Table")
public class Quiz {

    @PrimaryKey
    @NonNull
    private int QuizID;
    private boolean Passed;

    @NonNull
    public int getQuizID() {
        return QuizID;
    }

    public void setQuizID(@NonNull int quizID) {
        QuizID = quizID;
    }

    public boolean isPassed() {
        return Passed;
    }

    public void setPassed(boolean passed) {
        Passed = passed;
    }
}