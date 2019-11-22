package com.example.mainClasses;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 *  History.java
 *  This class defines how one 'History Object' looks like.
 *  These History Objects are displayed in the "History" tab of the BottomNavigationView.
 *  They represent all the past user answers during the straining mode.
 */

@Entity (tableName = "History_Table")
public class History {

    @PrimaryKey
    @NonNull
    private String question;
    private String correct_choice;
    private String user_choice;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_choice() {
        return correct_choice;
    }

    public void setCorrect_choice(String correct_choice) {
        this.correct_choice = correct_choice;
    }

    public String getUser_choice() {
        return user_choice;
    }

    public void setUser_choice(String user_choice) {
        this.user_choice = user_choice;
    }
}